import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'

/**
 * 路由配置列表
 * meta: {
 *   requiresAuth: boolean  // 是否需要登录权限
 *   hideNav: boolean      // 是否隐藏顶部导航栏
 * }
 */
const routes: RouteRecordRaw[] = [
  // 根路径重定向到首页
  {
    path: '/',
    redirect: '/home'
  },
  // 首页路由 - 需要登录权限
  {
    path: '/home',
    name: 'Home',
    component: Home,
    meta: { 
      requiresAuth: true,
      title: '首页'
    }
  },
  // 登录页面 - 不显示导航栏
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { hideNav: true }
  },
  // 注册页面 - 不显示导航栏
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { hideNav: true }
  },
  // 文章详情 - 需要登录权限
  {
    path: '/article/:id',
    name: 'ArticleDetail',
    component: () => import('../views/ArticleDetail.vue'),
    meta: { requiresAuth: true }
  },
  // 文章编辑页 - 需要登录权限且不显示导航栏
  {
    path: '/editor/:id?',
    name: 'Editor',
    component: () => import('../views/Editor.vue'),
    meta: { 
      requiresAuth: true,
      hideNav: true
    }
  },
  // 搜索页面 - 需要登录权限
  {
    path: '/search',
    name: 'Search',
    component: () => import('../views/Search.vue'),
    meta: { requiresAuth: true }
  },
  // 个人中心页面 - 需要登录权限
  {
    path: '/profile',
    component: () => import('../views/Profile/index.vue'),
    children: [
      {
        path: '',
        name: 'Profile',
        redirect: 'articles'
      },
      {
        path: 'articles',
        name: 'ProfileArticles',
        component: () => import('../views/Profile/Articles.vue')
      },
      {
        path: 'comments',
        name: 'ProfileComments',
        component: () => import('../views/Profile/Comments.vue')
      },
      {
        path: 'likes',
        name: 'ProfileLikes',
        component: () => import('../views/Profile/Likes.vue')
      },
      {
        path: 'favorites',
        name: 'ProfileFavorites',
        component: () => import('../views/Profile/Favorites.vue')
      },
      {
        path: 'following',
        name: 'ProfileFollowing',
        component: () => import('../views/Profile/Following.vue')
      },
      {
        path: 'fans',
        name: 'ProfileFans',
        component: () => import('../views/Profile/Fans.vue')
      },
      {
        path: 'achievements',
        name: 'ProfileAchievements',
        component: () => import('../views/Profile/Achievements.vue')
      },
      {
        path: 'view-history',
        name: 'ProfileViewHistory',
        component: () => import('../views/Profile/ViewHistory.vue')
      }
    ]
  },
  // 管理员页面 - 需要管理员权限
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/admin/index.vue'),
    meta: { 
      requiresAuth: true,
      requiresAdmin: true,
      hideNav: true
    },
    redirect: '/admin/articles',  // 直接重定向到文章管理页
    children: [
      {
        path: 'articles',
        name: 'AdminArticles',
        component: () => import('../views/admin/Articles.vue')
      }
    ]
  },
  // 404页面
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/NotFound.vue')
  },
  // 分类页面 - 需要登录权限
  {
    path: '/category',
    name: 'Category',
    component: () => import('../views/Category.vue'),
    meta: { 
      requiresAuth: true,
      title: '分类'
    }
  },
  // 用户主页 - 需要登录权限
  {
    path: '/user/:id',
    name: 'User',
    component: () => import('../views/User.vue'),
    meta: { 
      requiresAuth: true,
      title: '用户主页'
    }
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
})

/**
 * 全局路由守卫
 * 1. 处理已登录用户访问登录/注册页面的情况
 * 2. 处理未登录用户访问需要认证页面的情况
 * 3. 处理非管理员用户访问管理员页面的情况
 */
router.beforeEach((to, from, next) => {
  console.log('Navigation:', { 
    from: from.path, 
    to: to.path,
    token: !!localStorage.getItem('token')
  })
  
  const token = localStorage.getItem('token')
  // 从 localStorage 中获取用户信息来判断角色
  const userInfo = localStorage.getItem('userInfo')
  const isAdmin = userInfo ? JSON.parse(userInfo).role === 'admin' : false
  
  console.log('User role check:', { isAdmin })
  
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const requiresAdmin = to.matched.some(record => record.meta.requiresAdmin)
  const isAuthPage = ['/login', '/register'].includes(to.path)

  if (isAuthPage && token) {
    console.log('Redirecting to home: already logged in')
    next('/home')
    return
  }

  if (requiresAuth && !token) {
    console.log('Redirecting to login: auth required')
    next({
      path: '/login',
      query: { redirect: to.fullPath }
    })
    return
  }

  if (requiresAdmin && !isAdmin) {
    console.log('Redirecting to home: admin required')
    next('/home')
    return
  }

  console.log('Proceeding with navigation')
  next()
})

export default router 