<template>
  <div class="user-management">
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="logo-container">
        <i class="fas fa-gift logo-icon"></i>
        <span class="logo">抽奖管理系统</span>
      </div>
      <div class="auth-buttons" v-if="!authStore.isAuthenticated">
        <button class="login-btn" @click="goToLogin">
          <i class="fas fa-sign-in-alt"></i> 登录
        </button>
        <button class="register-btn" @click="goToRegister">
          <i class="fas fa-user-plus"></i> 注册
        </button>
      </div>
      <div class="user-info" v-else>
        <span>当前用户：{{ authStore.currentUser?.username }}</span>
        <button @click="authStore.logout" class="logout-btn">
          <i class="fas fa-sign-out-alt"></i> 退出登录
        </button>
      </div>
    </div>

    <div class="content">
      <h1 class="content-title"><i class="fas fa-trophy"></i> 开始抽奖</h1>

      <!-- 抽奖转盘区域 -->
      <div class="lottery-section">
        <div class="wheel-outer-container">
          <div class="wheel-container">
            <div class="wheel-light"></div>
            <div class="wheel" :style="{ transform: `rotate(${wheelRotation}deg)` }">
              <div
                  v-for="(award, index) in awards"
                  :key="index"
                  class="wheel-sector"
                  :style="{
                transform: `rotate(${index * sectorAngle}deg) skewY(${sectorSkew}deg)`,
                backgroundColor: sectorColors[index % sectorColors.length]
              }"
              >
                <div class="sector-content">
                  <span class="sector-text">{{ award.name }}</span>
                  <i class="sector-icon fas fa-gift"></i>
                </div>
              </div>
            </div>
            <div class="wheel-center">
              <!-- 尖形指针容器 -->
              <div class="wheel-pointer-container">
                <div class="wheel-pointer">
                  <div class="pointer-triangle"></div>
                  <div class="pointer-shaft"></div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <button
            class="draw-button"
            @click="handleDraw"
            :disabled="isDrawing || awards.length === 0"
        >
          <i class="fas fa-dice" :class="{ 'fa-spin': isDrawing }"></i>
          {{ isDrawing ? '抽奖中...' : '开始抽奖' }}
        </button>
      </div>

      <h1 class="content-title"><i class="fas fa-trophy"></i> 奖项列表</h1>
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
      <div v-if="showPrizeDialog" class="dialog-overlay">
        <div class="prize-dialog" :class="{ 'error-style': !prizeResult }">
          <div class="dialog-icon">
            <i v-if="prizeResult" class="fas fa-gift"></i>
            <i v-else class="fas fa-exclamation-triangle"></i>
          </div>
          <h3>{{ prizeResult ? '恭喜中奖!' : '系统提示' }}</h3>
          <p v-if="prizeResult">您获得了：<strong>{{ prizeResult }}</strong></p>
          <button @click="closeDialog" class="dialog-btn">确定</button>
        </div>
      </div>
    </div>

    <!-- 中奖记录表格 -->
    <div class="award-records">
      <h2><i class="fas fa-medal"></i> 我的中奖记录</h2>
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
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';
import { awardApi } from '@/api/awardApi';
import type { Award, AwardRecord } from '@/types/award';

const router = useRouter();
const authStore = useAuthStore();
const awards = ref<Award[]>([]);
const isDrawing = ref(false);
const showPrizeDialog = ref(false);
const prizeResult = ref<string | undefined>(undefined);
const awardRecords = ref<AwardRecord[]>([]);
const wheelRotation = ref(0);
const targetIndex = ref(-1); // 存储目标奖项索引

// 转盘相关变量
const sectorColors = [
  '#FF9800', '#E91E63', '#2196F3', '#4CAF50',
  '#9C27B0', '#FF5722', '#03A9F4', '#8BC34A'
];

// 转盘分区角度计算
const sectorAngle = computed(() => awards.value.length ? 360 / awards.value.length : 0);
const sectorSkew = computed(() => awards.value.length ? 90 - sectorAngle.value : 0);

// 格式化日期时间
const formatDateTime = (dateTimeStr: string) => {
  const date = new Date(dateTimeStr);
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  });
};

