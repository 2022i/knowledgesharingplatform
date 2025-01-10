<template>
  <div class="editor-page">
    <div class="editor-header" v-show="!isFullscreen">
      <el-button @click="handleBack">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <div class="header-actions">
        <el-button @click="handleSaveDraft">保存草稿</el-button>
        <el-button type="primary" @click="handleSubmit">发布文章</el-button>
      </div>
    </div>

    <div class="editor-container" :class="{ 'is-fullscreen': isFullscreen }">
      <el-form :model="articleForm" class="editor-form">
        <div class="title-section">
          <el-form-item>
            <el-input
              v-model="articleForm.title"
              placeholder="请输入文章标题"
              class="title-input"
            />
          </el-form-item>
          <el-form-item>
            <el-select
              v-model="articleForm.category"
              placeholder="选择文章分类"
              class="category-select"
            >
              <el-option
                v-for="item in categories"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </div>
        
        <el-form-item>
          <div class="editor-wrapper">
            <Toolbar
              style="border-bottom: 1px solid #dcdfe6"
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              mode="default"
            />
            <Editor
              style="height: 500px; overflow-y: hidden;"
              v-model="articleForm.content"
              :defaultConfig="editorConfig"
              mode="default"
              @onCreated="handleCreated"
              @onFullScreen="handleFullScreen"
            />
          </div>
        </el-form-item>
      </el-form>
    </div>

    <!-- 文章概要对话框 -->
    <el-dialog
      v-model="summaryDialogVisible"
      title="文章概要"
      width="600px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
    >
      <div class="summary-content">
        <p class="summary-tip">系统已根据文章内容生成以下概要，您可以进行修改：</p>
        <el-input
          v-model="articleForm.summary"
          type="textarea"
          :rows="4"
          placeholder="请输入文章概要"
        />
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="handleConfirmSummary">下一步</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 知识标签对话框 -->
    <el-dialog
      v-model="tagsDialogVisible"
      title="知识标签"
      width="600px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
    >
      <div class="tags-content">
        <p class="tags-tip">系统已根据文章内容生成以下知识标签，您可以进行修改：</p>
        <div class="tags-list">
          <el-tag
            v-for="tag in articleForm.tags"
            :key="tag"
            class="tag-item"
            closable
            @close="handleRemoveTag(tag)"
          >
            {{ tag }}
          </el-tag>
        </div>
        <div class="add-tag">
          <el-input
            v-if="inputTagVisible"
            ref="tagInputRef"
            v-model="inputTagValue"
            class="tag-input"
            size="small"
            @keyup.enter="handleAddTag"
            @blur="handleAddTag"
          />
          <el-button 
            v-else 
            class="button-new-tag" 
            size="small" 
            @click="showTagInput"
          >
            + 添加标签
          </el-button>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="backToSummary">上一步</el-button>
          <el-button type="primary" @click="handlePublish">发布文章</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
import { ArrowLeft } from '@element-plus/icons-vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useArticleStore } from '../store/article'
import type { Article } from '../types/article'

const router = useRouter()
const route = useRoute()
const articleStore = useArticleStore()

// 文章分类选项
const categories = [
  { label: '前端开发', value: 'frontend' },
  { label: '后端开发', value: 'backend' },
  { label: '移动开发', value: 'mobile' },
  { label: '人工智能', value: 'ai' },
  { label: '运维部署', value: 'devops' },
  { label: '数据库', value: 'database' },
  { label: '编程语言', value: 'programming' },
  { label: '架构设计', value: 'architecture' },
  { label: '其他', value: 'other' }
]

const isFullscreen = ref(false)
const summaryDialogVisible = ref(false)
const tagsDialogVisible = ref(false)
const inputTagVisible = ref(false)
const inputTagValue = ref('')
const tagInputRef = ref()
const articleForm = ref({
  title: '',
  content: '',
  category: '',
  tags: [],
  summary: ''
})

const editorRef = ref()

// 自动保存定时器
let autoSaveTimer: number | null = null

