<template>
  <div class="container">
    <div class="headContent">
      <h1>Account management</h1>
    </div>
    <div class="actions" id="account-mng">
      <div class="filters">
        <select id="role-filter" class="filter-select" @change="handleRoleChange">
          <option value="">All Role</option>
          <option value="staff">Staff</option>
          <option value="teacher">Teacher</option>
          <option value="student">Student</option>
        </select>
      </div>
      <button @click="showAddAccountPopup = true">
        <VsxIcon iconName="AddCircle" size="20" type="bold" />
        Add account
      </button>
    </div>
    <div class="actions">
      <div class="search-container">
        <input type="text" placeholder="Search..." class="search-field"/>
        <VsxIcon iconName="SearchNormal1" color="#ADB5BD" type="linear"/>
      </div>
    </div>

    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th class="center">No</th>
          <th>Full name</th>
          <th>Email</th>
          <th>Status</th>
          <th class="center">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(user, index) in users" :key="user.userId">
          <td class="center">{{ index + 1 + (currentPage - 1) * itemsPerPage }}</td>
          <td>{{ user?.fullName || 'N/A' }}</td>
          <td>{{ user?.email }}</td>
          <td :class="user.active ? 'active' : 'inactive'">
            {{ user.active ? 'Active' : 'Inactive' }}
          </td>
          <td class="center">
            <div class="icon-group">
              <!-- Nếu user.active === true, hiển thị icon "Unlock" -->
              <VsxIcon
                  v-if="user.active"
                  iconName="Unlock"
                  :size="30"
                  color="#171717"
                  type="linear"
                  @click="confirmToggleUserStatus(user)"
              />
              <!-- Nếu user.active === false, hiển thị icon "Lock" -->
              <VsxIcon
                  v-else
                  iconName="Lock"
                  :size="30"
                  color="#171717"
                  type="linear"
                  @click="confirmToggleUserStatus(user)"
              />
            </div>
          </td>
        </tr>
        </tbody>
      </table>
      <div class="pagination" v-if="totalPages > 1">
        <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">
          <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717"/>
        </button>
        <button v-for="page in displayedPages" :key="page" :class="{ active: page === currentPage }"
                @click="changePage(page)">
          {{ page }}
        </button>
        <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">
          <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717"/>
        </button>
      </div>
    </div>

    <div v-if="showAddAccountPopup" class="popup-overlay">
      <div class="popup">
        <div class="exit-icon">
          <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="showAddAccountPopup = false"/>
        </div>
        <div class="popup-title">
          <h2>Add account</h2>
        </div>
        <form @submit.prevent="addAccount">
          <div class="form-group">
            <label for="email">Email <span class="required">*</span></label>
            <input type="email" id="email" v-model="newAccount.email" required />
          </div>
          <div class="form-group">
            <label for="fullName">Full Name <span class="required">*</span></label>
            <input type="text" id="fullName" v-model="newAccount.fullName" required />
          </div>
          <div class="form-group">
            <label for="role">Role <span class="required">*</span></label>
            <select id="role-filter" class="filter-select" v-model="newAccount.role" required>
              <option value="" disabled>Select Role</option>
              <option value="staff">Staff</option>
              <option value="teacher">Teacher</option>
              <option value="student">Student</option>
            </select>
          </div>
          <div class="actions">
            <button type="submit" class="btn-submit">Create</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      users: [],
      showAddAccountPopup: false,
      currentPage: 1,
      itemsPerPage: 5,
      totalElements: 0,
      totalPages: 0,
      currentRole: "",
      newAccount: {
        email: "",
        fullName: "",
        japaneseName: "",
        role: "",
        active: true
      },
    }
  },
  methods: {
    async fetchUsersByRole(role) {
      this.isLoading = true; // Bắt đầu trạng thái loading
      try {
        const token = sessionStorage.getItem("jwtToken"); // Lấy token từ sessionStorage
        const response = await axios.get(
            `http://localhost:8088/fja-fap/user/get-users-by-role?role=${role}&page=${this.currentPage - 1}&size=${this.itemsPerPage}`,
            {headers: {Authorization: `Bearer ${token}`}}
        );
        // Cập nhật dữ liệu từ API
        this.users = response.data.result.content;
        this.totalElements = response.data.result.totalElements;
        this.totalPages = Math.ceil(this.totalElements / this.itemsPerPage);

        // Cập nhật danh sách trang hiển thị
        this.updateDisplayedPages();
      } catch (error) {
        console.error("Error fetching users:", error);
      } finally {
        this.isLoading = false; // Kết thúc trạng thái loading
      }
    },
    handleRoleChange(event) {
      const selectedRole = event.target.value.toUpperCase();
      this.currentRole = selectedRole; // Lưu role hiện tại
      this.currentPage = 1; // Reset về trang đầu
      this.fetchUsersByRole(selectedRole); // Gọi API với role mới
    },
    changePage(newPage) {
      if (newPage > 0 && newPage <= this.totalPages) {
        this.currentPage = newPage;
        this.fetchUsersByRole(this.currentRole); // Truyền role hiện tại
      }
    },
    updateDisplayedPages() {
      const pages = [];
      if (this.totalPages <= 5) {
        for (let i = 1; i <= this.totalPages; i++) {
          pages.push(i);
        }
      } else {
        if (this.currentPage <= 3) {
          pages.push(1, 2, 3, '...', this.totalPages);
        } else if (this.currentPage >= this.totalPages - 2) {
          pages.push(1, '...', this.totalPages - 2, this.totalPages - 1, this.totalPages);
        } else {
          pages.push(1, '...', this.currentPage, '...', this.totalPages);
        }
      }
      this.displayedPages = pages;
    },
    async toggleUserStatus(user) {
      if (!user || !user.userId) {
        console.error("User ID is missing.");
        return;
      }

      try {
        const token = sessionStorage.getItem("jwtToken"); // Lấy token từ sessionStorage
        await axios.get(
            `http://localhost:8088/fja-fap/user/change-users-active?userId=${user.userId}`,
            {headers: {Authorization: `Bearer ${token}`}}
        );

        // Cập nhật trạng thái `active` trên giao diện
        user.active = !user.active;

        console.log(`User ${user.userId} status updated to ${user.active ? "Active" : "Inactive"}`);
      } catch (error) {
        console.error("Error toggling user status:", error);
      }
    },
    async confirmToggleUserStatus(user) {
      if (!user || !user.userId) {
        console.error("User ID is missing.");
        return;
      }

      // Hiển thị cửa sổ xác nhận
      const isConfirmed = window.confirm(
          `Are you sure you want to ${user.active ? "deactivate" : "activate"} this user?`
      );

      // Nếu người dùng bấm "OK", gọi API
      if (isConfirmed) {
        this.toggleUserStatus(user);
      }
    },
    async addAccount() {
      if (!this.newAccount.role) {
        alert("Please select a role.");
        return;
      }

      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.post(
            `http://localhost:8088/fja-fap/user/create-user-with-role?role=${this.newAccount.role.toUpperCase()}`,
            {
              fullName: this.newAccount.fullName,
              email: this.newAccount.email,
              password: "12341234",
            },
            {
              headers: { Authorization: `Bearer ${token}` },
            }
        );

        if (response.data && response.data.code === 0) {
          alert("Account created successfully!");
          this.showAddAccountPopup = false;
          this.fetchUsersByRole(this.currentRole);
        } else {
          console.error("Error creating account:", response.data.message || "Unknown error");
          alert("Failed to create account. Please try again.");
        }
      } catch (error) {
        console.error("Error creating account:", error);
        alert("Failed to create account. Please check your input or try again later.");
      }
    }
  },
  watch: {
    // Watcher to update URL when `currentPage` changes
    currentPage(newPage) {
      this.$router.push({path: '/admin/account', query: {page: newPage}}).catch(() => {
      });
    }
  }

}
</script>

<style lang="scss">
#account-mng {
  justify-content: space-between;
}
</style>