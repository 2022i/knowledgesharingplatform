<template>
  <div class="admin-layout">
    <!-- 侧边栏 -->
    <div class="sidebar">
      <div class="logo">
        <h2>知识分享平台</h2>
        <p class="subtitle">管理后台</p>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        @select="handleSelect"
        router
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataLine /></el-icon>
          <span>数据概览</span>
        </el-menu-item>
        <el-menu-item index="/admin/articles">
          <el-icon><Document /></el-icon>
          <span>文章管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/comments">
          <el-icon><ChatDotRound /></el-icon>
          <span>评论管理</span>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 顶部导航栏 -->
      <div class="navbar">
        <h2 class="page-title">{{ currentRoute }}</h2>
        <div class="user-info">
          <el-dropdown @command="handleCommand">
            <span class="user-dropdown">
              <el-avatar :size="32" :src="userAvatar" />
              <span class="username">{{ userName }}</span>
              <el-icon class="dropdown-icon"><CaretBottom /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <!-- 路由视图 -->
      <div class="content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { 
  DataLine,
  Document,
  User,
  ChatDotRound,
  Setting,
  CaretBottom
} from '@element-plus/icons-vue'
import { useAuth } from '../../composables/useAuth'

const router = useRouter()
const route = useRoute()
const { userInfo, logout, getCurrentUser } = useAuth()

// 当前激活的菜单项
const activeMenu = computed(() => route.path)

// 当前路由名称
const currentRoute = computed(() => {
  const routeMap: Record<string, string> = {
    '/admin/dashboard': '数据概览',
    '/admin/articles': '文章管理',
    '/admin/users': '用户管理',
    '/admin/comments': '评论管理',
    '/admin/settings': '系统设置'
  }
  return routeMap[route.path] || '未知页面'
})

// 用户信息
const userAvatar = computed(() => userInfo.value?.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png')
const userName = computed(() => userInfo.value?.name || '管理员')

// 处理菜单选择
const handleSelect = (index: string) => {
  router.push(index)
}

// 处理下拉菜单命令
const handleCommand = async (command: string) => {
  if (command === 'logout') {
    await logout()
  }
}

// 初始化获取用户信息
onMounted(async () => {
  await getCurrentUser()
})
</script>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.sidebar {
  position: fixed;
  width: 240px;
  height: 100vh;
  background-color: #fff;
  box-shadow: 2px 0 8px 0 rgba(29, 35, 41, 0.05);
  display: flex;
  flex-direction: column;
  z-index: 1000;
}

.logo {
  height: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #f0f0f0;
  padding: 0 20px;
  background-color: #fff;
}

.logo h2 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
}

.logo .subtitle {
  margin: 2px 0 0;
  font-size: 12px;
  color: #666;
}

.sidebar-menu {
  flex: 1;
  border-right: none;
  padding: 8px 0;
}

.main-content {
  flex: 1;
  margin-left: 240px;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

.navbar {
  position: fixed;
  top: 0;
  right: 0;
  left: 240px;
  height: 60px;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  z-index: 999;
}

.page-title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 0 12px;
  height: 40px;
  border-radius: 4px;
  transition: all 0.3s;
}

.user-dropdown:hover {
  background-color: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #1a1a1a;
}

.dropdown-icon {
  color: #909399;
  font-size: 12px;
  margin-left: 4px;
}

.content {
  flex: 1;
  padding: 80px 24px 24px;
  overflow-y: auto;
}

/* 路由过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

:deep(.el-menu) {
  border-right: none;
}

:deep(.el-menu-item) {
  height: 48px;
  line-height: 48px;
  margin: 4px 12px;
  border-radius: 4px;
  color: #666;
}

:deep(.el-menu-item.is-active) {
  background-color: #e6f7ff;
  color: #1890ff;
  font-weight: 500;
}

:deep(.el-menu-item:hover) {
  color: #1890ff !important;
  background-color: #f0f7ff !important;
}

:deep(.el-menu-item .el-icon) {
  margin-right: 12px;
  font-size: 18px;
}

:deep(.el-avatar) {
  border: 2px solid #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style> 