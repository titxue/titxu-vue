<template>
  <div>
    <el-table ref="tableRef" :data="tableData" v-bind="_options" @selection-change="handleSelectionChange"
      @row-click="handleRowClick" @cell-click="handleCellClick" @sort-change="handleSortChange">
      <template v-for="(col, index) in columns" :key="index">
        <!---复选框, 序号 (START)-->
        <el-table-column
          v-if="col.type === 'index' || col.type === 'selection' || col.type === 'expand' && index !== undefined"
          :sortable="col.sortable" :prop="col.prop" :align="col.align" :label="col.label" :type="col.type"
          :index="indexMethod" :width="col.width" />
        <!---复选框, 序号 (END)-->
        <TableColumn :col="col" v-else @command="handleAction">
          <!-- 自定义表头插槽 -->
          <template #customHeader="{ slotName, column, index }">
            <slot :name="slotName" :column="column" :index="index" />
          </template>
          <!-- 自定义列插槽 -->
          <template #default="{ slotName, row, index }">
            <slot :name="slotName" :row="row" :index="index" />
          </template>
        </TableColumn>
      </template>
    </el-table>
    <!-- 分页器 -->
    <div v-if="_options.showPagination" class="mt20">
      <el-pagination v-bind="_paginationConfig" @size-change="pageSizeChange" @current-change="currentPageChange" />
    </div> 
  </div>
</template>
<script lang="ts" setup>
import { ComputedRef, computed } from 'vue'
import type { TableColumnCtx } from 'element-plus/es/components/table/src/table-column/defaults'
import TableColumn from './TableColumn.vue'
import { ElTable } from 'element-plus'
export type SortParams<T> = {
  column: TableColumnCtx<T | any>
  prop: string
  order: Table.Order
}
interface TableProps {
  tableData: Array<object> // table的数据
  columns: Table.Column[] // 每列的配置项
  options?: Table.Options
}
const props = defineProps<TableProps>()
const tableRef = ref<InstanceType<typeof ElTable>>()
// 设置option默认值，如果传入自定义的配置则合并option配置项
const _options: ComputedRef<Table.Options> = computed(() => {
  const option = {
    stripe: false,
    tooltipEffect: 'dark',
    showHeader: true,
    showPagination: false,
    rowStyle: () => 'cursor:pointer' // 行样式
  }
  return Object.assign(option, props?.options)
})
// 合并分页配置
const _paginationConfig = computed(() => {
  const config = {
    total: 0,
    currentPage: 1,
    pageSize: 10,
    pageSizes: [1,10, 20, 30, 40, 50, 100],
    layout: 'total, sizes, prev, pager, next, jumper'
  }
  return Object.assign(config, _options.value.paginationConfig)
})
interface EmitEvent {
  (e: 'selection-change', params: any): void // 当选择项发生变化时会触发该事件
  (e: 'row-click', row: any, column: TableColumnCtx<any>, event: Event): void // 当某一行被点击时会触发该事件
  (e: 'cell-click', row: any, column: TableColumnCtx<any>, cell: any, event: Event): void // 当某个单元格被点击时会触发该事件
  (e: 'command', command: Table.Command, row: any): void // 按钮组事件
  (e: 'size-change', pageSize: number): void // pageSize事件
  (e: 'current-change', currentPage: number): void // currentPage按钮组事件
  (e: 'pagination-change', currentPage: number, pageSize: number): void // currentPage或者pageSize改变触发
  (e: 'sort-change', params: SortParams<any>): void // 列排序发生改变触发
}
const emit = defineEmits<EmitEvent>()
// 自定义索引
const indexMethod = (index: number) => {
  const tabIndex = index + (_paginationConfig.value.currentPage - 1) * _paginationConfig.value.pageSize + 1
  return tabIndex
}
// 切换pageSize
const pageSizeChange = (pageSize: number) => {
  emit('size-change', pageSize)
  emit('pagination-change', 1, pageSize)
}
// 切换currentPage
const currentPageChange = (currentPage: number) => {
  emit('current-change', currentPage)
  emit('pagination-change', currentPage, _paginationConfig.value.pageSize)
}
// 按钮组事件
const handleAction = (command: Table.Command, scope: any) => {
  emit('command', command, scope.row)
}
// 多选事件
const handleSelectionChange = (val: any) => {
  emit('selection-change', val)
}
// 当某一行被点击时会触发该事件
const handleRowClick = (row: any, column: any, event: Event) => {
  emit('row-click', row, column, event)
}
// 当某个单元格被点击时会触发该事件
const handleCellClick = (row: any, column: any, cell: any, event: Event) => {
  emit('cell-click', row, column, cell, event)
}
/**
  *  当表格的排序条件发生变化的时候会触发该事件
   * 在列中设置 sortable 属性即可实现以该列为基准的排序， 接受一个 Boolean，默认为 false。
   * 可以通过 Table 的 default-sort 属性设置默认的排序列和排序顺序。 
   * 如果需要后端排序，需将 sortable 设置为 custom，同时在 Table 上监听 sort-change 事件，
   * 在事件回调中可以获取当前排序的字段名和排序顺序，从而向接口请求排序后的表格数据。
    */
const handleSortChange = ({ column, prop, order }: SortParams<any>) => {
  emit('sort-change', { column, prop, order })
}
// 暴露给父组件参数和方法，如果外部需要更多的参数或者方法，都可以从这里暴露出去。
defineExpose({ element: tableRef })
</script>
<style lang="less" scoped>
:deep(.el-image__inner) {
  transition: all 0.3s;
  cursor: pointer;
  &:hover {
    transform: scale(1.2);
  }
}
:deep(.hidden-columns) {
  display: none;
}
:deep(.cell) {
  // 居中
  display: flex;
  align-items: center;
}
</style>

