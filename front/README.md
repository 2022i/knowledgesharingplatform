# 知识共享平台

## 项目简介
这是一个仿知乎的知识共享平台，用户可以发布、搜索、评论和管理知识内容。

## 功能特性
- 用户认证与授权
- 富文本编辑器支持的知识发布
- 知识内容的评论、收藏、分享和点赞
- 基于关键字的知识搜索
- 个人中心管理（发布、评论、收藏、点赞等）

## 技术栈
- Vue 3
- TypeScript
- Element Plus
- Vite
- Vue Router
- Pinia

## 项目结构

## API 接口文档
### 用户相关
- POST /api/auth/login - 用户登录
- POST /api/auth/register - 用户注册
- GET /api/user/profile - 获取用户信息

### 文章相关
- GET /api/articles - 获取文章列表
- GET /api/articles/:id - 获取文章详情
- POST /api/articles - 创建文章
- PUT /api/articles/:id - 更新文章
- DELETE /api/articles/:id - 删除文章

### 互动相关
- POST /api/articles/:id/like - 点赞文章
- POST /api/articles/:id/collect - 收藏文章
- POST /api/articles/:id/comments - 发表评论

# 路由管理
npm install vue-router@4

# 状态管理
npm install pinia

# UI组件库
npm install element-plus

# 富文本编辑器
npm install @wangeditor/editor
npm install @wangeditor/editor-for-vue@next

# HTTP 客户端
npm install axios

# 工具库
npm install lodash-es