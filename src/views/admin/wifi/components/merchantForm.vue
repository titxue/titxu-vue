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
          <el-form-item prop="name" label="商家名称">
            <el-input v-model="xTable.form.items!.name" type="string" placeholder="请输入商家名称" />
          </el-form-item>
          <el-form-item prop="contactName" label="联系人姓名">
            <el-input v-model="xTable.form.items!.contactName" type="string" placeholder="请输入联系人姓名" />
          </el-form-item>
          <el-form-item prop="contactPhone" label="联系人电话">
            <el-input v-model="xTable.form.items!.contactPhone" type="string" placeholder="请输入联系人电话" />
          </el-form-item>
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="xTable.form.items!.email" type="string" placeholder="请输入邮箱" />
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
  import { MerchantType } from '/@/api/merchants/types';

  const formRef = ref<InstanceType<typeof ElForm>>();
  const xTable = inject('xTable') as xTableClass<MerchantType>;

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
