<template>
  <div class="article-comments">
    <h3 class="comments-title">评论 {{ total }}</h3>
    
    <!-- 评论输入框 -->
    <div class="comment-input">
      <el-avatar :size="40" :src="userAvatar">
        {{ username?.charAt(0).toUpperCase() }}
      </el-avatar>
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
          <el-button type="primary" @click="submitComment" :loading="submitting">
            发表评论
          </el-button>
        </div>
      </div>
    </div>

    <!-- 评论列表 -->
    <div v-if="loading" class="comments-loading">
      <el-skeleton :rows="3" animated />
    </div>
    <div v-else-if="comments.length" class="comments-list">
      <div v-for="comment in comments" :key="comment.id" class="comment-item">
        <div class="comment-header">
          <div class="comment-user">
            <el-avatar :size="32" :src="comment.user?.avatar">
              {{ comment.user?.username?.charAt(0).toUpperCase() }}
            </el-avatar>
            <span class="username">{{ comment.user?.username }}</span>
          </div>
          <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
        </div>
        <div class="comment-content">{{ comment.content }}</div>
        <div class="comment-actions">
          <el-button 
            link 
            type="primary" 
            size="small"
            @click="showReplyInput(comment.id)"
          >
            回复
          </el-button>
        </div>
        
        <!-- 回复输入框 -->
        <div v-if="activeReplyId === comment.id" class="reply-input">
          <el-input
            v-model="replyContent"
            type="textarea"
            :rows="2"
            placeholder="回复评论..."
            :maxlength="500"
            show-word-limit
          />
          <div class="reply-actions">
            <el-button @click="cancelReply">取消</el-button>
            <el-button 
              type="primary" 
              @click="submitReply(comment.id)"
              :loading="submitting"
            >
              回复
            </el-button>
          </div>
        </div>

        <!-- 回复列表 -->
        <div v-if="comment.replies?.length" class="replies-list">
          <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
            <div class="reply-header">
              <div class="reply-user">
                <el-avatar :size="24" :src="reply.user?.avatar">
                  {{ reply.user?.username?.charAt(0).toUpperCase() }}
                </el-avatar>
                <span class="username">{{ reply.user?.username }}</span>
              </div>
              <span class="reply-time">{{ formatTime(reply.createTime) }}</span>
            </div>
            <div class="reply-content">{{ reply.content }}</div>
          </div>
        </div>
      </div>
    </div>
    <el-empty v-else description="暂无评论" />

    <!-- 加载更多 -->
    <div v-if="hasMore" class="load-more">
      <el-button link @click="loadMore" :loading="loadingMore">
        加载更多评论
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

const props = defineProps<{
  articleId: number
}>()

// 状态
const loading = ref(false)
const submitting = ref(false)
const loadingMore = ref(false)
const comments = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const hasMore = ref(true)
const commentContent = ref('')
const replyContent = ref('')
const activeReplyId = ref<number | null>(null)

// 从 localStorage 获取用户信息
const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
const username = ref(userInfo.username)
const userAvatar = ref(userInfo.avatar)

// 加载评论列表
const loadComments = async (isLoadMore = false) => {
  if (!isLoadMore) {
    loading.value = true
  } else {
    loadingMore.value = true
  }

  try {
    // 获取所有评论
    const response = await request.get('/server/articleComment/getArticleAllComments', {
      params: {
        articleId: props.articleId
      }
    })

    console.log('All comments response:', response)

    if (Array.isArray(response.data)) {
      // 将评论数据转换为层级结构
      const commentMap = new Map() // 用于快速查找评论
      const rootComments = [] // 存储根评论

      // 第一次遍历：创建所有评论对象并存入 Map
      response.data.forEach(comment => {
        const commentObj = {
          ...comment,
          user: {
            id: comment.userId,
            username: comment.username,
            avatar: null // 后端暂未提供头像
          },
          replies: [] // 初始化回复数组
        }
        commentMap.set(comment.id, commentObj)
      })

      // 第二次遍历：构建评论层级关系
      response.data.forEach(comment => {
        const commentObj = commentMap.get(comment.id)
        if (comment.fatherId === 0) {
          // 这是根评论
          rootComments.push(commentObj)
        } else {
          // 这是回复评论，添加到父评论的 replies 数组中
          const parentComment = commentMap.get(comment.fatherId)
          if (parentComment) {
            parentComment.replies.push(commentObj)
          }
        }
      })

      // 更新评论列表
      if (isLoadMore) {
        comments.value.push(...rootComments)
      } else {
        comments.value = rootComments
      }
      total.value = rootComments.length
      hasMore.value = false // 暂时不支持分页
    } else {
      console.error('Invalid response format for comments:', response)
      throw new Error('评论数据格式错误')
    }
  } catch (error) {
    console.error('Failed to load comments:', error)
    ElMessage.error(error instanceof Error ? error.message : '评论加载失败，请重试')
  } finally {
    loading.value = false
    loadingMore.value = false
  }
}

