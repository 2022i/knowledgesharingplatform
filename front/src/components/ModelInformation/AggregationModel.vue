<script setup lang="js">

import {ref, onMounted, nextTick, reactive, computed, watch} from 'vue';
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

const dialog = ref(false);
const dialogDelete = ref(false);
const headers = [
  {
    title: '本地用户',
    align: 'start',
    // sortable: false,
    key: 'name',
  },
  {title: '信息1', key: 'calories'},
  {title: '信息2', key: 'fat'},
  {title: '信息3', key: 'carbs'},
  {title: '信息4', key: 'protein'},
  {title: '操作', key: 'actions', sortable: false},
];
const desserts = ref([]);
const editedIndex = ref(-1);
const editedItem = reactive({
  name: '',
  calories: 0,
  fat: 0,
  carbs: 0,
  protein: 0,
});
const defaultItem = {
  name: '',
  calories: 0,
  fat: 0,
  carbs: 0,
  protein: 0,
};

const formTitle = computed(() => (editedIndex.value === -1 ? 'New Item' : 'Edit Item'));

watch(dialog, (val) => {
  if (!val) close();
});
watch(dialogDelete, (val) => {
  if (!val) closeDelete();
});

onMounted(() => {
  initialize();
});

function initialize() {
  desserts.value = [
    {
      name: 'Frozen Yogurt',
      calories: 159,
      fat: 6.0,
      carbs: 24,
      protein: 4.0,
    },
    {
      name: 'Ice cream sandwich',
      calories: 237,
      fat: 9.0,
      carbs: 37,
      protein: 4.3,
    },
    {
      name: 'Eclair',
      calories: 262,
      fat: 16.0,
      carbs: 23,
      protein: 6.0,
    },
    {
      name: 'Cupcake',
      calories: 305,
      fat: 3.7,
      carbs: 67,
      protein: 4.3,
    },
    {
      name: 'Gingerbread',
      calories: 356,
      fat: 16.0,
      carbs: 49,
      protein: 3.9,
    },
    {
      name: 'Jelly bean',
      calories: 375,
      fat: 0.0,
      carbs: 94,
      protein: 0.0,
    },
    {
      name: 'Lollipop',
      calories: 392,
      fat: 0.2,
      carbs: 98,
      protein: 0,
    },
    {
      name: 'Honeycomb',
      calories: 408,
      fat: 3.2,
      carbs: 87,
      protein: 6.5,
    },
    {
      name: 'Donut',
      calories: 452,
      fat: 25.0,
      carbs: 51,
      protein: 4.9,
    },
    {
      name: 'KitKat',
      calories: 518,
      fat: 26.0,
      carbs: 65,
      protein: 7,
    },
    {
      name: 'KitKat',
      calories: 518,
      fat: 26.0,
      carbs: 65,
      protein: 7,
    },
    {
      name: 'KitKat',
      calories: 518,
      fat: 26.0,
      carbs: 65,
      protein: 7,
    },
    {
      name: 'KitKat',
      calories: 518,
      fat: 26.0,
      carbs: 65,
      protein: 7,
    },
    {
      name: 'KitKat',
      calories: 518,
      fat: 26.0,
      carbs: 65,
      protein: 7,
    },
  ];
}

function editItem(item) {
  editedIndex.value = desserts.value.indexOf(item);
  Object.assign(editedItem, item);
  dialog.value = true;
}

function deleteItem(item) {
  editedIndex.value = desserts.value.indexOf(item);
  Object.assign(editedItem, item);
  dialogDelete.value = true;
}

function deleteItemConfirm() {
  desserts.value.splice(editedIndex.value, 1);
  closeDelete();
}

function close() {
  dialog.value = false;
  nextTick(() => {
    Object.assign(editedItem, defaultItem);
    editedIndex.value = -1;
  });
}

function closeDelete() {
  dialogDelete.value = false;
  nextTick(() => {
    Object.assign(editedItem, defaultItem);
    editedIndex.value = -1;
  });
}

