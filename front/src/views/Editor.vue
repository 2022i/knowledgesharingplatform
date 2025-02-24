<template>
  <div class="editor-page">
    <div class="editor-header" v-show="!isFullscreen">
      <el-button @click="handleBack">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <div class="header-actions">
        <el-button 
          type="info" 
          :loading="savingDraft" 
          @click="handleSaveDraft"
        >保存草稿</el-button>
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
              placeholder="请选择分类"
              class="category-select"
              :loading="themeStore.isLoading"
              :disabled="themeStore.isLoading"
            >
              <el-option
                v-for="theme in themeOptions"
                :key="theme.id"
                :label="theme.name"
                :value="theme.id"
              />
              <template #empty>
                <el-empty
                  v-if="!themeStore.isLoading"
                  description="暂无分类数据"
                />
                <div v-else class="loading-text">
                  加载分类中...
                </div>
              </template>
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
import { ref, onMounted, onUnmounted, nextTick, computed } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
import { ArrowLeft } from '@element-plus/icons-vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElLoading } from 'element-plus'
import { useThemeStore } from '../store/theme'
import axios from 'axios'
import type { Article } from '../types/article'
import type { Theme } from '../store/theme'
import request from '../utils/request'

const router = useRouter()
const route = useRoute()
const themeStore = useThemeStore()
const isLoading = ref(false)
const loading = ref(false)
const savingDraft = ref(false)

const isFullscreen = ref(false)
const summaryDialogVisible = ref(false)
const tagsDialogVisible = ref(false)
const inputTagVisible = ref(false)
const inputTagValue = ref('')
const tagInputRef = ref()
const articleForm = ref({
  title: '',
  content: '',
  category: null,
  tags: [] as string[],
  summary: ''
})

const editorRef = ref()

// 自动保存定时器
let autoSaveTimer: number | null = null

// 添加计算属性来获取分类列表
const themeOptions = computed(() => {
  const themes = themeStore.getAllThemes
  console.log('当前分类列表:', themes)
  return themes
})

// 加载文章数据
const loadArticle = async (articleId: number) => {
  console.log('开始加载文章数据，文章ID:', articleId)
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const userId = userInfo.id

    // 发送请求获取文章数据
    const response = await request.get('/server/getRenderedArticle', {
      params: {
        articleId,
        userId
      }
    })

    console.log('原始响应数据:', response)

    // 获取文章数据
    const articleData = response.data

    // 打印完整的文章数据用于调试
    console.log('文章数据:', {
      id: articleData.id,
      title: articleData.title,
      theme: articleData.theme,
      content: articleData.content,
      summary: articleData.summary,
      relatedKnowledge: articleData.relatedKnowledge
    })

    if (!articleData) {
      throw new Error('文章数据为空')
    }

    // 查找匹配的主题ID
    const matchingTheme = themeStore.getAllThemes.find(
      theme => theme.name === articleData.theme
    )
    console.log('匹配的主题:', matchingTheme)

    // 更新表单数据
    articleForm.value = {
      title: articleData.title || '',
      content: articleData.content || '',
      category: matchingTheme ? matchingTheme.id : null,
      tags: Array.isArray(articleData.relatedKnowledge) ? articleData.relatedKnowledge : [],
      summary: articleData.summary || ''
    }

    console.log('更新后的表单数据:', articleForm.value)

    // 如果没有找到匹配的主题，显示警告
    if (!matchingTheme) {
      ElMessage.warning('未找到文章对应的分类，请重新选择分类')
    }

  } catch (error: any) {
    console.error('加载文章数据失败:', {
      error,
      message: error.message,
      response: error.response?.data,
      status: error.response?.status,
      stack: error.stack
    })
    
    if (error.response?.status === 404) {
      ElMessage.error('文章不存在或已被删除')
    } else {
      ElMessage.error(error.message || '加载文章数据失败，请稍后重试')
    }
    
    // 加载失败时返回上一页
    router.back()
  }
}

