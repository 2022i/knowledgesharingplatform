<template>
  <div class="search-container">
    <div class="search-header">
      <el-input
        v-model="searchQuery"
        placeholder="搜索文章、用户或标签"
        class="search-input"
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>

    <div class="search-filters">
      <el-radio-group v-model="searchType" @change="handleSearch">
        <el-radio-button label="articles">文章</el-radio-button>
        <el-radio-button label="users">用户</el-radio-button>
        <el-radio-button label="tags">标签</el-radio-button>
      </el-radio-group>
    </div>

    <div class="search-results">
      <!-- 文章搜索结果 -->
      <template v-if="searchType === 'articles'">
        <div v-for="article in results" :key="article.id" class="result-item">
          <h3 class="result-title">
            <router-link :to="`/article/${article.id}`">
              {{ article.title }}
            </router-link>
          </h3>
          <p class="result-excerpt">{{ article.excerpt }}</p>
          <div class="result-meta">
            <span>{{ article.author }}</span>
            <span>{{ article.publishTime }}</span>
            <span>{{ article.likes }} 点赞</span>
          </div>
        </div>
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

    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        @current-change="handlePageChange"
        layout="prev, pager, next"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const searchQuery = ref('')
const searchType = ref('articles')
const results = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

onMounted(() => {
  // 从 URL 参数中获取搜索条件
  const { q, type } = route.query
  if (q) searchQuery.value = q as string
  if (type) searchType.value = type as string
  
  handleSearch()
})

const handleSearch = async () => {
  // 更新 URL 参数
  router.push({
    query: {
      q: searchQuery.value,
      type: searchType.value
    }
  })

  // TODO: 实现搜索逻辑
  // const response = await searchContent({
  //   query: searchQuery.value,
  //   type: searchType.value,
  //   page: currentPage.value,
  //   pageSize: pageSize.value
  // })
  // results.value = response.data
  // total.value = response.total
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  handleSearch()
}

const handleTagClick = (tag: any) => {
  router.push(`/tag/${tag.id}`)
}
</script>

<style scoped>
.search-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.search-header {
  margin-bottom: 20px;
}

.search-input {
  width: 100%;
  max-width: 600px;
}

.search-filters {
  margin-bottom: 20px;
}

.result-item {
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.result-title {
  margin: 0 0 10px;
}

.result-excerpt {
  color: #666;
  margin-bottom: 10px;
}

.result-meta {
  color: #999;
  font-size: 14px;
  display: flex;
  gap: 16px;
}

.user-result-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.user-info {
  flex: 1;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  padding: 20px;
}

.tag-item {
  cursor: pointer;
  font-size: 14px;
}

.tag-count {
  margin-left: 6px;
  color: #999;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
