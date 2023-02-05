import { createRouter, createWebHashHistory } from 'vue-router';
import routes from 'virtual:generated-pages';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import { isLogin } from '../utils/auth';
routes.push({
  path: '/',
  redirect: '/login',
});
const router = createRouter({
  history: createWebHashHistory(),
  routes,
});
router.beforeEach(async (_to, _from, next) => {
  NProgress.configure({ showSpinner: false });
  NProgress.start();
  const whiteList = ['/login', '/'];
  if (whiteList.includes(_to.path)) {
    if (isLogin()) {
      if (_to.path === '/login') {
        console.log('已经登陆，直接跳转到admin页面');
        next('/admin');
        return;
      }
      next();
      return;
    }
    next();
    return;
  }
  if (!whiteList.includes(_to.path) && !isLogin()) {
    next('/login');
    return;
  }
  next();
});
router.afterEach((_to, _from) => {
  if (_from.meta.title) {
    document.title = _from.meta.title.toString();
    console.log('title', document.title);
  }
  NProgress.done();
});
export default router;
//# sourceMappingURL=index.js.map
