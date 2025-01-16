<template>
  <div class="profile-container">
    <!-- 个人信息头部 -->
    <div class="profile-header">
      <div class="header-main">
        <el-avatar 
          :size="80" 
          :src="userInfo.avatar"
          :fallback="() => getAvatarText"
        >
          {{ getAvatarText }}
        </el-avatar>
        <div class="user-info">
          <h2>{{ userInfo.username }}</h2>
          <div class="user-stats">
            <div class="stat-item" @click="router.push('/profile/articles')">
              <div class="stat-value">{{ userInfo.articles }}</div>
              <div class="stat-label">文章</div>
            </div>
            <div class="stat-item" @click="router.push('/profile/fans')">
              <div class="stat-value">{{ userInfo.followers }}</div>
              <div class="stat-label">粉丝</div>
            </div>
            <div class="stat-item" @click="router.push('/profile/following')">
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
  const storedInfo = localStorage.getItem('userInfo')
  if (!storedInfo) {
    return {
      id: 0,
      username: '用户',
      avatar: '',
      bio: '这个人很懒，还没有写简介',
      articles: 0,
      followers: 0,
      following: 0
    }
  }
  
  const info = JSON.parse(storedInfo)
  return {
    id: info.id,
    username: info.username,  // 直接使用存储的用户名
    avatar: info.avatar || '',
    bio: info.bio || '这个人很懒，还没有写简介',
    articles: info.articles || 0,
    followers: info.followers || 0,
    following: info.following || 0
  }
})

