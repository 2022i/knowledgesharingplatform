import { createRouter, createWebHistory } from 'vue-router';
import Register from "@/components/Register.vue";
import Login from "@/components/Login.vue";
import ResetPassword from "@/components/ResetPassword.vue";
import DashBoard from "@/components/DashBoard.vue";
import ModelUpdate from "@/components/GlobalModel/ModelUpdate.vue";
import PrivacyAttacks from "@/components/LocalModel/PrivacyAttacks.vue";
import ModelTest from "@/components/GlobalModel/ModelTest.vue";
import ModelTraining from "@/components/LocalModel/ModelTraining.vue";
import LocalModel from "@/components/ModelInformation/LocalModel.vue";
import AggregationModel from "@/components/ModelInformation/AggregationModel.vue";
import GlobalModel from "@/components/ModelInformation/GlobalModel.vue";


const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
  },
  {
    path:'/resetPassword',
    name: 'ResetPassword',
    component: ResetPassword
  },
  {
    path:'/dashBoard',
    name: 'DashBoard',
    component: DashBoard
  },
  {
    path: '/modelTraining',
    name: 'ModelTraining',
    component: ModelTraining
  },
  {
    path: '/modelUpdate',
    name: 'ModelUpdate',
    component: ModelUpdate
  },
  {
    path: '/privacyAttacks',
    name: 'PrivacyAttacks',
    component: PrivacyAttacks
  },
  {
    path: '/modelTest',
    name: 'ModelTest',
    component: ModelTest
  },
  {
    path: '/aggregationModel',
    name: 'AggregationModel',
    component: AggregationModel
  },
  {
    path: '/localModel',
    name: 'LocalModel',
    component: LocalModel
  },
  {
    path: '/globalModel',
    name: 'GlobalModel',
    component: GlobalModel
  }

];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;