<template>
  <div class="favorites-container">
    <!-- 收藏夹管理 -->
    <div class="folders-section">
      <div class="section-header">
        <h3>收藏夹</h3>
        <el-button type="primary" link @click="showCreateFolder = true">
          <el-icon><Plus /></el-icon>创建收藏夹
        </el-button>
      </div>
      
      <!-- 收藏夹列表 -->
      <div class="folder-list">
        <div 
          v-for="folder in folders" 
          :key="folder.id"
          :class="['folder-item', { active: currentFolder === folder.id }]"
          @click="currentFolder = folder.id"
        >
          <div class="folder-info">
            <el-icon><Folder /></el-icon>
            <span class="folder-name">{{ folder.name }}</span>
            <span class="folder-count">({{ folder.count }})</span>
          </div>
          <el-dropdown trigger="click" @command="(cmd) => handleFolderAction(cmd, folder)">
            <el-button link>
              <el-icon><More /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="edit">
                  <el-icon><Edit /></el-icon>重命名
                </el-dropdown-item>
                <el-dropdown-item command="delete" divided>
                  <el-icon><Delete /></el-icon>删除收藏夹
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!-- 收藏文章列表 -->
    <div class="articles-section">
      <div class="section-header">
        <h3>{{ currentFolderName }}的文章</h3>
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

      <!-- 文章列表 -->
      <div class="article-list">
        <div v-for="article in articles" :key="article.id" class="article-item">
          <div class="article-main">
            <h3 class="article-title">
              <router-link :to="`/article/${article.id}`">{{ article.title }}</router-link>
            </h3>
            <p class="article-excerpt">{{ article.excerpt }}</p>
            <div class="article-meta">
              <el-avatar :size="24" :src="article.author.avatar" />
              <span class="author-name">{{ article.author.name }}</span>
              <span class="divider">·</span>
              <span>收藏于 {{ article.favoriteTime }}</span>
              <span class="divider">·</span>
              <span>{{ article.views }} 阅读</span>
            </div>
          </div>
          <div class="article-actions">
            <el-dropdown trigger="click" @command="(cmd) => handleArticleAction(cmd, article)">
              <el-button link>
                <el-icon><More /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="move">
                    <el-icon><FolderAdd /></el-icon>移动到其他收藏夹
                  </el-dropdown-item>
                  <el-dropdown-item command="remove" divided>
                    <el-icon><Delete /></el-icon>取消收藏
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
    </div>

    <!-- 创建收藏夹对话框 -->
    <el-dialog
      v-model="showCreateFolder"
      title="创建收藏夹"
      width="400px"
    >
      <el-form :model="folderForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="folderForm.name" placeholder="请输入收藏夹名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="folderForm.description"
            type="textarea"
            placeholder="请输入收藏夹描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showCreateFolder = false">取消</el-button>
          <el-button type="primary" @click="handleCreateFolder">
            创建
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { 
  Plus, 
  Folder, 
  More, 
  Edit, 
  Delete, 
  ArrowDown,
  FolderAdd 
} from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'

// 收藏夹相关
const currentFolder = ref(1)
const showCreateFolder = ref(false)
const folderForm = ref({
  name: '',
  description: ''
})

// 模拟收藏夹数据
const folders = ref([
  { id: 1, name: '默认收藏夹', count: 12 },
  { id: 2, name: '前端开发', count: 8 },
  { id: 3, name: '学习资料', count: 5 },
  { id: 4, name: '待读文章', count: 3 }
])

const currentFolderName = computed(() => {
  const folder = folders.value.find(f => f.id === currentFolder.value)
  return folder ? folder.name : ''
})

// 排序选项
const sorts = [
  { label: '收藏时间', value: 'time' },
  { label: '最多阅读', value: 'views' }
]
const currentSort = ref(sorts[0])

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(28)

// 模拟文章数据
const articles = ref([
  {
    id: 1,
    title: 'Vue3 组合式 API 最佳实践',
    excerpt: 'Vue3 的组合式 API 提供了更好的代码组织方式和逻辑复用能力。本文将介绍组合式 API 的最佳实践和常见陷阱...',
    author: {
      name: 'Vue专家',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
    },
    favoriteTime: '2024-03-15',
    views: 1234
  }
  // ... 更多文章数据
])

// 处理收藏夹操作
const handleFolderAction = async (command: string, folder: any) => {
  switch (command) {
    case 'edit':
      // TODO: 实现重命名功能
      break
    case 'delete':
      try {
        await ElMessageBox.confirm('确定要删除这个收藏夹吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        // TODO: 实现删除收藏夹功能
      } catch {
        // 用户取消操作
      }
      break
  }
}

// 处理文章操作
const handleArticleAction = async (command: string, article: any) => {
  switch (command) {
    case 'move':
      // TODO: 实现移动文章功能
      break
    case 'remove':
      try {
        await ElMessageBox.confirm('确定要取消收藏这篇文章吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        // TODO: 实现取消收藏功能
      } catch {
        // 用户取消操作
      }
      break
  }
}

// 处理创建收藏夹
const handleCreateFolder = () => {
  // TODO: 实现创建收藏夹功能
  showCreateFolder.value = false
}

// 处理排序
const handleSort = (value: string) => {
  currentSort.value = sorts.find(sort => sort.value === value) || sorts[0]
  // TODO: 重新加载文章列表
}

// 处理分页
const handlePageChange = (page: number) => {
  currentPage.value = page
  // TODO: 重新加载文章列表
}
</script>

<style scoped>
.favorites-container {
  display: flex;
  gap: 20px;
  min-height: 600px;
}

.folders-section {
  width: 240px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

.articles-section {
  flex: 1;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #f0f0f0;
}

.section-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.folder-list {
  padding: 12px 0;
}

.folder-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 24px;
  cursor: pointer;
}

.folder-item:hover {
  background-color: #f6f8fa;
}

.folder-item.active {
  background-color: #e5efff;
  color: #056de8;
}

.folder-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.folder-count {
  color: #8590a6;
  font-size: 13px;
}

/* 其他样式保持不变... */
</style>
