<script setup lang="js">

import {ref, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import * as echarts from 'echarts';
import SvgIcon from '@jamescoyle/vue-icon';
import { mdiDatabase } from '@mdi/js';
import axios from 'axios';
import {
  Setting, CameraFilled, DataAnalysis, Folder, FolderOpened, Document, Memo, HomeFilled,
  Search, Message, Bell, UserFilled, Aim
} from '@element-plus/icons-vue';

const search = ref('');
const isFolderOpen = ref(false);
const isTraining = ref(false);
const showTable = ref(false);

const bellMessage = [
  {date: '2016-05-02', message: 'James'},
  {date: '2016-05-04', message: 'James'},
  {date: '2016-05-01', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
  {date: '2016-05-03', message: 'James'},
];

const changeFolder = () => {
  isFolderOpen.value = !isFolderOpen.value;
  console.log(isFolderOpen.value);
}

const changeTraining = () => {
  isTraining.value = !isTraining.value;
  console.log(isTraining.value);
}

const toggleTable = () => {
  showTable.value = !showTable.value;
  console.log(showTable.value);
}

const router = useRouter();
const logout = () => {
  router.push('/');
}

const chart1 = ref(null);

onMounted(() => {
  const myChart = echarts.init(chart1.value);
  const option = {
    xAxis: {
      type: 'category',
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: [150, 230, 224, 218, 135, 147, 260],
        type: 'line'
      }
    ]
  };
  myChart.setOption(option);
});

// const chart2 = ref(null);
//
// onMounted(() => {
//   const myChart = echarts.init(chart2.value);
//   const option = {
//     xAxis: {
//       type: 'category',
//       data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
//     },
//     yAxis: {
//       type: 'value'
//     },
//     series: [
//       {
//         data: [150, 230, 224, 218, 135, 147, 260],
//         type: 'line'
//       }
//     ]
//   };
//   myChart.setOption(option);
// });

// const chart3 = ref(null);
//
// onMounted(() => {
//   const myChart = echarts.init(chart3.value);
//   const option = {
//     xAxis: {
//       type: 'category',
//       data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
//     },
//     yAxis: {
//       type: 'value'
//     },
//     series: [
//       {
//         data: [150, 230, 224, 218, 135, 147, 260],
//         type: 'line'
//       }
//     ]
//   };
//   myChart.setOption(option);
// });

// const chart4 = ref(null);
//
// onMounted(() => {
//   const myChart = echarts.init(chart4.value);
//   const option = {
//     xAxis: {
//       type: 'category',
//       data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
//     },
//     yAxis: {
//       type: 'value'
//     },
//     series: [
//       {
//         data: [150, 230, 224, 218, 135, 147, 260],
//         type: 'line'
//       }
//     ]
//   };
//   myChart.setOption(option);
// });
const path = ref(mdiDatabase);

const chart2 = ref(null);
onMounted(() => {
  let chartDom = chart2.value;
  let myChart = echarts.init(chartDom);
  let option;

  let base = +new Date(1968, 9, 3);
  let oneDay = 24 * 3600 * 1000;
  let date = [];
  let data = [Math.random() * 300];
  for (let i = 1; i < 20000; i++) {
    let now = new Date((base += oneDay));
    date.push([now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/'));
    data.push(Math.round((Math.random() - 0.5) * 20 + data[i - 1]));
  }
  option = {
    tooltip: {
      trigger: 'axis',
      position: function (pt) {
        return [pt[0], '10%'];
      }
    },
    // title: {
    //   left: 'center',
    //   text: 'Large Area Chart'
    // },
    toolbox: {
      feature: {
        dataZoom: {
          yAxisIndex: 'none'
        },
        restore: {},
        saveAsImage: {}
      }
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: date
    },
    yAxis: {
      type: 'value',
      boundaryGap: [0, '100%']
    },
    dataZoom: [
      {
        type: 'inside',
        start: 0,
        end: 10
      },
      {
        start: 0,
        end: 10
      }
    ],
    series: [
      {
        name: 'Fake Data',
        type: 'line',
        symbol: 'none',
        sampling: 'lttb',
        itemStyle: {
          color: 'rgb(255, 70, 131)'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: 'rgb(255, 158, 68)'
            },
            {
              offset: 1,
              color: 'rgb(255, 70, 131)'
            }
          ])
        },
        data: data
      }
    ]
  };

  option && myChart.setOption(option);
});

