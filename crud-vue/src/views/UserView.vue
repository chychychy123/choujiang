<template>
  <div class="user-management">
    <!-- 新增导航栏 -->
    <div class="navbar">
      <span class="logo">抽奖管理系统</span>

      <!-- 未登录状态 -->
      <div class="auth-buttons" v-if="!authStore.isAuthenticated">
        <button class="login-btn" @click="goToLogin">登录</button>
        <button class="register-btn" @click="goToRegister">注册</button>
      </div>

      <!-- 已登录状态 -->
      <div class="user-info" v-else>
        <span>当前用户：{{ authStore.currentUser?.username }}</span>
        <button @click="authStore.logout" class="logout-btn">退出登录</button>
      </div>
    </div>

    <h1>用户信息表</h1>
    <button class="add-btn" @click="showAddForm">新增</button>
    <div class="search-box">
      <input
          v-model="searchId"
          type="number"
          placeholder="输入ID查询"
          @keyup.enter="searchUserById"
      >
      <button class="search-btn" @click="searchUserById">查询</button>
    </div>
    <table class="user-table">
      <thead>
      <tr>
        <th>ID</th>
        <th>用户名</th>
        <th>年龄</th>
        <th>性别</th>
        <th>地址</th>
        <th>电话</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in paginatedUsers" :key="user.id">
        <td>{{ user.id }}</td>
        <td>{{ user.username }}</td>
        <td>{{ user.age }}</td>
        <td>{{ user.sex }}</td>
        <td>{{ user.address }}</td>
        <td>{{ user.phone }}</td>
        <td>
          <button class="edit-btn" @click="editUser(user)">编辑</button>
          <button class="delete-btn" @click="deleteUser(user.id)">删除</button>
        </td>
      </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button
          class="page-arrow"
          :disabled="currentPage === 1"
          @click="goToPage(currentPage - 1)"
      >
        &lt;
      </button>

      <button
          v-for="page in visiblePages"
          :key="page"
          class="page-number"
          :class="{ 'active': page === currentPage }"
          @click="goToPage(page)"
      >
        {{ page }}
      </button>

      <button
          class="page-arrow"
          :disabled="currentPage === totalPages"
          @click="goToPage(currentPage + 1)"
      >
        &gt;
      </button>
    </div>

    <div v-if="showDialog" class="dialog-overlay">
      <div class="dialog">
        <h2>{{ isEditing ? '编辑用户' : '新增用户' }}</h2>
        <form @submit.prevent="submitForm">
          <div class="form-group">
            <label>用户名:</label>
            <input v-model="formData.username" required />
          </div>
          <div class="form-group">
            <label>年龄:</label>
            <input v-model="formData.age" type="number" required />
          </div>
          <div class="form-group">
            <label>性别:</label>
            <select v-model="formData.sex" required>
              <option value="男">男</option>
              <option value="女">女</option>
            </select>
          </div>
          <div class="form-group">
            <label>地址:</label>
            <input v-model="formData.address" required />
          </div>
          <div class="form-group">
            <label>电话:</label>
            <input v-model="formData.phone" required />
          </div>
          <div class="dialog-actions">
            <button type="button" @click="closeDialog">取消</button>
            <button type="submit">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed } from 'vue';
