// 作者信息接口
export interface Author {
  id: number
  name: string
  avatar: string
}

// 文章状态类型
export type ArticleStatus = 'draft' | 'pending' | 'published' | 'rejected' | 'pending_delete'

// 文章信息接口
export interface Article {
  id: number
  title: string
  excerpt: string
  content?: string
  views: number
  comments: number
  likes: number
  status: ArticleStatus
  createTime: string
  updateTime?: string
  rejectReason?: string
  tags?: string[]
  author: Author
}

export interface ArticleFilter {
  page: number
  pageSize: number
  status?: ArticleStatus
  keyword?: string
  authorId?: number
} 