import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

// 登录表单接口
export interface LoginForm {
  userUniqueIdentifier: string // 可以是邮箱或用户名
  password: string
  loginType: 'email' | 'username' // 登录类型
}

// 注册表单接口
export interface RegisterForm {
  username: string
  email: string
  password: string
  code: string
}

// 用户角色类型
export type UserRole = 'admin' | 'user'

// 用户信息接口
export interface UserInfo {
  id: number
  name: string
  email: string
  role: UserRole
  avatar?: string
  createdAt?: string
  lastLoginAt?: string
}

const API_BASE_URL = 'http://127.0.0.1:8081/server'

export function useAuth() {
  const router = useRouter()
  const userInfo = ref<UserInfo | null>(null)
  const isLoading = ref(false)

  // 计算属性：判断是否已登录
  const isAuthenticated = computed(() => !!userInfo.value)
  
  // 计算属性：判断是否是管理员
  const isAdmin = computed(() => userInfo.value?.role === 'admin')

  // 初始化认证状态
  const initAuth = async () => {
    const token = localStorage.getItem('token')
    if (token) {
      await getCurrentUser()
    }
  }

  // 获取当前用户信息
  const getCurrentUser = async () => {
    try {
      const token = localStorage.getItem('token')
      if (!token) {
        userInfo.value = null
        return null
      }

      // TODO: 实现获取用户信息的API调用
      // 这里暂时使用本地存储的信息
      const storedUserInfo = localStorage.getItem('userInfo')
      if (storedUserInfo) {
        userInfo.value = JSON.parse(storedUserInfo)
        return userInfo.value
      }
      return null
    } catch (error) {
      console.error('获取用户信息失败:', error)
      handleAuthError(error)
      return null
    }
  }

  // 发送注册验证码
  const sendRegisterCode = async (username: string, email: string) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/register/mailForRegister`, {
        username,
        email
      })

      if (response.data.code === 200) {
        ElMessage.success('验证码已发送到您的邮箱')
        return true
      } else {
        throw new Error(response.data.msg)
      }
    } catch (error) {
      handleAuthError(error)
      return false
    }
  }

  // 注册
  const register = async (form: RegisterForm) => {
    try {
      isLoading.value = true
      const response = await axios.post(`${API_BASE_URL}/register/writeUser`, form)

      if (response.data.code === 200) {
        ElMessage.success('注册成功')
        await router.push('/login')
        return true
      } else {
        throw new Error(response.data.msg)
      }
    } catch (error) {
      handleAuthError(error)
      return false
    } finally {
      isLoading.value = false
    }
  }

  // 登录处理
  const login = async (form: LoginForm) => {
    try {
      isLoading.value = true
      
      const endpoint = form.loginType === 'email' 
        ? '/login/loginByEmail' 
        : '/login/loginByUsername'

      const response = await axios.post(`${API_BASE_URL}${endpoint}`, {
        userUniqueIdentifier: form.userUniqueIdentifier,
        password: form.password
      })

      if (response.data.code === 200) {
        // 保存token
        const token = response.data.msg
        localStorage.setItem('token', token)

        // 创建模拟用户信息（实际项目中应该调用获取用户信息的API）
        const mockUserInfo: UserInfo = {
          id: 1,
          name: form.userUniqueIdentifier,
          email: form.loginType === 'email' ? form.userUniqueIdentifier : '',
          role: form.userUniqueIdentifier.includes('admin') ? 'admin' : 'user',
          lastLoginAt: new Date().toISOString()
        }

        userInfo.value = mockUserInfo
        localStorage.setItem('userInfo', JSON.stringify(mockUserInfo))

        // 根据角色跳转
        const redirectPath = mockUserInfo.role === 'admin' ? '/admin/dashboard' : '/home'
        await router.push(redirectPath)
        
        ElMessage.success('登录成功')
        return { role: mockUserInfo.role, user: mockUserInfo }
      } else {
        throw new Error(response.data.msg)
      }
    } catch (error) {
      handleAuthError(error)
      throw error
    } finally {
      isLoading.value = false
    }
  }

  // 登出处理
  const logout = async () => {
    try {
      // 清除本地存储和状态
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      userInfo.value = null
      
      // 跳转到登录页
      await router.push('/login')
      ElMessage.success('已安全退出登录')
    } catch (error) {
      handleAuthError(error)
    }
  }

  // 检查权限
  const checkPermission = (requiredRole: UserRole = 'user') => {
    if (!isAuthenticated.value) {
      router.push({
        path: '/login',
        query: { redirect: router.currentRoute.value.fullPath }
      })
      return false
    }

    if (requiredRole === 'admin' && !isAdmin.value) {
      ElMessage.error('无权访问该页面')
      router.push('/home')
      return false
    }

    return true
  }

  // 统一处理认证相关错误
  const handleAuthError = (error: unknown) => {
    let message = '操作失败'
    if (error instanceof Error) {
      message = error.message
    } else if (axios.isAxiosError(error) && error.response?.data?.msg) {
      message = error.response.data.msg
    }
    
    ElMessage.error(message)
    
    // 如果是认证失败，清除状态
    if (message.includes('认证') || message.includes('token')) {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      userInfo.value = null
    }
  }

  return {
    // 状态
    userInfo,
    isLoading,
    isAuthenticated,
    isAdmin,

    // 方法
    login,
    logout,
    register,
    sendRegisterCode,
    getCurrentUser,
    initAuth,
    checkPermission
  }
} 