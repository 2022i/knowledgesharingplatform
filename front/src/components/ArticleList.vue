<template>
  <div class="article-list">
    <div v-for="article in articles" :key="article.id" class="article-item">
      <div class="article-main">
        <h2 class="article-title">
          <router-link :to="`/article/${article.id}`">{{ article.title }}</router-link>
        </h2>
        <p class="article-excerpt">{{ article.excerpt }}</p>
        <div class="article-meta">
          <div class="author-info">
            <el-avatar :size="24" :src="article.author.avatar" />
            <span class="author-name">{{ article.author.name }}</span>
          </div>
          <div class="article-stats">
            <!-- 阅读量 -->
            <span class="stat-item">
              <el-icon><View /></el-icon>
              {{ formatNumber(article.viewUserIds?.length || 0) }}
            </span>

            <!-- 点赞 -->
            <el-button 
              class="action-btn"
              :type="isSupported(article.id) ? 'primary' : 'default'"
              link
              @click="handleSupport(article)"
            >
              <el-icon><CaretTop /></el-icon>
              {{ formatNumber(article.supportUserIds?.length || 0) }}
            </el-button>

            <!-- 反对 -->
            <el-button 
              class="action-btn"
              :type="isOpposed(article.id) ? 'danger' : 'default'"
              link
              @click="handleOppose(article)"
            >
              <el-icon><CaretBottom /></el-icon>
              {{ formatNumber(article.opposeUserIds?.length || 0) }}
            </el-button>

            <!-- 收藏 -->
            <el-button 
              class="action-btn"
              :type="isCollected(article.id) ? 'warning' : 'default'"
              link
              @click="handleCollect(article)"
            >
              <el-icon><Star /></el-icon>
              {{ formatNumber(article.collectionUserIds?.length || 0) }}
            </el-button>

            <!-- 分享 -->
            <el-button 
              class="action-btn"
              link
              @click="handleShare(article)"
            >
              <el-icon><Share /></el-icon>
              {{ formatNumber(article.shareUserIds?.length || 0) }}
            </el-button>

            <!-- 评论 -->
            <router-link 
              :to="`/article/${article.id}#comments`" 
              class="stat-item comment-link"
            >
              <el-icon><ChatLineRound /></el-icon>
              {{ getCommentCount(article) }}
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { View, Star, CaretTop, CaretBottom, Share, ChatLineRound } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import ArticleCommentPreview from './ArticleCommentPreview.vue'

// 模拟当前用户数据
const currentUser = ref({
  id: 1,
  username: '当前用户',
  supportArticleIds: [] as number[],
  opposeArticleIds: [] as number[],
  collectArticleId: [] as number[],
  shareArticleId: [] as number[],
})

interface Comment {
  id: number
  fatherId: number | null
  userId: number
  articleId: number
  content: string
  createTime: string
  user?: {
    id: number
    name: string
    avatar?: string
  }
}

interface Article {
  id: number
  title: string
  excerpt: string
  authorId: number
  themeId: number
  createTime: string
  relatedKnowledgeId: string[]
  collectionUserIds: number[]
  shareUserIds: number[]
  supportUserIds: number[]
  opposeUserIds: number[]
  viewUserIds: number[]
  commentIds: number[]
  comments?: Comment[] | number
  author: {
    name: string
    avatar: string
  }
}

const props = defineProps<{
  articles: Article[]
}>()

// 格式化数字
const formatNumber = (num: number) => {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + 'w'
  }
  return num.toString()
}

// 获取评论列表
const getComments = (article: Article): Comment[] => {
  if (Array.isArray(article.comments)) {
    return article.comments
  }
  return []
}

// 获取评论数量
const getCommentCount = (article: Article): number => {
  if (typeof article.comments === 'number') {
    return article.comments
  }
  if (Array.isArray(article.comments)) {
    return article.comments.length
  }
  return 0
}

// 检查当前用户是否已点赞
const isSupported = (articleId: number) => {
  return currentUser.value.supportArticleIds.includes(articleId)
}

// 检查当前用户是否已反对
const isOpposed = (articleId: number) => {
  return currentUser.value.opposeArticleIds.includes(articleId)
}

