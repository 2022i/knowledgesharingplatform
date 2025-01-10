<template>
  <div class="users-page">
    <!-- 搜索和筛选区域 -->
    <div class="filter-section">
      <el-form :inline="true" :model="filterForm">
        <el-form-item label="状态">
          <el-select v-model="filterForm.status" placeholder="选择状态">
            <el-option label="全部" value="" />
            <el-option label="正常" value="active" />
            <el-option label="禁用" value="disabled" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="filterForm.role" placeholder="选择角色">
            <el-option label="全部" value="" />
            <el-option label="普通用户" value="user" />
            <el-option label="管理员" value="admin" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input
            v-model="filterForm.keyword"
            placeholder="搜索用户名/邮箱"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 用户列表 -->
    <div class="users-table">
      <el-table
        v-loading="loading"
        :data="users"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="用户信息" min-width="200">
          <template #default="{ row }">
            <div class="user-info">
              <el-avatar :size="40" :src="row.avatar" />
              <div class="user-details">
                <div class="username">{{ row.username }}</div>
                <div class="email">{{ row.email }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="role" label="角色" width="120">
          <template #default="{ row }">
            <el-tag :type="row.role === 'admin' ? 'danger' : ''">
              {{ row.role === 'admin' ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'danger'">
              {{ row.status === 'active' ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="160">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="lastLoginTime" label="最后登录" width="160">
          <template #default="{ row }">
            {{ formatDate(row.lastLoginTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button-group>
              <el-button 
                size="small" 
                @click="handleEdit(row)"
              >
                编辑
              </el-button>
              <el-button 
                :type="row.status === 'active' ? 'danger' : 'success'"
                size="small" 
                @click="handleToggleStatus(row)"
              >
                {{ row.status === 'active' ? '禁用' : '启用' }}
              </el-button>
              <el-button 
                :type="row.role === 'admin' ? 'danger' : 'success'"
                size="small" 
                @click="handleToggleRole(row)"
              >
                {{ row.role === 'admin' ? '取消管理员' : '设为管理员' }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          layout="total, sizes, prev, pager, next"
        />
      </div>
    </div>

    <!-- 编辑用户对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑用户信息"
      width="500px"
    >
      <el-form
        v-if="currentUser"
        :model="editForm"
        label-width="100px"
      >
        <el-form-item label="用户名">
          <el-input v-model="editForm.username" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.email" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="editForm.role">
            <el-option label="普通用户" value="user" />
            <el-option label="管理员" value="admin" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="editForm.status">
            <el-option label="正常" value="active" />
            <el-option label="禁用" value="disabled" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSaveUser">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDate } from '../../utils/date'

// 列表数据
const loading = ref(false)
const users = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 筛选表单
const filterForm = ref({
  status: '',
  role: '',
  keyword: ''
})

// 编辑相关
const editDialogVisible = ref(false)
const currentUser = ref(null)
const editForm = ref({
  username: '',
  email: '',
  role: '',
  status: ''
})

// 获取用户列表
const fetchUsers = async () => {
  loading.value = true
  try {
    // 模拟API请求
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 模拟数据
    users.value = [
      {
        id: 1,
        username: '张三',
        email: 'zhangsan@example.com',
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        role: 'admin',
        status: 'active',
        createTime: '2024-01-01 10:00:00',
        lastLoginTime: '2024-03-15 15:30:00'
      },
      {
        id: 2,
        username: '李四',
        email: 'lisi@example.com',
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        role: 'user',
        status: 'active',
        createTime: '2024-02-01 14:20:00',
        lastLoginTime: '2024-03-14 09:15:00'
      }
    ]
    total.value = users.value.length
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 编辑用户
const handleEdit = (user: any) => {
  currentUser.value = user
  editForm.value = {
    username: user.username,
    email: user.email,
    role: user.role,
    status: user.status
  }
  editDialogVisible.value = true
}

// 保存用户信息
const handleSaveUser = async () => {
  try {
    // 模拟API请求
    await new Promise(resolve => setTimeout(resolve, 500))
    
    Object.assign(currentUser.value, editForm.value)
    ElMessage.success('保存成功')
    editDialogVisible.value = false
  } catch (error) {
    console.error('保存用户信息失败:', error)
    ElMessage.error('保存用户信息失败')
  }
}

// 切换用户状态
const handleToggleStatus = async (user: any) => {
  try {
    const action = user.status === 'active' ? '禁用' : '启用'
    await ElMessageBox.confirm(`确定要${action}该用户吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    user.status = user.status === 'active' ? 'disabled' : 'active'
    ElMessage.success(`${action}成功`)
  } catch {
    // 用户取消操作
  }
}

// 切换用户角色
const handleToggleRole = async (user: any) => {
  try {
    const action = user.role === 'admin' ? '取消管理员' : '设为管理员'
    await ElMessageBox.confirm(`确定要${action}吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    user.role = user.role === 'admin' ? 'user' : 'admin'
    ElMessage.success(`${action}成功`)
  } catch {
    // 用户取消操作
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchUsers()
}

// 重置筛选
const resetFilter = () => {
  filterForm.value = {
    status: '',
    role: '',
    keyword: ''
  }
  handleSearch()
}

// 分页相关方法
const handleSizeChange = (val: number) => {
  pageSize.value = val
  fetchUsers()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchUsers()
}

// 初始化
onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.users-page {
  padding: 20px;
  background: #fff;
  border-radius: 4px;
}

.filter-section {
  margin-bottom: 20px;
}

.users-table {
  margin-top: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-details {
  .username {
    font-weight: 500;
    margin-bottom: 4px;
  }

  .email {
    font-size: 12px;
    color: #666;
  }
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 