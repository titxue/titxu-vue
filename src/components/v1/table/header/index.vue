<template>
  <!-- 通用搜索 -->
  <transition name="el-zoom-in-bottom" mode="out-in">
    <ComSearch v-show="props.buttons.includes('comSearch') && xTable.table.showComSearch" />
  </transition>

  <!-- 操作按钮组 -->
  <div v-bind="$attrs" class="table-header ba-scroll-style">
    <el-tooltip v-if="props.buttons.includes('refresh')" content="刷新" placement="top">
      <el-button @click="onAction('refresh', { loading: true })" color="#40485b" class="table-header-operate" type="info">
        <Icon name="fa fa-refresh" />
      </el-button>
    </el-tooltip>
    <el-tooltip v-if="props.buttons.includes('add')" content="新建" placement="top">
      <el-button @click="onAction('add')" class="table-header-operate" type="primary">
        <Icon name="fa fa-plus" />
        <span class="table-header-operate-text">新建</span>
      </el-button>
    </el-tooltip>
    <el-tooltip v-if="props.buttons.includes('edit')" content="编辑选定的行" placement="top">
      <el-button @click="onAction('edit')" :disabled="!enableBatchOpt" class="table-header-operate" type="primary">
        <Icon name="fa fa-pencil" />
        <span class="table-header-operate-text">编辑</span>
      </el-button>
    </el-tooltip>
    <el-popconfirm
      v-if="props.buttons.includes('delete')"
      @confirm="onAction('delete')"
      confirm-button-text="删除"
      cancel-button-text="取消"
      confirmButtonType="danger"
      title="确定删除选中的记录吗？"
      :disabled="!enableBatchOpt"
    >
      <template #reference>
        <div class="mlr-12">
          <el-tooltip content="删除选定的行" placement="top">
            <el-button :disabled="!enableBatchOpt" class="table-header-operate" type="danger">
              <Icon name="fa fa-trash" />
              <span class="table-header-operate-text">删除</span>
            </el-button>
          </el-tooltip>
        </div>
      </template>
    </el-popconfirm>
    <el-tooltip v-if="props.buttons.includes('unfold')" :content="xTable.table.expandAll ? '收缩' : '展开'" placement="top">
      <el-button
        @click="xTable.onTableHeaderAction('unfold', { unfold: !xTable.table.expandAll })"
        class="table-header-operate"
        :type="xTable.table.expandAll ? 'danger' : 'warning'"
      >
        <span class="table-header-operate-text">{{ xTable.table.expandAll ? '收缩所有' : '展开所有' }}</span>
      </el-button>
    </el-tooltip>

    <!-- slot -->
    <slot></slot>

    <!-- 右侧搜索框和工具按钮 -->
    <div class="table-search">
      <el-input
        v-if="props.buttons.includes('quickSearch')"
        v-model="state.quickSearch"
        class="xs-hidden quick-search"
        @input="debounce(onSearchInput, 500)()"
        :placeholder="quickSearchPlaceholder ? quickSearchPlaceholder : '搜索'"
      />
      <div class="table-search-button-group" v-if="props.buttons.includes('columnDisplay') || props.buttons.includes('comSearch')">
        <el-dropdown v-if="props.buttons.includes('columnDisplay')" :max-height="380" :hide-on-click="false">
          <el-button
            class="table-search-button-item"
            :class="props.buttons.includes('comSearch') ? 'right-border' : ''"
            color="#dcdfe6"
            plain
          >
            <Icon size="14" name="el-icon-Grid" />
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item v-for="(item, idx) in columnDisplay" :key="idx">
                <el-checkbox
                  v-if="item.prop"
                  @change="onChangeShowColumn($event, item.prop!)"
                  :checked="!item.show"
                  :model-value="item.show"
                  size="small"
                  :label="item.label"
                />
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <el-tooltip
          v-if="props.buttons.includes('comSearch')"
          :disabled="xTable.table.showComSearch"
          content="t('Expand generic search')"
          placement="top"
        >
          <el-button
            class="table-search-button-item"
            @click="xTable.table.showComSearch = !xTable.table.showComSearch"
            color="#dcdfe6"
            plain
          >
            <Icon size="14" name="el-icon-Search" />
          </el-button>
        </el-tooltip>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { reactive, computed, inject } from 'vue';
  import { debounce } from '/@/utils/common';
  import type xTableClass from '/@/utils/xTable';
  import ComSearch from '/@/components/v1/table/comSearch/index.vue';
  import Icon from '/@/components/v1/icon/index.vue';

  const xTable = inject('xTable') as xTableClass<anyObj>;

  interface Props {
    buttons: HeaderOptButton[];
    quickSearchPlaceholder?: string;
  }
  const props = withDefaults(defineProps<Props>(), {
    buttons: () => {
      return ['refresh', 'add', 'edit', 'delete'];
    },
    quickSearchPlaceholder: '',
  });

  const state = reactive({
    quickSearch: '',
  });

  const columnDisplay = computed(() => {
    let columnDisplayArr: TableColumn[] = [];
    for (let item of xTable.table.column) {
      item.type === 'selection' || item.render === 'buttons' || item.enableColumnDisplayControl === false
        ? ''
        : columnDisplayArr.push(item);
    }
    return columnDisplayArr;
  });

  const enableBatchOpt = computed(() => xTable.table.selection!.length > 0);

  const onAction = (event: string, data: anyObj = {}) => {
    xTable.onTableHeaderAction(event, data);
  };

  const onSearchInput = () => {
    xTable.onTableHeaderAction('quick-search', { keyword: state.quickSearch });
  };

  const onChangeShowColumn = (value: string | number | boolean, field: string) => {
    xTable.onTableHeaderAction('change-show-column', { field: field, value: value });
  };
</script>

<style scoped lang="less">
  .table-header {
    position: relative;
    overflow-y: scroll;
    box-sizing: border-box;
    display: flex;
    align-items: center;
    width: 100%;
    max-width: 100%;
    background-color: #ffffff;
    border: 1px solid #f6f6f6;
    border-bottom: none;
    padding: 13px 15px;
    font-size: 14px;

    .table-header-operate-text {
      margin-left: 6px;
    }
  }

  .mlr-12 {
    margin-left: 12px;
  }

  .mlr-12 + .el-button {
    margin-left: 12px;
  }

  .table-search {
    display: flex;
    margin-left: auto;

    .quick-search {
      width: auto;
    }
  }

  .table-search-button-group {
    display: flex;
    margin-left: 12px;
    border: 1px solid var(--el-border-color);
    border-radius: var(--el-border-radius-base);
    overflow: hidden;

    button:focus,
    button:active {
      background-color: var(--ba-bg-color-overlay);
    }

    button:hover {
      background-color: var(--el-color-info-light-7);
    }

    .table-search-button-item {
      border: none;
      border-radius: 0;
    }

    .el-button + .el-button {
      margin: 0;
    }

    .right-border {
      border-right: 1px solid var(--el-border-color);
    }
  }

  html.dark {
    .table-search-button-group {
      button:focus,
      button:active {
        background-color: var(--el-color-info-dark-2);
      }

      button:hover {
        background-color: var(--el-color-info-light-7);
      }

      button {
        background-color: #898a8d;

        el-icon {
          color: white !important;
        }
      }
    }
  }
</style>
