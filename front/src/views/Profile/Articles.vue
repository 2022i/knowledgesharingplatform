<template>
  <div class="articles-container">
    <!-- 文章筛选和排序 -->
    <div class="filter-bar">
      <div class="filter-tabs">
        <span 
          v-for="filter in filters" 
          :key="filter.value"
          :class="['filter-item', { active: currentFilter === filter.value }]"
          @click="currentFilter = filter.value"
        >
          {{ filter.label }}
        </span>
      </div>
      <el-dropdown @command="handleSort">
        <span class="sort-trigger">
          {{ currentSort.label }}
          <el-icon><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item 
              v-for="sort in sorts" 
              :key="sort.value"
              :command="sort.value"
            >
              {{ sort.label }}
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

    <!-- 文章列表 -->
    <div class="article-list">
      <div v-for="article in articles" :key="article.id" class="article-item">
        <!-- 文章主体 -->
        <div class="article-main">
          <h3 class="article-title">
            <router-link :to="`/article/${article.id}`">{{ article.title }}</router-link>
            <span v-if="article.status === 'draft'" class="draft-tag">草稿</span>
          </h3>
          <p class="article-excerpt">{{ article.excerpt }}</p>
          
          <!-- 文章数据 -->
          <div class="article-meta">
            <span class="publish-time">{{ article.createTime }}</span>
            <template v-if="article.status === 'published'">
              <span class="divider">·</span>
              <span class="view-count">{{ article.views }} 次浏览</span>
              <span class="divider">·</span>
              <span class="like-count">{{ article.likes }} 赞同</span>
              <span class="divider">·</span>
              <span class="comment-count">{{ article.comments }} 评论</span>
            </template>
            <template v-else>
              <span class="divider">·</span>
              <span class="update-time">最后编辑于 {{ article.updateTime }}</span>
            </template>
          </div>
        </div>

        <!-- 文章操作 -->
        <div class="article-actions">
          <el-dropdown trigger="click" @command="(command) => handleAction(command, article)">
            <el-button link>
              <el-icon><More /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="edit">
                  <el-icon><Edit /></el-icon>编辑文章
                </el-dropdown-item>
                <el-dropdown-item command="delete" divided>
                  <el-icon><Delete /></el-icon>删除{{ article.status === 'draft' ? '草稿' : '文章' }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <!-- 空状态 -->
      <el-empty
        v-if="articles.length === 0"
        :description="currentFilter === 'draft' ? '草稿箱空空如也' : '还没有发布过文章'"
      >
        <el-button type="primary" @click="router.push('/editor')">
          写文章
        </el-button>
      </el-empty>
    </div>

    <!-- 分页器 -->
    <div class="pagination" v-if="articles.length > 0">
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
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { 
  ArrowDown, 
  More, 
  Edit, 
  FolderAdd, 
  Delete 
} from '@element-plus/icons-vue'
import { useArticleStore } from '../../store/article'
import type { Article } from '../../types/article'

const router = useRouter()
const articleStore = useArticleStore()

// 筛选选项
const filters = [
  { label: '全部文章', value: 'all' },
  { label: '已发布', value: 'published' },
  { label: '草稿箱', value: 'draft' }
]
const currentFilter = ref('all')

// 排序选项
const sorts = [
  { label: '最新发布', value: 'newest' },
  { label: '最多浏览', value: 'views' },
  { label: '最多点赞', value: 'likes' }
]
const currentSort = ref(sorts[0])

// 分页相关
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(0)

// 根据筛选条件获取文章列表
const articles = computed(() => {
  let filteredArticles: Article[] = []
  
  switch (currentFilter.value) {
    case 'published':
      filteredArticles = articleStore.getPublishedArticles
      break
    case 'draft':
      filteredArticles = articleStore.getDraftArticles
      break
    default:
      filteredArticles = [...articleStore.articles, ...articleStore.draftArticles]
  }

  // 排序
  return filteredArticles.sort((a, b) => {
    switch (currentSort.value.value) {
      case 'views':
        return b.views - a.views
      case 'likes':
        return b.likes - a.likes
      default:
        return new Date(b.createTime).getTime() - new Date(a.createTime).getTime()
    }
  })
})

// 加载文章列表
const loadArticles = async () => {
  await articleStore.fetchArticles()
  await articleStore.fetchArticles('draft')
  total.value = articles.value.length
}

onMounted(() => {
  loadArticles()
})

// 处理排序
const handleSort = (value: string) => {
  currentSort.value = sorts.find(sort => sort.value === value) || sorts[0]
}

// 处理文章操作
const handleAction = async (command: string, article: Article) => {
  switch (command) {
    case 'edit':
      router.push(`/editor/${article.id}`)
      break
    case 'delete':
      try {
        await ElMessageBox.confirm(
          `确定要删除${article.status === 'draft' ? '草稿' : '文章'}吗？`, 
          '提示', 
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        await articleStore.deleteArticle(article.id, article.status)
        ElMessage.success('删除成功')
        loadArticles()
      } catch {
        // 用户取消删除
      }
      break
  }
}

// 处理分页
const handlePageChange = (page: number) => {
  currentPage.value = page
}
</script>

<style scoped>
.articles-container {
  background: #fff;
  border-radius: 0 0 8px 8px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #f0f0f0;
}

.filter-tabs {
  display: flex;
  gap: 24px;
}

.filter-item {
  color: #8590a6;
  cursor: pointer;
  padding: 4px 0;
  position: relative;
}

.filter-item:hover {
  color: #121212;
}

.filter-item.active {
  color: #121212;
  font-weight: 500;
}

.filter-item.active::after {
  content: '';
  position: absolute;
  bottom: -16px;
  left: 0;
  right: 0;
  height: 2px;
  background: #056de8;
}

.sort-trigger {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #8590a6;
  cursor: pointer;
}

.article-list {
  padding: 0 24px;
}

.article-item {
  display: flex;
  justify-content: space-between;
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
}

.article-main {
  flex: 1;
  margin-right: 16px;
}

.article-title {
  margin: 0 0 8px;
  font-size: 18px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.article-title a {
  color: #121212;
  text-decoration: none;
}

.article-title a:hover {
  color: #056de8;
}

.draft-tag {
  font-size: 12px;
  color: #8590a6;
  border: 1px solid #8590a6;
  padding: 2px 6px;
  border-radius: 3px;
}

.article-excerpt {
  color: #646464;
  font-size: 15px;
  line-height: 1.6;
  margin: 0 0 12px;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.article-meta {
  color: #8590a6;
  font-size: 14px;
  display: flex;
  align-items: center;
}

.divider {
  margin: 0 8px;
}

.pagination {
  padding: 24px;
  display: flex;
  justify-content: center;
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
