<template>
  <div class="register-container">
    <div class="register-content">
      <!-- 注册页头部 -->
      <div class="register-header">
        <img src="../assets/logo.svg" alt="Logo" class="logo">
        <h1 class="title">知识分享平台</h1>
        <p class="subtitle">knowledge sharing platform</p>
      </div>

      <!-- 注册表单区域 -->
      <div class="register-box">
        <h2 class="register-title">注册账号</h2>
        <el-form
          ref="registerFormRef"
          :model="registerForm"
          :rules="rules"
          class="register-form"
        >
          <!-- 用户名输入框 -->
          <el-form-item prop="username">
            <el-input
              v-model="registerForm.username"
              placeholder="用户名"
              :prefix-icon="UserIcon"
            />
          </el-form-item>
          
          <!-- 邮箱输入框 -->
          <el-form-item prop="email">
            <el-input
              v-model="registerForm.email"
              placeholder="邮箱"
              :prefix-icon="MessageIcon"
            />
          </el-form-item>
          
          <!-- 密码输入框 -->
          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="密码"
              :prefix-icon="LockIcon"
              show-password
            />
          </el-form-item>

          <!-- 确认密码输入框 -->
          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="确认密码"
              :prefix-icon="LockIcon"
              show-password
            />
          </el-form-item>

          <!-- 验证码输入框 -->
          <el-form-item prop="code">
            <div class="verification-code">
              <el-input
                v-model="registerForm.code"
                placeholder="验证码"
                :prefix-icon="MessageIcon"
              />
              <el-button
                type="primary"
                :disabled="codeSending || countdown > 0"
                @click="handleSendCode"
              >
                {{ countdown > 0 ? `${countdown}s后重试` : '发送验证码' }}
              </el-button>
            </div>
          </el-form-item>

          <!-- 注册按钮 -->
          <el-form-item>
            <el-button 
              type="primary" 
              class="register-button"
              :loading="loading"
              @click="handleRegister"
            >
              注册
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 表单底部链接 -->
        <div class="form-footer">
          已有账号？
          <router-link to="/login" class="login-link">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { Message as MessageIcon, Lock as LockIcon, User as UserIcon } from '@element-plus/icons-vue'
import { useAuth } from '../composables/useAuth'

const router = useRouter()
const { register, sendRegisterCode } = useAuth()
const registerFormRef = ref<FormInstance>()
const loading = ref(false)
const codeSending = ref(false)
const countdown = ref(0)

// 注册表单数据
const registerForm = ref({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  code: ''
})

// 自定义校验规则：确认密码
const validateConfirmPassword = (rule: any, value: string, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.value.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

// 表单验证规则
const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, message: '用户名长度不能小于2位', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码长度应为6位', trigger: 'blur' }
  ]
}

// 发送验证码
const handleSendCode = async () => {
  try {
    // 验证邮箱和用户名
    await registerFormRef.value?.validateField(['email', 'username'])
    
    codeSending.value = true
    const success = await sendRegisterCode(
      registerForm.value.username,
      registerForm.value.email
    )
    
    if (success) {
      countdown.value = 60
      const timer = setInterval(() => {
        countdown.value--
        if (countdown.value <= 0) {
          clearInterval(timer)
        }
      }, 1000)
    }
  } catch (error) {
    console.error('发送验证码失败：', error)
  } finally {
    codeSending.value = false
  }
}

// 处理注册
const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  try {
    await registerFormRef.value.validate()
    loading.value = true
    
    const success = await register({
      username: registerForm.value.username,
      email: registerForm.value.email,
      password: registerForm.value.password,
      code: registerForm.value.code
    })
    
    if (success) {
      ElMessage({
        type: 'success',
        message: '注册成功，请登录',
        onClose: () => {
          router.push('/login')
        }
      })
    }
  } catch (error) {
    console.error('注册失败：', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 容器样式 */
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f6f7f9;
}

/* 注册内容区域 */
.register-content {
  width: 400px;
  text-align: center;
}

/* 头部样式 */
.register-header {
  margin-bottom: 40px;
}

.logo {
  width: 120px;
  height: 120px;
  margin-bottom: 20px;
  transition: transform 0.3s ease;
  filter: drop-shadow(0 2px 8px rgba(24, 112, 253, 0.1));
}

.logo:hover {
  transform: scale(1.05);
  filter: drop-shadow(0 4px 12px rgba(24, 112, 253, 0.2));
}

.title {
  font-size: 32px;
  font-weight: bold;
  color: #1870FD;
  margin: 0 0 8px;
  letter-spacing: 1px;
}

.subtitle {
  font-size: 16px;
  color: #8590a6;
  margin: 0;
}

/* 注册框样式 */
.register-box {
  background: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

.register-title {
  font-size: 24px;
  color: #1a1a1a;
  margin: 0 0 24px;
  font-weight: 500;
}

/* 表单样式 */
.register-form {
  margin-top: 24px;
}

.register-button {
  width: 100%;
  height: 40px;
  font-size: 16px;
}

/* 底部链接样式 */
.form-footer {
  margin-top: 24px;
  text-align: center;
  color: #8590a6;
}

.login-link {
  color: #1870FD;
  text-decoration: none;
  margin-left: 4px;
}

/* 验证码输入框样式 */
.verification-code {
  display: flex;
  gap: 12px;
}

.verification-code .el-input {
  flex: 1;
}

.verification-code .el-button {
  width: 120px;
  white-space: nowrap;
}
</style>
