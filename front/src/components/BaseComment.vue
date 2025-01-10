<template>
  <div class="base-comment" :class="{ 'is-reply': level > 0 }">
    <!-- 评论主体 -->
    <div class="comment-main">
      <el-avatar :size="mode === 'simple' ? 32 : 40" :src="comment.user?.avatar" />
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
            v-if="canReply"
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

    <!-- 子评论区域 -->
    <div v-if="childComments.length" class="comment-replies">
      <!-- 简单模式下最多显示3条子评论 -->
      <template v-if="mode === 'simple'">
        <BaseComment
          v-for="(reply, index) in displayedReplies"
          :key="reply.id"
          :comment="reply"
          :level="level + 1"
          :mode="mode"
          :max-level="maxLevel"
          @reply-added="handleReplyAdded"
        />
        <!-- 展开更多按钮 -->
        <div 
          v-if="hasMoreReplies" 
          class="expand-replies"
          @click="expandReplies"
        >
          <el-button link type="primary">
            展开{{ childComments.length - displayedReplies.length }}条回复
          </el-button>
        </div>
      </template>
      
      <!-- 完整模式下显示所有子评论 -->
      <template v-else>
        <BaseComment
          v-for="reply in childComments"
          :key="reply.id"
          :comment="reply"
          :level="level + 1"
          :mode="mode"
          :max-level="maxLevel"
          @reply-added="handleReplyAdded"
        />
      </template>
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
  childComments: Comment[]
  level?: number
  maxLevel?: number
  mode?: 'simple' | 'full' // 新增模式属性
}>()

const emit = defineEmits<{
  (e: 'reply-added', comment: Comment): void
  (e: 'expand-replies', comment: Comment): void
}>()

// 默认值
const level = computed(() => props.level || 0)
const maxLevel = computed(() => props.maxLevel || 3)
const mode = computed(() => props.mode || 'full')

// 是否可以回复（根据层级和模式判断）
const canReply = computed(() => {
  // 简单模式下只允许回复根评论
  if (mode.value === 'simple') {
    return level.value === 0
  }
  // 完整模式下只要不超过最大层级就可以回复
  return level.value < maxLevel.value
})

// 简单模式下的子评论显示逻辑
const isExpanded = ref(false)
const displayedReplies = computed(() => {
  if (mode.value === 'simple' && !isExpanded.value) {
    return props.childComments.slice(0, 3)
  }
  return props.childComments
})

const hasMoreReplies = computed(() => {
  return mode.value === 'simple' && 
         !isExpanded.value && 
         props.childComments.length > 3
})

// 展开更多回复
const expandReplies = () => {
  if (mode.value === 'simple') {
    emit('expand-replies', props.comment)
  } else {
    isExpanded.value = true
  }
}

// 回复相关状态和方法
const showReplyInput = ref(false)
const replyContent = ref('')

const handleReply = () => {
  showReplyInput.value = true
}

const submitReply = async () => {
  if (!replyContent.value.trim()) {
    return ElMessage.warning('请输入回复内容')
  }

  try {
    // TODO: 调用API提交回复
    const newComment: Comment = {
      id: Date.now(),
      fatherId: props.comment.id,
      userId: 1, // 当前用户ID
      articleId: props.comment.articleId,
      content: replyContent.value,
      createTime: new Date().toISOString(),
      // 不在这里设置用户信息，由父组件处理
      user: undefined
    }

    emit('reply-added', newComment)
    replyContent.value = ''
    showReplyInput.value = false
  } catch (error) {
    console.error('提交回复失败:', error)
    ElMessage.error('回复失败，请重试')
  }
}

const handleReplyAdded = (comment: Comment) => {
  emit('reply-added', comment)
}
</script>

<style scoped>
.base-comment {
  margin-bottom: 16px;
}

.comment-main {
  display: flex;
  gap: 12px;
}

.comment-content {
  flex: 1;
}

.comment-header {
  margin-bottom: 4px;
}

.username {
  font-weight: 500;
  margin-right: 8px;
  font-size: 14px;
}

.time {
  color: #999;
  font-size: 12px;
}

.comment-text {
  line-height: 1.6;
  font-size: 14px;
  margin-bottom: 4px;
}

.comment-actions {
  margin-top: 4px;
}

.reply-input {
  margin-top: 12px;
}

.reply-actions {
  margin-top: 8px;
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.comment-replies {
  margin-left: 44px;
  margin-top: 12px;
}

.is-reply {
  margin-bottom: 12px;
}

.is-reply:last-child {
  margin-bottom: 0;
}

.expand-replies {
  margin-top: 8px;
  cursor: pointer;
  color: var(--el-color-primary);
  font-size: 13px;
}

/* 简单模式的样式调整 */
:deep(.simple-mode) {
  font-size: 13px;
  
  .comment-main {
    gap: 8px;
  }
  
  .comment-replies {
    margin-left: 40px;
    margin-top: 8px;
  }
}
</style> 