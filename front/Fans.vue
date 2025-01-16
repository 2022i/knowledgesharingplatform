<template>
  <div class="fans-container">
    <!-- 粉丝列表 -->
    <div class="fans-list" v-loading="loading">
      <div v-for="user in fansList" :key="user.id" class="fan-item">
        <div class="user-info" @click="handleUserClick(user.id)" style="cursor: pointer">
          <el-avatar :size="50">{{ user.username.charAt(0).toUpperCase() }}</el-avatar>
          <div class="user-details">
            <h3 class="username">{{ user.username }}</h3>
          </div>
        </div>
        <div class="actions">
          <el-button 
            :type="user.isFollowed ? 'primary' : 'default'" 
            @click="handleFollowToggle(user)"
            :loading="user.loading"
          >
            {{ user.isFollowed ? '已关注' : '关注' }}
          </el-button>
        </div>
      </div>

      <!-- 空状态 -->
      <el-empty 
        v-if="fansList.length === 0 && !loading" 
        description="暂时还没有粉丝"
      >
        <template #description>
          <p>分享更多优质内容，吸引更多粉丝关注</p>
        </template>
        <el-button type="primary" @click="router.push('/editor')">
          去写文章
        </el-button>
      </el-empty>
    </div>

    <!-- 分页器 -->
    <div class="pagination" v-if="fansList.length > 0">
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

// 定义用户接口
interface FanUser {
  id: number
  username: string
  isFollowed?: boolean
  loading?: boolean
}

// 粉丝列表数据
const fansList = ref<FanUser[]>([])

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 获取当前用户ID
const userId = computed(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  return userInfo.id
})

// 加载粉丝列表
const loadFansList = async () => {
  if (!userId.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  loading.value = true
  try {
    const response = await request.get<FanUser[]>('/server/personalCenterData/getFans', {
      params: { 
        userId: userId.value 
      }
    })

    // 确保响应数据是数组
    if (Array.isArray(response.data)) {
      // 为每个用户添加 isFollowed 和 loading 状态
      fansList.value = response.data.map(user => ({
        ...user,
        isFollowed: false, // 默认未关注，后续可以通过接口获取实际关注状态
        loading: false
      }))
      total.value = response.data.length

      // 更新个人信息中的粉丝数
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      userInfo.followers = total.value
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    } else {
      throw new Error('返回数据格式错误')
    }
  } catch (error) {
    console.error('加载粉丝列表失败:', error)
    ElMessage.error('加载粉丝列表失败')
    fansList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 处理关注/取消关注
const handleFollowToggle = async (user: FanUser) => {
  if (!userId.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  try {
    user.loading = true
    const action = user.isFollowed ? 'unFollowUser' : 'followUser'
    
    // 构建请求参数
    const params = new URLSearchParams()
    params.append('currentUserId', userId.value.toString())
    params.append('followUserId', user.id.toString())

    const response = await request.put(`/server/follow/${action}`, params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })

    if (response.data.code === 200) {
      user.isFollowed = !user.isFollowed
      ElMessage.success(user.isFollowed ? '关注成功' : '已取消关注')
    } else {
      throw new Error(response.data.msg || '操作失败')
    }
  } catch (error: any) {
    console.error('关注操作失败:', error)
    ElMessage.error(error.message || '操作失败，请重试')
  } finally {
    user.loading = false
  }
}

// 处理分页变化
const handlePageChange = (page: number) => {
  currentPage.value = page
  loadFansList()
}

// 点击用户名或头像跳转到用户主页
const handleUserClick = (userId: number) => {
  router.push(`/user/${userId}`)
}

// 组件挂载时加载数据
onMounted(() => {
  loadFansList()
})
</script>

<style scoped>
.fans-container {
  background: white;
  border-radius: 0 0 8px 8px;
  padding: 24px;
  min-height: 400px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.fan-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
  transition: all 0.3s ease;
}

.fan-item:hover {
  background-color: #f9f9f9;
}

.fan-item:last-child {
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

.actions {
  display: flex;
  gap: 12px;
}

.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

.fans-list {
  position: relative;
  min-height: 200px;
}
</style> 