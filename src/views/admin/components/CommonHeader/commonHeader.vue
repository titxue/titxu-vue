<template>
  <el-header class="header">
    <div class="nav-tab">
      <el-tabs v-model="navTabStore.activeTab" type="card" class="demo-tabs" closable @tab-change="tabChange" @tab-remove="removeTab">
        <el-tab-pane v-for="item in navTabStore.tabList" :key="item.path" :label="item.title" :name="item.path" />
      </el-tabs>
    </div>
    <div class="nav-menus">
      <el-dropdown>
        <span class="user-info">
          <img class="user" :src="getImgSrc()" alt="头像" />
          <span class="user-name">{{ userInfo.userNick === undefined || '' ? '' : userInfo.userNick }}</span>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="skipUserInfo">我的</el-dropdown-item>
            <el-dropdown-item @click="logout()">退出</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <div class="x-tabs-icon" @click="openDrawer">
        <Icon name="el-icon-Setting" size="20" class="close-icon" />
        <Config />
      </div>
    </div>
  </el-header>
</template>

<script setup lang="ts">
  import { ElMessage, TabPaneName } from 'element-plus';
  import Config from '/@/views/admin/components/CommonHeader/config.vue';
  import Icon from '/@/components/v1/icon/index.vue';
  import { useUserStore, useAuthStore, useNavTabStore, usePermissionStore, useConfigStore } from '/@/store';

  const router = useRouter();
  const authStore = useAuthStore();
  const userStore = useUserStore();
  const navTabStore = useNavTabStore();
  const permissionStore = usePermissionStore();
  const configStore = useConfigStore();
  const { userLogout } = authStore;
  const { setUserInfo } = userStore;
  const { userInfo } = toRefs(userStore);

  const openDrawer = () => {
    configStore.setLayout('showDrawer', true);
  };

  // 跳转到个人信息
  const skipUserInfo = () => {
    router.push('/admin/routine/userinfo');
  };

  // 点击标签跳转路由
  const tabChange = (tab: TabPaneName) => {
    router.push(tab as string);
    navTabStore.activeTab = tab as string;
  };

  //添加标签导航
  //  function addTab(tab:any) {
  //     const notTab = tabList.value.findIndex((e) => e.path == tab.path) == -1;
  //     if (notTab) {
  //       tabList.value.push(tab);
  //     }
  //   }

  // 删除标签
  const removeTab = (targetName: string) => {
    const tabs = navTabStore.tabList;
    let activeName = navTabStore.activeTab;
    if (activeName === targetName) {
      tabs.forEach((tab, index) => {
        if (tab.path === targetName) {
          const nextTab = tabs[index + 1] || tabs[index - 1];
          if (nextTab) {
            activeName = nextTab.path;
          }
        }
      });
    }

    navTabStore.activeTab = activeName;
    navTabStore.tabList = tabs.filter((tab) => tab.path !== targetName);
  };

  const logout = async () => {
    userLogout()
      .then(() => {
        router.push('/login');
        ElMessage.success('退出登陆成功');
      })
      .catch((error) => {
        console.log(error);
      });
    // router.push('/login');
  };

  let getImgSrc = () => {
    // 参考https://cn.vitejs.dev/guide/assets.html#new-url-url-import-meta-url
    // console.log(import.meta.url)
    // console.log(new URL("../assets/images/user.png", import.meta.url))
    return new URL('../../../../assets/images/user.jpg', import.meta.url).href;
  };
  //添加标签导航
  function addTab(tab: { title: string; path: string }) {
    const notTab = navTabStore.tabList.findIndex((e) => e.path == tab.path) == -1;
    if (notTab) {
      navTabStore.tabList.push(tab);
    }
  }

  //获取路由跳转后的tab数据
  onBeforeRouteUpdate((p) => {
    navTabStore.activeTab = p.path;
    addTab({
      title: p.meta.title as string,
      path: p.path,
    });
  });
  function setCurrentRoute() {
    const currentRoute = router.currentRoute.value;
    navTabStore.activeTab = currentRoute.path;
    addTab({
      title: currentRoute.meta.title as string,
      path: currentRoute.path,
    });
  }

  function setRouteMeta() {
    const store = usePermissionStore();
    const routers = router.getRoutes();
    const menuList = store.selectMenu;
    for (let i = 0; i < routers.length; i++) {
      const element = routers[i];
      if (element.meta.title === undefined) {
        for (let j = 0; j < menuList.length; j++) {
          const menu = menuList[j];
          const path = element.path.split('/');
          if (path[path.length - 1] === menu.menuUrl) {
            element.meta.title = menu.permissionName;
            console.log(element.path);
          }
        }
      }
    }
  }
  onMounted(async () => {
    try {
      await permissionStore.setSelectMenu();
      await setUserInfo();
      await setRouteMeta();
      await setCurrentRoute();
    } catch (error) {
      // console.log(error);
    }
  });
</script>

<style lang="scss" scoped>
  .dark {
    .close-icon {
      color: v-bind('configStore.getColorVal("headerBarTabColor")') !important;
    }

    .ba-nav-tab.active {
      .close-icon {
        color: v-bind('configStore.getColorVal("headerBarTabActiveColor")') !important;
      }
    }
  }

  header {
    height: 50px;
    margin: 20px 16px 0 16px;
    // box-shadow: 0 0 8px rgba(0 0 0 / 8%);
    // 测试颜色
    // background: rgba(255, 255, 255, 1);
  }

  .demo-tabs > .el-tabs__content {
    padding: 32px;
    //color: #ffffff;
    font-size: 32px;
    font-weight: 600;
  }

  .el-header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    padding: 0;

    .nav-menus {
      border-radius: var(--el-border-radius-base);
      box-shadow: var(--el-box-shadow-light);
      //box-shadow: 0 0 12px rgba(0, 0, 0, 0.12);
      //border-radius: 4px;
      display: flex;
      align-items: center;
      height: 100%;
      background-color: v-bind('configStore.getColorVal("headerBarBackground")');
      overflow: hidden;

      .x-tabs-icon {
        width: 25px;
        height: 25px;
        border-radius: 50%;
        margin-right: 10px;
      }

      .user {
        width: 24px;
        height: 24px;
        border-radius: 50%;
        margin-right: 10px;
      }

      .user-info {
        // header 用户信息相对右边的距离
        padding: 0 10px 0 10px;
      }

      .user-name {
        /** 文本1 */
        font-size: 14px;
        font-weight: 400;
        letter-spacing: 0;
        line-height: 22px;
        //color: rgba(0, 0, 0, 0.65);
        text-align: left;
        vertical-align: top;
      }
    }
  }
</style>
