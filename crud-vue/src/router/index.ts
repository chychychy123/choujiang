import { createRouter, createWebHistory } from 'vue-router'
import UserView from '../views/UserView.vue'
import RegisterView from '../views/RegisterView.vue'


const routes = [
    {
        path: '/',
        redirect: '/home/login'
    },
    {
        path: '/home',
        name: 'Home',
        component: () => import('../views/HomeView.vue'),
        children: [
            {
                path: 'login',      // 相对路径
                name: 'Login',      // 建议首字母大写保持规范
                component: () => import('../views/LoginView.vue')
            },
            {
                path: 'register',
                name: 'Register',
                component: () => import('../views/RegisterView.vue')
            },
            {
                path: 'users',
                name: 'Users',
                component: () => import('../views/UserView.vue'),
                meta: { requiresAuth: true }
            },
        ],


    },


]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫
// router/index.ts
router.beforeEach((to) => {
    const isAuth = !!localStorage.getItem('token')

    // 需要登录但未认证 → 跳登录页
    if (to.meta.requiresAuth && !isAuth) {
        return {
            path: '/home/login',
            query: { redirect: to.fullPath }
        }
    }
    // 已登录时访问登录页 → 跳首页
    if (to.path === '/home/login' && isAuth) {
        return '/users'
    }
})
export default router