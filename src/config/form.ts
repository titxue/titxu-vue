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
// // 自定义验证表单配置数据
// export const validationFormFieldList = [
//     { label: '姓名', field: 'name', rules: [{ required: true, message: 'name is required' }] },
//     { label: '邮箱', field: 'email', rules: [{ required: true, validator: checkEmail }] },
// ] as Form.FieldItem[]

// 表单配置示例
export const exampleForm = {
  base: [
    { label: '姓名', field: 'name', disabled: true },
    {
      label: '性别',
      field: 'gender',
      type: 'radio',
      options: {
        data: [
          { label: '男', value: 1 },
          { label: '女', value: 0 },
        ],
      },
    },
    {
      label: '爱好',
      field: 'hobbies',
      type: 'checkbox',
      options: {
        data: [
          { label: '吃饭', value: 1 },
          { label: '睡觉', value: 2 },
          { label: '写代码', value: 3 },
        ],
      },
    },
    {
      label: '工作',
      field: 'job',
      type: 'select',
      options: {
        data: [
          { label: '吃饭', value: 1 },
          { label: '睡觉', value: 2 },
          { label: '写代码', value: 3 },
        ],
      },
    },
    { label: '密码', field: 'password', type: 'password', placeholder: '这是一个密码输入框' },
    { label: '只读', field: 'readonly', readonly: true, placeholder: '这是一个只读输入框' },
    { label: '留言板', field: 'summary', type: 'textarea', placeholder: '留言板' },
  ],
  editUser: [
    { label: '用户名', field: 'userNick', rules: [{ required: true, message: '用户名不能为空' }] },
    { label: '手机号', field: 'mobile', rules: [{ required: false, validator: checkMobile }] },
    { label: '邮箱', field: 'email', rules: [{ required: false, validator: checkEmail }] },
    {
      label: '角色列表',
      field: 'roleIdList',
      type: 'select',
      options: {
        data: [
          { label: '吃饭', value: 1 },
          { label: '睡觉', value: 2 },
          { label: '写代码', value: 3 },
        ],
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
  customkeyForm: [
    { label: '标题', field: 'name' },
    {
      label: '性别',
      field: 'gender',
      type: 'radio',
      options: {
        labelkey: 'title',
        valueKey: 'val',
        data: [
          { title: '男', val: 1 },
          { title: '女', val: 0 },
        ],
      },
    },
  ],
  ruleForm: [
    { label: '姓名', field: 'name', rules: [{ required: true, message: 'name is required' }] },
    { label: '邮箱', field: 'email', rules: [{ required: true, validator: checkEmail }] },
  ],
} as Record<string, Form.FieldItem[]>;

export const routineForm = {
  userinfo: [
    { label: '租户', field: 'tenantName', disabled: true },
    { label: '用户昵称', field: 'userNick', rules: [{ required: true, message: '用户昵称不能为空' }] },
    { label: '手机号码', field: 'mobile', rules: [{ required: true, validator: checkMobile }] },
    { label: '邮箱地址', field: 'email', rules: [{ required: false, validator: checkEmail }] },
    { label: '签名', field: 'remarks', type: 'textarea' },
  ],
} as Record<string, Form.FieldItem[]>;
