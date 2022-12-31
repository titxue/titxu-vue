<template>
    <div>
        <el-card>
            <template #header>
                <span>Render 函数自定义列、自定义表头</span>
            </template>
            <Table :columns="tableColumn" :table-data="tableData" @selection-change="handleSelection"
                @command="handleAction">
                <!-- 插槽自定义表头  addressHeader就是tableColumn中地址那一列定义的 -->
                <template #addressHeader="{ column }">
                    <span>{{ column.label }}(插槽自定义表头)</span>
                </template>
            </Table>
        </el-card>
    </div>
</template>

<script setup  name="ViewsAdminUserUserList" lang="ts">
// const { proxy } = getCurrentInstance()
// const router = useRouter()
// const route = useRoute()
import { ElMessageBox, ElMessage, ElTag, ElButton } from 'element-plus'
import dayjs from 'dayjs'
// import { h } from 'vue'
// import Table from '@/components/Table/index.vue'
// 本项目Table组件自动引入，如复制此代码，需根据路径引入Table组件后使用
interface User {
    date: number
    name: string
    address: string
}
const tableData: User[] = [
    {
        date: 1660737564000,
        name: '佘太君',
        address: '上海市普陀区金沙江路 1516 弄'
    },
    {
        date: 1462291200000,
        name: '王小虎',
        address: '上海市普陀区金沙江路 1517 弄'
    },
    {
        date: 1462032000000,
        name: '王小帅',
        address: '上海市普陀区金沙江路 1519 弄'
    },
    {
        date: 1462204800000,
        name: '王小呆',
        address: '上海市普陀区金沙江路 1516 弄'
    }
]
const tableColumn: Table.Column[] = [
    { type: 'selection', width: '50' },
    { type: 'index', width: '50', label: 'No.' },
    { prop: 'name', label: '名字' },
    // 日期使用render函数格式化
    {
        prop: 'date',
        label: '日期',
        headerRender: ({ column }) => h(ElTag, { type: 'danger' }, () => `${column.label}(渲染函数自定义表头)`),
        render: ({ row }: Record<string, User>) => h('span', dayjs(row.date).format('YYYY-MM-DD HH:mm'))
    },
    { prop: 'address', label: '地址', headerSlot: 'addressHeader', showOverflowTooltip: true },
    // 按钮使用render函数渲染
    {
        width: '140',
        label: '操作',
        render: ({ row }: Record<string, User>, scope: any) =>
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
                        onClick: () => handleRenderEdit(row, scope.attrs.index)
                    },
                    { default: () => '编辑' }
                ),
                h(
                    ElButton,
                    {
                        type: 'danger',
                        size: 'small',
                        onClick: () => handleRenderDelete(row, scope.attrs.index)
                    },
                    { default: () => '删除' }
                )
            ])
    }
]
const handleRenderEdit = (row: User, index: number) => {
    ElMessage.success(`${row.name} ----- ${index}`)
}
const handleRenderDelete = (row: User, index: number) => {
    ElMessage.error(`${row.name} ----- ${index}`)
}
const handleSelection = (val: User[]) => {
    console.log('父组件接收的多选数据', val)
}
const handleAction = (command: Table.Command, row: User) => {
    switch (command) {
        case 'edit':
            alert('点击了编辑')
            break
        case 'delete':
            console.log('row', row)
            ElMessageBox.confirm('确认删除吗？', '提示').then(() => {
                ElMessage(JSON.stringify(row))
            })
            break
        default:
            break
    }
}
</script>

<style lang="less" scoped>

</style>