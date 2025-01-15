<template>
  <div class="following-container">
    <div class="following-list" v-loading="loading">
      <!-- 用户列表 -->
      <div v-for="user in followingUsers" :key="user.id" class="user-item">
        <div class="user-info">
          <el-avatar :size="50" :src="user.avatar">
            {{ user.username?.charAt(0).toUpperCase() }}
          </el-avatar>
          <div class="user-details">
            <router-link :to="`/user/${user.id}`" class="username">
              {{ user.username }}
            </router-link>
            <p class="bio">{{ user.bio || '这个用户很懒，还没有写简介' }}</p>
          </div>
        </div>
        <el-button
          type="success"
          size="small"
          @click="handleUnfollow(user)"
          :loading="user.loading"
        >
          已关注
        </el-button>
      </div>

      <!-- 空状态 -->
      <el-empty
        v-if="!loading && (!followingUsers || followingUsers.length === 0)"
        description="暂未关注任何用户"
      >
        <template #description>
          <p>去发现更多有趣的创作者吧</p>
        </template>
        <el-button type="primary" @click="$router.push('/search?type=users')">
          发现用户
        </el-button>
      </el-empty>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../utils/request'
import { useAuth } from '../composables/useAuth'

const router = useRouter()
const { userId } = useAuth()
const loading = ref(false)
const followingUsers = ref<any[]>([])

// 加载关注的用户列表
const loadFollowingUsers = async () => {
  if (!userId) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  loading.value = true
  try {
    const response = await request.get('/server/follow/getFollowedUsers', {
      params: { userId }
    })

    console.log('=== 加载关注用户列表 ===')
    console.log('响应数据:', response.data)

    if (Array.isArray(response.data)) {
      followingUsers.value = response.data.map(user => ({
        ...user,
        loading: false
      }))
    } else {
      throw new Error('返回数据格式错误')
    }
  } catch (error: any) {
    console.error('加载关注用户列表失败:', error)
    ElMessage.error('加载关注用户列表失败，请重试')
  } finally {
    loading.value = false
  }
}

// 处理取消关注
const handleUnfollow = async (user: any) => {
  if (!userId) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  try {
    user.loading = true
    console.log('=== 取消关注操作 ===')
    console.log('当前用户ID:', userId)
    console.log('目标用户:', user.username)
    console.log('目标用户ID:', user.id)

    // 构建请求参数
    const params = new URLSearchParams()
    params.append('currentUserId', userId.toString())
    params.append('followUserId', user.id.toString())

    const response = await request.put('/server/follow/unFollowUser', params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })

    console.log('响应数据:', response.data)

    if (response.data.code === 200) {
      ElMessage.success('已取消关注')
      // 从列表中移除该用户
      followingUsers.value = followingUsers.value.filter(u => u.id !== user.id)
    } else {
      throw new Error(response.data.msg || '取消关注失败')
    }
  } catch (error: any) {
    console.error('取消关注失败:', {
      error,
      message: error.message,
      response: error.response?.data,
      status: error.response?.status
    })
    
    // 处理特定错误
    if (error.response?.status === 403) {
      ElMessage.error('您没有权限执行此操作')
    } else if (error.response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录')
      router.push('/login')
    } else if (error.code === 'ECONNABORTED') {
      ElMessage.error('请求超时，请检查网络连接')
    } else if (error.message.includes('Network Error')) {
      ElMessage.error('网络错误，请检查网络连接')
    } else {
      ElMessage.error(error.response?.data?.msg || error.message || '取消关注失败，请重试')
    }
  } finally {
    user.loading = false
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadFollowingUsers()
})
</script>

<style scoped>
.following-container {
  max-width: 800px;
  margin: 24px auto;
  padding: 20px;
}

.following-list {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

.user-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  border-bottom: 1px solid var(--el-border-color-light);
  transition: all 0.3s ease;
}

.user-item:last-child {
  border-bottom: none;
}

.user-item:hover {
  background: var(--el-color-primary-light-9);
  transform: translateY(-2px);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
  flex: 1;
}

.user-details {
  flex: 1;
}

.username {
  font-size: 16px;
  font-weight: 500;
  color: var(--el-text-color-primary);
  text-decoration: none;
  margin-bottom: 4px;
  display: inline-block;
}

.username:hover {
  color: var(--el-color-primary);
}

.bio {
  margin: 0;
  font-size: 14px;
  color: var(--el-text-color-secondary);
  line-height: 1.5;
}

:deep(.el-empty) {
  padding: 40px 0;
}
</style> 