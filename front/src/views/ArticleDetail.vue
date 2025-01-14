<template>
  <div class="article-detail">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-wrapper">
      <el-skeleton :rows="10" animated />
    </div>
    
    <!-- 文章内容 -->
    <template v-else-if="article">
      <!-- 文章头部信息 -->
      <div class="article-header">
        <h1 class="article-title">{{ article.title }}</h1>
        <div class="article-meta">
          <div class="meta-left">
            <div class="author-info">
              <el-avatar :size="32" :src="article.Author?.avatar">
                {{ article.Author?.username?.charAt(0).toUpperCase() }}
              </el-avatar>
              <span class="author-name">{{ article.Author?.username }}</span>
            </div>
            <el-tag size="small" type="info">{{ article.theme }}</el-tag>
            <span class="publish-time">{{ formatTime(article.createTime) }}</span>
          </div>
          <div class="meta-right">
            <span class="stat-item">
              <el-icon><View /></el-icon>
              {{ formatNumber(article.viewUserCount) }}
            </span>
          </div>
        </div>
        
        <!-- 知识标签 -->
        <div v-if="article.relatedKnowledge?.length" class="knowledge-tags">
          <el-tag
            v-for="tag in article.relatedKnowledge"
            :key="tag"
            size="small"
            effect="plain"
            class="knowledge-tag"
          >
            {{ tag }}
          </el-tag>
        </div>
      </div>

      <!-- 文章正文 -->
      <div class="article-content markdown-body" v-html="processContent(article.content)"></div>

      <!-- 文章底部操作栏 -->
      <div class="article-actions">
        <div class="action-buttons">
          <!-- 点赞 -->
          <el-button 
            :type="article.support ? 'primary' : 'default'"
            :icon="CaretTop"
            @click="handleSupport"
          >
            {{ article.support ? '已点赞' : '点赞' }}
            <span class="count">{{ formatNumber(article.supportUserCount) }}</span>
          </el-button>

          <!-- 反对 -->
          <el-button
            :type="article.oppose ? 'danger' : 'default'"
            :icon="CaretBottom"
            @click="handleOppose"
          >
            {{ article.oppose ? '已反对' : '反对' }}
            <span class="count">{{ formatNumber(article.opposeUserCount) }}</span>
          </el-button>

          <!-- 收藏 -->
          <el-button
            :type="article.collect ? 'warning' : 'default'"
            :icon="Star"
            @click="handleCollect"
          >
            {{ article.collect ? '已收藏' : '收藏' }}
            <span class="count">{{ formatNumber(article.collectionUserCount) }}</span>
          </el-button>

          <!-- 分享 -->
          <el-button
            :icon="Share"
            @click="handleShare"
          >
            分享
            <span class="count">{{ formatNumber(article.shareUserCount) }}</span>
          </el-button>
        </div>
      </div>

      <!-- 评论区 -->
      <div id="comments" class="comments-section">
        <ArticleComments :article-id="articleId" />
      </div>
    </template>

    <!-- 加载失败 -->
    <el-empty 
      v-else 
      description="文章加载失败" 
      :image-size="200"
    >
      <el-button type="primary" @click="loadArticle">重试</el-button>
    </el-empty>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { View, CaretTop, CaretBottom, Star, Share } from '@element-plus/icons-vue'
import request from '../utils/request'
import { useUserArticleStore } from '../store/userArticle'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'
import ArticleComments from '../components/ArticleComments.vue'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

const route = useRoute()
const router = useRouter()
const articleStore = useUserArticleStore()

// 文章ID
const articleId = Number(route.params.id)

// 加载状态
const loading = ref(true)
// 文章数据
const article = ref<any>(null)

// 加载文章数据
const loadArticle = async () => {
  loading.value = true
  try {
    const response = await request.get('/server/getRenderedArticle', {
      params: { articleId }
    })
    article.value = response.data
  } catch (error) {
    console.error('Failed to load article:', error)
    ElMessage.error('文章加载失败，请重试')
    article.value = null
  } finally {
    loading.value = false
  }
}

// 格式化数字
const formatNumber = (num: number) => {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + 'w'
  }
  return num.toString()
}

// 格式化时间
const formatTime = (time: string) => {
  return dayjs(time).fromNow()
}

// 处理点赞
const handleSupport = async () => {
  try {
    await articleStore.toggleSupport(articleId)
    // 重新加载文章数据以更新状态
    loadArticle()
  } catch (error) {
    console.error('Failed to toggle support:', error)
    ElMessage.error('操作失败，请重试')
  }
}

// 处理反对
const handleOppose = async () => {
  try {
    await articleStore.toggleOppose(articleId)
    // 重新加载文章数据以更新状态
    loadArticle()
  } catch (error) {
    console.error('Failed to toggle oppose:', error)
    ElMessage.error('操作失败，请重试')
  }
}

