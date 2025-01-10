<template>
  <div class="dashboard-page">
    <!-- 数据概览卡片 -->
    <div class="stat-cards">
      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <span>文章总数</span>
            <el-tag type="info" size="small">今日 +{{ todayStats.articles }}</el-tag>
          </div>
        </template>
        <div class="card-value">
          <span class="number">{{ stats.totalArticles }}</span>
          <el-icon class="trend-icon" :class="{ up: todayStats.articles > 0 }">
            <TrendCharts />
          </el-icon>
        </div>
      </el-card>

      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <span>待审核文章</span>
            <el-tag type="warning" size="small">待处理</el-tag>
          </div>
        </template>
        <div class="card-value">
          <span class="number">{{ stats.pendingArticles }}</span>
          <el-button type="primary" link @click="goToArticles">
            去审核
            <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
      </el-card>

      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <span>用户总数</span>
            <el-tag type="info" size="small">今日 +{{ todayStats.users }}</el-tag>
          </div>
        </template>
        <div class="card-value">
          <span class="number">{{ stats.totalUsers }}</span>
          <el-icon class="trend-icon" :class="{ up: todayStats.users > 0 }">
            <TrendCharts />
          </el-icon>
        </div>
      </el-card>

      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <span>评论总数</span>
            <el-tag type="info" size="small">今日 +{{ todayStats.comments }}</el-tag>
          </div>
        </template>
        <div class="card-value">
          <span class="number">{{ stats.totalComments }}</span>
          <el-icon class="trend-icon" :class="{ up: todayStats.comments > 0 }">
            <TrendCharts />
          </el-icon>
        </div>
      </el-card>
    </div>

    <!-- 图表区域 -->
    <div class="chart-section">
      <el-row :gutter="20">
        <el-col :span="16">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>文章发布趋势</span>
                <el-radio-group v-model="articleChartPeriod" size="small">
                  <el-radio-button label="week">近7天</el-radio-button>
                  <el-radio-button label="month">近30天</el-radio-button>
                </el-radio-group>
              </div>
            </template>
            <div class="chart-container">
              <el-empty v-if="!articleChartData.length" description="暂无数据" />
              <div v-else ref="articleChartRef" class="chart"></div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="8">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>文章状态分布</span>
              </div>
            </template>
            <div class="chart-container">
              <el-empty v-if="!articleStatusData.length" description="暂无数据" />
              <div v-else ref="statusChartRef" class="chart"></div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 最近活动 -->
    <el-card class="recent-activities">
      <template #header>
        <div class="card-header">
          <span>最近活动</span>
          <el-button type="primary" link>查看更多</el-button>
        </div>
      </template>
      <el-timeline>
        <el-timeline-item
          v-for="activity in recentActivities"
          :key="activity.id"
          :timestamp="activity.time"
          :type="activity.type"
        >
          {{ activity.content }}
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { TrendCharts, ArrowRight } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { useArticleStore } from '../../store/article'

const router = useRouter()
const articleStore = useArticleStore()

// 统计数据
const stats = ref({
  totalArticles: 0,
  pendingArticles: 0,
  totalUsers: 0,
  totalComments: 0
})

const todayStats = ref({
  articles: 0,
  users: 0,
  comments: 0
})

// 图表相关
const articleChartRef = ref()
const statusChartRef = ref()
const articleChartPeriod = ref('week')
const articleChartData = ref([])
const articleStatusData = ref([])

// 最近活动
const recentActivities = ref([
  {
    id: 1,
    content: '用户 张三 发布了新文章《Vue3 组件设计最佳实践》',
    time: '10分钟前',
    type: 'primary'
  },
  {
    id: 2,
    content: '管理员 审核通过了 文章《TypeScript 高级特性详解》',
    time: '30分钟前',
    type: 'success'
  },
  {
    id: 3,
    content: '用户 李四 注册成为新用户',
    time: '1小时前',
    type: 'info'
  },
  {
    id: 4,
    content: '系统 自动备份完成',
    time: '2小时前',
    type: 'warning'
  }
])

// 初始化数据
const initData = async () => {
  // 这里应该从API获取数据，现在用模拟数据
  stats.value = {
    totalArticles: 256,
    pendingArticles: 15,
    totalUsers: 1280,
    totalComments: 3456
  }

  todayStats.value = {
    articles: 12,
    users: 48,
    comments: 156
  }

  // 模拟文章发布趋势数据
  articleChartData.value = [
    { date: '03-01', count: 5 },
    { date: '03-02', count: 8 },
    { date: '03-03', count: 3 },
    { date: '03-04', count: 7 },
    { date: '03-05', count: 12 },
    { date: '03-06', count: 6 },
    { date: '03-07', count: 9 }
  ]

  // 模拟文章状态分布数据
  articleStatusData.value = [
    { value: 156, name: '已发布' },
    { value: 15, name: '待审核' },
    { value: 45, name: '草稿' },
    { value: 40, name: '已拒绝' }
  ]

  initCharts()
}

// 初始化图表
const initCharts = () => {
  // 文章发布趋势图
  if (articleChartRef.value) {
    const articleChart = echarts.init(articleChartRef.value)
    articleChart.setOption({
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: articleChartData.value.map(item => item.date)
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: articleChartData.value.map(item => item.count),
          type: 'line',
          smooth: true,
          areaStyle: {
            opacity: 0.2
          }
        }
      ]
    })
  }

  // 文章状态分布图
  if (statusChartRef.value) {
    const statusChart = echarts.init(statusChartRef.value)
    statusChart.setOption({
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        right: 10,
        top: 'center'
      },
      series: [
        {
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: false
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '16',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: articleStatusData.value
        }
      ]
    })
  }
}

// 跳转到文章管理页面
const goToArticles = () => {
  router.push('/admin/articles')
}

// 监听图表周期变化
watch(articleChartPeriod, () => {
  // 这里应该根据选择的周期重新获取数据
  initCharts()
})

// 初始化
onMounted(() => {
  initData()
})
</script>

<style scoped>
.dashboard-page {
  padding: 20px;
}

.stat-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .card-value {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 8px;

    .number {
      font-size: 24px;
      font-weight: 600;
    }

    .trend-icon {
      font-size: 20px;
      color: #909399;

      &.up {
        color: #67c23a;
        transform: rotate(-45deg);
      }
    }
  }
}

.chart-section {
  margin-bottom: 20px;

  .chart-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .chart-container {
      height: 300px;

      .chart {
        width: 100%;
        height: 100%;
      }
    }
  }
}

.recent-activities {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style> 