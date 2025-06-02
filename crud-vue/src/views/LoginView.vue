<template>
  <div class="auth-container">
    <!-- 粒子背景容器 -->
    <canvas ref="particlesRef" class="particles-container"></canvas>

    <!-- 背景装饰元素 -->
    <div class="decoration circle1"></div>
    <div class="decoration circle2"></div>
    <div class="decoration circle3"></div>

    <!-- 登录卡片 -->
    <div class="auth-card">
      <div class="card-header">
        <div class="logo">
          <i class="fas fa-shield-alt"></i>
        </div>
        <h1 class="auth-title">用户登录</h1>
        <p class="auth-subtitle">欢迎回来，请登录您的账户</p>
      </div>

      <form @submit.prevent="handleLogin" class="auth-form">
        <div class="form-group">
          <div class="input-with-icon">
            <i class="fas fa-user"></i>
            <input
                v-model="form.username"
                type="text"
                placeholder="用户名"
                required
            >
          </div>
        </div>

        <div class="form-group">
          <div class="input-with-icon">
            <i class="fas fa-lock"></i>
            <input
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="密码"
                required
            >
            <i
                class="toggle-password fas"
                :class="showPassword ? 'fa-eye-slash' : 'fa-eye'"
                @click="showPassword = !showPassword"
            ></i>
          </div>
        </div>

        <div class="remember-forgot">
          <label class="remember-me">
            <input type="checkbox" v-model="rememberMe"> 记住我
          </label>
          <a href="#" class="forgot-password">忘记密码?</a>
        </div>

        <button type="submit" class="auth-btn" :disabled="loading">
          <div class="btn-content">
            <i class="fas fa-sign-in-alt" v-if="!loading"></i>
            <div class="spinner" v-if="loading"></div>
            {{ loading ? '登录中...' : '立即登录' }}
          </div>
          <div class="btn-pulse"></div>
        </button>

        <div class="auth-footer">
          <div class="divider"></div>
          <p>还没有账号? <router-link to="/home/register">立即注册</router-link></p>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { defineComponent } from 'vue'


const router = useRouter()
const authStore = useAuthStore()

const form = ref({
  username: '',
  password: ''
})
const rememberMe = ref(false)
const loading = ref(false)
const showPassword = ref(false)
const particlesRef = ref(null)

// 处理登录 - 修复版
const handleLogin = async () => {
  try {
    loading.value = true

    // 调用原始的登录方法
    await authStore.loginC(form.value)

    // 登录成功后跳转到用户页面
    router.push('/home/users')

  } catch (error) {
    console.error('登录失败:', error)
    // 在实际项目中，这里可以添加错误提示逻辑
    // 例如使用Toast、Notification等组件显示错误信息
  } finally {
    loading.value = false
  }
}

// 粒子系统初始化（非功能性代码，不影响登录功能）
const initParticles = () => {
  const canvas = particlesRef.value
  if (!canvas) return

  canvas.width = window.innerWidth
  canvas.height = window.innerHeight
}

onMounted(() => {
  initParticles()
})
</script>

