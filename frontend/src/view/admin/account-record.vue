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
      <button>
        <VsxIcon iconName="AddCircle" size="20" type="bold" @click="showAddAccountPopup = true" />
        Add account
      </button>
    </div>
    <div class="actions">
      <div class="search-container">
        <input type="text" placeholder="Search..." class="search-field" />
        <VsxIcon iconName="SearchNormal1" color="#ADB5BD" type="linear" />
      </div>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th class="center">No</th>
            <th>Full name</th>
            <th>Email</th>
            <th>Password</th>
            <th>Role</th>
            <th>Status</th>
            <th class="center">Action</th>
          </tr>
        </thead>
        <tbody>
        <tr v-for="(user, index) in users" :key="user.userId">
            <td class="center">{{ index + 1 + (currentPage - 1) * itemsPerPage }}</td>
            <td>{{ user?.fullName || 'N/A' }}</td>
            <td>{{ user?.email }}</td>
            <td></td>
            <td></td>
            <td></td>
            <td class="center">
              <div class="icon-group">
                <VsxIcon iconName="Unlock" :size="30" color="#171717" type="linear" />
                <VsxIcon iconName="Key" :size="30" color="#171717" type="linear" />
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="pagination" v-if="totalPages > 1">
        <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">
          <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717" />
        </button>
        <button v-for="page in displayedPages" :key="page" :class="{ active: page === currentPage }"
          @click="changePage(page)">
          {{ page }}
        </button>
        <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">
          <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717" />
        </button>
      </div>
    </div>

    <div v-if="showAddAccountPopup" class="popup-overlay">
      <div class="popup">
        <div class="exit-icon">
          <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="showAddAccountPopup = false" />
        </div>
        <div class="popup-title">
          <h2>Add account</h2>
        </div>
        <form @submit.prevent="addAccount">
          <div class="form-group">
            <label for="username">Email <span class="required">*</span></label>
            <input type="text" id="email" v-model="newAccount.email" required />
          </div>
          <div class="form-group">
            <label for="fullName">Full Name <span class="required">*</span></label>
            <input type="text" id="fullName" v-model="newAccount.fullName" required />
          </div>
          <div class="form-group">
            <label for="japaneseName">Japanese Name <span class="required">*</span></label>
            <input type="text" id="japaneseName" v-model="newAccount.japaneseName" required />
          </div>
          <div class="form-group">
            <label for="role">Role <span class="required">*</span></label>
            <div class="filters">
              <select id="role-filter" class="filter-select" v-model="newAccount.role">
                <option value="" disabled>Select Role</option>
                <option value="staff">Staff</option>
                <option value="staff">Teacher</option>
                <option value="staff">Student</option>
              </select>
            </div>
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
  },
  watch: {
    // Watcher to update URL when `currentPage` changes
    currentPage(newPage) {
      this.$router.push({ path: '/admin/account', query: { page: newPage } }).catch(() => {
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