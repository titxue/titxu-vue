import viteCompression from 'vite-plugin-compression';
import { COMPRESSION } from '../../constant';
export const ConfigCompressPlugin = () => {
  if (COMPRESSION) {
    return viteCompression({
      verbose: true,
      disable: false,
      deleteOriginFile: false,
      threshold: 10240,
      algorithm: 'gzip',
      ext: '.gz',
    });
  }
  return [];
};
