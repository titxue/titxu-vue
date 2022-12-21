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
    if (isLogin()) {
      // 如果在登陆页面，直接跳转到admin页面
      if (_to.path === "/login") {
        console.log("已经登陆，直接跳转到admin页面");
        next("/admin");
        return;
      }
      next();
      return;
    }
    next();
    return;
  }

  // 如果不在白名单，判断是否登陆，如果没有登陆，跳转到登陆页面
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
