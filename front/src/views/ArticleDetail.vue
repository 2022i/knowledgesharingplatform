<template>
  <div class="article-detail">
    <div class="article-main">
      <div class="article-header">
        <h1 class="article-title">{{ article.title }}</h1>
        <div class="article-meta">
          <div class="author-info">
            <el-avatar :size="40" :src="article.author.avatar" />
            <div class="author-detail">
              <div class="author-name">{{ article.author.name }}</div>
              <div class="publish-time">
                发布于 {{ article.createTime }}
                <span v-if="article.updateTime">· 更新于 {{ article.updateTime }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="article-content" v-html="article.content"></div>

      <!-- 评论区域 -->
      <div id="comments" class="article-comments">
        <h3 class="comments-title">评论 {{ article.comments?.length || 0 }}</h3>
        
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

        <!-- 评论列表 -->
        <div class="comments-list">
          <BaseComment
            v-for="comment in rootComments"
            :key="comment.id"
            :comment="comment"
            :child-comments="getChildComments(comment.id)"
            mode="full"
            @reply-added="handleReplyAdded"
          />
        </div>

        <!-- 加载更多 -->
        <div v-if="hasMore" class="load-more">
          <el-button link @click="loadMore">加载更多评论</el-button>
        </div>
      </div>
    </div>

    <div class="article-sidebar">
      <div class="author-card">
        <div class="author-header">
          <el-avatar :size="50" :src="article.author.avatar" />
          <div class="author-info">
            <div class="author-name">{{ article.author.name }}</div>
            <div class="author-bio">{{ article.author.bio || '这个作者很懒，还没有写简介' }}</div>
          </div>
        </div>
        <div class="author-stats">
          <div class="stat-item">
            <div class="stat-value">{{ authorArticleCount }}</div>
            <div class="stat-label">文章</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ authorFansCount }}</div>
            <div class="stat-label">粉丝</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ authorTotalLikes }}</div>
            <div class="stat-label">获赞</div>
          </div>
        </div>
        <el-button 
          type="primary" 
          class="follow-button"
          :class="{ 'is-following': isFollowing }"
          @click="handleFollow"
        >
          {{ isFollowing ? '已关注' : '关注作者' }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useArticleStore } from '../store/article'
import type { Article } from '../types/article'
import BaseComment from '../components/BaseComment.vue'

const route = useRoute()
const router = useRouter()
const articleStore = useArticleStore()

// 文章数据
const article = ref<Article>({
  id: 0,
  title: '',
  content: '',
  excerpt: '',
  createTime: '',
  authorId: 0,
  themeId: 0,
  relatedKnowledgeId: [],
  collectionUserIds: [],
  shareUserIds: [],
  supportUserIds: [],
  opposeUserIds: [],
  viewUserIds: [],
  commentIds: [],
  comments: [],
  author: {
    id: 0,
    name: '',
    avatar: '',
    bio: '',
    writeArticleIds: [],
    fansId: [],
    articles: 0,
    followers: 0,
    likes: 0
  }
})

// 加载文章数据
const loadArticle = async () => {
  const articleId = Number(route.params.id)
  if (!articleId) return

  // 从 store 中查找文章
  const foundArticle = articleStore.articles.find(a => a.id === articleId) ||
                      articleStore.draftArticles.find(a => a.id === articleId)
  
  if (!foundArticle) {
    // 如果找不到文章，跳转到 404 页面
    router.push('/404')
    return
  }

  // 如果是草稿且不是作者本人，不允许查看
  if (foundArticle.status === 'draft' && foundArticle.author.id !== 1) { // TODO: 替换为实际的用户ID
    router.push('/404')
    return
  }

  article.value = {
    ...foundArticle,
    favorites: 1234 // 模拟数据
  }
}

// 评论相关状态和方法
const commentContent = ref('')
const currentUser = ref({
  id: 1,
  username: '当前用户',
  followUserId: [] as number[]
})

// 检查是否已关注作者
const isFollowing = computed(() => {
  return currentUser.value.followUserId.includes(article.value.author.id)
})

// 获取作者的文章总数
const authorArticleCount = computed(() => {
  return article.value.author.writeArticleIds?.length || 0
})

// 获取作者的粉丝数量
const authorFansCount = computed(() => {
  return article.value.author.fansId?.length || 0
})

// 获取作者获得的总点赞数
const authorTotalLikes = computed(() => {
  // 这里应该是从所有文章中统计点赞数
  // 暂时返回模拟数据
  return 1234
})

