<template>
  <transition name="el-fade-in">
    <div class="table-com-search">
      <el-form @submit.prevent="" @keyup.enter="onComSearch" label-position="top" :model="xTable.comSearch.form">
        <el-row>
          <template v-for="(item, _idx) in xTable.table.column" :key="_idx">
            <template v-if="item.operator !== false">
              <!-- 时间范围 -->
              <el-col v-if="item.render === 'datetime' && (item.operator === 'RANGE' || item.operator === 'NOT RANGE')" :xs="24" :sm="12">
                <div class="com-search-col">
                  <div class="com-search-col-label w16">{{ item.label }}</div>
                  <div class="com-search-col-input-range w83">
                    <el-date-picker
                      class="datetime-picker"
                      v-model="xTable.comSearch.form[item.prop!]"
                      :default-value="xTable.comSearch.form[item.prop! + '-default'] ? xTable.comSearch.form[item.prop! + '-default']: [new Date(), new Date()]"
                      :type="item.comSearchRender === 'date' ? 'daterange' : 'datetimerange'"
                      range-separator="区间"
                      start-placeholder="开始"
                      end-placeholder="接收"
                      :value-format="item.comSearchRender === 'date' ? 'YYYY-MM-DD' : 'YYYY-MM-DD HH:mm:ss'"
                    />
                  </div>
                </div>
              </el-col>
              <el-col v-else :xs="24" :sm="6">
                <div class="com-search-col">
                  <div class="com-search-col-label">{{ item.label }}</div>
                  <!-- 数字范围 -->
                  <div v-if="item.operator === 'RANGE' || item.operator === 'NOT RANGE'" class="com-search-col-input-range">
                    <el-input
                      :placeholder="item.operatorPlaceholder"
                      type="string"
                      v-model="xTable.comSearch.form[item.prop! + '-start']"
                      :clearable="true"
                    />
                    <div class="range-separator">区间</div>
                    <el-input
                      :placeholder="item.operatorPlaceholder"
                      type="string"
                      v-model="xTable.comSearch.form[item.prop! + '-end']"
                      :clearable="true"
                    />
                  </div>
                  <!-- 是否 [NOT] NULL -->
                  <div v-else-if="item.operator === 'NULL' || item.operator === 'NOT NULL'" class="com-search-col-input">
                    <el-checkbox v-model="xTable.comSearch.form[item.prop!]" :label="item.operator" size="large" />
                  </div>
                  <div v-else-if="item.operator" class="com-search-col-input">
                    <!-- 时间筛选 -->
                    <el-date-picker
                      class="datetime-picker"
                      v-if="item.render === 'datetime' || item.comSearchRender === 'date'"
                      v-model="xTable.comSearch.form[item.prop!]"
                      :type="item.comSearchRender === 'date' ? 'date' : 'datetime'"
                      :value-format="item.comSearchRender === 'date' ? 'YYYY-MM-DD' : 'YYYY-MM-DD HH:mm:ss'"
                      :placeholder="item.operatorPlaceholder"
                      :default-value="xTable.comSearch.form[item.prop! + '-default'] ? xTable.comSearch.form[item.prop! + '-default']:new Date()"
                    />

                    <!-- tag、select -->
                    <el-select
                      class="w100"
                      :placeholder="item.operatorPlaceholder"
                      v-else-if="(item.render === 'tag' || item.comSearchRender === 'select') && item.replaceValue"
                      v-model="xTable.comSearch.form[item.prop!]"
                      :clearable="true"
                    >
                      <el-option v-for="(opt, okey) in item.replaceValue" :key="item.prop! + okey" :label="opt" :value="okey" />
                    </el-select>

                    <!-- 远程 select -->
                    <xInput
                      v-else-if="item.comSearchRender === 'remoteSelect'"
                      type="remoteSelect"
                      v-model="xTable.comSearch.form[item.prop!]"
                      :attr="item.remote"
                      :placeholder="item.operatorPlaceholder"
                    />

                    <!-- 自定义组件/函数渲染 -->
                    <component
                      v-else-if="item.comSearchRender === 'customRender'"
                      :is="item.comSearchCustomRender"
                      :renderRow="item"
                      :renderField="item.prop!"
                      :renderValue="xTable.comSearch.form[item.prop!]"
                    />

                    <!-- 开关 -->
                    <el-select
                      :placeholder="item.operatorPlaceholder"
                      v-else-if="item.render === 'switch'"
                      v-model="xTable.comSearch.form[item.prop!]"
                      :clearable="true"
                      class="w100"
                    >
                      <template v-if="!isEmpty(item.replaceValue)">
                        <el-option v-for="(opt, okey) in item.replaceValue" :key="item.prop! + okey" :label="opt" :value="okey" />
                      </template>
                      <template v-else>
                        <el-option label="打开" value="1" />
                        <el-option label="取消" value="0" />
                      </template>
                    </el-select>

                    <!-- 字符串 -->
                    <el-input
                      :placeholder="item.operatorPlaceholder"
                      v-else
                      type="string"
                      v-model="xTable.comSearch.form[item.prop!]"
                      :clearable="true"
                    />
                  </div>
                </div>
              </el-col>
            </template>
          </template>
          <el-col :xs="24" :sm="6">
            <div class="com-search-col pl-20">
              <el-button @click="onComSearch" type="primary">搜索</el-button>
              <el-button @click="onResetForm()">重置</el-button>
            </div>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </transition>
