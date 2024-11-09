<template>
  <div class="container">
    <div class="batch-title-container">
      <h1 class="batch-title">{{ batchName }}</h1>
    </div>

    <!-- Phần chuyển đổi tab -->
    <VaButtonGroup>
      <VaButton
          @click="switchTab('student')"
          :class="{'active-tab': activeTab === 'student'}"
          class="buttonGroup">
        Student Record
      </VaButton>
      <VaButton
          @click="switchTab('class')"
          :class="{'active-tab': activeTab === 'class'}"
          class="buttonGroup">
        Class Record
      </VaButton>
    </VaButtonGroup>

    <!-- Nội dung Student Record -->
    <div v-if="activeTab === 'student'" class="batchEntity-detail-content student-record">
      <div class="headContent">
        <div class="student-actions-left">
          <label for="class-filter" class="class-filter-label">Class:</label>
          <select id="class-filter" class="filter-select">
            <option value="">All Classes</option>
            <option value="Blue">Blue</option>
            <option value="Red">Red</option>
            <option value="Green">Green</option>
            <option value="Yellow">Yellow</option>
            <option value="Purple">Purple</option>
          </select>
        </div>
        <!-- Các nút hành động cho Student Record -->
        <div class="actions-right">
          <button class="btn btn-import-student" @click="navigateToImportStudent">
            <VsxIcon iconName="Import" size="20" type="bold" />
            Import student
          </button>
          <button class="btn btn-add-student" @click="openAddStudentPopup">
            <VsxIcon iconName="AddCircle" size="20" type="bold" />
            Add student
          </button>
        </div>
      </div>
      <!-- Bảng chi tiết sinh viên -->
      <div class="table-container">
        <table class="student-table">
          <thead>
          <tr>
            <th style="width: 5%;">No</th>
            <th style="width: 20%;">Fullname</th>
            <th style="width: 15%;">Roll number</th>
            <th style="width: 20%;">Japanese name</th>
            <th style="width: 10%;">Class</th>
            <th style="width: 20%;">Email</th>
            <th style="width: 10%;">Action</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(student, index) in students" :key="student.id">
            <td>{{ index + 1 }}</td>
            <td>{{ student.fullname }}</td>
            <td>{{ student.rollNumber }}</td>
            <td>{{ student.japaneseName }}</td>
            <td :style="{ color: student.classColor }">{{ student.class }}</td>
            <td>{{ student.email }}</td>
            <td class="action-column">
              <VsxIcon
                  iconName="Eye"
                  :size="32"
                  color="#5584FF"
                  type="linear"
                  @click="navigateToProfile(student.id)"
                  class="icon-button"
              />
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">‹</button>
      <button
          v-for="page in displayedPages"
          :key="page"
          :class="{ active: page === currentPage }"
          @click="changePage(page)"
      >
        {{ page }}
      </button>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">›</button>
    </div>

    <!-- Nội dung Class Record -->
    <div v-if="activeTab === 'class'" class="class-record-container">
      <div class="class-record-header">
        <h1 class="batch-title"></h1>
        <div class="class-record-actions">
          <button class="btn btn-add-class" @click="openAddClassPopup">Add class</button>
        </div>
      </div>
      <div class="class-record-content">
        <table class="class-table">
          <thead>
          <tr>
            <th>No</th>
            <th>Class</th>
            <th>Number of students</th>
            <th>Action</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(classItem, index) in classes" :key="classItem.id">
            <td>{{ index + 1 }}</td>
            <td :style="{ color: classItem.classColor }">{{ classItem.name }}</td>
            <td>{{ classItem.studentCount }}</td>
            <td class="action-column">
              <VsxIcon iconName="Eye" :size="24" color="#5584FF" type="linear" @click="viewClassDetail(classItem)" />
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>

  <!-- Popup Add Student -->
  <div v-if="showAddStudentPopup" class="add-student-popup-overlay">
    <div class="add-student-popup">
      <h2 class="popup-title">Add student</h2>
      <form @submit.prevent="addStudent">
        <div class="form-group">
          <label for="fullname">Full name *</label>
          <input type="text" id="fullname" v-model="newStudent.fullname" required />
        </div>
        <div class="form-group">
          <label for="japaneseName">Japanese name *</label>
          <input type="text" id="japaneseName" v-model="newStudent.japaneseName" required />
        </div>
        <div class="form-group">
          <label for="email">Email *</label>
          <input type="email" id="email" v-model="newStudent.email" required />
        </div>
        <div class="form-group">
          <label for="class">Class</label>
          <select id="class" v-model="newStudent.class">
            <option value="">Choose class now or later</option>
            <option value="Blue">Blue</option>
            <option value="Red">Red</option>
            <option value="Green">Green</option>
            <option value="Yellow">Yellow</option>
            <option value="Purple">Purple</option>
          </select>
        </div>
        <div class="form-group">
          <label for="dob">DOB *</label>
          <input type="text" id="dob" v-model="newStudent.dob" placeholder="dd/mm/yyyy" required />
        </div>
        <div class="form-group gender-group">
          <label>Gender *</label>
          <div>
            <input type="radio" id="male" value="Male" v-model="newStudent.gender" required />
            <label for="male">Male</label>
            <input type="radio" id="female" value="Female" v-model="newStudent.gender" required />
            <label for="female">Female</label>
          </div>
        </div>
        <div class="form-group">
          <label for="phone">Phone *</label>
          <input type="text" id="phone" v-model="newStudent.phone" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-create">Create</button>
          <button type="button" class="btn btn-cancel" @click="showAddStudentPopup = false">Cancel</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";
