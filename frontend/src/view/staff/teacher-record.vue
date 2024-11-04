<template>
  <div class="teacher-record">
    <header class="teacher-record-header elevated-header">
      <h1>Teacher Record</h1>
    </header>
    <div class="teacher-record-actions">
      <button class="btn btn-add" @click="showAddTeacherPopup = true">
        <VsxIcon iconName="AddCircle" size="20" /> Add teacher
      </button>
      <button class="btn btn-chart">
        <VsxIcon iconName="Chart" size="20" /> View statistical chart
      </button>
    </div>
    <table class="teacher-table">
      <thead>
      <tr>
        <th>No</th>
        <th>Name</th>
        <th>Japanese Name</th>
        <th>Dob</th>
        <th>Email</th>
        <th>Gender</th>
        <th>Status</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(teacher, index) in teachers" :key="teacher.userId">
        <td>{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
        <td>{{ teacher.firstName }}</td>
        <td>{{ teacher.japaneseName }}</td>
        <td>{{ teacher.dob }}</td>
        <td>{{ teacher.email }}</td>
        <td>{{ teacher.gender ? 'Male' : 'Female' }}</td>
        <td :class="{'status-progress': teacher.active, 'status-inactive': !teacher.active}">
          {{ teacher.active ? 'Active' : 'Inactive' }}
        </td>
        <td>
          <VsxIcon iconName="Eye" :size="32" color="#5584FF" type="linear" @click="viewTeacherDetail(teacher)" />
        </td>
      </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">Previous</button>
      <span>Page {{ currentPage }} of {{ totalPages }}</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">Next</button>
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
            <label for="firstName">First Name <span class="required">*</span></label>
            <input type="text" id="firstName" v-model="newTeacher.firstName" required />
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
        firstName: "",
        japaneseName: "",
        email: "",
        dob: "",
        gender: true,
        active: true
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

          console.log(this.totalElements);
          console.log(this.totalPages);
        }
      } catch (error) {
        console.error('Error fetching teachers:', error);
      }
    },
    viewTeacherDetail(teacher) {
      this.$router.push({ name: 'TeacherDetail', params: { teacherId: teacher.userId } });
    },
    addTeacher() {
      const newTeacher = {
        id: this.teachers.length + 1,
        name: this.newTeacher.name,
        year: new Date(this.newTeacher.startTime).getFullYear(),
        startTime: this.newTeacher.startTime,
        endTime: this.newTeacher.endTime,
        numberOfStudents: 0,
        status: "On progress"
      };
      this.teachers.push(newTeacher);
      this.showAddTeacherPopup = false;
      this.newTeacher = { name: "", startTime: "", endTime: "" };
    },
    changePage(newPage) {
      if (newPage > 0 && newPage <= this.totalPages) {
        this.currentPage = newPage;
        this.fetchTeachers();
      }
    }
  },
  watch: {
    // Watcher để cập nhật URL khi `currentPage` thay đổi
    currentPage(newPage) {
      this.$router.push({ path: '/staff/teacher-record', query: { page: newPage } }).catch(() => {});
    }
  }
};
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 20px 0;
}

.pagination button {
  padding: 10px 15px;
  border: none;
  background-color: #4a90e2;
  color: white;
  cursor: pointer;
  border-radius: 5px;
  margin: 0 5px;
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.teacher-record {
  padding: 20px;
  max-width: 1200px;
  margin: auto;
}

.teacher-record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
}

.teacher-record-header h1 {
  font-size: 36px;
  font-weight: bold;
  background: -webkit-linear-gradient(180deg, #304CB2, #1A2C6F);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.teacher-record-actions {
  display: flex;
  flex-direction: row-reverse;
  gap: 20px;
  margin-bottom: 20px;
}

.teacher-record-actions .btn {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  font-size: 14px;
  font-weight: normal;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  background-image: linear-gradient(90deg, #3E5DD4, #223374);
  color: #fff;
  gap: 10px;
}

.teacher-table {
  width: 100%;
  border-collapse: collapse;
}

.teacher-table th, .teacher-table td {
  padding: 12px 24px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  align-items: center;
  align-content: center;
}

.teacher-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}

.status-progress {
  color: #304CB2;
}

.status-inactive {
  color: #d5182d;
}

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
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input, .form-group select {
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
</style>