</template>

<script setup lang="ts">
  import { inject } from 'vue';
  import type xTableClass from '/@/utils/xTable';
  import { isEmpty } from 'lodash-es';
  import xInput from '/@/components/v1/xInput/index.vue';

  const xTable = inject('xTable') as xTableClass<anyObj>;

  const onComSearch = () => {
    let comSearchData: comSearchData[] = [];
    for (const key in xTable.comSearch.form) {
      if (!xTable.comSearch.fieldData.has(key)) {
        continue;
      }

      let val = '';
      let fieldDataTemp = xTable.comSearch.fieldData.get(key);
      if (fieldDataTemp.render == 'datetime' && (fieldDataTemp.operator == 'RANGE' || fieldDataTemp.operator == 'NOT RANGE')) {
        // 时间范围组件返回的是时间数组
        if (xTable.comSearch.form[key] && xTable.comSearch.form[key].length >= 2) {
          // 数组转字符串，以实现通过url参数传递预设搜索值
          if (fieldDataTemp.comSearchRender == 'date') {
            val = xTable.comSearch.form[key][0] + ' 00:00:00' + ',' + xTable.comSearch.form[key][1] + ' 23:59:59';
          } else {
            val = xTable.comSearch.form[key][0] + ',' + xTable.comSearch.form[key][1];
          }
        }
      } else if (fieldDataTemp.operator == 'RANGE' || fieldDataTemp.operator == 'NOT RANGE') {
        // 普通的范围筛选，xTable在初始化时已准备好start和end字段
        if (!xTable.comSearch.form[key + '-start'] && !xTable.comSearch.form[key + '-end']) {
          continue;
        }
        val = xTable.comSearch.form[key + '-start'] + ',' + xTable.comSearch.form[key + '-end'];
      } else if (xTable.comSearch.form[key]) {
        val = xTable.comSearch.form[key];
      }

      if (val) {
        comSearchData.push({
          field: key,
          val: val,
          operator: fieldDataTemp.operator,
          render: fieldDataTemp.render,
        });
      }
    }

    xTable.onTableAction('com-search', comSearchData);
  };

  const onResetForm = () => {
    // 封装好的onResetForm在此处不能使用
    for (const key in xTable.comSearch.form) {
      xTable.comSearch.form[key] = '';
    }
  };
</script>

<style scoped lang="scss">
  .table-com-search {
    overflow: hidden;
    box-sizing: border-box;
    width: 100%;
    max-width: 100%;
    background-color: var(--ba-bg-color-overlay);
    border: 1px solid var(--ba-border-color);
    border-bottom: none;
    padding: 13px 15px;
    font-size: 14px;

    .com-search-col {
      display: flex;
      align-items: center;
      padding-top: 8px;
      color: var(--el-text-color-regular);
      font-size: 13px;
    }

    .com-search-col-label {
      width: 33.33%;
      padding: 0 15px;
      text-align: right;
      overflow: hidden;
      white-space: nowrap;
    }

    .com-search-col-input {
      padding: 0 15px;
      width: 66.66%;
    }

    .com-search-col-input-range {
      display: flex;
      align-items: center;
      padding: 0 15px;
      width: 66.66%;

      .range-separator {
        padding: 0 5px;
      }
    }
  }

  :deep(.datetime-picker) {
    width: 100%;
  }

  .pl-20 {
    padding-left: 20px;
  }

  .w16 {
    width: 16.5% !important;
  }

  .w83 {
    width: 83.5% !important;
  }
</style>
