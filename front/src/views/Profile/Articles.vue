<template>
  <div class="articles-container">
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="已发布" name="published">
        <article-list 
          :articles="publishedArticles" 
          :loading="loading.published"
          type="published"
          empty-text="暂无已发布的文章"
          @page-change="(page) => handlePageChange(page, 'published')"
          @delete="handleDeletePublished"
          @edit="handleEdit"
        />
      </el-tab-pane>
      
      <el-tab-pane label="待审核" name="publishing">
        <article-list 
          :articles="publishingArticles" 
          :loading="loading.publishing"
          type="publishing"
          empty-text="暂无待审核的文章"
          @page-change="(page) => handlePageChange(page, 'publishing')"
          @delete="handleDirectDelete"
          @edit="handleEdit"
        />
      </el-tab-pane>
      
      <el-tab-pane label="待删除" name="deleting">
        <article-list 
          :articles="deletingArticles" 
          :loading="loading.deleting"
          type="deleting"
          empty-text="暂无待删除的文章"
          @page-change="(page) => handlePageChange(page, 'deleting')"
        />
      </el-tab-pane>
      
      <el-tab-pane label="已拒绝" name="unpublished">
        <article-list 
          :articles="unpublishedArticles" 
          :loading="loading.unpublished"
          type="unpublished"
          empty-text="暂无被拒绝的文章"
          @page-change="(page) => handlePageChange(page, 'unpublished')"
          @delete="handleDirectDelete"
          @edit="handleEdit"
        />
      </el-tab-pane>

      <el-tab-pane label="草稿箱" name="draft">
        <article-list 
          :articles="draftArticles" 
          :loading="loading.draft"
          type="draft"
          empty-text="暂无草稿"
          @page-change="(page) => handlePageChange(page, 'draft')"
          @delete="handleDirectDelete"
          @edit="handleEdit"
        />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import ArticleList from './ArticleList.vue'
import request from '../../utils/request'

const router = useRouter()

// 当前激活的标签页
const activeTab = ref('published')

// 文章数据接口
interface Author {
  id: number
  username: string
}

interface Article {
  id: number
  title: string
  theme: string
  content: string
  summary: string | null
  relatedKnowledge: any[]
  createTime: string
  viewUserCount: number
  supportUserCount: number
  opposeUserCount: number
  commentCount: number
  collectionUserCount: number
  shareUserCount: number
  author: Author
  oppose: boolean
  collect: boolean
  support: boolean
}

interface Draft {
  id: number
  title: string
  content: string
  theme: {
    id: number
    name: string
  }
}

// 各状态文章列表
const publishedArticles = ref<Article[]>([])
const publishingArticles = ref<Article[]>([])
const deletingArticles = ref<Article[]>([])
const unpublishedArticles = ref<Article[]>([])
const draftArticles = ref<Draft[]>([])

// 加载状态
const loading = ref({
  published: false,
  publishing: false,
  deleting: false,
  unpublished: false,
  draft: false
})

// 分页相关
const currentPage = ref({
  published: 1,
  publishing: 1,
  deleting: 1,
  unpublished: 1,
  draft: 1
})

// 获取用户ID
const userId = computed(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  return userInfo.id
})

// 加载文章列表
const loadArticles = async (type: string) => {
  if (!userId.value) {
    console.log('未找到用户ID，请先登录')
    return
  }

  const apiMap = {
    published: '/server/personalCenterData/getPublishedArticles',
    publishing: '/server/personalCenterData/getPublishingArticles',
    deleting: '/server/personalCenterData/getDeletingArticles',
    unpublished: '/server/personalCenterData/getUnpublishedArticles',
    draft: '/server/personalCenterData/getDrafts'
  }

  const api = apiMap[type]
  if (!api) return

  console.log(`开始加载${type}文章列表，用户ID:`, userId.value)
  loading.value[type] = true

  try {
    const response = await request.get(api, {
      params: { userId: userId.value }
    })

    console.log(`获取${type}文章列表成功:`, response.data)

    if (Array.isArray(response.data)) {
      switch (type) {
        case 'published':
          publishedArticles.value = response.data
          break
        case 'publishing':
          publishingArticles.value = response.data
          break
        case 'deleting':
          deletingArticles.value = response.data
          break
        case 'unpublished':
          unpublishedArticles.value = response.data
          break
        case 'draft':
          draftArticles.value = response.data
          break
      }
    } else {
      console.warn('API返回数据格式错误，期望数组类型:', response.data)
    }
  } catch (error) {
    console.error(`加载${type}文章列表失败:`, error)
  } finally {
    loading.value[type] = false
    console.log(`${type}文章列表加载完成`)
  }
}

// 处理已发布文章的删除
const handleDeletePublished = async (articleId: number) => {
  try {
    const response = await request.put('/server/submitDeleteRequest', null, {
      params: { articleId }
    })
    
    if (response.data.code === 200) {
      ElMessage.success(response.data.msg)
      loadArticles('published')
    } else {
      ElMessage.error(response.data.msg || '删除请求提交失败')
    }
  } catch (error) {
    console.error('删除请求提交失败:', error)
    ElMessage.error('删除请求提交失败')
  }
}

// 处理直接删除
const handleDirectDelete = async (articleId: number) => {
  try {
    const response = await request.delete('/server/directDelete', {
      params: { articleId }
    })
    
    if (response.data.code === 200) {
      ElMessage.success(response.data.msg)
      loadArticles(activeTab.value)
    } else {
      ElMessage.error(response.data.msg || '删除失败')
    }
  } catch (error) {
    console.error('删除失败:', error)
    ElMessage.error('删除失败')
  }
}

// 处理编辑
const handleEdit = async (articleId: number) => {
  try {
    // 获取文章详情
    const response = await request.get('/server/getRenderedArticle', {
      params: { 
        articleId,
        userId: userId.value 
      }
    })
    
    if (response.data) {
      // 跳转到编辑页面
      router.push({
        path: `/editor/${articleId}`,
        query: { type: activeTab.value }
      })
    }
  } catch (error) {
    console.error('获取文章详情失败:', error)
    ElMessage.error('获取文章详情失败')
  }
}

// 处理标签页切换
const handleTabChange = (tab: string) => {
  console.log('切换标签页:', tab)
  loadArticles(tab)
}

// 处理分页变化
const handlePageChange = (page: number, type: string) => {
  console.log('切换页码:', page, '类型:', type)
  currentPage.value[type] = page
  loadArticles(type)
}

// 组件挂载时加载数据
onMounted(() => {
  loadArticles('published')
})
</script>

<style scoped>
.articles-container {
  background: white;
  border-radius: 8px;
  padding: 24px;
  min-height: 400px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

:deep(.el-tabs__nav-wrap::after) {
  height: 1px;
  background-color: #f0f0f0;
}

:deep(.el-tabs__item) {
  font-size: 16px;
  padding: 0 24px;
}

:deep(.el-tabs__item.is-active) {
  font-weight: 600;
}
</style>
