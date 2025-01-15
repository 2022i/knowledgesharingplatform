<template>
  <div class="article-detail">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-wrapper">
      <el-skeleton :rows="10" animated />
    </div>
    
    <!-- 文章内容 -->
    <template v-else-if="article">
      <!-- 文章头部信息 -->
      <div class="article-header">
        <h1 class="article-title">{{ article.title }}</h1>
        <div class="article-meta">
          <div class="meta-left">
            <div class="author-info">
              <el-avatar :size="32" :src="article.Author?.avatar">
                {{ article.Author?.username?.charAt(0).toUpperCase() }}
              </el-avatar>
              <div class="author-name">
                <router-link :to="`/user/${article.Author?.id}`" @click.stop>
                  {{ article.Author?.username }}
                </router-link>
                <!-- 只有当当前用户不是作者时才显示关注按钮 -->
                <el-button
                  v-if="userId && article.Author?.id !== userId"
                  :type="article.Author?.followed ? 'success' : 'primary'"
                  size="small"
                  @click.stop="handleFollowAuthor"
                  :loading="followLoading"
                >
                  {{ article.Author?.followed ? '已关注' : '关注' }}
                </el-button>
              </div>
            </div>
            <el-tag size="small" type="info">{{ article.theme }}</el-tag>
            <span class="publish-time">{{ formatTime(article.createTime) }}</span>
          </div>
          <div class="meta-right">
            <span class="stat-item">
              <el-icon><View /></el-icon>
              {{ formatNumber(article.viewUserCount) }}
            </span>
          </div>
        </div>
      </div>

      <!-- 文章正文 -->
      <div class="article-content markdown-body" v-html="processContent(article.content)"></div>

      <!-- 文章底部操作栏 -->
      <div class="article-actions">
        <div class="action-buttons">
          <!-- 点赞 -->
          <el-button 
            :type="article.support ? 'primary' : 'default'"
            :icon="CaretTop"
            @click.stop="handleSupport"
          >
            {{ article.support ? '已点赞' : '点赞' }}
            <span class="count">{{ formatNumber(article.supportUserCount) }}</span>
          </el-button>

          <!-- 反对 -->
          <el-button
            :type="article.oppose ? 'danger' : 'default'"
            :icon="CaretBottom"
            @click.stop="handleOppose"
          >
            {{ article.oppose ? '已反对' : '反对' }}
            <span class="count">{{ formatNumber(article.opposeUserCount) }}</span>
          </el-button>

          <!-- 收藏 -->
          <el-button
            :type="article.collect ? 'warning' : 'default'"
            :icon="Star"
            @click.stop="handleCollect"
          >
            {{ article.collect ? '已收藏' : '收藏' }}
            <span class="count">{{ formatNumber(article.collectionUserCount) }}</span>
          </el-button>

          <!-- 分享 -->
          <el-button
            :icon="Share"
            @click.stop="handleShare"
          >
            分享
            <span class="count">{{ formatNumber(article.shareUserCount) }}</span>
          </el-button>
        </div>
      </div>

      <!-- 评论区 -->
      <div id="comments" class="comments-section">
        <ArticleComments :article-id="articleId" />
      </div>
    </template>

    <!-- 加载失败 -->
    <el-empty 
      v-else 
      description="文章加载失败" 
      :image-size="200"
    >
      <el-button type="primary" @click="loadArticle">重试</el-button>
    </el-empty>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { View, CaretTop, CaretBottom, Star, Share } from '@element-plus/icons-vue'
import request from '../utils/request'
import { useUserArticleStore } from '../store/userArticle'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'
import ArticleComments from '../components/ArticleComments.vue'
import { useAuth } from '../composables/useAuth'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

const route = useRoute()
const router = useRouter()
const articleStore = useUserArticleStore()
const { userId } = useAuth()
const followLoading = ref(false)

// 文章ID
const articleId = Number(route.params.id)

// 加载状态
const loading = ref(true)
// 文章数据
const article = ref<any>(null)

