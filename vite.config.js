import { createVitePlugins } from './build/vite/plugins';
import { resolve } from 'path';
import proxy from './build/vite/proxy';
import { VITE_PORT } from './build/constant';
function pathResolve(dir) {
  return resolve(process.cwd(), '.', dir);
}
export default ({ command }) => {
  console.log('command', command);
  const isBuild = command === 'build';
  let base;
  if (command === 'build') {
    base = '/titxu-admin/';
  } else {
    base = '/';
  }
  return {
    base,
    resolve: {
      alias: [
        {
          find: 'vue-i18n',
          replacement: 'vue-i18n/dist/vue-i18n.cjs.js',
        },
        {
          find: /\/@\//,
          replacement: pathResolve('src') + '/',
        },
        {
          find: /\/#\//,
          replacement: pathResolve('types') + '/',
        },
      ],
    },
    plugins: createVitePlugins(isBuild),
    css: {},
    server: {
      hmr: { overlay: false },
      port: VITE_PORT,
      open: false,
      cors: false,
      host: '0.0.0.0',
      proxy,
    },
  };
};
//# sourceMappingURL=vite.config.js.map
