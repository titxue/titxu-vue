import createAxios from '../utils/http/axios/axios';

/*
 * 公共请求函数和Url定义
 */

// 角色模块
export enum ADMIN_URL {
  user = '/sys/user/',
  role = '/sys/role/',
  tenant = '/sys/tenant/',
  permission = '/sys/permission/',
}

/**
 * 生成一个控制器的：增、删、改、查、排序的操作url
 */
export class xTableApi {
  // @ts-ignore
  private controllerUrl: string;
  public actionUrl: Map<string, string>;

  constructor(controllerUrl: string) {
    this.controllerUrl = controllerUrl;
    this.actionUrl = new Map([
      ['index', controllerUrl + 'list'],
      ['add', controllerUrl + 'save'],
      ['edit', controllerUrl + 'update'],
      ['del', controllerUrl + 'delete'],
      ['switch', controllerUrl + 'disable'],
    ]);
  }

  index(filter: anyObj = {}): ApiPromise<any> {
    return createAxios({
      url: this.actionUrl.get('index'),
      method: 'get',
      params: filter,
    }) as ApiPromise;
  }

  edit(params: anyObj) {
    return createAxios({
      url: this.actionUrl.get('edit'),
      method: 'get',
      params: params,
    });
  }

  del(ids: string[]) {
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

  postData(action: string, data: anyObj) {
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
