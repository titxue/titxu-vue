import { createPinia } from 'pinia';
import { useAppStore } from './modules/app';
import { useUserStore } from './modules/user';
import { usePermissionStore } from './modules/permission';
import { useRoleStore } from './modules/role';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';
import { useAuthStore } from './modules/auth';
import { useLogStore } from './modules/log';
import { useConfigStore } from './modules/config';
import { useNavTabStore } from './modules/navTab';

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

export { useAppStore, useUserStore, usePermissionStore, useRoleStore, useAuthStore, useLogStore, useConfigStore, useNavTabStore };
export default pinia;
