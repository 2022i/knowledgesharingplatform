<template>
  <div class="category-container">
    <!-- 分类标签列表 -->
    <div class="category-tabs">
      <div 
        v-for="category in categories" 
        :key="category.value"
        :class="['category-tab', { active: currentCategory === category.value }]"
        @click="handleCategoryChange(category.value)"
      >
        <el-icon><component :is="category.icon" /></el-icon>
        <span>{{ category.label }}</span>
        <span class="article-count">({{ category.count }})</span>
      </div>
    </div>

    <!-- 文章列表区域 -->
    <div class="article-container">
      <h2 class="category-title">
        {{ getCurrentCategoryLabel() }}
        <span class="category-desc">共{{ articleStore.total }}篇文章</span>
      </h2>
      
      <article-list 
        :articles="articleStore.currentPageArticles"
        v-loading="articleStore.loading"
        @comment-added="handleCommentAdded"
      />

      <!-- 分页器 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="articleStore.currentPage"
          :page-size="articleStore.pageSize"
          :total="articleStore.total"
          layout="prev, pager, next"
          @current-change="handlePageChange"
          background
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { 
  Monitor, 
  DataLine, 
  Connection, 
  Setting, 
  Promotion,
  Management,
  Briefcase,
  TrendCharts
} from '@element-plus/icons-vue'
import ArticleList from '../components/ArticleList.vue'
import { useArticleStore } from '../store/article'

const route = useRoute()
const router = useRouter()
const articleStore = useArticleStore()

// 分类列表
const categories = [
  { label: '前端开发', value: 'frontend', icon: 'Monitor', count: 328 },
  { label: '后端开发', value: 'backend', icon: 'DataLine', count: 256 },
  { label: '人工智能', value: 'ai', icon: 'Connection', count: 198 },
  { label: '运维部署', value: 'devops', icon: 'Setting', count: 167 },
  { label: '产品设计', value: 'design', icon: 'Promotion', count: 145 },
  { label: '项目管理', value: 'management', icon: 'Management', count: 134 },
  { label: '职场经验', value: 'career', icon: 'Briefcase', count: 123 },
  { label: '数据分析', value: 'data', icon: 'TrendCharts', count: 112 }
]

// 当前选中的分类
const currentCategory = ref(route.query.category as string || 'frontend')

// 处理分类切换
const handleCategoryChange = (category: string) => {
  currentCategory.value = category
  router.push({
    path: '/category',
    query: { category }
  })
  // 重置页码并加载新分类的文章
  articleStore.setCurrentPage(1)
  loadArticles()
}

// 获取当前分类的标签文本
const getCurrentCategoryLabel = () => {
  const category = categories.find(c => c.value === currentCategory.value)
  return category ? category.label : '全部文章'
}

// 处理分页变化
const handlePageChange = (page: number) => {
  articleStore.setCurrentPage(page)
  loadArticles()
  // 滚动到顶部
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

// 加载文章
const loadArticles = async () => {
  await articleStore.fetchArticles({
    category: currentCategory.value
  })
}

// 处理评论添加
const handleCommentAdded = ({ articleId, comment }: any) => {
  // TODO: 处理评论添加的逻辑
  console.log('Comment added:', articleId, comment)
}

// 监听路由参数变化
watch(
  () => route.query.category,
  (newCategory) => {
    if (newCategory && newCategory !== currentCategory.value) {
      currentCategory.value = newCategory as string
      loadArticles()
    }
  }
)

// 组件挂载时加载文章
onMounted(() => {
  loadArticles()
})
</script>

<style scoped>
.category-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.category-tabs {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 32px;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

.category-tab {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.category-tab:hover {
  background-color: #f6f8fa;
  color: #056de8;
}

.category-tab.active {
  background-color: #e8f3ff;
  color: #056de8;
}

.article-count {
  color: #8590a6;
  font-size: 13px;
  margin-left: 4px;
}

.category-title {
  font-size: 22px;
  font-weight: 600;
  color: #121212;
  margin: 0 0 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.category-desc {
  font-size: 14px;
  color: #8590a6;
  font-weight: normal;
  margin-left: 12px;
}

.article-container {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style> 