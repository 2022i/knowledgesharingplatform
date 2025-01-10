<template>
  <div class="comments-container">
    <!-- 筛选和排序 -->
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

    <!-- 评论列表 -->
    <div class="comment-list">
      <div v-for="comment in comments" :key="comment.id" class="comment-item">
        <!-- 评论内容 -->
        <div class="comment-main">
          <!-- 文章信息 -->
          <div class="article-info">
            <router-link :to="`/article/${comment.articleId}`" class="article-title">
              {{ comment.articleTitle }}
            </router-link>
            <span class="comment-time">{{ comment.publishTime }}</span>
          </div>

          <!-- 评论内容 -->
          <div class="comment-content">
            <p class="comment-text">{{ comment.content }}</p>
            <div class="comment-stats">
              <span class="likes">
                <el-button type="text">
                  <el-icon><CaretTop /></el-icon>
                  {{ comment.likes }}
                </el-button>
              </span>
              <span class="replies">
                <el-icon><ChatDotRound /></el-icon>
                {{ comment.replies }}
              </span>
            </div>
          </div>

          <!-- 评论回复，如果有的话 -->
          <div v-if="comment.replyTo" class="reply-info">
            <span class="reply-label">回复</span>
            <el-avatar :size="16" :src="comment.replyTo.avatar" />
            <span class="reply-name">{{ comment.replyTo.name }}</span>
            <span class="reply-content">：{{ comment.replyTo.content }}</span>
          </div>
        </div>

        <!-- 评论操作 -->
        <div class="comment-actions">
          <el-dropdown trigger="click" @command="(cmd) => handleAction(cmd, comment)">
            <el-button link>
              <el-icon><More /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="edit">
                  <el-icon><Edit /></el-icon>编辑评论
                </el-dropdown-item>
                <el-dropdown-item command="delete" divided>
                  <el-icon><Delete /></el-icon>删除评论
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!-- 分页器 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="handlePageChange"
        background
      />
    </div>

    <!-- 编辑评论对话框 -->
    <el-dialog
      v-model="showEditDialog"
      title="编辑评论"
      width="500px"
    >
      <el-input
        v-model="editForm.content"
        type="textarea"
        :rows="4"
        placeholder="请输入评论内容"
      />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showEditDialog = false">取消</el-button>
          <el-button type="primary" @click="handleSaveEdit">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { 
  ArrowDown, 
  More, 
  Edit, 
  Delete,
  ThumbsUp,
  ChatDotRound,
  CaretTop,
  Share
} from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'

// 筛选选项
const filters = [
  { label: '全部评论', value: 'all' },
  { label: '我发出的', value: 'sent' },
  { label: '我收到的', value: 'received' }
]
const currentFilter = ref('all')

// 排序选项
const sorts = [
  { label: '最新发布', value: 'newest' },
  { label: '最多点赞', value: 'likes' }
]
const currentSort = ref(sorts[0])

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(96)

// 编辑相关
const showEditDialog = ref(false)
const editForm = ref({
  id: null,
  content: ''
})

// 模拟评论数据
const comments = ref([
  {
    id: 1,
    articleId: 1,
    articleTitle: 'Vue3 组合式 API 最佳实践',
    content: '这篇文章写得非常好，对组合式 API 的解释很清晰，特别是关于响应式数据的部分...',
    publishTime: '2024-03-15 14:30',
    likes: 12,
    replies: 3,
    replyTo: {
      name: '前端达人',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      content: '组合式 API 确实比选项式 API 更灵活'
    }
  }
  // ... 更多评论数据
])

// 处理排序
const handleSort = (value: string) => {
  currentSort.value = sorts.find(sort => sort.value === value) || sorts[0]
  // TODO: 重新加载评论列表
}

// 处理评论操作
const handleAction = async (command: string, comment: any) => {
  switch (command) {
    case 'edit':
      editForm.value = {
        id: comment.id,
        content: comment.content
      }
      showEditDialog.value = true
      break
    case 'delete':
      try {
        await ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        // TODO: 实现删除评论功能
      } catch {
        // 用户取消操作
      }
      break
  }
}

// 保存编辑
const handleSaveEdit = async () => {
  // TODO: 实现保存编辑功能
  showEditDialog.value = false
}

// 处理分页
const handlePageChange = (page: number) => {
  currentPage.value = page
  // TODO: 重新加载评论列表
}
</script>

<style scoped>
.comments-container {
  background: #fff;
  border-radius: 8px;
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
}

.filter-item:hover {
  color: #121212;
}

.filter-item.active {
  color: #121212;
  font-weight: 500;
}

.sort-trigger {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #8590a6;
  cursor: pointer;
}

.comment-list {
  padding: 0 24px;
}

.comment-item {
  display: flex;
  justify-content: space-between;
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
}

.comment-main {
  flex: 1;
  margin-right: 16px;
}

.article-info {
  margin-bottom: 8px;
}

.article-title {
  color: #121212;
  text-decoration: none;
  font-weight: 500;
}

.article-title:hover {
  color: #056de8;
}

.comment-time {
  margin-left: 12px;
  color: #8590a6;
  font-size: 14px;
}

.comment-content {
  margin-bottom: 12px;
}

.comment-text {
  color: #121212;
  margin: 0 0 8px;
  line-height: 1.6;
}

.comment-stats {
  display: flex;
  gap: 16px;
  color: #8590a6;
  font-size: 14px;
}

.comment-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.reply-info {
  background: #f6f8fa;
  padding: 8px 12px;
  border-radius: 4px;
  font-size: 14px;
  color: #8590a6;
  display: flex;
  align-items: center;
  gap: 8px;
}

.reply-name {
  color: #121212;
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
