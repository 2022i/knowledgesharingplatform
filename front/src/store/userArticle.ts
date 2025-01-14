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
    // 获取用户ID
    getCurrentUserId(): number {
      console.log('=== 认证信息 ===')
      console.log('Token:', localStorage.getItem('token'))
      console.log('UserInfo:', localStorage.getItem('userInfo'))
      
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      console.log('解析后的用户信息:', userInfo)
      return Number(userInfo.id) || -1
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
      if (!userId || userId === -1) {
        throw new Error('请先登录')
      }

      console.log('记录浏览历史:', articleId, '用户ID:', userId)
      try {
        const response = await axios.put(`${API_BASE_URL}/addData/viewData`, null, {
          params: {
            articleId,
            userId
          },
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        console.log('浏览记录响应:', response.data)
        return response.data.code === 200
      } catch (error) {
        console.error('记录浏览历史失败:', error)
        throw error
      }
    },

    // 点赞文章
    async toggleSupport(articleId: number) {
      const userId = this.getCurrentUserId()
      if (!userId || userId === -1) {
        throw new Error('请先登录')
      }

      console.log('开始点赞文章:', articleId, '用户ID:', userId)
      try {
        const response = await axios.put(`${API_BASE_URL}/addDataAndSentMessage/supportData`, null, {
          params: {
            articleId,
            userId
          },
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        console.log('点赞响应:', response.data)
        if (response.data.code === 200) {
          // 更新文章状态
          this.updateArticleState(articleId, 'support', !this.articles.find(a => a.id === articleId)?.support)
          return await this.getRenderedArticle(articleId)
        }
        throw new Error(response.data.msg || '点赞失败')
      } catch (error) {
        console.error('点赞失败:', error)
        throw error
      }
    },

    // 反对文章
    async toggleOppose(articleId: number) {
      const userId = this.getCurrentUserId()
      if (!userId || userId === -1) {
        throw new Error('请先登录')
      }

      console.log('开始反对文章:', articleId, '用户ID:', userId)
      try {
        const response = await axios.put(`${API_BASE_URL}/addDataAndSentMessage/oppositionData`, null, {
          params: {
            articleId,
            userId
          },
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        console.log('反对响应:', response.data)
        if (response.data.code === 200) {
          // 更新文章状态
          this.updateArticleState(articleId, 'oppose', !this.articles.find(a => a.id === articleId)?.oppose)
          return await this.getRenderedArticle(articleId)
        }
        throw new Error(response.data.msg || '反对失败')
      } catch (error) {
        console.error('反对失败:', error)
        throw error
      }
    },

    // 收藏文章
    async toggleCollect(articleId: number) {
      const userId = this.getCurrentUserId()
      if (!userId || userId === -1) {
        throw new Error('请先登录')
      }

      console.log('开始收藏文章:', articleId, '用户ID:', userId)
      try {
        const response = await axios.put(`${API_BASE_URL}/addDataAndSentMessage/collectionData`, null, {
          params: {
            articleId,
            userId
          },
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        console.log('收藏响应:', response.data)
        if (response.data.code === 200) {
          // 更新文章状态
          this.updateArticleState(articleId, 'collect', !this.articles.find(a => a.id === articleId)?.collect)
          return await this.getRenderedArticle(articleId)
        }
        throw new Error(response.data.msg || '收藏失败')
      } catch (error) {
        console.error('收藏失败:', error)
        throw error
      }
    },

    // 分享文章
    async shareArticle(articleId: number) {
      const userId = this.getCurrentUserId()
      if (!userId || userId === -1) {
        throw new Error('请先登录')
      }

      console.log('开始分享文章:', articleId, '用户ID:', userId)
      try {
        const response = await axios.put(`${API_BASE_URL}/addDataAndSentMessage/shareData`, null, {
          params: {
            articleId,
            userId
          },
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        console.log('分享响应:', response.data)
        if (response.data.code === 200) {
          return await this.getRenderedArticle(articleId)
        }
        throw new Error(response.data.msg || '分享失败')
      } catch (error) {
        console.error('分享失败:', error)
        throw error
      }
    },

    // 添加评论
    async addComment(content: string, articleId: number, fatherId: number = 0) {
      const userId = this.getCurrentUserId()
      if (!userId || userId === -1) {
        throw new Error('请先登录')
      }

      console.log('添加评论:', { content, articleId, fatherId, userId })
      try {
        const endpoint = fatherId === 0 ? '/write/articleComment' : '/write/commentComment'
        const response = await axios.post(`${API_BASE_URL}${endpoint}`, {
          content,
          userId,
          articleId,
          fatherId
        }, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        console.log('评论响应:', response.data)
        if (response.data.code === 200) {
          return true
        }
        throw new Error(response.data.msg || '评论失败')
      } catch (error) {
        console.error('评论失败:', error)
        throw error
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

    // 获取渲染文章数据
    async getRenderedArticle(articleId: number) {
      const userId = this.getCurrentUserId()
      try {
        console.log('获取文章数据:', { articleId, userId })
        
        const response = await axios.get(`${API_BASE_URL}/getRenderedArticle`, {
          params: {
            articleId,
            userId
          },
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        if (response.data) {
          return response.data
        }
        throw new Error('获取文章数据失败')
      } catch (error) {
        console.error('获取文章数据失败:', error)
        throw error
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