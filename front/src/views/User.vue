<template>
  <div class="user-container">
    <div class="user-header">
      <el-skeleton v-if="loading" :rows="3" animated />
      
      <template v-else>
        <div class="header-main">
          <el-avatar 
            :size="80" 
            :fallback="() => achievements.userName?.charAt(0) || '用'"
          >
            {{ achievements.userName?.charAt(0) || '用' }}
          </el-avatar>
          
          <div class="user-info">
            <div class="user-info-header">
              <h2>{{ achievements.userName }}</h2>
              <el-button
                v-if="showFollowButton"
                :type="isFollowed ? 'default' : 'primary'"
                :loading="followLoading"
                @click="handleFollowToggle"
              >
                {{ isFollowed ? '已关注' : '关注' }}
              </el-button>
            </div>
            <div class="user-stats">
              <div class="stat-item">
                <div class="stat-value">{{ achievements.writeArticleCount }}</div>
                <div class="stat-label">文章</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ achievements.fansCount }}</div>
                <div class="stat-label">粉丝</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ achievements.supportedCount }}</div>
                <div class="stat-label">获赞</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ achievements.collectedCount }}</div>
                <div class="stat-label">被收藏</div>
              </div>
            </div>
          </div>
        </div>
      </template>
    </div>

    <!-- 文章列表 -->
    <div class="articles-section">
      <h3 class="section-title">发布的文章</h3>
      <div v-if="articlesLoading" class="loading-wrapper">
        <el-skeleton :rows="3" animated />
      </div>
      <template v-else>
        <div v-if="articles.length > 0" class="articles-list">
          <div v-for="article in articles" :key="article.id" class="article-item" @click="router.push(`/article/${article.id}`)">
            <div class="article-main">
              <h4 class="article-title">{{ article.title }}</h4>
              <p class="article-summary">{{ article.summary }}</p>
              <div class="article-meta">
                <span class="meta-item">
                  <el-icon><Calendar /></el-icon>
                  {{ formatDate(article.createTime) }}
                </span>
                <span class="meta-item">
                  <el-icon><View /></el-icon>
                  {{ article.viewCount || 0 }}
                </span>
                <span class="meta-item">
                  <el-icon><Star /></el-icon>
                  {{ article.supportCount || 0 }}
                </span>
                <span class="meta-item">
                  <el-icon><ChatLineRound /></el-icon>
                  {{ article.commentCount || 0 }}
                </span>
              </div>
            </div>
          </div>
        </div>
        <el-empty v-else description="暂无文章" />
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Calendar, View, Star, ChatLineRound } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

// 用户成就数据接口
interface UserAchievements {
  userName: string
  writeArticleCount: number
  supportedCount: number
  sharedCount: number
  fansCount: number
  haveMessage: boolean
  opposedCount: number
  collectedCount: number
}

// 文章接口
interface Article {
  id: number
  title: string
  summary: string
  createTime: string
  viewCount: number
  supportCount: number
  commentCount: number
}

const route = useRoute()
const router = useRouter()

// 计算属性
const currentUserId = computed(() => localStorage.getItem('userId'))
const showFollowButton = computed(() => {
  return currentUserId.value && currentUserId.value !== route.params.id
})

const achievements = ref<UserAchievements>({
  userName: '',
  writeArticleCount: 0,
  supportedCount: 0,
  sharedCount: 0,
  fansCount: 0,
  haveMessage: false,
  opposedCount: 0,
  collectedCount: 0
})

const loading = ref(true)
const articlesLoading = ref(true)
const articles = ref<Article[]>([])
const isFollowed = ref(false)
const followLoading = ref(false)

// 检查用户ID并在需要时重定向
const checkUserId = () => {
  if (currentUserId.value && route.params.id && currentUserId.value === route.params.id) {
    console.log('访问的是当前用户的主页，重定向到个人中心')
    router.replace('/profile/articles')
    return false
  }
  return true
}

// 加载用户成就数据
const loadUserAchievements = async () => {
  const userId = route.params.id
  if (!userId) return

  loading.value = true
  try {
    const response = await request.get('/server/personalCenterData/getPersonalAchievements', {
      params: { userId }
    })
    
    if (response.data) {
      achievements.value = response.data
      console.log('User achievements loaded:', response.data)
    }
  } catch (error) {
    console.error('Failed to load user achievements:', error)
  } finally {
    loading.value = false
  }
}

