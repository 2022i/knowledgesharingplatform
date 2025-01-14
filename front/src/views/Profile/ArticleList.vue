<template>
  <div class="article-list">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-wrapper">
      <el-skeleton :rows="3" animated />
      <el-skeleton :rows="3" animated />
      <el-skeleton :rows="3" animated />
    </div>

    <!-- 文章列表 -->
    <template v-else>
      <div v-if="articles.length > 0">
        <div v-for="article in articles" :key="article.id" class="article-item">
          <div class="article-header">
            <h3 class="article-title" @click="handleArticleClick(article.id)">
              {{ article.title }}
              <el-tag 
                v-if="type" 
                :type="getStatusType(type)"
                size="small" 
                class="status-tag"
              >
                {{ getStatusLabel(type) }}
              </el-tag>
            </h3>
            <div class="article-actions">
              <el-button 
                v-if="type === 'deleting'"
                type="primary" 
                text 
                @click="handleUndoDelete(article.id)"
              >
                撤销删除
              </el-button>
              <template v-else>
                <el-button 
                  type="primary" 
                  text 
                  @click="$emit('edit', article.id)"
                >
                  编辑
                </el-button>
                <el-button 
                  type="danger" 
                  text 
                  @click="handleDelete(article.id)"
                >
                  删除
                </el-button>
              </template>
            </div>
          </div>

          <p class="article-summary">{{ article.summary || '暂无概要' }}</p>

          <div class="article-meta">
            <span class="meta-item">
              <el-icon><Clock /></el-icon>
              {{ formatDate(article.createTime) }}
            </span>
            <span class="meta-item">
              <el-icon><View /></el-icon>
              {{ article.viewUserCount || 0 }} 浏览
            </span>
            <span class="meta-item">
              <el-icon><Star /></el-icon>
              {{ article.supportUserCount || 0 }} 点赞
            </span>
            <span class="meta-item">
              <el-icon><Collection /></el-icon>
              {{ article.collectionUserCount || 0 }} 收藏
            </span>
            <span class="meta-item">
              <el-icon><ChatDotRound /></el-icon>
              {{ article.commentCount || 0 }} 评论
            </span>
            <span class="meta-item">
              <el-icon><Share /></el-icon>
              {{ article.shareUserCount || 0 }} 分享
            </span>
          </div>
        </div>

        <!-- 分页器 -->
        <div class="pagination">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="10"
            :total="total"
            layout="prev, pager, next"
            @current-change="handlePageChange"
            background
          />
        </div>
      </div>

      <!-- 空状态 -->
      <el-empty 
        v-else 
        :description="emptyText"
      >
        <el-button type="primary" @click="$router.push('/editor')">
          写文章
        </el-button>
      </el-empty>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { Clock, View, Star, Collection, ChatDotRound, Share } from '@element-plus/icons-vue'
import request from '../../utils/request'

const router = useRouter()

// 组件属性定义
const props = defineProps<{
  articles: any[]
  loading?: boolean
  type?: string
  emptyText?: string
}>()

// 组件事件
const emit = defineEmits<{
  (e: 'delete', id: number): void
  (e: 'edit', id: number): void
  (e: 'page-change', page: number): void
}>()

// 分页相关
const currentPage = ref(1)
const total = computed(() => props.articles.length)

// 获取文章状态类型
const getStatusType = (type: string) => {
  const typeMap = {
    published: 'success',
    publishing: 'warning',
    deleting: 'danger',
    unpublished: 'info',
    draft: ''
  }
  return typeMap[type] || ''
}

// 获取文章状态标签
const getStatusLabel = (type: string) => {
  const labelMap = {
    published: '已发布',
    publishing: '待审核',
    deleting: '待删除',
    unpublished: '已拒绝',
    draft: '草稿'
  }
  return labelMap[type] || ''
}

// 格式化日期
const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

// 处理文章点击
const handleArticleClick = (id: number) => {
  router.push(`/article/${id}`)
}

// 处理删除
const handleDelete = async (id: number) => {
  try {
    const confirmMessage = props.type === 'published'
      ? '确定要申请删除这篇文章吗？删除后需要等待审核。'
      : '确定要删除这篇文章吗？此操作不可恢复。'

    await ElMessageBox.confirm(
      confirmMessage,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    emit('delete', id)
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除操作失败:', error)
    }
  }
}

// 处理分页
const handlePageChange = (page: number) => {
  currentPage.value = page
  emit('page-change', page)
}

// 处理撤销删除
const handleUndoDelete = async (id: number) => {
  try {
    const response = await request.put('/server/undoDeleteRequest', null, {
      params: { articleId: id }
    })
    
    if (response.data.code === 200) {
      ElMessage.success(response.data.msg)
      emit('page-change', currentPage.value) // 刷新当前页面
    } else {
      ElMessage.error(response.data.msg || '撤销删除失败')
    }
  } catch (error) {
    console.error('撤销删除失败:', error)
    ElMessage.error('撤销删除失败')
  }
}
</script>

<style scoped>
.article-list {
  min-height: 200px;
}

.loading-wrapper {
  padding: 20px 0;
}

.article-item {
  padding: 24px 0;
  border-bottom: 1px solid #f0f0f0;
  transition: all 0.3s ease;
}

.article-item:hover {
  background-color: #f9f9f9;
  padding-left: 12px;
  padding-right: 12px;
}

.article-item:last-child {
  border-bottom: none;
}

.article-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.article-title {
  margin: 0;
  font-size: 18px;
  color: #303133;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
}

.article-title:hover {
  color: var(--el-color-primary);
}

.status-tag {
  margin-left: 8px;
}

.article-actions {
  display: flex;
  gap: 16px;
}

.article-summary {
  color: #606266;
  margin: 0 0 12px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.article-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  color: #909399;
  font-size: 14px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.meta-item .el-icon {
  font-size: 16px;
}

.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style> 