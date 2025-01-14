<template>
  <div class="category-container">
    <!-- 分类标签列表 -->
    <div class="category-tabs" v-loading="themeStore.loading">
      <div 
        v-for="theme in themeStore.getThemesWithCount" 
        :key="theme.theme.id"
        :class="[
          'category-tab', 
          { 
            active: currentCategory === theme.theme.id.toString(),
            subscribed: theme.theme.subscribed
          }
        ]"
      >
        <div class="category-content" @click="handleCategoryChange(theme.theme.id.toString())">
          <el-icon><component :is="getCategoryIcon(theme.theme.name)" /></el-icon>
          <span>{{ theme.theme.name }}</span>
          <span class="article-count">({{ theme.articleCount }})</span>
        </div>
        <el-button
          v-if="theme.theme.subscribed"
          size="small"
          type="success"
          @click.stop="handleUnfollow(theme.theme.id)"
          class="follow-button"
        >
          已关注
        </el-button>
        <el-button
          v-else
          size="small"
          @click.stop="handleFollow(theme.theme.id)"
          class="follow-button"
        >
          关注
        </el-button>
      </div>
    </div>

    <!-- 错误提示 -->
    <el-alert
      v-if="themeStore.error"
      :title="themeStore.error"
      type="error"
      show-icon
      class="error-alert"
    />

    <!-- 文章列表区域 -->
    <div class="article-container" v-if="!themeStore.error">
      <div class="category-header">
        <h2 class="category-title">
          {{ getCurrentCategoryLabel() }}
          <span class="category-desc">共{{ articleStore.total }}篇文章</span>
        </h2>
        
        <!-- 排序选项 -->
        <el-select v-model="sortBy" class="sort-select" size="small">
          <el-option label="最新发布" value="latest" />
          <el-option label="最多浏览" value="views" />
          <el-option label="最多点赞" value="likes" />
        </el-select>
      </div>
      
      <article-list 
        :articles="articleStore.currentPageArticles"
        v-loading="articleStore.loading"
        @comment-added="handleCommentAdded"
      />

      <!-- 分页器 -->
      <div class="pagination" v-if="articleStore.total > 0">
        <el-pagination
          v-model:current-page="articleStore.currentPage"
          :page-size="articleStore.pageSize"
          :total="articleStore.total"
          :page-sizes="[10, 20, 50]"
          layout="sizes, prev, pager, next, total"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
          background
        />
      </div>

      <!-- 无数据提示 -->
      <el-empty
        v-else
        description="暂无文章"
        :image-size="200"
      >
        <template #description>
          <p>该分类下暂无文章</p>
        </template>
      </el-empty>
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
import { useUserArticleStore } from '../store/userArticle'
import { useThemeStore } from '../store/theme'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const articleStore = useUserArticleStore()
const themeStore = useThemeStore()

// 排序方式
const sortBy = ref('latest')

// 当前选中的分类
const currentCategory = ref(route.query.category?.toString() || '')

// 分类图标映射
const categoryIcons: Record<string, any> = {
  '前端开发': Monitor,
  '后端开发': DataLine,
  '人工智能': Connection,
  '运维部署': Setting,
  '产品设计': Promotion,
  '项目管理': Management,
  '职场经验': Briefcase,
  '数据分析': TrendCharts
}

// 获取分类图标
const getCategoryIcon = (categoryName: string) => {
  return categoryIcons[categoryName] || Monitor
}

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
  const currentTheme = themeStore.findThemeById(Number(currentCategory.value))
  return currentTheme ? currentTheme.name : '全部文章'
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

// 处理每页数量变化
const handleSizeChange = (size: number) => {
  articleStore.setPageSize(size)
  loadArticles()
}

// 加载文章
const loadArticles = async () => {
  const params = {
    category: currentCategory.value,
    sortBy: sortBy.value === 'latest' ? 'time' : sortBy.value === 'views' ? 'views' : 'likes'
  }
  await articleStore.fetchArticles(params)
}

// 处理评论添加
const handleCommentAdded = ({ articleId, comment }: any) => {
  // TODO: 处理评论添加的逻辑
  console.log('Comment added:', articleId, comment)
}

// 处理关注
const handleFollow = async (themeId: number) => {
  try {
    await themeStore.followTheme(themeId)
    ElMessage.success('关注成功')
  } catch (error: any) {
    ElMessage.error(error.message || '关注失败')
  }
}

// 处理取消关注
const handleUnfollow = async (themeId: number) => {
  try {
    await themeStore.unfollowTheme(themeId)
    ElMessage.success('已取消关注')
  } catch (error: any) {
    ElMessage.error(error.message || '取消关注失败')
  }
}

// 监听路由参数变化
watch(
  () => route.query.category,
  (newCategory) => {
    if (newCategory !== currentCategory.value) {
      currentCategory.value = newCategory?.toString() || ''
      loadArticles()
    }
  }
)

// 监听排序方式变化
watch(sortBy, () => {
  loadArticles()
})

// 组件挂载时加载数据
onMounted(async () => {
  try {
    await themeStore.fetchAllThemes()
    // 如果URL中没有指定分类，使用第一个分类
    if (!currentCategory.value && themeStore.getAllThemes.length > 0) {
      currentCategory.value = themeStore.getAllThemes[0].id.toString()
    }
    await loadArticles()
  } catch (error) {
    console.error('Failed to load initial data:', error)
  }
})
</script>

<style scoped>
.category-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.category-tabs {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 16px;
  margin-bottom: 32px;
  padding: 20px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

.category-tab {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid transparent;
  position: relative;
}

.category-tab:hover {
  background-color: var(--el-color-primary-light-9);
  border-color: var(--el-color-primary-light-7);
}

.category-tab.active {
  background-color: var(--el-color-primary-light-8);
  border-color: var(--el-color-primary);
  color: var(--el-color-primary);
}

.category-tab.subscribed {
  border-color: var(--el-color-success);
  background-color: var(--el-color-success-light-9);
}

.subscribed-tag {
  position: absolute;
  top: -1px;
  right: -1px;
  border-radius: 0 8px 0 8px;
  font-size: 12px;
  padding: 2px 6px;
}

.category-tab .el-icon {
  font-size: 20px;
}

.article-count {
  color: var(--el-text-color-secondary);
  font-size: 13px;
  margin-left: auto;
}

.category-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--el-border-color-light);
}

.category-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.category-desc {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  font-weight: normal;
}

.article-container {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

.pagination {
  margin-top: 32px;
  display: flex;
  justify-content: center;
}

.error-alert {
  margin-bottom: 24px;
}

.sort-select {
  width: 120px;
}

.category-content {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 12px;
}

.follow-button {
  margin-left: auto;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .category-container {
    padding: 16px;
  }

  .category-tabs {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    padding: 16px;
  }

  .category-tab {
    padding: 12px;
  }

  .category-title {
    font-size: 20px;
  }

  .article-container {
    padding: 16px;
  }
}
</style> 