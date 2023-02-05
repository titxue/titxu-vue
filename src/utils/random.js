const hexList = [];
for (let i = 0; i <= 15; i++) {
  hexList[i] = i.toString(16);
}
export function randomNum(min, max) {
  switch (arguments.length) {
    case 1:
      return parseInt((Math.random() * min + 1).toString(), 10);
      break;
    case 2:
      return parseInt((Math.random() * (max - min + 1) + min).toString(), 10);
      break;
    default:
      return 0;
      break;
  }
}
export function uuid() {
  let uuid = '';
  for (let i = 1; i <= 36; i++) {
    if (i === 9 || i === 14 || i === 19 || i === 24) {
      uuid += '-';
    } else if (i === 15) {
      uuid += 4;
    } else if (i === 20) {
      uuid += hexList[(Math.random() * 4) | 8];
    } else {
      uuid += hexList[(Math.random() * 16) | 0];
    }
  }
  return uuid;
}
export function shortUuid(prefix = '') {
  const time = Date.now();
  const random = Math.floor(Math.random() * 1000000000);
  if (!window.unique) window.unique = 0;
  window.unique++;
  return prefix + '_' + random + window.unique + String(time);
}
//# sourceMappingURL=random.js.map