// 获取用户头像显示文本
const getAvatarText = computed(() => {
  return userInfo.value.username ? userInfo.value.username.charAt(0).toUpperCase() : '用'
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
    label: '粉丝',
    path: 'fans',
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
  // 统一转换为小写以进行匹配
  const type = title.toLowerCase()
  const typeMap: Record<string, string> = {
    'follow': 'success',         // 关注
    'followuser': 'success',     // 关注用户
    'followtheme': 'success',    // 关注主题
    'unfollow': 'info',         // 取消关注
    'unfollowuser': 'info',     // 取消关注用户
    'unfollowtheme': 'info',    // 取消关注主题
    'support': 'success',
    'collect': 'warning',
    'share': 'warning',
    'articlecomment': 'info',
    'commentcomment': 'info',
    'deletearticlesuccess': 'success',
    'deletearticlefail': 'danger',
    'rejectarticle': 'danger',
    'oppose': 'danger',
    'approvearticle': 'success',
    'deleterequest': 'warning',
    'undodelete': 'info'
  }
  return typeMap[type] || 'info'
}

// 获取消息标签
const getMessageLabel = (title: string) => {
  // 统一转换为小写以进行匹配
  const type = title.toLowerCase()
  const labelMap: Record<string, string> = {
    'follow': '关注',
    'followuser': '关注用户',
    'followtheme': '关注主题',
    'unfollow': '取消关注',
    'unfollowuser': '取消关注用户',
    'unfollowtheme': '取消关注主题',
    'support': '点赞',
    'collect': '收藏',
    'share': '分享',
    'articlecomment': '文章评论',
    'commentcomment': '评论回复',
    'deletearticlesuccess': '删除成功',
    'deletearticlefail': '删除失败',
    'rejectarticle': '审核拒绝',
    'oppose': '反对',
    'approvearticle': '审核通过',
    'deleterequest': '删除申请',
    'undodelete': '撤销删除'
  }
  return labelMap[type] || title
}

// 获取消息内容
const getMessageContent = (message: Message) => {
  const { title, messageGeneratorName, articleTitle, content } = message
  // 统一转换为小写以进行匹配
  const type = title.toLowerCase()
  const contentMap: Record<string, string> = {
    'follow': `${messageGeneratorName} 关注了你`,
    'followuser': `${messageGeneratorName} 关注了你`,
    'followtheme': `${messageGeneratorName} 关注了主题《${content}》`,
    'unfollow': `${messageGeneratorName} 取消了对你的关注`,
    'unfollowuser': `${messageGeneratorName} 取消了对你的关注`,
    'unfollowtheme': `${messageGeneratorName} 取消了对主题《${content}》的关注`,
    'support': `${messageGeneratorName} 点赞了你的文章《${articleTitle}》`,
    'collect': `${messageGeneratorName} 收藏了你的文章《${articleTitle}》`,
    'share': `${messageGeneratorName} 分享了你的文章《${articleTitle}》`,
    'articlecomment': `${messageGeneratorName} 评论了你的文章《${articleTitle}》`,
    'commentcomment': `${messageGeneratorName} 回复了你的评论`,
    'deletearticlesuccess': `你的文章《${articleTitle}》已被删除`,
    'deletearticlefail': `你的文章《${articleTitle}》删除申请被拒绝`,
    'rejectarticle': content ? `你的文章《${articleTitle}》未通过审核，原因：${content}` : `你的文章《${articleTitle}》未通过审核`,
    'oppose': `${messageGeneratorName} 反对了你的文章《${articleTitle}》`,
    'approvearticle': `你的文章《${articleTitle}》已通过审核，现在可以被其他用户看到了`,
    'deleterequest': `${messageGeneratorName} 申请删除文章《${articleTitle}》`,
    'undodelete': `${messageGeneratorName} 撤销了删除文章《${articleTitle}》的申请`
  }
  return contentMap[type] || '未知消息'
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

// 获取用户统计数据
const loadUserStats = async () => {
  try {
    const userId = localStorage.getItem('userId')
    if (!userId) return

    const response = await request.get('/server/personalCenterData/getPersonalAchievements', {
      params: { userId }
    })

    if (response.data) {
      // 更新本地存储的用户信息
      const storedInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      const updatedInfo = {
        ...storedInfo,
        articles: response.data.writeArticleCount || 0,
        followers: response.data.fansCount || 0,
        following: response.data.followingCount || 0
      }
      localStorage.setItem('userInfo', JSON.stringify(updatedInfo))
    }
  } catch (error) {
    console.error('Failed to load user stats:', error)
  }
}

// 组件挂载时加载用户统计数据
onMounted(() => {
  loadUserStats()
})

// 组件挂载时加载消息
onMounted(() => {
  loadMessages()
})
</script>

<style scoped>
.profile-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.profile-header {
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
  align-items: center;
  gap: 48px;
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
  cursor: pointer;
}

.stat-item:hover {
  background-color: rgba(64, 158, 255, 0.1);
  transform: translateY(-1px);
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

.header-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.header-actions .el-button {
  padding: 12px 24px;
  font-size: 15px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-actions .el-icon {
  margin-right: 4px;
}

.profile-content {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.content-tabs {
  display: flex;
  gap: 32px;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
  overflow-x: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.content-tabs::-webkit-scrollbar {
  display: none;
}

.tab-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  color: #8590a6;
  text-decoration: none;
  font-size: 15px;
  border-radius: 8px;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.tab-item:hover {
  color: #1a1a1a;
  background-color: rgba(0, 0, 0, 0.02);
}

.tab-item.active {
  color: var(--el-color-primary);
  background-color: var(--el-color-primary-light-9);
  font-weight: 500;
}

.tab-item .el-icon {
  font-size: 16px;
}

.tab-count {
  font-size: 12px;
  background: rgba(0, 0, 0, 0.06);
  padding: 2px 6px;
  border-radius: 10px;
  margin-left: 4px;
}

/* 消息弹窗样式 */
.messages-list {
  max-height: 60vh;
  overflow-y: auto;
  padding-right: 16px;
}

.message-item {
  padding: 16px;
  border-radius: 8px;
  background: #f9f9f9;
  margin-bottom: 12px;
  transition: all 0.3s ease;
}

.message-item:hover {
  background: #f5f5f5;
  transform: translateY(-1px);
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.message-time {
  font-size: 13px;
  color: #8590a6;
}

.message-content {
  font-size: 14px;
  color: #1a1a1a;
  line-height: 1.6;
  margin: 8px 0;
}

.message-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
  font-size: 13px;
}

.message-from {
  color: #8590a6;
}

/* 动画效果 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .profile-container {
    padding: 16px;
  }

  .profile-header {
    padding: 20px;
  }

  .header-main {
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 20px;
  }

  .user-info {
    flex-direction: column;
    gap: 20px;
    align-items: center;
  }

  .user-info h2 {
    text-align: center;
  }

  .user-stats {
    justify-content: center;
  }

  .header-actions {
    width: 100%;
    justify-content: center;
  }

  .content-tabs {
    gap: 16px;
    padding-bottom: 12px;
  }

  .tab-item {
    padding: 6px 12px;
    font-size: 14px;
  }
}
</style>
