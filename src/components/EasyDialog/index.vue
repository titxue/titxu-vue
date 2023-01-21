<template>
  <el-dialog
    v-model="dialogVisible"
    :fullscreen="fullscreen || false"
    :title="title"
    :width="width || '50%'"
    :draggable="draggable || true"
    v-bind="_options"
  >
    <el-form
      class="px-15 pb-10"
      @submit.prevent
      :model="form"
      ref="formRef"
      label-position="right"
      label-width="80px"
      style="max-width: 100%"
    >
      <template v-for="(item, index) in fieldList" :key="index">
        <!-- 单选框 -->
        <el-form-item :label="item.label" v-if="item.type === 'radio' && index !== undefined" :rules="item.rules" :prop="[item.field]">
          <el-radio-group v-model="form[item.field]" :disabled="item.disabled">
            <el-radio
              :label="val[item.options?.valueKey || 'value']"
              size="default"
              v-for="val in item.options?.data"
              :key="val[item.options?.valueKey || 'value']"
              border
            >
              {{ val[item.options?.labelkey || 'label'] }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 复选框 -->
        <el-form-item :label="item.label" v-else-if="item.type === 'checkbox'" :rules="item.rules" :prop="[item.field]">
          <el-checkbox-group v-model="form[item.field]" :disabled="item.disabled">
            <el-checkbox
              v-for="c in item.options?.data"
              :key="c[item.options?.valueKey || 'value']"
              :label="c[item.options?.valueKey || 'value']"
              >{{ c[item.options?.labelkey || 'label'] }}</el-checkbox
            >
          </el-checkbox-group>
        </el-form-item>
        <!-- 下拉框 -->
        <el-form-item :label="item.label" v-else-if="item.type === 'select'" :rules="item.rules" :prop="[item.field]">
          <!-- <EasySelect
                  v-model="model[item.field]"
                  clearable
                  :disabled="item.disabled"
                  :label-key="item.options?.labelkey"
                  :value-key="item.options?.valueKey"
                  :select-data="item.options?.data" /> -->
          <el-select
            v-model="form[item.field]"
            :multiple="item.options?.multiple === undefined ? true : item.options?.multiple"
            :placeholder="item.options?.placeholder || '请选择'"
            :clearable="item.clearable"
          >
            <el-option
              v-for="s in item.options?.data"
              :key="s[item.options?.valueKey || 'value']"
              :label="s[item.options?.labelkey || 'label']"
              :value="s[item.options?.valueKey || 'value']"
            />
          </el-select>
        </el-form-item>
        <!-- 默认输入框 -->
        <el-form-item :label="item.label" :rules="item.rules" :prop="[item.field]" v-else>
          <el-input
            v-model="form[item.field]"
            :readonly="item.readonly"
            :type="item.type ?? 'text'"
            :placeholder="item.placeholder || item.label"
            :disabled="item.disabled"
            :showPassword="item.showPassword"
            :clearable="item.clearable"
            @keyup.enter="handleKeyUp(item.enterable)"
          />
        </el-form-item>
      </template>

      <el-form-item />
    </el-form>
    <template #footer>
      <span class="px-15">
        <slot name="buttons" :model="form" :formRef="formRef">
          <el-button v-if="_options.showCancelButton" @click="emit('cancel')">
            {{ _options.cancelButtonText }}
          </el-button>
          <el-button type="primary" @click="onSubmit(formRef)">{{ _options.submitButtonText }}</el-button>
          <el-button v-if="_options.showResetButton" type="info" @click="resetForm(formRef)">
            {{ _options.resetButtonText }}
          </el-button>
        </slot>
      </span>
    </template>
  </el-dialog>
</template>
<script lang="ts" setup>
  import type { FormInstance } from 'element-plus';
  import { ComputedRef, ref, computed } from 'vue';

  // 父组件传递的值
  interface Props {
    fieldList: Form.FieldItem[];
    model?: Record<string, any>;
    options?: Form.Options;
    visible: boolean;
    width?: string | number;
    // 弹框标题
    title: string;
    // 是否为全屏 Dialog
    fullscreen?: boolean;
    // 为 Dialog 启用可拖拽功能
    draggable?: boolean;
    // Dialog 自身是否插入至 body 元素上。 嵌套的 Dialog 必须指定该属性并赋值为 true
    'append-to-body'?: boolean;
    // 是否可以通过点击 modal 关闭 Dialog
    'close-on-click-modal'?: boolean;
  }

  const dialogVisible = ref(false);
  // 表单的数据
  const form = ref<Record<string, any>>({});
  const formRef = ref<FormInstance>();
  const props = defineProps<Props>();
  // 设置option默认值，如果传入自定义的配置则合并option配置项
  const _options: ComputedRef<Form.Options> = computed(() => {
    const option = {
      labelPosition: 'right',
      disabled: false,
      submitButtonText: '提交',
      resetButtonText: '重置',
      cancelButtonText: '取消',
    };
    return Object.assign(option, props?.options);
  });
  interface EmitEvent {
    (e: 'submit', params: any): void;
    (e: 'reset'): void;
    (e: 'cancel'): void;
    (e: 'update:visible', params: boolean): void;
  }
  const emit = defineEmits<EmitEvent>();
  defineExpose({
    formRef,
  });

  // 监听父组件的visible，用来简介控制el-dialog的弹框开关
  watch(
    () => props.visible,
    (n, _o) => {
      dialogVisible.value = n;
    },
  );
  // 监听el-dialog显示状态，再通过@update:visible 通知父组件，一般是用于关
  watch(dialogVisible, (n) => {
    emit('update:visible', n);
  });

  // 每次触发，就证明父组件点了修改或者添加的按钮，传递了一个新的formData
  // 需要重新给form 赋值，并且，对该表单项进行重置
  watch(
    () => props.model,
    (n, _o) => {
      console.log('fieldList', n);
      resetForm(formRef.value);
      if (n) {
        form.value = reactive(n);
      }
    },
  );

  // const model = ref<Record<string, any>>({})
  // 根据fieldList初始化model， 如果model有传值就用传递的model数据模型，否则就给上面声明的model设置相应的(key,value) [item.field]， item.value是表单的默认值（选填）
  props.fieldList.map((item: Form.FieldItem) => {
    // 如果类型为checkbox，默认值需要设置一个空数组
    const value = item.type === 'checkbox' ? [] : '';
    props.model ? (form.value = props.model) : (form.value[item.field] = item.value || value);
  });
  // 提交按钮
  const onSubmit = (formEl: FormInstance | undefined) => {
    if (!formEl) return;
    formEl.validate((valid) => {
      dialogVisible.value = false;
      if (valid) {
        emit('submit', form.value);
      } else {
        return false;
      }
    });
  };
  // 输入框回车事件
  const handleKeyUp = (enterable: boolean | undefined) => {
    if (!enterable) return;
    onSubmit(formRef.value);
  };
  // 重置
  const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return;
    formEl.resetFields();
  };
  onMounted(() => {
    // 初始化 配置弹框是否可显示
    dialogVisible.value = props.visible;
    // 初始化 配置formData
    if (props.model) {
      form.value = reactive(props.model);
    }
  });
</script>
<style lang="less" scoped>
  // ::v-deep(.el-dialog__footer){
  //   width: 100% !important;
  //   box-shadow: 0px 12px 32px 4px rgba(0, 0, 0, .36), 0px 8px 20px rgba(0, 0, 0, .72) !important;
  //   padding: 10px 20px;
  //   position: absolute !important;
  //   bottom: 0;
  // }

  .el-select {
    width: 100%;
  }
</style>