// 获取奖项列表
const fetchAwards = async () => {
  try {
    awards.value = await awardApi.getAwards();
    console.log("获取奖项成功", awards.value);
  } catch (err) {
    console.error('获取奖项失败:', err);
  }
};

// 获取中奖记录
const loadAwardRecords = async () => {
  try {
    if (authStore.isAuthenticated && authStore.currentUser?.id) {
      awardRecords.value = await awardApi.getUserAwardRecords();
      console.log('成功获取获奖记录:', awardRecords.value);
    }
  } catch (err) {
    console.error('加载中奖记录失败:', err);
  }
};

// 计算目标旋转角度
const calculateTargetRotation = (targetIndex: number, startRotation: number) => {
  const n = awards.value.length;
  if (n === 0) return startRotation;

  // 每个奖项的角度大小
  const awardAngle = 360 / n;

  // 目标奖项的中心角度（偏移半角使指针指向中心）
  const targetCenterAngle = targetIndex * awardAngle + awardAngle / 2;

  // 使旋转结束时指针指向目标奖项
  // 基本旋转圈数：8圈 + 调整到目标位置
  // 注意：减去起始角度的余数，确保指针准确指向
  return startRotation + 360 * 8 - (targetCenterAngle % 360) + 360;
};

// 执行抽奖
const handleDraw = async () => {
  if (!authStore.currentUser?.id) {
    prizeResult.value = '请先登录';
    showPrizeDialog.value = true;
    return;
  }

  isDrawing.value = true;
  const startRotation = wheelRotation.value;

  try {
    const result = await awardApi.draw();
    prizeResult.value = result.prize;

    // 查找目标奖项的索引
    targetIndex.value = awards.value.findIndex(a => a.id === result.awardId);
    let finalTargetRotation;

    // 如果找到目标奖项
    if (targetIndex.value >= 0) {
      finalTargetRotation = calculateTargetRotation(targetIndex.value, startRotation);
    } else {
      // 未中奖，使用随机旋转
      finalTargetRotation = startRotation + 360 * 8 + Math.floor(Math.random() * 360);
    }

    // 开始动画
    const spinDuration = 4000; // 4秒
    const startTime = Date.now();

    const animateWheel = () => {
      const currentTime = Date.now();
      const elapsed = currentTime - startTime;

      if (elapsed < spinDuration) {
        const progress = elapsed / spinDuration;

        // 三阶段缓动函数
        let easeProgress;
        if (progress < 0.2) {
          // 快速启动阶段
          easeProgress = progress * progress * 2.5;
        } else if (progress < 0.8) {
          // 匀速旋转阶段
          easeProgress = 0.1 + (progress - 0.2) * 0.8;
        } else {
          // 减速缓冲阶段
          const lastProgress = (progress - 0.8) / 0.2;
          easeProgress = 0.9 + (1 - Math.pow(1 - lastProgress, 4)) * 0.1;
        }

        wheelRotation.value = startRotation + (finalTargetRotation - startRotation) * easeProgress;
        requestAnimationFrame(animateWheel);
      } else {
        // 确保最终位置准确
        wheelRotation.value = finalTargetRotation;
        // 旋转结束，立即显示结果（没有延迟）
        showPrizeDialog.value = true;
        isDrawing.value = false;
        // 刷新记录
        loadAwardRecords();
        fetchAwards();
      }
    };

    requestAnimationFrame(animateWheel);
  } catch (error) {
    console.error('抽奖失败:', error);
    prizeResult.value = '抽奖失败，请重试';
    showPrizeDialog.value = true;
    isDrawing.value = false;
  }
};

// 关闭弹窗
const closeDialog = () => {
  showPrizeDialog.value = false;
};

// 跳转到登录/注册
const goToLogin = () => router.push('/home/login');
const goToRegister = () => router.push('/home/register');

// 初始化数据
onMounted(async () => {
  await authStore.initialize();
  await fetchAwards();
  if (authStore.isAuthenticated) {
    await loadAwardRecords();
  }
});
</script>

