<template>
  <el-aside width="210px">
    <el-menu class="el-menu-vertical-demo" default-active="sys" @open="handleOpen" @close="handleClose">
      <h3>{{ false ? '后台' : '后台管理' }}</h3>
      <el-menu-item :index="item.menuUrl + ''" v-for="item in noChildern()" :key="item.id" @click="handleClick">
        <Icon class="icons" :name="item.menuIcon" />
        <!-- <template #title>{{item.label}}</template> -->
        <span>{{ item.permissionName }}</span>
      </el-menu-item>
      <el-sub-menu :index="item.menuUrl + ''" v-for="item in hasChildern()" :key="item.id">
        <template #title>
          <Icon class="icons" :name="item.menuIcon" />
          <span>{{ item.permissionName }}</span>
        </template>
        <el-menu-item-group v-for="subItem in item.subList" :index="subItem.menuUrl + ''" :key="subItem.id">
          <el-menu-item :index="item.menuUrl + '/' + subItem.menuUrl" @click="handleClick">
            <Icon class="icons" :name="subItem.menuIcon" />
            <span>{{ subItem.permissionName }}</span>
          </el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>
    </el-menu>
  </el-aside>
</template>

<script setup lang="ts">
  import { MenuItemRegistered } from 'element-plus/es/components/menu/src/types';
  import { usePermissionStore } from '/@/store';
  import Icon from '/@/components/v1/icon/index.vue';

  const router = useRouter();
  const route = useRoute();
  const permissionStore = usePermissionStore();

  const { setNavList } = permissionStore;
  const { navList } = toRefs(permissionStore);

  const handleOpen = (key: string, keyPath: string[]) => {
    console.log(key, keyPath);
  };
  const handleClose = (key: string, keyPath: string[]) => {
    console.log(key, keyPath);
  };

  const handleClick = (menuItem: MenuItemRegistered) => {
    router.push('/admin/' + menuItem.index);
  };

  // 返回没有子菜单的菜单项
  const noChildern = () => {
    return navList.value.filter((item) => !item.subList);
  };

  // 返回有子菜单的菜单项
  const hasChildern = () => {
    return navList.value.filter((item) => item.subList);
  };
  // 获取权限菜单
  const initPermission = async () => {
    route.meta.title = `后台管理`;
    await setNavList();
    // console.log('permissionStore', permissionStore.menuList);
  };
  // vue3 生命周期
  onMounted(() => {
    initPermission();
  });
</script>

<style lang="scss" scoped>
  .icons {
    width: 18px;
    height: 18px;
    margin-right: 10px;
  }

  // .el-menu-item-group {
  //   background-color: rgba(34, 45, 60, 1);
  // }
  ::v-deep(.el-menu) {
    // 填满父元素
    flex: 1;
  }

  .el-aside {
    background: var(--ba-bg-color-overlay);
    margin: 16px 0 16px 16px;
    height: calc(100vh - 32px);
    box-shadow: var(--el-box-shadow-light);
    border-radius: var(--el-border-radius-base);
    overflow: hidden;
    transition: width 0.3s ease;
    width: 260px;
  }

  .el-menu-vertical-demo {
    width: 100%;
    border-right: none;

    h3 {
      //color: rgb(70, 163, 221);
      text-align: center;
      margin-top: 10px;
    }
  }
</style>
