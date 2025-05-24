// src/stores/authStore.ts
import { defineStore } from 'pinia'

import {userApi} from '@/api/userApi'

interface User {
    username: string
}

export const useAuthStore = defineStore('auth', {
    state: () => ({
        currentUser: null as User | null,
        token: null as string | null
    }),

    actions: {
        // 新增初始化方法
        initialize() {
            const auth = localStorage.getItem('auth')
            if (auth) {
                const { user, token } = JSON.parse(auth)
                this.currentUser = user
                this.token = token
            }
        },

        // 修改登录方法
        async loginC(credentials: { username: string; password: string }) {
            // const response = await fetch('/users/login', {
            //     method: 'POST',
            //     body: JSON.stringify(credentials)
            // })

            const response = await userApi.login(credentials)
            const data = response.data

            // ✅ 正确赋值方式（保持对象结构）
            this.currentUser = {
                username: data.username // 将字符串包装为对象
            }

            this.token = data.token
            localStorage.setItem('auth', JSON.stringify({
                user: this.currentUser, // 存储完整对象
                token: this.token
            }))
        },

        logout() {
            this.$reset()
            localStorage.removeItem('auth')
        }
    },

    getters: {
        isAuthenticated: (state) => !!state.token
    }
})