import axios from 'axios';

export default {
  name: "BatchDetail",
  components: {
    VsxIcon
  },
  data() {
    return {
      batchName: this.$route.params.batchName,
      activeTab: 'student',
      showAddStudentPopup: false,
      showAddClassPopup: false,
      currentPage: 1,
      itemsPerPage: 5,
      newStudent: {
        fullname: '',
        japaneseName: '',
        email: '',
        class: '',
        dob: '',
        gender: '',
        phone: ''
      },
      newClass: {
        name: '',
        color: '#000000'
      },
      students: [],
      classes: [
        { id: 1, name: 'Blue', classColor: 'blue', studentCount: 30 },
        { id: 2, name: 'Red', classColor: 'red', studentCount: 30 },
        { id: 3, name: 'Green', classColor: 'green', studentCount: 30 },
        { id: 4, name: 'Yellow', classColor: 'yellow', studentCount: 30 },
        { id: 5, name: 'Purple', classColor: 'purple', studentCount: 30 }
      ],
      notification: {
        message: '',
        type: ''
      }
    };
  },
  methods: {
    switchTab(tab) {
      this.activeTab = tab;
    },
    navigateToProfile(studentId) {
      this.$router.push({ name: "StudentProfile", params: { id: studentId } });
    },
    openAddStudentPopup() {
      this.showAddStudentPopup = true;
    },
    async addStudent() {
      try {
        const token = sessionStorage.getItem('jwtToken');
        const genderBoolean = this.newStudent.gender === 'Male';

        const response = await axios.post(
            `http://localhost:8088/fja-fap/staff/create-student?batch_name=${this.batchName}`,
            {
              fullName: this.newStudent.fullname,
              japaneseName: this.newStudent.japaneseName,
              email: this.newStudent.email,
              dob: this.formatDate(this.newStudent.dob),
              phone: this.newStudent.phone,
              gender: genderBoolean,
            },
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            }
        );

        const addedStudent = response.data;
        this.students.push({
          id: addedStudent.id,
          fullname: addedStudent.fullname,
          japaneseName: addedStudent.japaneseName,
          email: addedStudent.email,
          class: addedStudent.class,
          classColor: this.getClassColor(addedStudent.class),
          rollNumber: addedStudent.rollNumber,
          gender: addedStudent.gender,
          dob: addedStudent.dob,
          phone: addedStudent.phone
        });

        this.showAddStudentPopup = false;
        this.resetNewStudent();

        this.showNotification("Student created successfully!", "success");
      } catch (error) {
        console.error('Error creating student:', error);
        this.showNotification(error.response?.data?.message || "Error creating student. Please try again.", 'error');
      }
    },
    async fetchStudent() {
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-student-by-batch?page=${this.currentPage - 1}&size=${this.itemsPerPage}&batch_name=${this.batchName}`,
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            }
        );

        // Kiểm tra nếu response.data.result và response.data.result.content tồn tại
        if (response.status === 200 && response.data.result && Array.isArray(response.data.result.content)) {
          this.students = response.data.result.content.map((item) => ({
            id: item.rollNumber, // Giả sử rollNumber là duy nhất và dùng làm id
            rollNumber: item.rollNumber,
            fullname: item.user.fullName,
            japaneseName: item.user.japaneseName,
            email: item.user.email,
            class: item.class || "Unknown",
            classColor: this.getClassColor(item.class),
            dob: item.user.dob,
            phone: item.user.phone,
            gender: item.user.gender
          }));

          this.totalElements = response.data.result.totalElements;
          this.totalPages = Math.ceil(this.totalElements / this.itemsPerPage);
          this.updateDisplayedPages();
        } else {
          console.error('No student data available:', response);
          this.students = []; // Gán mảng trống nếu không có dữ liệu
          alert('Không có dữ liệu sinh viên nào để hiển thị.');
        }
      } catch (error) {
        console.error('Error fetching students:', error);
        alert('Đã có lỗi xảy ra khi kết nối tới server.');
      }
    },
    changePage(newPage) {
      if (newPage > 0 && newPage <= this.totalPages) {
        this.currentPage = newPage;
        this.fetchStudent();
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
    formatDate(dob) {
      const [day, month, year] = dob.split('/');
      return `${year}-${month}-${day}`;
    },
    getClassColor(className) {
      const colors = {
        Blue: 'blue',
        Red: 'red',
        Green: 'green',
        Yellow: 'yellow',
        Purple: 'purple'
      };
      return colors[className] || 'black';
    },
    resetNewStudent() {
      this.newStudent = {
        fullname: '',
        japaneseName: '',
        email: '',
        class: '',
        dob: '',
        gender: '',
        phone: ''
      };
    },
    openAddClassPopup() {
      this.showAddClassPopup = true;
    },
    addClass() {
      this.classes.push({
        id: this.classes.length + 1,
        name: this.newClass.name,
        classColor: this.newClass.color,
        studentCount: 0
      });
      this.showAddClassPopup = false;
      this.resetNewClass();
    },
    resetNewClass() {
      this.newClass = {
        name: '',
        color: '#000000'
      };
    },
    navigateToImportStudent() {
      this.$router.push({ name: 'ImportStudentPage' });
    },
    showNotification(message, type) {
      this.notification = { message, type };
      setTimeout(() => {
        this.notification.message = "";
      }, 3000);
    }
  },
  mounted() {
    this.fetchStudent();
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

.pagination button.active {
  background-color: #007bff;
}

.add-student-popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.add-student-popup {
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  width: 400px;
  max-width: 90%;
}

.batch-title-container {
  text-align: center;
  margin-bottom: 20px;
}

.batch-title {
  font-size: 24px;
  font-weight: bold;
  color: #304CB2;
}

.active-tab {
  background-color: #ffffff;
  color: #304CB2;
}

.headContent {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.student-actions-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.actions-right {
  display: flex;
  gap: 10px;
  margin-left: auto;
}

.class-filter-label {
  font-weight: bold;
}

.filter-select {
  padding: 5px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

.table-container {
  overflow-x: auto;
}

.btn-add-student,
.btn-import-student,
.btn-add-class {
  background-color: #304CB2;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
}

.student-table,
.class-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: auto;
}

.student-table th,
.student-table td,
.class-table th,
.class-table td {
  padding: 12px;
  border-bottom: 1px solid #ddd;
  text-align: left;
  word-wrap: break-word;
}

.student-table th,
.class-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}

.action-column {
  text-align: center;
}

.class-record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.add-student-popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.add-student-popup {
  background-color: #fff;
  padding: 30px;
  border-radius: 12px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
}

.popup-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  text-align: center;
  color: #304CB2;
}

.form-group {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.form-group label {
  font-weight: bold;
  margin-right: 10px;
  width: 30%;
  text-align: right;
}

.form-group input[type="text"],
.form-group input[type="email"],
.form-group select {
  width: 70%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 16px;
}

.form-group.gender-group {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 15px;
}

.form-actions {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.btn-create,
.btn-cancel {
  background-color: #304CB2;
  color: #fff;
  padding: 10px 30px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 16px;
  font-weight: bold;
}

.add-class-popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.add-class-popup {
  background-color: #fff;
  padding: 30px;
  border-radius: 12px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
}

</style>
