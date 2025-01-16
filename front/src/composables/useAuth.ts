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
  username: string
  email: string
  role: UserRole
  avatar?: string
  createdAt?: string
  lastLoginAt?: string
}

// API 响应接口
interface ApiResponse<T = any> {
  code: number
  msg: string
  username: string
  id: number
  data?: T
}

const API_BASE_URL = 'http://127.0.0.1:8081/server'

// 创建axios实例
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
})

// 添加请求拦截器
axiosInstance.interceptors.request.use(
  (config) => {
    // 从 localStorage 获取 token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

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
    const userId = localStorage.getItem('userId')
    if (token && userId) {
      await getCurrentUser()
    }
  }

  // 获取当前用户信息
  const getCurrentUser = async () => {
    try {
      const token = localStorage.getItem('token')
      const userId = localStorage.getItem('userId')
      if (!token || !userId) {
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

      console.log('=== 开始登录 ===')
      console.log('登录类型:', form.loginType)
      console.log('登录标识:', form.userUniqueIdentifier)
      console.log('请求地址:', endpoint)

      const response = await axiosInstance.post<ApiResponse>(endpoint, {
        userUniqueIdentifier: form.userUniqueIdentifier,
        password: form.password
      })

      console.log('=== 登录响应 ===')
      console.log('响应状态码:', response.data.code)
      console.log('响应消息:', response.data.msg)
      console.log('用户名:', response.data.username)
      console.log('用户ID:', response.data.id)

      if (response.data.code === 200) {
        // 保存token和userId
        const token = response.data.msg
        const userId = response.data.id
        const username = response.data.username
        
        // 根据返回的用户名判断角色
        const role: UserRole = username === 'Administrator' ? 'admin' : 'user'
        
        console.log('=== 用户信息 ===')
        console.log('Token:', token)
        console.log('用户ID:', userId)
        console.log('用户名:', username)
        console.log('角色:', role)
        
        localStorage.setItem('token', token)
        localStorage.setItem('userId', userId.toString())
        localStorage.setItem('username', username)

        // 创建用户信息
        const userInfoData: UserInfo = {
          id: userId,
          username: username,
          email: form.loginType === 'email' ? form.userUniqueIdentifier : '',
          role: role,
          lastLoginAt: new Date().toISOString()
        }

        userInfo.value = userInfoData
        localStorage.setItem('userInfo', JSON.stringify(userInfoData))
        
        console.log('=== 登录成功 ===')
        console.log('用户信息已保存:', userInfoData)
        
        // 根据角色跳转到不同页面
        if (role === 'admin') {
          console.log('管理员登录，跳转到管理页面')
          await router.push('/admin/articles')
        } else {
          console.log('普通用户登录，跳转到首页')
          await router.push('/home')
        }
        
        return true
      } else {
        console.log('=== 登录失败 ===')
        console.log('错误消息:', response.data.msg)
        throw new Error(response.data.msg)
      }
    } catch (error) {
      console.error('=== 登录错误 ===')
      console.error('错误详情:', error)
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
      localStorage.removeItem('userId')
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
    } else if (axios.isAxiosError(error)) {
      // 处理后端返回的验证错误
      if (error.response?.data?.username?.[0]) {
        message = error.response.data.username[0]
      } else if (error.response?.data?.msg) {
        message = error.response.data.msg
      }
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
    userId: Number(localStorage.getItem('userId')) || -1,

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