// 处理关注作者
const handleFollow = () => {
  const authorId = article.value.author.id
  
  if (isFollowing.value) {
    // 取消关注
    currentUser.value.followUserId = currentUser.value.followUserId.filter(id => id !== authorId)
    article.value.author.fansId = article.value.author.fansId.filter(id => id !== currentUser.value.id)
    ElMessage.success('已取消关注')
  } else {
    // 添加关注
    currentUser.value.followUserId.push(authorId)
    if (!article.value.author.fansId) {
      article.value.author.fansId = []
    }
    article.value.author.fansId.push(currentUser.value.id)
    ElMessage.success('关注成功')
  }
}

// 分页相关
const page = ref(1)
const pageSize = ref(20)
const hasMore = ref(true)

// 获取根评论
const rootComments = computed(() => {
  const comments = article.value.comments
  return Array.isArray(comments) ? comments.filter(comment => !comment.fatherId) : []
})

// 获取子评论
const getChildComments = (parentId: number) => {
  const comments = article.value.comments
  return Array.isArray(comments) ? comments.filter(comment => comment.fatherId === parentId) : []
}

// 提交评论
const submitComment = async () => {
  if (!commentContent.value.trim()) {
    return ElMessage.warning('请输入评论内容')
  }

  try {
    // TODO: 调用API提交评论
    const newComment = {
      id: Date.now(),
      fatherId: null,
      userId: currentUser.value.id,
      articleId: article.value.id,
      content: commentContent.value,
      createTime: new Date().toISOString(),
      user: currentUser.value
    }

    // 确保 comments 是数组
    if (!Array.isArray(article.value.comments)) {
      article.value.comments = []
    }

    // 添加到评论列表
    article.value.comments.unshift(newComment)
    
    // 更新评论数量
    if (article.value.commentCount !== undefined) {
      article.value.commentCount++
    }
    
    // 清空输入框
    commentContent.value = ''
    ElMessage.success('评论成功')
  } catch (error) {
    console.error('提交评论失败:', error)
    ElMessage.error('评论失败，请重试')
  }
}

// 处理回复
const handleReplyAdded = (comment: any) => {
  if (!Array.isArray(article.value.comments)) {
    article.value.comments = []
  }
  
  // 添加用户信息到评论
  const newComment = {
    ...comment,
    user: currentUser.value
  }
  
  // 添加到评论列表
  article.value.comments.push(newComment)
  
  // 更新评论数量
  if (article.value.commentCount !== undefined) {
    article.value.commentCount++
  }
  
  // 显示成功提示
  ElMessage.success('回复成功')
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

onMounted(() => {
  loadArticle()
})
</script>

<style scoped>
.article-detail {
  max-width: 1140px;
  margin: 0 auto;
  padding: 20px;
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 20px;
}

.article-main {
  background: #fff;
  padding: 32px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

.article-title {
  font-size: 28px;
  font-weight: 600;
  margin: 0 0 24px;
  color: #121212;
}

.article-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-detail {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.author-name {
  font-size: 16px;
  font-weight: 500;
  color: #121212;
}

.publish-time {
  font-size: 14px;
  color: #8590a6;
}

.article-content {
  font-size: 16px;
  line-height: 1.8;
  color: #121212;
}

.article-footer {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #f0f0f0;
}

.article-tags {
  display: flex;
  gap: 8px;
}

.article-sidebar {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.author-card {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

.author-header {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.author-bio {
  font-size: 14px;
  color: #8590a6;
  margin-top: 4px;
  line-height: 1.5;
}

.author-stats {
  display: flex;
  justify-content: space-around;
  margin: 20px 0;
  text-align: center;
}

.stat-item {
  flex: 1;
  padding: 0 8px;
  border-right: 1px solid #f0f0f0;
}

.stat-item:last-child {
  border-right: none;
}

.stat-value {
  font-size: 18px;
  font-weight: 600;
  color: #121212;
}

.stat-label {
  font-size: 14px;
  color: #8590a6;
  margin-top: 4px;
}

.follow-button {
  width: 100%;
  transition: all 0.3s;
}

.follow-button.is-following {
  background-color: #f0f0f0;
  border-color: #f0f0f0;
  color: #8590a6;
}

.follow-button.is-following:hover {
  background-color: #ff4d4f;
  border-color: #ff4d4f;
  color: #fff;
}

.follow-button.is-following:hover::after {
  content: '取消关注';
}

.article-comments {
  margin-top: 40px;
  padding-top: 32px;
  border-top: 1px solid #f0f0f0;
}

.comments-title {
  font-size: 18px;
  font-weight: 600;
  color: #121212;
  margin: 0 0 24px;
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

.comments-list {
  margin-bottom: 24px;
}

.load-more {
  text-align: center;
  margin-top: 24px;
}
</style>