const chart3 = ref(null);
onMounted(() => {
  const chartDom = chart3.value;
  const myChart = echarts.init(chartDom);
  const option = {
    xAxis: {
      type: 'category',
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: [120, 200, 150, 80, 70, 110, 130],
        type: 'line',
        symbol: 'triangle',
        symbolSize: 20,
        lineStyle: {
          color: '#5470C6',
          width: 4,
          type: 'dashed'
        },
        itemStyle: {
          borderWidth: 3,
          borderColor: '#EE6666',
          color: 'yellow'
        }
      }
    ]
  };

  myChart.setOption(option);
});

const chart4 = ref(null);

onMounted(() => {
  const chartDom = chart4.value;
  const myChart = echarts.init(chartDom);
  const colors = ['#5470C6', '#EE6666'];
  const option = {
    color: colors,
    tooltip: {
      trigger: 'none',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {},
    grid: {
      top: 70,
      bottom: 50
    },
    xAxis: [
      {
        type: 'category',
        axisTick: {
          alignWithLabel: true
        },
        axisLine: {
          onZero: false,
          lineStyle: {
            color: colors[1]
          }
        },
        axisPointer: {
          label: {
            formatter: function (params) {
              return (
                  'Precipitation  ' +
                  params.value +
                  (params.seriesData.length ? '：' + params.seriesData[0].data : '')
              );
            }
          }
        },
        data: ['2016-1', '2016-2', '2016-3', '2016-4', '2016-5', '2016-6', '2016-7', '2016-8', '2016-9', '2016-10', '2016-11', '2016-12']
      },
      {
        type: 'category',
        axisTick: {
          alignWithLabel: true
        },
        axisLine: {
          onZero: false,
          lineStyle: {
            color: colors[0]
          }
        },
        axisPointer: {
          label: {
            formatter: function (params) {
              return (
                  'Precipitation  ' +
                  params.value +
                  (params.seriesData.length ? '：' + params.seriesData[0].data : '')
              );
            }
          }
        },
        data: ['2015-1', '2015-2', '2015-3', '2015-4', '2015-5', '2015-6', '2015-7', '2015-8', '2015-9', '2015-10', '2015-11', '2015-12']
      }
    ],
    yAxis: [
      {
        type: 'value'
      }
    ],
    series: [
      {
        name: 'Precipitation(2015)',
        type: 'line',
        xAxisIndex: 1,
        smooth: true,
        emphasis: {
          focus: 'series'
        },
        data: [
          2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3
        ]
      },
      {
        name: 'Precipitation(2016)',
        type: 'line',
        smooth: true,
        emphasis: {
          focus: 'series'
        },
        data: [
          3.9, 5.9, 11.1, 18.7, 48.3, 69.2, 231.6, 46.6, 55.4, 18.4, 10.3, 0.7
        ]
      }
    ]
  };

  myChart.setOption(option);
});

// const chart3 = ref(null);
// onMounted(() => {
//   const ROOT_PATH = 'https://echarts.apache.org/examples';
//   const chartDom = chart3.value;
//   const myChart = echarts.init(chartDom);
//   let option;
//
//   axios.get(`${ROOT_PATH}/data/asset/data/life-expectancy-table.json`)
//       .then(response => {
//         run(response.data);
//       });
//
//   function run(_rawData) {
//     const countries = [
//       'Finland',
//       'France',
//       'Germany',
//       'Iceland',
//       'Norway',
//       'Poland',
//       'Russia',
//       'United Kingdom'
//     ];
//     const datasetWithFilters = [];
//     const seriesList = [];
//     echarts.util.each(countries, function (country) {
//       let datasetId = 'dataset_' + country;
//       datasetWithFilters.push({
//         id: datasetId,
//         fromDatasetId: 'dataset_raw',
//         transform: {
//           type: 'filter',
//           config: {
//             and: [
//               { dimension: 'Year', gte: 1950 },
//               { dimension: 'Country', '=': country }
//             ]
//           }
//         }
//       });
//       seriesList.push({
//         type: 'line',
//         datasetId: datasetId,
//         showSymbol: false,
//         name: country,
//         endLabel: {
//           show: true,
//           formatter: function (params) {
//             return params.value[3] + ': ' + params.value[0];
//           }
//         },
//         labelLayout: {
//           moveOverlap: 'shiftY'
//         },
//         emphasis: {
//           focus: 'series'
//         },
//         encode: {
//           x: 'Year',
//           y: 'Income',
//           label: ['Country', 'Income'],
//           itemName: 'Year',
//           tooltip: ['Income']
//         }
//       });
//     });
//     option = {
//       animationDuration: 10000,
//       dataset: [
//         {
//           id: 'dataset_raw',
//           source: _rawData
//         },
//         ...datasetWithFilters
//       ],
//       title: {
//         text: 'Income of Germany and France since 1950'
//       },
//       tooltip: {
//         order: 'valueDesc',
//         trigger: 'axis'
//       },
//       xAxis: {
//         type: 'category',
//         nameLocation: 'middle'
//       },
//       yAxis: {
//         name: 'Income'
//       },
//       grid: {
//         right: 140
//       },
//       series: seriesList
//     };
//     myChart.setOption(option);
//   }
// });
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="aside">
        <el-menu
            class="navigation"
        >
          <el-icon class="camera">
            <CameraFilled/>
          </el-icon>
          <h1 class="title" >目标检测系统</h1>

          <hr class="divider" />
          <p class="subheading" style="color:rgb(30, 149, 179);">HOME</p>
          <el-menu-item index="1">
            <el-icon><DataAnalysis /></el-icon>
            <span>DashBord   </span>
          </el-menu-item>

          <hr class="divider" />

          <p class="subheading" style="color:rgb(30, 149, 179);">GLOBAL</p>
          <el-sub-menu index="2" @mousedown="changeTraining">
            <template #title>
              <el-icon :class="{ rotating: isTraining }"><Setting /></el-icon>
              <span>全局模型</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="2-1" @click="router.push( '/modelUpdate')">模型更新</el-menu-item>
              <el-menu-item index="2-2" @click="router.push('/modelTest')">模型测试</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>

          <hr class="divider" />

          <p class="subheading" style="color:rgb(30, 149, 179);">LOCAL</p>
          <el-sub-menu index="3" @mousedown="changeTraining">
            <template #title>
              <el-icon><Aim /></el-icon>
              <span>本地模型</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="3-1" @click="router.push( '/modelTraining')">模型训练</el-menu-item>
              <el-menu-item index="3-2" @click="router.push('/privacyAttacks')">隐私攻击</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>

          <hr class="divider" />

          <p class="subheading" style="color:rgb(30, 149, 179);">ADDONS</p>
          <el-sub-menu index="4">
            <template #title>
              <el-icon><Memo /></el-icon>
              <span>模型信息</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="4-1" @click="router.push( '/localModel')">本地模型列表</el-menu-item>
              <el-menu-item index="4-2" @click="router.push('/aggregationModel')">聚合模型列表</el-menu-item>
              <el-menu-item index="4-3" @click="router.push('/globalModel')">全局模型列表</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
          <hr class="divider" />
        </el-menu>
      </el-aside>

      <el-container>
        <el-header class="header" >
          <div class="headerContainer" style="display: flex; align-items: center;">
            <el-icon class="headerIcon" ><HomeFilled /></el-icon>
            <h2 class="headerTitle">DashBoard</h2>
<!--            <el-input class="input" v-model="search" size="large" placeholder="Please Input"/>-->
<!--            <el-button type="primary" class="searchButton" :icon="Search" ></el-button>-->
            <div class="block"></div>
            <el-badge is-dot  style="margin-right: 20px">
              <el-icon style="cursor: pointer; transform: scale(1.5);"><Message /></el-icon>
            </el-badge>
            <el-badge is-dot  style="margin-right:20px">
              <el-icon class="bellIcon" @click="toggleTable"><Bell /></el-icon>
            </el-badge>
            <el-dropdown>
              <el-avatar :icon="UserFilled" />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>个人信息</el-dropdown-item>
                  <el-dropdown-item>设置</el-dropdown-item>
                  <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <el-main class="main" >
          <el-table class="bellTable" :data="bellMessage" max-height="300" v-if="showTable" >
            <el-table-column prop="date" label="Date" width="180" ></el-table-column>
            <el-table-column prop="message" label="Message" width="180" ></el-table-column>
          </el-table>
          <div style="display: flex; justify-content: space-between;">
            <v-card
                title="模型数量"
                subtitle="10"
                variant="elevated"
                hover
                append-icon=""
                class="topData"
            >
              <template>
                <svg-icon type="mdi" :path="path"></svg-icon>
              </template>
            </v-card>
            <v-card
                title="数据集数量"
                subtitle="5"
                variant="elevated"
                hover
                append-icon="mdi-check"
                class="topData"
            >
            </v-card>

            <v-card
                title="最高准确率"
                variant="elevated"
                hover
                append-icon="mdi-check"
                class="topData"
            >
              <template #subtitle>
                <div style="display: flex; align-items: center;">
                  <span>20%</span>
                  <v-progress-linear model-value="20" style="flex: 1; margin-left: 10px;"></v-progress-linear>
                </div>
              </template>
            </v-card>

            <v-card
                title="历史数据"
                subtitle="66"
                variant="elevated"
                hover
                append-icon="mdi-check"
                class="topData"
            >
            </v-card>
          </div>

          <div style="display: flex; flex-wrap: wrap;">
            <v-row>
              <v-col cols="6">
                <v-card
                  variant="elevated"
                  class="dataTile"
                  >
                  <v-card-title style="font-size: 20px">
                    系统性能参数1:
                  </v-card-title>
                </v-card>
                <v-card
                    variant="elevated"
                    class="chartData"
                    style="background-size: 0"
                >
                  <div ref="chart1" style="width: 100%; height: 400px;"></div>
                  <!--                  <v-img src="src/assets/img_1.png" class="chart" ></v-img>-->
                </v-card>
              </v-col>
              <v-col cols="6">
                <v-card
                    variant="elevated"
                    class="dataTile"
                >
                  <v-card-title style="font-size: 20px">
                    系统性能参数2:
                  </v-card-title>
                </v-card>
                <v-card
                    variant="elevated"
                    class="chartData"
                >
                  <div ref="chart2" style="width: 100%; height: 400px;"></div>
                </v-card>
              </v-col>
              <v-col cols="6">
                <v-card
                    variant="elevated"
                    class="dataTile"
                >
                  <v-card-title style="font-size: 20px">
                    系统性能参数3:
                  </v-card-title>
                </v-card>
                <v-card
                    variant="elevated"
                    class="chartData"
                >
                  <div ref="chart3" style="width: 100%; height: 400px;"></div>
                </v-card>
              </v-col>
              <v-col cols="6">
                <v-card
                    variant="elevated"
                    class="dataTile"
                >
                  <v-card-title style="font-size: 20px">
                    系统性能参数4:
                  </v-card-title>
                </v-card>
                <v-card
                    variant="elevated"
                    class="chartData"
                >
                  <div ref="chart4" style="width: 100%; height: 400px;"></div>
                </v-card>
              </v-col>
            </v-row>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped lang="sass">
.aside
  position: fixed
  height: 100vh
  background-color: #d6e4ff
  width: 150px /* Adjust the width as needed */


.navigation
  background-color: #d6e4ff

.camera
  display: inline-block
  vertical-align: middle
  transform: scale(1.5)
  margin-left: 8px
  margin-top: 15px
  margin-bottom: 15px

.title
  display: inline-block
  vertical-align: middle
  margin-left: 8px
  margin-top: 15px
  margin-bottom: 15px

.subheading
  font-size: 12px
  color: #ccc
  margin: 10px 10px 0

.divider
  border: none
  border-top: 1px solid #ccc
  margin: 2px 10px 10px 10px

.rotating
  animation: rotate 1s linear infinite

@keyframes rotate
  from
    transform: rotate(0deg)
  to
    transform: rotate(360deg)

.header
  position: fixed
  top: 0
  left: 150px
  right: 0
  height: 60px
  background-color: #f5f5f5
  padding: 0 20px
  display: flex
  align-items: center
  justify-content: space-between
  z-index: 1000
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1), 0 -2px 4px rgba(0, 0, 0, 0.1)

.headerContainer
  display: flex
  align-items: center
  height: 100%
  padding: 0 20px

.headerIcon
  transform: scale(2)
  margin-left: 10px
  margin-right: 20px

.headerTitle
  width: 200px
  margin-right: 50px

.input
  width: 240px
  margin-left: 550px
  height: 40px
  border-radius: 0

.searchButton
  width: 20px
  height: 40px
  border-radius: 0
  box-shadow: 0 0 10px
  margin-right: 50px
.block
  margin-left: 900px

.bellIcon
  cursor: pointer
  transform: scale(1.5)

.main
  padding-left: 170px
  background-color: #80808014
  padding-top: 70px

.bellTable
  position: fixed
  top: 60px
  right: 20px
  width: 500px
  z-index: 1000
  background-color: white

.topData
  flex: 1
  margin: 10px 10px 30px 10px

.dataTile
  flex: 1
  margin-top: 0
  height: 40px
  margin-bottom: 0
  border: 1px solid #ccc
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1)
  background-color: #80808014

.chartData
  flex: 1
  height: 400px
  margin-top: 0

.chart
  height: 100%
  width: 100%
</style>

