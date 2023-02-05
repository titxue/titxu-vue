import { createSvgIconsPlugin } from 'vite-plugin-svg-icons';
import path from 'path';
export const ConfigSvgIconsPlugin = (isBuild) => {
  return createSvgIconsPlugin({
    iconDirs: [path.resolve(process.cwd(), 'src/assets/icons')],
    symbolId: 'icon-[dir]-[name]',
    svgoOptions: isBuild,
  });
};
//# sourceMappingURL=svgIcons.js.map
