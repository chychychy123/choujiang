// src/api/awardApi.ts
import axios from 'axios'

// 创建配置好的axios实例（关键修复1：使用统一的axios实例）
const api = axios.create({
    baseURL: 'http://127.0.0.1:8080', // 确保这是正确的后端地址
    timeout: 5000
})

// 请求拦截器：自动添加token（关键修复2）
api.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

// 类型定义
export interface Award {
    name: string
    quantity: number
    prize: string
}

export interface DrawResult {
    success: boolean
    prize?: string
    message?: string
}

export const awardApi = {
    /**
     * 获取所有奖项信息
     */
    async getAwards(): Promise<Award[]> {
        try {
            // 关键修复3：使用配置好的api实例而不是裸axios
            const response = await api.get('/awards/showAward')
            return response.data
        } catch (error) {
            console.error('获取奖项列表失败:', error)
            throw new Error('获取奖项列表失败，请检查网络连接')
        }
    },

    /**
     * 执行抽奖
     */
    async draw(): Promise<DrawResult> {
        try {
            const response = await api.get('/awards/draw');

            // 处理纯文本响应
            if (typeof response.data === 'string') {
                const hasPrize = response.data.includes('恭喜') && response.data.includes('：');
                return {
                    success: hasPrize,
                    prize: hasPrize ?
                        response.data.split('：')[1]?.replace('！', '') :
                        undefined,
                    message: response.data
                };
            }

            // 处理JSON响应
            return {
                success: response.data.success,
                prize: response.data.prize,
                message: response.data.message || ''
            };

        } catch (error: any) {
            return {
                success: false,
                message: error.response?.data?.message ||
                    error.message ||
                    '抽奖服务异常'
            };
        }
    }
}