<template>
  <div class="articles-page">
    <!-- 搜索和筛选区域 -->
    <div class="filter-section">
      <el-form :inline="true" :model="filterForm">
        <el-form-item label="状态">
          <el-select v-model="filterForm.status" placeholder="选择状态">
            <el-option label="全部" value="" />
            <el-option label="待审核" value="pending" />
            <el-option label="已发布" value="published" />
            <el-option label="已拒绝" value="rejected" />
            <el-option label="待删除" value="pending_delete" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input
            v-model="filterForm.keyword"
            placeholder="搜索文章标题/作者"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 当前筛选状态 -->
      <div class="current-filter" v-if="filterForm.status || filterForm.keyword">
        <span class="filter-label">当前筛选：</span>
        <el-tag v-if="filterForm.status" closeable @close="clearStatus">
          状态：{{ getStatusText(filterForm.status) }}
        </el-tag>
        <el-tag v-if="filterForm.keyword" closeable @close="clearKeyword">
          关键词：{{ filterForm.keyword }}
        </el-tag>
      </div>
    </div>

    <!-- 文章列表 -->
    <div class="articles-table">
      <el-table
        v-loading="loading"
        :data="articles"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200">
          <template #default="{ row }">
            <el-link type="primary" @click="viewArticle(row)">{{ row.title }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="author" label="作者" width="120">
          <template #default="{ row }">
            <div class="author-info">
              <el-avatar :size="24" :src="row.author.avatar" />
              <span>{{ row.author.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
            <el-tooltip
              v-if="row.status === 'rejected' && row.rejectReason"
              :content="row.rejectReason"
              placement="top"
            >
              <el-icon class="ml-2"><Warning /></el-icon>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button-group>
              <el-button 
                size="small" 
                @click="viewArticle(row)"
              >
                查看
              </el-button>
              <template v-if="row.status === 'pending'">
                <el-button 
                  type="success" 
                  size="small" 
                  @click="handleApprove(row)"
                >
                  通过
                </el-button>
                <el-button 
                  type="danger" 
                  size="small" 
                  @click="handleReject(row)"
                >
                  拒绝
                </el-button>
              </template>
              <template v-if="row.status === 'pending_delete'">
                <el-button 
                  type="success" 
                  size="small" 
                  @click="handleApproveDelete(row)"
                >
                  通过删除
                </el-button>
                <el-button 
                  type="danger" 
                  size="small" 
                  @click="handleRejectDelete(row)"
                >
                  拒绝删除
                </el-button>
              </template>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          layout="total, sizes, prev, pager, next"
        />
      </div>
    </div>

    <!-- 文章预览对话框 -->
    <el-dialog
      v-model="previewVisible"
      title="文章预览"
      width="800px"
      class="preview-dialog"
    >
      <div v-if="currentArticle" class="article-preview">
        <h1>{{ currentArticle.title }}</h1>
        <div class="article-meta">
          <span>作者：{{ currentArticle.author.name }}</span>
          <span>创建时间：{{ formatDate(currentArticle.createTime) }}</span>
          <span>状态：{{ getStatusText(currentArticle.status) }}</span>
          <span v-if="currentArticle.updateTime">
            最后更新：{{ formatDate(currentArticle.updateTime) }}
          </span>
        </div>
        <div v-if="currentArticle.status === 'rejected'" class="reject-reason">
          <el-alert
            type="error"
            :title="'拒绝原因：' + (currentArticle.rejectReason || '无')"
            :closable="false"
          />
        </div>
        <div class="article-content" v-html="currentArticle.content"></div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="previewVisible = false">关闭</el-button>
          <template v-if="currentArticle?.status === 'pending'">
            <el-button type="success" @click="handleApprove(currentArticle)">通过</el-button>
            <el-button type="danger" @click="handleReject(currentArticle)">拒绝</el-button>
          </template>
          <template v-if="currentArticle?.status === 'pending_delete'">
            <el-button type="success" @click="handleApproveDelete(currentArticle)">通过删除</el-button>
            <el-button type="danger" @click="handleRejectDelete(currentArticle)">拒绝删除</el-button>
          </template>
        </span>
      </template>
    </el-dialog>

    <!-- 拒绝原因对话框 -->
    <el-dialog
      v-model="rejectDialogVisible"
      title="拒绝原因"
      width="500px"
    >
      <el-form :model="rejectForm" :rules="rejectFormRules">
        <el-form-item 
          label="拒绝原因" 
          prop="reason"
          :rules="[
            { required: true, message: '请输入拒绝原因', trigger: 'blur' },
            { min: 10, message: '拒绝原因至少10个字符', trigger: 'blur' }
          ]"
        >
          <el-input
            v-model="rejectForm.reason"
            type="textarea"
            rows="4"
            placeholder="请详细说明拒绝原因，至少10个字符"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="rejectDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmReject">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Warning } from '@element-plus/icons-vue'
import { useArticleStore } from '../../store/article'
import { formatDate } from '../../utils/date'
import type { Article, ArticleStatus } from '../../types/article'

const articleStore = useArticleStore()

// 列表数据
const loading = ref(false)
const articles = ref<Article[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 筛选表单
const filterForm = ref({
  status: '',
  keyword: ''
})

// 预览相关
const previewVisible = ref(false)
const currentArticle = ref<Article | null>(null)

// 拒绝对话框相关
const rejectDialogVisible = ref(false)
const rejectForm = ref({
  reason: ''
})
const articleToReject = ref<Article | null>(null)

// 获取文章列表
const fetchArticles = async () => {
  loading.value = true
  try {
    const response = await articleStore.getArticleList({
      page: currentPage.value,
      pageSize: pageSize.value,
      status: filterForm.value.status || undefined,
      keyword: filterForm.value.keyword
    })
    
    articles.value = response.data
    total.value = response.total
  } catch (error) {
    console.error('获取文章列表失败:', error)
    ElMessage.error('获取文章列表失败')
  } finally {
    loading.value = false
  }
}

// 状态相关方法
const getStatusType = (status: ArticleStatus) => {
  const statusMap: Record<ArticleStatus, string> = {
    draft: 'info',
    pending: 'warning',
    published: 'success',
    rejected: 'danger',
    pending_delete: 'warning'
  }
  return statusMap[status]
}

const getStatusText = (status: ArticleStatus) => {
  const statusMap: Record<ArticleStatus, string> = {
    draft: '草稿',
    pending: '待审核',
    published: '已发布',
    rejected: '已拒绝',
    pending_delete: '待删除'
  }
  return statusMap[status]
}

// 查看文章
const viewArticle = (article: Article) => {
  currentArticle.value = article
  previewVisible.value = true
}

// 通过文章
const handleApprove = async (article: Article) => {
  try {
    await ElMessageBox.confirm(
      '确定通过这篇文章吗？通过后文章将立即发布。',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success'
      }
    )
    
    await articleStore.approveArticle(article.id)
    ElMessage.success('文章已通过并发布')
    previewVisible.value = false
    fetchArticles()
  } catch {
    // 用户取消操作
  }
}

// 拒绝文章
const handleReject = (article: Article) => {
  articleToReject.value = article
  rejectForm.value.reason = ''
  rejectDialogVisible.value = true
  previewVisible.value = false
}

// 确认拒绝
const confirmReject = async () => {
  if (!rejectForm.value.reason.trim()) {
    return ElMessage.warning('请输入拒绝原因')
  }

  if (rejectForm.value.reason.length < 10) {
    return ElMessage.warning('拒绝原因至少需要10个字符')
  }

  try {
    await articleStore.rejectArticle(
      articleToReject.value!.id,
      rejectForm.value.reason
    )
    ElMessage.success('已拒绝文章')
    rejectDialogVisible.value = false
    fetchArticles()
  } catch (error) {
    console.error('拒绝文章失败:', error)
    ElMessage.error('拒绝文章失败')
  }
}

// 通过删除请求
const handleApproveDelete = async (article: Article) => {
  try {
    await ElMessageBox.confirm(
      '确定通过删除请求吗？通过后文章将被永久删除。',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await articleStore.deleteArticle(article.id)
    ElMessage.success('文章已删除')
    fetchArticles()
  } catch {
    // 用户取消操作
  }
}

// 拒绝删除请求
const handleRejectDelete = (article: Article) => {
  articleToReject.value = article
  rejectForm.value.reason = ''
  rejectDialogVisible.value = true
  previewVisible.value = false
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchArticles()
}

// 重置筛选
const resetFilter = () => {
  filterForm.value = {
    status: '',
    keyword: ''
  }
  handleSearch()
}

// 分页相关方法
const handleSizeChange = (val: number) => {
  pageSize.value = val
  fetchArticles()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchArticles()
}

// 清除状态筛选
const clearStatus = () => {
  filterForm.value.status = ''
  handleSearch()
}

// 清除关键词筛选
const clearKeyword = () => {
  filterForm.value.keyword = ''
  handleSearch()
}

// 初始化
onMounted(() => {
  fetchArticles()
})
</script>

<style scoped>
.articles-page {
  background: #fff;
  border-radius: 4px;
  padding: 20px;
}

.filter-section {
  margin-bottom: 20px;
}

.articles-table {
  margin-top: 20px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.preview-dialog :deep(.el-dialog__body) {
  padding: 20px;
}

.article-preview {
  max-height: 60vh;
  overflow-y: auto;
}

.article-preview h1 {
  margin: 0 0 20px;
  font-size: 24px;
}

.article-meta {
  margin-bottom: 20px;
  color: #666;
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.reject-reason {
  margin: 20px 0;
}

.article-content {
  line-height: 1.6;
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.ml-2 {
  margin-left: 8px;
}

.current-filter {
  margin-top: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-label {
  color: #606266;
  font-size: 14px;
}

.current-filter :deep(.el-tag) {
  margin-right: 8px;
}
</style> 