<script lang="ts">
  import { createVNode, resolveComponent, defineComponent, PropType, VNode, computed } from 'vue';
  import { inputTypes, modelValueTypes, InputAttr, InputData } from '/@/components/v1/xInput';
  import Array from '/@/components/v1/xInput/components/array.vue';
  import IconSelector from '/@/components/v1/xInput/components/iconSelector.vue';
  import Editor from '/@/components/v1/xInput/components/editor.vue';

  export default defineComponent({
    name: 'XInput',
    props: {
      // 输入框类型,支持的输入框见 inputTypes
      type: {
        type: String,
        required: true,
        validator: (value: string) => {
          return inputTypes.includes(value);
        },
      },
      // 双向绑定值
      modelValue: {
        type: null,
        required: true,
      },
      // 输入框的附加属性
      attr: {
        type: Object as PropType<InputAttr>,
        default: () => {},
      },
      // 额外数据,radio、checkbox的选项等数据
      data: {
        type: Object as PropType<InputData>,
        default: () => {},
      },
    },
    emits: ['update:modelValue'],
    setup(props, { emit }) {
      const onValueUpdate = (value: modelValueTypes) => {
        emit('update:modelValue', value);
      };

      // 子级元素属性
      let childrenAttr = props.data && props.data.childrenAttr ? props.data.childrenAttr : {};

      // string number textarea password
      const sntp = () => {
        return () =>
          createVNode(resolveComponent('el-input'), {
            type: props.type == 'string' ? 'text' : props.type,
            ...props.attr,
            modelValue: props.modelValue,
            'onUpdate:modelValue': onValueUpdate,
          });
      };
      // radio checkbox
      const rc = () => {
        if (!props.data || !props.data.content) {
          console.warn('请传递 ' + props.type + '的 content');
        }
        let vNode: VNode[] = [];
        for (const key in props.data.content) {
          vNode.push(
            createVNode(
              resolveComponent('el-' + props.type),
              {
                label: key,
                ...childrenAttr,
              },
              () => props.data.content[key],
            ),
          );
        }
        return () => {
          const valueComputed = computed(() => {
            if (props.type == 'radio') {
              if (props.modelValue == undefined) return '';
              return '' + props.modelValue;
            } else {
              let modelValueArr: anyObj = [];
              for (const key in props.modelValue) {
                modelValueArr[key] = '' + props.modelValue[key];
              }
              return modelValueArr;
            }
          });
          return createVNode(
            resolveComponent('el-' + props.type + '-group'),
            {
              ...props.attr,
              modelValue: valueComputed.value,
              'onUpdate:modelValue': onValueUpdate,
            },
            () => vNode,
          );
        };
      };
      // select selects
      const select = () => {
        let vNode: VNode[] = [];
        if (!props.data || !props.data.content) {
          console.warn('请传递 ' + props.type + '的 content');
        }
        for (const key in props.data.content) {
          vNode.push(
            createVNode(resolveComponent('el-option'), {
              key: key,
              label: props.data.content[key],
              value: key,
              ...childrenAttr,
            }),
          );
        }
        return () => {
          const valueComputed = computed(() => {
            if (props.type == 'select') {
              if (props.modelValue == undefined) return '';
              return '' + props.modelValue;
            } else {
              let modelValueArr: anyObj = [];
              for (const key in props.modelValue) {
                modelValueArr[key] = '' + props.modelValue[key];
              }
              return modelValueArr;
            }
          });
          return createVNode(
            resolveComponent('el-select'),
            {
              class: 'w100',
              multiple: props.type != 'select',
              clearable: true,
              ...props.attr,
              modelValue: valueComputed.value,
              'onUpdate:modelValue': onValueUpdate,
            },
            () => vNode,
          );
        };
      };
      // datetime
      const datetime = () => {
        let valueFormat = 'YYYY-MM-DD HH:mm:ss';
        switch (props.type) {
          case 'date':
            valueFormat = 'YYYY-MM-DD';
            break;
          case 'year':
            valueFormat = 'YYYY';
            break;
        }
        return () =>
          createVNode(resolveComponent('el-date-picker'), {
            class: 'w100',
            type: props.type,
            'value-format': valueFormat,
            ...props.attr,
            modelValue: props.modelValue,
            'onUpdate:modelValue': onValueUpdate,
          });
      };

      const buildFun = new Map([
        ['string', sntp],
        ['number', sntp],
        ['textarea', sntp],
        ['password', sntp],
        ['radio', rc],
        ['checkbox', rc],
        [
          'switch',
          () => {
            const valueType = computed(() => typeof props.modelValue);
            const valueComputed = computed(() => {
              if (valueType.value === 'boolean') {
                return props.modelValue;
              } else {
                let valueTmp = parseInt(props.modelValue as string);
                return !(isNaN(valueTmp) || valueTmp <= 0);
              }
            });
            return () =>
              createVNode(resolveComponent('el-switch'), {
                ...props.attr,
                modelValue: valueComputed.value,
                'onUpdate:modelValue': (value: boolean) => {
                  let newValue: boolean | string | number = value;
                  switch (valueType.value) {
                    case 'string':
                      newValue = value ? '1' : '0';
                      break;
                    case 'number':
                      newValue = value ? 1 : 0;
                  }
                  emit('update:modelValue', newValue);
                },
              });
          },
        ],
        ['datetime', datetime],
        [
          'year',
          () => {
            return () => {
              const valueComputed = computed(() => (!props.modelValue ? null : '' + props.modelValue));
              return createVNode(resolveComponent('el-date-picker'), {
                class: 'w100',
                type: props.type,
                'value-format': 'YYYY',
                ...props.attr,
                modelValue: valueComputed.value,
                'onUpdate:modelValue': onValueUpdate,
              });
            };
          },
        ],
        ['date', datetime],
        [
          'time',
          () => {
            const valueComputed = computed(() => {
              if (props.modelValue instanceof Date) {
                return props.modelValue;
              } else if (!props.modelValue) {
                return '';
              } else {
                let date = new Date();
                return new Date(date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' + props.modelValue);
              }
            });
            return () =>
              createVNode(resolveComponent('el-time-picker'), {
                class: 'w100',
                clearable: true,
                format: 'HH:mm:ss',
                ...props.attr,
                modelValue: valueComputed.value,
                'onUpdate:modelValue': onValueUpdate,
              });
          },
        ],
        ['select', select],
        ['selects', select],
        [
          'array',
          () => {
            return () =>
              createVNode(Array, {
                modelValue: props.modelValue,
                'onUpdate:modelValue': onValueUpdate,
                ...props.attr,
              });
          },
        ],
        [
          'icon',
          () => {
            return () =>
              createVNode(IconSelector, {
                modelValue: props.modelValue,
                'onUpdate:modelValue': onValueUpdate,
                ...props.attr,
              });
          },
        ],
        [
          'color',
          () => {
            return () =>
              createVNode(resolveComponent('el-color-picker'), {
                modelValue: props.modelValue,
                'onUpdate:modelValue': onValueUpdate,
                ...props.attr,
              });
          },
        ],
        [
          'editor',
          () => {
            return () =>
              createVNode(Editor, {
                modelValue: props.modelValue,
                'onUpdate:modelValue': onValueUpdate,
                ...props.attr,
              });
          },
        ],
        [
          'default',
          () => {
            console.warn('暂不支持' + props.type + '的输入框类型，你可以自行在 BaInput 组件内添加逻辑');
          },
        ],
      ]);

      let action = buildFun.get(props.type) || buildFun.get('default');
      return action!.call(this);
    },
  });
</script>

<style scoped lang="scss">
  .ba-upload-image :deep(.el-upload--picture-card) {
    display: inline-flex;
    align-items: center;
    justify-content: center;
  }

  .ba-upload-file :deep(.el-upload-list) {
    margin-left: -10px;
  }
</style>
