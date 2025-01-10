<template>
  <div class="user-layout">
    <!-- 用户页面导航栏 -->
    <TheNavbar />
    
    <!-- 主要内容区域 -->
    <main class="main-content">
      <slot></slot>
    </main>
  </div>
</template>

<script setup lang="ts">
import TheNavbar from '../components/TheNavbar.vue'
</script>

<style scoped>
.user-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  padding-top: 60px;
}
</style> 