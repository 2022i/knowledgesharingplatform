<template>
  <nav v-if="!route.meta.hideNav" class="nav-bar">
    <div class="nav-content">
      <!-- Logo区域 -->
      <div class="nav-left">
        <router-link to="/" class="logo-link">
          <img src="../assets/logo.svg" alt="Logo" class="nav-logo">
          <span class="site-name">知识分享平台</span>
        </router-link>

        <!-- 导航链接 -->
        <div class="nav-links">
          <router-link 
            to="/" 
            class="nav-item"
            :class="{ active: route.path === '/' }"
          >
            首页
          </router-link>
          <router-link 
            to="/category" 
            class="nav-item"
            :class="{ active: route.path === '/category' }"
          >
            分类
          </router-link>
        </div>
      </div>

      <!-- 右侧操作区 -->
      <div class="nav-right">
        <!-- 搜索框 -->
        <div class="nav-search">
          <el-input
            v-model="searchQuery"
            placeholder="搜索你感兴趣的内容..."
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>

        <!-- 用户头像 -->
        <div class="nav-user">
          <el-dropdown trigger="click" @command="handleCommand">
            <el-avatar :size="32" :src="userInfo?.avatar" />
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>个人主页
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Search, User, SwitchButton } from '@element-plus/icons-vue'
import { useAuth } from '../composables/useAuth'

const route = useRoute()
const router = useRouter()
const searchQuery = ref('')
const { userInfo, logout } = useAuth()


// 处理搜索
const handleSearch = () => {
  if (searchQuery.value.trim()) {
    router.push({
      path: '/search',
      query: { 
        q: searchQuery.value,
        type: 'articles',  // 默认搜索文章
        sort: 'default'    // 默认排序方式
      }
    })
    // 搜索后清空导航栏搜索框
    searchQuery.value = ''
  }
}

// 监听路由变化，同步搜索框状态
watch(
  () => route.path,
  (newPath) => {
    // 如果不是搜索页面，清空搜索框
    if (newPath !== '/search') {
      searchQuery.value = ''
    }
  }
)

// 处理下拉菜单命令
const handleCommand = async (command: string) => {
  console.log('Dropdown command:', command) // 添加调试日志
  
  switch (command) {
    case 'profile':
      router.push('/profile/articles')  // 修改为具体的子路由
      break
    case 'logout':
      await logout()
      router.push('/login')
      break
  }
}
</script>

<style scoped>
.nav-bar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 52px;  /* 稍微降低高度 */
  background-color: #fff;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
  z-index: 100;
}

.nav-content {
  max-width: 1200px;
  height: 100%;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 48px;  /* 增加Logo和导航链接的间距 */
}

.logo-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #056de8;
  gap: 8px;
}

.nav-logo {
  width: 26px;  /* 稍微缩小logo */
  height: 26px;
}

.site-name {
  font-size: 18px;
  font-weight: 600;
}

.nav-links {
  display: flex;
  gap: 24px;  /* 调整导航项间距 */
}

.nav-item {
  color: #8590a6;  /* 未选中时使用更浅的颜色 */
  text-decoration: none;
  font-size: 15px;
  padding: 0 8px;
  line-height: 52px;
  transition: all 0.3s;
  position: relative;
}

.nav-item:hover {
  color: #121212;
}

.nav-item.active {
  color: #121212;
  font-weight: 500;
}

.nav-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 2px;
  background-color: #056de8;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.nav-search {
  width: 320px;  /* 调整搜索框宽度 */
}

:deep(.el-input__wrapper) {
  background-color: #f6f8fa;
  border-radius: 16px;  /* 圆角搜索框 */
  padding: 0 16px;
}

:deep(.el-input__inner) {
  height: 32px;
  font-size: 14px;
}

:deep(.el-input__prefix) {
  color: #8590a6;
}

.nav-user {
  display: flex;
  align-items: center;
}

:deep(.el-dropdown) {
  display: flex;
  align-items: center;
}

:deep(.el-avatar) {
  border: 2px solid #fff;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
  transition: transform 0.2s;
  cursor: pointer;
}

:deep(.el-avatar:hover) {
  transform: scale(1.05);
}

:deep(.el-dropdown-menu) {
  margin-top: 8px;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  font-size: 14px;
}

:deep(.el-dropdown-menu__item:not(.is-disabled):hover) {
  background-color: #f6f8fa;
  color: #056de8;
}
</style> 