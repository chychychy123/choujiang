import { defineStore } from 'pinia'
import { userApi } from '@/api/userApi'

interface User {
    username: string
    id: number
}

export const useAuthStore = defineStore('auth', {
    state: () => ({
        currentUser: null as User | null,
        token: null as string | null
    }),

    actions: {
        async loginC(credentials: { username: string; password: string }) {
            const response = await userApi.login(credentials)
            const data = response.data

            const tokenPayload = data.token.split('.')[1]
            const decodedPayload = JSON.parse(atob(tokenPayload))
            const userId = decodedPayload.user

            this.currentUser = {
                username: data.username,
                id: userId
            }
            this.token = data.token
            
            // 修复：添加userId到localStorage
            localStorage.setItem('auth', JSON.stringify({
                user: {
                    username: data.username,
                    id: userId // 新增用户ID存储
                },
                token: this.token
            }))
        },

        initialize() {
            const auth = localStorage.getItem('auth')
            if (auth) {
                const { user, token } = JSON.parse(auth)
                
                this.currentUser = {
                    username: user.username,
                    id: user.id
                }
                
                this.token = token
            }
        },  // ← Add this comma

        logout() {
            this.$reset()
            localStorage.removeItem('auth')
        }
    },

    getters: {
        isAuthenticated: (state) => !!state.currentUser
    }
})