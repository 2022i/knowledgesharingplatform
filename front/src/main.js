import { createApp } from 'vue';
import App from './App.vue';
import router from './router/index.js';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import '@mdi/font/css/materialdesignicons.css'; // 引入 @mdi/font


const app = createApp(App);

const vuetify = createVuetify({
    components,
    directives,
})

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
}

app.use(vuetify);
app.use(ElementPlus);
app.use(router);
app.mount('#app');