// 在组件挂载时检查是否有文章ID参数
onMounted(async () => {
  console.log('Editor组件开始加载...')
  try {
    // 首先加载分类数据
    console.log('开始获取分类数据...')
    await themeStore.fetchAllThemes()
    console.log('分类数据加载完成:', themeStore.getAllThemes)
    
    // 检查是否有文章ID参数
    const articleId = route.params.id
    if (articleId) {
      // 编辑模式：加载现有文章
      const id = parseInt(articleId as string, 10)
      console.log('检测到文章ID，进入编辑模式:', id)
      await loadArticle(id)
    } else {
      // 新建模式：初始化空白表单
      console.log('未检测到文章ID，进入新建模式')
      articleForm.value = {
        title: '',
        content: '',
        category: null,
        tags: [],
        summary: ''
      }
    }

    // 设置自动保存
    autoSaveTimer = window.setInterval(async () => {
      if (articleForm.value.title || articleForm.value.content) {
        await handleSaveDraft()
      }
    }, 60000) // 每分钟自动保存
  } catch (error) {
    console.error('初始化编辑器失败:', error)
    ElMessage.error('初始化失败，请刷新页面重试')
  }
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
  if (!articleForm.value.title.trim()) {
    ElMessage.warning('请输入文章标题')
    return
  }
  if (!articleForm.value.content.trim()) {
    ElMessage.warning('请输入文章内容')
    return
  }
  if (!articleForm.value.category) {
    ElMessage.warning('请选择文章分类')
    return
  }

  savingDraft.value = true
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const draftData = {
      authorId: userInfo.id,
      themeId: parseInt(String(articleForm.value.category), 10),
      title: articleForm.value.title.trim(),
      content: articleForm.value.content.trim(),
      summary: '',
      relatedKnowledge: []
    }

    console.log('保存草稿数据:', draftData)

    const response = await request.post('/server/write/draft', draftData)

    console.log('保存草稿响应:', response)

    if (response.data.code === 200) {
      ElMessage.success(response.data.msg || '草稿保存成功')
      // 等待消息显示完成后再跳转到个人中心的文章列表页
      setTimeout(() => {
        router.push('/profile/articles')
      }, 1500)
    } else {
      throw new Error(response.data.msg || '草稿保存失败')
    }
  } catch (error: any) {
    console.error('保存草稿失败:', {
      error,
      message: error.message,
      response: error.response?.data,
      status: error.response?.status
    })
    ElMessage.error(error.message || '保存草稿失败，请稍后重试')
  } finally {
    savingDraft.value = false
  }
}

