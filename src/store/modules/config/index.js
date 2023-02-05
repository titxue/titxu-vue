import { defineStore } from 'pinia';
import piniaStore from '/@/store/index';
import { STORE_CONFIG } from '/@/store/modules/constant/cacheKey';
export const useConfigStore = defineStore(
  'config',
  () => {
    const layout = reactive({
      showDrawer: false,
      shrink: false,
      layoutMode: 'Default',
      mainAnimation: 'slide-right',
      isDark: false,
      menuBackground: ['#ffffff', '#1d1e1f'],
      menuColor: ['#303133', '#CFD3DC'],
      menuActiveBackground: ['#ffffff', '#1d1e1f'],
      menuActiveColor: ['#409eff', '#3375b9'],
      menuTopBarBackground: ['#fcfcfc', '#1d1e1f'],
      menuWidth: 260,
      menuDefaultIcon: 'el-icon-Minus',
      menuCollapse: false,
      menuUniqueOpened: false,
      menuShowTopBar: true,
      headerBarTabColor: ['#000000', '#CFD3DC'],
      headerBarTabActiveBackground: ['#ffffff', '#1d1e1f'],
      headerBarTabActiveColor: ['#000000', '#409EFF'],
      headerBarBackground: ['#ffffff', '#1d1e1f'],
      headerBarHoverBackground: ['#f5f5f5', '#18222c'],
    });
    const lang = reactive({
      defaultLang: 'zh-cn',
      fallbackLang: 'zh-cn',
      langArray: [
        { name: 'zh-cn', value: '中文简体' },
        { name: 'en', value: 'English' },
      ],
    });
    function menuWidth() {
      if (layout.shrink) {
        return layout.menuCollapse ? '0px' : layout.menuWidth + 'px';
      }
      return layout.menuCollapse ? '64px' : layout.menuWidth + 'px';
    }
    function setLang(val) {
      lang.defaultLang = val;
    }
    function onSetLayoutColor(data = layout.layoutMode) {
      const tempValue = layout.isDark
        ? { idx: 1, color: '#1d1e1f', newColor: '#141414' }
        : { idx: 0, color: '#ffffff', newColor: '#f5f5f5' };
      if (
        data == 'Classic' &&
        layout.headerBarBackground[tempValue.idx] == tempValue.color &&
        layout.headerBarTabActiveBackground[tempValue.idx] == tempValue.color
      ) {
        layout.headerBarTabActiveBackground[tempValue.idx] = tempValue.newColor;
      } else if (
        data == 'Default' &&
        layout.headerBarBackground[tempValue.idx] == tempValue.color &&
        layout.headerBarTabActiveBackground[tempValue.idx] == tempValue.newColor
      ) {
        layout.headerBarTabActiveBackground[tempValue.idx] = tempValue.color;
      }
    }
    function setLayoutMode(data) {
      layout.layoutMode = data;
      onSetLayoutColor(data);
    }
    const setLayout = (name, value) => {
      layout[name] = value;
    };
    const getColorVal = function (name) {
      const colors = layout[name];
      if (layout.isDark) {
        return colors[1];
      } else {
        return colors[0];
      }
    };
    return { layout, lang, menuWidth, setLang, setLayoutMode, setLayout, getColorVal, onSetLayoutColor };
  },
  {
    persist: {
      key: STORE_CONFIG,
    },
  },
);
export function useConfigOutsideStore() {
  return useConfigStore(piniaStore);
}
//# sourceMappingURL=index.js.map
