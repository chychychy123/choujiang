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
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';
import { awardApi, type Award, type DrawResult } from '@/api/awardApi';

export default defineComponent({
  name: 'UserView',
  setup() {
    const router = useRouter();
    const authStore = useAuthStore();
    const awards = ref<Award[]>([]);
    const isDrawing = ref(false);
    const drawResult = ref<string | null>(null);
    const isLoading = ref(false);
    const error = ref<string | null>(null);
    const showPrizeDialog = ref(false)
    const prizeResult = ref('')
    // 获取奖项列表
    const fetchAwards = async () => {
      isLoading.value = true;
      error.value = null;
      try {
        awards.value = await awardApi.getAwards();
      } catch (err) {
        error.value = '获取奖项列表失败，请刷新重试';
        console.error(err);
      } finally {
        isLoading.value = false;
      }
    };
    // 加载奖项
    const loadAwards = async () => {
      try {
        awards.value = await awardApi.getAwards()
      } catch (error) {
        console.error('加载奖项失败:', error)
      }
    }
    // 执行抽奖
    const handleDraw = async () => {
      isDrawing.value = true;
      showPrizeDialog.value = false;

      try {
        const result = await awardApi.draw();

        // 安全访问message属性（关键修复）
        const message = result.message || '';

        // 中奖情况处理
        if (result.success && (result.prize || message.includes('恭喜'))) {
          prizeResult.value = result.prize ||
              message.split('：')[1]?.replace('！', '') ||
              '神秘奖品';
          showPrizeDialog.value = true;
        }
        // 未中奖情况
        else {
          alert(message || '很遗憾，您没有中奖');
        }

        await loadAwards();

      } catch (error) {
        alert('网络错误，请检查连接');
      } finally {
        isDrawing.value = false;
      }
    };

    // 关闭弹窗
    const closeDialog = () => {
      showPrizeDialog.value = false
    }
    // 跳转到登录/注册
    const goToLogin = () => router.push('/home/login');
    const goToRegister = () => router.push('/home/register');

    // 初始化
    onMounted(() => {
      authStore.initialize();
      fetchAwards();
    });

    return {
      authStore,
      awards,
      isDrawing,
      drawResult,
      isLoading,
      error,
      handleDraw,
      goToLogin,
      goToRegister,
      closeDialog,
      showPrizeDialog,
      prizeResult
    };
  },
});
</script>




<style scoped>

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