<style scoped>
/* 基础样式 */
.user-management {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f8f9fa;
  font-family: 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

/* 导航栏样式 */
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 25px;
  background: linear-gradient(135deg, #1e88e5, #0d47a1);
  color: white;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(30, 136, 229, 0.3);
  margin-bottom: 30px;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 24px;
  color: #FFD700;
}

.logo {
  font-size: 20px;
  font-weight: bold;
}

.auth-buttons {
  display: flex;
  gap: 15px;
}

.login-btn, .register-btn, .logout-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 6px;
}

.login-btn {
  background-color: rgba(255, 255, 255, 0.2);
  color: white;
}

.login-btn:hover {
  background-color: rgba(255, 255, 255, 0.3);
}

.register-btn {
  background-color: #4CAF50;
  color: white;
}

.register-btn:hover {
  background-color: #3d8b40;
}

.logout-btn {
  background-color: rgba(255, 255, 255, 0.15);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.logout-btn:hover {
  background-color: rgba(255, 255, 255, 0.25);
}

/* 内容区域样式 */
.content {
  background-color: white;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
  margin-bottom: 30px;
}

.content-title {
  color: #1e88e5;
  font-size: 28px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 抽奖区域样式 */
.lottery-section {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  margin-bottom: 30px;
  padding-bottom: 30px;
  border-bottom: 1px solid #eee;
}

.wheel-outer-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  margin-bottom: 20px;
}

.wheel-container {
  position: relative;
  width: 320px;
  height: 320px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.wheel-light {
  position: absolute;
  width: 110%;
  height: 110%;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255,255,255,0) 58%, rgba(255,215,0,0.3) 60%, rgba(255,215,0,0) 70%);
  animation: pulse 2s infinite alternate;
  z-index: 1;
}

.wheel {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  position: relative;
  overflow: hidden;
  border: 8px solid #E83C3D;
  box-shadow:
      0 0 0 8px #FFDF3F,
      0 0 10px rgba(0, 0, 0, 0.3),
      0 0 30px rgba(255, 215, 0, 0.2);
  transition: transform 4s cubic-bezier(0.1, 0.05, 0.15, 1);
  z-index: 2;
  background: #FFF3D0;
}

.wheel::before {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: repeating-conic-gradient(
      transparent 0deg 10deg,
      rgba(255, 255, 255, 0.1) 10deg 20deg
  );
  z-index: 1;
}

.wheel-sector {
  position: absolute;
  width: 50%;
  height: 50%;
  top: 0;
  right: 0;
  transform-origin: 0% 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  border-right: 2px dashed rgba(255, 255, 255, 0.3);
  box-sizing: border-box;
}

.sector-content {
  transform: rotate(45deg);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  padding-bottom: 40%;
}

.sector-text {
  width: 80px;
  font-weight: 600;
  color: white;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  font-size: 14px;
  text-align: center;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sector-icon {
  font-size: 18px;
  color: white;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
}

.wheel-center {
  position: absolute;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: radial-gradient(circle, #FFDF3F, #E83C3D);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
  border: 4px solid white;
}

/* 尖形指针样式 */
.wheel-pointer-container {
  position: absolute;
  top: -50px;
  width: 40px;
  height: 70px;
  z-index: 15;
  display: flex;
  justify-content: center;
}

.wheel-pointer {
  position: relative;
  width: 100%;
  height: 100%;
  z-index: 12;
  display: flex;
  justify-content: center;
  animation: pointerPulse 1s infinite alternate;
}

.pointer-triangle {
  position: absolute;
  top: 0;
  width: 0;
  height: 0;
  border-left: 20px solid transparent;
  border-right: 20px solid transparent;
  border-bottom: 50px solid #E83C3D;
  z-index: 11;
  filter: drop-shadow(0 3px 5px rgba(0, 0, 0, 0.3));
}

.pointer-shaft {
  position: absolute;
  top: 50px;
  width: 15px;
  height: 20px;
}

.draw-button {
  background: linear-gradient(135deg, #E83C3D, #FF5722);
  color: white;
  border: none;
  padding: 15px 40px;
  font-size: 18px;
  font-weight: 600;
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 8px 20px rgba(232, 60, 61, 0.3);
  display: flex;
  align-items: center;
  gap: 12px;
  position: relative;
  overflow: hidden;
  z-index: 5;
}

.draw-button::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.3) 0%, rgba(255,255,255,0) 70%);
  opacity: 0;
  transition: opacity 0.3s;
}

