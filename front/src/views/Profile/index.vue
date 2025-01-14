<template>
  <div class="profile-container">
    <!-- 个人信息头部 -->
    <div class="profile-header">
      <div class="header-main">
        <el-avatar :size="80" :src="userInfo.avatar" />
        <div class="user-info">
          <h2>{{ userInfo.name }}</h2>
          <div class="user-stats">
            <div class="stat-item">
              <div class="stat-value">{{ userInfo.articles }}</div>
              <div class="stat-label">文章</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ userInfo.followers }}</div>
              <div class="stat-label">关注者</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ userInfo.following }}</div>
              <div class="stat-label">关注</div>
            </div>
          </div>
        </div>
        <div class="header-actions">
          <el-button @click="router.push('/editor')" type="primary">
            <el-icon><Edit /></el-icon>写文章
          </el-button>
          <el-badge :value="unreadCount" :hidden="unreadCount === 0">
            <el-button @click="showMessages" :loading="loading">
              <el-icon><Bell /></el-icon>消息通知
            </el-button>
          </el-badge>
        </div>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="profile-content">
      <div class="content-tabs">
        <router-link 
          v-for="tab in tabs" 
          :key="tab.name"
          :to="`/profile/${tab.path}`"
          class="tab-item"
          :class="{ active: currentTab === tab.path }"
        >
          <el-icon>
            <component :is="tab.icon" />
          </el-icon>
          {{ tab.label }}
          <span class="tab-count" v-if="tab.count">{{ tab.count }}</span>
        </router-link>
      </div>

      <!-- 路由视图 -->
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </div>

    <!-- 消息通知弹窗 -->
    <el-dialog
      v-model="messageDialogVisible"
      title="消息通知"
      width="600px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <div v-if="loading" class="loading-wrapper">
        <el-skeleton :rows="3" animated />
        <el-skeleton :rows="3" animated />
      </div>

      <template v-else>
        <div v-if="messages.length > 0" class="messages-list">
          <div v-for="message in messages" :key="message.id" class="message-item">
            <div class="message-header">
              <el-tag 
                :type="getMessageType(message.title)"
                size="small"
              >
                {{ getMessageLabel(message.title) }}
              </el-tag>
              <span class="message-time">{{ formatDate(message.createTime) }}</span>
            </div>

            <div class="message-content">
              <template v-if="message.content">
                {{ message.content }}
              </template>
              <template v-else>
                {{ getMessageContent(message) }}
              </template>
            </div>

            <div class="message-footer">
              <span class="message-from">
                来自：{{ message.messageGeneratorName }}
              </span>
              <el-button 
                v-if="message.articleId"
                type="primary" 
                link 
                @click="handleArticleClick(message.articleId)"
              >
                查看文章
              </el-button>
            </div>
          </div>
        </div>

        <el-empty 
          v-else 
          description="暂无消息通知"
        />
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  Edit, 
  Document, 
  Star, 
  ChatLineRound, 
  Collection, 
  UserFilled, 
  Medal,
  View,
  Bell
} from '@element-plus/icons-vue'
import request from '../../utils/request'

const router = useRouter()
const route = useRoute()

// 用户信息
const userInfo = computed(() => {
  const info = JSON.parse(localStorage.getItem('userInfo') || '{}')
  return {
    id: info.id,
    name: info.username,
    avatar: info.avatar || '',
    bio: info.bio || '这个人很懒，还没有写简介',
    articles: info.articles || 0,
    followers: info.followers || 0,
    following: info.following || 0
  }
})

// 标签页配置
const tabs = [
  {
    label: '文章',
    path: 'articles',
    icon: Document
  },
  {
    label: '评论',
    path: 'comments',
    icon: ChatLineRound
  },
  {
    label: '点赞',
    path: 'likes',
    icon: Star
  },
  {
    label: '收藏',
    path: 'favorites',
    icon: Collection
  },
  {
    label: '关注',
    path: 'following',
    icon: UserFilled
  },
  {
    label: '成就',
    path: 'achievements',
    icon: Medal
  },
  {
    label: '浏览记录',
    path: 'view-history',
    icon: View
  }
]

// 当前标签页
const currentTab = computed(() => {
  const path = route.path.split('/').pop()
  return path || 'articles'
})

// 消息相关
interface Message {
  id: number
  title: string
  createTime: string
  content: string | null
  messageGeneratorId: number
  messageGeneratorName: string
  messageRecipientId: number
  articleId: number
  articleTitle: string
  read: boolean
}

const messages = ref<Message[]>([])
const loading = ref(false)
const messageDialogVisible = ref(false)
const unreadCount = computed(() => messages.value.filter(msg => !msg.read).length)

