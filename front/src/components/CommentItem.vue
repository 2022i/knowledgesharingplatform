<template>
  <div class="comment-item" :class="{ 'is-reply': level > 0 }">
    <!-- 评论主体 -->
    <div class="comment-main">
      <el-avatar :size="40" :src="comment.user?.avatar" />
      <div class="comment-content">
        <div class="comment-header">
          <span class="username">{{ comment.user?.name }}</span>
          <span class="time">{{ formatDate(comment.createTime) }}</span>
        </div>
        <div class="comment-text">{{ comment.content }}</div>
        <div class="comment-actions">
          <el-button 
            link 
            type="primary" 
            size="small"
            @click="handleReply"
            v-if="level < maxLevel"
          >
            回复
          </el-button>
        </div>

        <!-- 回复输入框 -->
        <div v-if="showReplyInput" class="reply-input">
          <el-input
            v-model="replyContent"
            type="textarea"
            :rows="2"
            placeholder="写下你的回复..."
            :maxlength="500"
            show-word-limit
          />
          <div class="reply-actions">
            <el-button @click="showReplyInput = false">取消</el-button>
            <el-button type="primary" @click="submitReply">回复</el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 递归渲染子评论 -->
    <div v-if="childComments.length" class="comment-replies">
      <CommentItem
        v-for="reply in childComments"
        :key="reply.id"
        :comment="reply"
        :level="level + 1"
        :max-level="maxLevel"
        @reply-added="handleReplyAdded"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { formatDate } from '../utils/date'
import { ElMessage } from 'element-plus'

interface User {
  id: number
  name: string
  avatar?: string
}

interface Comment {
  id: number
  fatherId: number | null
  userId: number
  articleId: number
  content: string
  createTime: string
  user?: User
}

const props = defineProps<{
  comment: Comment
  level?: number
  maxLevel?: number
  childComments?: Comment[]
}>()

const emit = defineEmits<{
  (e: 'reply-added', comment: Comment): void
}>()

// 默认值
const level = computed(() => props.level || 0)
const maxLevel = computed(() => props.maxLevel || 3)

// 回复相关状态
const showReplyInput = ref(false)
const replyContent = ref('')

// 处理回复按钮点击
const handleReply = () => {
  showReplyInput.value = true
}

// 提交回复
const submitReply = async () => {
  if (!replyContent.value.trim()) {
    return ElMessage.warning('请输入回复内容')
  }

  try {
    // TODO: 调用API提交回复
    const newComment: Comment = {
      id: Date.now(), // 临时ID，实际应该由后端生成
      fatherId: props.comment.id,
      userId: 1, // 当前用户ID
      articleId: props.comment.articleId,
      content: replyContent.value,
      createTime: new Date().toISOString(),
      user: {
        id: 1,
        name: '当前用户',
        avatar: 'https://example.com/avatar.jpg'
      }
    }

    // 触发回复添加事件
    emit('reply-added', newComment)
    
    // 清理状态
    replyContent.value = ''
    showReplyInput.value = false
    ElMessage.success('回复成功')
  } catch (error) {
    console.error('提交回复失败:', error)
    ElMessage.error('回复失败，请重试')
  }
}

// 处理子评论的回复
const handleReplyAdded = (comment: Comment) => {
  emit('reply-added', comment)
}
</script>

<style scoped>
.comment-item {
  margin-bottom: 20px;
}

.comment-main {
  display: flex;
  gap: 16px;
}

.comment-content {
  flex: 1;
}

.comment-header {
  margin-bottom: 8px;
}

.username {
  font-weight: 500;
  margin-right: 8px;
}

.time {
  color: #999;
  font-size: 12px;
}

.comment-text {
  line-height: 1.6;
  margin-bottom: 8px;
}

.comment-actions {
  margin-top: 8px;
}

.reply-input {
  margin-top: 16px;
}

.reply-actions {
  margin-top: 8px;
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.comment-replies {
  margin-left: 56px;
  margin-top: 16px;
}

.is-reply {
  margin-bottom: 16px;
}

.is-reply:last-child {
  margin-bottom: 0;
}
</style> 