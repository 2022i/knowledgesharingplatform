import { defineStore } from 'pinia'
import axios from 'axios'

export interface Comment {
  id: number
  fatherId: number
  userId: number
  articleId: number
  username: string
  content: string
  replyCount: number
  createTime: string
}

interface CommentState {
  comments: Comment[]
  loading: boolean
  error: string | null
}

export const useCommentStore = defineStore('comment', {
  state: (): CommentState => ({
    comments: [],
    loading: false,
    error: null
  }),

  getters: {
    // 获取文章的根评论
    getRootComments: (state) => (articleId: number) => {
      return state.comments.filter(comment => comment.fatherId === 0 && comment.articleId === articleId)
    },

    // 获取评论的回复
    getCommentReplies: (state) => (commentId: number) => {
      return state.comments.filter(comment => comment.fatherId === commentId)
    },

    // 获取评论总数
    getCommentCount: (state) => (articleId: number) => {
      return state.comments.filter(comment => comment.articleId === articleId).length
    },

    isLoading: (state) => state.loading,
    getError: (state) => state.error
  },

  actions: {
    // 获取文章的根评论
    async fetchArticleRootComments(articleId: number) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/server/articleComment/getArticleRootComments`, {
          params: { articleId },
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        if (response.data) {
          // 更新评论列表，只保留根评论
          this.comments = response.data
        }
      } catch (error) {
        console.error('获取根评论失败:', error)
        this.error = '获取评论失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    // 获取评论的所有回复
    async fetchCommentReplies(commentId: number) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/server/articleComment/getUserCommentedList`, {
          params: { commentId },
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        if (response.data) {
          // 将回复添加到评论列表中
          this.comments = [...this.comments, ...response.data]
        }
      } catch (error) {
        console.error('获取评论回复失败:', error)
        this.error = '获取回复失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    // 添加根评论
    async addComment(articleId: number, content: string) {
      this.loading = true
      this.error = null
      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        const response = await axios.post('/server/write/articleComment', {
          content,
          userId: userInfo.id,
          articleId,
          fatherId: 0
        }, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        if (response.data.code === 200) {
          // 重新获取根评论列表
          await this.fetchArticleRootComments(articleId)
          return response.data
        }
      } catch (error) {
        console.error('添加评论失败:', error)
        this.error = '添加评论失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    // 添加评论的回复
    async addReply(articleId: number, fatherId: number, content: string) {
      this.loading = true
      this.error = null
      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        const response = await axios.post('/server/write/commentComment', {
          content,
          userId: userInfo.id,
          articleId,
          fatherId
        }, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })

        if (response.data.code === 200) {
          // 重新获取该评论的回复列表
          await this.fetchCommentReplies(fatherId)
          return response.data
        }
      } catch (error) {
        console.error('添加回复失败:', error)
        this.error = '添加回复失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    // 重置 store 状态
    reset() {
      this.comments = []
      this.loading = false
      this.error = null
    }
  }
}) 