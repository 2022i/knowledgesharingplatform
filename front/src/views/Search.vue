<template>
  <div class="search-container">
    <!-- 搜索过滤器 -->
    <div class="search-filters">
      <div class="filter-group">
        <!-- 当前搜索信息 -->
        <div class="current-search" v-if="searchQuery">
          <span class="search-label">搜索：</span>
          <span class="search-keyword">"{{ searchQuery }}"</span>
        </div>

        <!-- 搜索类型选择 -->
        <el-radio-group v-model="searchType" @change="handleSearch" size="large">
          <el-radio-button value="articles">文章</el-radio-button>
          <el-radio-button value="users">用户</el-radio-button>
          <el-radio-button value="tags">分类</el-radio-button>
        </el-radio-group>

        <!-- 文章排序选项 -->
        <el-select 
          v-if="searchType === 'articles'" 
          v-model="sortBy" 
          @change="handleSearch"
          class="sort-select"
          size="large"
        >
          <el-option label="默认排序" value="default" />
          <el-option label="最多点赞" value="likes" />
          <el-option label="最新发布" value="time" />
        </el-select>
      </div>

      <!-- 搜索结果统计 -->
      <div v-if="searchQuery && !loading" class="search-stats">
        找到 {{ total }} 条相关结果
      </div>
    </div>

    <!-- 搜索结果区域 -->
    <div class="search-results" v-loading="loading">
      <!-- 文章搜索结果 -->
      <template v-if="searchType === 'articles'">
        <article-list 
          v-if="results.length > 0"
          :articles="results"
        />
        <el-empty 
          v-else-if="!loading && searchQuery" 
          description="未找到相关文章"
        >
          <template #description>
            <p>试试其他关键词，或者检查一下是否有错别字</p>
          </template>
        </el-empty>
      </template>

      <!-- 用户搜索结果 -->
      <template v-if="searchType === 'users'">
        <div v-for="user in results" :key="user.id" class="user-result-item">
          <el-avatar :size="50" :src="user.avatar" />
          <div class="user-info">
            <h3>{{ user.username }}</h3>
            <p>{{ user.bio }}</p>
          </div>
          <el-button
            :type="user.followed ? 'success' : 'primary'"
            size="small"
            @click="handleFollowUser(user)"
            :loading="user.loading"
          >
            {{ user.followed ? '已关注' : '关注' }}
          </el-button>
        </div>
        <el-empty 
          v-if="!loading && results.length === 0 && searchQuery" 
          description="未找到相关用户"
        >
          <template #description>
            <p>试试其他关键词，或者检查一下是否有错别字</p>
          </template>
        </el-empty>
      </template>

      <!-- 标签搜索结果 -->
      <template v-if="searchType === 'tags'">
        <div class="tags-container">
          <el-tag
            v-for="tag in results"
            :key="tag.id"
            :class="['tag-item', { subscribed: tag.subscribed }]"
            @click="handleTagClick(tag)"
          >
            {{ tag.name }}
            <el-button
              v-if="tag.subscribed"
              size="small"
              type="success"
              @click.stop="handleUnfollowTag(tag)"
              class="follow-button"
              :loading="tag.loading"
            >
              已关注
            </el-button>
            <el-button
              v-else
              size="small"
              @click.stop="handleFollowTag(tag)"
              class="follow-button"
              :loading="tag.loading"
            >
              关注
            </el-button>
          </el-tag>
        </div>
        <el-empty 
          v-if="!loading && results.length === 0 && searchQuery" 
          description="未找到相关分类"
        >
          <template #description>
            <p>试试其他关键词，或者检查一下是否有错别字</p>
          </template>
        </el-empty>
      </template>
    </div>

    <!-- 分页器 -->
    <div class="pagination-container" v-if="total > 0">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="sizes, prev, pager, next, total"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
        background
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import ArticleList from '../components/ArticleList.vue'
import axios from 'axios'
import { useAuth } from '../composables/useAuth'

const route = useRoute()
const router = useRouter()
const { userId } = useAuth()

// 搜索状态
const searchQuery = ref('')
const searchType = ref('articles')
const sortBy = ref('default')
const results = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)

// API 基础URL
const API_BASE_URL = 'http://127.0.0.1:8081/server'

