<template>
  <div class="likes-container">
    <div v-for="article in likes" :key="article.id" class="like-item">
      <div class="article-main">
        <h3 class="article-title">
          <router-link :to="`/article/${article.id}`">{{ article.title }}</router-link>
        </h3>
        <p class="article-excerpt">{{ article.excerpt }}</p>
        <div class="article-meta">
          <el-avatar :size="24" :src="article.author.avatar" />
          <span class="author-name">{{ article.author.name }}</span>
          <span>点赞于 {{ article.likeTime }}</span>
          <span>{{ article.views }} 阅读</span>
          <span>{{ article.likes }} 点赞</span>
        </div>
      </div>
      <div class="article-actions">
        <el-button 
          type="danger" 
          size="small" 
          @click="handleCancelLike(article)"
        >
          取消点赞
        </el-button>
      </div>
    </div>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        @current-change="handlePageChange"
        layout="prev, pager, next"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessageBox } from 'element-plus'

const likes = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

onMounted(async () => {
  await fetchLikes()
})

const fetchLikes = async () => {
  // TODO: 实现获取点赞列表逻辑
  // const response = await getUserLikes({
  //   page: currentPage.value,
  //   pageSize: pageSize.value
  // })
  // likes.value = response.data
  // total.value = response.total
}

const handleCancelLike = async (article: any) => {
  try {
    await ElMessageBox.confirm('确定要取消点赞这篇文章吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    // TODO: 实现取消点赞逻辑
    // await cancelLike(article.id)
    // await fetchLikes()
  } catch {
    // 用户取消操作
  }
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchLikes()
}
</script>

<style scoped>
.likes-container {
  padding: 20px 0;
}

.like-item {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.article-main {
  flex: 1;
  margin-right: 20px;
}

.article-title {
  margin: 0 0 10px;
  font-size: 18px;
}

.article-title a {
  color: #303133;
  text-decoration: none;
}

.article-title a:hover {
  color: #409eff;
}

.article-excerpt {
  color: #666;
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.article-meta {
  color: #999;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.author-name {
  color: #666;
}

.article-actions {
  display: flex;
  gap: 8px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
