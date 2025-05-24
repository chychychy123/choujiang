import { createApp } from 'vue';
import App from './App.vue';
import router from "@/router";
import { createPinia } from 'pinia'


const pinia = createPinia()
const app = createApp(App);


app.use(pinia)
app.use(router);
// 初始化认证状态·
import { useAuthStore } from '@/stores/authStore'
const authStore = useAuthStore()
authStore.initialize()

app.mount('#app');