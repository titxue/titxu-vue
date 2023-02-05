export function tranListToTreeData(list, rootValue) {
  const arr = [];
  list.forEach((item) => {
    if (item.parentId === rootValue) {
      const children = tranListToTreeData(list, item.id);
      if (children.length) {
        item.children = children;
      }
      arr.push(item);
    }
  });
  return arr;
}
export function changeKey(arr, key, newKey) {
  const newArr = [];
  arr.forEach((item, _index) => {
    let newObj = {};
    if (item[key] !== undefined) {
      newObj[newKey] = item[key];
      delete item[key];
    }
    newObj = Object.assign(newObj, item);
    newArr.push(newObj);
  });
  return newArr;
}
export function isNumber(val) {
  const regPos = /^\d+(\.\d+)?$/;
  const regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/;
  if (regPos.test(val) && regNeg.test(val)) {
    return true;
  } else {
    return false;
  }
}
export function setPaginationOptions(page, options) {
  options.paginationConfig = {
    currentPage: page.currPage,
    pageSize: page.pageSize,
    total: page.totalCount,
  };
}
//# sourceMappingURL=index.js.map
