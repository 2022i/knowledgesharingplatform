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
          <el-radio-button value="tags">标签</el-radio-button>
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
            <h3>{{ user.name }}</h3>
            <p>{{ user.bio }}</p>
          </div>
          <el-button type="primary" size="small">关注</el-button>
        </div>
      </template>

      <!-- 标签搜索结果 -->
      <template v-if="searchType === 'tags'">
        <div class="tags-container">
          <el-tag
            v-for="tag in results"
            :key="tag.id"
            class="tag-item"
            @click="handleTagClick(tag)"
          >
            {{ tag.name }}
            <span class="tag-count">{{ tag.count }}</span>
          </el-tag>
        </div>
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

const route = useRoute()
const router = useRouter()

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

    const response = await axios.get(`${API_BASE_URL}${endpoint}`, {
      params: {
        keyword: searchQuery.value.trim(),
        page: currentPage.value,
        pageSize: pageSize.value
      },
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })

    // 在控制台显示搜索结果数据
    console.log('搜索关键词:', searchQuery.value.trim())
    console.log('当前页码:', currentPage.value)
    console.log('每页数量:', pageSize.value)
    console.log('排序方式:', sortBy.value)
    console.log('API返回的原始数据:', response.data)

    // 转换API返回的数据格式以匹配ArticleList组件的需求
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
      collect: article.collect === true,
      hasShared: article.hasShared === true,
      hasViewed: article.hasViewed === true
    }))
    
    total.value = response.data.length // 如果后端返回总数，应该使用 response.data.total
  } catch (error) {
    console.error('搜索失败:', error)
    ElMessage.error('搜索失败，请重试')
    results.value = []
    total.value = 0
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
  router.push(`/tag/${tag.id}`)
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
}

.user-info {
  flex: 1;
}

.user-info h3 {
  margin: 0 0 8px;
  font-size: 16px;
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
  padding: 20px;
  background: #fff;
  border-radius: 8px;
}

.tag-item {
  cursor: pointer;
  font-size: 14px;
  padding: 8px 16px;
  transition: all 0.3s;
}

.tag-item:hover {
  background: var(--el-color-primary-light-8);
}

.tag-count {
  margin-left: 6px;
  color: var(--el-text-color-secondary);
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
}
</style>
