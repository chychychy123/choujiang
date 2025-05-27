<template>
  <div class="user-management">
    <!-- 导航栏 -->
    <div class="navbar">
      <span class="logo">抽奖管理系统</span>
      <div class="auth-buttons" v-if="!authStore.isAuthenticated">
        <button class="login-btn" @click="goToLogin">登录</button>
        <button class="register-btn" @click="goToRegister">注册</button>
      </div>
      <div class="user-info" v-else>
        <span>当前用户：{{ authStore.currentUser?.username }}</span>
        <button @click="authStore.logout" class="logout-btn">退出登录</button>
      </div>
    </div>

    <div class="content">
      <h1>奖项列表</h1>
      <button
          class="draw-button"
          @click="handleDraw"
          :disabled="isDrawing || awards.length === 0"
      >
        {{ isDrawing ? '抽奖中...' : '开始抽奖' }}
      </button>

      <!-- 修改后的弹窗 -->
      <div v-if="showPrizeDialog" class="dialog-overlay">
        <div class="prize-dialog" :class="{ 'error-style': !prizeResult }">
          <h3>{{ prizeResult ? '恭喜中奖!' : '系统提示' }}</h3>
          <p v-if="prizeResult">您获得了：<strong>{{ prizeResult }}</strong></p>
          <button @click="closeDialog">确定</button>
        </div>
      </div>

      <!-- 奖项表格 -->
      <table class="award-table">
        <thead>
        <tr>
          <th>名称</th>
          <th>数量</th>
          <th>奖品</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(award, index) in awards" :key="index">
          <td>{{ award.name }}</td>
          <td>{{ award.quantity }}</td>
          <td>{{ award.prize }}</td>
        </tr>
        </tbody>
      </table>
      <!-- 中奖弹窗 -->

    </div>
    <!-- 在奖项表格后添加中奖记录表格 -->
    <div class="award-records">
        <h2>我的中奖记录</h2>
        <table class="award-record-table">
            <thead>
                <tr>
                    <th>奖项名称</th>
                    <th>奖品内容</th>
                    <th>中奖时间</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="record in awardRecords" :key="record.id">
                    <td>{{ record.awardName }}</td>
                    <td>{{ record.prize }}</td>
                    <td>{{ formatDateTime(record.createTime) }}</td>
                </tr>
            </tbody>
        </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, Ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';
import { awardApi } from '@/api/awardApi';
import type { Award, AwardRecord } from '@/types/award';

const router = useRouter();
const authStore = useAuthStore();
const awards = ref<Award[]>([])
const isDrawing = ref(false)
const drawResult = ref<string | null>(null)
const isLoading = ref(false)
const error = ref<string | null>(null)
const showPrizeDialog = ref(false)
const prizeResult = ref<string | undefined>(undefined) // 统一类型并初始化为 undefined

const awardRecords = ref<AwardRecord[]>([])

// 格式化日期时间
const formatDateTime = (dateTimeStr: string) => {
    const date = new Date(dateTimeStr)
    return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    })
}

// 获取奖项列表
const fetchAwards = async () => {
    isLoading.value = true
    error.value = null
    try {
        awards.value = await awardApi.getAwards()
        console.log("获取成功",awards.value)
    } catch (err) {
        error.value = '获取奖项列表失败，请刷新重试'
        console.error(err)
    } finally {
        isLoading.value = false
    }
}

// 加载奖项
const loadAwards = async () => {
    try {
        awards.value = await awardApi.getAwards()
    } catch (error) {
        console.error('加载奖项失败:', error)
    }
}

// 获取中奖记录
// 加载中奖记录
const loadAwardRecords = async () => {
    try {
        if (!authStore.currentUser?.id) {
            console.warn('用户未登录或无用户ID')
            return
        }
        const timeoutPromise = new Promise((_, reject) => {
            setTimeout(() => reject(new Error('请求超时')), 10000)
        })
        const recordsPromise = awardApi.getUserAwardRecords()
        // 添加类型断言
        awardRecords.value = await Promise.race([recordsPromise, timeoutPromise]) as AwardRecord[]
        console.log('成功获取获奖记录:', awardRecords.value); // 添加这行代码
    } catch (err) {
        // 添加错误类型检查
        if (err instanceof Error) {
            console.error('加载中奖记录失败:', err)
            if (err.message === '请求超时') {
                console.error('请求超时，请检查网络连接')
            } else if (err.message.includes('401')) { // 示例：处理未授权错误
                 console.error('用户未授权，请重新登录');
                 // 可以选择在此处重定向到登录页
                 // router.push('/home/login');
            }
        } else {
            console.error('未知错误:', err)
        }
    }
}

