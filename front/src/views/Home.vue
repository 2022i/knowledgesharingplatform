<template>
  <div class="home-container">
    <!-- 内容分类标签 -->
    <div class="content-tabs">
      <span 
        v-for="tab in tabs" 
        :key="tab.value"
        :class="['tab-item', { active: activeTab === tab.value }]"
        @click="activeTab = tab.value"
      >
        {{ tab.label }}
      </span>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧文章列表 -->
      <div class="content-left">
        <!-- 使用 v-loading 显示加载状态 -->
        <article-list 
          :articles="articleStore.currentPageArticles"
          v-loading="articleStore.loading"
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

      <!-- 右侧边栏 -->
      <div class="content-right">
        <!-- 写文章卡片 -->
        <div class="write-card">
          <p>分享你的知识、经验和见解</p>
          <el-button type="primary" @click="$router.push('/editor')">
            <el-icon><Edit /></el-icon>写文章
          </el-button>
        </div>

        <!-- 热门话题 -->
        <div class="topic-card">
          <div class="card-header">
            <h3>热门话题</h3>
            <el-button link @click="$router.push('/category')">查看更多</el-button>
          </div>
          <div class="topic-list">
            <div 
              v-for="topic in hotTopics" 
              :key="topic.id" 
              class="topic-item"
              @click="$router.push(`/category?topic=${topic.id}`)"
            >
              <span class="topic-name"># {{ topic.name }}</span>
              <span class="topic-count">{{ topic.count }}篇文章</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Edit } from '@element-plus/icons-vue'
import { useArticleStore } from '../store/article'
import ArticleList from '../components/ArticleList.vue'

// 初始化 store
const articleStore = useArticleStore()
const activeTab = ref('recommend')

// 内容分类标签
const tabs = [
  { label: '关注', value: 'follow' },
  { label: '推荐', value: 'recommend' },
  { label: '热榜', value: 'hot' }
]

// 热门话题数据
const hotTopics = ref([
  { id: 'frontend', name: '前端开发', count: 328 },
  { id: 'ai', name: '人工智能', count: 256 },
  { id: 'backend', name: '后端架构', count: 198 },
  { id: 'design', name: '产品设计', count: 167 },
  { id: 'management', name: '项目管理', count: 145 }
])

// 处理分页变化
const handlePageChange = (page: number) => {
  articleStore.setCurrentPage(page)
  // 滚动到顶部
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

// 组件挂载时加载文章数据
onMounted(async () => {
  await articleStore.fetchArticles()
})

// 确保组件被正确注册
defineOptions({
  name: 'HomePage'
})
</script>

<style scoped>
.home-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

/* 内容分类标签样式 */
.content-tabs {
  display: flex;
  gap: 32px;
  margin-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.tab-item {
  padding: 12px 0;
  color: #8590a6;
  cursor: pointer;
  transition: all 0.3s;
}

.tab-item:hover {
  color: #121212;
}

.tab-item.active {
  color: #121212;
  font-weight: 500;
  position: relative;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 2px;
  background-color: #056de8;
}

/* 主要内容区域样式 */
.main-content {
  display: flex;
  gap: 20px;
}

.content-left {
  flex: 1;
  min-width: 0;
}

.content-right {
  width: 296px;
  flex-shrink: 0;
}

/* 分页器样式 */
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

/* 右侧卡片通用样式 */
.write-card,
.topic-card {
  background: #fff;
  border-radius: 2px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

/* 写文章卡片样式 */
.write-card {
  text-align: center;
}

.write-card p {
  color: #8590a6;
  margin-bottom: 12px;
}

.write-card .el-button {
  width: 100%;
}

/* 热门话题卡片样式 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.card-header h3 {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
}

.topic-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.topic-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  cursor: pointer;
  transition: all 0.3s ease;
}

.topic-item:hover {
  background-color: #f7f8fa;
  padding-left: 8px;
  padding-right: 8px;
  margin: 0 -8px;
  border-radius: 4px;
}

.topic-item:hover .topic-name {
  color: #056de8;
}

.topic-name {
  color: #121212;
  font-size: 14px;
}

.topic-count {
  color: #8590a6;
  font-size: 12px;
}
</style>
