// src/api/awardApi.ts
import axios from 'axios'

// 创建配置好的axios实例
const api = axios.create({
    baseURL: 'http://127.0.0.1:8080', // 确保这是正确的后端地址
    timeout: 5000
})

// 请求拦截器：从auth对象中获取token
api.interceptors.request.use(config => {
    const authStr = localStorage.getItem('auth')
    if (authStr) {
        try {
            const auth = JSON.parse(authStr)
            if (auth.token) {
                config.headers.Authorization = `Bearer ${auth.token}`
            }
        } catch (e) {
            console.error('解析auth数据失败', e)
        }
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

// 修改token验证函数，从auth对象中获取token
const isValidToken = () => {
    const authStr = localStorage.getItem('auth')
    if (!authStr) return false
    
    try {
        const auth = JSON.parse(authStr)
        return auth.token && auth.token.length > 0
    } catch (e) {
        console.error('解析auth数据失败', e)
        return false
    }
}

// 添加中奖记录的类型定义
export interface AwardRecord {
    id: number
    userId: string  // 修改为string类型以适配Java Long
    userName: string
    awardId: number
    awardName: string
    prize: string
    createTime: string
}

export const awardApi = {
    async getAwards(): Promise<Award[]> {
        try {
            const response = await api.get('/awards/showAward')
            // 添加数据验证
            if (!Array.isArray(response.data)) {
                throw new Error('Invalid response format')
            }
            // 验证每个奖项的数据结构
            return response.data.map(award => {
                if (!award.name || !award.quantity || !award.prize) {
                    throw new Error('Invalid award data structure')
                }
                return award as Award
            })
        } catch (error) {
            // 统一错误处理方式
            return Promise.reject({
                success: false,
                message: error instanceof Error ? error.message : '获取奖项列表失败'
            })
        }
    },

    async draw(): Promise<DrawResult> {
        try {
            // 统一使用token验证函数
            if (!isValidToken()) {
                return {
                    success: false,
                    message: '登录状态已失效，请重新登录'
                }
            }

            const response = await api.post('/awards/draw')
            console.log('抽奖响应:', response)

            // 处理字符串格式的响应
            if (typeof response.data === 'string') {
                return {
                    success: true,
                    prize: response.data
                }
            }

            // 如果是对象格式，保持原有处理逻辑
            if (typeof response.data === 'object' && response.data !== null) {
                return response.data as DrawResult
            }

            throw new Error('Invalid response format')
        } catch (error) {
            console.error('抽奖请求失败:', error)
            return {
                success: false,
                message: error instanceof Error ? error.message : '抽奖失败，请稍后重试'
            }
        }
    },
    
    // 获取用户中奖记录
    getUserAwardRecords: async (): Promise<AwardRecord[]> => {
        try {
            const authStr = localStorage.getItem('auth')

            if (!authStr) {
                console.log('localStorage中没有auth数据，返回空数组。') // 新增日志
                return [] 
            }
            const auth = JSON.parse(authStr)
            // 从解析后的auth对象中获取userId，并确保其类型为number
            const userId = auth.user && auth.user.id ? Number(auth.user.id) : undefined; // 修改这里
            
            console.log('从localStorage解析出的userId:', userId); // 修改这里，使用新的userId变量

            // 确保有userId
            if (!userId) { // 修改这里，使用新的userId变量
                console.error('未找到用户ID')
                return []
            }
    
            console.log('发送请求到/award/showUserAward，参数userId:', userId); // 修改这里，使用新的userId变量
            const response = await api.get(`/award/showUserAward`, {
                params: {
                    userId: userId // 修改这里，使用新的userId变量
                }
            })
            console.log('中奖记录响应:', response.data)
            return Array.isArray(response.data) ? response.data : []
    
        } catch (error) {
            console.error('获取中奖记录失败:', error)
            return []
        }
    }
}

// 添加token过期时间检查
const isTokenExpired = () => {
  const auth = JSON.parse(localStorage.getItem('auth') || '{}')
  if (!auth.token) return true
  
  try {
    const base64Url = auth.token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const payload = JSON.parse(window.atob(base64))
    
    return payload.exp * 1000 < Date.now()
  } catch (e) {
    return true
  }
}

// 在发送请求前检查token
axios.interceptors.request.use(async config => {
  if (isTokenExpired()) {
    // token过期，跳转到登录页面
    window.location.href = '/login'
    return Promise.reject('Token expired')
  }
  return config
})