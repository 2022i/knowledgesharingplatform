<template>
  <div class="article-list">
    <div v-if="loading" class="loading-skeleton">
      <el-skeleton :rows="3" animated />
      <el-skeleton :rows="3" animated />
      <el-skeleton :rows="3" animated />
    </div>
    <template v-else>
      <article-list-item
        v-for="article in articles"
        :key="article.id"
        :article="article"
        @support="handleSupport"
        @oppose="handleOppose"
        @collect="handleCollect"
        @share="handleShare"
      />
      <div v-if="articles.length === 0" class="empty-state">
        暂无文章
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import ArticleListItem from './ArticleListItem.vue'
import { useUserArticleStore } from '../store/userArticle'
import { ElMessage } from 'element-plus'

// Props 定义
const props = defineProps<{
  articles: any[]
  loading?: boolean
}>()

// Emits 定义
const emit = defineEmits<{
  'update:articles': [articles: any[]]
}>()

const articleStore = useUserArticleStore()

// 处理点赞
const handleSupport = async (articleId: number) => {
  try {
    await articleStore.toggleSupport(articleId)
    const updatedArticle = await articleStore.getRenderedArticle(articleId)
    if (updatedArticle) {
      const newArticles = [...props.articles]
      const index = newArticles.findIndex(article => article.id === articleId)
      if (index !== -1) {
        newArticles[index] = { ...newArticles[index], ...updatedArticle }
        emit('update:articles', newArticles)
      }
    }
  } catch (error) {
    console.error('Failed to toggle support:', error)
    ElMessage.error('操作失败，请重试')
  }
}

// 处理反对
const handleOppose = async (articleId: number) => {
  try {
    await articleStore.toggleOppose(articleId)
    const updatedArticle = await articleStore.getRenderedArticle(articleId)
    if (updatedArticle) {
      const newArticles = [...props.articles]
      const index = newArticles.findIndex(article => article.id === articleId)
      if (index !== -1) {
        newArticles[index] = { ...newArticles[index], ...updatedArticle }
        emit('update:articles', newArticles)
      }
    }
  } catch (error) {
    console.error('Failed to toggle oppose:', error)
    ElMessage.error('操作失败，请重试')
  }
}

// 处理收藏
const handleCollect = async (articleId: number) => {
  try {
    console.log('开始收藏操作:', articleId)
    const result = await articleStore.toggleCollect(articleId)
    if (result) {
      const newArticles = [...props.articles]
      const index = newArticles.findIndex(article => article.id === articleId)
      if (index !== -1) {
        newArticles[index] = { ...newArticles[index], ...result }
        console.log('更新后的文章数据:', newArticles[index])
        emit('update:articles', newArticles)
      }
    }
  } catch (error) {
    console.error('收藏操作失败:', error)
    ElMessage.error('操作失败，请重试')
  }
}

// 处理分享
const handleShare = async (articleId: number) => {
  try {
    console.log('开始分享操作:', articleId)
    const result = await articleStore.shareArticle(articleId)
    if (result) {
      const newArticles = [...props.articles]
      const index = newArticles.findIndex(article => article.id === articleId)
      if (index !== -1) {
        newArticles[index] = { ...newArticles[index], ...result }
        console.log('更新后的文章数据:', newArticles[index])
        emit('update:articles', newArticles)
      }
    }
  } catch (error) {
    console.error('分享操作失败:', error)
    ElMessage.error('分享失败，请重试')
  }
}
</script>

<style scoped>
.article-list {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}

.loading-skeleton {
  padding: 20px;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #909399;
}
</style> 