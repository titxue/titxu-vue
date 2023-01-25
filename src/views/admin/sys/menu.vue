<template>
  <div>
    <el-card>
      <template #header>
        <ElButton type="success" @click="setMenuList"> 新增用户 </ElButton>
        <!-- <ElButton type="danger" @click="deleteUserList(deleteUserIdList)"> 删除用户 </ElButton>
        <ElButton type="info" :icon="Refresh" @click="refreshTable"> 刷新表格 </ElButton>
        <ElButton type="info" :icon="Refresh" @click="refreshAccessToken"> 刷新Token </ElButton> -->
      </template>
      <Table :columns="tableColumn" :table-data="menuList || []" :options="options">
        <!-- 插槽自定义表头  addressHeader就是tableColumn中地址那一列定义的
                <template #addressHeader="{ column }">
                    <span>{{ column.label }}(插槽自定义表头)</span>
                </template> -->
      </Table>
    </el-card>

    <!-- <easy-dialog
      :title="dialogTitle"
      :fieldList="fieldList"
      :model="formData"
      @submit="handleUserSubmit"
      @update:visible="recorddialogVisible"
      :visible="dialogVisible"
      :options="optionsDialog"
      @cancel="cancelEdit"
    >
      如果不使用默认的按钮可以使用插槽自定义内容， 插槽返回的model就是当前表单的数据 -->
    <!-- <template #buttons="{ model }">
                  <el-button">提交</el-button>
              </template>
    </easy-dialog> -->
  </div>
</template>

<script setup lang="ts">
  import { dayjs, ElButton, ElIcon, ElTag } from 'element-plus';
  import { resolveDynamicComponent } from 'vue';
  import { usePermissionStore } from '/@/store';

  // 权限状态管理
  const permissionStore = usePermissionStore();
  const { setMenuList } = permissionStore;
  const { menuList } = toRefs(permissionStore);

  interface State {
    options: Table.Options;
  }
  const state = reactive<State>({
    options: {
      showPagination: true,
      height: 600,
      rowKey: 'id',
      // treeProps: { hasChildren: 'subList', children: 'subList' }
    },
  });

  const { options } = toRefs(state);

  const tableColumn: Table.Column[] = [
    { type: 'selection', width: '50' },
    { prop: 'permissionName', label: '菜单名称' },
    { prop: 'parentName', label: '父级菜单' },
    // 日期使用render函数格式化
    {
      prop: 'created_time',
      label: '创建日期',
      headerRender: ({ column }) => h(ElTag, { type: 'danger', effect: 'plain' }, () => `${column.label}`),
      render: ({ row }: Record<string, any>) => h('span', dayjs(row.createdTime).format('YYYY-MM-DD HH:mm')),
    },
    {
      prop: 'menuIcon',
      label: '菜单图标',
      render: ({ row }: Record<string, any>) => {
        const Component = resolveDynamicComponent(row.menuIcon);
        return h(ElIcon, null, () => h(Component as any, '1'));
      },
    },
    { prop: 'menuUrl', label: '菜单URL' },
    {
      prop: 'permissionType',
      label: '菜单类型',
      render: ({ row }: Record<string, any>) =>
        h(
          ElTag,
          { type: row.permissionType === '0' ? 'danger' : 'success', effect: 'plain' },
          row.permissionType === '0' ? '目录' : '菜单',
        ),
    },
    {
      prop: 'permissionLevel',
      label: '菜单级别',
      render: ({ row }: Record<string, any>) =>
        h(
          ElTag,
          { type: row.permissionLevel === '0' ? 'danger' : 'success', effect: 'plain' },
          row.permissionLevel === '0' ? '系统' : '租户',
        ),
    },
    // 按钮使用render函数渲染
    {
      width: '200',
      label: '操作',
      render: ({ row }: Record<string, any>) =>
        h('div', null, [
          h(
            ElButton,
            {
              type: 'primary',
              size: 'small',
              //   onClick: () => handleRenderEdit(row),
            },
            { default: () => '编辑' + row },
          ),
          h(
            ElButton,
            {
              type: 'danger',
              size: 'small',
              //   onClick: () => handleRenderDelete(row),
            },
            { default: () => '删除' },
          ),
        ]),
    },
  ];

  onMounted(() => {
    console.log('mounted');
    setMenuList();
  });
</script>

<style lang="less" scoped>
  .a {
    display: none !important;
  }
</style>
