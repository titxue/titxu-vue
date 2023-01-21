import { createPinia } from 'pinia';
import { useAppStore } from './modules/app';
import { useUserStore } from './modules/user';
import { usePermissionStore } from './modules/permission';
import { useRoleStore } from './modules/role';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';
import { useAuthStore } from './modules/auth';

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

export { useAppStore, useUserStore, usePermissionStore, useRoleStore, useAuthStore };
export default pinia;
