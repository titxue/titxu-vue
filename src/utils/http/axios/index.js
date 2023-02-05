import createAxios from '/@/utils/http/axios/axios';
function get(config, options) {
  return createAxios({ ...config, method: 'GET' }, options);
}
function post(config, options) {
  return createAxios({ ...config, method: 'POST' }, options);
}
function put(config, options) {
  return createAxios({ ...config, method: 'PUT' }, options);
}
function del(config, options) {
  return createAxios({ ...config, method: 'DELETE' }, options);
}
export { get, post, put, del };
//# sourceMappingURL=index.js.map
