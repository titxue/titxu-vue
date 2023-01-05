<template>
  <div class="common-layout">
    <el-container>
      <!-- 侧边栏 -->
      <common-aside />
      <el-container class="r-container">
        <el-header>
          <!-- 头部 -->
          <common-header />
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
  import CommonHeader from './admin/components/CommonHeader/commonHeader.vue';
  import CommonAside from './admin/components/CommonAside/commonAside.vue';
</script>

<style lang="less" scoped>
  .common-layout {
    height: 100%;

    & > .el-container {
      height: 100%;
      background-color: #f5f5f5;

      & > .el-aside {
        display: flex;
        flex-direction: column;
      }

      & > .el-container {
        & > .el-header {
          padding: 0%;
        }
      }
    }
  }
</style>