// 获取搜索结果
const fetchSearchResults = async () => {
  if (!searchQuery.value.trim()) {
    results.value = []
    total.value = 0
    return
  }

  loading.value = true
  try {
    let endpoint = ''
    let params = {
      name: searchQuery.value.trim(),
      userId: userId
    }

    console.log('=== 开始搜索 ===')
    console.log('搜索类型:', searchType.value)
    console.log('搜索关键词:', searchQuery.value.trim())
    console.log('用户ID:', userId)

    switch (searchType.value) {
      case 'articles':
        switch (sortBy.value) {
          case 'likes':
            endpoint = '/articleSearch/orderedSupportCount'
            break
          case 'time':
            endpoint = '/articleSearch/orderedCreateTime'
            break
          default:
            endpoint = '/articleSearch/default'
        }
        params = {
          keyword: searchQuery.value.trim(),
          userId
        }
        break
      case 'users':
        endpoint = '/search/userData'
        break
      case 'tags':
        endpoint = '/search/theme'
        break
      default:
        throw new Error('未知的搜索类型')
    }

    console.log('=== 发送请求 ===')
    console.log('请求URL:', endpoint)
    console.log('请求参数:', params)
    console.log('完整请求URL:', `${API_BASE_URL}${endpoint}?${new URLSearchParams(params).toString()}`)

    const response = await axios.get(`${API_BASE_URL}${endpoint}`, {
      params,
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })

    console.log('=== 接收响应 ===')
    console.log('响应状态:', response.status)
    console.log('响应数据:', response.data)

    // 根据不同的搜索类型处理数据
    if (Array.isArray(response.data)) {
      switch (searchType.value) {
        case 'articles':
          results.value = response.data.map((article: any) => ({
            id: article.id,
            title: article.title,
            theme: article.theme || '未分类',
            content: article.content,
            summary: article.summary,
            relatedKnowledge: article.relatedKnowledge || [],
            createTime: article.createTime,
            viewUserCount: article.viewUserCount || 0,
            supportUserCount: article.supportUserCount || 0,
            opposeUserCount: article.opposeUserCount || 0,
            shareUserCount: article.shareUserCount || 0,
            collectionUserCount: article.collectionUserCount || 0,
            commentCount: article.commentCount || 0,
            author: {
              id: article.author?.id || 0,
              username: article.author?.username || '未知用户'
            },
            support: article.support === true,
            oppose: article.oppose === true,
            collect: article.collect === true
          }))
          break
        case 'users':
          results.value = response.data.map((user: any) => ({
            id: user.id,
            username: user.username,
            followed: user.followed,
            avatar: user.avatar || '', // 添加默认头像
            bio: '暂无简介' // 添加默认简介
          }))
          break
        case 'tags':
          results.value = response.data.map((theme: any) => ({
            id: theme.id,
            name: theme.name,
            subscribed: theme.subscribed,
            loading: false,
            count: theme.count || 0
          }))
          break
      }
      total.value = response.data.length
      console.log('=== 数据处理完成 ===')
      console.log('处理后的数据:', results.value)
      console.log('数据条数:', total.value)
    } else {
      console.error('=== 数据格式错误 ===')
      console.error('预期格式: Array')
      console.error('实际数据:', response.data)
      results.value = []
      total.value = 0
      ElMessage.warning('暂无搜索结果')
    }
  } catch (error: any) {
    console.error('=== 搜索错误 ===')
    console.error('错误类型:', error.constructor.name)
    console.error('错误消息:', error.message)
    if (error.response) {
      console.error('响应状态:', error.response.status)
      console.error('响应数据:', error.response.data)
    }
    results.value = []
    total.value = 0
    ElMessage.error('搜索失败，请重试')
  } finally {
    loading.value = false
  }
}

// 处理搜索
const handleSearch = async () => {
  currentPage.value = 1
  // 更新 URL 参数
  router.push({
    query: {
      q: searchQuery.value,
      type: searchType.value,
      sort: sortBy.value
    }
  })

  await fetchSearchResults()
}

// 处理分页变化
const handlePageChange = (page: number) => {
  currentPage.value = page
  handleSearch()
}

// 处理每页数量变化
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  handleSearch()
}

// 处理标签点击
const handleTagClick = (tag: any) => {
  router.push({
    path: '/category',
    query: { category: tag.id.toString() }
  })
}

// 监听路由参数变化
watch(
  () => route.query,
  (newQuery) => {
    const { q, type, sort } = newQuery
    if (q !== undefined) searchQuery.value = q as string
    if (type) searchType.value = type as string
    if (sort) sortBy.value = sort as string
    
    if (q !== undefined) {
      handleSearch()
    }
  }
)

// 组件挂载时初始化
onMounted(() => {
  const { q, type, sort } = route.query
  if (q) searchQuery.value = q as string
  if (type) searchType.value = type as string
  if (sort) sortBy.value = sort as string
  
  if (searchQuery.value) {
    handleSearch()
  }
})

