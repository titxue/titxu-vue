<template>
  <div class="default-main ba-table-box">
    <el-alert class="ba-table-alert" v-if="xTable.table.remark" :title="xTable.table.remark" type="info" show-icon />

    <!-- 表格顶部菜单 -->
    <TableHeader :buttons="['refresh', 'quickSearch', 'columnDisplay']" quick-search-placeholder="通过角色名称模糊搜索" />

    <!-- 表格 -->
    <!-- 要使用`el-table`组件原有的属性，直接加在Table标签上即可 -->
    <Table ref="tableRef" />
  </div>
</template>

<script setup lang="ts">
  import xTableClass from '/@/utils/xTable';
  import { ADMIN_URL, xTableApi } from '/@/api/common';
  import Table from '/@/components/v1/table/index.vue';
  import TableHeader from '/@/components/v1/table/header/index.vue';

  const tableRef = ref();
  const xTable: xTableClass<anyObj> = new xTableClass(new xTableApi(ADMIN_URL.log), {
    dblClickNotEditColumn: ['all'],
    column: [
      { type: 'selection', align: 'center' },
      { label: '管理昵称', prop: 'userNick', align: 'center' },
      { label: '管理手机号', prop: 'mobile', align: 'center' },
      { label: '操作', prop: 'operation', align: 'center' },
      { label: '请求方法', prop: 'method', align: 'center' },
      { label: '请求参数', prop: 'params', align: 'center' },
      { label: '请求IP', prop: 'ip', align: 'center' },
      { label: '请求时间', prop: 'time', align: 'center' },
      { label: '创建时间', prop: 'createdTime', align: 'center', width: '160', render: 'datetime' },
    ],
  });

  provide('xTable', xTable);

  onMounted(() => {
    xTable.table.ref = tableRef.value;
    xTable.table.filter!.limit = 20;
    xTable.mount();
    xTable.getIndex();
  });
</script>

<style lang="scss" scoped></style>