// 如果是编辑模式，加载文章内容
onMounted(async () => {
  const articleId = route.params.id
  if (articleId) {
    // TODO: 从API获取文章详情
    const article = articleStore.articles.find(a => a.id === Number(articleId)) ||
                   articleStore.draftArticles.find(a => a.id === Number(articleId))
    if (article) {
      articleForm.value = {
        title: article.title,
        content: article.content || '',
        category: article.category || '',
        tags: article.tags || [],
        summary: article.summary || ''
      }
    }
  }

  // 设置自动保存
  autoSaveTimer = window.setInterval(async () => {
    if (articleForm.value.title || articleForm.value.content) {
      await handleSaveDraft()
    }
  }, 60000) // 每分钟自动保存
})

// 组件卸载时清除定时器
onUnmounted(() => {
  if (autoSaveTimer) {
    clearInterval(autoSaveTimer)
  }
})

// 返回上一页
const handleBack = () => {
  router.back()
}

// 保存草稿
const handleSaveDraft = async () => {
  try {
    const draft = await articleStore.saveDraft({
      title: articleForm.value.title,
      content: articleForm.value.content,
      category: articleForm.value.category,
      tags: articleForm.value.tags,
      summary: articleForm.value.summary
    })
    ElMessage.success('草稿已保存')
    return draft
  } catch (error) {
    ElMessage.error('保存草稿失败')
    console.error(error)
  }
}

// 发布文章
const handleSubmit = async () => {
  if (!articleForm.value.title.trim()) {
    return ElMessage.warning('请输入文章标题')
  }
  if (!articleForm.value.category) {
    return ElMessage.warning('请选择文章分类')
  }
  if (!articleForm.value.content.trim()) {
    return ElMessage.warning('请输入文章内容')
  }

  try {
    // 调用后端接口生成文章概要
    const summary = await generateSummary(articleForm.value.content)
    articleForm.value.summary = summary
    
    // 显示概要编辑对话框
    summaryDialogVisible.value = true
  } catch (error) {
    ElMessage.error('生成文章概要失败')
    console.error(error)
  }
}

// 生成文章概要（模拟API调用）
const generateSummary = async (content: string) => {
  // 模拟API调用延迟
  await new Promise(resolve => setTimeout(resolve, 1000))
  return '这是一段自动生成的文章概要，实际项目中应该通过调用后端接口来获取。您可以根据需要修改这段内容，使其更好地概括文章的主要内容。'
}

// 生成知识标签（模拟API调用）
const generateTags = async (content: string) => {
  // 模拟API调用延迟
  await new Promise(resolve => setTimeout(resolve, 1000))
  return ['前端开发', 'Vue3', 'TypeScript', '最佳实践']
}

// 确认文章概要，进入标签编辑
const handleConfirmSummary = async () => {
  if (!articleForm.value.summary.trim()) {
    return ElMessage.warning('请输入文章概要')
  }

  try {
    // 调用后端接口生成知识标签
    const tags = await generateTags(articleForm.value.content)
    articleForm.value.tags = tags
    
    // 关闭概要对话框，显示标签对话框
    summaryDialogVisible.value = false
    tagsDialogVisible.value = true
  } catch (error) {
    ElMessage.error('生成知识标签失败')
    console.error(error)
  }
}

// 返回概要编辑
const backToSummary = () => {
  tagsDialogVisible.value = false
  summaryDialogVisible.value = true
}

// 显示标签输入框
const showTagInput = () => {
  inputTagVisible.value = true
  nextTick(() => {
    tagInputRef.value?.input?.focus()
  })
}

// 添加标签
const handleAddTag = () => {
  if (inputTagValue.value) {
    if (!articleForm.value.tags.includes(inputTagValue.value)) {
      articleForm.value.tags.push(inputTagValue.value)
    }
  }
  inputTagVisible.value = false
  inputTagValue.value = ''
}

// 移除标签
const handleRemoveTag = (tag: string) => {
  articleForm.value.tags = articleForm.value.tags.filter(t => t !== tag)
}

