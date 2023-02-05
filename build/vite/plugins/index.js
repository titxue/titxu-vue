import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import VitePluginCertificate from 'vite-plugin-mkcert';
import vueSetupExtend from 'vite-plugin-vue-setup-extend';
import { ConfigSvgIconsPlugin } from './svgIcons';
import { AutoRegistryComponents } from './component';
import { AutoImportDeps } from './autoImport';
import { ConfigMockPlugin } from './mock';
import { ConfigVisualizerConfig } from './visualizer';
import { ConfigCompressPlugin } from './compress';
import { ConfigPagesPlugin } from './pages';
import { ConfigRestartPlugin } from './restart';
import { ConfigProgressPlugin } from './progress';
import { ConfigImageminPlugin } from './imagemin';
import { ConfigUnocssPlugin } from './unocss';
export function createVitePlugins(isBuild) {
  const vitePlugins = [
    vue(),
    vueJsx(),
    vueSetupExtend(),
    VitePluginCertificate({
      source: 'coding',
    }),
  ];
  vitePlugins.push(AutoRegistryComponents());
  vitePlugins.push(AutoImportDeps());
  vitePlugins.push(ConfigPagesPlugin());
  vitePlugins.push(ConfigCompressPlugin());
  vitePlugins.push(ConfigRestartPlugin());
  vitePlugins.push(ConfigProgressPlugin());
  vitePlugins.push(ConfigUnocssPlugin());
  vitePlugins.push(ConfigSvgIconsPlugin(isBuild));
  vitePlugins.push(ConfigMockPlugin(isBuild));
  vitePlugins.push(ConfigVisualizerConfig());
  vitePlugins.push(ConfigImageminPlugin());
  return vitePlugins;
}
