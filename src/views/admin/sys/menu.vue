<template>
  <div>
    <el-card>
      <template #header>
        <ElButton type="success"> 新增用户 </ElButton>
        <!-- <ElButton type="danger" @click="deleteUserList(deleteUserIdList)"> 删除用户 </ElButton>
        <ElButton type="info" :icon="Refresh" @click="refreshTable"> 刷新表格 </ElButton>
        <ElButton type="info" :icon="Refresh" @click="refreshAccessToken"> 刷新Token </ElButton> -->
      </template>
      <Table :columns="tableColumn" :table-data="tableData || []" :options="options">
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
  import { dayjs, ElButton, ElTag } from 'element-plus';

  interface State {
    options: Table.Options;
  }
  const state = reactive<State>({
    options: { showPagination: true, height: 600, rowKey: 'name' },
  });

  const { options } = toRefs(state);
  // 基本表格数据
  const tableData: any[] = [
    {
      date: 1660737564000,
      name: '佘太君',
      address: '上海市普陀区金沙江路 1516 弄',
    },
    {
      date: 1462291200000,
      name: '王小虎',
      address: '上海市普陀区金沙江路 1517 弄',
    },
    {
      date: 1462032000000,
      name: '王小帅',
      address: '上海市普陀区金沙江路 1519 弄',
    },
    {
      date: 1462204800000,
      name: '王小呆',
      address: '上海市普陀区金沙江路 1516 弄',
      children: [
        {
          date: 1462291200000,
          name: '王小虎',
          address: '上海市普陀区金沙江路 1517 弄',
        },
      ],
    },
  ];
  const tableColumn: Table.Column[] = [
    { type: 'selection', width: '50' },
    // {
    //   type: 'index',
    //   width: '50',
    //   label: 'No.',
    //   render: ({ row }: Record<string, any>) => {
    //     return h('span', row.id);
    //   },
    // },
    { prop: 'name', label: '名字' },
    // 日期使用render函数格式化
    {
      prop: 'date',
      label: '创建日期',
      headerRender: ({ column }) => h(ElTag, { type: 'danger', effect: 'plain' }, () => `${column.label}`),
      render: ({ row }: Record<string, any>) => h('span', dayjs(row.createdTime).format('YYYY-MM-DD HH:mm')),
    },
    { prop: 'address', label: '手机号' },
    { prop: 'email', label: '邮箱' },

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
            { default: () => '编辑' + row.name },
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
</script>

<style lang="less" scoped></style>
