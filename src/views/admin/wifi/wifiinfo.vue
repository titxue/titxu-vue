<template>
  <div class="default-main ba-table-box">
    <el-alert class="ba-table-alert" v-if="xTable.table.remark" :title="xTable.table.remark" type="info" show-icon />

    <!-- 表格顶部菜单 -->
    <TableHeader
      :buttons="['refresh', 'add', 'edit', 'delete', 'quickSearch', 'comSearch', 'columnDisplay']"
      quick-search-placeholder="通过角色名称模糊搜索"
    />

    <!-- 表格 -->
    <!-- 要使用`el-table`组件原有的属性，直接加在Table标签上即可 -->
    <Table ref="tableRef" />

    <!-- 表单 -->
    <WifiInfoForm ref="formRef" />
  </div>
</template>

<script setup lang="ts">
  import { cloneDeep } from 'lodash-es';
  import xTableClass from '/@/utils/xTable';
  import { ADMIN_URL, xTableApi } from '/@/api/common';
  import { defaultOptButtons } from '/@/components/v1/table/index';
  import WifiInfoForm from './components/wifiInfoForm.vue';
  import Table from '/@/components/v1/table/index.vue';
  import TableHeader from '/@/components/v1/table/header/index.vue';
  import { WifiInfoType } from '/@/api/wifiinfo/types';
  import { TableColumnCtx } from 'element-plus';
  import { useWifiStore } from '/@/store';

  const wifiStore = useWifiStore();
  const { setMerchantsvList } = wifiStore;
  const { merchantList } = toRefs(wifiStore);
  // 根据商家id获取商家名称
  function getMerchantNameById(id: string) {
    const merchant = merchantList.value.find((merchant) => merchant.id === id);
    return merchant ? merchant.name : null;
  }
  const formRef = ref();
  const tableRef = ref();
  const xTable: xTableClass<WifiInfoType> = new xTableClass(
    new xTableApi(ADMIN_URL.wifiInfo),
    {
      dblClickNotEditColumn: [undefined],
      column: [
        { type: 'selection', align: 'center', operator: false },
        { label: 'WiFi名称', prop: 'wifiName', align: 'center', operator: 'LIKE' },
        { label: 'WiFi密码', prop: 'wifiPassword', align: 'center', width: '170' },
        {
          label: '商家',
          prop: 'merchantId',
          align: 'center',
          operator: 'LIKE',
          render: 'tag',
          renderFormatter: (_row: TableRow, _field: TableColumn, value: any, _column: TableColumnCtx<TableRow>, _index: number) => {
            // console.log(merchantList.value);

            return getMerchantNameById(value);
          },
        },
        {
          label: '状态',
          prop: 'status',
          align: 'center',
          render: 'tag',
          custom: { '0': 'success', '1': 'danger' },
          replaceValue: { '0': '启用', '1': '禁用' },
        },
        { label: '更新时间', prop: 'updatedTime', align: 'center', width: '160', render: 'datetime' },
        { label: '创建时间', prop: 'createdTime', align: 'center', width: '160', render: 'datetime' },
        { label: '操作', align: 'center', width: '130', render: 'buttons', buttons: defaultOptButtons(['edit', 'delete']) },
      ],
    },
    {
      defaultItems: {
        status: '0',
      },
    },
    {
      // 提交前
      onSubmit: ({ formEl, items }) => {
        const item = cloneDeep(items);

        for (const key in item) {
          if (item[key] === null || item[key].length === 0) {
            delete item[key];
          }
        }
        // 表单验证通过后执行的api请求操作
        let submitCallback = () => {
          xTable.form.submitLoading = true;
          xTable.api
            .postData(xTable.form.operate!, item)
            .then((res: anyObj) => {
              xTable.onTableHeaderAction('refresh', {});
              xTable.form.submitLoading = false;
              xTable.form.operateIds?.shift();
              if (xTable.form.operateIds!.length > 0) {
                xTable.toggleForm('edit', xTable.form.operateIds);
              } else {
                xTable.toggleForm();
              }
              xTable.runAfter('onSubmit', { res });
            })
            .catch(() => {
              xTable.form.submitLoading = false;
            });
        };

        if (formEl) {
          xTable.form.ref = formEl;
          formEl.validate((valid) => {
            if (valid) {
              submitCallback();
            }
          });
        } else {
          submitCallback();
        }
        return false;
      },
    },
  );

  provide('xTable', xTable);

  onMounted(() => {
    xTable.table.ref = tableRef.value;
    xTable.mount();
    xTable.getIndex();
    setMerchantsvList();
  });
</script>

<style lang="scss" scoped></style>