<style>
/* 基础重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

/* 主容器 */
.auth-container {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  overflow: hidden;
  background: linear-gradient(135deg, #0d47a1, #1a237e, #1e88e5);
  background-size: 300% 300%;
  animation: gradientAnimation 15s ease infinite;
}

@keyframes gradientAnimation {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

/* 粒子画布 */
.particles-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

/* 装饰元素 */
.decoration {
  position: absolute;
  border-radius: 50%;
  filter: blur(60px);
  z-index: 2;
  opacity: 0.5;
}

.circle1 {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, #ff9a9e, #fad0c4);
  top: 10%;
  left: 5%;
  animation: float 15s infinite ease-in-out;
}

.circle2 {
  width: 250px;
  height: 250px;
  background: linear-gradient(135deg, #a1c4fd, #c2e9fb);
  bottom: 15%;
  right: 8%;
  animation: float 18s infinite ease-in-out;
  animation-delay: 2s;
}

.circle3 {
  width: 200px;
  height: 200px;
  background: linear-gradient(135deg, #d4fc79, #96e6a1);
  top: 50%;
  right: 20%;
  animation: float 20s infinite ease-in-out;
  animation-delay: 4s;
}

@keyframes float {
  0% { transform: translate(0, 0); }
  25% { transform: translate(-50px, -30px); }
  50% { transform: translate(-20px, 40px); }
  75% { transform: translate(40px, -20px); }
  100% { transform: translate(0, 0); }
}

/* 登录卡片 */
.auth-card {
  position: relative;
  z-index: 10;
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  padding: 40px;
  border-radius: 20px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2),
  0 0 0 1px rgba(255, 255, 255, 0.1) inset;
  width: 100%;
  max-width: 450px;
  transition: transform 0.4s ease, box-shadow 0.4s ease;
  overflow: hidden;
}

.auth-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at top right, rgba(255, 255, 255, 0.1), transparent 70%);
  pointer-events: none;
  z-index: -1;
}

.auth-card:hover {
  transform: translateY(-10px) scale(1.02);
  box-shadow: 0 20px 45px rgba(0, 0, 0, 0.3),
  0 0 0 1px rgba(255, 255, 255, 0.15) inset;
}

/* 卡片头部 */
.card-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  width: 70px;
  height: 70px;
  background: linear-gradient(135deg, #1e88e5, #0d47a1);
  border-radius: 50%;
  margin: 0 auto 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 20px rgba(30, 136, 229, 0.4);
}

.logo i {
  font-size: 32px;
  color: white;
}

.auth-title {
  color: white;
  font-size: 32px;
  margin-bottom: 8px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.auth-subtitle {
  color: rgba(255, 255, 255, 0.85);
  font-size: 16px;
}

/* 表单样式 */
.auth-form {
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.form-group {
  position: relative;
}

.input-with-icon {
  position: relative;
}

.input-with-icon i {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: rgba(255, 255, 255, 0.7);
  font-size: 18px;
  transition: all 0.3s;
}

.input-with-icon input {
  width: 100%;
  padding: 16px 20px 16px 52px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  font-size: 16px;
  color: white;
  transition: all 0.3s;
}

.input-with-icon input::placeholder {
  color: rgba(255, 255, 255, 0.6);
}

.input-with-icon input:focus {
  background: rgba(255, 255, 255, 0.15);
  border-color: rgba(255, 255, 255, 0.4);
  box-shadow: 0 0 0 3px rgba(30, 136, 229, 0.3);
  outline: none;
}

.toggle-password {
  position: absolute;
  right: 18px;
  top: 50%;
  transform: translateY(-50%);
  color: rgba(255, 255, 255, 0.7);
  cursor: pointer;
  transition: all 0.3s;
}

.toggle-password:hover {
  color: white;
}

/* 记住我 & 忘记密码 */
.remember-forgot {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 8px;
}

.forgot-password {
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  transition: all 0.3s;
}

.forgot-password:hover {
  color: white;
  text-decoration: underline;
}

/* 登录按钮 */
.auth-btn {
  position: relative;
  background: transparent;
  border: none;
  padding: 0;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  margin-top: 10px;
  height: 54px;
}

.btn-content {
  position: relative;
  z-index: 2;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  height: 100%;
  padding: 0 24px;
  font-size: 17px;
  font-weight: 600;
  color: white;
  transition: all 0.3s;
}

.btn-pulse {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #1e88e5, #0d47a1);
  z-index: 1;
  transition: all 0.3s;
}

.auth-btn:hover .btn-pulse {
  background: linear-gradient(135deg, #2196F3, #1565C0);
}

.auth-btn:disabled .btn-content {
  opacity: 0.8;
}

.auth-btn:disabled .btn-pulse {
  opacity: 0.7;
}

/* 加载动画 */
.spinner {
  width: 20px;
  height: 20px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top: 3px solid white;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 页脚样式 */
.auth-footer {
  text-align: center;
  margin-top: 25px;
  color: rgba(255, 255, 255, 0.8);
}

.divider {
  position: relative;
  height: 1px;
  background: linear-gradient(to right, transparent, rgba(255, 255, 255, 0.3), transparent);
  margin: 0 auto 20px;
  width: 80%;
}

.auth-footer a {
  color: white;
  text-decoration: none;
  font-weight: 500;
  position: relative;
}

.auth-footer a:hover {
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 600px) {
  .auth-card {
    padding: 30px 20px;
    max-width: 90%;
  }

  .auth-title {
    font-size: 26px;
  }

  .input-with-icon input {
    padding: 14px 18px 14px 48px;
  }
}

@media (max-width: 400px) {
  .remember-forgot {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .forgot-password {
    align-self: flex-end;
  }
}
</style>