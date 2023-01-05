<template>
  <el-card class="mb-5">
    <template #header> 基本表单 </template>
    <easy-form :fieldList="fieldList" :model="model" @submit="handleBaseSubmit">
      <!-- 如果不使用默认的按钮可以使用插槽自定义内容， 插槽返回的model就是当前表单的数据 -->
      <!-- <template #buttons="{ model }">
                  <el-button">提交</el-button>
              </template> -->
    </easy-form>
  </el-card>
</template>
<script lang="ts" setup>
  import { exampleForm } from '/@/config/form';
  // import { ref } from 'vue'
  // import EasyForm from '@/components/EasyForm/index.vue'
  // 本项目EasyForm组件自动引入，如复制此代码，需根据路径引入Form组件后使用
  const fieldList: Form.FieldItem[] = exampleForm.base;
  const model = ref<Record<string, any>>({
    name: '张三',
    gender: 1,
    hobbies: [1],
    job: 3,
    readonly: '只读输入框',
    summary: '尤雨溪懂个锤子vue是什么梗',
  });
  /**
   * 注意： model数据模型非必填项，如果仅仅是用于数据收集，model参数可以不用填，表单的submit事件会返回所有搜集的数据对象
   *       如果是编辑的情况下，页面需要回显数据，则model数据模型必须要填写
   */
  const handleBaseSubmit = (model: Record<string, any>) => {
    console.log(model);
  };
</script>
<style lang="scss" scoped></style>
