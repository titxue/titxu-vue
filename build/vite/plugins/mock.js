import { viteMockServe } from 'vite-plugin-mock';
export const ConfigMockPlugin = (isBuild) => {
  return viteMockServe({
    ignore: /^\_/,
    mockPath: 'mock',
    localEnabled: !isBuild,
    prodEnabled: false,
    injectCode: `
       import { setupProdMockServer } from '../mock/_createProdMockServer';
       setupProdMockServer();
       `,
  });
};