function save() {
  if (editedIndex.value > -1) {
    Object.assign(desserts.value[editedIndex.value], editedItem);
  } else {
    desserts.value.push({...editedItem});
  }
  close();
}

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
              <!--              <el-menu-item index="2-2" @click="router.push('/privacyAttacks')">隐私攻击测试</el-menu-item>-->
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
              <!--              <el-menu-item index="2-2" @click="router.push('/privacyAttacks')">隐私攻击测试</el-menu-item>-->
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
          <!--          <el-menu-item index="4">-->
          <!--            <el-icon><Memo /></el-icon>-->
          <!--            <span>Tables</span>-->
          <!--          </el-menu-item>-->
          <hr class="divider" />
        </el-menu>
      </el-aside>

      <el-container>
        <el-header class="header" >
          <div class="headerContainer" style="display: flex; align-items: center;">
            <el-icon class="headerIcon" ><Memo /></el-icon>
            <h2 class="headerTitle">AggregationModel</h2>
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
          <v-data-table
              :headers="headers"
              :items="desserts"
              :sort-by="[{ key: 'calories', order: 'asc' }]"
              style="margin-top:0;height: 600px"
          >
            <template v-slot:top>
              <v-toolbar
                  flat
              >
                <v-toolbar-title style="width: 20px">参与聚合本地模型详细信息</v-toolbar-title>
                <v-divider
                    class="mx-4"
                    inset
                    vertical
                ></v-divider>
                <!--                  <v-spacer></v-spacer>-->
                <v-dialog
                    v-model="dialog"
                    max-width="500px"
                >
                  <template v-slot:activator="{ props }">
                    <el-input class="listInput" v-model="search" size="large" placeholder="Please Input"/>
                    <el-button type="primary" class="listSearchButton" :icon="Search" ></el-button>
                  </template>
                  <v-card>
                    <v-card-title>
                      <span class="text-h5">{{ formTitle }}</span>
                    </v-card-title>

                    <v-card-text>
                      <v-container>
                        <v-row>
                          <v-col
                              cols="12"
                              md="4"
                              sm="6"
                          >
                            <v-text-field
                                v-model="editedItem.name"
                                label="Dessert name"
                            ></v-text-field>
                          </v-col>
                          <v-col
                              cols="12"
                              md="4"
                              sm="6"
                          >
                            <v-text-field
                                v-model="editedItem.calories"
                                label="Calories"
                            ></v-text-field>
                          </v-col>
                          <v-col
                              cols="12"
                              md="4"
                              sm="6"
                          >
                            <v-text-field
                                v-model="editedItem.fat"
                                label="Fat (g)"
                            ></v-text-field>
                          </v-col>
                          <v-col
                              cols="12"
                              md="4"
                              sm="6"
                          >
                            <v-text-field
                                v-model="editedItem.carbs"
                                label="Carbs (g)"
                            ></v-text-field>
                          </v-col>
                          <v-col
                              cols="12"
                              md="4"
                              sm="6"
                          >
                            <v-text-field
                                v-model="editedItem.protein"
                                label="Protein (g)"
                            ></v-text-field>
                          </v-col>
                        </v-row>
                      </v-container>
                    </v-card-text>

                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn
                          color="blue-darken-1"
                          variant="text"
                          @click="close"
                      >
                        Cancel
                      </v-btn>
                      <v-btn
                          color="blue-darken-1"
                          variant="text"
                          @click="save"
                      >
                        Save
                      </v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
                <v-dialog v-model="dialogDelete" max-width="500px">
                  <v-card>
                    <v-card-title class="text-h5">Are you sure you want to delete this item?</v-card-title>
                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn color="blue-darken-1" variant="text" @click="closeDelete">Cancel</v-btn>
                      <v-btn color="blue-darken-1" variant="text" @click="deleteItemConfirm">OK</v-btn>
                      <v-spacer></v-spacer>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
              </v-toolbar>
            </template>
            <template v-slot:item.actions="{ item }">
              <v-icon
                  class="me-2"
                  size="small"
                  @click="editItem(item)"
              >
                mdi-pencil
              </v-icon>
              <v-icon
                  size="small"
                  @click="deleteItem(item)"
              >
                mdi-delete
              </v-icon>
            </template>
            <template v-slot:no-data>
              <v-btn
                  color="primary"
                  @click="initialize"
              >
                Reset
              </v-btn>
            </template>
          </v-data-table>
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

.listInput
  width: 240px
  //margin-left:
  height: 40px
  border-radius: 0

.listSearchButton
  width: 20px
  height: 40px
  border-radius: 0
  box-shadow: 0 0 10px
  margin-right: 50px

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

