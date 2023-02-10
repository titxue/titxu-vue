<template>
  <el-drawer v-model="config.layout.showDrawer" title="I am the title" :before-close="handleClose">
    <el-switch v-model="config.layout.isDark" active-text="Dark" inactive-text="Light" @click="toggleDark()" />
  </el-drawer>
</template>

<script setup lang="ts">
  import { useConfigStore } from '/@/store';
  import toggleDark from '/@/utils/useDark';

  const config = useConfigStore();

  const handleClose = (done: any) => {
    config.setLayout('showDrawer', false);
    done();
  };
</script>

<style scoped></style>
