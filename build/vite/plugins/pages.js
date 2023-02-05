import Pages from 'vite-plugin-pages';
export const ConfigPagesPlugin = () => {
  return Pages({
    pagesDir: [{ dir: 'src/views', baseRoute: '' }],
    extensions: ['vue', 'md'],
    exclude: ['**/components/*.vue', '**/components/*/*.vue'],
    nuxtStyle: true,
  });
};
