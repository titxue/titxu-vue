import { nextTick } from 'vue';
import * as elIcons from '@element-plus/icons-vue';
import router from '/@/router/index';
import Icon from '/@/components/v1/icon/index.vue';
import { useAppStore } from '/@/store';
import { useTitle } from '@vueuse/core';
import { getUrl } from './http/axios/axios';
export function registerIcons(app) {
  app.component('Icon', Icon);
  const icons = elIcons;
  for (const i in icons) {
    app.component(`el-icon-${icons[i].name}`, icons[i]);
  }
}
export function loadCss(url) {
  const link = document.createElement('link');
  link.rel = 'stylesheet';
  link.href = url;
  link.crossOrigin = 'anonymous';
  document.getElementsByTagName('head')[0].appendChild(link);
}
export function loadJs(url) {
  const link = document.createElement('script');
  link.src = url;
  document.body.appendChild(link);
}
export function setTitleFromRoute() {
  if (typeof router.currentRoute.value.meta.title != 'string') {
    return;
  }
  nextTick(() => {
    let webTitle;
    if (router.currentRoute.value.meta.title.indexOf('pagesTitle.') === -1) {
      webTitle = router.currentRoute.value.meta.title;
    } else {
      webTitle = router.currentRoute.value.meta.title;
    }
    const title = useTitle();
    const appStore = useAppStore();
    title.value = `${webTitle}${appStore.title ? ' - ' + appStore.title : ''}`;
  });
}
export function setTitle(webTitle) {
  const title = useTitle();
  const appStore = useAppStore();
  title.value = `${webTitle}${appStore.title ? ' - ' + appStore.title : ''}`;
}
export function isExternal(path) {
  return /^(https?|ftp|mailto|tel):/.test(path);
}
export const debounce = (fn, ms) => {
  return (...args) => {
    if (window.lazy) {
      clearTimeout(window.lazy);
    }
    window.lazy = setTimeout(() => {
      fn(...args);
    }, ms);
  };
};
export const getArrayKey = (arr, pk, value) => {
  for (const key in arr) {
    if (arr[key][pk] == value) {
      return key;
    }
  }
  return false;
};
export const onResetForm = (formEl) => {
  if (!formEl) return;
  formEl.resetFields && formEl.resetFields();
};
export const isAdminApp = (path = '') => {
  if (path) {
    return /^\/admin/.test(path);
  }
  return /^\/admin/.test(router.currentRoute.value.fullPath) || window.location.hash.indexOf('#/admin') === 0;
};
export const isMobile = () => {
  return !!navigator.userAgent.match(
    /android|webos|ip(hone|ad|od)|opera (mini|mobi|tablet)|iemobile|windows.+(phone|touch)|mobile|fennec|kindle (Fire)|Silk|maemo|blackberry|playbook|bb10; (touch|kbd)|Symbian(OS)|Ubuntu Touch/i,
  );
};
export const getFileNameFromPath = (path) => {
  const paths = path.split('/');
  return paths[paths.length - 1];
};
export const fullUrl = (relativeUrl, domain = '') => {
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
export const checkFileMimetype = (fileName, fileType) => {
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
export const arrayFullUrl = (relativeUrls, domain = '') => {
  if (typeof relativeUrls === 'string') {
    relativeUrls = relativeUrls == '' ? [] : relativeUrls.split(',');
  }
  for (const key in relativeUrls) {
    relativeUrls[key] = fullUrl(relativeUrls[key], domain);
  }
  return relativeUrls;
};
//# sourceMappingURL=common.js.map