// 处理用户关注
const handleFollowUser = async (user: any) => {
  if (!userId) {
    ElMessage.warning('请先登录');
    return;
  }

  try {
    user.loading = true;
    const isFollowing = user.followed;
    const endpoint = isFollowing ? '/follow/unFollowUser' : '/follow/followUser';
    
    // 构建请求参数
    const params = {
      currentUserId: userId,
      followUserId: user.id
    };
    
    console.log('=== 用户关注操作 ===');
    console.log('操作类型:', isFollowing ? '取消关注' : '关注');
    console.log('当前用户ID:', userId);
    console.log('目标用户ID:', user.id);
    console.log('目标用户:', user.username);
    console.log('请求路径:', endpoint);
    console.log('请求参数:', params);
    console.log('完整请求URL:', `${API_BASE_URL}${endpoint}?currentUserId=${userId}&followUserId=${user.id}`);

    const response = await axios.put(`${API_BASE_URL}${endpoint}`, null, {
      params: params,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    console.log('响应数据:', response.data);

    if (response.data.code === 200) {
      user.followed = !isFollowing;
      ElMessage.success(isFollowing ? '已取消关注' : '关注成功');
    } else {
      throw new Error(response.data.msg || '操作失败');
    }
  } catch (error: any) {
    console.error('关注操作失败:', {
      error,
      message: error.message,
      response: error.response?.data,
      status: error.response?.status
    });

    // 处理特定错误
    if (error.response?.status === 403) {
      ElMessage.error('您没有权限执行此操作，请确保已登录并刷新页面');
    } else if (error.response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录');
      router.push('/login');
    } else if (error.code === 'ECONNABORTED') {
      ElMessage.error('请求超时，请检查网络连接');
    } else if (error.message.includes('Network Error')) {
      ElMessage.error('网络错误，请检查网络连接');
    } else {
      ElMessage.error(error.response?.data?.msg || error.message || '操作失败，请重试');
    }
  } finally {
    user.loading = false;
  }
}

// 处理分类关注
const handleFollowTag = async (tag: any) => {
  if (!userId) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    tag.loading = true
    const response = await axios.put(`${API_BASE_URL}/follow/followTheme`, null, {
      params: {
        userId,
        themeId: tag.id
      },
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })

    if (response.data.code === 200) {
      tag.subscribed = true
      ElMessage.success('关注成功')
    } else {
      throw new Error(response.data.msg || '关注失败')
    }
  } catch (error: any) {
    console.error('关注分类失败:', error)
    ElMessage.error(error.message || '关注失败，请重试')
  } finally {
    tag.loading = false
  }
}

// 处理分类取消关注
const handleUnfollowTag = async (tag: any) => {
  if (!userId) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    tag.loading = true
    const response = await axios.put(`${API_BASE_URL}/follow/unFollowTheme`, null, {
      params: {
        userId,
        themeId: tag.id
      },
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })

    if (response.data.code === 200) {
      tag.subscribed = false
      ElMessage.success('已取消关注')
    } else {
      throw new Error(response.data.msg || '取消关注失败')
    }
  } catch (error: any) {
    console.error('取消关注分类失败:', error)
    ElMessage.error(error.message || '取消关注失败，请重试')
  } finally {
    tag.loading = false
  }
}
</script>

<style scoped>
.search-container {
  max-width: 1200px;
  margin: 24px auto 0;  /* 将顶部边距从72px减少到24px */
  padding: 24px;
}

.current-search {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
}

.search-label {
  color: var(--el-text-color-secondary);
}

.search-keyword {
  color: var(--el-color-primary);
  font-weight: 500;
}

.search-filters {
  margin-bottom: 20px;  /* 稍微减少底部边距 */
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
  background: #fff;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.sort-select {
  width: 120px;
}

.search-stats {
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.search-results {
  min-height: 200px;
  margin-bottom: 24px;
}

.user-result-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  border-bottom: 1px solid var(--el-border-color-light);
  background: #fff;
  border-radius: 8px;
  margin-bottom: 12px;
  transition: all 0.3s ease;
}

.user-result-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.user-info {
  flex: 1;
}

.user-info h3 {
  margin: 0 0 8px;
  font-size: 16px;
  color: var(--el-text-color-primary);
}

.user-info p {
  margin: 0;
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  padding: 24px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

.tag-item {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 14px;
  padding: 12px 20px;
  transition: all 0.3s ease;
  border: 1px solid var(--el-border-color-light);
  border-radius: 6px;
  background-color: var(--el-bg-color);
  color: var(--el-text-color-primary);
  min-width: 120px;
  justify-content: space-between;
}

.tag-item:hover {
  background: var(--el-color-primary-light-9);
  border-color: var(--el-color-primary-light-5);
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.tag-item.subscribed {
  background-color: var(--el-color-success-light-9);
  border-color: var(--el-color-success-light-5);
  color: var(--el-color-success);
}

.tag-item.subscribed:hover {
  background-color: var(--el-color-success-light-8);
  border-color: var(--el-color-success);
}

.follow-button {
  padding: 4px 12px;
  height: 28px;
  font-size: 12px;
  border-radius: 4px;
}

.pagination-container {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .search-container {
    padding: 16px;
    margin-top: 16px;  /* 移动端减少顶部边距 */
  }

  .search-filters {
    flex-direction: column;
    align-items: flex-start;
    padding: 12px;
  }

  .filter-group {
    width: 100%;
    justify-content: space-between;
  }

  .sort-select {
    width: 100px;
  }

  .current-search {
    font-size: 14px;
  }

  .user-result-item {
    padding: 16px;
    flex-wrap: wrap;
  }

  .user-info {
    width: 100%;
    margin-bottom: 12px;
  }

  .tags-container {
    padding: 16px;
    gap: 8px;
  }

  .tag-item {
    width: calc(100% - 16px);
    padding: 10px 16px;
    min-width: unset;
  }
}
</style>