// 处理收藏
const handleCollect = async () => {
  try {
    await articleStore.toggleCollect(articleId)
    // 重新加载文章数据以更新状态
    loadArticle()
  } catch (error) {
    console.error('Failed to toggle collect:', error)
    ElMessage.error('操作失败，请重试')
  }
}

// 处理分享
const handleShare = async () => {
  try {
    await articleStore.shareArticle(articleId)
    // 重新加载文章数据以更新状态
    loadArticle()
  } catch (error) {
    console.error('Failed to share article:', error)
    ElMessage.error('分享失败，请重试')
  }
}

// 处理文章内容
const processContent = (content: string) => {
  if (!content) return ''
  
  // 创建临时DOM元素
  const tempDiv = document.createElement('div')
  tempDiv.innerHTML = content
  
  // 移除分页相关元素
  const paginationElements = tempDiv.querySelectorAll(
    '.el-pagination, [class*="el-pagination"], .pagination, .pager, nav[role="navigation"]'
  )
  paginationElements.forEach(el => el.remove())
  
  return tempDiv.innerHTML
}

// 组件挂载时加载文章数据
onMounted(() => {
  if (!articleId) {
    ElMessage.error('无效的文章ID')
    router.push('/')
    return
  }
  loadArticle()
})
</script>

<style scoped>
.article-detail {
  max-width: 900px;
  margin: 0 auto;
  padding: 24px;
  background: var(--el-bg-color);
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.loading-wrapper {
  padding: 24px;
}

.article-header {
  margin-bottom: 32px;
}

.article-title {
  margin: 0 0 16px;
  font-size: 28px;
  font-weight: 600;
  line-height: 1.4;
  color: var(--el-text-color-primary);
}

.article-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.meta-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.meta-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-name {
  font-size: 16px;
  font-weight: 500;
  color: var(--el-text-color-primary);
}

.publish-time {
  font-size: 14px;
  color: var(--el-text-color-secondary);
}

.knowledge-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 24px;
}

.article-content {
  margin-bottom: 32px;
  line-height: 1.8;
  overflow-wrap: break-word;
  word-wrap: break-word;
  word-break: break-word;
  max-width: 100%;
}

.article-actions {
  margin-bottom: 32px;
  padding: 16px 0;
  border-top: 1px solid var(--el-border-color-light);
  border-bottom: 1px solid var(--el-border-color-light);
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 16px;
}

.action-buttons .el-button {
  min-width: 100px;
}

.count {
  margin-left: 4px;
  font-size: 14px;
  color: var(--el-text-color-secondary);
}

.comments-section {
  margin-top: 32px;
}

:deep(.article-content) {
  font-size: 16px;
  color: var(--el-text-color-primary);
  
  h1, h2, h3, h4, h5, h6 {
    margin: 24px 0 16px;
    font-weight: 600;
    line-height: 1.4;
  }
  
  h1 { font-size: 28px; }
  h2 { font-size: 24px; }
  h3 { font-size: 20px; }
  h4 { font-size: 18px; }
  h5, h6 { font-size: 16px; }

  p {
    margin: 16px 0;
    line-height: 1.8;
  }

  img {
    max-width: 100%;
    height: auto;
    border-radius: 4px;
    margin: 16px 0;
  }

  a {
    color: var(--el-color-primary);
    text-decoration: none;
    &:hover {
      text-decoration: underline;
    }
  }

  ul, ol {
    margin: 16px 0;
    padding-left: 24px;
  }

  li {
    margin: 8px 0;
  }

  blockquote {
    margin: 16px 0;
    padding: 8px 16px;
    border-left: 4px solid var(--el-color-primary-light-7);
    background-color: var(--el-color-primary-light-9);
    color: var(--el-text-color-regular);
  }

  pre {
    margin: 16px 0;
    padding: 16px;
    background-color: var(--el-bg-color-page);
    border-radius: 4px;
    overflow-x: auto;
  }

  code {
    font-family: Monaco, Consolas, Courier New, monospace;
    font-size: 14px;
    padding: 2px 4px;
    background-color: var(--el-bg-color-page);
    border-radius: 2px;
  }

  table {
    width: 100%;
    margin: 16px 0;
    border-collapse: collapse;
  }

  th, td {
    padding: 8px;
    border: 1px solid var(--el-border-color);
    text-align: left;
  }

  th {
    background-color: var(--el-bg-color-page);
    font-weight: 600;
  }

  hr {
    margin: 24px 0;
    border: none;
    border-top: 1px solid var(--el-border-color);
  }
}

:deep(.markdown-body) {
  background: none;
}
</style>