// 加载文章数据
const loadArticle = async () => {
  loading.value = true
  try {
    // 先记录浏览历史
    await articleStore.addViewRecord(articleId)
    console.log('浏览历史记录成功')

    const userId = Number(localStorage.getItem('userId')) || -1
    const response = await request.get('/server/getRenderedArticle', {
      params: { 
        articleId,
        userId
      }
    })

    console.log('=== 文章数据加载 ===');
    console.log('原始响应数据:', response.data);

    if (response.data) {
      // 处理作者信息
      const authorData = response.data.Author || response.data.author || {}
      const processedAuthor = {
        id: authorData.id || authorData.userId || null,
        username: authorData.username || authorData.name || '未知用户',
        avatar: authorData.avatar || '',
        followed: authorData.followed || false
      }

      // 处理文章基本信息
      article.value = {
        id: response.data.id,
        title: response.data.title || '无标题',
        content: response.data.content || '',
        theme: response.data.theme || '未分类',
        createTime: response.data.createTime || new Date().toISOString(),
        viewUserCount: response.data.viewUserCount || 0,
        supportUserCount: response.data.supportUserCount || 0,
        opposeUserCount: response.data.opposeUserCount || 0,
        collectionUserCount: response.data.collectionUserCount || 0,
        shareUserCount: response.data.shareUserCount || 0,
        support: response.data.support || false,
        oppose: response.data.oppose || false,
        collect: response.data.collect || false,
        relatedKnowledge: Array.isArray(response.data.relatedKnowledge) ? response.data.relatedKnowledge : [],
        Author: processedAuthor
      }

      console.log('处理后的文章数据:', article.value)
    } else {
      throw new Error('文章数据为空')
    }
  } catch (error: any) {
    console.error('文章加载失败:', error)
    if (error.message === '请先登录') {
      ElMessage.warning('登录后可以获得更好的阅读体验')
    } else {
      console.error('错误详情:', {
        message: error.message,
        response: error.response?.data,
        status: error.response?.status
      })
      ElMessage.error('文章加载失败，请重试')
    }
    article.value = null
  } finally {
    loading.value = false
  }
}

// 格式化数字
const formatNumber = (num: number | undefined) => {
  if (num === undefined || num === null) return '0'
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + 'w'
  }
  return num.toString()
}

// 格式化时间
const formatTime = (time: string) => {
  return dayjs(time).fromNow()
}

// 处理点赞
const handleSupport = async () => {
  try {
    await articleStore.toggleSupport(articleId)
    // 重新加载文章数据以更新状态
    loadArticle()
  } catch (error) {
    console.error('Failed to toggle support:', error)
    ElMessage.error('操作失败，请重试')
  }
}

// 处理反对
const handleOppose = async () => {
  try {
    await articleStore.toggleOppose(articleId)
    // 重新加载文章数据以更新状态
    loadArticle()
  } catch (error) {
    console.error('Failed to toggle oppose:', error)
    ElMessage.error('操作失败，请重试')
  }
}

// 处理收藏
const handleCollect = async () => {
  try {
    await articleStore.toggleCollect(articleId)
    // 重新加载文章数据以更新状态
    loadArticle()
  } catch (error) {
    console.error('Failed to toggle collect:', error)
    ElMessage.error('操作失败，请重试')
  }
}

// 处理分享
const handleShare = async () => {
  try {
    await articleStore.shareArticle(articleId)
    // 重新加载文章数据以更新状态
    loadArticle()
  } catch (error) {
    console.error('Failed to share article:', error)
    ElMessage.error('分享失败，请重试')
  }
}