// 确保在组件挂载时加载记录
onMounted(async () => {
    await authStore.initialize() // 等待初始化完成
    await fetchAwards()
    if (authStore.isAuthenticated) {
        await loadAwardRecords()
    }
})

// 执行抽奖
const handleDraw = async () => {
    if (!authStore.currentUser?.id) {
        console.error('用户未登录或无用户ID')
        prizeResult.value = '请先登录'
        showPrizeDialog.value = true
        return
    }
    
    isDrawing.value = true
    try {
        const result = await awardApi.draw()
        prizeResult.value = result.prize
        showPrizeDialog.value = true
        // 抽奖成功后刷新记录
        await loadAwardRecords()
        await loadAwards()
    } catch (error) {
        console.error('抽奖失败:', error)
        prizeResult.value = '抽奖失败，请重试'
        showPrizeDialog.value = true
    } finally {
        isDrawing.value = false
    }
}

// 关闭弹窗
const closeDialog = () => {
    showPrizeDialog.value = false
}

// 跳转到登录/注册
const goToLogin = () => router.push('/home/login')
const goToRegister = () => router.push('/home/register')

</script>

<style scoped>
.award-record-table{
  width: 100%;
  border-collapse: collapse;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}
.award-record-table th {
  background-color: #1e88e5;
  padding: 12px 16px;
  text-align: left;
  font-weight: 500;
  color: white;
  border-bottom: 1px solid #f0f0f0;
}

.award-record-table td {
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
  color: #333;
}

.award-record-table tr:hover td {
  background-color: #fafafa;
}
.user-info {
  padding: 15px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}


button {
  padding: 6px 12px;
  background: #e53935;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.auth-buttons {
  display: flex;
  gap: 15px;
}

.login-btn, .register-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.award-table {
  width: 100%;
  border-collapse: collapse;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.award-table th {
  background-color: #1e88e5;
  padding: 12px 16px;
  text-align: left;
  font-weight: 500;
  color: white;
  border-bottom: 1px solid #f0f0f0;
}

.award-table td {
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
  color: #333;
}

.award-table tr:hover td {
  background-color: #fafafa;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .navbar {
    flex-direction: column;
    gap: 12px;
  }

  .auth-buttons {
    width: 100%;
    justify-content: flex-end;
  }

  .draw-button {
    width: 100%;
    margin: 0 0 16px 0;
  }
}

.register-btn {
  background-color: #4caf50;
  color: white;
}

.login-btn:hover {
  background-color: #f1f1f1;
}

.register-btn:hover {
  background-color: #3d8b40;
}
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #1e88e5;
  color: white;
  margin-bottom: 20px;
}

.logo {
  font-size: 18px;
  font-weight: bold;
}

.logout-btn {
  background-color: transparent;
  color: white;
  border: 1px solid white;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.logout-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
}
search-box {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.search-box input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  width: 200px;
}

.user-management {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

h1 {
  color: #333;
  display: inline-block;
  margin-right: 20px;
}




.user-table th, .user-table td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: left;
}

.user-table th {
  background-color: #f5f5f5;
  font-weight: bold;
}

.user-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.user-table tr:hover {
  background-color: #f1f1f1;
}



.dialog h2 {
  margin-top: 0;
  color: #333;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input, .form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}



.dialog-actions button {
  margin-left: 10px;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.dialog-actions button[type="button"] {
  background-color: #e0e0e0;
}

.dialog-actions button[type="submit"] {
  background-color: #1e88e5;
  color: white;
}

.dialog-actions button[type="button"]:hover {
  background-color: #d5d5d5;
}

.dialog-actions button[type="submit"]:hover {
  background-color: #1976d2;
}

</style>

