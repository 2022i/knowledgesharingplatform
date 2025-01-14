import { defineStore } from 'pinia'
import axios from 'axios'

// 创建axios实例
const apiClient = axios.create({
  baseURL: 'http://127.0.0.1:8081/server',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
    'Accept': 'application/json'
  },
  withCredentials: false
})

// 添加请求拦截器
apiClient.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 定义分类主题的接口
export interface Theme {
  id: number
  name: string
}

// 定义主题数据接口
interface ThemeData {
  theme: Theme
  articleCount: number
}

// 定义 store 的 state 接口
interface ThemeState {
  themes: ThemeData[]
  loading: boolean
  error: string | null
}

// 创建 store
export const useThemeStore = defineStore('theme', {
  state: (): ThemeState => ({
    themes: [],
    loading: false,
    error: null
  }),

  getters: {
    // 获取所有分类
    getAllThemes: (state: ThemeState): Theme[] => state.themes.map(item => item.theme),
    
    // 获取分类数量
    getThemeCount: (state: ThemeState): number => state.themes.length,
    
    // 根据 ID 获取分类名称
    getThemeNameById: (state: ThemeState) => (id: number): string => {
      const themeData = state.themes.find(item => item.theme.id === id)
      return themeData ? themeData.theme.name : ''
    },

    // 获取分类及其文章数量
    getThemesWithCount: (state: ThemeState): ThemeData[] => state.themes,

    // 根据ID获取分类文章数量
    getArticleCountById: (state: ThemeState) => (id: number): number => {
      const themeData = state.themes.find(item => item.theme.id === id)
      return themeData ? themeData.articleCount : 0
    },

    // 检查是否正在加载
    isLoading: (state: ThemeState): boolean => state.loading,

    // 获取错误信息
    getError: (state: ThemeState): string | null => state.error
  },

  actions: {
    // 重置 store 状态
    reset(): void {
      this.themes = []
      this.loading = false
      this.error = null
    },

    // 获取所有分类
    async fetchAllThemes(): Promise<void> {
      this.loading = true
      this.error = null
      
      try {
        console.log('开始获取所有分类...')
        const response = await apiClient.get('/theme/getAllThemeAndArticleCount')

        console.log('获取分类响应:', response.data)
        
        if (Array.isArray(response.data)) {
          this.themes = response.data.map(item => ({
            theme: {
              id: parseInt(String(item.theme.id), 10),
              name: item.theme.name
            },
            articleCount: parseInt(String(item.articleCount), 10)
          }))
          console.log('成功获取分类列表:', this.themes)
        } else {
          throw new Error('返回数据格式不正确')
        }
      } catch (error: any) {
        console.error('获取分类失败:', error)
        this.error = error.message || '获取分类失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    // 根据 ID 查找分类
    findThemeById(id: number): Theme | undefined {
      const themeData = this.themes.find(item => item.theme.id === id)
      return themeData?.theme
    },

    // 根据名称查找分类
    findThemeByName(name: string): Theme | undefined {
      const themeData = this.themes.find(item => item.theme.name === name)
      return themeData?.theme
    }
  }
}) 