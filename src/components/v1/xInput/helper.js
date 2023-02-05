export const stringToArray = (val) => {
  if (typeof val === 'string') {
    return val == '' ? [] : val.split(',');
  } else {
    return val;
  }
};
//# sourceMappingURL=helper.js.map
