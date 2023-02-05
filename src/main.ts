import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import piniaStore from './store';
import { registerIcons } from '/@/utils/common';
import 'element-plus/dist/index.css';
import 'element-plus/theme-chalk/display.css';
import '/@/styles/index.scss';
import 'uno.css';

// 支持SVG
import 'virtual:svg-icons-register';
import ElementPlus from 'element-plus';
import { loadLang } from '/@/lang';

const app = createApp(App);
app.use(router);
app.use(piniaStore);
// 全局语言包加载
const i18n = await loadLang(app);

app.use(router);
app.use(ElementPlus, { i18n: i18n.global.t });
// 注册所有图标
registerIcons(app); // icons

app.mount('#app');