// 显示消息弹窗
const showMessages = async () => {
  messageDialogVisible.value = true
  await loadMessages()
}

// 加载消息列表
const loadMessages = async () => {
  if (!userInfo.value.id) {
    console.log('未找到用户ID，请先登录')
    return
  }

  loading.value = true
  console.log('开始加载消息列表，用户ID:', userInfo.value.id)

  try {
    const response = await request.get('/server/getList/messages', {
      params: { userId: userInfo.value.id }
    })

    console.log('获取消息列表成功:', response.data)

    if (Array.isArray(response.data)) {
      messages.value = response.data
    } else {
      console.warn('API返回数据格式错误，期望数组类型:', response.data)
      messages.value = []
    }
  } catch (error) {
    console.error('加载消息列表失败:', error)
    ElMessage.error('加载消息列表失败')
  } finally {
    loading.value = false
    console.log('消息列表加载完成')
  }
}

// 获取消息类型
const getMessageType = (title: string) => {
  const typeMap: Record<string, string> = {
    'Follow': 'success',
    'Support': 'success',
    'Collect': 'warning',
    'Share': 'warning',
    'ArticleComment': 'info',
    'CommentComment': 'info',
    'DeleteArticleSuccess': 'success',
    'DeleteArticleFail': 'danger',
    'RejectArticle': 'danger',
    'Oppose': 'danger'
  }
  return typeMap[title] || 'info'
}

// 获取消息标签
const getMessageLabel = (title: string) => {
  const labelMap: Record<string, string> = {
    'Follow': '关注',
    'Support': '点赞',
    'Collect': '收藏',
    'Share': '分享',
    'ArticleComment': '文章评论',
    'CommentComment': '评论回复',
    'DeleteArticleSuccess': '删除成功',
    'DeleteArticleFail': '删除失败',
    'RejectArticle': '审核拒绝',
    'Oppose': '反对'
  }
  return labelMap[title] || title
}

// 获取消息内容
const getMessageContent = (message: Message) => {
  const { title, messageGeneratorName, articleTitle } = message
  const contentMap: Record<string, string> = {
    'Follow': `${messageGeneratorName} 关注了你`,
    'Support': `${messageGeneratorName} 点赞了你的文章《${articleTitle}》`,
    'Collect': `${messageGeneratorName} 收藏了你的文章《${articleTitle}》`,
    'Share': `${messageGeneratorName} 分享了你的文章《${articleTitle}》`,
    'ArticleComment': `${messageGeneratorName} 评论了你的文章《${articleTitle}》`,
    'CommentComment': `${messageGeneratorName} 回复了你的评论`,
    'DeleteArticleSuccess': `你的文章《${articleTitle}》已被删除`,
    'DeleteArticleFail': `你的文章《${articleTitle}》删除申请被拒绝`,
    'RejectArticle': `你的文章《${articleTitle}》未通过审核`,
    'Oppose': `${messageGeneratorName} 反对了你的文章《${articleTitle}》`
  }
  return contentMap[title] || '未知消息'
}

// 格式化日期
const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

// 处理文章点击
const handleArticleClick = (id: number) => {
  messageDialogVisible.value = false
  router.push(`/article/${id}`)
}

// 组件挂载时加载消息
onMounted(() => {
  loadMessages()
})
</script>

<style scoped>
.profile-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.profile-header {
  background: white;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.header-main {
  display: flex;
  gap: 24px;
  align-items: flex-start;
}

.user-info {
  flex: 1;
}

.user-info h2 {
  margin: 0 0 8px;
  font-size: 24px;
}

.user-stats {
  display: flex;
  gap: 40px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.content-tabs {
  background: white;
  border-radius: 8px 8px 0 0;
  display: flex;
  padding: 0 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.tab-item {
  padding: 16px 24px;
  color: #606266;
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 8px;
  border-bottom: 2px solid transparent;
}

.tab-item:hover {
  color: var(--el-color-primary);
}

.tab-item.active {
  color: var(--el-color-primary);
  border-bottom-color: var(--el-color-primary);
}

.tab-count {
  font-size: 12px;
  background: #f4f4f5;
  padding: 2px 6px;
  border-radius: 10px;
}

.loading-wrapper {
  padding: 20px 0;
}

.messages-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  max-height: 60vh;
  overflow-y: auto;
}

.message-item {
  padding: 16px;
  border-radius: 8px;
  background: #f8f9fa;
  transition: all 0.3s ease;
}

.message-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.message-time {
  color: #909399;
  font-size: 14px;
}

.message-content {
  color: #303133;
  margin-bottom: 12px;
  line-height: 1.6;
}

.message-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #909399;
  font-size: 14px;
}

.message-from {
  color: #606266;
}

:deep(.el-dialog__body) {
  padding-top: 20px;
}
</style>
