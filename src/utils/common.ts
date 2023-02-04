import { nextTick } from 'vue';
import type { App } from 'vue';
import * as elIcons from '@element-plus/icons-vue';
import router from '/@/router/index';
import Icon from '/@/components/v1/icon/index.vue';
import { ElForm } from 'element-plus';
import { useAppStore } from '/@/store';
import { useTitle } from '@vueuse/core';
import { getUrl } from './http/axios/axios';

export function registerIcons(app: App) {
  /*
   * 全局注册 Icon
   * 使用方式: <Icon name="name" size="size" color="color" />
   * 详见<待完善>
   */
  app.component('Icon', Icon);

  /*
   * 全局注册element Plus的icon
   */
  const icons = elIcons as any;
  for (const i in icons) {
    app.component(`el-icon-${icons[i].name}`, icons[i]);
  }
}

/**
 * 加载网络css文件
 * @param url css资源url
 */
export function loadCss(url: string): void {
  const link = document.createElement('link');
  link.rel = 'stylesheet';
  link.href = url;
  link.crossOrigin = 'anonymous';
  document.getElementsByTagName('head')[0].appendChild(link);
}

/**
 * 加载网络js文件
 * @param url js资源url
 */
export function loadJs(url: string): void {
  const link = document.createElement('script');
  link.src = url;
  document.body.appendChild(link);
}

/**
 * 根据路由 meta.title 设置浏览器标题
 */
export function setTitleFromRoute() {
  if (typeof router.currentRoute.value.meta.title != 'string') {
    return;
  }
  nextTick(() => {
    let webTitle: string;
    if ((router.currentRoute.value.meta.title as string).indexOf('pagesTitle.') === -1) {
      webTitle = router.currentRoute.value.meta.title as string;
    } else {
      webTitle = router.currentRoute.value.meta.title as string;
    }
    const title = useTitle();
    const appStore = useAppStore();
    title.value = `${webTitle}${appStore.title ? ' - ' + appStore.title : ''}`;
  });
}

/**
 * 设置浏览器标题-只能在路由加载完成后调用
 * @param webTitle 新的标题
 */
export function setTitle(webTitle: string) {
  const title = useTitle();
  const appStore = useAppStore();
  title.value = `${webTitle}${appStore.title ? ' - ' + appStore.title : ''}`;
}

/**
 * 是否是外部链接
 * @param path
 */
export function isExternal(path: string): boolean {
  return /^(https?|ftp|mailto|tel):/.test(path);
}

/**
 * 防抖
 * @param fn 执行函数
 * @param ms 间隔毫秒数
 */
export const debounce = (fn: Function, ms: number) => {
  return (...args: any[]) => {
    if (window.lazy) {
      clearTimeout(window.lazy);
    }
    window.lazy = setTimeout(() => {
      fn(...args);
    }, ms);
  };
};

/**
 * 根据pk字段的值从数组中获取key
 * @param arr
 * @param pk
 * @param value
 */
export const getArrayKey = (arr: any, pk: string, value: string): any => {
  for (const key in arr) {
    if (arr[key][pk] == value) {
      return key;
    }
  }
  return false;
};

/**
 * 表单重置
 * @param formEl
 */
export const onResetForm = (formEl: InstanceType<typeof ElForm> | undefined) => {
  if (!formEl) return;
  formEl.resetFields && formEl.resetFields();
};

/**
 * 是否在后台应用内
 * @param path 不传递则通过当前路由 path 检查
 */
export const isAdminApp = (path = '') => {
  if (path) {
    return /^\/admin/.test(path);
  }
  return /^\/admin/.test(router.currentRoute.value.fullPath) || window.location.hash.indexOf('#/admin') === 0;
};

/**
 * 是否为手机设备
 */
export const isMobile = () => {
  return !!navigator.userAgent.match(
    /android|webos|ip(hone|ad|od)|opera (mini|mobi|tablet)|iemobile|windows.+(phone|touch)|mobile|fennec|kindle (Fire)|Silk|maemo|blackberry|playbook|bb10; (touch|kbd)|Symbian(OS)|Ubuntu Touch/i,
  );
};

/**
 * 从一个文件路径中获取文件名
 * @param path 文件路径
 */
export const getFileNameFromPath = (path: string) => {
  const paths = path.split('/');
  return paths[paths.length - 1];
};

/**
 * 获取资源完整地址
 * @param relativeUrl 资源相对地址
 * @param domain 指定域名
 */
export const fullUrl = (relativeUrl: string, domain = '') => {
  const appStore = useAppStore();
  if (!domain) {
    domain = appStore.cdnUrl ? appStore.cdnUrl : getUrl();
  }
  if (!relativeUrl) return domain;

  const regUrl = new RegExp(/^http(s)?:\/\//);
  const regexImg = new RegExp(/^((?:[a-z]+:)?\/\/|data:image\/)(.*)/i);
  if (!domain || regUrl.test(relativeUrl) || regexImg.test(relativeUrl)) {
    return relativeUrl;
  }
  return domain + relativeUrl;
};

/**
 * 文件类型效验，主要用于云存储
 * 服务端并不能单纯此函数来限制文件上传
 * @param {string} fileName 文件名
 * @param {string} fileType 文件mimetype，不一定存在
 */
export const checkFileMimetype = (fileName: string, fileType: string) => {
  if (!fileName) return false;
  const appStore = useAppStore();
  const mimetype = appStore.upload.mimetype.toLowerCase().split(',');

  const fileSuffix = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
  if (appStore.upload.mimetype === '*' || mimetype.includes(fileSuffix) || mimetype.includes('.' + fileSuffix)) {
    return true;
  }
  if (fileType) {
    const fileTypeTemp = fileType.toLowerCase().split('/');
    if (mimetype.includes(fileTypeTemp[0] + '/*') || mimetype.includes(fileType)) {
      return true;
    }
  }
  return false;
};

/**
 * 获取一组资源的完整地址
 * @param relativeUrls 资源相对地址
 * @param domain 指定域名
 */
export const arrayFullUrl = (relativeUrls: string | string[], domain = '') => {
  if (typeof relativeUrls === 'string') {
    relativeUrls = relativeUrls == '' ? [] : relativeUrls.split(',');
  }
  for (const key in relativeUrls) {
    relativeUrls[key] = fullUrl(relativeUrls[key], domain);
  }
  return relativeUrls;
};
