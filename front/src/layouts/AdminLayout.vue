<template>
  <div class="admin-layout">
    
    <!-- 主要内容区域 -->
    <main class="main-content">
      <slot></slot>
    </main>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  padding-top: 60px;
}
</style> 