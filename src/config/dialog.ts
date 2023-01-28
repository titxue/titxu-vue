// 自定义验证邮箱方法
const checkEmail = (rule: any, value: any, callback: any) => {
  // 如果不是必填项，且用户没有输入值，则不做校验
  if (!rule.required && !value) {
    callback();
  }
  if (!value) callback(new Error('请输入电子邮箱地址'));
  const regExp = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.(com|cn|net)$/;
  regExp.test(value) ? callback() : callback(new Error('请输入正确的电子邮箱地址'));
};
// 自定义验证手机号方法
const checkMobile = (rule: any, value: any, callback: any) => {
  // 如果不是必填项，且用户没有输入值，则不做校验
  if (!rule.required && !value) {
    callback();
  }
  if (!value) callback(new Error('请输入手机号码'));
  const regExp = /^1(3\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\d|9[0-35-9])\d{8}$/;
  regExp.test(value) ? callback() : callback(new Error('请输入正确的手机号码'));
};
// 表单配置示例
export const userDialog = {
  editUser: [
    { label: '用户名', field: 'userNick', rules: [{ required: true, message: '用户名不能为空' }] },
    { label: '手机号', field: 'mobile', rules: [{ required: false, validator: checkMobile }] },
    { label: '邮箱', field: 'email', rules: [{ required: false, validator: checkEmail }] },
    {
      label: '角色列表',
      field: 'roleIdList',
      type: 'select',
      options: {
        data: [],
        multiple: true,
      },
    },
    {
      label: '状态',
      field: 'status',
      type: 'radio',
      options: {
        data: [
          { label: '有效', value: '0' },
          { label: '禁用', value: '1' },
        ],
      },
    },
  ],
  addUser: [
    { label: '用户名', field: 'userNick', rules: [{ required: true, message: '用户名不能为空' }] },
    { label: '手机号', field: 'mobile', rules: [{ required: false, validator: checkMobile }] },
    { label: '邮箱', field: 'email', rules: [{ required: false, validator: checkEmail }] },
    {
      label: '角色列表',
      field: 'roleIdList',
      type: 'select',
      options: {
        data: [],
        multiple: true,
      },
    },
  ],
} as Record<string, Form.FieldItem[]>;

// 菜单配置示例
export const menuDialog = {
  editMenu: [
    { label: '菜单名称', field: 'permissionName' },
    {
      label: '父级菜单',
      field: 'parentName',
      type: 'select',
      options: {
        data: [],
        multiple: false,
      },
    },

    { label: '菜单图标', field: 'menuIcon' },
    { label: '菜单URL', field: 'menuUrl' },
    {
      label: '菜单类型',
      field: 'permissionType',
      type: 'select',
      options: {
        data: [
          { label: '目录', value: '0' },
          { label: '菜单', value: '1' },
          { label: '按钮', value: '2' },
        ],
        multiple: false,
      },
    },
    {
      label: '菜单级别',
      field: 'permissionLevel',
      type: 'select',
      options: {
        data: [
          { label: '系统', value: '0' },
          { label: '租户', value: '1' },
        ],
        multiple: false,
      },
    },
  ],
  addMenu: [
    { label: '菜单名称', field: 'permissionName' },
    {
      label: '父级菜单',
      field: 'parentId',
      type: 'select',
      options: {
        data: [],
        multiple: false,
      },
    },
    { label: '菜单图标', field: 'menuIcon' },
    { label: '菜单URL', field: 'menuUrl' },
    {
      label: '菜单类型',
      field: 'permissionType',
      type: 'select',
      options: {
        data: [
          { label: '目录', value: '0' },
          { label: '菜单', value: '1' },
          { label: '按钮', value: '2' },
        ],
        multiple: false,
      },
    },
    {
      label: '菜单级别',
      field: 'permissionLevel',
      type: 'select',
      options: {
        data: [
          { label: '系统', value: '0' },
          { label: '租户', value: '1' },
        ],
        multiple: false,
      },
    },
  ],
} as Record<string, Form.FieldItem[]>;
