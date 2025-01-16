<template>
  <div class="likes-container">
    <div class="article-list" v-loading="loading">
      <!-- 文章列表 -->
      <div v-for="article in likedArticles" :key="article.id" class="article-item">
        <div class="article-main">
          <div class="article-content">
            <h3 class="article-title" @click="handleArticleClick(article.id)">
              {{ article.title }}
            </h3>
            <p class="article-summary">{{ article.summary || '暂无概要' }}</p>
            <div class="article-meta">
              <span class="meta-item">
                <el-icon><User /></el-icon>
                {{ article.author.username }}
              </span>
              <span class="meta-item">
                <el-icon><View /></el-icon>
                {{ article.viewUserCount }}
              </span>
              <span class="meta-item">
                <el-icon><Star /></el-icon>
                {{ article.supportUserCount }}
              </span>
              <span class="meta-item">
                <el-icon><ChatLineRound /></el-icon>
                {{ article.commentCount }}
              </span>
              <span class="meta-item">
                <el-icon><Collection /></el-icon>
                {{ article.collectionUserCount }}
              </span>
              <span class="meta-item">
                <el-icon><Share /></el-icon>
                {{ article.shareUserCount }}
              </span>
              <span class="create-time">
                <el-icon><Clock /></el-icon>
                {{ formatDateTime(article.createTime) }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <el-empty 
        v-if="likedArticles.length === 0 && !loading" 
        description="暂无点赞文章"
      >
        <template #description>
          <p>去发现更多优质文章吧</p>
        </template>
        <el-button type="primary" @click="router.push('/home')">
          去首页看看
        </el-button>
      </el-empty>
    </div>

    <!-- 分页器 -->
    <div class="pagination" v-if="likedArticles.length > 0">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="handlePageChange"
        background
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { 
  User, 
  View, 
  Star, 
  ChatLineRound, 
  Collection, 
  Share,
  Clock 
} from '@element-plus/icons-vue'
import request from '../../utils/request'

const router = useRouter()

// 加载状态
const loading = ref(false)

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 文章数据接口
interface Author {
  id: number
  username: string
}

interface Article {
  id: number
  title: string
  theme: string
  content: string
  summary: string | null
  relatedKnowledge: any[]
  createTime: string
  viewUserCount: number
  supportUserCount: number
  opposeUserCount: number
  commentCount: number
  collectionUserCount: number
  shareUserCount: number
  author: Author
  oppose: boolean
  collect: boolean
  support: boolean
}

const likedArticles = ref<Article[]>([])

// 获取用户ID
const userId = computed(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  return userInfo.id
})

// 加载点赞文章列表
const loadLikedArticles = async () => {
  if (!userId.value) {
    console.log('未找到用户ID，请先登录')
    return
  }

  console.log('开始加载点赞文章列表，用户ID:', userId.value)
  loading.value = true
  
  try {
    const response = await request.get<Article[]>('/server/personalCenterData/getSupportArticles', {
      params: { userId: userId.value }
    })
    
    console.log('API返回原始数据:', JSON.stringify(response.data, null, 2))
    
    if (Array.isArray(response.data)) {
      likedArticles.value = response.data
      total.value = response.data.length
      console.log('点赞文章列表数据处理完成:')
      response.data.forEach((article, index) => {
        console.log(`文章 ${index + 1}:`, {
          id: article.id,
          title: article.title,
          author: article.author.username,
          summary: article.summary,
          content: article.content?.substring(0, 100) + '...',
          createTime: article.createTime,
          stats: {
            views: article.viewUserCount,
            likes: article.supportUserCount,
            comments: article.commentCount,
            collections: article.collectionUserCount,
            shares: article.shareUserCount
          }
        })
      })
      
      if (total.value === 0) {
        console.log('暂无点赞文章')
      }
    } else {
      console.warn('API返回数据格式错误，期望数组类型:', response.data)
      likedArticles.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('加载点赞文章列表失败:', error)
    likedArticles.value = []
    total.value = 0
  } finally {
    loading.value = false
    console.log('点赞文章列表加载完成，共', total.value, '篇文章')
  }
}

// 格式化日期时间
const formatDateTime = (dateTimeStr: string) => {
  console.log('格式化时间:', dateTimeStr)
  const date = new Date(dateTimeStr)
  const formatted = new Intl.DateTimeFormat('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  }).format(date)
  console.log('格式化后:', formatted)
  return formatted
}

// 处理文章点击
const handleArticleClick = (articleId: number) => {
  console.log('点击文章，跳转到文章详情页，文章ID:', articleId)
  router.push(`/article/${articleId}`)
}

// 处理分页变化
const handlePageChange = (page: number) => {
  console.log('切换页码:', page)
  currentPage.value = page
  loadLikedArticles()
}

// 组件挂载时加载数据
onMounted(() => {
  loadLikedArticles()
})
</script>

<style scoped>
.likes-container {
  background: white;
  border-radius: 0 0 8px 8px;
  padding: 24px;
  min-height: 400px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.article-item {
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
  transition: all 0.3s ease;
}

.article-item:hover {
  background-color: #f9f9f9;
}

.article-item:last-child {
  border-bottom: none;
}

.article-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 12px;
  cursor: pointer;
}

.article-title:hover {
  color: var(--el-color-primary);
}

.article-summary {
  font-size: 14px;
  color: #606266;
  margin: 0 0 12px;
  line-height: 1.6;
}

.article-meta {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
  font-size: 13px;
  color: #909399;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.create-time {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-left: auto;
  color: #606266;
}

.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style>
