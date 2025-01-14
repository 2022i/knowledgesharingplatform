<template>
  <div class="view-history-container">
    <div class="article-list" v-loading="loading">
      <!-- 文章列表 -->
      <div v-for="item in viewHistory" :key="item.article.id" class="article-item">
        <div class="article-main">
          <div class="article-content">
            <h3 class="article-title" @click="handleArticleClick(item.article.id)">
              {{ item.article.title }}
            </h3>
            <p class="article-summary">{{ item.article.summary || item.article.content.slice(0, 150) + '...' }}</p>
            <div class="article-meta">
              <span class="meta-item">
                <el-icon><User /></el-icon>
                {{ item.article.author.username }}
              </span>
              <span class="meta-item">
                <el-icon><View /></el-icon>
                {{ item.article.viewUserCount }}
              </span>
              <span class="meta-item">
                <el-icon><Star /></el-icon>
                {{ item.article.supportUserCount }}
              </span>
              <span class="meta-item">
                <el-icon><ChatLineRound /></el-icon>
                {{ item.article.commentCount }}
              </span>
              <span class="meta-item">
                <el-icon><Collection /></el-icon>
                {{ item.article.collectionUserCount }}
              </span>
              <span class="meta-item">
                <el-icon><Share /></el-icon>
                {{ item.article.shareUserCount }}
              </span>
              <span class="view-time">
                <el-icon><Clock /></el-icon>
                浏览于 {{ formatDateTime(item.viewTime) }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <el-empty 
        v-if="viewHistory.length === 0 && !loading" 
        description="暂无浏览记录"
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
    <div class="pagination" v-if="viewHistory.length > 0">
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

// 浏览历史数据
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

interface ViewHistoryItem {
  article: Article
  viewTime: string
}

const viewHistory = ref<ViewHistoryItem[]>([])

// 获取用户ID
const userId = computed(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  return userInfo.id
})

// 加载浏览历史
const loadViewHistory = async () => {
  if (!userId.value) {
    console.log('未找到用户ID，请先登录')
    return
  }

  console.log('开始加载浏览历史，用户ID:', userId.value)
  loading.value = true
  
  try {
    const response = await request.get<ViewHistoryItem[]>('/server/personalCenterData/getViewHistory', {
      params: { userId: userId.value }
    })
    
    console.log('获取浏览历史成功:', response.data)
    
    if (Array.isArray(response.data)) {
      viewHistory.value = response.data
      total.value = response.data.length
      console.log(`共加载 ${total.value} 条浏览记录`)
      
      if (total.value === 0) {
        console.log('暂无浏览记录')
      }
    } else {
      console.warn('API返回数据格式错误，期望数组类型:', response.data)
      viewHistory.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('加载浏览历史失败:', error)
    viewHistory.value = []
    total.value = 0
  } finally {
    loading.value = false
    console.log('浏览历史加载完成')
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
  loadViewHistory()
}

// 组件挂载时加载数据
onMounted(() => {
  loadViewHistory()
})
</script>

<style scoped>
.view-history-container {
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

.view-time {
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