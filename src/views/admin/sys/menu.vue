<template>
  <div>
    <el-card>
      <template #header>
        <ElButton type="success" @click="handleMenuAdd"> 新增菜单 </ElButton>
        <ElButton type="danger" @click="refreshTable"> 刷新权限 </ElButton>
        <ElButton type="danger" @click="setRoleList"> 刷新 </ElButton>
      </template>
      <Table :columns="tableColumn" :table-data="menuList || []" :options="options" />
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
  import { ElButton, ElIcon, ElMessage, ElMessageBox, ElSwitch, ElTag } from 'element-plus';
  import SvgIcon from '/@/components/SvgIcon/index.vue';
  import { resolveDynamicComponent } from 'vue';
  import { usePermissionStore, useRoleStore } from '/@/store';
  import { menuDialog } from '/@/config/dialog';
  import { MenuType } from '/@/api/permission/types';

  // 权限状态管理
  const permissionStore = usePermissionStore();
  const roleStore = useRoleStore();
  const { setRoleList } = roleStore;
  const { setMenuList, removePermission, editMenu, addMenu, refreshTable, setParentMenuList, disableMenu } = permissionStore;
  const { menuList, parentMenuList } = toRefs(permissionStore);

  // 用于dialog配置
  const dialogTitle = ref('');
  const dialogVisible = ref(false);
  const fieldList = ref(menuDialog.editMenu);
  const formData = ref<Record<string, string>>();

  // 监听子组件dialogVisible变化
  const recorddialogVisible = (n: boolean) => {
    dialogVisible.value = n;
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
      showPagination: false,
      height: 600,
      rowKey: 'id',
    },
    optionsDialog: { showCancelButton: true },
  });
  const { options, optionsDialog } = toRefs(state);
  // 设置dialog的fieldList
  const setFieldOptions = (fieldList: Form.FieldItem[], row: MenuType) => {
    fieldList.forEach((item) => {
      // 填充父级菜单
      if (item.field === 'parentId') {
        console.log(parentMenuList.value);
        item.options = {
          data: parentMenuList.value.map((item) => {
            return {
              label: item.permissionName,
              value: item.id,
            };
          }),
          multiple: false,
        };
      }
      if (item.field === 'menuUrl') {
        if (row.permissionType === '0') {
          item.disabled = false;
          item.placeholder = '目录不需要填写';
        } else {
          item.disabled = false;
          item.placeholder = '请输入菜单URL';
        }
      }
    });
  };

  // 新增菜单 dialog
  const handleMenuAdd = async () => {
    // 初始化父级菜单列表
    await setParentMenuList();
    // 设置编辑用户的fieldList
    fieldList.value = menuDialog.addMenu;
    setFieldOptions(fieldList.value, {} as MenuType);
    dialogTitle.value = '新增菜单';
    formData.value = {};
    dialogVisible.value = true;
  };

  // 编辑 打开dialog
  const handleMenuEdit = async (row: MenuType) => {
    // 初始化父级菜单列表
    await setParentMenuList();
    // 设置编辑用户的fieldList
    fieldList.value = menuDialog.editMenu;
    setFieldOptions(fieldList.value, row);
    dialogTitle.value = '编辑菜单';
    // json数据深拷贝
    const data = JSON.parse(JSON.stringify(row));
    formData.value = data;
    dialogVisible.value = true;
  };
  // 菜单编辑提交
  const handleMenuSubmit = async (model: Record<string, MenuType>) => {
    const menuInfo = model as unknown as MenuType;
    const { id } = menuInfo;
    if (id) {
      // 编辑
      const res = await editMenu(menuInfo);
      if (res) {
        ElMessage.success('编辑成功');
        // 重新获取菜单列表
        await refreshTable();
        dialogVisible.value = false;
      }
    } else {
      // 新增
      const res = await addMenu(menuInfo);
      if (res) {
        ElMessage.success('新增成功');
        // 重新获取菜单列表
        await refreshTable();
        dialogVisible.value = false;
      }
    }
  };

  const isSvgIcon = (menuIcon: string): boolean => {
    if (menuIcon !== null) {
      return menuIcon.indexOf('svg-') !== -1;
    }
    return false;
  };

  const tableColumn: Table.Column[] = [
    { type: 'selection', width: '50' },
    { prop: 'parentName', label: '父级菜单', width: '120' },
    { prop: 'permissionName', label: '菜单名称' },
    {
      prop: 'menuIcon',
      label: '菜单图标',
      align: 'center',
      render: ({ row }: Record<string, MenuType>) => {
        const menuIcon = row.menuIcon;

        if (menuIcon !== undefined && menuIcon !== '') {
          if (isSvgIcon(menuIcon)) {
            return h(SvgIcon, { name: menuIcon });
          }
          const Component = resolveDynamicComponent(menuIcon);
          return h(ElIcon, { size: 20 }, () => h(Component as any));
        }
        return h(SvgIcon, { name: 'svg-role' });
      },
    },
    {
      prop: 'menuIcon',
      label: '图标名称',
      render: ({ row }: Record<string, MenuType>) => h(ElTag, { type: 'info', effect: 'plain' }, () => row.menuIcon || '默认图标'),
    },
    {
      prop: 'menuUrl',
      label: '菜单URL',
      width: '110',
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
    // 状态切换
    {
      prop: 'status',
      label: '状态',
      render: ({ row }: Record<string, MenuType>) =>
        h(
          ElSwitch,
          {
            modelValue: row.status === '0',
            'onUpdate:modelValue': () => disableMenu(row.id),
          },
          { default: () => '' },
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
        await refreshTable();
        ElMessage.success(`删除${row.permissionName}菜单成功`);
      })
      .catch((err) => {
        ElMessage.error(err);
      });
  };

  onMounted(async () => {
    // 初始化菜单列表
    await setMenuList();
  });
</script>

<style lang="less" scoped></style>
