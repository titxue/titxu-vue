<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="10" class="ba-array-key">键名</el-col>
      <el-col :span="10" class="ba-array-value">键值</el-col>
    </el-row>
    <el-row class="ba-array-item" v-for="(item, _idx) in state.value" :gutter="10" :key="_idx">
      <el-col :span="10">
        <el-input v-model="item.key" />
      </el-col>
      <el-col :span="10">
        <el-input v-model="item.value" />
      </el-col>
      <el-col :span="4">
        <el-button @click="onDelArrayItem(_idx)" size="small" icon="el-icon-Delete" circle />
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="10" :offset="10">
        <el-button class="ba-add-array-item" @click="onAddArrayItem" icon="el-icon-Plus">添加</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
  import { reactive, watch } from 'vue';

  type xInputArray = { key: string; value: string };
  interface Props {
    modelValue: xInputArray[];
  }

  const props = withDefaults(defineProps<Props>(), {
    modelValue: () => [],
  });

  const state = reactive({
    value: props.modelValue,
  });

  /* const emits = defineEmits<{
    (e: 'update:modelValue', value: xInputArray[]): void
}>()
const onInput = () => {
    emits('update:modelValue', state.value)
} */

  const onAddArrayItem = () => {
    state.value.push({
      key: '',
      value: '',
    });
    // emits('update:modelValue', state.value)
  };

  const onDelArrayItem = (_idx: number) => {
    state.value.splice(_idx, 1);
  };

  watch(
    () => props.modelValue,
    (newVal) => {
      state.value = newVal;
    },
  );
</script>

<style scoped lang="scss">
  .ba-array-key,
  .ba-array-value {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 5px 0;
    color: var(--el-text-color-secondary);
  }

  .ba-array-item {
    margin-bottom: 6px;
  }

  .ba-add-array-item {
    float: right;
  }
</style>
