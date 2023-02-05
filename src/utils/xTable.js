import { reactive } from 'vue';
import { getArrayKey } from '/@/utils/common';
import Sortable from 'sortablejs';
import { findIndexRow } from '/@/components/v1/table';
import { ElNotification } from 'element-plus';
import { onBeforeRouteUpdate, useRoute } from 'vue-router';
import { isUndefined } from 'lodash-es';
export default class xTable {
  api;
  table = reactive({
    ref: undefined,
    pk: 'id',
    data: [],
    remark: null,
    loading: false,
    selection: [],
    column: [],
    total: 0,
    filter: {},
    dragSortLimitField: 'pid',
    acceptQuery: true,
    showComSearch: false,
    dblClickNotEditColumn: [undefined],
    expandAll: false,
    extend: {},
  });
  form = reactive({
    ref: undefined,
    labelWidth: 160,
    operate: '',
    operateIds: [],
    items: {},
    submitLoading: false,
    defaultItems: {},
    loading: false,
    extend: {},
  });
  before;
  after;
  comSearch = reactive({
    form: {},
    fieldData: new Map(),
  });
  constructor(api, table, form = {}, before = {}, after = {}) {
    this.api = api;
    this.form = Object.assign(this.form, form);
    this.table = Object.assign(this.table, table);
    this.before = before;
    this.after = after;
    const route = useRoute();
    this.initComSearch(!isUndefined(route) ? route.query : {});
  }
  runBefore(funName, args = {}) {
    if (this.before && this.before[funName] && typeof this.before[funName] == 'function') {
      return this.before[funName]({ ...args }) === false ? false : true;
    }
    return true;
  }
  runAfter(funName, args = {}) {
    if (this.after && this.after[funName] && typeof this.after[funName] == 'function') {
      return this.after[funName]({ ...args }) === false ? false : true;
    }
    return true;
  }
  getIndex = () => {
    if (this.runBefore('getIndex') === false) return;
    this.table.loading = true;
    return this.api
      .index(this.table.filter)
      .then((res) => {
        this.table.data = res.data.list;
        this.table.total = res.data?.totalCount;
        this.runAfter('getIndex', { res });
      })
      .finally(() => {
        this.table.loading = false;
      });
  };
  postDel = (ids) => {
    if (this.runBefore('postDel', { ids }) === false) return;
    this.api.del(ids).then((res) => {
      this.onTableHeaderAction('refresh', {});
      this.runAfter('postDel', { res });
    });
  };
  requestEdit = (id) => {
    if (this.runBefore('requestEdit', { id }) === false) return;
    this.form.loading = true;
    this.form.items = {};
    return this.api
      .edit({
        [this.table.pk]: id,
      })
      .then((res) => {
        this.form.items = res.data;
        this.runAfter('requestEdit', { res });
      })
      .catch((err) => {
        this.toggleForm();
        this.runAfter('requestEdit', { err });
      })
      .finally(() => {
        this.form.loading = false;
      });
  };
  onTableDblclick = (row, column) => {
    if (!this.table.dblClickNotEditColumn.includes('all') && !this.table.dblClickNotEditColumn.includes(column.property)) {
      if (this.runBefore('onTableDblclick', { row, column }) === false) return;
      this.toggleForm('edit', [row[this.table.pk]]);
      this.runAfter('onTableDblclick', { row, column });
    }
  };
  toggleForm = (operate = '', operateIds = []) => {
    if (this.runBefore('toggleForm', { operate, operateIds }) === false) return;
    if (this.form.ref) {
      this.form.ref.resetFields();
    }
    if (operate == 'edit') {
      if (!operateIds.length) {
        return false;
      }
      this.requestEdit(operateIds[0]);
    } else if (operate == 'add') {
      this.form.items = Object.assign({}, this.form.defaultItems);
    }
    this.form.operate = operate;
    this.form.operateIds = operateIds;
    this.runAfter('toggleForm', { operate, operateIds });
  };
  onSubmit = (formEl = undefined) => {
    if (this.runBefore('onSubmit', { formEl: formEl, operate: this.form.operate, items: this.form.items }) === false) return;
    Object.keys(this.form.items).forEach((item) => {
      if (this.form.items[item] === null) delete this.form.items[item];
    });
    console.log('xt', this.form);
    const submitCallback = () => {
      this.form.submitLoading = true;
      this.api
        .postData(this.form.operate, this.form.items)
        .then((res) => {
          this.onTableHeaderAction('refresh', {});
          this.form.operateIds?.shift();
          if (this.form.operateIds.length > 0) {
            this.toggleForm('edit', this.form.operateIds);
          } else {
            this.toggleForm();
          }
          this.runAfter('onSubmit', { res });
        })
        .finally(() => {
          this.form.submitLoading = false;
        });
    };
    if (formEl) {
      this.form.ref = formEl;
      formEl.validate((valid) => {
        if (valid) {
          submitCallback();
        }
      });
    } else {
      submitCallback();
    }
  };
  getSelectionIds() {
    const ids = [];
    this.table.selection?.forEach((item) => {
      ids.push(item[this.table.pk]);
    });
    return ids;
  }
  onTableAction = (event, data) => {
    if (this.runBefore('onTableAction', { event, data }) === false) return;
    const actionFun = new Map([
      [
        'selection-change',
        () => {
          this.table.selection = data;
        },
      ],
      [
        'page-size-change',
        () => {
          this.table.filter.limit = data.size;
          this.getIndex();
        },
      ],
      [
        'current-page-change',
        () => {
          this.table.filter.page = data.page;
          this.getIndex();
        },
      ],
      [
        'sort-change',
        () => {
          let newOrder;
          if (data.prop && data.order) {
            newOrder = data.prop + ',' + data.order;
          }
          if (newOrder != this.table.filter.order) {
            this.table.filter.order = newOrder;
            this.getIndex();
          }
        },
      ],
      [
        'edit',
        () => {
          this.toggleForm('edit', [data.row[this.table.pk]]);
        },
      ],
      [
        'delete',
        () => {
          this.postDel([data.row[this.table.pk]]);
        },
      ],
      [
        'field-change',
        () => {
          if (data.field.render == 'switch') {
            if (!data.field || !data.field.prop) return;
            data.row.loading = true;
            this.api
              .postData('edit', { [this.table.pk]: data.row[this.table.pk], [data.field.prop]: data.value })
              .then(() => {
                data.row.loading = false;
                data.row[data.field.prop] = data.value;
              })
              .catch(() => {
                data.row.loading = false;
              });
          }
        },
      ],
      [
        'com-search',
        () => {
          this.table.filter.search = data;
          this.getIndex();
        },
      ],
      [
        'default',
        () => {
          console.warn('No action defined');
        },
      ],
    ]);
    const action = actionFun.get(event) || actionFun.get('default');
    action.call(this);
    return this.runAfter('onTableAction', { event, data });
  };
  onTableHeaderAction = (event, data) => {
    if (this.runBefore('onTableHeaderAction', { event, data }) === false) return;
    const actionFun = new Map([
      [
        'refresh',
        () => {
          this.table.data = [];
          this.getIndex();
        },
      ],
      [
        'add',
        () => {
          this.toggleForm('add');
        },
      ],
      [
        'edit',
        () => {
          this.toggleForm('edit', this.getSelectionIds());
        },
      ],
      [
        'delete',
        () => {
          this.postDel(this.getSelectionIds());
        },
      ],
      [
        'unfold',
        () => {
          if (!this.table.ref) {
            console.warn('Collapse/expand failed because table ref is not defined. Please assign table ref when onMounted');
            return;
          }
          this.table.expandAll = data.unfold;
          this.table.ref.unFoldAll(data.unfold);
        },
      ],
      [
        'quick-search',
        () => {
          this.table.filter.quick_search = data.keyword;
          this.getIndex();
        },
      ],
      [
        'change-show-column',
        () => {
          const columnKey = getArrayKey(this.table.column, 'prop', data.field);
          this.table.column[columnKey].show = data.value;
        },
      ],
      [
        'default',
        () => {
          console.warn('No action defined');
        },
      ],
    ]);
    const action = actionFun.get(event) || actionFun.get('default');
    action.call(this);
    return this.runAfter('onTableHeaderAction', { event, data });
  };
  initSort = () => {
    if (this.table.defaultOrder && this.table.defaultOrder.prop) {
      if (!this.table.ref) {
        console.warn('Failed to initialize default sorting because table ref is not defined. Please assign table ref when onMounted');
        return;
      }
      const defaultOrder = this.table.defaultOrder.prop + ',' + this.table.defaultOrder.order;
      if (this.table.filter && this.table.filter.order != defaultOrder) {
        this.table.filter.order = defaultOrder;
        this.table.ref.getRef()?.sort(this.table.defaultOrder.prop, this.table.defaultOrder.order == 'desc' ? 'descending' : 'ascending');
      }
    }
  };
  dragSort = () => {
    const buttonsKey = getArrayKey(this.table.column, 'render', 'buttons');
    const moveButton = getArrayKey(this.table.column[buttonsKey]?.buttons, 'render', 'moveButton');
    if (moveButton === false) {
      return;
    }
    if (!this.table.ref) {
      console.warn('Failed to initialize drag sort because table ref is not defined. Please assign table ref when onMounted');
      return;
    }
    const el = this.table.ref.getRef().$el.querySelector('.el-table__body-wrapper .el-table__body tbody');
    Sortable.create(el, {
      animation: 200,
      handle: '.table-row-weigh-sort',
      ghostClass: 'ba-table-row',
      onStart: () => {
        this.table.column[buttonsKey].buttons?.forEach((item) => {
          item.disabledTip = true;
        });
      },
      onEnd: (evt) => {
        this.table.column[buttonsKey].buttons?.forEach((item) => {
          item.disabledTip = false;
        });
        const moveRow = findIndexRow(this.table.data, evt.oldIndex);
        const replaceRow = findIndexRow(this.table.data, evt.newIndex);
        if (this.table.dragSortLimitField && moveRow[this.table.dragSortLimitField] != replaceRow[this.table.dragSortLimitField]) {
          this.onTableHeaderAction('refresh', {});
          ElNotification({
            type: 'error',
            message: 'utils.The moving position is beyond the movable range!',
          });
          return;
        }
      },
    });
  };
  mount = () => {
    if (this.runBefore('mount') === false) return;
    onBeforeRouteUpdate((to) => {
      this.initComSearch(to.query);
      this.getIndex();
    });
  };
  initComSearch = (query = {}) => {
    const form = {};
    const field = this.table.column;
    if (field.length <= 0) {
      return;
    }
    for (const key in field) {
      if (field[key].operator === false) {
        continue;
      }
      const prop = field[key].prop;
      if (typeof field[key].operator == 'undefined') {
        field[key].operator = '=';
      }
      if (prop) {
        if (field[key].operator == 'RANGE' || field[key].operator == 'NOT RANGE') {
          form[prop] = '';
          form[prop + '-start'] = '';
          form[prop + '-end'] = '';
        } else if (field[key].operator == 'NULL' || field[key].operator == 'NOT NULL') {
          form[prop] = false;
        } else {
          form[prop] = '';
        }
        if (this.table.acceptQuery && typeof query[prop] != 'undefined') {
          const queryProp = query[prop] ?? '';
          if (field[key].operator == 'RANGE' || field[key].operator == 'NOT RANGE') {
            const range = queryProp.split(',');
            if (field[key].render == 'datetime') {
              if (range && range.length >= 2) {
                form[prop + '-default'] = [new Date(range[0]), new Date(range[1])];
              }
            } else {
              form[prop + '-start'] = range[0] ?? '';
              form[prop + '-end'] = range[1] ?? '';
            }
          } else if (field[key].operator == 'NULL' || field[key].operator == 'NOT NULL') {
            form[prop] = queryProp ? true : false;
          } else if (field[key].render == 'datetime') {
            form[prop + '-default'] = new Date(queryProp);
          } else {
            form[prop] = queryProp;
          }
        }
        this.comSearch.fieldData.set(prop, {
          operator: field[key].operator,
          render: field[key].render,
          comSearchRender: field[key].comSearchRender,
        });
      }
    }
    if (this.table.acceptQuery) {
      const comSearchData = [];
      for (const key in query) {
        const fieldDataTemp = this.comSearch.fieldData.get(key);
        if (fieldDataTemp) {
          comSearchData.push({
            field: key,
            val: query[key],
            operator: fieldDataTemp.operator,
            render: fieldDataTemp.render,
          });
        }
      }
      this.table.filter.search = comSearchData;
    }
    this.comSearch.form = Object.assign(this.comSearch.form, form);
  };
}
//# sourceMappingURL=xTable.js.map
