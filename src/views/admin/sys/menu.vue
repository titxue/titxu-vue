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

    <easy-dialog
      :title="dialogTitle"
      :fieldList="fieldList"
      :model="formData"
      @submit="handleMenuSubmit"
      @update:visible="recorddialogVisible"
      :visible="dialogVisible"
      :options="optionsDialog"
      @cancel="cancelEdit"
    />
  </div>
</template>

<script setup lang="ts">
  import { ElButton, ElIcon, ElMessage, ElMessageBox, ElTag } from 'element-plus';
  import { resolveDynamicComponent } from 'vue';
  import { usePermissionStore } from '/@/store';
  import { menuDialog } from '/@/config/dialog';
  import { MenuType } from '/@/api/permission/types';

  // 权限状态管理
  const permissionStore = usePermissionStore();
  const { setMenuList, removePermission } = permissionStore;
  const { menuList } = toRefs(permissionStore);

  // 用于dialog配置
  const dialogTitle = ref('');
  const dialogVisible = ref(false);
  const fieldList = ref(menuDialog.editMenu);
  const formData = ref<Record<string, string>>();

  // 监听子组件dialogVisible变化
  const recorddialogVisible = (n: boolean) => {
    dialogVisible.value = n;
  };

  const handleMenuSubmit = async (model: Record<string, string>) => {
    console.log(model);
  };

  // 取消编辑
  const cancelEdit = () => {
    dialogVisible.value = false;
    ElMessage.warning('取消编辑');
  };

  interface State {
    options: Table.Options;
    optionsDialog: Form.Options;
  }

  const state = reactive<State>({
    options: {
      showPagination: true,
      height: 600,
      rowKey: 'id',
      // treeProps: { hasChildren: 'subList', children: 'subList' }
    },
    optionsDialog: { showCancelButton: true },
  });

  const { options, optionsDialog } = toRefs(state);

  const tableColumn: Table.Column[] = [
    { type: 'selection', width: '50' },
    { prop: 'permissionName', label: '菜单名称' },
    { prop: 'parentName', label: '父级菜单' },
    {
      prop: 'menuIcon',
      label: '菜单图标',
      align: 'center',
      render: ({ row }: Record<string, MenuType>) => {
        const Component = resolveDynamicComponent(row.menuIcon);
        return h(ElIcon, { size: 18 }, () => h(Component as any));
      },
    },
    {
      prop: 'menuIcon',
      label: '图标名称',
    },
    {
      prop: 'menuUrl',
      label: '菜单URL',
      render: ({ row }: Record<string, MenuType>) =>
        h(ElTag, { type: row.menuUrl === null ? 'danger' : 'success', effect: 'plain' }, () =>
          row.menuUrl === null ? '目录' : row.menuUrl,
        ),
    },
    {
      prop: 'permissionType',
      label: '菜单类型',
      render: ({ row }: Record<string, MenuType>) =>
        h(ElTag, { type: row.permissionType === '0' ? 'danger' : 'success', effect: 'plain' }, () =>
          row.permissionType === '0' ? '目录' : '菜单',
        ),
    },
    {
      prop: 'permissionLevel',
      label: '菜单级别',
      render: ({ row }: Record<string, MenuType>) =>
        h(
          ElTag,
          { type: row.permissionLevel === '0' ? 'danger' : 'success', effect: 'plain' },
          // Non-function value encountered for default slot. Prefer function slots for better performance.
          // 必须使用函数返回值
          () => (row.permissionLevel === '0' ? '系统' : '租户'),
        ),
    },
    // 按钮使用render函数渲染
    {
      width: '200',
      label: '操作',
      render: ({ row }: Record<string, MenuType>) =>
        h('div', null, [
          h(
            ElButton,
            {
              type: 'primary',
              size: 'small',
              onClick: () => handleMenuEdit(row),
            },
            { default: () => '编辑' },
          ),
          h(
            ElButton,
            {
              type: 'danger',
              size: 'small',
              onClick: () => handleMenuDelete(row),
            },
            { default: () => '删除' },
          ),
        ]),
    },
  ];

  const handleMenuDelete = (row: MenuType) => {
    ElMessageBox.confirm('此操作将永久删除该菜单, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
      .then(async () => {
        await removePermission(row.id);
        ElMessage.success(`删除${row.permissionName}菜单成功`);
      })
      .catch((err) => {
        ElMessage.error(err);
      });
  };
  // 编辑 打开dialog
  const handleMenuEdit = (row: MenuType) => {
    // json数据深拷贝
    const data = JSON.parse(JSON.stringify(row));
    formData.value = data;
    dialogVisible.value = true;
    console.log(row);
  };

  onMounted(() => {
    setMenuList();
  });
</script>

<style lang="less" scoped></style>