// 添加默认图片的 Base64 编码
const DEFAULT_IMAGE = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMAAAADACAYAAABS3GwHAAAACXBIWXMAAAsTAAALEwEAmpwYAAAF0WlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNy4yLWMwMDAgNzkuMWI2NWE3OWI0LCAyMDIyLzA2LzEzLTIyOjAxOjAxICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdEV2dD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlRXZlbnQjIiB4bWxuczpkYz0iaHR0cDovL3B1cmwub3JnL2RjL2VsZW1lbnRzLzEuMS8iIHhtbG5zOnBob3Rvc2hvcD0iaHR0cDovL25zLmFkb2JlLmNvbS9waG90b3Nob3AvMS4wLyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgMjQuMCAoTWFjaW50b3NoKSIgeG1wOkNyZWF0ZURhdGU9IjIwMjMtMDEtMjVUMTY6NDA6NTgrMDg6MDAiIHhtcDpNZXRhZGF0YURhdGU9IjIwMjMtMDEtMjVUMTY6NDA6NTgrMDg6MDAiIHhtcDpNb2RpZnlEYXRlPSIyMDIzLTAxLTI1VDE2OjQwOjU4KzA4OjAwIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjY1ZWRkMjQzLTk5ZTAtNDM5Yy1hNmJlLTY3NjA2YmY1NDRhYiIgeG1wTU06RG9jdW1lbnRJRD0iYWRvYmU6ZG9jaWQ6cGhvdG9zaG9wOjY1ZWRkMjQzLTk5ZTAtNDM5Yy1hNmJlLTY3NjA2YmY1NDRhYiIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjY1ZWRkMjQzLTk5ZTAtNDM5Yy1hNmJlLTY3NjA2YmY1NDRhYiIgZGM6Zm9ybWF0PSJpbWFnZS9wbmciIHBob3Rvc2hvcDpDb2xvck1vZGU9IjMiPiA8eG1wTU06SGlzdG9yeT4gPHJkZjpTZXE+IDxyZGY6bGkgc3RFdnQ6YWN0aW9uPSJjcmVhdGVkIiBzdEV2dDppbnN0YW5jZUlEPSJ4bXAuaWlkOjY1ZWRkMjQzLTk5ZTAtNDM5Yy1hNmJlLTY3NjA2YmY1NDRhYiIgc3RFdnQ6d2hlbj0iMjAyMy0wMS0yNVQxNjo0MDo1OCswODowMCIgc3RFdnQ6c29mdHdhcmVBZ2VudD0iQWRvYmUgUGhvdG9zaG9wIDI0LjAgKE1hY2ludG9zaCkiLz4gPC9yZGY6U2VxPiA8L3htcE1NOkhpc3Rvcnk+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+Af/+/fz7+vn49/b19PPy8fDv7u3s6+rp6Ofm5eTj4uHg397d3Nva2djX1tXU09LR0M/OzczLysnIx8bFxMPCwcC/vr28u7q5uLe2tbSzsrGwr66trKuqqainpqWko6KhoJ+enZybmpmYl5aVlJOSkZCPjo2Mi4qJiIeGhYSDgoGAf359fHt6eXh3dnV0c3JxcG9ubWxramloZ2ZlZGNiYWBfXl1cW1pZWFdWVVRTUlFQT05NTEtKSUhHRkVEQ0JBQD8+PTw7Ojk4NzY1NDMyMTAvLi0sKyopKCcmJSQjIiEgHx4dHBsaGRgXFhUUExIREA8ODQwLCgkIBwYFBAMCAQAALAAAAAAyADIAAAL/jI+py+0Po5y02ouz3rz7D4biSJbmiabqyrbuC8fyTNf2jef6zvf+DwwKh8Si8YhMKpfMpvMJjUqn1Kr1is1qt9yu9wsOi8fksvmMTqvX7Lb7DY/L5/S6/Y7P6/f8vv8PGCg4SFhoeIiYqLjI2Oj4CBkpOUlZaXmJmam5ydnp+QkaKjpKWmp6ipqqusra6voKGys7S1tre4ubq7vL2+v7CxwsPExcbHyMnKy8zNzs/AwdLT1NXW19jZ2tvc3d7f0NHi4+Tl5ufo6err7O3u7+Dh8vP09fb3+Pn6+/z9/v/w8woMCBBAsaPIgwocKFDBs6fAgxosSJFCtavIgxo8aNs2A7evwIMqTIkSRLmjyJMqXKlSxbunwJM6bMmTRr2ryJM6fOnTx7+vwJNKjQoUSLGj2KNKnSpUybOn0KNarUqVSrWr2KNavWrVy7ev0KNqzYsWTLmj2LNq3atWzbun0LN67cuXTr2r2LN6/evXz7+v0LOLDgwYQLGz6MOLHixYwbO34MObLkyZQrW76MObPmzZw7e/4MOrTo0aRLmz6NOrXq1axbu34NO7bs2bRr276NO7fu3bx7+/4NPLjw4cSLGz+OPLny5cybO38OPbr06dSrW7+OPbv27dy7e/8OPrz48eTLmz+PPr369ezbu38PP778+fTr27+PP7/+/fz7+/8PYIACDkhggQYeiGCCCi7IYIMOPghhhBJOSGGFFl6IYYYabshhhx5+CGKIIo5IYokmnohiiiquyGKLLr4IY4wyzkhjjTbeiGOOOu7IY48+/ghkkEIOSWSRRh6JZJJKLslkk04+CWWUUk5JZZVWXollllpuyWWXXn4JZphijklmmWaeiWaaaq7JZptuvglnnHLOSWeddt6JZ5567slnn37+CWiggg5KaKGGHopoooouymijjj4KaaSSTkpppZZeimmmm/8Kaqiijkpqqaaeimqqqq7KaquuvgprrLLOSmuttt6Ka6667sprr77+Cmywwg5LbLHGHotsssouy2yzzj4LbbTSTkttNQUAADs='

