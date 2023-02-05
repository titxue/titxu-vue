import createAxios from '../utils/http/axios/axios';
export var ADMIN_URL;
(function (ADMIN_URL) {
  ADMIN_URL['role'] = '/sys/role/';
})(ADMIN_URL || (ADMIN_URL = {}));
export class xTableApi {
  controllerUrl;
  actionUrl;
  constructor(controllerUrl) {
    this.controllerUrl = controllerUrl;
    this.actionUrl = new Map([
      ['index', controllerUrl + 'list'],
      ['add', controllerUrl + 'save'],
      ['edit', controllerUrl + 'update'],
      ['del', controllerUrl + 'delete'],
      ['switch', controllerUrl + 'disable'],
    ]);
  }
  index(filter = {}) {
    return createAxios({
      url: this.actionUrl.get('index'),
      method: 'get',
      params: filter,
    });
  }
  edit(params) {
    return createAxios({
      url: this.actionUrl.get('edit'),
      method: 'get',
      params: params,
    });
  }
  del(ids) {
    return createAxios(
      {
        url: this.actionUrl.get('del'),
        method: 'DELETE',
        data: ids,
      },
      {
        showSuccessMessage: true,
      },
    );
  }
  postData(action, data) {
    if (!this.actionUrl.has(action)) {
      throw new Error('action does not exist');
    }
    return createAxios(
      {
        url: this.actionUrl.get(action),
        method: 'post',
        data: data,
      },
      {
        showSuccessMessage: true,
      },
    );
  }
}
//# sourceMappingURL=common.js.map
