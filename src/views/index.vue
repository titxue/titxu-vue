<template>
  <div class="dark:text-slate-400 dark:bg-slate-900">
    <Header />
    <main class="max-w-5xl px-4 mx-auto pb-22 sm:px-6 md:px-8 xl:px-12 xl:max-w-6xl">
      <div class="pt-8 pb-7 sm:pb-8 sm:text-center">
        <h1 class="relative mb-4 text-4xl tracking-tight font-blimone sm:text-5xl lg:text-6xl text-slate-900 dark:text-slate-200">
          {{ appStore.h1 }}
        </h1>
        <p class="text-2xl text-slate-800 dark:text-slate-400">{{ appStore.title }}</p>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
  import { useAppStore } from '/@/store/modules/app';
  import Header from '/@/components/Header/index.vue';

  const appStore = useAppStore();
</script>

<style lang="scss" scoped></style>
