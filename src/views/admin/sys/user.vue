<template>
  <div class="default-main ba-table-box">
    <el-alert class="ba-table-alert" v-if="xTable.table.remark" :title="xTable.table.remark" type="info" show-icon />

    <!-- 表格顶部菜单 -->
    <TableHeader
      :buttons="['refresh', 'add', 'edit', 'delete', 'quickSearch', 'columnDisplay']"
      quick-search-placeholder="通过角色名称模糊搜索"
    />

    <!-- 表格 -->
    <!-- 要使用`el-table`组件原有的属性，直接加在Table标签上即可 -->
    <Table ref="tableRef" />

    <!-- 表单 -->
    <UserForm ref="formRef" />
  </div>
</template>

<script setup lang="ts">
  import { cloneDeep } from 'lodash-es';
  import xTableClass from '/@/utils/xTable';
  import { ADMIN_URL, xTableApi } from '/@/api/common';
  import { defaultOptButtons } from '/@/components/v1/table/index';
  import UserForm from './components/userForm.vue';
  import Table from '/@/components/v1/table/index.vue';
  import TableHeader from '/@/components/v1/table/header/index.vue';
  import { UserInfoType } from '/@/api/user/types';

  const formRef = ref();
  const tableRef = ref();
  const xTable: xTableClass<UserInfoType> = new xTableClass(
    new xTableApi(ADMIN_URL.user),
    {
      dblClickNotEditColumn: [undefined],
      column: [
        { type: 'selection', align: 'center' },
        { label: '用户昵称', prop: 'userNick', align: 'center', width: '150' },
        { label: '手机号', prop: 'mobile', align: 'center' },
        { label: '邮箱', prop: 'email', align: 'center' },
        { label: '租户', prop: 'tenantName', align: 'center' },
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
  });
</script>

<style lang="scss" scoped></style>
