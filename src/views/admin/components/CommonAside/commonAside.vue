<template>
  <el-aside width="210px">
    <el-menu active-text-color="#fff" background-color="rgba(51, 65, 84, 1)" class="el-menu-vertical-demo"
      default-active="1" text-color="rgba(192, 196, 204, 1)" @open="handleOpen" @close="handleClose">
      <h3>{{ false ? '后台' : '后台管理' }}</h3>
      <el-menu-item :index="item.path + ''" v-for="item in noChildern()" :key="item.label" @click="handleClick(item)">
        <component class="icons" :is="item.icon"></component>
        <!-- <template #title>{{item.label}}</template> -->
        <span>{{ item.label }}</span>
      </el-menu-item>
      <el-sub-menu :index="item.path + ''" v-for="item in hasChildern()" :key="item.label">
        <template #title>
          <component class="icons" :is="item.icon"></component>
          <span>{{ item.label }}</span>
        </template>
        <el-menu-item-group v-for="(subItem, subIndex) in item.children" :index="subItem.path + ''"
          :key="subItem.label">
          <el-menu-item :index="subIndex + ''" @click="handleClick(subItem)">
            <component class="icons" :is="subItem.icon"></component>
            <span>{{ subItem.label }}</span>
          </el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>

    </el-menu>
  </el-aside>
</template>


<script setup lang="ts">
const router = useRouter();

const list = [
  {
    path: '/home',
    name: 'home',
    label: '首页',
    icon: 'house',
    url: '/home'
  },
  {
    label: '系统管理',
    name: 'system',
    icon: 'menu',
    path: '/system',
    children: [
      {
        path: '/system/user',
        name: 'user',
        label: '用户管理',
        icon: 'user',
        url: '/system/user'
      },
      {
        path: '/system/role',
        name: 'role',
        label: '角色管理',
        icon: 'UserFilled',
        url: '/system/role'
      },
    ]
  },
];
const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}

const handleClick = (item: any) => {
  console.log('router name', item)
  console.log('router', router.getRoutes())
  router.push("/admin"+item.url)
};
// 返回没有子菜单的菜单项
const noChildern = () => {
  return list.filter((item) => !item.children)
};

// 返回有子菜单的菜单项
const hasChildern = () => {
  return list.filter((item) => item.children)
};
</script>

<style lang="less" scoped>
.icons {
  width: 18px;
  height: 18px;
  margin-right: 10px;
}

.el-menu-item-group {
  background-color: rgba(34, 45, 60, 1);
}


.el-menu-vertical-demo {
  width: 100%;
  border-right: none;

  h3 {
    color: #fff;
    text-align: center;
    margin-top: 10px;
  }
}
</style>