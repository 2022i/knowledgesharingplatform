<template>
  <div class="articles-container">
    <el-radio-group v-model="currentTab" @change="handleTabChange">
      <el-radio-button value="pending">待审核文章</el-radio-button>
      <el-radio-button value="deleting">待删除文章</el-radio-button>
    </el-radio-group>

    <div class="article-list" v-loading="adminArticleStore.isLoading">
      <el-empty v-if="!adminArticleStore.getArticles.length" description="暂无文章" />
      <el-card v-else v-for="article in adminArticleStore.getArticles" :key="article.id" class="article-item">
        <h3>{{ article.title }}</h3>
        <p class="article-info">
          作者ID: {{ article.authorId }} | 创建时间: {{ formatTime(article.createTime) }}
        </p>
        <p class="article-summary">{{ article.summary || '暂无摘要' }}</p>
        
        <div class="article-actions">
          <el-button type="primary" @click="viewArticle(article)">查看详情</el-button>
          <template v-if="currentTab === 'pending'">
            <el-button type="success" @click="handleApprove(article.id)">通过</el-button>
            <el-button type="danger" @click="handleReject(article.id)">不通过</el-button>
          </template>
          <template v-else>
            <el-button type="success" @click="handleApproveDelete(article.id)">同意删除</el-button>
            <el-button type="danger" @click="handleRejectDelete(article.id)">拒绝删除</el-button>
          </template>
        </div>
      </el-card>
    </div>

    <!-- 文章详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="文章详情"
      width="70%"
      :before-close="handleCloseDetail"
    >
      <div v-if="currentArticle" class="article-detail">
        <h2>{{ currentArticle.title }}</h2>
        <p class="article-info">
          作者ID: {{ currentArticle.authorId }} | 创建时间: {{ formatTime(currentArticle.createTime) }}
        </p>
        <div class="article-content" v-html="currentArticle.content"></div>
      </div>
    </el-dialog>

    <!-- 拒绝原因对话框 -->
    <el-dialog
      v-model="rejectDialogVisible"
      :title="currentTab === 'pending' ? '拒绝发布原因' : '拒绝删除原因'"
      width="50%"
    >
      <el-form>
        <el-form-item label="拒绝原因">
          <el-input
            v-model="rejectReason"
            type="textarea"
            :rows="4"
            placeholder="请输入拒绝原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="rejectDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmReject" :loading="submitting">
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAdminArticleStore } from '../../store/adminArticle'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'

const adminArticleStore = useAdminArticleStore()
const currentTab = ref<'pending' | 'deleting'>('pending')
const detailDialogVisible = ref(false)
const rejectDialogVisible = ref(false)
const rejectReason = ref('')
const currentArticle = ref<any>(null)
const currentRejectId = ref<number | null>(null)
const submitting = ref(false)

// 格式化时间
const formatTime = (time: string) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
}

// 处理标签切换
const handleTabChange = async (tab: 'pending' | 'deleting') => {
  try {
    if (tab === 'pending') {
      await adminArticleStore.fetchUncheckArticles()
    } else {
      await adminArticleStore.fetchDeletingArticles()
    }
  } catch (error) {
    console.error('Failed to fetch articles:', error)
    ElMessage.error('获取文章列表失败')
  }
}

// 查看文章详情
const viewArticle = (article: any) => {
  currentArticle.value = article
  detailDialogVisible.value = true
}

// 关闭详情对话框
const handleCloseDetail = () => {
  currentArticle.value = null
  detailDialogVisible.value = false
}

// 通过文章
const handleApprove = async (articleId: number) => {
  try {
    const auditorId = parseInt(localStorage.getItem('userId') || '0', 10)
    await adminArticleStore.approveArticle(articleId, auditorId)
  } catch (error) {
    console.error('Failed to approve article:', error)
  }
}

// 拒绝文章
const handleReject = (articleId: number) => {
  currentRejectId.value = articleId
  rejectReason.value = ''
  rejectDialogVisible.value = true
}

// 通过删除请求
const handleApproveDelete = async (articleId: number) => {
  try {
    const auditorId = parseInt(localStorage.getItem('userId') || '0', 10)
    await adminArticleStore.approveDeleteArticle(articleId, auditorId)
  } catch (error) {
    console.error('Failed to approve delete:', error)
  }
}

// 拒绝删除请求
const handleRejectDelete = (articleId: number) => {
  currentRejectId.value = articleId
  rejectReason.value = ''
  rejectDialogVisible.value = true
}

// 确认拒绝
const confirmReject = async () => {
  if (!rejectReason.value.trim()) {
    ElMessage.warning('请输入拒绝原因')
    return
  }

  if (!currentRejectId.value) return

  submitting.value = true
  try {
    const auditorId = parseInt(localStorage.getItem('userId') || '0', 10)
    if (currentTab.value === 'pending') {
      await adminArticleStore.rejectArticle(currentRejectId.value, auditorId, rejectReason.value)
    } else {
      await adminArticleStore.rejectDeleteArticle(currentRejectId.value, auditorId, rejectReason.value)
    }
    rejectDialogVisible.value = false
  } catch (error) {
    console.error('Failed to reject:', error)
  } finally {
    submitting.value = false
  }
}

// 初始加载
onMounted(async () => {
  await handleTabChange(currentTab.value)
})
</script>

<style scoped>
.articles-container {
  padding: 20px;
}

.article-list {
  margin-top: 20px;
}

.article-item {
  margin-bottom: 20px;
}

.article-info {
  color: #666;
  font-size: 14px;
  margin: 10px 0;
}

.article-summary {
  color: #333;
  margin: 10px 0;
}

.article-actions {
  margin-top: 15px;
}

.article-detail {
  padding: 20px;
}

.article-content {
  margin-top: 20px;
  line-height: 1.6;
}

.el-button {
  margin-right: 10px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 