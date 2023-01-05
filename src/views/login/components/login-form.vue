<template>
  <div class="login-form-wrapper">
    <div class="login-form-title">欢迎登录</div>
    <div class="login-form-sub-title">后台登陆</div>
    <div class="login-form-error-msg">{{ errorMessage }}</div>
    <el-form ref="ruleFormRef" :model="userFormData" class="login-form" layout="vertical" :rules="rules">
      <el-form-item
        prop="mobile"
        :rules="[{ required: true, message: '手机号不能为空' }]"
        :validate-trigger="['change', 'blur']"
        hide-label
      >
        <el-input v-model="userFormData.mobile" placeholder="saodimangseng" />
      </el-form-item>
      <el-form-item
        prop="password"
        :rules="[{ required: true, message: '密码不能为空' }]"
        :validate-trigger="['change', 'blur']"
        hide-label
      >
        <el-input v-model="userFormData.password" placeholder="密码：saodimangseng" allow-clear />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit(ruleFormRef)">登录</el-button>
        <el-button @click="resetForm(ruleFormRef)">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
  import { ElMessage, FormInstance, FormRules } from 'element-plus';
  import { useUserStore } from '/@/store';

  const router = useRouter();
  const errorMessage = ref('');
  const userStore = useUserStore();
  const userFormData = reactive({
    mobile: '18666666666',
    password: '123456',
  });

  const ruleFormRef = ref<FormInstance>();
  const rules = reactive<FormRules>({
    username: [
      {
        required: true,
        message: '手机号不能为空',
      },
    ],
    password: [
      {
        required: true,
        message: '密码不能为空',
      },
    ],
  });
  const handleSubmit = async (formEl: FormInstance | undefined) => {
    console.log(formEl);
    if (!formEl) return;
    await formEl.validate(async (valid) => {
      if (valid) {
        ElMessage.success('欢迎使用');
        await userStore.login(userFormData);
        router.push('/admin');
      } else {
        ElMessage.error('错误信息');
      }
    });
  };
  const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return;
    formEl.resetFields();
  };
</script>

<style lang="less" scoped>
  .login-form {
    &-wrapper {
      width: 320px;
    }

    &-title {
      color: var(--color-text-1);
      font-weight: 500;
      font-size: 24px;
      line-height: 32px;
    }

    &-sub-title {
      color: var(--color-text-3);
      font-size: 16px;
      line-height: 24px;
    }

    &-error-msg {
      height: 32px;
      color: rgb(var(--red-6));
      line-height: 32px;
    }

    &-password-actions {
      display: flex;
      justify-content: space-between;
    }

    &-register-btn {
      color: var(--color-text-3) !important;
    }
  }
</style>