// 检查当前用户是否已收藏
const isCollected = (articleId: number) => {
  return currentUser.value.collectArticleId.includes(articleId)
}

// 处理点赞
const handleSupport = (article: Article) => {
  const userId = currentUser.value.id
  const articleId = article.id
  
  // 如果已经点赞，则取消点赞
  if (isSupported(articleId)) {
    currentUser.value.supportArticleIds = currentUser.value.supportArticleIds.filter(id => id !== articleId)
    article.supportUserIds = article.supportUserIds.filter(id => id !== userId)
    ElMessage.success('已取消点赞')
  } else {
    // 如果已经点踩，则先取消点踩
    if (isOpposed(articleId)) {
      handleOppose(article)
    }
    currentUser.value.supportArticleIds.push(articleId)
    article.supportUserIds.push(userId)
    ElMessage.success('点赞成功')
  }
}

// 处理反对
const handleOppose = (article: Article) => {
  const userId = currentUser.value.id
  const articleId = article.id
  
  if (isOpposed(articleId)) {
    currentUser.value.opposeArticleIds = currentUser.value.opposeArticleIds.filter(id => id !== articleId)
    article.opposeUserIds = article.opposeUserIds.filter(id => id !== userId)
    ElMessage.success('已取消反对')
  } else {
    // 如果已经点赞，则先取消点赞
    if (isSupported(articleId)) {
      handleSupport(article)
    }
    currentUser.value.opposeArticleIds.push(articleId)
    article.opposeUserIds.push(userId)
    ElMessage.success('已反对')
  }
}

// 处理收藏
const handleCollect = (article: Article) => {
  const userId = currentUser.value.id
  const articleId = article.id
  
  if (isCollected(articleId)) {
    currentUser.value.collectArticleId = currentUser.value.collectArticleId.filter(id => id !== articleId)
    article.collectionUserIds = article.collectionUserIds.filter(id => id !== userId)
    ElMessage.success('已取消收藏')
  } else {
    currentUser.value.collectArticleId.push(articleId)
    article.collectionUserIds.push(userId)
    ElMessage.success('收藏成功')
  }
}

// 处理分享
const handleShare = (article: Article) => {
  const userId = currentUser.value.id
  const articleId = article.id
  
  // 复制链接到剪贴板
  const url = `${window.location.origin}/article/${articleId}`
  navigator.clipboard.writeText(url)
  ElMessage.success('链接已复制到剪贴板')
  
  // 更新分享记录
  if (!currentUser.value.shareArticleId.includes(articleId)) {
    currentUser.value.shareArticleId.push(articleId)
    article.shareUserIds.push(userId)
  }
}

// 处理评论回复
const handleReplyAdded = (articleId: number, comment: Comment) => {
  emit('comment-added', { articleId, comment })
}

const emit = defineEmits<{
  (e: 'comment-added', data: { articleId: number, comment: Comment }): void
}>()
</script>

<style scoped>
.article-list {
  background: #fff;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

.article-item {
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.article-item:last-child {
  border-bottom: none;
}

.article-title {
  margin: 0 0 8px;
  font-size: 18px;
  font-weight: 600;
  line-height: 1.6;
}

.article-title a {
  color: #121212;
  text-decoration: none;
}

.article-title a:hover {
  color: #056de8;
}

.article-excerpt {
  margin: 0 0 12px;
  font-size: 15px;
  line-height: 1.67;
  color: #646464;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.article-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-name {
  font-size: 14px;
  color: #8590a6;
}

.article-stats {
  display: flex;
  align-items: center;
  gap: 16px;
  color: #8590a6;
  font-size: 14px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
}

.action-btn :deep(.el-icon) {
  margin-right: 2px;
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
}

.comment-icon {
  cursor: pointer;
}

.comment-icon:hover {
  color: #056de8;
}

:deep(.comment-preview-popover) {
  padding: 0;
  .el-popover__title {
    margin: 0;
  }
}

.comment-link {
  color: #8590a6;
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 4px;
}

.comment-link:hover {
  color: #056de8;
}
</style> 