<template>
  <el-aside width="210px">
    <el-menu class="el-menu-vertical-demo" default-active="sys" @open="handleOpen" @close="handleClose">
      <h3>{{ false ? '后台' : '后台管理' }}</h3>
      <el-menu-item :index="item.id + ''" v-for="item in noChildern()" :key="item.permissionName" @click="handleClick(item)">
        <component class="icons" :is="item.menuIcon" />
        <!-- <template #title>{{item.label}}</template> -->
        <span>{{ item.permissionName }}</span>
      </el-menu-item>
      <el-sub-menu :index="item.id + ''" v-for="item in hasChildern()" :key="item.permissionName">
        <template #title>
          <component class="icons" :is="item.menuIcon" />
          <span>{{ item.permissionName }}</span>
        </template>
        <el-menu-item-group v-for="(subItem, subIndex) in item.subList" :index="subItem.menuUrl + ''" :key="subItem.permissionName">
          <el-menu-item :index="subIndex + ''" @click="handleClick(subItem)">
            <component class="icons" :is="subItem.menuIcon" />
            <span>{{ subItem.permissionName }}</span>
          </el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>
    </el-menu>
  </el-aside>
</template>

<script setup lang="ts">
  import { usePermissionStore } from '/@/store';
  import { Menu } from '/@/store/modules/permission/types';

  const router = useRouter();
  const route = useRoute();
  const permissionStore = usePermissionStore();
  // const list = [
  //   {
  //     path: '/home',
  //     name: 'home',
  //     label: '首页',
  //     icon: 'house',
  //     url: '/home'
  //   },
  //   {
  //     label: '系统管理',
  //     name: 'system',
  //     icon: 'menu',
  //     path: '/sys',
  //     children: [
  //       {
  //         path: '/sys/user',
  //         name: 'user',
  //         label: '用户管理',
  //         icon: 'user',
  //         url: '/sys/user'
  //       },
  //       {
  //         path: '/sys/role',
  //         name: 'role',
  //         label: '角色管理',
  //         icon: 'UserFilled',
  //         url: '/sys/role'
  //       },
  //     ]
  //   },
  // ];
  const handleOpen = (key: string, keyPath: string[]) => {
    console.log(key, keyPath);
  };
  const handleClose = (key: string, keyPath: string[]) => {
    console.log(key, keyPath);
  };

  const handleClick = (item: any) => {
    console.log('aside item', item);
    // console.log('router', router.getRoutes());
    router.push('/admin/' + item.menuUrl);
    route.meta.title = `${item.parentName}-${item.permissionName}`;
  };

  // 返回没有子菜单的菜单项
  const noChildern = () => {
    const menu: Array<Menu> | undefined = permissionStore.permissionsProfile.menuList;
    return menu?.filter((item) => !item.subList);
  };

  // 返回有子菜单的菜单项
  const hasChildern = () => {
    const menu: Array<Menu> | undefined = permissionStore.permissionsProfile.menuList;
    return menu?.filter((item) => item.subList);
  };
  // 获取权限菜单
  const initPermission = async () => {
    await permissionStore.info();
    console.log('permissionStore', permissionStore.permissionsProfile.menuList);
  };
  // vue3 生命周期
  onMounted(() => {
    initPermission();
  });
</script>

<style lang="less" scoped>
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
    background-color: #ffffff;
    margin: 16px 0 16px 16px;
    // height: calc(100vh - 32px);
    box-shadow: 0px 0px 12px rgba(0, 0, 0, 0.12);
    border-radius: 4px;
    overflow: hidden;
    transition: width 0.3s ease;
    width: 260px;
  }

  .el-menu-vertical-demo {
    width: 100%;
    border-right: none;

    h3 {
      color: rgb(70, 163, 221);
      text-align: center;
      margin-top: 10px;
    }
  }
</style>
