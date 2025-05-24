import axios from 'axios'

const api = axios.create({
    baseURL: 'http://127.0.0.1:8080',
    timeout: 5000
})

export const authApi = {
    login(credentials: { username: string; password: string }) {
        return api.post('/users/login', credentials)
    },
    register(userData: { username: string; password: string }) {
        return api.post('/users/register', userData)
    }
}