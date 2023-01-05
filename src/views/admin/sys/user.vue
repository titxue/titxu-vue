<template>
  <div>
    <el-card>
      <template #header>
        <ElButton type="info" :icon="Refresh" @click="refreshTable" />
      </template>
      <Table
        :columns="tableColumn"
        :table-data="tableDataList"
        :options="options"
        @pagination-change="handlePaginationChange"
        @selection-change="handleSelection"
        @command="handleAction"
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
      @submit="handleBaseSubmit"
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
  import { useUserStore } from '/@/store';
  import { ReqListParams, UserInfoType } from '/@/api/user/types';
  // import { refreshToken } from '/@/utils/auth';
  import { exampleForm } from '/@/config/form';
  import { Refresh } from '@element-plus/icons-vue';
  // const { proxy } = getCurrentInstance()
  const router = useRouter();

  const route = useRoute();
  const store = useUserStore();
  const { list } = store;

  const dialogVisible = ref(false);
  const fieldList: Form.FieldItem[] = exampleForm.editUser;
  const formData = ref<Record<string, any>>();
  /**
   * 注意： model数据模型非必填项，如果仅仅是用于数据收集，model参数可以不用填，表单的submit事件会返回所有搜集的数据对象
   *       如果是编辑的情况下，页面需要回显数据，则model数据模型必须要填写
   */
  const handleBaseSubmit = (model: Record<string, any>) => {
    console.log(model);
  };
  // const handleTest = () => {
  //     refreshToken()
  // }

  // 取消编辑
  const cancelEdit = () => {
    dialogVisible.value = false;
    ElMessage.warning('取消编辑');
  };

  // import { h } from 'vue'
  // import Table from '@/components/Table/index.vue'
  // 本项目Table组件自动引入，如复制此代码，需根据路径引入Table组件后使用
  interface State {
    tableDataList: UserInfoType[];
    options: Table.Options;
    optionsDialog: Form.Options;
  }

  const state = reactive<State>({
    tableDataList: [],
    options: { showPagination: true, height: 600 },
    optionsDialog: { showCancelButton: true },
  });
  const params: ReqListParams = {
    page: 1,
    limit: 10,
  };

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
    { prop: 'userName', label: '名字' },
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
        return h(ElTag, { type: 'success' }, () => (row.status === '0' ? '有效' : '禁用'));
      },
    },

    // 按钮使用render函数渲染
    {
      width: '140',
      label: '操作',
      render: ({ row }: Record<string, UserInfoType>, scope: any) =>
        // 渲染单个元素
        //   h(
        //             ElButton,
        //             {
        //                 type: 'primary',
        //                 size: 'small',
        //                 onClick: () => handleRenderEdit(row, index)
        //             },
        //             { default: () => '编辑' }
        //         ),
        // 渲染多个元素
        h('div', null, [
          h(
            ElButton,
            {
              type: 'primary',
              size: 'small',
              onClick: () => handleRenderEdit(row, scope.attrs.index),
            },
            { default: () => '编辑' },
          ),
          h(
            ElButton,
            {
              type: 'danger',
              size: 'small',
              onClick: () => handleRenderDelete(row, scope.attrs.index),
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

  // 编辑用户
  const handleRenderEdit = (row: UserInfoType, index: number) => {
    dialogVisible.value = true;
    console.log('row', row);
    formData.value = row;

    ElMessage.success(`${row.userName} ----- ${index}`);
  };
  const handleRenderDelete = (row: UserInfoType, index: number) => {
    ElMessage.error(`${row.userName} ----- ${index}`);
  };
  const handleSelection = (val: UserInfoType[]) => {
    console.log('父组件接收的多选数据', val);
  };
  const handleAction = (command: Table.Command, row: UserInfoType) => {
    switch (command) {
      case 'edit':
        alert('点击了编辑');
        break;
      case 'delete':
        console.log('row', row);
        ElMessageBox.confirm('确认删除吗？', '提示').then(() => {
          ElMessage(JSON.stringify(row));
        });
        break;
      default:
        break;
    }
  };

  watch(
    () => route.query,
    async (newval) => {
      const { page, pageSize } = newval;
      params.page = Number(page) || params.page;
      params.limit = Number(pageSize) || params.limit;
      const { page: result } = await list(params);
      const { list: UserList } = result;
      if (UserList) {
        state.tableDataList = UserList;
      }
      state.options.paginationConfig = {
        currentPage: result.currPage,
        pageSize: result.pageSize,
        total: result.totalCount,
      };
    },
    { immediate: true },
  );
  // 刷新表格数据
  const refreshTable = () => {
    list(params);
    ElMessage.success('刷新成功');
  };

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
  const { tableDataList, options, optionsDialog } = toRefs(state);
</script>

<style lang="less" scoped></style>
