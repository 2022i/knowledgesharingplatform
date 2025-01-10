<template>
  <div class="comments-page">
    <!-- 搜索和筛选区域 -->
    <div class="filter-section">
      <el-form :inline="true" :model="filterForm">
        <el-form-item label="状态">
          <el-select v-model="filterForm.status" placeholder="选择状态">
            <el-option label="全部" value="" />
            <el-option label="正常" value="normal" />
            <el-option label="已隐藏" value="hidden" />
            <el-option label="已举报" value="reported" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input
            v-model="filterForm.keyword"
            placeholder="搜索评论内容/用户"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 评论列表 -->
    <div class="comments-table">
      <el-table
        v-loading="loading"
        :data="comments"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="评论内容" min-width="300">
          <template #default="{ row }">
            <div class="comment-content">
              <div class="text">{{ row.content }}</div>
              <div class="article-link">
                评论于文章：
                <el-link type="primary" @click="viewArticle(row.article)">
                  {{ row.article.title }}
                </el-link>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="评论用户" width="180">
          <template #default="{ row }">
            <div class="user-info">
              <el-avatar :size="24" :src="row.user.avatar" />
              <span>{{ row.user.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="评论时间" width="160">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button-group>
              <el-button 
                size="small" 
                @click="handleView(row)"
              >
                查看
              </el-button>
              <el-button 
                v-if="row.status === 'normal'"
                type="warning" 
                size="small" 
                @click="handleHide(row)"
              >
                隐藏
              </el-button>
              <el-button 
                v-if="row.status === 'hidden'"
                type="success" 
                size="small" 
                @click="handleShow(row)"
              >
                显示
              </el-button>
              <el-button 
                type="danger" 
                size="small"
                @click="handleDelete(row)"
              >
                删除
              </el-button>
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

    <!-- 评论详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="评论详情"
      width="600px"
    >
      <div v-if="currentComment" class="comment-detail">
        <div class="detail-item">
          <label>评论内容：</label>
          <div class="content">{{ currentComment.content }}</div>
        </div>
        <div class="detail-item">
          <label>评论文章：</label>
          <el-link type="primary" @click="viewArticle(currentComment.article)">
            {{ currentComment.article.title }}
          </el-link>
        </div>
        <div class="detail-item">
          <label>评论用户：</label>
          <div class="user-info">
            <el-avatar :size="24" :src="currentComment.user.avatar" />
            <span>{{ currentComment.user.name }}</span>
          </div>
        </div>
        <div class="detail-item">
          <label>评论时间：</label>
          <span>{{ formatDate(currentComment.createTime) }}</span>
        </div>
        <div class="detail-item">
          <label>当前状态：</label>
          <el-tag :type="getStatusType(currentComment.status)">
            {{ getStatusText(currentComment.status) }}
          </el-tag>
        </div>
        <div v-if="currentComment.reportInfo" class="detail-item">
          <label>举报信息：</label>
          <div class="report-info">
            <p>举报原因：{{ currentComment.reportInfo.reason }}</p>
            <p>举报时间：{{ formatDate(currentComment.reportInfo.time) }}</p>
            <p>举报用户：{{ currentComment.reportInfo.user }}</p>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDate } from '../../utils/date'

const router = useRouter()

// 列表数据
const loading = ref(false)
const comments = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 筛选表单
const filterForm = ref({
  status: '',
  keyword: ''
})

// 查看相关
const viewDialogVisible = ref(false)
const currentComment = ref(null)

// 获取评论列表
const fetchComments = async () => {
  loading.value = true
  try {
    // 模拟API请求
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 模拟数据
    comments.value = [
      {
        id: 1,
        content: '这篇文章写得很好，对我帮助很大！',
        createTime: '2024-03-15 10:30:00',
        status: 'normal',
        user: {
          name: '张三',
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
        },
        article: {
          id: 1,
          title: 'Vue3 组件设计最佳实践'
        }
      },
      {
        id: 2,
        content: '这个观点我不太认同，建议作者多考虑一下其他场景。',
        createTime: '2024-03-14 15:20:00',
        status: 'reported',
        user: {
          name: '李四',
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
        },
        article: {
          id: 2,
          title: 'TypeScript 高级特性详解'
        },
        reportInfo: {
          reason: '评论带有争议性内容',
          time: '2024-03-14 16:00:00',
          user: '王五'
        }
      }
    ]
    total.value = comments.value.length
  } catch (error) {
    console.error('获取评论列表失败:', error)
    ElMessage.error('获取评论列表失败')
  } finally {
    loading.value = false
  }
}

// 状态相关方法
const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    normal: 'success',
    hidden: 'info',
    reported: 'warning'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    normal: '正常',
    hidden: '已隐藏',
    reported: '已举报'
  }
  return statusMap[status] || '未知'
}

// 查看评论
const handleView = (comment: any) => {
  currentComment.value = comment
  viewDialogVisible.value = true
}

// 查看文章
const viewArticle = (article: any) => {
  router.push(`/article/${article.id}`)
}

// 隐藏评论
const handleHide = async (comment: any) => {
  try {
    await ElMessageBox.confirm('确定要隐藏这条评论吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    comment.status = 'hidden'
    ElMessage.success('评论已隐藏')
  } catch {
    // 用户取消操作
  }
}

// 显示评论
const handleShow = async (comment: any) => {
  try {
    await ElMessageBox.confirm('确定要显示这条评论吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    comment.status = 'normal'
    ElMessage.success('评论已显示')
  } catch {
    // 用户取消操作
  }
}

// 删除评论
const handleDelete = async (comment: any) => {
  try {
    await ElMessageBox.confirm('确定要删除这条评论吗？此操作不可恢复！', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    comments.value = comments.value.filter(item => item.id !== comment.id)
    ElMessage.success('评论已删除')
  } catch {
    // 用户取消操作
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchComments()
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
  fetchComments()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchComments()
}

// 初始化
onMounted(() => {
  fetchComments()
})
</script>

<style scoped>
.comments-page {
  padding: 20px;
  background: #fff;
  border-radius: 4px;
}

.filter-section {
  margin-bottom: 20px;
}

.comments-table {
  margin-top: 20px;
}

.comment-content {
  .text {
    margin-bottom: 8px;
  }

  .article-link {
    font-size: 12px;
    color: #666;
  }
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.comment-detail {
  .detail-item {
    margin-bottom: 16px;

    label {
      font-weight: 500;
      margin-right: 8px;
      color: #666;
    }

    .content {
      margin-top: 8px;
      padding: 12px;
      background: #f5f7fa;
      border-radius: 4px;
    }

    .report-info {
      margin-top: 8px;
      padding: 12px;
      background: #fff5f5;
      border-radius: 4px;
      color: #f56c6c;

      p {
        margin: 4px 0;
      }
    }
  }
}
</style> 