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
    <el-scrollbar v-loading="xTable.form.loading" class="ba-table-form-scrollbar">
      <div
        class="ba-operate-form"
        :class="'ba-' + xTable.form.operate + '-form'"
        :style="'width: calc(100% - ' + xTable.form.labelWidth! / 2 + 'px)'"
      >
        <el-form
          ref="formRef"
          @keyup.enter="xTable.onSubmit(formRef)"
          :model="xTable.form.items"
          :rules="rules"
          label-position="right"
          :label-width="xTable.form.labelWidth + 'px'"
          v-if="!xTable.form.loading"
        >
          <el-form-item prop="roleName" label="角色名称">
            <el-input v-model="xTable.form.items!.roleName" type="string" placeholder="请输入角色名称" />
          </el-form-item>

          <el-form-item prop="roleCode" label="角色编码">
            <el-input v-model="xTable.form.items!.roleCode" type="string" placeholder="请输入角色编码" />
          </el-form-item>
          <el-form-item prop="permissionIdList" label="角色权限">
            <el-tree
              ref="treeRef"
              :key="state.treeKey"
              :default-checked-keys="state.defaultCheckedKeys"
              :default-expand-all="true"
              show-checkbox
              node-key="id"
              :props="{ children: 'subList', label: 'permissionName', class: treeNodeClass }"
              :data="state.permissionTree"
            />
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
        <el-button @click="xTable.toggleForm('')">'Cancel</el-button>
        <el-button :loading="xTable.form.submitLoading" @click="xTable.onSubmit(formRef)" type="primary">
          {{ xTable.form.operateIds && xTable.form.operateIds.length > 1 ? 'Save and edit next item' : 'Save' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
  import type xTableClass from '/@/utils/xTable';
  import type { ElForm, ElTree } from 'element-plus';
  import { uuid } from '/@/utils/random';
  import { RoleResultType } from '/@/api/role/types';
  import { getPermissionTree } from '/@/api/permission';
  import { PermissionType } from '/@/api/permission/types';
  import type Node from 'element-plus/es/components/tree/src/model/node';

  const treeRef = ref<InstanceType<typeof ElTree>>();
  const formRef = ref<InstanceType<typeof ElForm>>();
  const xTable = inject('xTable') as xTableClass<RoleResultType>;

  const state: {
    treeKey: string;
    defaultCheckedKeys: string[];
    permissionTree: PermissionType[];
  } = reactive({
    treeKey: uuid(),
    defaultCheckedKeys: [],
    permissionTree: [],
  });

  const treeNodeClass = (data: anyObj, node: Node) => {
    if (node.isLeaf && data.open) return '';
    let addClass = true;
    for (const key in node.childNodes) {
      if (!node.childNodes[key].isLeaf) {
        addClass = false;
      }
    }
    return addClass ? 'penultimate-node' : '';
  };

  const rules: Partial<Record<string, any[]>> = reactive({
    roleName: [{ name: 'required', title: 'auth.group.Group name' }],
    permissionIdList: [
      {
        required: true,
        validator: (_rule: any, _val: string, callback: Function) => {
          let ids = getCheckeds();
          if (ids.length <= 0) {
            return callback(new Error('Please select field'));
          }
          return callback();
        },
      },
    ],
  });

  getPermissionTree().then((res) => {
    state.permissionTree = res.data as PermissionType[];
  });

  watch(
    () => xTable.form.items!.id,
    () => {
      if (xTable.form.items && xTable.form.items) {
        if (xTable.form.items!.id === '1') {
          console.log('管理员');
          let arr: string[] = [];
          for (const key in state.permissionTree) {
            arr.push(state.permissionTree[key].id);
          }
          state.defaultCheckedKeys = arr;
        } else {
          // for (const key in xTable.form.items.permissionIdList) {
          //     for (const key1 in state.permissionTree) {
          //         if (xTable.form.items.permissionIdList[key] === state.permissionTree[key1].id) {
          //             if(state.permissionTree[key1].open){
          //                 console.log("不需要");
          //                xTable.form.items.permissionIdList.splice(key,1)
          //             }
          //         }
          //     }

          // }
          state.defaultCheckedKeys = xTable.form.items.permissionIdList;
        }
      }

      state.treeKey = uuid();
    },
  );
  const getCheckeds = () => {
    return treeRef.value!.getCheckedKeys();
  };

  defineExpose({
    getCheckeds,
  });
</script>

<style scoped lang="scss">
  :deep(.penultimate-node) {
    .el-tree-node__children {
      padding-left: 60px;
      white-space: pre-wrap;
      line-height: 12px;

      .el-tree-node {
        display: inline-block;
      }

      .el-tree-node__content {
        padding-left: 5px !important;
        padding-right: 5px;

        .el-tree-node__expand-icon {
          display: none;
        }
      }
    }
  }
</style>
