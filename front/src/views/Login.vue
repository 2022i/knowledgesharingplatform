<template>
  <div class="login-container">
    <div class="login-content">
      <!-- 登录页头部 -->
      <div class="login-header">
        <img src="../assets/logo.svg" alt="Logo" class="logo">
        <h1 class="title">知识分享平台</h1>
        <p class="subtitle">knowledge sharing platform</p>
      </div>

      <!-- 登录表单区域 -->
      <div class="login-box">
        <h2 class="login-type">账号登录</h2>

        <!-- 登录方式切换 -->
        <div class="login-tabs">
          <div
            :class="['tab-item', { active: loginForm.loginType === 'email' }]"
            @click="toggleLoginType"
          >
            邮箱登录
          </div>
          <div
            :class="['tab-item', { active: loginForm.loginType === 'username' }]"
            @click="toggleLoginType"
          >
            用户名登录
          </div>
        </div>

        <!-- 登录表单 -->
        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="rules"
          class="login-form"
        >
          <!-- 邮箱/用户名输入框 -->
          <el-form-item prop="userUniqueIdentifier">
            <el-input
              v-model="loginForm.userUniqueIdentifier"
              :placeholder="loginForm.loginType === 'email' ? '邮箱' : '用户名'"
              :prefix-icon="loginForm.loginType === 'email' ? MessageIcon : UserIcon"
            />
          </el-form-item>
          
          <!-- 密码输入框 -->
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="密码"
              :prefix-icon="LockIcon"
              show-password
            />
          </el-form-item>

          <!-- 登录按钮 -->
          <el-form-item>
            <el-button 
              type="primary" 
              class="login-button" 
              :loading="loading"
              @click="handleLogin"
            >
              登录
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 表单底部链接 -->
        <div class="form-footer">
          <router-link to="/register" class="register-link">注册账号</router-link>
          <el-link type="primary">忘记密码？</el-link>
        </div>
      </div>

      <!-- 测试账号信息 -->
      <div class="test-account">
        <p class="test-title">测试账号：</p>
        <div class="test-info">
          <p>普通用户：test@example.com / 123456</p>
          <p>管理员：admin@example.com / admin123</p>
          <el-button 
            type="primary" 
            link 
            @click="useTestAccount('user')"
          >
            使用用户账号
          </el-button>
          <el-button 
            type="primary" 
            link 
            @click="useTestAccount('admin')"
          >
            使用管理员账号
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { Message as MessageIcon, Lock as LockIcon, User as UserIcon } from '@element-plus/icons-vue'
import { useAuth } from '../composables/useAuth'

// 路由实例
const router = useRouter()
const route = useRoute()
// 获取登录方法
const { login } = useAuth()

// 表单相关响应式数据
const loginFormRef = ref<FormInstance>()
const loading = ref(false)
const loginForm = ref({
  userUniqueIdentifier: '',
  password: '',
  loginType: 'email' as 'email' | 'username'
})

// 表单验证规则
const emailRules = [
  { required: true, message: '请输入邮箱', trigger: 'blur' },
  { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
]

const usernameRules = [
  { required: true, message: '请输入用户名', trigger: 'blur' }
]

const rules = ref<FormRules>({
  userUniqueIdentifier: emailRules,
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ]
})

// 切换登录方式
const toggleLoginType = () => {
  loginForm.value.userUniqueIdentifier = ''
  loginForm.value.loginType = loginForm.value.loginType === 'email' ? 'username' : 'email'
  rules.value.userUniqueIdentifier = loginForm.value.loginType === 'email' ? emailRules : usernameRules
  if (loginFormRef.value) {
    loginFormRef.value.clearValidate('userUniqueIdentifier')
  }
}

/**
 * 处理登录逻辑
 */
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    loading.value = true
    
    // 调用登录接口
    const { role, user } = await login(loginForm.value)
    
    // 登录成功提示
    ElMessage({
      type: 'success',
      message: `欢迎回来，${user?.name || ''}`,
      duration: 1500
    })

    // 获取重定向地址
    const redirect = route.query.redirect as string
    const defaultPath = role === 'admin' ? '/admin/dashboard' : '/home'
    const targetPath = redirect || defaultPath

    // 等待消息显示后再跳转
    setTimeout(async () => {
      try {
        await router.replace(targetPath)
      } catch (error) {
        console.error('路由跳转失败：', error)
        window.location.href = targetPath
      }
    }, 1500)
  } catch (error) {
    loading.value = false
    console.error('登录失败：', error)
  }
}

// 测试账号数据
const testAccounts = {
  user: {
    userUniqueIdentifier: 'test@example.com',
    password: '123456',
    loginType: 'email' as const
  },
  admin: {
    userUniqueIdentifier: 'admin@example.com',
    password: 'admin123',
    loginType: 'email' as const
  }
}

/**
 * 一键填写测试账号
 */
const useTestAccount = (type: 'user' | 'admin') => {
  loginForm.value = { ...testAccounts[type] }
}
</script>

<style scoped>
/* 容器样式 */
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f6f7f9;
}

/* 登录内容区域 */
.login-content {
  width: 400px;
  text-align: center;
}

/* 头部样式 */
.login-header {
  margin-bottom: 40px;
}

.logo {
  width: 120px;
  height: 120px;
  margin-bottom: 20px;
  transition: transform 0.3s ease;
  filter: drop-shadow(0 2px 8px rgba(0, 102, 255, 0.1));
}

.logo:hover {
  transform: scale(1.05);
  filter: drop-shadow(0 4px 12px rgba(0, 102, 255, 0.2));
}

.title {
  font-size: 32px;
  font-weight: bold;
  color: #0066ff;
  margin: 0 0 8px;
  letter-spacing: 1px;
}

.subtitle {
  font-size: 16px;
  color: #8590a6;
  margin: 0;
}

/* 登录框样式 */
.login-box {
  background: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

/* 登录方式切换标签 */
.login-tabs {
  display: flex;
  margin: 24px 0;
  border-bottom: 1px solid #f0f0f0;
}

.tab-item {
  flex: 1;
  padding: 12px;
  cursor: pointer;
  color: #8590a6;
  transition: all 0.3s ease;
  text-align: center;
}

.tab-item:hover {
  color: #0066ff;
}

.tab-item.active {
  color: #0066ff;
  border-bottom: 2px solid #0066ff;
}

/* 表单样式 */
.login-form {
  margin-top: 24px;
}

.login-button {
  width: 100%;
  height: 40px;
  font-size: 16px;
}

/* 底部链接样式 */
.form-footer {
  margin-top: 24px;
  display: flex;
  justify-content: space-between;
  color: #8590a6;
}

.register-link {
  color: #0066ff;
  text-decoration: none;
}

/* 测试账号样式 */
.test-account {
  margin-top: 24px;
  padding: 16px;
  background: #f6f8fa;
  border-radius: 4px;
  text-align: left;
}

.test-title {
  color: #1f2328;
  font-weight: 500;
  margin: 0 0 8px;
}

.test-info {
  color: #656d76;
  font-size: 14px;
}

.test-info p {
  margin: 4px 0;
}

.login-type {
  font-size: 24px;
  color: #1a1a1a;
  margin: 0;
  font-weight: 500;
}

.test-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.test-info .el-button {
  align-self: flex-start;
}
</style>
