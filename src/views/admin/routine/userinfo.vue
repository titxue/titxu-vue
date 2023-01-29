<template>
  <div>
    <el-row>
      <el-col :span="12">
        <el-card class="box-card">
          <div class="box-avatar">
            <el-avatar :size="150" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
          </div>

          <div class="userinfo-form">
            <easy-form :fieldList="fieldList" :model="userInfo" :options="optionsForm" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <div class="grid-content ep-bg-purple-light"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
  import { routineForm } from '/@/config/form';
  import { useUserStore } from '/@/store';
  // const { proxy } = getCurrentInstance()
  // const router = useRouter()
  // const route = useRoute()
  const userStore = useUserStore();
  const { setUserInfo } = userStore;
  const { userInfo } = toRefs(userStore);

  const fieldList = ref(routineForm.userinfo);
  // const userInfo = ref<Record<string, string>>();

  interface State {
    optionsForm: Form.Options;
  }
  const state = reactive<State>({
    optionsForm: {
      labelPosition: 'top',
      showResetButton: true,
      submitButtonText: '保存',
      resetButtonText: '重置',
    },
  });
  const { optionsForm } = toRefs(state);

  onMounted(() => {
    setUserInfo();
  });
</script>

<style lang="less" scoped>
  .box-card {
    .box-avatar {
      display: flex;
      justify-content: center;
    }

    .userinfo-form {
      margin-top: 50px;
    }
  }
</style>
