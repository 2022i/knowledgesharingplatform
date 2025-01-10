<template>
  <component :is="layout">
    <router-view v-slot="{ Component }">
      <transition name="fade" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
  </component>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuth } from './composables/useAuth'
import UserLayout from './layouts/UserLayout.vue'
import AdminLayout from './layouts/AdminLayout.vue'

const route = useRoute()
const { initAuth } = useAuth()

// 根据路由元信息决定使用哪个布局
const layout = computed(() => {
  const isAdminRoute = route.path.startsWith('/admin')
  return isAdminRoute ? AdminLayout : UserLayout
})

// 初始化认证状态
onMounted(async () => {
  await initAuth()
})
</script>

<style>
/* 路由过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>