// 生成文章概要（调用后端API）
const generateSummary = async (content: string) => {
  console.log('开始生成文章概要，文章内容长度:', content.length)
  try {
    console.log('发送请求到 AI 摘要生成接口...')
    console.log('发送的文章内容:', content)

    const response = await axios({
      method: 'post',
      url: 'http://127.0.0.1:8081/server/ai/summary',
      headers: {
        'Content-Type': 'text/plain',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      data: content,  // 直接发送原始内容，包含 HTML 标签
      transformRequest: [(data) => data]  // 防止 axios 自动转换请求体
    })

    console.log('收到摘要生成响应:', response.data)
    
    // 确保返回的是字符串类型
    const summary = typeof response.data === 'string' ? response.data : response.data.msg || ''
    console.log('处理后的摘要:', summary)
    return summary
  } catch (error: any) {
    console.error('生成摘要失败:', {
      error: error,
      message: error.message,
      response: error.response?.data,
      status: error.response?.status,
      requestData: content.substring(0, 500) + '...' // 记录前500个字符
    })
    ElMessage.error(`生成摘要失败: ${error.response?.data?.msg || error.message}`)
    throw error
  }
}

// 生成知识标签（调用后端API）
const generateTags = async (content: string) => {
  console.log('开始生成知识标签，文章内容长度:', content.length)
  try {
    console.log('发送请求到 AI 标签生成接口...')
    console.log('发送的文章内容:', content)

    const response = await axios({
      method: 'post',
      url: 'http://127.0.0.1:8081/server/ai/tags',
      headers: {
        'Content-Type': 'text/plain',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      data: content,  // 直接发送原始内容，包含 HTML 标签
      transformRequest: [(data) => data]  // 防止 axios 自动转换请求体
    })

    console.log('收到标签生成响应:', response.data)

    // 处理返回的标签数据
    let tags: string[] = []
    if (Array.isArray(response.data)) {
      // 如果返回的是数组，直接使用
      tags = response.data.filter(tag => typeof tag === 'string' && tag.trim())
    } else if (typeof response.data === 'string') {
      // 如果返回的是字符串，按分隔符分割
      tags = response.data.split('、').filter(Boolean)
    }

    if (!tags || tags.length === 0) {
      console.warn('未生成有效的标签')
      return []
    }

    console.log('处理后的标签列表:', tags)
    return tags
  } catch (error: any) {
    console.error('生成标签失败:', {
      error: error,
      message: error.message,
      response: error.response?.data,
      status: error.response?.status,
      requestData: content.substring(0, 500) + '...' // 记录前500个字符
    })
    ElMessage.error(`生成标签失败: ${error.response?.data?.msg || error.message}`)
    throw error
  }
}

// 发布文章
const handleSubmit = async () => {
  console.log('开始文章发布流程')
  console.log('当前文章表单数据:', {
    title: articleForm.value.title,
    categoryId: articleForm.value.category,
    contentLength: articleForm.value.content.length
  })

  if (!articleForm.value.title.trim()) {
    console.warn('标题为空')
    return ElMessage.warning('请输入文章标题')
  }
  if (!articleForm.value.category) {
    console.warn('未选择分类')
    return ElMessage.warning('请选择文章分类')
  }
  if (!articleForm.value.content.trim()) {
    console.warn('内容为空')
    return ElMessage.warning('请输入文章内容')
  }

  try {
    console.log('开始生成文章概要...')
    // 显示加载提示
    const loadingInstance = ElLoading.service({
      text: '正在生成文章概要...'
    })

    try {
      // 调用后端接口生成文章概要
      const summary = await generateSummary(articleForm.value.content)
      console.log('成功生成文章概要:', summary)
      articleForm.value.summary = summary
      
      // 显示概要编辑对话框
      summaryDialogVisible.value = true
    } finally {
      // 关闭加载提示
      loadingInstance.close()
    }
  } catch (error: any) {
    console.error('文章发布流程失败:', {
      error: error,
      message: error.message,
      response: error.response?.data,
      status: error.response?.status
    })
    ElMessage.error('发布失败，请重试')
  }
}

// 确认文章概要，进入标签编辑
const handleConfirmSummary = async () => {
  // 确保 summary 是字符串并且不为空
  const summary = String(articleForm.value.summary || '').trim()
  if (!summary) {
    return ElMessage.warning('请输入文章概要')
  }

  try {
    // 显示加载提示
    const loadingInstance = ElLoading.service({
      text: '正在生成知识标签...'
    })

    try {
      // 调用后端接口生成知识标签
      const tags = await generateTags(articleForm.value.content)
      
      // 检查标签是否为空
      if (!tags || tags.length === 0) {
        ElMessage.warning('未能生成有效的知识标签，请手动添加')
        articleForm.value.tags = []
      } else {
        articleForm.value.tags = tags
      }
      
      // 关闭概要对话框，显示标签对话框
      summaryDialogVisible.value = false
      tagsDialogVisible.value = true
    } finally {
      // 关闭加载提示
      loadingInstance.close()
    }
  } catch (error: any) {
    console.error('生成标签失败:', {
      error: error,
      message: error.message,
      response: error.response?.data,
      status: error.response?.status
    })
    
    // 即使标签生成失败，也允许用户继续
    ElMessage.warning('标签生成失败，请手动添加标签')
    articleForm.value.tags = []
    summaryDialogVisible.value = false
    tagsDialogVisible.value = true
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
  if (!articleForm.value.title.trim()) {
    return ElMessage.warning('请输入文章标题')
  }

  if (!articleForm.value.content.trim()) {
    return ElMessage.warning('请输入文章内容')
  }

  if (!articleForm.value.category) {
    return ElMessage.warning('请选择文章分类')
  }

  if (!articleForm.value.summary.trim()) {
    return ElMessage.warning('请输入文章概要')
  }

  if (!articleForm.value.tags.length) {
    return ElMessage.warning('请添加知识标签')
  }

  try {
    loading.value = true
    console.log('开始处理文章...')
    
    // 获取当前用户ID
    const userInfo = localStorage.getItem('userInfo')
    if (!userInfo) {
      throw new Error('用户未登录')
    }
    const { id: authorId } = JSON.parse(userInfo)

    // 准备发布数据
    const articleData = {
      authorId: parseInt(String(authorId), 10),
      themeId: parseInt(String(articleForm.value.category), 10),
      title: articleForm.value.title,
      content: articleForm.value.content,
      summary: articleForm.value.summary,
      relatedKnowledge: articleForm.value.tags
    }

    // 获取文章ID
    const articleId = route.params.id
    let response

    if (articleId) {
      // 更新文章
      console.log('更新文章，ID:', articleId)
      articleData.id = parseInt(articleId as string, 10)
      response = await request({
        url: '/server/write/updateArticle',
        method: 'put',
        data: articleData
      })
    } else {
      // 发布新文章
      console.log('发布新文章')
      response = await request({
        url: '/server/write/article',
        method: 'post',
        data: articleData
      })
    }

    console.log('响应:', response.data)

    if (response.data.code === 200) {
      ElMessage.success(articleId ? '文章更新成功' : '文章发布成功')
      // 关闭对话框
      tagsDialogVisible.value = false
      // 发布成功后跳转到个人中心的文章列表页
      router.push('/profile/articles')
    } else {
      throw new Error(response.data.msg || '操作失败')
    }
  } catch (error: any) {
    console.error('操作失败:', error)
    ElMessage.error(error.message || '操作失败，请重试')
  } finally {
    loading.value = false
  }
}

// 处理确认发布
const handleConfirmPublish = async () => {
  if (!articleForm.value.title.trim()) {
    ElMessage.warning('请输入文章标题')
    return
  }

  if (!articleForm.value.content.trim()) {
    ElMessage.warning('请输入文章内容')
    return
  }

  if (!articleForm.value.themeId) {
    ElMessage.warning('请选择文章分类')
    return
  }

  if (!articleForm.value.summary.trim()) {
    ElMessage.warning('请生成或编辑文章摘要')
    return
  }

  if (!articleForm.value.tags.length) {
    ElMessage.warning('请生成或编辑文章标签')
    return
  }

  try {
    await handlePublish()
  } catch (error) {
    console.error('确认发布失败:', error)
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
  },
  attributes: {
    writingsuggestions: 'on'
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

.loading-text {
  padding: 12px;
  text-align: center;
  color: #909399;
  font-size: 14px;
}

:deep(.el-select-dropdown__empty) {
  padding: 8px 0;
}

:deep(.el-empty) {
  padding: 12px 0;
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
