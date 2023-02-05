const checkEmail = (rule, value, callback) => {
  if (!rule.required && !value) {
    callback();
  }
  if (!value) callback(new Error('请输入电子邮箱地址'));
  const regExp = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.(com|cn|net)$/;
  regExp.test(value) ? callback() : callback(new Error('请输入正确的电子邮箱地址'));
};
const checkMobile = (rule, value, callback) => {
  if (!rule.required && !value) {
    callback();
  }
  if (!value) callback(new Error('请输入手机号码'));
  const regExp = /^1(3\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\d|9[0-35-9])\d{8}$/;
  regExp.test(value) ? callback() : callback(new Error('请输入正确的手机号码'));
};
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
};
export const menuDialog = {
  editMenu: [
    {
      label: '父级菜单',
      field: 'parentId',
      type: 'select',
      options: {
        data: [],
        multiple: false,
      },
    },
    { label: '菜单名称', field: 'permissionName' },
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
    {
      label: '父级菜单',
      field: 'parentId',
      type: 'select',
      options: {
        data: [],
        multiple: false,
      },
    },
    { label: '菜单名称', field: 'permissionName' },
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
};
export const roleDialog = {
  editRole: [
    {
      label: '角色名称',
      field: 'roleName',
    },
    {
      label: '角色编码',
      field: 'roleCode',
    },
    {
      label: '备注',
      field: 'remarks',
    },
    {
      label: '权限',
      field: 'permissionIdList',
      type: 'tree',
      options: {
        labelkey: 'permissionName',
        valueKey: 'id',
        childrenKey: 'subList',
        data: [],
        multiple: false,
      },
    },
  ],
  addRole: [
    {
      label: '角色编码',
      field: 'roleCode',
    },
  ],
};
//# sourceMappingURL=dialog.js.map
