<template>
  <!-- 对话框表单 -->
  <el-dialog
    class="ba-operate-dialog"
    :close-on-click-modal="false"
    :model-value="!!xTable.form.operate"
    @close="xTable.toggleForm"
    :destroy-on-close="true"
  >
    <template #header>
      <div class="title">
        {{ xTable.form.operate ? xTable.form.operate : '' }}
      </div>
    </template>
    <el-scrollbar v-loading="!!xTable.form.loading" class="ba-table-form-scrollbar">
      <div
        class="ba-operate-form"
        :class="'ba-' + xTable.form.operate + '-form'"
        :style="'width: calc(100% - ' + xTable.form.labelWidth! / 2 + 'px)'"
      >
        <el-form
          ref="formRef"
          @keyup.enter="xTable.onSubmit(formRef)"
          :model="xTable.form.items"
          label-position="right"
          :label-width="xTable.form.labelWidth + 'px'"
          v-if="!xTable.form.loading"
        >
          <el-form-item prop="userNick" label="用户昵称">
            <el-input v-model="xTable.form.items!.userNick" type="string" placeholder="请输入用户昵称" />
          </el-form-item>

          <el-form-item prop="mobile" label="手机号">
            <el-input v-model="xTable.form.items!.mobile" type="string" placeholder="请输入手机号" />
          </el-form-item>
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="xTable.form.items!.email" type="string" placeholder="请输入邮箱" />
          </el-form-item>
          <!-- 下拉框 -->
          <el-form-item prop="roleIdList" label="角色列表">
            <el-select v-model="xTable.form.items!.roleIdList" placeholder="请选择角色" multiple clearable collapse-tags>
              <el-option v-for="item in state.roleList" :key="item.id" :label="item.roleName" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item prop="status" label="状态">
            <el-radio-group v-model="xTable.form.items!.status">
              <el-radio border label="0">启用</el-radio>
              <el-radio border label="1">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
    </el-scrollbar>
    <template #footer>
      <div :style="'width: calc(100% - ' + xTable.form.labelWidth! / 1.8 + 'px)'">
        <el-button @click="xTable.toggleForm('')">Cancel</el-button>
        <el-button :loading="xTable.form.submitLoading" @click="xTable.onSubmit(formRef)" type="primary">
          {{ xTable.form.operateIds && xTable.form.operateIds.length > 1 ? 'Save and edit next item' : 'Save' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
  import type xTableClass from '/@/utils/xTable';
  import type { ElForm } from 'element-plus';
  import { getRoleAll } from '/@/api/role';
  import { RoleType } from '/@/api/role/types';
  import { UserInfoType } from '/@/api/user/types';

  const formRef = ref<InstanceType<typeof ElForm>>();
  const xTable = inject('xTable') as xTableClass<UserInfoType>;

  const state: {
    roleList: RoleType[];
  } = reactive({
    roleList: [],
  });

  // 获取角色列表
  getRoleAll().then((res) => {
    state.roleList = res.data as RoleType[];
  });
</script>

<style scoped lang="scss"></style>
