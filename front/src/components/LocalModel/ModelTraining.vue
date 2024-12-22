<script setup lang="js">

import { ref } from 'vue';
import {useRouter} from 'vue-router';
import { genFileId } from 'element-plus'
import { ElButton, ElBadge } from 'element-plus';



import {
  Setting, CameraFilled, DataAnalysis, Folder, FolderOpened, Document, Memo,
  Search, Message, Bell, UserFilled, Aim
} from '@element-plus/icons-vue';

const search= ref('');
const isFolderOpen = ref(false);
const isTraining = ref(false);
const showProgress = ref(false);
const showTable = ref(false);
const updateLearningRate = ref(0);
const updateRoundNumber= ref(0);
const gradientClippingNorm= ref(0);
const privacyBudget= ref(0);
const selectedModel= ref('');

const bellMessage = [
  { date: '2016-05-02', message: 'James'},
  { date: '2016-05-04', message: 'James' },
  { date: '2016-05-01', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
  { date: '2016-05-03', message: 'James'},
];



const changeFolder = () =>{
  isFolderOpen.value = !isFolderOpen.value;
  console.log(isFolderOpen.value);
}

const changeTraining = () =>{
  isTraining.value = !isTraining.value;
  console.log(isTraining.value);
}

const toggleTable = () => {
  showTable.value = !showTable.value;
  console.log(showTable.value);
}

const enlargeDiv = (event, className) => {
  const elements = document.getElementsByClassName(className);
  for (let element of elements) {
    element.style.transition = 'transform 0.2s ease';
    element.style.transform = 'scale(1.1)';
  }
};

const resetDiv = (event, className) => {
  const elements = document.getElementsByClassName(className);
  for (let element of elements) {
    element.style.transition = 'transform 0.2s ease';
    element.style.transform = 'scale(1)';
  }
};

const router = useRouter();
const logout = () => {
  router.push('/');
};


const upload = ref(null);

const handleExceed = (files) => {
  upload.value.clearFiles()
  const file = files[0]
  file.uid = genFileId()
  upload.value.handleStart(file)
}

const startTraining = () => {
  showProgress.value = true;
};

const stopTraining = () => {
  showProgress.value = false;
};

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
          <el-menu-item index="1"  @click="router.push( '/dashBoard')">
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
          <div class="headerContainer" >
            <el-icon class="headerIcon" ><Aim /></el-icon>
            <h2 class="headerTitle">ModelTraining</h2>
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
          <div>
            <div class="formContainer" @mouseover="enlargeDiv($event, 'formContainer')"
                 @mouseleave="resetDiv($event, 'formContainer')"
            >
              <h2 style="position: absolute; top: 0; left:5px;">本地模型训练控制台</h2>
              <el-form class="form" @mouseover="enlargeDiv($event, 'formContainer')"
                       @mouseleave="resetDiv($event, 'formContainer')" label-width="auto">
                <el-form-item label="上传本地模型" style="margin-bottom: 0">
                  <el-upload
                      ref="upload"
                      action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
                      :auto-upload="false"
                  >
                    <template #trigger>
                      <el-button type="primary" style="background-color: #91d5ff; margin-right: 50px;">选择模型文件
                      </el-button>
                    </template>

                    <el-button type="success" style="background-color: #61DDAA; margin-left: 10px;" >
                      上传模型
                    </el-button>

                    <template #tip>
                      <div style="margin-top: 0">
                        YOLO目标检测模型文件
                      </div>
                    </template>
                  </el-upload>
                </el-form-item>

                <el-form-item label="上传本地模型数据集" style="margin-bottom: 0">
                  <el-upload
                      ref="upload"
                      action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
                      :limit="1"
                      :on-exceed="handleExceed"
                      :auto-upload="false"
                  >
                    <template #trigger>
                      <el-button type="primary" style="background-color: #91d5ff;">选择数据集文件</el-button>
                    </template>
                    <template #tip>
                      <div style="margin-top: 0; color: red; font-size: 10px;">
                        只能上传一个数据集文件
                      </div>
                    </template>
                  </el-upload>
                </el-form-item>

                <el-form-item label="已上传本地模型">
                  <div style="display: flex; align-items: center;">
                    <el-select class="select" v-model="selectedModel" placeholder="请选择你的模型文件" >
                      <el-option label="模型1" value="shanghai"/>
                      <el-option label="模型2" value="beijing" />
                    </el-select>
                    <el-button type="primary" style="background-color: #91d5ff">选择当前模型</el-button>
                  </div>
                </el-form-item>



                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="本地模型更新学习率">
                      <el-input v-model="updateLearningRate"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="本地模型更新轮数">
                      <el-input v-model="updateRoundNumber"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="初始梯度裁剪范数">
                      <el-input v-model="gradientClippingNorm"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="隐私预算">
                      <el-input v-model="privacyBudget"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <div style="display: flex; justify-content: center; gap: 10px;">
                  <el-button style="background-color:#bfbfbf;margin-right: 20px" @click="startTraining">开始训练
                  </el-button>
                  <el-button style="background-color:#fa541c;margin-left: 20px" @click="stopTraining">停止训练
                  </el-button>
                </div>
                <el-progress v-if="showProgress" :text-inside="true" :stroke-width="26" :percentage="70" style="margin-top: 20px" />
              </el-form>
            </div>
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
  height: 100vh
  padding-left: 170px
  background-color: #80808014
  padding-top: 70px
  display: flex
  justify-content: center
  align-items: center

.formContainer
  display: flex
  justify-content: space-between
  background-color: white
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.5)
  padding: 20px
  transition: transform 0.2s ease
  transform-origin: center
  position: relative

.form
  max-width: 600px
  flex-grow: 1
  margin-top: 40px

.select
  margin-right: 10px
  width: 300px
  border: 1px solid gray

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

.el-input:hover
  transform: scale(1.05) /* 设置输入框的放大效果 */


.el-input :deep(.el-input__wrapper)
  border: 1px solid gray /* 设置输入框的边框为1px的灰色实线 */
  border-radius: 2px /* 设置输入框的边框圆角为2px */
  transition: all 0.3s ease-in-out /* 设置输入框所有属性的过渡效果，持续时间为0.3秒 */
</style>

