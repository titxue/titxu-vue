import { post } from '/@/utils/http/axios';
var URL;
(function (URL) {
  URL['listLogin'] = '/sys/log/listLogin';
})(URL || (URL = {}));
const listLogin = async (currPage) =>
  post({
    url: URL.listLogin,
    data: {
      page: currPage,
      limit: 10,
    },
  });
export { listLogin };
//# sourceMappingURL=index.js.map