// 提交评论
const submitComment = async () => {
  if (!commentContent.value.trim()) {
    return ElMessage.warning('请输入评论内容')
  }

  submitting.value = true
  try {
    const userId = Number(localStorage.getItem('userId'))
    if (!userId) {
      throw new Error('请先登录')
    }

    await request.post('/server/write/articleComment', {
      content: commentContent.value,
      userId,
      articleId: props.articleId,
      fatherId: 0 // 根评论的 fatherId 为 0
    })

    ElMessage.success('评论成功')
    commentContent.value = ''
    loadComments() // 重新加载评论列表
  } catch (error: any) {
    console.error('Failed to submit comment:', error)
    ElMessage.error(error.message || '评论失败，请重试')
  } finally {
    submitting.value = false
  }
}

// 显示回复输入框
const showReplyInput = (commentId: number) => {
  activeReplyId.value = commentId
  replyContent.value = ''
}

// 取消回复
const cancelReply = () => {
  activeReplyId.value = null
  replyContent.value = ''
}

// 提交回复
const submitReply = async (commentId: number) => {
  if (!replyContent.value.trim()) {
    return ElMessage.warning('请输入回复内容')
  }

  submitting.value = true
  try {
    const userId = Number(localStorage.getItem('userId'))
    if (!userId) {
      throw new Error('请先登录')
    }

    await request.post('/server/write/commentComment', {
      content: replyContent.value,
      userId,
      articleId: props.articleId,
      fatherId: commentId
    })

    ElMessage.success('回复成功')
    cancelReply()
    loadComments() // 重新加载评论列表
  } catch (error: any) {
    console.error('Failed to submit reply:', error)
    ElMessage.error(error.message || '回复失败，请重试')
  } finally {
    submitting.value = false
  }
}

// 加载更多评论
const loadMore = async () => {
  if (loadingMore.value) return
  page.value++
  await loadComments(true)
}

// 格式化时间
const formatTime = (time: string) => {
  return dayjs(time).fromNow()
}

// 组件挂载时加载评论
onMounted(() => {
  loadComments()
})
</script>

<style scoped>
.article-comments {
  padding: 24px 0;
}

.comments-title {
  margin: 0 0 24px;
  font-size: 20px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.comment-input {
  display: flex;
  gap: 16px;
  margin-bottom: 32px;
}

.input-wrapper {
  flex: 1;
}

.input-actions {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}

.comments-loading {
  padding: 16px 0;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.comment-item {
  padding: 16px;
  background: var(--el-fill-color-blank);
  border-radius: 8px;
  transition: background-color 0.3s;
}

.comment-item:hover {
  background: var(--el-fill-color-light);
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.comment-user {
  display: flex;
  align-items: center;
  gap: 8px;
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: var(--el-text-color-primary);
}

.comment-time {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.comment-content {
  font-size: 14px;
  line-height: 1.6;
  color: var(--el-text-color-primary);
  margin-bottom: 8px;
}

.comment-actions {
  display: flex;
  justify-content: flex-end;
}

.reply-input {
  margin: 16px 0;
  padding-left: 40px;
}

.reply-actions {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.replies-list {
  margin-top: 16px;
  padding-left: 40px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.reply-item {
  padding: 12px;
  background: var(--el-fill-color-light);
  border-radius: 4px;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.reply-user {
  display: flex;
  align-items: center;
  gap: 8px;
}

.reply-time {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.reply-content {
  font-size: 14px;
  line-height: 1.6;
  color: var(--el-text-color-primary);
}

.load-more {
  margin-top: 24px;
  text-align: center;
}
</style> 