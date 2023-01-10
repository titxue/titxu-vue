<p align="center" style="background:#e6e6e6;padding:20px">
    <h1 align="center">vue+ts 后台</h1>
</p>

<p align="center">
    <img src="https://img.shields.io/badge/-Vue3-34495e?logo=vue.j" />
    <img src="https://img.shields.io/badge/-Vite2.7-646cff?logo=vite&logoColor=white" />
    <img src="https://img.shields.io/badge/-TypeScript-blue?logo=typescript&logoColor=white" />
    <img src="https://img.shields.io/badge/-Pinia-yellow?logo=picpay&logoColor=white" />
    <img src="https://img.shields.io/badge/-ESLint-4b32c3?logo=eslint&logoColor=white" />
    <img src="https://img.shields.io/badge/-pnpm-F69220?logo=pnpm&logoColor=white" />
    <img src="https://img.shields.io/badge/-Axios-008fc7?logo=axios.js&logoColor=white" />
    <img src="https://img.shields.io/badge/-Prettier-ef9421?logo=Prettier&logoColor=white" alt="Prettier">
    <img src="https://img.shields.io/badge/-Less-1D365D?logo=less&logoColor=white" alt="Less">
    <img src="https://img.shields.io/badge/-Tailwind%20CSS-06B6D4?logo=Tailwind%20CSS&logoColor=white" alt="Taiwind">
    <img src="" alt="">
</p>

## 🪂 大厂协作-代码规范

🪁 目前多数大厂团队一般使用[husky](https://github.com/typicode/husky)和  [lint-staged](https://github.com/okonet/lint-staged) 来约束代码规范，

- 通过`pre-commit`实现 lint 检查、单元测试、代码格式化等。
- 结合 VsCode 编辑器（保存时自动执行格式化：editor.formatOnSave: true）
- 配合 Git hooks 钩子（commit 前或提交前执行：pre-commit => npm run lint:lint-staged）
- IDE 配置（`.editorconfig`）、ESLint 配置（`.eslintrc.js`  和  `.eslintignore`）、StyleLint 配置（`.stylelintrc`  和  `.stylelintignore`），详细请看对应的配置文件。

🔌 关闭代码规范  
将  `src/`  目录分别加入  `.eslintignore`  和  `.stylelintignore`  进行忽略即可。

## 目录结构

以下是系统的目录结构

```
├── config
│   ├── vite             // vite配置
│   ├── constant         // 系统常量
|   └── themeConfig      // 主题配置
├── docs                 // 文档相关
├── mock                 // mock数据
├── plop-tpls            // plop模板
├── src
│    ├── api             // api请求
│    ├── assets          // 静态文件
│    ├── components      // 业务通用组件
│    ├── page            // 业务页面
│    ├── router          // 路由文件
│    ├── store           // 状态管理
│    ├── utils           // 工具类
│    ├── App.vue         // vue模板入口
│    ├── main.ts         // vue模板js
├── .d.ts                // 类型定义
├── tailwind.config.js   // tailwind全局配置
├── tsconfig.json        // ts配置
└── vite.config.ts       // vite全局配置
```
