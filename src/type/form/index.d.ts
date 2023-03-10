declare namespace Form {
  type ItemType = 'password' | 'text' | 'textarea' | 'radio' | 'checkbox' | 'tree' | 'select';
  // 当FiledItem的type === 'radio' | 'checkbox'时，options的参数类型
  interface IFieldOptions {
    labelkey?: string;
    valueKey?: string;
    childrenKey?: string;
    placeholder?: string;
    data: Recode<string, any>[];
    multiple?: boolean; // 是否多选
  }
  interface Options {
    labelWidth?: string | number;
    labelPosition?: 'left' | 'right' | 'top';
    disabled?: boolean;
    size?: 'large' | 'small' | 'default';
    showResetButton?: boolean; // 是否展示重置按钮
    showCancelButton?: boolean; // 是否展示取消按钮
    submitButtonText?: string;
    resetButtonText?: string;
    cancelButtonText?: string;
  }
  interface FieldItem {
    label?: string;
    labelWidth?: string | number; // 标签宽度，例如 '50px'。 可以使用 auto。
    field: string;
    type?: ItemType;
    value?: any;
    placeholder?: string;
    disabled?: boolean;
    readonly?: boolean;
    options?: IFieldOptions;
    rules?: import('element-plus').FormItemRule[];
    clearable?: boolean; // 是否可清空
    showPassword?: boolean; // 是否显示切换密码图标
    enterable?: boolean; // 当为输入框时，是否启用回车触发提交功能
  }
}
