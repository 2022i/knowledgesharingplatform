// 文章作者接口
export interface Author {
  id: number
  username: string
}

// 文章接口
export interface Article {
  id: number
  title: string
  Author: Author
  theme: string
  content: string
  summary: string
  relatedKnowledge: string[]
  createTime: string
  viewUserCount: number
  supportUserCount: number
  opposeUserCount: number
  commentCount: number
  collectionUserCount: number
  shareUserCount: number
  support: boolean
  oppose: boolean
  collect: boolean
} 