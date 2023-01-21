<template>
  <div>
    <el-card>
      <template #header>
        <ElButton type="success"> 新增用户 </ElButton>
        <ElButton type="danger" @click="refreshAccessToken"> 删除删除 </ElButton>
        <ElButton type="info" :icon="Refresh" @click="refreshTable" />
        <ElButton type="info" :icon="Refresh" @click="refreshAccessToken" />
      </template>
      <Table
        :columns="tableColumn"
        :table-data="getUserInfoList.list || []"
        :options="options"
        @pagination-change="handlePaginationChange"
        @selection-change="handleSelection"
      >
        <!-- 插槽自定义表头  addressHeader就是tableColumn中地址那一列定义的
                <template #addressHeader="{ column }">
                    <span>{{ column.label }}(插槽自定义表头)</span>
                </template> -->
      </Table>
    </el-card>

    <easy-dialog
      title="编辑用户"
      :fieldList="fieldList"
      :model="formData"
      @submit="handleUserSubmit"
      @update:visible="recorddialogVisible"
      :visible="dialogVisible"
      :options="optionsDialog"
      @cancel="cancelEdit"
    >
      <!-- 如果不使用默认的按钮可以使用插槽自定义内容， 插槽返回的model就是当前表单的数据 -->
      <!-- <template #buttons="{ model }">
                  <el-button">提交</el-button>
              </template> -->
    </easy-dialog>
  </div>
</template>

<script setup name="ViewsAdminUserUserList" lang="ts">
  import { ElMessageBox, ElMessage, ElTag, ElButton } from 'element-plus';
  import dayjs from 'dayjs';
  import { useUserStore, useRoleStore } from '/@/store';
  import { UserInfoType } from '/@/api/user/types';
  import { exampleForm } from '/@/config/form';
  import { Refresh } from '@element-plus/icons-vue';
  const router = useRouter();

  const route = useRoute();
  const userStore = useUserStore();
  const {
    list,
    editUserInfo,
    updateUser,
    deleteUser,
    refreshAccessToken,
    getPagingArguments,
    setPagingArguments,
    refreshTable,
    getUserInfoList,
  } = userStore;

  const roleStore = useRoleStore();
  const {
    // getRoleInfo,
    // getRoleAll,
    setRoleAll,
    setRoleInfo,
  } = roleStore;

  const dialogVisible = ref(false);
  const fieldList: Form.FieldItem[] = exampleForm.editUser;
  const formData = ref<Record<string, any>>();

  // import { h } from 'vue'
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

  const tableColumn: Table.Column[] = [
    { type: 'selection', width: '50' },
    {
      type: 'index',
      width: '50',
      label: 'No.',
      render: ({ row }: Record<string, UserInfoType>) => {
        return h('span', row.id);
      },
    },
    { prop: 'userNick', label: '名字' },
    // 日期使用render函数格式化
    {
      prop: 'createdTime',
      label: '创建日期',
      headerRender: ({ column }) => h(ElTag, { type: 'danger', effect: 'plain' }, () => `${column.label}(渲染函数自定义表头)`),
      render: ({ row }: Record<string, UserInfoType>) => h('span', dayjs(row.createdTime).format('YYYY-MM-DD HH:mm')),
    },
    { prop: 'mobile', label: '手机号' },
    { prop: 'email', label: '邮箱' },
    {
      prop: 'status',
      label: '状态',
      render: ({ row }: Record<string, UserInfoType>) => {
        return h(ElTag, { type: row.status === '0' ? 'success' : 'danger' }, () => (row.status === '0' ? '有效' : '禁用'));
      },
    },

    // 按钮使用render函数渲染
    {
      width: '140',
      label: '操作',
      render: ({ row }: Record<string, UserInfoType>) =>
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

  // 监听子组件dialogVisible变化
  const recorddialogVisible = (n: any) => {
    dialogVisible.value = n;
  };

  // 取消编辑
  const cancelEdit = () => {
    dialogVisible.value = false;
    ElMessage.warning('取消编辑');
  };

  /**
   * 注意： model数据模型非必填项，如果仅仅是用于数据收集，model参数可以不用填，表单的submit事件会返回所有搜集的数据对象
   *       如果是编辑的情况下，页面需要回显数据，则model数据模型必须要填写
   */
  const handleUserSubmit = async (model: Record<string, UserInfoType>) => {
    const userInfo = model as unknown as UserInfoType;
    // 编辑用户
    if (userInfo.id) {
      await updateUser(model as unknown as UserInfoType);
      // 修改用户状态
      const currentStatus: string | undefined = getUserInfoList.list?.find((item) => item.id === userInfo.id)?.status;
      if (!currentStatus) return;
      await editUserInfo(model as unknown as UserInfoType, currentStatus);
      ElMessage.success(`编辑${model.userName}用户成功`);
    } else {
      // 新增用户
      ElMessage.success('新增用户成功');
    }
    refreshTable();
  };
  // 编辑用户
  const handleRenderEdit = (row: UserInfoType) => {
    dialogVisible.value = true;
    // json数据深拷贝
    const data = JSON.parse(JSON.stringify(row));
    formData.value = data;
  };

  const handleRenderDelete = (row: UserInfoType) => {
    ElMessageBox.confirm('此操作将永久删除该用户, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
      .then(async () => {
        await deleteUser(new Array(row.id));
        ElMessage.success(`删除${row.userNick}用户成功`);
        refreshTable();
      })
      .catch((err) => {
        ElMessage.error(err);
      });
  };
  const handleSelection = (val: UserInfoType[]) => {
    console.log('父组件接收的多选数据', val);
  };

  onMounted(() => {
    setRoleAll();
    setRoleInfo('1');
  });

  watch(
    () => route.query,
    async (newval) => {
      const { page, pageSize } = newval;
      setPagingArguments({
        page: Number(page) || getPagingArguments.page,
        limit: Number(pageSize) || getPagingArguments.limit,
      });
      const result = await list(getPagingArguments);
      state.options.paginationConfig = {
        currentPage: result.currPage,
        pageSize: result.pageSize,
        total: result.totalCount,
      };
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
  const { options, optionsDialog } = toRefs(state);
</script>

<style lang="less" scoped></style>
