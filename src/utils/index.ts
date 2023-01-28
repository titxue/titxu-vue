// import { resolve } from 'path';
// import fs from 'fs';

// function pathResolve(dir: string) {
//   return resolve(process.cwd(), '.', dir);
// }

// export const getFolder = (path: any) => {
//   const components: Array<string> = [];
//   const files = fs.readdirSync(path);
//   files.forEach(function (item: string) {
//     const stat = fs.lstatSync(path + '/' + item);
//     if (stat.isDirectory() === true && item != 'components') {
//       components.push(path + '/' + item);
//       components.push(pathResolve(path + '/' + item));
//     }
//   });
//   return components;
// };

/** *
 *
 *  将列表型的数据转化成树形数据 => 递归算法 => 自身调用自身 => 一定条件不能一样， 否则就会死循环
 *  遍历树形 有一个重点 要先找一个头儿
 * ***/
export function tranListToTreeData(list: any, rootValue: any) {
  //定义个空数组接收参数
  const arr: any = [];
  list.forEach((item: { parentId: any; id: any; children: any }) => {
    if (item.parentId === rootValue) {
      // 找到之后 就要去找 item 下面有没有子节点
      //条件不能是(list, rootValue),否则死递归
      const children = tranListToTreeData(list, item.id);
      if (children.length) {
        // 如果children的长度大于0 说明找到了子节点
        item.children = children;
      }
      arr.push(item); // 把找到的数据内容加入到数组中
    }
  });
  //返回这个数组
  return arr;
}

export function changeKey(arr: Array<any>, key: string, newKey: string) {
  const newArr: Array<any> = [];
  arr.forEach((item, _index) => {
    let newObj: object = {};
    if (item[key] !== undefined) {
      newObj[newKey] = item[key];
      delete item[key];
    }
    // 合并对象除了key以外的属性
    newObj = Object.assign(newObj, item);
    newArr.push(newObj);
  });
  return newArr;
}

export function isNumber(val: string) {
  const regPos = /^\d+(\.\d+)?$/;
  const regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/;
  if (regPos.test(val) && regNeg.test(val)) {
    return true;
  } else {
    return false;
  }
}
