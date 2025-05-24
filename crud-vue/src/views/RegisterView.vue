<template>
  <div class="auth-container">
    <div class="auth-card">
      <h1 class="auth-title">用户注册</h1>

      <form @submit.prevent="handleRegister" class="auth-form">
        <div class="form-group">
          <label>用户名</label>
          <input
              v-model="form.username"
              type="text"
              placeholder="请输入用户名"
              required
          >
        </div>

        <div class="form-group">
          <label>密码</label>
          <input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              required
          >
        </div>

        <div class="form-group">
          <label>确认密码</label>
          <input
              v-model="form.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              required
          >
        </div>

        <button type="submit" class="auth-btn">注册</button>

        <div class="auth-footer">
          已有账号？<router-link to="/login">立即登录</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '@/api/authApi'

export default defineComponent({
  setup() {
    const router = useRouter()
    const form = ref({
      username: '',
      password: '',
      confirmPassword: ''
    })

    const handleRegister = async () => {
      if (form.value.password !== form.value.confirmPassword) {
        alert('两次输入的密码不一致')
        return
      }

      try {
        await authApi.register(form.value)
        router.push('/home/login')
      } catch (error) {
        console.error('注册失败:', error)
      }
    }

    return { form, handleRegister }
  }
})
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #1e88e5, #0d47a1);
}

.auth-card {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  width: 400px;
  max-width: 90%;
}

.auth-title {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
  font-size: 24px;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  color: #555;
  font-size: 14px;
}

.form-group input {
  padding: 12px 15px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 16px;
  transition: border 0.3s;
}

.form-group input:focus {
  border-color: #1e88e5;
  outline: none;
}

.auth-btn {
  background-color: #1e88e5;
  color: white;
  border: none;
  padding: 14px;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.auth-btn:hover {
  background-color: #1565c0;
}

.auth-footer {
  text-align: center;
  margin-top: 20px;
  color: #666;
  font-size: 14px;
}

.auth-footer a {
  color: #1e88e5;
  text-decoration: none;
}

.auth-footer a:hover {
  text-decoration: underline;
}
</style>