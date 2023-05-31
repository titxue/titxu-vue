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
          <!-- <el-form-item prop="merchantId" label="商家列表">
            <el-input v-model="xTable.form.items!.merchantId" type="string" placeholder="请输入商家名称" />
          </el-form-item> -->
          <!-- 下拉框 -->
          <el-form-item prop="merchantId" label="商家列表">
            <el-select v-model="xTable.form.items!.merchantId" placeholder="请选择商家" clearable>
              <el-option v-for="item in merchantList" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item prop="wifiName" label="WiFi名称">
            <el-input v-model="xTable.form.items!.wifiName" type="string" placeholder="请输入联系人姓名" />
          </el-form-item>
          <el-form-item prop="wifiPassword" label="WiFi密码">
            <el-input v-model="xTable.form.items!.wifiPassword" type="string" placeholder="请输入联系人电话" />
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
  import { WifiInfoType } from '/@/api/wifiinfo/types';
  import { useWifiStore } from '/@/store';

  const formRef = ref<InstanceType<typeof ElForm>>();
  const xTable = inject('xTable') as xTableClass<WifiInfoType>;
  const wifiStore = useWifiStore();
  const { merchantList } = toRefs(wifiStore);
  // const { setMerchantsvList } = wifiStore;
  // setMerchantsvList();
</script>

<style scoped lang="scss"></style>
