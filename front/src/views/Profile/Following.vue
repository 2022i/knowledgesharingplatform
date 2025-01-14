<template>
  <div class="following-container">
    <!-- 关注列表 -->
    <div class="following-list" v-loading="loading">
      <div v-for="user in followingUsers" :key="user.id" class="following-item">
        <div class="user-info" @click="handleUserClick(user.id)" style="cursor: pointer">
          <el-avatar :size="50">{{ user.username.charAt(0).toUpperCase() }}</el-avatar>
          <div class="user-details">
            <h3 class="username">{{ user.username }}</h3>
          </div>
        </div>
        <div class="actions">
          <el-button type="primary" @click="handleUnfollow(user.id)">取消关注</el-button>
        </div>
      </div>

      <!-- 空状态 -->
      <el-empty 
        v-if="followingUsers.length === 0 && !loading" 
        description="还没有关注任何人"
      >
        <template #description>
          <p>关注感兴趣的作者，及时获取他们的最新文章</p>
        </template>
        <el-button type="primary" @click="router.push('/home')">
          去首页发现更多作者
        </el-button>
      </el-empty>
    </div>

    <!-- 分页器 -->
    <div class="pagination" v-if="followingUsers.length > 0">
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
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const router = useRouter()

// 加载状态
const loading = ref(false)

// 定义 ArticleAuthor 接口
interface ArticleAuthor {
  id: number
  username: string
}

// 关注列表数据
const followingUsers = ref<ArticleAuthor[]>([])

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 获取用户ID
const userId = computed(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  return userInfo.id
})

// 加载关注列表
const loadFollowingUsers = async () => {
  if (!userId.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  loading.value = true
  try {
    const response = await request.get<ArticleAuthor[]>('/server/personalCenterData/getFollowUsers', {
      params: { 
        userId: userId.value 
      }
    })

    // 确保响应数据是数组
    if (Array.isArray(response.data)) {
      followingUsers.value = response.data
      total.value = response.data.length

      // 更新个人信息中的关注数
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      userInfo.following = total.value
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    } else {
      throw new Error('返回数据格式错误')
    }
  } catch (error) {
    followingUsers.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 取消关注
const handleUnfollow = async (followingId: number) => {
  try {
    await ElMessageBox.confirm(
      '确定要取消关注该用户吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await loadFollowingUsers()
    ElMessage.success('取消关注成功')
  } catch (error) {
    if (error === 'cancel') return
    console.error('Failed to unfollow user:', error)
    ElMessage.error('取消关注失败')
  }
}

// 处理分页变化
const handlePageChange = (page: number) => {
  currentPage.value = page
  loadFollowingUsers()
}

// 点击用户名或头像跳转到用户主页
const handleUserClick = (userId: number) => {
  router.push(`/user/${userId}`)
}

// 组件挂载时加载数据
onMounted(() => {
  loadFollowingUsers()
})
</script>

<style scoped>
.following-container {
  background: white;
  border-radius: 0 0 8px 8px;
  padding: 24px;
  min-height: 400px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.following-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
  transition: all 0.3s ease;
}

.following-item:hover {
  background-color: #f9f9f9;
}

.following-item:last-child {
  border-bottom: none;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.username {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin: 0;
}

.user-bio {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.actions {
  display: flex;
  gap: 12px;
}

.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

.following-list {
  position: relative;
  min-height: 200px;
}
</style> 