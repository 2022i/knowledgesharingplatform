<template>
  <div class="comment-list">
    <!-- 评论输入框 -->
    <div class="comment-input">
      <el-avatar :size="40" :src="currentUser?.avatar" />
      <div class="input-wrapper">
        <el-input
          v-model="commentContent"
          type="textarea"
          :rows="2"
          placeholder="写下你的评论..."
          :maxlength="500"
          show-word-limit
        />
        <div class="input-actions">
          <el-button type="primary" @click="submitComment">发表评论</el-button>
        </div>
      </div>
    </div>

    <!-- 评论数量统计 -->
    <div class="comment-count">
      <span class="count">{{ totalComments }}条评论</span>
    </div>

    <!-- 评论列表 -->
    <div class="comments">
      <CommentItem
        v-for="comment in rootComments"
        :key="comment.id"
        :comment="comment"
        :child-comments="getChildComments(comment.id)"
        @reply-added="handleReplyAdded"
      />
    </div>

    <!-- 加载更多 -->
    <div v-if="hasMore" class="load-more">
      <el-button link @click="loadMore">加载更多评论</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import CommentItem from './CommentItem.vue'

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
  articleId: number
}>()

// 当前用户信息（实际项目中应该从用户状态获取）
const currentUser = ref<User>({
  id: 1,
  name: '当前用户',
  avatar: 'https://example.com/avatar.jpg'
})

// 评论相关状态
const commentContent = ref('')
const comments = ref<Comment[]>([])
const page = ref(1)
const pageSize = ref(20)
const hasMore = ref(true)

// 计算根评论（没有父评论的评论）
const rootComments = computed(() => {
  return comments.value.filter(comment => !comment.fatherId)
})

// 计算评论总数
const totalComments = computed(() => comments.value.length)

// 获取指定评论的子评论
const getChildComments = (parentId: number) => {
  return comments.value.filter(comment => comment.fatherId === parentId)
}

// 提交评论
const submitComment = async () => {
  if (!commentContent.value.trim()) {
    return ElMessage.warning('请输入评论内容')
  }

  try {
    // TODO: 调用API提交评论
    const newComment: Comment = {
      id: Date.now(), // 临时ID，实际应该由后端生成
      fatherId: null,
      userId: currentUser.value.id,
      articleId: props.articleId,
      content: commentContent.value,
      createTime: new Date().toISOString(),
      user: currentUser.value
    }

    // 添加到评论列表
    comments.value.unshift(newComment)
    
    // 清空输入框
    commentContent.value = ''
    ElMessage.success('评论成功')
  } catch (error) {
    console.error('提交评论失败:', error)
    ElMessage.error('评论失败，请重试')
  }
}

// 处理回复添加
const handleReplyAdded = (comment: Comment) => {
  comments.value.push(comment)
}

// 加载更多评论
const loadMore = async () => {
  try {
    // TODO: 调用API加载更多评论
    page.value++
    // 模拟没有更多数据
    if (page.value > 3) {
      hasMore.value = false
    }
  } catch (error) {
    console.error('加载更多评论失败:', error)
    ElMessage.error('加载失败，请重试')
  }
}

// 初始化加载评论
const loadComments = async () => {
  try {
    // TODO: 调用API获取评论列表
    // 这里使用模拟数据
    comments.value = [
      {
        id: 1,
        fatherId: null,
        userId: 2,
        articleId: props.articleId,
        content: '这是一条根评论',
        createTime: new Date().toISOString(),
        user: {
          id: 2,
          name: '用户A',
          avatar: 'https://example.com/avatar1.jpg'
        }
      },
      {
        id: 2,
        fatherId: 1,
        userId: 3,
        articleId: props.articleId,
        content: '这是对根评论的回复',
        createTime: new Date().toISOString(),
        user: {
          id: 3,
          name: '用户B',
          avatar: 'https://example.com/avatar2.jpg'
        }
      }
    ]
  } catch (error) {
    console.error('加载评论失败:', error)
    ElMessage.error('加载评论失败')
  }
}

// 初始化
loadComments()
</script>

<style scoped>
.comment-list {
  padding: 20px 0;
}

.comment-input {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
}

.input-wrapper {
  flex: 1;
}

.input-actions {
  margin-top: 8px;
  display: flex;
  justify-content: flex-end;
}

.comment-count {
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.count {
  font-size: 16px;
  font-weight: 500;
  color: #1a1a1a;
}

.comments {
  margin-bottom: 20px;
}

.load-more {
  text-align: center;
  margin-top: 20px;
}
</style> 