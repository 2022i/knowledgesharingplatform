<script setup>
import axios from 'axios';
import {useRouter} from 'vue-router';
import {ElForm, ElIcon} from 'element-plus';
import {ref} from 'vue';

const ruleForm = ref({
  email: '',
  pass: '',
  checkPass: '',
  code: ''
});

const checkEmail = (rule, value, callback) => {
  const emailRegex = /^([\w-]+)@([\w-]+\.)+[\w-]{2,4}$/;
  if (!value) {
    return callback(new Error('请输入电子邮箱'));
  } else if (!emailRegex.test(value)) {
    return callback(new Error('请输入有效的电子邮箱'));
  }
  callback();
};

const validatePass = (rule, value, callback) => {
  const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]).{8,}$/;
  if (value === '') {
    callback(new Error('请输入密码'));
  } else if (!passwordRegex.test(value)) {
    callback(new Error('密码必须包含大小写字母、数字和符号，且不少于8位'));
  }
};

const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'));
  } else if (value !== ruleForm.value.pass) {
    callback(new Error('两次输入密码不一致!'));
  } else {
    callback();
  }
};

const checkCode = (rule, value, callback) => {
  if (!value) {
    return callback(new Error('请输入验证码'));
  }
  callback();
};

const rules = ref({
  email: [{validator: checkEmail, trigger: 'change'}],
  pass: [{validator: validatePass, trigger: 'change'}],
  checkPass: [{validator: validatePass2, trigger: 'change'}],
  code: [{validator: checkCode, trigger: 'change'}]
});

const verificationButtonText = ref('获取验证码');
const verificationButtonDisabled = ref(false);
const loading = ref(false);

const showPassword = ref(false);
const showCheckPass = ref(false);

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value;
};

const toggleCheckPasswordVisibility = () => {
  showCheckPass.value = !showCheckPass.value;
};

const getVerificationCode = async () => {
  if (!ruleForm.value.email) {
    alert('请先输入电子邮箱');
    return;
  }

  try {
    const response = await axios.post('http://127.0.0.1:8081/server/register/mailForRegister',
        {email: ruleForm.value.email});
    if (response.data.code === 200) {
      alert('验证码发送成功');

      verificationButtonDisabled.value = true;
      let countdown = 60;
      verificationButtonText.value = `${countdown}秒后重新获取`;
      const timer = setInterval(() => {
        countdown--;
        if (countdown > 0) {
          verificationButtonText.value = `${countdown}秒后重新获取`;
        } else {
          clearInterval(timer);
          verificationButtonText.value = '获取验证码';
          verificationButtonDisabled.value = false;
        }
      }, 1000);
    } else {
      alert(response.data.message);
    }
  } catch (error) {
    console.error('获取验证码时出错:', error);
    alert('获取验证码失败，请重试');
  }
};

const submitForm = async () => {
  loading.value = true;
  try {
    const data = {
      email: ruleForm.value.email,
      password: ruleForm.value.pass,
      code: ruleForm.value.code
    };
    const response = await axios.post(' http://127.0.0.1:8081/server/register/registerByEmail', data);
    if (response.data.code === 200) {
      alert('注册成功！');
      resetRuleForm();
    } else {
      alert(response.data.message);
    }
  } catch (error) {
    console.error('提交表单时出错:', error);
    alert('提交表单失败，请检查后重试');
  } finally {
    loading.value = false;
  }
};

const router = useRouter();

const returnToLogin = () => {
  router.push('/');
};

const resetRuleForm = () => {
  ruleForm.value = {
    email: '',
    pass: '',
    checkPass: '',
    code: ''
  }
};
</script>

<template>
  <div class="background-container">
    <h1 class="title">目标检测系统</h1>
    <div class="content-container">
      <h2>重置密码</h2>
      <el-form :model="ruleForm" :rules="rules" label-width="100px">
        <el-form-item prop="email">
          <el-input v-model="ruleForm.email" clearable placeholder="电子邮箱"></el-input>
        </el-form-item>
        <el-form-item prop="pass">
          <el-input :type="showPassword ? 'text' : 'password'" v-model="ruleForm.pass" placeholder="新密码">
            <template #suffix>
              <el-icon @mousedown="togglePasswordVisibility" style="cursor: pointer; color: #333;">
                <component :is="showPassword ? 'View' : 'Hide'"/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="checkPass">
          <el-input :type="showCheckPass ? 'text' : 'password'" v-model="ruleForm.checkPass" placeholder="确认密码">
            <template #suffix>
              <el-icon @mousedown="toggleCheckPasswordVisibility" style="cursor: pointer; color: #333;">
                <component :is="showCheckPass ? 'View' : 'Hide'"/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input v-model="ruleForm.code" clearable placeholder="验证码" style="flex: 1;"></el-input>
          <el-button class="verification-button" @click="getVerificationCode">获取验证码
          </el-button>
        </el-form-item>
        <el-button class="resetPasswordButton" type="primary"  @click="submitForm">重置密码</el-button>
        <p class="remember">想起密码？</p>
        <el-button class="returnToLogin" @click="returnToLogin">返回登录</el-button>
      </el-form>
    </div>
  </div>
</template>


<style lang="scss" scoped>
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

.reset-password {
  width: 100%;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0); /* 半透明白色背景，增强对比 */
  border-radius: 3px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0);
}

.el-input :deep(.el-input__wrapper) {
  width:100%;
  margin-left: -100px;
  margin-bottom: 10px; /* 设置输入框底部的外边距为10px */
  height: 40px; /* 设置输入框的高度为40px */
  padding: 0 10px; /* 设置输入框的内边距，左右各10px */
  background-color: transparent; /* 设置输入框的背景颜色为透明 */
  border: 1px solid gray; /* 设置输入框的边框为1px的灰色实线 */
  border-radius: 2px; /* 设置输入框的边框圆角为2px */
  box-shadow: 0 0 5px #3f4548; /* 设置输入框的阴影效果 */
  transition: all 0.3s ease-in-out; /* 设置输入框所有属性的过渡效果，持续时间为0.3秒 */
}

.el-input :deep(.el-input__wrapper):hover {
  transform: scale(1.05); /* 设置输入框的放大效果 */
}
.form-actions {
  display: flex;
  justify-content: space-between;
}

.verification-button {
  padding: 10px 20px; /* 调整按钮的内边距 */
  background-color: #409eff;
  color: white;
  border: none;
  cursor: pointer;
  height: 40px;
  margin-left: 10px;
  border-radius: 2px;
  margin-top: -10px;
}

.verification-button:hover {
  background-color: #66b1ff;
}

.resetPasswordButton{
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
  text-align: center;
}

.large-button {
  font-size: 16px; /* 调整按钮文字大小 */
}

.register-link,
.InputText input {
  color: white;
}

.remember{
  text-align: left;
  margin-left: 275px;
  font-size: 14px;
  color: white;
  padding-right: 0;
  margin-bottom: 10px;
}

.returnToLogin{
  color: #409eff;
  margin-left: 275px;
  padding-left: 10px;
  padding-right: 10px;
}

</style>