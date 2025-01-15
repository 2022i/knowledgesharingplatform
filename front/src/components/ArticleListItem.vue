<template>
  <div class="article-item" @click.stop="handleArticleClick">
    <div class="article-header">
      <h3 class="article-title">{{ article.title }}</h3>
      <div class="article-meta">
        <span class="author">{{ article.author?.username || article.Author?.username || '未知用户' }}</span>
        <span class="theme">{{ article.theme || '未分类' }}</span>
        <span class="time">{{ formatTime(article.createTime) }}</span>
      </div>
    </div>
    
    <p class="article-summary">{{ article.summary || '暂无概要' }}</p>
    
    <div class="article-footer">
      <div class="article-stats">
        <span class="stat-item">
          <el-icon><View /></el-icon>
          {{ article.viewUserCount || 0 }}
        </span>
        <span class="stat-item" @click.stop="$emit('support', article.id)">
          <el-icon :class="{ active: article.support }"><CaretTop /></el-icon>
          {{ article.supportUserCount || 0 }}
        </span>
        <span class="stat-item" @click.stop="$emit('oppose', article.id)">
          <el-icon :class="{ active: article.oppose }"><CaretBottom /></el-icon>
          {{ article.opposeUserCount || 0 }}
        </span>
        <span class="stat-item" @click.stop="$emit('collect', article.id)">
          <el-icon :class="{ active: article.collect }"><Star /></el-icon>
          {{ article.collectionUserCount || 0 }}
        </span>
        <span class="stat-item" @click.stop="$emit('share', article.id)">
          <el-icon><Share /></el-icon>
          {{ article.shareUserCount || 0 }}
        </span>
        <span class="stat-item comment-item" @click.stop="$emit('comment', article)">
          <el-icon><ChatDotRound /></el-icon>
          {{ article.commentCount || 0 }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { View, CaretTop, CaretBottom, Star, Share, ChatDotRound } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

const router = useRouter()

// Props 定义
const props = defineProps<{
  article: {
    id: number
    title: string
    theme: string
    summary: string | null
    content: string
    createTime: string
    viewUserCount: number
    supportUserCount: number
    opposeUserCount: number
    collectionUserCount: number
    shareUserCount: number
    commentCount: number
    author: {
      id: number
      username: string
    }
    support: boolean
    oppose: boolean
    collect: boolean
  }
}>()

// Emits 定义
defineEmits<{
  (e: 'support', id: number): void
  (e: 'oppose', id: number): void
  (e: 'collect', id: number): void
  (e: 'share', id: number): void
  (e: 'comment', article: any): void
}>()

// 格式化时间
const formatTime = (time: string) => {
  return dayjs(time).fromNow()
}

// 处理文章点击
const handleArticleClick = () => {
  router.push(`/article/${props.article.id}`)
}
</script>

<style scoped>
.article-item {
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.3s ease;
}

.article-item:hover {
  background-color: #f9f9f9;
}

.article-item:last-child {
  border-bottom: none;
}

.article-header {
  margin-bottom: 12px;
}

.article-title {
  font-size: 18px;
  font-weight: 600;
  color: #121212;
  margin: 0 0 8px;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #8590a6;
  font-size: 14px;
}

.article-summary {
  color: #444;
  font-size: 14px;
  line-height: 1.6;
  margin: 0 0 16px;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.article-stats {
  display: flex;
  gap: 24px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #8590a6;
  font-size: 14px;
  cursor: pointer;
}

.stat-item:hover {
  color: #056de8;
}

.stat-item .el-icon {
  font-size: 16px;
}

.stat-item .el-icon.active {
  color: #056de8;
}

.comment-item {
  margin-left: auto;
}
</style> 