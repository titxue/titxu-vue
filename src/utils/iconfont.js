import { nextTick } from 'vue';
import { loadCss, loadJs } from './common';
import * as elIcons from '@element-plus/icons-vue';
const cssUrls = [
  '//at.alicdn.com/t/font_3135462_5axiswmtpj.css',
  '//cdn.bootcdn.net/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css',
];
const jsUrls = [];
export default function init() {
  if (cssUrls.length > 0) {
    cssUrls.map((v) => {
      loadCss(v);
    });
  }
  if (jsUrls.length > 0) {
    jsUrls.map((v) => {
      loadJs(v);
    });
  }
}
function getStylesFromDomain(domain) {
  const sheets = [];
  const styles = document.styleSheets;
  for (const key in styles) {
    if (styles[key].href && styles[key].href.indexOf(domain) > -1) {
      sheets.push(styles[key]);
    }
  }
  return sheets;
}
export function getLocalIconfontNames() {
  return new Promise((resolve, reject) => {
    nextTick(() => {
      let iconfonts = [];
      const svgEl = document.getElementById('local-icon');
      if (svgEl?.dataset.iconName) {
        iconfonts = (svgEl?.dataset.iconName).split(',');
      }
      if (iconfonts.length > 0) {
        resolve(iconfonts);
      } else {
        reject('No Local Icons');
      }
    });
  });
}
export function getAwesomeIconfontNames() {
  return new Promise((resolve, reject) => {
    nextTick(() => {
      const iconfonts = [];
      const sheets = getStylesFromDomain('cdn.bootcdn.net/ajax/libs/font-awesome/');
      for (const key in sheets) {
        const rules = sheets[key].cssRules;
        for (const k in rules) {
          if (rules[k].selectorText && /^\.fa-(.*)::before$/g.test(rules[k].selectorText)) {
            if (rules[k].selectorText.indexOf(', ') > -1) {
              const iconNames = rules[k].selectorText.split(', ');
              iconfonts.push(`${iconNames[0].substring(1, iconNames[0].length).replace(/::before/gi, '')}`);
            } else {
              iconfonts.push(`${rules[k].selectorText.substring(1, rules[k].selectorText.length).replace(/::before/gi, '')}`);
            }
          }
        }
      }
      if (iconfonts.length > 0) {
        resolve(iconfonts);
      } else {
        reject('No AwesomeIcon style sheet');
      }
    });
  });
}
export function getIconfontNames() {
  return new Promise((resolve, reject) => {
    nextTick(() => {
      const iconfonts = [];
      const sheets = getStylesFromDomain('at.alicdn.com');
      for (const key in sheets) {
        const rules = sheets[key].cssRules;
        for (const k in rules) {
          if (rules[k].selectorText && /^\.icon-(.*)::before$/g.test(rules[k].selectorText)) {
            iconfonts.push(`${rules[k].selectorText.substring(1, rules[k].selectorText.length).replace(/::before/gi, '')}`);
          }
        }
      }
      if (iconfonts.length > 0) {
        resolve(iconfonts);
      } else {
        reject('No Iconfont style sheet');
      }
    });
  });
}
export function getElementPlusIconfontNames() {
  return new Promise((resolve, reject) => {
    nextTick(() => {
      const iconfonts = [];
      const icons = elIcons;
      for (const i in icons) {
        iconfonts.push(`el-icon-${icons[i].name}`);
      }
      if (iconfonts.length > 0) {
        resolve(iconfonts);
      } else {
        reject('No ElementPlus Icons');
      }
    });
  });
}
//# sourceMappingURL=iconfont.js.map
