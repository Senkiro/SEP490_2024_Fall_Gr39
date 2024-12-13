<template>
  <div class="container">
    <div class="headContent">
      <h1>Student Record</h1>
    </div>

    <div class="actions">
      <button @click="openAddStudentPopup">
        <VsxIcon iconName="AddCircle" size="20" type="bold"/>
        Add student
      </button>
      <button @click="navigateToImportStudent">
        <VsxIcon iconName="Import" size="20" type="bold"/>
        Import student
      </button>
    </div>

    <div class="filters">
      <select id="class-filter" class="filter-select" v-model="selectedClass" @change="filterStudentsByClass">
        <option value="">All Class</option>
        <option v-for="classItem in classList" :key="classItem.id" :value="classItem.name">
          {{ classItem.name }}
        </option>
      </select>
    </div>

    <div class="actions">
      <div class="search-container">
        <input
            type="text"
            placeholder="Search..."
            class="search-field"
            v-model="searchQuery"
            @input="searchStudent"
        />
        <VsxIcon iconName="SearchNormal1" color="#ADB5BD" type="linear"/>
      </div>
    </div>

    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th class="center">No</th>
          <th>Fullname</th>
          <th>Roll number</th>
          <th>Japanese name</th>
          <th>Class</th>
          <th>Email</th>
          <th class="center">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(student, index) in students" :key="student.id">
          <td class="center">{{ index + 1 }}</td>
          <td>
            {{ student.fullname }}
          </td>
          <td>{{ student.rollNumber }}</td>
          <td>{{ student.japaneseName }}</td>
          <td :style="{ color: student.classResponse?.classColour || '#000' }">
            {{ student.classResponse?.className || "Unknown" }}
          </td>
          <td>{{ student.email }}</td>
          <td class="center">
            <VsxIcon iconName="Eye" :size="30" color="#171717" type="linear"
                     @click="navigateToProfile(student.id)"/>
          </td>
        </tr>
        <tr v-if="students.length === 0">
          <td colspan="7" class="center">No record.</td>
        </tr>
        </tbody>
      </table>

      <div class="pagination" v-if="studentPagination.totalElements > 0">
        <button @click="changeStudentPage(studentPagination.currentPage - 1)"
                :disabled="studentPagination.currentPage <= 1">
          <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717"/>
        </button>
        <button v-for="page in studentPagination.displayedPages" :key="page"
                :class="{ active: page === studentPagination.currentPage }"
                @click="changeStudentPage(page)">
          {{ page }}
        </button>
        <button @click="changeStudentPage(studentPagination.currentPage + 1)"
                :disabled="studentPagination.currentPage >= studentPagination.totalPages">
          <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717"/>
        </button>
      </div>
    </div>
  </div>

  <div v-if="showAddStudentPopup" class="popup-overlay">
    <div class="popup">
      <div class="exit-icon">
        <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="showAddStudentPopup = false"/>
      </div>
      <div class="popup-title">
        <h2>Add student</h2>
      </div>
      <form @submit.prevent="addStudent">
        <div class="form-group">
          <label for="fullname">Full name <span class="required">*</span></label>
          <input type="text" id="fullname" v-model="newStudent.fullname" required/>
        </div>
        <div class="form-group">
          <label for="japaneseName">Japanese name <span class="required">*</span></label>
          <input type="text" id="japaneseName" v-model="newStudent.japaneseName" required/>
        </div>
        <div class="form-group">
          <label for="email">Email <span class="required">*</span></label>
          <input type="email" id="email" v-model="newStudent.email" required/>
        </div>
        <div class="form-group">
          <label for="class">Class <span class="required">*</span></label>
          <select id="class" v-model="newStudent.class" required>
            <option value="">Choose class</option>
            <option v-for="classItem in classList" :key="classItem.id" :value="classItem.name">
              {{ classItem.name }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label for="dob">DOB <span class="required">*</span></label>
          <input type="date" id="dob" v-model="newStudent.dob" placeholder="dd/mm/yyyy" required/>
        </div>
        <div class="form-group">
          <label>Gender <span class="required">*</span></label>
          <div class="gender-group">
            <div class="radio">
              <input type="radio" id="male" value="Male" name="gender" v-model="newStudent.gender" checked/>
              <label for="male">Male</label>
            </div>
            <div class="radio">
              <input type="radio" id="female" value="Female" name="gender" v-model="newStudent.gender"/>
              <label for="female">Female</label>
            </div>
          </div>
        </div>
        <div class="form-group">
          <label for="phone">Phone <span class="required">*</span></label>
          <input type="text" id="phone" v-model="newStudent.phone" required/>
        </div>
        <div class="actions">
          <button type="submit" class="btn-submit">Create</button>
        </div>
      </form>
    </div>
  </div>

  <div v-if="notification.message" :class="['notification', notification.type]">
    {{ notification.message }}
  </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";
import axios from "axios";

export default {
  name: "StudentRecord",
  components: { VsxIcon },
  data() {
    return {
      batchName: 'Fall24',
      activeTab: 'student',
      selectedClass: "",
      showAddStudentPopup: false,
      showAddClassPopup: false,
      showEditClassPopup: false,
      studentPagination: {
        currentPage: 1,
        itemsPerPage: 5,
        totalElements: 0,
        totalPages: 0,
        displayedPages: [],
      },
      classPagination: {
        currentPage: 1,
        itemsPerPage: 5,
        totalElements: 0,
        totalPages: 0,
        displayedPages: [],
      },
      newStudent: {
        fullname: '',
        japaneseName: '',
        email: '',
        class: '',
        dob: '',
        gender: ''
      },
      newClass: {
        name: '',
        color: '#000000'
      },
      students: [],
      classes: [],
      classList: [],
      notification: {
        message: '',
        type: ''
      },
      searchQuery: '',
    };
  },
  methods: {
    openAddStudentPopup() {
      this.showAddStudentPopup = true;
    },

    navigateToProfile(studentId) {
      this.$router.push({name: "StaffStudentProfile", params: {id: studentId}});
    },

    async addStudent() {
      try {
        const token = sessionStorage.getItem('jwtToken');
        const genderBoolean = this.newStudent.gender === 'Male';

        const response = await axios.post(
            `http://localhost:8088/fja-fap/staff/create-student`,
            {
              fullName: this.newStudent.fullname,
              japaneseName: this.newStudent.japaneseName,
              email: this.newStudent.email,
              dob: this.formatDate(this.newStudent.dob),
              phone: this.newStudent.phone,
              gender: genderBoolean,
              batchName: this.batchName,
              className: this.newStudent.class
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
          rollNumber: addedStudent.rollNumber,
          gender: addedStudent.gender,
          dob: addedStudent.dob,
          phone: addedStudent.phone
        });

        await this.fetchStudent();
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
            `http://localhost:8088/fja-fap/staff/get-student-by-batch?page=${this.studentPagination.currentPage - 1}&size=${this.studentPagination.itemsPerPage}&batch_name=${this.batchName}`,
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            }
        );

        // Kiểm tra nếu response.data.result và response.data.result.content tồn tại
        if (response.status === 200 && response.data.result && Array.isArray(response.data.result.content)) {
          this.students = response.data.result.content.map((item) => ({
            id: item.studentId || "Unknown ID",
            rollNumber: item.rollNumber || "N/A",
            fullname: item.userInforResponse?.fullName || "Unknown Name",
            japaneseName: item.userInforResponse?.japaneseName || "N/A",
            email: item.userInforResponse?.email || "N/A",
            class: item.classResponse?.name || "Unknown",
            classResponse: item.classResponse || {},
            dob: item.userInforResponse?.dob || "N/A",
            phone: item.userInforResponse?.phone || "N/A",
            gender: item.userInforResponse?.gender === false ? "Female" : "Male",
          }));

          this.studentPagination.totalElements = response.data.result.totalElements;
          this.studentPagination.totalPages = Math.ceil(this.studentPagination.totalElements / this.studentPagination.itemsPerPage);
          this.updateStudentDisplayedPages();
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

    resetNewStudent() {
      this.newStudent = {
        fullname: '',
        japaneseName: '',
        email: '',
        class: '',
        dob: '',
        gender: ''
      };
    },

    navigateToImportStudent() {
      this.$router.push({name: 'ImportStudent'});
    },

    showNotification(message, type) {
      this.notification = {message, type};
      setTimeout(() => {
        this.notification.message = "";
      }, 3000);
    },

    updateStudentDisplayedPages() {
      const pages = [];
      const {currentPage, totalPages} = this.studentPagination;

      if (totalPages <= 5) {
        for (let i = 1; i <= totalPages; i++) {
          pages.push(i);
        }
      } else {
        if (currentPage <= 3) {
          pages.push(1, 2, 3, '...', totalPages);
        } else if (currentPage >= totalPages - 2) {
          pages.push(1, '...', totalPages - 2, totalPages - 1, totalPages);
        } else {
          pages.push(1, '...', currentPage - 1, currentPage, currentPage + 1, '...', totalPages);
        }
      }

      this.studentPagination.displayedPages = pages;
    },

    changeStudentPage(newPage) {
      if (newPage > 0 && newPage <= this.studentPagination.totalPages) {
        this.studentPagination.currentPage = newPage;
        this.fetchStudent();
      }
    },

    async searchStudent() {
      if (!this.searchQuery.trim()) {
        await this.fetchStudent();
        return;
      }
      try {
        const token = sessionStorage.getItem('jwtToken');

        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/search-student?name=${this.searchQuery}&page=${this.studentPagination.currentPage - 1}&size=${this.studentPagination.itemsPerPage}`,
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            }
        );

        if (response.status === 200 && response.data.result) {
          this.students = response.data.result.content.map((item) => ({
            id: item.studentId || "Unknown ID",
            rollNumber: item.rollNumber || "N/A",
            fullname: item.userInforResponse?.fullName || "Unknown Name",
            japaneseName: item.userInforResponse?.japaneseName || "N/A",
            email: item.userInforResponse?.email || "N/A",
            dob: item.userInforResponse?.dob || "N/A",
            phone: item.userInforResponse?.phone || "N/A",
            gender: item.userInforResponse?.gender ? "Male" : "Female",
            class: item.classResponse?.className || "Unknown",
            classResponse: item.classResponse || {},
          }));

          // Cập nhật thông tin phân trang
          this.studentPagination.totalElements = response.data.result.totalElements;
          this.studentPagination.totalPages = Math.ceil(
              this.studentPagination.totalElements /
              this.studentPagination.itemsPerPage
          );

          this.updateStudentDisplayedPages();
        } else {
          console.error('No student data available:', response);
          this.students = []; // Reset dữ liệu nếu không có kết quả
        }
      } catch (error) {
        console.error('Error searching students:', error);
      }
    },
    formatDate(dob) {
      if (!dob) return '';

      const [year, month, day] = dob.split('-');
      return `${year}-${month}-${day}`;
    }
  },
  mounted() {
    this.fetchStudent();

  },
};
</script>

<style scoped></style>
