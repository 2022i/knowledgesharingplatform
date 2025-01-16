<template>
  <div class="achievements-container" v-loading="loading">
    <!-- 数据概览卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><Document /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ achievements.writeArticleCount }}</div>
          <div class="stat-label">发表文章</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><Star /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ achievements.supportedCount }}</div>
          <div class="stat-label">获得点赞</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><Share /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ achievements.sharedCount }}</div>
          <div class="stat-label">被分享</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><Collection /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ achievements.collectedCount }}</div>
          <div class="stat-label">被收藏</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><UserFilled /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ achievements.fansCount }}</div>
          <div class="stat-label">粉丝</div>
        </div>
      </div>
    </div>

    <!-- 数据趋势图表 -->
    <div class="chart-section">
      <div class="chart-title">数据趋势</div>
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="chart-card">
            <div ref="interactionChart" style="height: 300px"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-card">
            <div ref="contentChart" style="height: 300px"></div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { Document, Star, Share, Collection, UserFilled } from '@element-plus/icons-vue'
import request from '../../utils/request'
import * as echarts from 'echarts'

// 加载状态
const loading = ref(false)

// 成就数据接口
interface Achievements {
  writeArticleCount: number
  supportedCount: number
  sharedCount: number
  fansCount: number
  collectedCount: number
  opposedCount: number
}

// 成就数据
const achievements = ref<Achievements>({
  writeArticleCount: 0,
  supportedCount: 0,
  sharedCount: 0,
  fansCount: 0,
  collectedCount: 0,
  opposedCount: 0
})

// 获取用户ID
const userId = computed(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  return userInfo.id
})

// 加载成就数据
const loadAchievements = async () => {
  if (!userId.value) {
    return
  }

  loading.value = true
  try {
    const response = await request.get<Achievements>('/server/personalCenterData/getPersonalAchievements', {
      params: { userId: userId.value }
    })
    achievements.value = response.data

    // 更新本地存储中的部分数据
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    userInfo.articleCount = response.data.writeArticleCount
    userInfo.likeCount = response.data.supportedCount
    userInfo.fans = response.data.fansCount
    localStorage.setItem('userInfo', JSON.stringify(userInfo))

    // 初始化图表
    initCharts()
  } catch (error) {
    console.error('Failed to load achievements:', error)
  } finally {
    loading.value = false
  }
}

// 图表引用
const interactionChart = ref<HTMLElement | null>(null)
const contentChart = ref<HTMLElement | null>(null)

// 初始化图表
const initCharts = () => {
  // 互动数据图表
  if (interactionChart.value) {
    const chart1 = echarts.init(interactionChart.value)
    chart1.setOption({
      title: {
        text: '互动数据',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          type: 'pie',
          radius: '50%',
          data: [
            { value: achievements.value.supportedCount, name: '获赞' },
            { value: achievements.value.sharedCount, name: '分享' },
            { value: achievements.value.collectedCount, name: '收藏' }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    })
  }

  // 内容数据图表
  if (contentChart.value) {
    const chart2 = echarts.init(contentChart.value)
    chart2.setOption({
      title: {
        text: '内容与粉丝',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      xAxis: {
        type: 'category',
        data: ['文章数', '粉丝数']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [
            achievements.value.writeArticleCount,
            achievements.value.fansCount
          ],
          type: 'bar',
          showBackground: true,
          backgroundStyle: {
            color: 'rgba(180, 180, 180, 0.2)'
          }
        }
      ]
    })
  }
}

// 监听窗口大小变化，调整图表大小
window.addEventListener('resize', () => {
  const charts = document.querySelectorAll('.chart-card div')
  charts.forEach(chart => {
    const instance = echarts.getInstanceByDom(chart as HTMLElement)
    instance?.resize()
  })
})

onMounted(() => {
  loadAchievements()
})
</script>

<style scoped>
.achievements-container {
  background: white;
  border-radius: 8px;
  padding: 24px;
  min-height: 400px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 32px;
}

.stat-card {
  background: #f8f9fa;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  background: var(--el-color-primary);
  color: white;
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  line-height: 1.2;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}

.chart-section {
  margin-top: 32px;
}

.chart-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
}

.chart-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}
</style> 