<template>
  <div class="container">
    <div class="headContent">
      <h1>Teacher Record</h1>
    </div>

    <div class="actions">
      <button>
        <VsxIcon iconName="Chart" size="20" type="bold" /> View statistical chart
      </button>
      <button @click="showAddTeacherPopup = true">
        <VsxIcon iconName="AddCircle" size="20" type="bold" /> Add teacher
      </button>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th class="center">No</th>
            <th>Name</th>
            <th>Japanese Name</th>
            <th>Dob</th>
            <th>Email</th>
            <th>Gender</th>
            <th>Status</th>
            <th class="center">Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(teacher, index) in teachers" :key="teacher.userId">
            <td class="center">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
            <td>{{ teacher.fullName }}</td>
            <td>{{ teacher.japaneseName }}</td>
            <td>{{ teacher.dob }}</td>
            <td>{{ teacher.email }}</td>
            <td>{{ teacher.gender ? 'Male' : 'Female' }}</td>
            <td :class="{ 'status-progress': teacher.active, 'status-inactive': !teacher.active }">
              {{ teacher.active ? 'Active' : 'Inactive' }}
            </td>
            <td class="center">
              <VsxIcon iconName="Eye" :size="32" color="#5584FF" type="linear" @click="viewTeacherDetail(teacher)" />
            </td>
          </tr>
        </tbody>
      </table>
      <div class="pagination">
        <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">‹</button>
        <button v-for="page in displayedPages" :key="page" :class="{ active: page === currentPage }"
          @click="changePage(page)">
          {{ page }}
        </button>
        <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">›</button>
      </div>
    </div>

    <div v-if="showAddTeacherPopup" class="popup-overlay">
      <div class="popup">
        <h2>Add teacher</h2>
        <form @submit.prevent="addTeacher">
          <div class="form-group">
            <label for="username">Username <span class="required">*</span></label>
            <input type="text" id="username" v-model="newTeacher.username" required />
          </div>
          <div class="form-group">
            <label for="fullName">Full Name <span class="required">*</span></label>
            <input type="text" id="fullName" v-model="newTeacher.fullName" required />
          </div>
          <div class="form-group">
            <label for="japaneseName">Japanese Name</label>
            <input type="text" id="japaneseName" v-model="newTeacher.japaneseName" />
          </div>
          <div class="form-group">
            <label for="email">Email <span class="required">*</span></label>
            <input type="email" id="email" v-model="newTeacher.email" required />
          </div>
          <div class="form-group">
            <label for="dob">Date of Birth</label>
            <input type="date" id="dob" v-model="newTeacher.dob" />
          </div>
          <div class="form-group">
            <label for="gender">Gender</label>
            <select id="gender" v-model="newTeacher.gender">
              <option :value="true">Male</option>
              <option :value="false">Female</option>
            </select>
          </div>
          <div class="button-group">
            <button type="submit" class="btn btn-create">Create</button>
            <button type="button" class="btn btn-cancel" @click="showAddTeacherPopup = false">Cancel</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Thông báo lỗi hoặc thành công -->
    <div v-if="notification.message" :class="['notification', notification.type]">
      {{ notification.message }}
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { VsxIcon } from "vue-iconsax";

export default {
  name: "TeacherRecord",
  components: {
    VsxIcon
  },
  data() {
    return {
      teachers: [],
      currentPage: 1,
      itemsPerPage: 5,
      totalElements: 0,
      totalPages: 0,
      showAddTeacherPopup: false,
      newTeacher: {
        username: "",
        fullName: "",
        japaneseName: "",
        email: "",
        dob: "",
        gender: true,
        active: true
      },
      notification: {
        message: "",
        type: "", // "success" hoặc "error"
      }
    };
  },
  mounted() {
    this.fetchTeachers();
  },
  methods: {
    async fetchTeachers() {
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
          `http://localhost:8088/fja-fap/staff/teacher?page=${this.currentPage - 1}&size=${this.itemsPerPage}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        if (response.data.code === 1000) {
          this.teachers = response.data.result.teachers;
          this.totalElements = response.data.result.totalElements;
          this.totalPages = Math.ceil(this.totalElements / this.itemsPerPage);
          this.updateDisplayedPages();
        } else {
          this.showNotification('Không thể tải danh sách giáo viên: ' + response.data.message, 'error');
        }
      } catch (error) {
        console.error('Lỗi khi lấy dữ liệu giáo viên:', error);
        this.showNotification('Đã xảy ra lỗi khi tải danh sách giáo viên.', 'error');
      }
    },
    viewTeacherDetail(teacher) {
      this.$router.push({ name: 'TeacherDetail', params: { teacherId: teacher.userId } });
    },
    async addTeacher() {
      try {
        const token = sessionStorage.getItem('jwtToken');
        await axios.post('http://localhost:8088/fja-fap/staff/create-teacher', {
          fullName: this.newTeacher.fullName,
          japaneseName: this.newTeacher.japaneseName,
          email: this.newTeacher.email,
          dob: this.newTeacher.dob,
          gender: this.newTeacher.gender,
          active: true
        }, {
          headers: { Authorization: `Bearer ${token}` }
        });

        this.teachers.push({ ...this.newTeacher });
        this.showAddTeacherPopup = false;

        // Đặt lại giá trị các trường sau khi thêm
        this.newTeacher = {
          username: "",
          fullName: "",
          japaneseName: "",
          email: "",
          dob: "",
          gender: true,
          active: true
        };

        this.showNotification("Thêm giáo viên thành công!", "success");

      } catch (error) {
        console.error('Lỗi khi thêm giáo viên:', error);
        const errorMessage = error.response && error.response.data && error.response.data.message
          ? error.response.data.message
          : 'Đã xảy ra lỗi khi thêm giáo viên. Vui lòng thử lại.';

        this.showNotification(errorMessage, 'error');
      }
    },
    changePage(newPage) {
      if (newPage > 0 && newPage <= this.totalPages) {
        this.currentPage = newPage;
        this.fetchTeachers();
      }
    },
    showNotification(message, type) {
      this.notification = { message, type };
      setTimeout(() => {
        this.notification.message = "";
      }, 3000);
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
    }
  },
  watch: {
    // Watcher để cập nhật URL khi `currentPage` thay đổi
    currentPage(newPage) {
      this.$router.push({ path: '/staff/teacher-record', query: { page: newPage } }).catch(() => { });
    }
  }
};
</script>

<style scoped>

.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.popup {
  background: #fff;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
  width: 400px;
  max-width: 90%;
}

.popup h2 {
  margin-top: 0;
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.button-group {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.btn-create {
  background-color: #4a90e2;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
}

.btn-cancel {
  background-color: #ccc;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  margin-left: 10px;
}

.notification {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 15px 20px;
  border-radius: 8px;
  font-size: 16px;
  color: #fff;
  z-index: 1000;
  transition: all 0.5s ease;
}

.notification.success {
  background-color: #4caf50;
}

.notification.error {
  background-color: #f44336;
}
</style>