// 加载用户文章列表
const loadArticles = async () => {
  const userId = route.params.id
  if (!userId) return

  articlesLoading.value = true
  try {
    const response = await request.get('/server/personalCenterData/getPublishedArticles', {
      params: { userId }
    })
    
    if (Array.isArray(response.data)) {
      articles.value = response.data
      console.log('User articles loaded:', response.data)
    }
  } catch (error) {
    console.error('Failed to load articles:', error)
  } finally {
    articlesLoading.value = false
  }
}

// 格式化日期
const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

// 检查关注状态
const checkFollowStatus = async () => {
  const targetUserId = route.params.id
  
  if (!currentUserId.value || !targetUserId || currentUserId.value === targetUserId) return
  
  try {
    const response = await request.get('/server/isFollow', {
      params: {
        currentUserId: currentUserId.value,
        followUserId: targetUserId
      }
    })
    isFollowed.value = response.data === true
    console.log('Follow status:', isFollowed.value)
  } catch (error) {
    console.error('Failed to check follow status:', error)
    isFollowed.value = false
  }
}

// 处理关注/取消关注
const handleFollowToggle = async () => {
  if (!currentUserId.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  followLoading.value = true
  try {
    const action = isFollowed.value ? 'unFollowUser' : 'followUser'
    const params = new URLSearchParams()
    params.append('currentUserId', currentUserId.value)
    params.append('followUserId', route.params.id as string)

    const response = await request.put(`/server/follow/${action}`, params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })

    if (response.data.code === 200) {
      isFollowed.value = !isFollowed.value
      ElMessage.success(isFollowed.value ? '关注成功' : '已取消关注')
      // 重新加载成就数据以更新粉丝数
      loadUserAchievements()
    } else {
      throw new Error(response.data.msg || '操作失败')
    }
  } catch (error: any) {
    console.error('关注操作失败:', error)
    ElMessage.error(error.message || '操作失败，请重试')
  } finally {
    followLoading.value = false
  }
}

// 监听路由参数变化，重新加载数据
watch(() => route.params.id, () => {
  if (checkUserId()) {
    Promise.all([
      loadUserAchievements(),
      loadArticles(),
      checkFollowStatus()
    ])
  }
}, { immediate: true })
</script>

<style scoped>
.user-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.user-header {
  background: white;
  border-radius: 12px;
  padding: 32px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.header-main {
  display: flex;
  align-items: flex-start;
  gap: 32px;
}

.user-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.user-info-header {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-info h2 {
  margin: 0;
  font-size: 28px;
  font-weight: 600;
  color: #1a1a1a;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-info-header .el-button {
  padding: 8px 24px;
  font-size: 14px;
}

.user-stats {
  display: flex;
  gap: 48px;
  margin: 0;
}

.stat-item {
  text-align: center;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
  line-height: 1.2;
}

.stat-label {
  font-size: 14px;
  color: #8590a6;
  margin-top: 4px;
}

/* 文章列表样式 */
.articles-section {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.section-title {
  margin: 0 0 20px;
  font-size: 18px;
  font-weight: 600;
  color: #1a1a1a;
}

.articles-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.article-item {
  padding: 20px;
  border-radius: 8px;
  background: #f9f9f9;
  cursor: pointer;
  transition: all 0.3s ease;
}

.article-item:hover {
  background: #f5f5f5;
  transform: translateY(-1px);
}

.article-title {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
  line-height: 1.4;
}

.article-summary {
  margin: 0 0 12px;
  font-size: 14px;
  color: #8590a6;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.article-meta {
  display: flex;
  gap: 16px;
  color: #8590a6;
  font-size: 13px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.meta-item .el-icon {
  font-size: 14px;
}

.loading-wrapper {
  padding: 20px;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .user-container {
    padding: 16px;
  }

  .user-header {
    padding: 20px;
  }

  .header-main {
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 20px;
  }

  .user-info {
    gap: 20px;
    align-items: center;
  }

  .user-info-header {
    flex-direction: column;
    gap: 12px;
  }

  .user-info h2 {
    text-align: center;
  }

  .user-stats {
    justify-content: center;
    flex-wrap: wrap;
    gap: 24px;
  }

  .articles-section {
    padding: 16px;
  }

  .article-item {
    padding: 16px;
  }

  .article-meta {
    flex-wrap: wrap;
    gap: 12px;
  }
}
</style> 