// 最终发布文章
const handlePublish = async () => {
  if (articleForm.value.tags.length === 0) {
    return ElMessage.warning('请至少添加一个知识标签')
  }

  try {
    // 先保存为草稿
    const draft = await handleSaveDraft()
    if (!draft) return

    // 发布文章
    await articleStore.publishArticle({
      ...draft,
      category: articleForm.value.category,
      summary: articleForm.value.summary,
      tags: articleForm.value.tags
    })
    
    ElMessage.success('文章发布成功')
    tagsDialogVisible.value = false
    router.push('/profile/articles')
  } catch (error) {
    ElMessage.error('发布文章失败')
    console.error(error)
  }
}

// 编辑器配置
const editorConfig = {
  placeholder: '请输入正文内容...',
  MENU_CONF: {
    uploadImage: {
      server: `${import.meta.env.VITE_API_BASE_URL}/api/upload/image`,
      fieldName: 'image',
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      },
      maxFileSize: 5 * 1024 * 1024, // 5MB
      maxNumberOfFiles: 10,
      allowedFileTypes: ['image/*'],
      customInsert(res: any, insertFn: any) {
        insertFn(res.data.url)
      }
    }
  }
}

const toolbarConfig = {
  toolbarKeys: [
    'headerSelect',
    'blockquote',
    '|',
    'bold',
    'underline',
    'italic',
    {
      key: 'group-more-style',
      title: '更多',
      menuKeys: ['through', 'code', 'sup', 'sub']
    },
    '|',
    'bulletedList',
    'numberedList',
    'todo',
    {
      key: 'group-justify',
      title: '对齐',
      menuKeys: ['justifyLeft', 'justifyCenter', 'justifyRight', 'justifyJustify']
    },
    '|',
    'insertLink',
    'insertImage',
    'insertTable',
    'codeBlock',
    '|',
    'fullScreen'
  ]
}

const handleCreated = (editor: any) => {
  editorRef.value = editor
}

const handleFullScreen = (isFullscreen: boolean) => {
  isFullscreen.value = isFullscreen
}

// 组件卸载时销毁编辑器
onUnmounted(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
</script>

<style scoped>
.editor-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

.editor-header {
  padding: 16px;
  background: #fff;
  border-bottom: 1px solid #dcdfe6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 100;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.editor-container {
  flex: 1;
  overflow: auto;
  padding: 20px;
}

.editor-container.is-fullscreen {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
  background: #fff;
  padding: 0;
}

.editor-form {
  max-width: 1000px;
  margin: 0 auto;
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.title-section {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.title-input {
  font-size: 24px;
}

.category-select {
  width: 200px;
}

.summary-content {
  padding: 0 20px;
}

.summary-tip {
  margin-bottom: 16px;
  color: #666;
  font-size: 14px;
}

.title-input :deep(.el-input__wrapper),
.category-select :deep(.el-input__wrapper) {
  box-shadow: none;
  padding-left: 0;
}

.category-select :deep(.el-input__wrapper) {
  padding-left: 12px;
}

.dialog-footer {
  display: flex;
  justify-content: center;
}

.editor-wrapper {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

:deep(.w-e-text-container) {
  background-color: #fff;
}

:deep(.w-e-toolbar) {
  border-bottom: 1px solid #dcdfe6;
  background-color: #fff;
}

:deep(.w-e-text-placeholder) {
  color: #999;
}

:deep(.w-e-full-screen-container) {
  z-index: 9999;
}

.tags-content {
  padding: 0 20px;
}

.tags-tip {
  margin-bottom: 16px;
  color: #666;
  font-size: 14px;
}

.tags-list {
  margin-bottom: 16px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-item {
  margin-right: 8px;
  margin-bottom: 8px;
}

.button-new-tag {
  margin-left: 8px;
  height: 32px;
  padding-top: 0;
  padding-bottom: 0;
}

.tag-input {
  width: 90px;
  margin-left: 8px;
  vertical-align: bottom;
}
</style>
