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
        <article-list 
          :articles="articles"
          :loading="loading"
          @update:articles="articles = $event"
          @page-change="handlePageChange"
          :empty-text="getEmptyText()"
        />
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
import { ref, onMounted, watch } from 'vue'
import { Edit } from '@element-plus/icons-vue'
import ArticleList from '../components/ArticleList.vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

// 状态管理
const activeTab = ref('recommend')
const loading = ref(false)
const articles = ref([])
const currentPage = ref(1)
const total = ref(0)

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

// 获取用户ID
const getUserId = () => {
  const userInfo = localStorage.getItem('userInfo')
  return userInfo ? JSON.parse(userInfo).id : 0
}

// 加载文章列表
const loadArticles = async () => {
  loading.value = true
  try {
    let response
    const userId = getUserId()

    console.log('=== 开始加载文章列表 ===')
    console.log('当前标签:', activeTab.value)
    console.log('用户ID:', userId)

    let apiEndpoint = ''
    let requestParams = {}

    // 根据不同的tab调用不同的接口
    switch (activeTab.value) {
      case 'follow':
        apiEndpoint = '/server/getList/followArticles'
        requestParams = { userId }
        break
      case 'recommend':
        apiEndpoint = '/server/getList/recommendedArticlesUser'
        requestParams = { userId }
        break
      case 'hot':
        apiEndpoint = '/server/getList/hotArticles'
        requestParams = {}
        break
      default:
        throw new Error('未知的标签类型')
    }

    // 打印请求信息
    console.log('=== 发送请求 ===')
    console.log('请求URL:', apiEndpoint)
    console.log('请求参数:', requestParams)
    console.log('完整请求URL:', `http://127.0.0.1:8081${apiEndpoint}${Object.keys(requestParams).length ? '?' + new URLSearchParams(requestParams).toString() : ''}`)

    response = await request.get(apiEndpoint, {
      params: requestParams
    })

    console.log('=== 接收响应 ===')
    console.log('响应状态:', response.status)
    console.log('响应头:', response.headers)
    console.log('响应数据:', response.data)

    // 检查响应数据是否是数组
    if (Array.isArray(response.data)) {
      console.log('=== 数据处理 ===')
      console.log('原始数据条数:', response.data.length)
      
      // 格式化文章数据
      articles.value = response.data.map((article: any) => {
        const formattedArticle = {
          id: article.id,
          title: article.title,
          theme: article.theme,
          content: article.content,
          summary: article.summary,
          relatedKnowledge: article.relatedKnowledge || [],
          createTime: article.createTime,
          viewUserCount: article.viewUserCount || 0,
          supportUserCount: article.supportUserCount || 0,
          opposeUserCount: article.opposeUserCount || 0,
          commentCount: article.commentCount || 0,
          collectionUserCount: article.collectionUserCount || 0,
          shareUserCount: article.shareUserCount || 0,
          author: {
            id: article.author?.id,
            username: article.author?.username || '未知用户',
            avatar: article.author?.avatar
          },
          support: article.support || false,
          oppose: article.oppose || false,
          collect: article.collect || false
        }
        return formattedArticle
      })

      total.value = response.data.length
      
      // 打印处理后的数据示例
      if (articles.value.length > 0) {
        console.log('处理后的第一篇文章数据:', articles.value[0])
        console.log('处理后的数据条数:', articles.value.length)
      }
    } else {
      console.error('=== 数据格式错误 ===')
      console.error('预期格式: Array')
      console.error('实际数据:', response.data)
      articles.value = []
      total.value = 0
      ElMessage.warning('暂无文章')
    }
  } catch (error: any) {
    console.error('=== 请求错误 ===')
    console.error('错误类型:', error.constructor.name)
    console.error('错误消息:', error.message)
    console.error('错误堆栈:', error.stack)
    if (error.response) {
      console.error('响应状态:', error.response.status)
      console.error('响应数据:', error.response.data)
    }
    articles.value = []
    total.value = 0
    ElMessage.error(error?.response?.data?.msg || error?.message || '加载文章失败')
  } finally {
    console.log('=== 加载文章列表结束 ===')
    console.log('最终文章数量:', articles.value.length)
    console.log('加载状态:', loading.value)
    loading.value = false
  }
}

// 处理分页变化
const handlePageChange = (page: number) => {
  currentPage.value = page
  // 滚动到顶部
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
  loadArticles()
}

// 监听标签切换
watch(activeTab, () => {
  currentPage.value = 1
  loadArticles()
})

// 组件挂载时加载文章数据
onMounted(() => {
  loadArticles()
})

// 确保组件被正确注册
defineOptions({
  name: 'HomePage'
})

// 获取空状态文本
const getEmptyText = () => {
  switch (activeTab.value) {
    case 'follow':
      return '暂无关注内容，去关注感兴趣的作者和主题吧'
    case 'recommend':
      return '暂无推荐内容'
    case 'hot':
      return '暂无热门内容'
    default:
      return '暂无内容'
  }
}
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
