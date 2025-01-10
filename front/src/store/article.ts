import { defineStore } from 'pinia'
import type { Article, ArticleStatus } from '../types/article'

// 定义 store 的状态接口
interface ArticleState {
  articles: Article[]
  currentPage: number
  pageSize: number
  total: number
  loading: boolean
  draftArticles: Article[]
}

// 创建 store
export const useArticleStore = defineStore('article', {
  // 定义状态
  state: (): ArticleState => ({
    articles: [],
    draftArticles: [],
    currentPage: 1,
    pageSize: 5,
    total: 0,
    loading: false
  }),

  // 定义 getters
  getters: {
    // 获取当前页的文章列表
    currentPageArticles(state): Article[] {
      const start = (state.currentPage - 1) * state.pageSize
      const end = start + state.pageSize
      return state.articles.slice(start, end)
    },

    // 获取草稿箱文章
    getDraftArticles(state): Article[] {
      return state.draftArticles
    },

    // 获取已发布文章
    getPublishedArticles(state): Article[] {
      return state.articles.filter(article => article.status === 'published')
    },

    // 获取待审核文章
    getPendingArticles(state): Article[] {
      return state.articles.filter(article => article.status === 'pending')
    }
  },

  actions: {
    // 加载文章列表
    async fetchArticles(status?: ArticleStatus) {
      this.loading = true
      try {
        // 模拟API请求
        await new Promise(resolve => setTimeout(resolve, 500))
        
        const mockArticles: Article[] = [
          {
            id: 1,
            title: '如何有效提升编程技能？',
            excerpt: '编程是一项需要持续学习和实践的技能。本文将分享一些有效的学习方法和实践经验，帮助你更好地提升编程能力。',
            content: `
              <h2>建立正确的学习方法</h2>
              <p>很多人在学习编程时往往会陷入一个误区：只重视理论知识的积累，而忽视了实践的重要性。</p>
              <h2>制定合理的学习计划</h2>
              <p>一个好的学习计划应该包括：明确的学习目标、合理的时间安排、具体的实践项目、定期的学习总结。</p>
            `,
            views: 12340,
            comments: 156,
            likes: 2789,
            status: 'published',
            createTime: '2024-03-15 10:00:00',
            tags: ['编程学习', '经验分享', '技能提升'],
            author: {
              id: 1,
              name: '技术专家',
              avatar: 'https://example.com/avatar1.jpg'
            }
          },
          {
            id: 2,
            title: '前端性能优化最佳实践',
            excerpt: '本文将详细介绍前端性能优化的各个方面，包括加载优化、渲染优化、代码优化等多个维度。',
            content: `
              <h2>性能优化的重要性</h2>
              <p>在当今互联网时代，用户体验至关重要，而性能是用户体验的核心指标之一。</p>
              <h2>常见的优化方向</h2>
              <p>1. 资源加载优化</p>
              <p>2. 代码执行优化</p>
              <p>3. 渲染性能优化</p>
            `,
            views: 8560,
            comments: 89,
            likes: 1543,
            status: 'pending',
            createTime: '2024-03-16 14:30:00',
            tags: ['前端开发', '性能优化', '技术分享'],
            author: {
              id: 2,
              name: '前端工程师',
              avatar: 'https://example.com/avatar2.jpg'
            }
          },
          {
            id: 3,
            title: 'Vue3 组件设计模式',
            excerpt: '不合适的组件设计会导致代码难以维护和扩展。本文将介绍Vue3中常见的组件设计模式及最佳实践。',
            content: `
              <h2>组件设计的原则</h2>
              <p>组件设计应遵循单一职责、可复用、可测试等原则。</p>
              <p>本文内容有待完善...</p>
            `,
            views: 0,
            comments: 0,
            likes: 0,
            status: 'draft',
            createTime: '2024-03-16 16:45:00',
            updateTime: '2024-03-16 17:30:00',
            tags: ['Vue3', '组件设计', '前端开发'],
            author: {
              id: 2,
              name: '前端工程师',
              avatar: 'https://example.com/avatar2.jpg'
            }
          },
          {
            id: 4,
            title: 'React状态管理方案对比',
            excerpt: '本文对比分析Redux、MobX、Recoil等主流状态管理方案的优劣，帮助你选择合适的方案。',
            content: `
              <h2>为什么需要状态管理？</h2>
              <p>随着应用规模的增长，组件间的状态共享和管理变得越来越复杂。</p>
              <p>文章结构和内容需要进一步优化...</p>
            `,
            views: 0,
            comments: 0,
            likes: 0,
            status: 'rejected',
            createTime: '2024-03-15 09:20:00',
            updateTime: '2024-03-15 10:30:00',
            rejectReason: '文章内容不够完整，建议补充更多实际案例和性能对比数据',
            tags: ['React', '状态管理', '前端架构'],
            author: {
              id: 3,
              name: '资深前端',
              avatar: 'https://example.com/avatar3.jpg'
            }
          },
          {
            id: 5,
            title: 'TypeScript高级类型实战',
            excerpt: '通过实际案例深入理解TypeScript的高级类型，包括映射类型、条件类型、类型推导等。',
            content: `
              <h2>TypeScript类型系统概述</h2>
              <p>TypeScript提供了强大的类型系统，能够帮助我们在开发阶段发现潜在的问题。</p>
              <h2>高级类型实战</h2>
              <p>1. 映射类型的实际应用</p>
              <p>2. 条件类型的使用场景</p>
            `,
            views: 6789,
            comments: 45,
            likes: 890,
            status: 'published',
            createTime: '2024-03-14 11:00:00',
            tags: ['TypeScript', '前端开发', '编程技巧'],
            author: {
              id: 1,
              name: '技术专家',
              avatar: 'https://example.com/avatar1.jpg'
            }
          },
          {
            id: 6,
            title: '微前端架构实践指南',
            excerpt: '本文将介绍微前端的概念、实现方案、实践经验，以及如何在大型项目中应用微前端架构。',
            content: `
              <h2>什么是微前端？</h2>
              <p>微前端是一种将前端应用分解成一些更小、更简单的能够独立开发、测试、部署的应用的架构风格。</p>
              <p>待补充更多实践案例...</p>
            `,
            views: 0,
            comments: 0,
            likes: 0,
            status: 'pending',
            createTime: '2024-03-16 15:20:00',
            tags: ['微前端', '前端架构', '技术架构'],
            author: {
              id: 4,
              name: '架构师',
              avatar: 'https://example.com/avatar4.jpg'
            }
          },
          {
            id: 7,
            title: 'Node.js性能调优实战',
            excerpt: '从实际项目出发，分享Node.js应用的性能优化经验，包括内存管理、异步操作、集群部署等。',
            content: `
              <h2>性能优化的关键指标</h2>
              <p>在进行性能优化之前，我们需要明确优化的目标和关键指标。</p>
              <p>文章需要补充更多监控和调优的具体方法...</p>
            `,
            views: 0,
            comments: 0,
            likes: 0,
            status: 'rejected',
            createTime: '2024-03-15 16:40:00',
            updateTime: '2024-03-15 17:30:00',
            rejectReason: '建议增加具体的性能优化案例和数据支撑，当前内容过于理论化',
            tags: ['Node.js', '性能优化', '后端开发'],
            author: {
              id: 5,
              name: '后端专家',
              avatar: 'https://example.com/avatar5.jpg'
            }
          },
          {
            id: 8,
            title: '前端安全最佳实践',
            excerpt: '前端安全是不容忽视的话题，本文将介绍常见的安全威胁和防范措施。',
            content: `
              <h2>常见的安全威胁</h2>
              <p>1. XSS攻击</p>
              <p>2. CSRF攻击</p>
              <p>3. 点击劫持</p>
              <h2>防范措施</h2>
              <p>针对不同的安全威胁，我们需要采取相应的防范措施。</p>
            `,
            views: 5678,
            comments: 34,
            likes: 876,
            status: 'published',
            createTime: '2024-03-13 14:20:00',
            tags: ['前端安全', 'Web安全', '最佳实践'],
            author: {
              id: 1,
              name: '技术专家',
              avatar: 'https://example.com/avatar1.jpg'
            }
          }
        ]

        if (status) {
          this.articles = mockArticles.filter(article => article.status === status)
        } else {
          this.articles = mockArticles
        }
        
        this.total = this.articles.length
      } catch (error) {
        console.error('Failed to fetch articles:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    // 管理员：获取文章列表
    async getArticleList({ page, pageSize, status, keyword }: { 
      page: number
      pageSize: number
      status?: ArticleStatus
      keyword?: string 
    }) {
      this.loading = true
      try {
        // 模拟API请求
        await new Promise(resolve => setTimeout(resolve, 500))
        
        let filteredArticles = [...this.articles]

        // 过滤掉草稿状态的文章
        filteredArticles = filteredArticles.filter(article => article.status !== 'draft')

        // 根据状态筛选
        if (status) {
          filteredArticles = filteredArticles.filter(article => article.status === status)
        }

        // 根据关键词搜索
        if (keyword) {
          const lowerKeyword = keyword.toLowerCase()
          filteredArticles = filteredArticles.filter(article => 
            article.title.toLowerCase().includes(lowerKeyword) ||
            article.author.name.toLowerCase().includes(lowerKeyword)
          )
        }

        // 计算总数
        const total = filteredArticles.length

        // 分页
        const start = (page - 1) * pageSize
        const end = start + pageSize
        const paginatedArticles = filteredArticles.slice(start, end)

        return {
          data: paginatedArticles,
          total
        }
      } catch (error) {
        console.error('Failed to get article list:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    // 管理员：审核通过文章
    async approveArticle(articleId: number) {
      try {
        const article = this.articles.find(a => a.id === articleId)
        if (article) {
          article.status = 'published'
          article.updateTime = new Date().toISOString()
        }
      } catch (error) {
        console.error('Failed to approve article:', error)
        throw error
      }
    },

    // 管理员：拒绝文章
    async rejectArticle(articleId: number, reason: string) {
      try {
        const article = this.articles.find(a => a.id === articleId)
        if (article) {
          article.status = 'rejected'
          article.rejectReason = reason
          article.updateTime = new Date().toISOString()
        }
      } catch (error) {
        console.error('Failed to reject article:', error)
        throw error
      }
    },

    // 保存草稿
    async saveDraft(article: Partial<Article>) {
      try {
        // 模拟API请求
        await new Promise(resolve => setTimeout(resolve, 500))
        
        const newDraft: Article = {
          id: Date.now(),
          title: article.title || '无标题文章',
          excerpt: article.content?.slice(0, 200) || '暂无简介',
          content: article.content,
          views: 0,
          comments: 0,
          likes: 0,
          status: 'draft',
          createTime: new Date().toISOString(),
          updateTime: new Date().toISOString(),
          author: {
            id: 1, // 应该是当前登录用户的ID
            name: '当前用户',
            avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
          }
        }

        this.draftArticles.unshift(newDraft)
        return newDraft
      } catch (error) {
        console.error('Failed to save draft:', error)
        throw error
      }
    },

    // 发布文章
    async publishArticle(article: Article) {
      try {
        // 模拟API请求
        await new Promise(resolve => setTimeout(resolve, 500))
        
        const publishedArticle: Article = {
          ...article,
          status: 'pending', // 改为待审核状态
          updateTime: new Date().toISOString()
        }

        // 如果是从草稿发布，删除草稿
        if (article.status === 'draft') {
          this.draftArticles = this.draftArticles.filter(draft => draft.id !== article.id)
        }

        this.articles.unshift(publishedArticle)
        return publishedArticle
      } catch (error) {
        console.error('Failed to publish article:', error)
        throw error
      }
    },

    // 删除文章
    async deleteArticle(articleId: number, status: ArticleStatus = 'published') {
      try {
        // 模拟API请求
        await new Promise(resolve => setTimeout(resolve, 500))
        
        if (status === 'draft') {
          this.draftArticles = this.draftArticles.filter(article => article.id !== articleId)
        } else {
          this.articles = this.articles.filter(article => article.id !== articleId)
        }
      } catch (error) {
        console.error('Failed to delete article:', error)
        throw error
      }
    },

    // 设置当前页码
    setCurrentPage(page: number) {
      this.currentPage = page
    },

    // 设置每页显示数量
    setPageSize(size: number) {
      this.pageSize = size
      // 重新计算当前页，避免超出范围
      const maxPage = Math.ceil(this.total / size)
      if (this.currentPage > maxPage) {
        this.currentPage = maxPage
      }
    },

    // 管理员：拒绝删除请求
    async rejectDeleteRequest(articleId: number, reason: string) {
      try {
        const article = this.articles.find(a => a.id === articleId)
        if (article) {
          article.status = article.status === 'pending_delete' ? 'published' : article.status
          article.rejectReason = reason
          article.updateTime = new Date().toISOString()
        }
      } catch (error) {
        console.error('Failed to reject delete request:', error)
        throw error
      }
    },

    // 用户：请求删除文章
    async requestDeleteArticle(articleId: number) {
      try {
        const article = this.articles.find(a => a.id === articleId)
        if (article) {
          article.status = 'pending_delete'
          article.updateTime = new Date().toISOString()
        }
      } catch (error) {
        console.error('Failed to request article deletion:', error)
        throw error
      }
    }
  }
}) 