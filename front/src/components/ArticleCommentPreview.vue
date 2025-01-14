<template>
  <div class="article-comment-preview">
    <!-- 评论图标和数量 -->
    <div class="comment-trigger" @click="toggleComments">
      <el-badge :value="commentCount" :hidden="!commentCount">
        <el-button link>
          <el-icon><ChatLineRound /></el-icon>
          评论
        </el-button>
      </el-badge>
    </div>

    <!-- 评论预览区域 -->
    <div v-if="showComments" class="comment-preview">
      <!-- 评论列表 -->
      <div v-if="comments.length" class="preview-list">
        <BaseComment
          v-for="comment in topComments"
          :key="comment.id"
          :comment="comment"
          :child-comments="getChildComments(comment.id)"
          mode="simple"
          @reply-added="handleReplyAdded"
          @expand-replies="handleExpandReplies"
        />
      </div>
      
      <!-- 暂无评论 -->
      <div v-else class="no-comments">
        暂无评论，快来抢沙发吧~
      </div>

      <!-- 查看全部评论 -->
      <div v-if="commentCount > 3" class="view-all">
        <el-button link type="primary" @click="viewAllComments">
          查看全部{{ commentCount }}条评论
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ChatLineRound } from '@element-plus/icons-vue'
import { useCommentStore } from '../store/comment'
import type { Comment } from '../store/comment'
import BaseComment from './BaseComment.vue'

interface Props {
  articleId: number
}

const props = defineProps<Props>()
const router = useRouter()
const commentStore = useCommentStore()

// 控制评论预览的显示
const showComments = ref(false)

// 获取前3条根评论
const topComments = computed(() => {
  return commentStore.getRootComments(props.articleId).slice(0, 3)
})

// 获取评论总数
const commentCount = computed(() => {
  return commentStore.getCommentCount(props.articleId)
})

// 切换评论显示
const toggleComments = async () => {
  if (!showComments.value) {
    await commentStore.fetchArticleRootComments(props.articleId)
  }
  showComments.value = !showComments.value
}

// 查看全部评论
const viewAllComments = () => {
  router.push(`/article/${props.articleId}`)
}

// 处理展开更多回复
const handleExpandReplies = (comment: Comment) => {
  // 点击展开更多时直接跳转到文章详情页
  router.push(`/article/${props.articleId}`)
}

const emit = defineEmits<{
  (e: 'reply-added', comment: any): void
}>()
</script>

<style scoped>
.article-comment-preview {
  margin-top: 12px;
}

.comment-trigger {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 8px;
}

.comment-preview {
  background-color: #f5f7fa;
  border-radius: 4px;
  padding: 16px;
  margin-top: 8px;
}

.preview-list {
  margin-bottom: 12px;
}

.no-comments {
  text-align: center;
  color: #8590a6;
  padding: 20px 0;
}

.view-all {
  text-align: center;
  border-top: 1px solid #e4e7ed;
  padding-top: 12px;
  margin-top: 12px;
}

:deep(.el-badge__content) {
  transform: scale(0.8);
}
</style> 