.draw-button:hover:not(:disabled)::before {
  opacity: 1;
  animation: ripple 1s infinite;
}

.draw-button:hover:not(:disabled) {
  transform: translateY(-3px);
  box-shadow: 0 12px 25px rgba(232, 60, 61, 0.4);
}

.draw-button:disabled {
  background: #cccccc;
  cursor: not-allowed;
}

/* 添加新的动画效果 */
@keyframes pulse {
  0% { opacity: 0.3; transform: scale(0.98); }
  100% { opacity: 1; transform: scale(1.02); }
}

@keyframes pointerPulse {
  0% { transform: scale(1); }
  100% { transform: scale(1.1); }
}

@keyframes rotate {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes ripple {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 表格样式 */
.award-table, .award-record-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  margin-top: 20px;
  margin-bottom: 30px;
}

.award-table th {
  background-color: #1e88e5;
  padding: 16px 20px;
  text-align: left;
  font-weight: 600;
  color: white;
}

.award-table td, .award-record-table td {
  padding: 14px 20px;
  border-bottom: 1px solid #f0f4f8;
  color: #333;
}

.award-table tr:last-child td {
  border-bottom: none;
}

.award-table tr:hover td, .award-record-table tr:hover td {
  background-color: #f8fbff;
}

/* 中奖记录区域 */
.award-records {
  background-color: white;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
}

.award-records h2 {
  color: #1e88e5;
  font-size: 24px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.award-record-table th {
  background-color: #4CAF50;
  padding: 16px 20px;
  text-align: left;
  font-weight: 600;
  color: white;
}

/* 中奖弹窗样式 */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.prize-dialog {
  background: white;
  border-radius: 16px;
  padding: 40px;
  text-align: center;
  max-width: 500px;
  width: 90%;
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
  animation: popIn 0.4s ease;
  position: relative;
  overflow: hidden;
}

.prize-dialog.error-style {
  background: linear-gradient(135deg, #fef4f4, #ffebee);
  border-left: 6px solid #e53935;
}

.prize-dialog::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 6px;
  background: linear-gradient(90deg, #ff9800, #1e88e5);
}

.dialog-icon {
  font-size: 60px;
  color: #ff9800;
  margin-bottom: 20px;
}

.error-style .dialog-icon {
  color: #e53935;
}

.prize-dialog h3 {
  font-size: 32px;
  color: #1e88e5;
  margin-bottom: 20px;
}

.prize-dialog p {
  font-size: 24px;
  color: #333;
  margin-bottom: 30px;
}

.prize-dialog strong {
  color: #ff5722;
  font-size: 28px;
}

.dialog-btn {
  background: linear-gradient(135deg, #1e88e5, #0d47a1);
  color: white;
  border: none;
  padding: 12px 40px;
  font-size: 18px;
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s;
  font-weight: 500;
}

.dialog-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(30, 136, 229, 0.4);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .navbar {
    flex-direction: column;
    gap: 15px;
  }

  .content, .award-records {
    padding: 20px;
  }

  .lottery-section {
    flex-direction: column;
    align-items: center;
  }

  .wheel-container {
    width: 250px;
    height: 250px;
  }

  .wheel-pointer-container {
    top: -40px;
    height: 60px;
  }

  .pointer-triangle {
    border-left: 16px solid transparent;
    border-right: 16px solid transparent;
    border-bottom: 40px solid #E83C3D;
  }

  .pointer-shaft {
    top: 40px;
    width: 12px;
    height: 20px;
  }
}

@media (max-width: 480px) {
  .wheel-container {
    width: 220px;
    height: 220px;
  }

  .sector-text {
    font-size: 12px;
    width: 70px;
  }

  .draw-button {
    padding: 12px 30px;
    font-size: 16px;
  }
}

/* 动画效果 */
@keyframes popIn {
  0% {
    opacity: 0;
    transform: scale(0.8) translateY(20px);
  }
  100% {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

.fa-spin {
  animation: fa-spin 1s infinite linear;
}

@keyframes fa-spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>