import { userApi } from '@/api/userApi';
import type {CurrentUser, IUser, UserForm} from '@/types/user';
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
export default defineComponent({
  name: 'UserView',
  setup() {
    const authStore = useAuthStore()
// 组件挂载时初始化状态
    onMounted(() => {
      authStore.initialize()
    })
    const router = useRouter()
    const goToLogin = () => {
      router.push('/home/login') // 跳转到登录页
    }

    const goToRegister = () => {
      router.push('/home/register') // 跳转到注册页
    }

    const searchId = ref('');
    const users = ref<IUser[]>([]);
    const showDialog = ref(false);
    const isEditing = ref(false);
    const currentUserId = ref<number | null>(null);
    const formData = ref<UserForm>({
      username: '',
      age: 0,
      sex: '男',
      address: '',
      phone: ''
    });
    const searchUserById = async () => {
      if (!searchId.value) {
        // 如果搜索框为空，恢复显示所有用户
        loadAllUsers();
        return;
      }
      try {
        const user = await userApi.getUserById(parseInt(searchId.value));
        if (user) {
          users.value = [user]; // 显示查询到的单个用户
        } else {
          users.value = []; // 没有查询到结果
        }
      } catch (error) {
        console.error('查询用户失败:', error);
        users.value = [];
      }
    };
    // 加载所有用户的方法
    const loadAllUsers = async () => {
      users.value = await userApi.getAllUsers();
    };
    const currentPage = ref(1);
    const itemsPerPage = 10;

    const totalPages = computed(() => {
      return Math.max(1, Math.ceil(users.value.length / itemsPerPage));
    });

    const paginatedUsers = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      return users.value.slice(start, end);
    });

    const visiblePages = computed(() => {
      const pages = [];
      const maxVisible = 3;
      let start = 1;

      if (totalPages.value > maxVisible) {
        start = Math.max(1, Math.min(
            currentPage.value - Math.floor(maxVisible / 2),
            totalPages.value - maxVisible + 1
        ));
      }

      const end = Math.min(start + maxVisible - 1, totalPages.value);

      for (let i = start; i <= end; i++) {
        pages.push(i);
      }

      return pages;
    });

    const loadUsers = async () => {
      try {
        users.value = await userApi.getAllUsers();
        // 不要重置 currentPage，否则分页不会工作
        // currentPage.value = 1;
      } catch (error) {
        console.error('加载用户数据失败:', error);
      }
    };

    const goToPage = (page: number) => {
      if (page >= 1 && page <= totalPages.value) {
        currentPage.value = page;
      }
    };

    const showAddForm = () => {
      isEditing.value = false;
      currentUserId.value = null;
      resetForm();
      showDialog.value = true;
    };

    const editUser = (user: IUser) => {
      isEditing.value = true;
      currentUserId.value = user.id;
      formData.value = {
        username: user.username,
        age: user.age,
        sex: user.sex,
        address: user.address,
        phone: user.phone
      };
      showDialog.value = true;
    };

    const submitForm = async () => {
      try {
        if (isEditing.value && currentUserId.value) {
          await userApi.updateUser(currentUserId.value, formData.value);
        } else {
          await userApi.createUser(formData.value);
        }
        closeDialog();
        await loadUsers();
      } catch (error) {
        console.error('保存用户失败:', error);
      }
    };

    const deleteUser = async (id: number) => {
      if (confirm('确定要删除该用户吗?')) {
        try {
          await userApi.deleteUser(id);
          await loadUsers();
          // 如果当前页数据删除后为空，自动回到上一页（避免空页）
          if (paginatedUsers.value.length === 0 && currentPage.value > 1) {
            currentPage.value--;
          }
        } catch (error) {
          console.error('删除用户失败:', error);
        }
      }
    };

    const closeDialog = () => {
      showDialog.value = false;
    };

    const resetForm = () => {
      formData.value = {
        username: '',
        age: 0,
        sex: '男',
        address: '',
        phone: ''
      };
    };

    onMounted(() => {
      loadUsers();
    });

    return {
      users,
      showDialog,
      isEditing,
      formData,
      currentPage,
      totalPages,
      paginatedUsers,
      visiblePages,
      showAddForm,
      editUser,
      submitForm,
      deleteUser,
      closeDialog,
      goToPage,
      searchId,
      searchUserById,
      goToLogin,
      goToRegister,
      authStore
    };
  }
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

.search-btn {
  background-color: #1e88e5;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.search-btn:hover {
  background-color: #1976d2;
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

.add-btn {
  background-color: #1e88e5;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.add-btn:hover {
  background-color: #1976d2;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
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

.edit-btn {
  background-color: #1e88e5;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 5px;
}

.edit-btn:hover {
  background-color: #1976d2;
}

.delete-btn {
  background-color: #e53935;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.delete-btn:hover {
  background-color: #d32f2f;
}

.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.dialog {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.dialog h2 {
  margin-top: 0;
  color: #333;
}

.form-group {
  margin-bottom: 15px;
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

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
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

/* 优化后的分页样式 - 完全匹配图片描述 */
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 5px;
}

.page-arrow, .page-number {
  width: 32px;
  height: 32px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  background-color: #f5f5f5;
  color: #333;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.2s ease;
}

.page-arrow:hover:not(:disabled),
.page-number:hover:not(.active) {
  background-color: #e0e0e0;
}

.page-arrow:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-number.active {
  background-color: #1e88e5;
  color: white;
  font-weight: normal;
  border-color: #1e88e5;
}
</style>