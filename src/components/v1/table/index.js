export const openUrl = (url, field) => {
  if (field.target == '_blank') {
    window.open(url);
  } else {
    window.location.href = url;
  }
};
export const defaultOptButtons = (optButType = ['weigh-sort', 'edit', 'delete']) => {
  const optButtonsPre = new Map([
    [
      'weigh-sort',
      {
        render: 'moveButton',
        name: 'weigh-sort',
        title: 'weigh-sort',
        text: '',
        type: 'info',
        icon: 'fa fa-arrows',
        class: 'table-row-weigh-sort',
        disabledTip: false,
      },
    ],
    [
      'edit',
      {
        render: 'tipButton',
        name: 'edit',
        title: 'edit',
        text: '',
        type: 'primary',
        icon: 'fa fa-pencil',
        class: 'table-row-edit',
        disabledTip: false,
      },
    ],
    [
      'delete',
      {
        render: 'confirmButton',
        name: 'delete',
        title: 'delete',
        text: '',
        type: 'danger',
        icon: 'fa fa-trash',
        class: 'table-row-delete',
        popconfirm: {
          confirmButtonText: '删除',
          cancelButtonText: '取消',
          confirmButtonType: 'danger',
          title: '确定删除选中的数据吗?',
        },
        disabledTip: false,
      },
    ],
  ]);
  const optButtons = [];
  for (const key in optButType) {
    if (optButtonsPre.has(optButType[key])) {
      optButtons.push(optButtonsPre.get(optButType[key]));
    }
  }
  return optButtons;
};
export const timeFormat = (dateTime = null, fmt = 'yyyy-mm-dd hh:MM:ss') => {
  if (dateTime == 'none') return 'none';
  if (!dateTime) dateTime = Number(new Date());
  if (dateTime.toString().length === 10) {
    dateTime = +dateTime * 1000;
  }
  const date = new Date(dateTime);
  let ret;
  const opt = {
    'y+': date.getFullYear().toString(),
    'm+': (date.getMonth() + 1).toString(),
    'd+': date.getDate().toString(),
    'h+': date.getHours().toString(),
    'M+': date.getMinutes().toString(),
    's+': date.getSeconds().toString(),
  };
  for (const k in opt) {
    ret = new RegExp('(' + k + ')').exec(fmt);
    if (ret) {
      fmt = fmt.replace(ret[1], ret[1].length == 1 ? opt[k] : padStart(opt[k], ret[1].length, '0'));
    }
  }
  return fmt;
};
const padStart = (str, maxLength, fillString = ' ') => {
  if (str.length >= maxLength) return str;
  const fillLength = maxLength - str.length;
  let times = Math.ceil(fillLength / fillString.length);
  while ((times >>= 1)) {
    fillString += fillString;
    if (times === 1) {
      fillString += fillString;
    }
  }
  return fillString.slice(0, fillLength) + str;
};
export const findIndexRow = (data, findIdx, keyIndex = -1) => {
  for (const key in data) {
    if (typeof keyIndex == 'number') {
      keyIndex++;
    }
    if (keyIndex == findIdx) {
      return data[key];
    }
    if (data[key].children) {
      keyIndex = findIndexRow(data[key].children, findIdx, keyIndex);
      if (typeof keyIndex != 'number') {
        return keyIndex;
      }
    }
  }
  return keyIndex;
};
//# sourceMappingURL=index.js.map
