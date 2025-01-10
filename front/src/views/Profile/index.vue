<template>
  <div class="profile-container">
    <!-- 个人信息头部 -->
    <div class="profile-header">
      <div class="header-main">
        <el-avatar :size="80" :src="userInfo.avatar" />
        <div class="user-info">
          <h2>{{ userInfo.name }}</h2>
          <p class="bio">{{ userInfo.bio || '这个人很懒，还没有写简介' }}</p>
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
          <el-button>编辑资料</el-button>
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
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { 
  Document, 
  Star, 
  Pointer,
  ChatDotRound, 
  CircleClose,
  Edit
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const currentTab = computed(() => route.path.split('/').pop())

// 标签页配置
const tabs = [
  { label: '文章', path: 'articles', icon: Document, count: 43 },
  { label: '收藏', path: 'favorites', icon: Star, count: 52 },
  { label: '点赞', path: 'likes', icon: Pointer, count: 128 },
  { label: '评论', path: 'comments', icon: ChatDotRound, count: 96 },
  { label: '反对', path: 'dislikes', icon: CircleClose, count: 15 }
]

// 模拟用户数据
const userInfo = ref({
  name: '张三',
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
  bio: '前端开发工程师，热爱技术分享',
  articles: 43,
  followers: 128,
  following: 56
})
</script>

<style scoped>
.profile-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.profile-header {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
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
  font-size: 26px;
  font-weight: 600;
}

.bio {
  color: #8590a6;
  margin: 0 0 16px;
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
  font-weight: 600;
  color: #121212;
}

.stat-label {
  font-size: 14px;
  color: #8590a6;
}

.content-tabs {
  display: flex;
  gap: 32px;
  padding: 0 24px;
  margin-bottom: 8px;
  background: #fff;
  border-radius: 8px 8px 0 0;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

.tab-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 16px 0;
  color: #8590a6;
  text-decoration: none;
  border-bottom: 3px solid transparent;
  transition: all 0.3s;
}

.tab-item:hover {
  color: #121212;
}

.tab-item.active {
  color: #056de8;
  border-bottom-color: #056de8;
}

.tab-count {
  font-size: 12px;
  color: #8590a6;
}

/* 路由过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.header-actions {
  display: flex;
  gap: 12px;
}
</style>
