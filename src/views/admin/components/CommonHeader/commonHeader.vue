<template>
  <el-header>
      <div class="l-content">
          <!-- 这个button本来应该是有用途的，但是与本笔记主体内容无关，所以去除了，只保留样式 -->
          <!-- <el-button size="small" plain>
              <el-icon :size="20">
                  <Menu />
              </el-icon>
          </el-button> -->
      </div>
      <div class="r-content">
          <el-dropdown>
              <span class="el-dropdown-link">
                  <img class="user" :src="getImgSrc()" alt="头像" />
                  <span class="user-name">用户名</span>
              </span>
              <template #dropdown>
                  <el-dropdown-menu>
                      <el-dropdown-item>我的</el-dropdown-item>
                      <el-dropdown-item>退出</el-dropdown-item>
                  </el-dropdown-menu>
              </template>
          </el-dropdown>
          
      </div>
  </el-header>
</template>


<script>
export default {
  setup() {
      let getImgSrc = () => {
          // 参考https://cn.vitejs.dev/guide/assets.html#new-url-url-import-meta-url
          // console.log(import.meta.url)
          // console.log(new URL("../assets/images/user.png", import.meta.url))
          return new URL("../../../../assets/images/user.jpg", import.meta.url).href;
      };
      return {
          getImgSrc,
      }
  }
}
</script>

<style lang="less" scoped>
header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 56px;
  background: rgba(255, 255, 255, 1);
  box-shadow: 0px 1px 4px 0px rgba(0, 21, 41, 0.12);
}
.l-content {
  display: flex;
  align-items: center;
  .el-button {
      margin-right: 16px;
      margin-left: 10px;
  }
  h3 {
      color: #fff;
  }
}
.r-content {
  .user {
      width: 24px;
      height: 24px;
      border-radius: 50%;
      margin-right: 10px;
  }
  .el-dropdown-link{
    // header 用户信息相对右边的距离
    margin-right: 10px;
  }
  .user-name {
      /** 文本1 */
      font-size: 14px;
      font-weight: 400;
      letter-spacing: 0px;
      line-height: 22px;
      color: rgba(0, 0, 0, 0.65);
      text-align: left;
      vertical-align: top;
  }
}

</style>