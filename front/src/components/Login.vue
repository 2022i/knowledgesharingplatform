<script setup>
import {ref} from 'vue';
import {Lock, User, } from "@element-plus/icons-vue";
import {ElIcon} from "element-plus";
import {useRouter} from "vue-router";


const ruleForm = ref({
  userName: '',
  password: ''
});

const router = useRouter();
const submit =() => {
  router.push('/dashBoard')

}

// const submit = async () => { // 定义 submit 方法
//   console.log('submit method triggered');
//   console.log('Username:', this.inputValue, 'Password:', this.password);
//
//   try {
//     const response = await axios.post('http://localhost:8081/server/login/loginByUsername', {
//       username: this.inputValue,
//       password: this.password
//     }, {
//       headers: {
//         'Content-Type': 'application/json'
//       }
//     });
//
//     if (response.data.code === 200) {
//       this.inputValue = '';
//       this.password = '';
//
//       // 保存 token 到本地存储
//       localStorage.setItem('token', response.data.data);
//
//       this.$message({
//         message: '登录成功！',
//         type: 'success',
//         duration: 2000
//       });
//
//       // 使用路由跳转到 dashboard 页面
//       this.$router.push({name: 'Dashboard'});
//     } else if (response.data.code === 401) {
//       this.$message({
//         message: '密码错误！',
//         type: 'error',
//         duration: 2000
//       });
//     } else if (response.data.code === 404) {
//       this.$message({
//         message: '不存在该用户！',
//         type: 'error',
//         duration: 2000
//       });
//     } else {
//       this.$message({
//         message: '登录失败，请重试！',
//         type: 'error',
//         duration: 2000
//       });
//     }
//   } catch (error) {
//     console.error('登录失败:', error);
//     this.$message({
//       message: '登录失败，请重试！',
//       type: 'error',
//       duration: 2000
//     });
//   }
// };

const showPassword = ref(false);

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value;
};

</script>

<template>
  <div class="background-container">
    <h1 class="title">目标检测系统</h1>
    <div class="content-container">
      <h2 class="MyName">登陆</h2>
       <el-form :model="ruleForm">
         <el-form-item>
           <el-input v-model="ruleForm.userName" clearable placeholder="用户名">
             <template #prepend>
               <el-icon style="color: red;"><User /></el-icon>
             </template>
           </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input :type="showPassword ? 'text' : 'password'"  v-model="ruleForm.password"  placeholder="密码">
              <template #prepend>
                <el-icon style="color: red"><Lock /></el-icon>
              </template>
              <template #suffix>
                <el-icon @mousedown="togglePasswordVisibility" style="cursor: pointer; color: #333;">
                  <component :is="showPassword ? 'View' : 'Hide'"/>
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
      <div class="links-container">  <!-- 注册和忘记密码的容器 -->
        <router-link to="/register" class="register-link" >立即注册</router-link>
        <router-link to="/resetPassword" class="resetPassword-link">忘记密码</router-link>
      </div>
      <div style="margin-top: 50px;"></div>
      <button class="submitButton" @click="submit">登录</button>  <!-- 登录按钮 -->
      <div style="margin-top: 20px;" ></div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.background-container {
  position: relative;
  background-image: url('@/assets/score-bg.jpg'); /* 背景图的路径 */
  background-size: cover; /* 背景图填充整个容器 */
  background-position: center center; /* 背景图居中对齐 */
  height: 100vh; /* 全屏高度 */
  display: flex;
  align-items: center;
  justify-content: center;
}

.title{
  position: absolute;
  top: 10px;
  left: 10px;
  text-align: left;
}

.content-container {
  background-color: rgba(255, 255, 255, 0.5); /* 白色半透明背景 */
  padding: 20px; /* 内边距 */
  border-radius: 10px; /* 圆角 */
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1); /* 阴影效果 */
  max-width: 400px; /* 最大宽度 */
  width: 100%; /* 宽度100% */
}

.MyName {
  font-size: 24px;
  margin-bottom: 20px;
  position: relative;
  top: 0;
  left: 0;
  text-align: center;
}

.InputText {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.inputUsername , .inputPassword {
  width: 100%; /* 设置输入框的宽度为100% */
  margin-bottom: 10px; /* 设置输入框底部的外边距为10px */
  height: 40px; /* 设置输入框的高度为40px */
  padding: 0 10px; /* 设置输入框的内边距，左右各10px */
  background-color: transparent; /* 设置输入框的背景颜色为透明 */
  border: 1px solid gray; /* 设置输入框的边框为1px的灰色实线 */
  border-radius: 2px; /* 设置输入框的边框圆角为2px */
  box-shadow: 0 0 5px #3f4548; /* 设置输入框的阴影效果 */
  transition: all 0.3s ease-in-out; /* 设置输入框所有属性的过渡效果，持续时间为0.3秒 */
}

.inputUsername:hover, .inputPassword:hover {
  transform: scale(1.05); /* 设置输入框的放大效果 */
  box-shadow: 0 0 15px rgba(24, 144, 255, 0.8); /* 设置输入框的阴影效果 */
}

.links-container {
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-top: 10px;
}

.register-link,
.resetPassword-link {
  color: black;
  text-decoration: none;
  font-size: 14px;
  transition: transform 0.3s ease-in-out;
}

.register-link:hover,
.resetPassword-link:hover {
  transform: scale(1.1);
  cursor: pointer;
}


.submitButton {
  height: 40px;
  padding: 0;
  background-color: white;
  border: none;
  cursor: pointer;
  border-radius: 20px;
  transform: scale(1);
  transition: all 0.3s ease-in-out;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
  width: 200px;
  color: black;
  font-weight: bold;
  display: block;
  margin: 0 auto;
}

.submitButton:hover {
  transform: scale(1.2);
}

.el-input :deep(.el-input__wrapper) {
  width:100%;
  margin-bottom: 10px; /* 设置输入框底部的外边距为10px */
  height: 40px; /* 设置输入框的高度为40px */
  padding: 0 10px; /* 设置输入框的内边距，左右各10px */
  background-color: transparent; /* 设置输入框的背景颜色为透明 */
  border: 1px solid gray; /* 设置输入框的边框为1px的灰色实线 */
  border-radius: 2px; /* 设置输入框的边框圆角为2px */
  box-shadow: 0 0 5px #3f4548; /* 设置输入框的阴影效果 */
  transition: all 0.3s ease-in-out; /* 设置输入框所有属性的过渡效果，持续时间为0.3秒 */
}

.el-input:hover{
  transform: scale(1.05); /* 设置输入框的放大效果 */

}

.el-input:deep(.el-input-group__prepend) {
  margin-bottom: 10px; /* 设置输入框底部的外边距为10px */
  height: 40px; /* 设置输入框的高度为40px */
  padding: 0 10px; /* 设置输入框的内边距，左右各10px */
  background-color: transparent; /* 设置输入框的背景颜色为透明 */
  border: 1px solid gray; /* 设置输入框的边框为1px的灰色实线 */
  border-radius: 2px; /* 设置输入框的边框圆角为2px */
  box-shadow: 0 0 5px #3f4548; /* 设置输入框的阴影效果 */
  transition: all 0.3s ease-in-out; /* 设置输入框所有属性的过渡效果，持续时间为0.3秒 */
}
</style>