const processContent = (content: string) => {
  if (!content) return ''
  
  const parser = new DOMParser()
  const doc = parser.parseFromString(content, 'text/html')
  
  // 处理图片
  doc.querySelectorAll('img').forEach(img => {
    // 添加延迟加载属性
    img.setAttribute('loading', 'lazy')
    // 添加错误处理
    img.setAttribute('onerror', `this.onerror=null;this.src='${DEFAULT_IMAGE}';this.classList.add('img-load-error')`)
    // 添加加载完成处理
    img.setAttribute('onload', `this.classList.add('img-loaded')`)
    
    // 设置图片样式
    img.style.maxWidth = '100%'
    img.style.height = 'auto'
    img.style.display = 'block'
    img.style.margin = '10px auto'
    
    // 阻止图片点击事件冒泡
    img.setAttribute('onclick', 'event.stopPropagation()')
  })
  
  // 处理链接
  doc.querySelectorAll('a').forEach(link => {
    // 阻止链接点击事件冒泡
    link.setAttribute('onclick', 'event.stopPropagation()')
  })
  
  return doc.body.innerHTML
}

// 添加相关样式
const style = document.createElement('style')
style.textContent = `
  .article-content img {
    max-width: 100%;
    height: auto;
    display: block;
    margin: 10px auto;
    border-radius: 4px;
    transition: opacity 0.3s;
    pointer-events: none;  /* 防止图片干扰点击事件 */
  }
  
  .article-content img.img-loaded {
    opacity: 1;
  }
  
  .article-content img.img-load-error {
    opacity: 0.6;
    max-height: 200px;
    object-fit: contain;
    background: #f5f5f5;
  }

  .article-content a {
    position: relative;
    z-index: 1;
  }
`
document.head.appendChild(style)

// 处理关注作者
const handleFollowAuthor = async () => {
  if (!article.value?.Author?.id) {
    console.error('作者ID不存在:', article.value?.Author);
    ElMessage.error('无法获取作者信息');
    return;
  }

  console.log('开始关注操作，当前状态：', {
    userId,
    authorId: article.value.Author.id,
    isFollowing: article.value.Author.followed
  });

  if (!userId) {
    console.log('用户未登录');
    ElMessage.warning('请先登录');
    router.push('/login');
    return;
  }

  if (article.value.Author.id === userId) {
    console.log('尝试关注自己');
    ElMessage.warning('不能关注自己');
    return;
  }

  try {
    followLoading.value = true;
    const isFollowing = article.value.Author.followed;
    const endpoint = isFollowing ? '/server/follow/unFollowUser' : '/server/follow/followUser';
    const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://127.0.0.1:8081';
    
    // 构建请求参数
    const params = {
      currentUserId: userId,
      followUserId: article.value.Author.id
    };
    
    console.log('=== 用户关注操作 ===');
    console.log('操作类型:', isFollowing ? '取消关注' : '关注');
    console.log('当前用户ID:', userId);
    console.log('目标作者ID:', article.value.Author.id);
    console.log('目标用户:', article.value.Author.username);
    console.log('请求路径:', endpoint);
    console.log('请求参数:', params);
    console.log('完整请求URL:', `${baseUrl}${endpoint}?currentUserId=${userId}&followUserId=${article.value.Author.id}`);

    const response = await request.put(endpoint, null, {
      params: params,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    });

    console.log('响应数据:', response.data);

    if (response.data.code === 200) {
      article.value.Author.followed = !isFollowing;
      ElMessage.success(isFollowing ? '已取消关注' : '关注成功');
    } else {
      console.error('服务器返回错误：', response.data);
      throw new Error(response.data.msg || '操作失败');
    }
  } catch (error: any) {
    console.error('关注操作失败，详细信息：', {
      error,
      message: error.message,
      response: error.response?.data,
      status: error.response?.status,
      config: error.config
    });

    // 处理特定错误
    if (error.response?.status === 403) {
      ElMessage.error('您没有权限执行此操作，请确保已登录并刷新页面');
    } else if (error.response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录');
      router.push('/login');
    } else if (error.code === 'ECONNABORTED') {
      ElMessage.error('请求超时，请检查网络连接');
    } else if (error.message.includes('Network Error')) {
      ElMessage.error('网络错误，请检查网络连接');
    } else {
      ElMessage.error(error.response?.data?.msg || error.message || '操作失败，请重试');
    }
  } finally {
    followLoading.value = false;
  }
}

