import { createRouter, createWebHashHistory } from 'vue-router';
import routes from 'virtual:generated-pages';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import { isLogin } from '../utils/auth';

routes.push({
  path: '/',
  redirect: '/login',
});
//导入生成的路由数据
const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

router.beforeEach(async (_to, _from, next) => {
  // 圆圈加载：showSpinner
  NProgress.configure({ showSpinner: false });

  NProgress.start();


  // 白名单
  const whiteList = ["/login","/"];
  if (whiteList.includes(_to.path)) {
    next();
    return;
  }
  if (isLogin()) {
    next();
    return;
  }
  if (!whiteList.includes(_to.path) && !isLogin()) {
    next("/login");
    return;
  }
  next();










});

router.afterEach((_to) => {
  NProgress.done();
});




export default router;
