import axios from 'axios';
import type { IUser, UserForm } from '@/types/user';

// 根据您的后端地址配置
const API_BASE_URL = 'http://127.0.0.1:8080/';

const api = axios.create({
    baseURL: API_BASE_URL,
    timeout: 5000
});

export const userApi = {
    // 获取分页用户列表
    async getUsers(page: number, pageSize: number): Promise<{
        data: IUser[],
        total: number
    }> {
        const response = await api.get('/users/queryPage ', {
            params: {
                page,
                pageSize
            }
        });
        return {
            data: response.data.items, // 假设返回结构为 { items: [], total: 100 }
            total: response.data.total
        };
    },

    // 展示所有用户
    async getAllUsers(): Promise<IUser[]> {
        const response = await api.get('/users/show');
        return response.data;
    },

    // 创建用户
    async createUser(user: UserForm): Promise<IUser> {
        const response = await api.post('/users/insert', user);
        return response.data;
    },

    // 更新用户
    async updateUser(id: number, user: UserForm): Promise<IUser> {
        // 将ID包含在请求体中
        const response = await api.post('/users/update', { id, ...user });
        return response.data;
    },

    // 删除用户
    async deleteUser(id: number): Promise<void> {
        // 将ID作为请求体发送，格式为 { id: 123 }
        await api.post('/users/delete', { id });
    },
    // 根据ID查询用户
    async getUserById(id: number): Promise<IUser> {
        const response = await api.get('/users/query', { params: { id } });
        return response.data;
    },

    login(credentials: { username: string; password: string }) {
        return api.post('/users/login', credentials)
    },
    register(userData: { username: string; password: string }) {
        return api.post('/users/register', userData)
    }
};