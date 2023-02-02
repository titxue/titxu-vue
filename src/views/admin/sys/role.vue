<template>
  <div>
    <el-card>
      <template #header>
        <ElButton type="success" @click="handleRenderAdd"> 新增角色 </ElButton>
        <ElButton type="danger" @click="deleteRoleList(deleteRoleIdList)"> 删除角色 </ElButton>
      </template>
      <Table
        :table-data="rolePageList"
        :columns="tableColumn"
        :options="options"
        @selection-change="handleSelection"
        @pagination-change="handlePaginationChange"
      >
        <!-- 插槽自定义表头  addressHeader就是tableColumn中地址那一列定义的
                <template #addressHeader="{ column }">
                    <span>{{ column.label }}(插槽自定义表头)</span>
                </template> -->
      </Table>
    </el-card>
    <easy-dialog
      :title="dialogTitle"
      :fieldList="roleFieldList"
      :model="roleFormData"
      @submit="handleRoleSubmit"
      @update:visible="recorddialogVisible"
      :visible="dialogVisible"
      :options="optionsDialog"
      @cancel="cancelEdit"
    />
  </div>
</template>

<script setup lang="ts">
  import dayjs from 'dayjs';
  import { ElButton, ElMessage, ElMessageBox, ElSwitch, ElTag } from 'element-plus';
  import { RoleResultType, RoleSaveOrUpdateType } from '/@/api/role/types';
  import { roleDialog } from '/@/config/dialog';
  import { useRoleStore } from '/@/store/modules/role';
  import { setPaginationOptions } from '/@/utils/index';
  const router = useRouter();
  const route = useRoute();

  //角色状态管理
  const roleStore = useRoleStore();

  const { setRoleList, switchRoleStatus, deleteRoleByIds, setPagingArguments, updateRole, addRole, setRoleInfoById } = roleStore;

  // 响应式数据
  const { rolePageList, pagingArguments, rolePage } = toRefs(roleStore);

  // 用于dialog配置
  const dialogTitle = ref('');
  const dialogVisible = ref(false);
  const roleFormData = ref<Record<string, RoleSaveOrUpdateType>>();
  const roleFieldList = ref(roleDialog.editRole);

  const handleRoleSubmit = async (model: Record<string, RoleSaveOrUpdateType>) => {
    const userInfo = model as unknown as RoleSaveOrUpdateType;
    // 编辑用户
    // id不为空则是编辑用户
    if (userInfo.id !== undefined && userInfo.id !== null) {
      await updateRole(model as unknown as RoleSaveOrUpdateType);
      ElMessage.success(`编辑${model.userNick}用户成功`);
    } else {
      // 新增用户
      await addRole(model as unknown as RoleSaveOrUpdateType);
      roleFormData.value = {};
      ElMessage.success(`新增${model.userNick}用户成功`);
    }
  };
  // 监听子组件dialogVisible变化
  const recorddialogVisible = (n: boolean) => {
    dialogVisible.value = n;
  };
  // 取消编辑
  const cancelEdit = () => {
    dialogVisible.value = false;
    ElMessage.warning('取消编辑');
  };

  // ---------------------------------------表格相关---------------------------------------
  // import Table from '@/components/Table/index.vue'
  // 本项目Table组件自动引入，如复制此代码，需根据路径引入Table组件后使用
  interface State {
    options: Table.Options;
    optionsDialog: Form.Options;
  }

  const state = reactive<State>({
    options: { showPagination: true, height: 600 },
    optionsDialog: { showCancelButton: true },
  });

  const { options, optionsDialog } = toRefs(state);

  const tableColumn: Table.Column[] = [
    { type: 'selection', width: '50' },

    { prop: 'roleName', label: '角色名称' },
    { prop: 'roleCode', label: '角色编码' },
    {
      prop: 'updatedTime',
      label: '更新日期',
      width: '155',
      headerRender: ({ column }) => h(ElTag, { type: 'danger', effect: 'plain' }, () => `${column.label}`),
      render: ({ row }: Record<string, RoleResultType>) =>
        h('span', dayjs(row.updatedTime ?? '1111-11-11T11:11:11').format('YYYY-MM-DD HH:mm:ss')),
    },
    // 状态切换
    {
      prop: 'status',
      label: '状态',
      render: ({ row }: Record<string, RoleResultType>) =>
        h(
          ElSwitch,
          {
            modelValue: row.status === '0',
            'onUpdate:modelValue': () => switchRoleStatus(row.id),
          },
          { default: () => '' },
        ),
    },

    // 按钮使用render函数渲染
    {
      width: '140',
      label: '操作',
      render: ({ row }: Record<string, RoleResultType>) =>
        h('div', null, [
          h(
            ElButton,
            {
              type: 'primary',
              size: 'small',
              onClick: () => handleRenderEdit(row),
            },
            { default: () => '编辑' },
          ),
          h(
            ElButton,
            {
              type: 'danger',
              size: 'small',
              onClick: () => handleRenderDelete(row),
            },
            { default: () => '删除' },
          ),
        ]),
    },
  ];

  // 删除用户id列表
  const deleteRoleIdList = ref<string[]>([]);
  const handleSelection = (val: RoleResultType[]) => {
    // console.log('父组件接收的多选数据', val);
    // 返回id列表
    deleteRoleIdList.value = val.map((item) => item.id);
  };
  // 删除多选
  const deleteRoleList = async (ids: string[]) => {
    ElMessageBox.confirm('此操作将永久删除该角色, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
      .then(async () => {
        await deleteRoleByIds(ids);
        ElMessage.success(`删除${ids.length}个角色成功`);
      })
      .catch((err) => {
        ElMessage.error(err);
      });
  };
  const handleRenderDelete = (row: RoleResultType) => {
    ElMessageBox.confirm('此操作将永久删除该角色, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
      .then(async () => {
        await deleteRoleByIds(Array.of(row.id));
        ElMessage.success(`删除${row.roleName}角色成功`);
      })
      .catch((err) => {
        ElMessage.error(err);
      });
  };
  // 编辑角色
  const handleRenderEdit = async (row: RoleResultType) => {
    // 设置编辑用户的fieldList
    roleFieldList.value = roleDialog.editRole;
    // json数据深拷贝
    const data = JSON.parse(JSON.stringify(row));
    await setRoleInfoById(data.id);
    // 设置编辑角色的formData
    roleFormData.value = {
      id: data.id,
      roleName: data.roleName,
      roleCode: data.roleCode,
    };
    dialogTitle.value = '编辑角色';
    dialogVisible.value = true;
  };
  // 新增角色
  const handleRenderAdd = () => {
    roleFormData.value = {};
    dialogTitle.value = '新增用户';
    dialogVisible.value = true;
  };
  watch(
    () => route.query,
    async (newval) => {
      try {
        const { page, pageSize } = newval;
        setPagingArguments({
          page: Number(page) || pagingArguments.value.page,
          limit: Number(pageSize) || pagingArguments.value.limit,
        });
        await setRoleList(pagingArguments.value);
        setPaginationOptions(rolePage.value, state.options);
      } catch (error) {
        console.log('watch', error);
      }
    },
    { immediate: true },
  );
  // pageSize或者currentPage改变触发
  const handlePaginationChange = (page: number, pageSize: number) => {
    router.push({
      path: route.path,
      query: {
        page,
        pageSize,
      },
    });
  };

  onMounted(async () => {
    // 获取角色分页列表
    await setRoleList();
    setPaginationOptions(rolePage.value, state.options);
  });
</script>

<style lang="less" scoped></style>
