<template>
  <div class="comments-container">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-wrapper">
      <el-skeleton :rows="3" animated />
      <el-skeleton :rows="3" animated />
      <el-skeleton :rows="3" animated />
    </div>

    <!-- 评论列表 -->
    <template v-else>
      <div v-if="commentedArticles.length > 0">
        <div v-for="item in commentedArticles" :key="item.article.id" class="comment-item">
          <!-- 文章信息 -->
          <div class="article-info" @click="handleArticleClick(item.article.id)">
            <h3 class="article-title">
              {{ item.article.title }}
            </h3>
            <p class="article-summary">
              {{ item.article.summary || '暂无简介' }}
            </p>
            <div class="article-meta">
              <span class="meta-item">
                <el-icon><User /></el-icon>
                {{ item.article.author.username }}
              </span>
              <span class="meta-item">
                <el-icon><Clock /></el-icon>
                {{ formatDate(item.article.createTime) }}
              </span>
              <span class="meta-item">
                <el-icon><View /></el-icon>
                {{ item.article.viewUserCount || 0 }} 浏览
              </span>
              <span class="meta-item">
                <el-icon><ChatDotRound /></el-icon>
                {{ item.article.commentCount || 0 }} 评论
              </span>
            </div>
          </div>

          <!-- 评论列表 -->
          <div class="comments-list">
            <div v-for="(comment, index) in item.comments" :key="index" class="comment-content">
              <el-icon color="#409EFF"><ChatLineSquare /></el-icon>
              <span>{{ comment }}</span>
            </div>
          </div>
        </div>

        <!-- 分页器 -->
        <div class="pagination">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="10"
            :total="total"
            layout="prev, pager, next"
            @current-change="handlePageChange"
            background
          />
        </div>
      </div>

      <!-- 空状态 -->
      <el-empty 
        v-else 
        description="暂无评论记录"
      >
        <el-button type="primary" @click="$router.push('/')">
          去浏览文章
        </el-button>
      </el-empty>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Clock, View, ChatDotRound, User, ChatLineSquare } from '@element-plus/icons-vue'
import request from '../../utils/request'

const router = useRouter()

// 评论文章列表
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
  support: boolean
  collect: boolean
}

interface CommentedArticle {
  article: Article
  comments: string[]
}

const commentedArticles = ref<CommentedArticle[]>([])
const loading = ref(false)
const currentPage = ref(1)
const total = computed(() => commentedArticles.value.length)

// 获取用户ID
const userId = computed(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  return userInfo.id
})

// 加载评论列表
const loadComments = async () => {
  if (!userId.value) {
    console.log('未找到用户ID，请先登录')
    return
  }

  loading.value = true
  console.log('开始加载评论列表，用户ID:', userId.value)

  try {
    const response = await request.get('/server/personalCenterData/getCommentArticles', {
      params: { userId: userId.value }
    })

    console.log('获取评论列表成功:', response.data)

    if (Array.isArray(response.data)) {
      commentedArticles.value = response.data
    } else {
      console.warn('API返回数据格式错误，期望数组类型:', response.data)
      commentedArticles.value = []
    }
  } catch (error) {
    console.error('加载评论列表失败:', error)
    ElMessage.error('加载评论列表失败')
  } finally {
    loading.value = false
    console.log('评论列表加载完成')
  }
}

// 格式化日期
const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

// 处理文章点击
const handleArticleClick = (id: number) => {
  router.push(`/article/${id}`)
}

// 处理分页变化
const handlePageChange = (page: number) => {
  currentPage.value = page
  loadComments()
}

// 组件挂载时加载数据
onMounted(() => {
  loadComments()
})
</script>

<style scoped>
.comments-container {
  background: white;
  border-radius: 8px;
  padding: 24px;
  min-height: 400px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.loading-wrapper {
  padding: 20px 0;
}

.comment-item {
  padding: 24px;
  border-radius: 8px;
  background: #f8f9fa;
  margin-bottom: 16px;
  transition: all 0.3s ease;
}

.comment-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.article-info {
  cursor: pointer;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.article-title {
  margin: 0 0 12px;
  font-size: 18px;
  color: #303133;
}

.article-info:hover .article-title {
  color: var(--el-color-primary);
}

.article-summary {
  color: #606266;
  margin: 0 0 12px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.article-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  color: #909399;
  font-size: 14px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.meta-item .el-icon {
  font-size: 16px;
}

.comments-list {
  padding-top: 16px;
}

.comment-content {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  padding: 8px 0;
  color: #606266;
  font-size: 14px;
  line-height: 1.6;
}

.comment-content .el-icon {
  margin-top: 3px;
}

.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style>
