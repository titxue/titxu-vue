import ViteRestart from 'vite-plugin-restart';
export const ConfigRestartPlugin = () => {
  return ViteRestart({
    restart: ['*.config.[jt]s', '**/config/*.[jt]s'],
  });
};
//# sourceMappingURL=restart.js.map