// 组件挂载时加载文章数据
onMounted(() => {
  if (!articleId) {
    ElMessage.error('无效的文章ID')
    router.push('/')
    return
  }
  loadArticle()
})
</script>

<style scoped>
.article-detail {
  max-width: 900px;
  margin: 0 auto;
  padding: 24px;
  background: var(--el-bg-color);
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.loading-wrapper {
  padding: 24px;
}

.article-header {
  margin-bottom: 32px;
}

.article-title {
  margin: 0 0 16px;
  font-size: 28px;
  font-weight: 600;
  line-height: 1.4;
  color: var(--el-text-color-primary);
}

.article-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  position: relative;
  z-index: 2;
}

.meta-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.meta-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
  position: relative;
  z-index: 2;
}

.author-name {
  display: flex;
  align-items: center;
  gap: 12px;
  position: relative;
  z-index: 2;
}

.author-name a {
  color: var(--el-text-color-primary);
  text-decoration: none;
  font-weight: 500;
  position: relative;
  z-index: 2;
}

.author-name a:hover {
  color: var(--el-color-primary);
}

.publish-time {
  font-size: 14px;
  color: var(--el-text-color-secondary);
}

.knowledge-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 24px;
}

.article-content {
  margin-bottom: 32px;
  line-height: 1.8;
  overflow-wrap: break-word;
  word-wrap: break-word;
  word-break: break-word;
  max-width: 100%;
  position: relative;
  z-index: 1;
}

.article-actions {
  margin-bottom: 32px;
  padding: 16px 0;
  border-top: 1px solid var(--el-border-color-light);
  border-bottom: 1px solid var(--el-border-color-light);
  position: relative;
  z-index: 2;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 16px;
}

.action-buttons .el-button {
  min-width: 100px;
}

.count {
  margin-left: 4px;
  font-size: 14px;
  color: var(--el-text-color-secondary);
}

.comments-section {
  margin-top: 32px;
  position: relative;
  z-index: 2;
}

:deep(.article-content) {
  font-size: 16px;
  color: var(--el-text-color-primary);
  
  h1, h2, h3, h4, h5, h6 {
    margin: 24px 0 16px;
    font-weight: 600;
    line-height: 1.4;
  }
  
  h1 { font-size: 28px; }
  h2 { font-size: 24px; }
  h3 { font-size: 20px; }
  h4 { font-size: 18px; }
  h5, h6 { font-size: 16px; }

  p {
    margin: 16px 0;
    line-height: 1.8;
  }

  img {
    max-width: 100%;
    height: auto;
    border-radius: 4px;
    margin: 16px 0;
    pointer-events: none;
  }

  a {
    color: var(--el-color-primary);
    text-decoration: none;
    position: relative;
    z-index: 2;
    pointer-events: auto;
    &:hover {
      text-decoration: underline;
    }
  }

  ul, ol {
    margin: 16px 0;
    padding-left: 24px;
  }

  li {
    margin: 8px 0;
  }

  blockquote {
    margin: 16px 0;
    padding: 8px 16px;
    border-left: 4px solid var(--el-color-primary-light-7);
    background-color: var(--el-color-primary-light-9);
    color: var(--el-text-color-regular);
  }

  pre {
    margin: 16px 0;
    padding: 16px;
    background-color: var(--el-bg-color-page);
    border-radius: 4px;
    overflow-x: auto;
  }

  code {
    font-family: Monaco, Consolas, Courier New, monospace;
    font-size: 14px;
    padding: 2px 4px;
    background-color: var(--el-bg-color-page);
    border-radius: 2px;
  }

  table {
    width: 100%;
    margin: 16px 0;
    border-collapse: collapse;
  }

  th, td {
    padding: 8px;
    border: 1px solid var(--el-border-color);
    text-align: left;
  }

  th {
    background-color: var(--el-bg-color-page);
    font-weight: 600;
  }

  hr {
    margin: 24px 0;
    border: none;
    border-top: 1px solid var(--el-border-color);
  }
}

:deep(.markdown-body) {
  background: none;
}
</style>
