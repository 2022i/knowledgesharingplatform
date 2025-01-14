import { defineStore } from 'pinia'
import axios from 'axios'
import { ElMessage } from 'element-plus'

// 文章状态类型
export type ArticleStatus = 'draft' | 'pending' | 'published' | 'rejected' | 'pending_delete'

// 定义文章接口
export interface Article {
  id: number
  authorId: number
  title: string
  content: string
  summary: string | null
  createTime: string
  // 文章状态相关字段
  status?: ArticleStatus
  check?: boolean
  delete?: boolean
  draft?: boolean
  reject?: boolean
  rejectReason?: string
  // 统计相关字段
  collectCount?: number
  shareCount?: number
  supportCount?: number
  opposeCount?: number
  viewCount?: number
  author?: {
    id: number
    name: string
    avatar?: string
  }
}

// 定义 store 的状态接口
interface ArticleState {
  articles: Article[]
  loading: boolean
  error: string | null
}

// API 基础URL
const API_BASE_URL = 'http://127.0.0.1:8081/server'

// 创建管理员文章操作 store
export const useAdminArticleStore = defineStore('adminArticle', {
  state: (): ArticleState => ({
    articles: [],
    loading: false,
    error: null
  }),

  getters: {
    getArticles: (state) => state.articles,
    isLoading: (state) => state.loading,
    getError: (state) => state.error
  },

  actions: {
    // 获取待审核文章列表
    async fetchUncheckArticles() {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`${API_BASE_URL}/auditor/getUnCheckArticles`, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })
        this.articles = response.data
      } catch (error) {
        console.error('Failed to fetch unchecked articles:', error)
        this.error = '获取待审核文章失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    // 获取待删除文章列表
    async fetchDeletingArticles() {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`${API_BASE_URL}/auditor/getDeletingArticlesList`, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })
        this.articles = response.data
      } catch (error) {
        console.error('Failed to fetch deleting articles:', error)
        this.error = '获取待删除文章失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    // 通过文章
    async approveArticle(articleId: number, auditorId: number) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.put(`${API_BASE_URL}/auditor/approveArticle`, null, {
          params: { articleId, auditorId },
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })
        if (response.data.code === 200) {
          // 从列表中移除已审核的文章
          this.articles = this.articles.filter(article => article.id !== articleId)
          ElMessage.success('文章审核通过')
          return response.data
        }
      } catch (error) {
        console.error('Failed to approve article:', error)
        this.error = '审核文章失败'
        ElMessage.error(this.error)
        throw error
      } finally {
        this.loading = false
      }
    },

    // 拒绝文章
    async rejectArticle(articleId: number, auditorId: number, reason: string) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.put(`${API_BASE_URL}/auditor/rejectArticle`, {
          articleId,
          auditorId,
          reason
        }, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })
        if (response.data.code === 200) {
          // 从列表中移除已拒绝的文章
          this.articles = this.articles.filter(article => article.id !== articleId)
          ElMessage.success('已拒绝文章')
          return response.data
        }
      } catch (error) {
        console.error('Failed to reject article:', error)
        this.error = '拒绝文章失败'
        ElMessage.error(this.error)
        throw error
      } finally {
        this.loading = false
      }
    },

    // 通过删除请求
    async approveDeleteArticle(articleId: number, auditorId: number) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.put(`${API_BASE_URL}/auditor/deleteArticleSuccess`, null, {
          params: { articleId, auditorId },
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })
        if (response.data.code === 200) {
          // 从列表中移除已删除的文章
          this.articles = this.articles.filter(article => article.id !== articleId)
          ElMessage.success('已通过删除请求')
          return response.data
        }
      } catch (error) {
        console.error('Failed to approve delete request:', error)
        this.error = '通过删除请求失败'
        ElMessage.error(this.error)
        throw error
      } finally {
        this.loading = false
      }
    },

    // 拒绝删除请求
    async rejectDeleteArticle(articleId: number, auditorId: number, reason: string) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.put(`${API_BASE_URL}/auditor/deleteArticleFail`, {
          articleId,
          auditorId,
          reason
        }, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })
        if (response.data.code === 200) {
          // 从列表中移除已拒绝删除的文章
          this.articles = this.articles.filter(article => article.id !== articleId)
          ElMessage.success('已拒绝删除请求')
          return response.data
        }
      } catch (error) {
        console.error('Failed to reject delete request:', error)
        this.error = '拒绝删除请求失败'
        ElMessage.error(this.error)
        throw error
      } finally {
        this.loading = false
      }
    }
  }
}) 