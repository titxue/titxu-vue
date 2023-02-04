import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import piniaStore from './store';
import '/@/styles/index.less';
import '/@/styles/reset.less';
import 'uno.css';
import '/@/assets/styles/base.less';
import 'element-plus/es/components/message/style/css'; // 引入样式
import 'element-plus/theme-chalk/index.css';

// 支持SVG
import 'virtual:svg-icons-register';
import { registerIcons } from '/@/utils/common';

const app = createApp(App);

// 注册所有图标
registerIcons(app); // icons

app.use(router);
app.use(piniaStore);
app.mount('#app');
