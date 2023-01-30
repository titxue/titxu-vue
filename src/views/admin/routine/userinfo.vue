<template>
  <div>
    <el-row>
      <el-col :span="12">
        <el-card class="box-card">
          <div class="box-avatar">
            <el-avatar :size="150" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
          </div>

          <div class="userinfo-form">
            <easy-form :fieldList="fieldList" :model="userInfo" :options="optionsForm" @submit="handleUpdateUserSubmit" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card log-card">
          <template #header>
            <div class="card-header">
              <span>操作日志</span>
              <el-button class="button" text @click="setLogList()">刷新日志</el-button>
            </div>
          </template>
          <div class="card-centent">
            <div class="timeline-centent">
              <el-timeline>
                <el-timeline-item
                  v-for="(activity, index) in logList"
                  :key="index"
                  :timestamp="dayjs(activity.createTime).format('YYYY-MM-DD HH:mm:ss')"
                >
                  {{ activity.operation }}
                </el-timeline-item>
              </el-timeline>
            </div>
            <div class="pagination-centent">
              <el-pagination
                v-model:current-page="page.currPage"
                :page-size="page.pageSize"
                :pageCount="page.totalPage"
                layout="total, prev, pager, next"
                :total="page.totalCount"
                @size-change="sizeChangeLog"
                @current-change="currentChangeLog"
              />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
  import dayjs from 'dayjs';
  import { ElMessage } from 'element-plus';
  import { UserInfoType } from '/@/api/user/types';
  import { routineForm } from '/@/config/form';
  import { useLogStore, useUserStore } from '/@/store';

  // const { proxy } = getCurrentInstance()
  // const router = useRouter()
  // const route = useRoute()
  const userStore = useUserStore();
  const logStore = useLogStore();
  const { setUserInfo, updateUser } = userStore;
  const { userInfo } = toRefs(userStore);
  const { setLogList } = logStore;
  const { logList, page } = toRefs(logStore);

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

  const currentChangeLog = (page: number) => {
    console.log('currentChangeLog', page);
    setLogList(page.toString());
  };
  const sizeChangeLog = (page: any) => {
    console.log('sizeChangeLog' + page);
  };

  const handleUpdateUserSubmit = async (data: Record<string, UserInfoType>) => {
    const userInfo = data as unknown as UserInfoType;
    await updateUser(userInfo);
    // 重新获取用户信息
    setUserInfo();
    ElMessage.success('更新成功');
  };

  onMounted(() => {
    setUserInfo();
    setLogList();
  });
</script>

<style lang="less" scoped>
  .log-card {
    margin-left: 30px;
  }

  .box-card {
    height: 100%;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .box-avatar {
      display: flex;
      justify-content: center;
      margin-top: 20px;
    }

    .userinfo-form {
      margin-top: 60px;
    }

    .card-centent {
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
    }
  }
</style>
