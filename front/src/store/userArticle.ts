import { defineStore } from 'pinia'
import axios from 'axios'
import { ElMessage } from 'element-plus'

// API 基础URL
const API_BASE_URL = 'http://127.0.0.1:8081/server'

// 文章作者接口
interface Author {
  id: number
  username: string
  avatar?: string
}

// 文章接口
interface Article {
  id: number
  title: string
  theme: string
  summary: string | null
  relatedKnowledge: string[]
  createTime: string
  viewUserCount: number
  supportUserCount: number
  opposeUserCount: number
  commentCount: number
  collectionUserCount: number
  shareUserCount: number
  author: Author
  support: boolean
  oppose: boolean
  collect: boolean
}

// 定义 store 的状态接口
interface ArticleState {
  articles: Article[]
  loading: boolean
  error: string | null
  currentPage: number
  pageSize: number
  total: number
}

// 创建用户文章操作 store
export const useUserArticleStore = defineStore('userArticle', {
  state: (): ArticleState => ({
    articles: [],
    loading: false,
    error: null,
    currentPage: 1,
    pageSize: 10,
    total: 0
  }),

  getters: {
    currentPageArticles: (state) => {
      const start = (state.currentPage - 1) * state.pageSize
      const end = start + state.pageSize
      return state.articles.slice(start, end)
    }
  },

  actions: {
    // 获取当前用户ID
    getCurrentUserId() {
      const userInfo = localStorage.getItem('userInfo')
      const token = localStorage.getItem('token')
      
      console.log('=== 认证信息 ===')
      console.log('Token:', token)
      console.log('UserInfo:', userInfo)
      
      if (!userInfo) return null
      try {
        const parsed = JSON.parse(userInfo)
        console.log('解析后的用户信息:', parsed)
        return parsed.id
      } catch (e) {
        console.error('解析用户信息失败:', e)
        return null
      }
    },

    // 获取文章列表
    async fetchArticles(params?: {
      category?: string,
      sortBy?: string,
      keyword?: string
    }) {
      this.loading = true
      this.error = null
      try {
        let endpoint = '/article/list'
        
        // 根据排序方式选择不同的端点
        if (params?.sortBy) {
          switch (params.sortBy) {
            case 'likes':
              endpoint = '/article/orderedSupportCount'
              break
            case 'time':
              endpoint = '/article/orderedCreateTime'
              break
            case 'views':
              endpoint = '/article/orderedViewCount'
              break
          }
        }

        const response = await axios.get(`${API_BASE_URL}${endpoint}`, {
          params: {
            page: this.currentPage,
            pageSize: this.pageSize,
            themeId: params?.category,
            keyword: params?.keyword
          },
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        if (response.data.code === 200) {
          this.articles = response.data.data.map((article: any) => ({
            id: article.id,
            title: article.title,
            theme: article.theme || '未分类',
            summary: article.summary,
            relatedKnowledge: article.relatedKnowledge || [],
            createTime: article.createTime,
            viewUserCount: article.viewCount || 0,
            supportUserCount: article.supportCount || 0,
            opposeUserCount: article.opposeCount || 0,
            commentCount: article.commentCount || 0,
            collectionUserCount: article.collectCount || 0,
            shareUserCount: article.shareCount || 0,
            author: {
              id: article.authorId,
              username: article.author?.name || '未知用户',
              avatar: article.author?.avatar
            },
            support: article.support || false,
            oppose: article.oppose || false,
            collect: article.collect || false
          }))
          this.total = response.data.total || this.articles.length
        } else {
          throw new Error(response.data.msg || '获取文章列表失败')
        }
      } catch (error) {
        console.error('Failed to fetch articles:', error)
        this.error = '获取文章列表失败'
        ElMessage.error(this.error)
      } finally {
        this.loading = false
      }
    },

    // 设置当前页码
    setCurrentPage(page: number) {
      this.currentPage = page
    },

    // 记录浏览历史
    async addViewRecord(articleId: number) {
      const userId = this.getCurrentUserId()
      if (!userId) {
        ElMessage.warning('请先登录')
        return
      }

      try {
        const response = await axios.put(`${API_BASE_URL}/addData/viewData`, null, {
          params: {
            articleId,
            userId
          },
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        if (response.data.code === 200) {
          const article = this.articles.find(a => a.id === articleId)
          if (article) {
            article.viewUserCount += 1
          }
        }
      } catch (error) {
        console.error('Failed to add view record:', error)
        ElMessage.error('记录浏览历史失败')
      }
    },

    // 切换点赞状态
    async toggleSupport(articleId: number) {
      console.log('=== 点赞操作开始 ===')
      const userId = this.getCurrentUserId()
      console.log('当前用户ID:', userId)
      
      if (!userId) {
        console.warn('未找到用户ID，终止操作')
        ElMessage.warning('请先登录')
        return
      }

      try {
        console.log('发送点赞请求:', {
          articleId,
          userId,
          url: `${API_BASE_URL}/addDataAndSentMessage/supportData`
        })
        
        const response = await axios.put(`${API_BASE_URL}/addDataAndSentMessage/supportData?articleId=${articleId}&userId=${userId}`, null, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        console.log('点赞请求响应:', response.data)

        if (response.data.code === 200) {
          const article = this.articles.find(a => a.id === articleId)
          if (article) {
            article.support = !article.support
            article.supportUserCount += article.support ? 1 : -1
            console.log('文章状态更新:', {
              articleId,
              newSupportState: article.support,
              newCount: article.supportUserCount
            })
            ElMessage.success(article.support ? '点赞成功' : '已取消点赞')
          }
          // 获取最新的文章数据
          await this.getRenderedArticle(articleId)
        }
      } catch (error: any) {
        console.error('点赞请求失败:', error)
        console.error('错误详情:', {
          message: error.message,
          response: error.response?.data
        })
        ElMessage.error('操作失败，请重试')
      }
    },

    // 切换反对状态
    async toggleOppose(articleId: number) {
      console.log('=== 反对操作开始 ===')
      const userId = this.getCurrentUserId()
      console.log('当前用户ID:', userId)
      
      if (!userId) {
        console.warn('未找到用户ID，终止操作')
        ElMessage.warning('请先登录')
        return
      }

      try {
        console.log('发送反对请求:', {
          articleId,
          userId,
          url: `${API_BASE_URL}/addDataAndSentMessage/oppositionData`
        })
        
        const response = await axios.put(`${API_BASE_URL}/addDataAndSentMessage/oppositionData`, null, {
          params: {
            articleId,
            userId
          },
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        console.log('反对请求响应:', response.data)

        if (response.data.code === 200) {
          const article = this.articles.find(a => a.id === articleId)
          if (article) {
            article.oppose = !article.oppose
            article.opposeUserCount += article.oppose ? 1 : -1
            console.log('文章状态更新:', {
              articleId,
              newOpposeState: article.oppose,
              newCount: article.opposeUserCount
            })
            ElMessage.success(article.oppose ? '已反对' : '已取消反对')
          }
          // 获取最新的文章数据
          await this.getRenderedArticle(articleId)
        }
      } catch (error: any) {
        console.error('反对请求失败:', error)
        console.error('错误详情:', {
          message: error.message,
          response: error.response?.data
        })
        ElMessage.error('操作失败，请重试')
      }
    },

    // 切换收藏状态
    async toggleCollect(articleId: number) {
      const userId = this.getCurrentUserId()
      if (!userId) {
        ElMessage.warning('请先登录')
        return
      }

      try {
        console.log('发送收藏请求:', {
          articleId,
          userId,
          url: `${API_BASE_URL}/addDataAndSentMessage/collectionData`
        })

        const response = await axios.put(`${API_BASE_URL}/addDataAndSentMessage/collectionData`, null, {
          params: {
            articleId,
            userId
          },
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        console.log('收藏请求响应:', response.data)

        if (response.data.code === 200) {
          const article = this.articles.find(a => a.id === articleId)
          if (article) {
            article.collect = !article.collect
            article.collectionUserCount += article.collect ? 1 : -1
            console.log('文章状态更新:', {
              articleId,
              newCollectState: article.collect,
              newCount: article.collectionUserCount
            })
            ElMessage.success(article.collect ? '收藏成功' : '已取消收藏')
          }
          // 获取最新的文章数据
          return await this.getRenderedArticle(articleId)
        }
      } catch (error: any) {
        console.error('收藏请求失败:', error)
        console.error('错误详情:', {
          message: error.message,
          response: error.response?.data
        })
        ElMessage.error('操作失败，请重试')
        return false
      }
    },

    // 分享文章
    async shareArticle(articleId: number) {
      const userId = this.getCurrentUserId()
      if (!userId) {
        ElMessage.warning('请先登录')
        return
      }

      try {
        console.log('发送分享请求:', {
          articleId,
          userId,
          url: `${API_BASE_URL}/addDataAndSentMessage/shareData`
        })

        const response = await axios.put(`${API_BASE_URL}/addDataAndSentMessage/shareData`, null, {
          params: {
            articleId,
            userId
          },
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        console.log('分享请求响应:', response.data)

        if (response.data.code === 200) {
          const article = this.articles.find(a => a.id === articleId)
          if (article) {
            article.shareUserCount += 1
            console.log('文章状态更新:', {
              articleId,
              newShareCount: article.shareUserCount
            })
            ElMessage.success('分享成功')
          }
          // 获取最新的文章数据
          return await this.getRenderedArticle(articleId)
        }
      } catch (error: any) {
        console.error('分享请求失败:', error)
        console.error('错误详情:', {
          message: error.message,
          response: error.response?.data
        })
        ElMessage.error('分享失败，请重试')
        return false
      }
    },

    // 保存草稿
    async saveDraft(draftData: {
      title: string,
      content: string,
      themeId: number
    }) {
      const userId = this.getCurrentUserId()
      if (!userId) {
        ElMessage.warning('请先登录')
        return
      }

      this.loading = true
      this.error = null
      try {
        const response = await axios.post(`${API_BASE_URL}/write/draft`, {
          ...draftData,
          authorId: userId,
          summary: '',
          relatedKnowledge: ['']
        }, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })
        
        if (response.data.code === 200) {
          ElMessage.success('草稿保存成功')
          return response.data
        } else {
          throw new Error(response.data.msg || '保存草稿失败')
        }
    } catch (error) {
        console.error('Failed to save draft:', error)
        this.error = '保存草稿失败'
        ElMessage.error(this.error)
      throw error
      } finally {
        this.loading = false
      }
    },

    // 更新文章列表中的文章数据
    updateArticleInList(updatedArticle: Article) {
      const index = this.articles.findIndex(article => article.id === updatedArticle.id)
      if (index !== -1) {
        // 创建新的文章数组以触发响应式更新
        const newArticles = [...this.articles]
        // 保留原有的作者信息
        const originalAuthor = this.articles[index].author
        // 更新文章，同时确保所有计数器都有值
        newArticles[index] = {
          ...updatedArticle,
          author: originalAuthor,
          supportUserCount: updatedArticle.supportUserCount || 0,
          opposeUserCount: updatedArticle.opposeUserCount || 0,
          collectionUserCount: updatedArticle.collectionUserCount || 0,
          shareUserCount: updatedArticle.shareUserCount || 0,
          viewUserCount: updatedArticle.viewUserCount || 0,
          commentCount: updatedArticle.commentCount || 0
        }
        // 更新整个数组以触发响应式
        this.articles = newArticles
        console.log('文章列表已更新:', this.articles[index])
      }
    },

    // 获取渲染后的文章数据
    async getRenderedArticle(articleId: number) {
      console.log('开始获取文章数据:', articleId)
      try {
        const response = await axios.get(`${API_BASE_URL}/getRenderedArticle`, {
          params: { articleId }
        })
        console.log('获取文章数据响应:', response.data)
        
        if (response.data) {
          // 更新文章列表
          this.updateArticleInList(response.data)
          return response.data
        }
        return false
      } catch (error) {
        console.error('获取文章数据失败:', error)
        ElMessage.error('获取文章数据失败')
        return false
      }
    },

    // 更新文章交互状态
    updateArticleState(articleId: number, type: 'support' | 'oppose' | 'collect', value: boolean) {
      const index = this.articles.findIndex(article => article.id === articleId)
      if (index !== -1) {
        const newArticles = [...this.articles]
        const article = { ...newArticles[index] }
        
        article[type] = value
        const countField = `${type}UserCount` as 'supportUserCount' | 'opposeUserCount' | 'collectionUserCount'
        article[countField] += value ? 1 : -1
        
        newArticles[index] = article
        this.articles = newArticles
      }
    }
  }
}) 