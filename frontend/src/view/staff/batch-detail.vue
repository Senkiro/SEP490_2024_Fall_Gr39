<template>
  <div class="container">
    <div class="headContent">
      <h1>{{ batchName }}</h1>
      <button @click="confirmBatchSummary">Batch Summary</button>
    </div>

    <!-- Phần chuyển đổi tab -->
    <div class="btn-group">
      <button @click="switchTab('student')" :class="{ 'active-tab': activeTab === 'student' }">
        Student Record
      </button>
      <button @click="switchTab('class')" :class="{ 'active-tab': activeTab === 'class' }">
        Class Record
      </button>
    </div>

    <!-- Nội dung Student Record -->
    <div v-if="activeTab === 'student'" class="record">
      <div class="filters">
        <select id="class-filter" class="filter-select" v-model="selectedClass" @change="filterStudentsByClass">
          <option value="">All Class</option>
          <option v-for="classItem in classList" :key="classItem.id" :value="classItem.id">
            {{ classItem.name }}
          </option>
        </select>
      </div>

      <!-- Các nút hành động cho Student Record -->
      <div class="actions">
        <button @click="openAddStudentPopup">
          <VsxIcon iconName="AddCircle" size="20" type="bold" />
          Add student
        </button>
        <button @click="navigateToImportStudent">
          <VsxIcon iconName="Import" size="20" type="bold" />
          Import student
        </button>
      </div>

      <div class="actions">
        <div class="search-container">
          <input type="text" placeholder="Search..." class="search-field" v-model="searchQuery"
            @input="searchStudent" />
          <VsxIcon iconName="SearchNormal1" color="#ADB5BD" type="linear" />
        </div>
      </div>

      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th id="index" class="center">No</th>
              <th>Full name</th>
              <th>Roll number</th>
              <th>Japanese name</th>
              <th id="class">Class</th>
              <th id="email">Email</th>
              <th id="action" class="center">Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(student, index) in students" :key="student.id">
              <td id="index" class="center">{{ index + 1 }}</td>
              <td>
                {{ student.fullname }}
              </td>
              <td>{{ student.rollNumber }}</td>
              <td>{{ student.japaneseName }}</td>
              <td id="class" :style="{ color: student.classResponse?.classColour || '#000' }">
                {{ student.classResponse?.className || "Unknown" }}
              </td>
              <td id="email">{{ student.email }}</td>
              <td id="action" class="center">
                <VsxIcon iconName="Eye" :size="30" color="#171717" type="linear"
                  @click="navigateToProfile(student.id)" />
              </td>
            </tr>
            <tr v-if="students.length === 0">
              <td colspan="7" class="center">No record.</td>
            </tr>
          </tbody>
        </table>

        <div class="pagination" v-if="studentPagination.totalElements > 1">
          <button @click="changeStudentPage(studentPagination.currentPage - 1)"
            :disabled="studentPagination.currentPage <= 1">
            <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717" />
          </button>
          <button v-for="page in studentPagination.displayedPages" :key="page"
            :class="{ active: page === studentPagination.currentPage }" @click="changeStudentPage(page)">
            {{ page }}
          </button>
          <button @click="changeStudentPage(studentPagination.currentPage + 1)"
            :disabled="studentPagination.currentPage >= studentPagination.totalPages">
            <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717" />
          </button>
        </div>
      </div>
    </div>

    <!-- Nội dung Class Record -->
    <div v-if="activeTab === 'class'" class="record">
      <div class="actions">
        <button @click="openAddClassPopup">
          <VsxIcon iconName="AddCircle" size="20" type="bold" />
          Add class
        </button>
      </div>
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th class="center">No</th>
              <th>Class Name</th>
              <th class="center">Number of students</th>
              <th class="center">Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(classItem, index) in classes" :key="classItem.id">
              <td class="center">{{ index + 1 }}</td>
              <td :style="{ color: classItem.color }">{{ classItem.name }}</td>
              <td class="center">{{ classItem.numberOfStudents }}</td>
              <td class="center">
                <VsxIcon iconName="Edit2" :size="30" color="#171717" type="linear"
                  @click="openEditClassPopup(classItem)" />
                <VsxIcon iconName="Slash" :size="25" color="#171717" type="linear"
                         @click="deleteClass(classItem.id)"/>
              </td>
            </tr>
            <tr v-if="classes.length === 0">
              <td colspan="4" class="center">No record.</td>
            </tr>
          </tbody>
        </table>

        <div class="pagination" v-if="classPagination.totalElements > 1">
          <button @click="changeClassPage(classPagination.currentPage - 1)"
            :disabled="classPagination.currentPage <= 1">
            <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717" />
          </button>
          <button v-for="page in classPagination.displayedPages" :key="page"
            :class="{ active: page === classPagination.currentPage }" @click="changeClassPage(page)">
            {{ page }}
          </button>
          <button @click="changeClassPage(classPagination.currentPage + 1)"
            :disabled="classPagination.currentPage >= studentPagination.totalPages">
            <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717" />
          </button>
        </div>
      </div>
    </div>
    <!-- Popup Add Student -->
    <div v-if="showAddStudentPopup" class="popup-overlay">
      <div class="popup">
        <div class="exit-icon">
          <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="showAddStudentPopup = false" />
        </div>
        <div class="popup-title">
          <h2>Add student</h2>
        </div>
        <form @submit.prevent="addStudent">
          <div class="form-group">
            <label for="fullname">Full name <span class="required">*</span></label>
            <input type="text" id="fullname" v-model="newStudent.fullname" required />
          </div>
          <div class="form-group">
            <label for="japaneseName">Japanese name <span class="required">*</span></label>
            <input type="text" id="japaneseName" v-model="newStudent.japaneseName" required />
          </div>
          <div class="form-group">
            <label for="email">Email <span class="required">*</span></label>
            <input type="email" v-model="newStudent.email" required />
          </div>
          <div class="form-group">
            <label for="class">Class <span class="required">*</span></label>
            <select v-model="newStudent.class" required>
              <option value="">Choose class</option>
              <option v-for="classItem in classList" :key="classItem.id" :value="classItem.name">
                {{ classItem.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="dob">DOB <span class="required">*</span></label>
            <input type="date" id="dob" v-model="newStudent.dob" placeholder="dd/mm/yyyy" required />
          </div>
          <div class="form-group">
            <label>Gender <span class="required">*</span></label>
            <div class="gender-group">
              <div class="radio">
                <input type="radio" id="male" value="Male" name="gender" v-model="newStudent.gender" checked />
                <label for="male">Male</label>
              </div>
              <div class="radio">
                <input type="radio" id="female" value="Female" name="gender" v-model="newStudent.gender" />
                <label for="female">Female</label>
              </div>
            </div>
          </div>
          <div class="form-group">
            <label for="phone">Phone <span class="required">*</span></label>
            <input type="text" id="phone" v-model="newStudent.phone" required />
          </div>
          <div class="actions">
            <button class="btn-submit" type="submit">Create</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Popup Add Class -->
    <div v-if="showAddClassPopup" class="popup-overlay">
      <div class="popup">
        <div class="exit-icon">
          <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="showAddClassPopup = false" />
        </div>
        <div class="popup-title">
          <h2>Add class</h2>
        </div>

        <form @submit.prevent="addClass">
          <div class="form-group">
            <label for="className">Class name <span class="required">*</span></label>
            <input type="text" id="className" v-model="newClass.name" required />
          </div>
          <div class="form-group">
            <label for="classColor">Color <span class="required">*</span></label>
            <div id="color-picker">
              <input type="color" id="classColor" v-model="newClass.color" required />
            </div>

          </div>
          <div class="actions">
            <button class="btn-submit" type="submit">Create</button>
          </div>
        </form>
      </div>
    </div>

    <div v-if="showEditClassPopup" class="popup-overlay">
      <div class="popup">
        <div class="exit-icon">
          <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="showEditClassPopup = false" />
        </div>
        <div class="popup-title">
          <h2>Edit Class</h2>
        </div>
        <form @submit.prevent="editClass">
          <div class="form-group">
            <label for="className">Class Name <span class="required">*</span></label>
            <input type="text" id="className" v-model="editedClass.name" required />
          </div>
          <div class="form-group">
            <label for="classColor">Class Color <span class="required">*</span></label>
            <div id="color-picker">
              <input type="color" id="classColor" v-model="editedClass.color" required />
            </div>
          </div>
          <div class="actions">
            <button class="btn-submit" type="submit">Save</button>
          </div>
        </form>
      </div>
    </div>

    <div v-if="notification.message" :class="['notification', notification.type]">
      {{ notification.message }}
    </div>

  </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";
import axios from "axios";

export default {
  name: "BatchDetail",
  components: {
    VsxIcon
  },
  data() {
    return {
      batchName: this.$route.params.batchName,
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
    async deleteClass(classId) {
      try {
        const token = sessionStorage.getItem('jwtToken');
        if (!token) {
          this.showNotification('Authentication token is missing.', 'error');
          return;
        }

        // Xác nhận trước khi xóa
        const userConfirmed = confirm('Are you sure you want to delete this class? This action cannot be undone.');
        if (!userConfirmed) {
          return;
        }

        // Gửi yêu cầu xóa tới API
        const response = await axios.delete(
            `http://localhost:8088/fja-fap/staff/delete-class`,
            {
              params: { class_id: classId },
              headers: { Authorization: `Bearer ${token}` },
            }
        );

        if (response.status === 200) {
          // Xóa thành công, cập nhật danh sách lớp
          this.showNotification('Class deleted successfully!', 'success');
          await this.fetchClass();
          await this.fetchClassFilter();
        } else {
          this.showNotification(response.data.message || 'Failed to delete class.', 'error');
        }
      } catch (error) {
        console.error('Error deleting class:', error);
        this.showNotification(error.response?.data?.message || 'An error occurred while deleting the class.', 'error');
      }
    },
    async confirmBatchSummary() {
      const userConfirmed = confirm("Do you want to summarize this batch?");
      if (!userConfirmed) {
        return;
      }

      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/batch-summary`,
            {
              params: { batch_name: this.batchName },
              headers: { Authorization: `Bearer ${token}` },
            }
        );

        if (response.status === 200) {
          this.showNotification("Batch summary completed successfully!", "success");
        } else {
          this.showNotification("Batch summary failed. Please try again.", "error");
        }
      } catch (error) {
        console.error("Error during batch summary:", error);
        this.showNotification(error.response?.data?.message, "error");
      }
    },
    switchTab(tab) {
      this.activeTab = tab;
    },
    navigateToProfile(studentId) {
      this.$router.push({ name: "StaffStudentProfile", params: { id: studentId } });
    },
    openAddStudentPopup() {
      this.showAddStudentPopup = true;
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
        const apiEndpoint = this.selectedClass
            ? `http://localhost:8088/fja-fap/staff/get-student-by-batch-class?batch_name=${this.batchName}&class_id=${this.selectedClass}&page=${this.studentPagination.currentPage - 1}&size=${this.studentPagination.itemsPerPage}`
            : `http://localhost:8088/fja-fap/staff/get-student-by-batch?page=${this.studentPagination.currentPage - 1}&size=${this.studentPagination.itemsPerPage}&batch_name=${this.batchName}`;

        const response = await axios.get(apiEndpoint, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.status === 200 && response.data.result) {
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
          this.studentPagination.totalPages = Math.ceil(
              this.studentPagination.totalElements / this.studentPagination.itemsPerPage
          );
          this.updateStudentDisplayedPages();
        }
      } catch (error) {
        console.error("Error fetching students:", error);
        alert("Đã có lỗi xảy ra khi lấy danh sách sinh viên.");
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

    openAddClassPopup() {
      this.showAddClassPopup = true;
    },

    resetNewClass() {
      this.newClass = {
        name: '',
        color: '#000000'
      };
    },

    navigateToImportStudent() {
      this.$router.push({ name: 'ImportStudent' });
    },

    showNotification(message, type) {
      this.notification = { message, type };
      setTimeout(() => {
        this.notification.message = "";
      }, 3000);
    },

    updateStudentDisplayedPages() {
      const pages = [];
      const { currentPage, totalPages } = this.studentPagination;

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
      this.studentPagination.currentPage = 1;
      if (!this.searchQuery.trim()) {
        // Nếu không có từ khóa tìm kiếm, chỉ cần tải lại danh sách sinh viên dựa vào lớp được chọn (nếu có)
        if (this.selectedClass) {
          await this.filterStudentsByClass({ target: { value: this.selectedClass } });
        } else {
          await this.fetchStudent();
        }
        return;
      }

      try {
        const token = sessionStorage.getItem('jwtToken');
        const apiEndpoint = this.selectedClass
            ? `http://localhost:8088/fja-fap/staff/search-student?name=${this.searchQuery}&page=${this.studentPagination.currentPage - 1}&size=${this.studentPagination.itemsPerPage}&class_id=${this.selectedClass}`
            : `http://localhost:8088/fja-fap/staff/search-student-by-batch?name=${this.searchQuery}&page=${this.studentPagination.currentPage - 1}&size=${this.studentPagination.itemsPerPage}&batch_name=${this.batchName}`;

        const response = await axios.get(apiEndpoint, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });

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

          // Cập nhật phân trang
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

    async fetchClass() {
      try {
        const token = sessionStorage.getItem('jwtToken');

        const response = await axios.get(`http://localhost:8088/fja-fap/staff/get-class-by-batch?batch_name=${this.batchName}&page=${this.classPagination.currentPage - 1}&size=${this.classPagination.itemsPerPage}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.status === 200 && response.data.result) {
          this.classes = await Promise.all(
            response.data.result.content.map(async (classItem) => {
              // Gọi API lấy số lượng sinh viên cho từng lớp
              const studentCount = await this.fetchStudentCountByClass(classItem.classId);
              return {
                id: classItem.classId || "Unknown ID",
                name: classItem.className || "Unknown Name",
                color: classItem.classColour || "#000000",
                numberOfStudents: studentCount || 0 // Gán số lượng sinh viên
              };
            })
          );

          this.classPagination.totalElements = response.data.result.totalElements;
          this.classPagination.totalPages = response.data.result.totalPages;

          this.updateClassDisplayedPages();
        }
      } catch (error) {
        console.error('Error fetching classes:', error);
        alert('Đã có lỗi xảy ra khi lấy danh sách lớp.');
      }
    },

    async fetchClassFilter() {
      try {
        const token = sessionStorage.getItem('jwtToken');

        const response = await axios.get(`http://localhost:8088/fja-fap/staff/get-class-by-batch?batch_name=${this.batchName}&page=${this.classPagination.currentPage - 1}&size=${this.classPagination.itemsPerPage}`, {
          params: {
            page: 0,
            size: 100,
          },
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.status === 200 && response.data.result) {
          this.classList = response.data.result.content.map((item) => ({
            id: item.classId || "Unknown ID",
            name: item.className || "Unknown Name",
          }));
        }
      } catch (error) {
        console.error('Error fetching classes:', error);
        alert('Đã có lỗi xảy ra khi lấy danh sách lớp.');
      }
    },

    async addClass() {
      try {
        const token = sessionStorage.getItem('jwtToken');

        // API call to create a new class
        const response = await axios.post(
          'http://localhost:8088/fja-fap/staff/create-class',
          {
            className: this.newClass.name,
            classColour: this.newClass.color,
            batchName: this.batchName,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        // Successfully created the class
        const addedClass = response.data;
        this.classes.push({
          id: addedClass.id,
          name: addedClass.className,
          color: addedClass.classColour,
        });

        // Reset class form and close popup
        this.resetNewClass();
        this.showAddClassPopup = false;
        this.fetchClass();
        this.fetchClassFilter();
        this.showNotification('Class created successfully!', 'success');
      } catch (error) {
        console.error('Error creating class:', error);

        if (error.response && error.response.data) {
          this.showNotification(error.response.data.message || 'Error creating class!', 'error');
        } else {
          this.showNotification('Unexpected error occurred!', 'error');
        }
      }
    },

    async editClass() {
      if (!this.editedClass.name.trim()) {
        this.showNotification("Class name is required.", "error");
        return;
      }

      try {
        const token = sessionStorage.getItem('jwtToken');
        await axios.post(
          `http://localhost:8088/fja-fap/staff/update-class/${this.editedClass.id}`,
          {
            className: this.editedClass.name,
            classColour: this.editedClass.color,
          },
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        await this.fetchClass();
        await this.fetchClassFilter();
        this.showEditClassPopup = false;

        this.editedClass = { id: "", name: "", color: "" };

        this.showNotification("Class updated successfully!", "success");
      } catch (error) {
        console.error("Error updating class:", error);
        this.showNotification(error.response?.data?.message || "Error updating class. Please try again.", "error");
      }
    },

    openEditClassPopup(classItem) {
      this.editedClass = {
        id: classItem.id,
        name: classItem.name,
        color: classItem.color,
      };
      this.showEditClassPopup = true;
    },

    async filterStudentsByClass(event) {
      const selectedClass = event.target.value; // Lấy giá trị từ dropdown
      if (selectedClass === "") {
        this.fetchStudent();
        return;
      }

      const token = sessionStorage.getItem('jwtToken'); // Lấy token nếu cần

      try {
        // Gọi API với `class_name` và `batch_name`
        const response = await axios.get(
          `http://localhost:8088/fja-fap/staff/get-student-by-batch-class`,
          {
            params: {
              page: this.studentPagination.currentPage - 1, // Phân trang
              size: this.studentPagination.itemsPerPage,
              batch_name: this.batchName, // Tên batch
              class_id: selectedClass // Lớp được chọn
            },
            headers: {
              Authorization: `Bearer ${token}`,
            }
          }
        );

        // Cập nhật danh sách sinh viên và phân trang
        if (response.status === 200 && response.data.result) {
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
          return response.data.result.numberOfElements;
        }
      } catch (error) {
        console.error("Error filtering students by class:", error);
        alert("Đã xảy ra lỗi khi lọc danh sách sinh viên.");
      }
    },

    async fetchStudentCountByClass(className) {
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(`http://localhost:8088/fja-fap/staff/get-student-by-batch-class`, {
          params: {
            batch_name: this.batchName,
            class_id: className,
            page: 0, // Không cần phân trang khi đếm số lượng
            size: 1000
          },
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.status === 200 && response.data.result) {
          return response.data.result.totalElements || 0; // Trả về số lượng sinh viên
        }
      } catch (error) {
        console.error(`Error fetching student count for class ${className}:`, error);
        return 0; // Trả về 0 nếu có lỗi
      }
    },

    updateClassDisplayedPages() {
      const pages = [];
      const { currentPage, totalPages } = this.classPagination;
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
      this.classPagination.displayedPages = pages;
    },

    changeClassPage(newPage) {
      if (newPage > 0 && newPage <= this.classPagination.totalPages) {
        this.classPagination.currentPage = newPage;
        this.fetchClass();
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
    this.fetchClass();
    this.fetchClassFilter();
  },
};
</script>

<style lang="scss" scoped>
#index{
  width: 8%;
}
#action{
  width: 10%;
}
#class{
  width: 10%;
}
#email{
  width: 23%;
}
.container {
  .btn-group {
    display: flex;
    flex-direction: row;
    padding: 5px 5px;
    width: fit-content;
    border-radius: 10px;
    background: linear-gradient(to right, #1A2C6F, #304CB2);

    button {
      background: none;
      padding: 10px 40px;
      border-radius: 6px;
      color: #fff;
      font-weight: bold;
    }

    .active-tab {
      background: #fff;
      color: #1A2C6F;
    }
  }

  .record {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }

  #color-picker {
    width: 250px;
    display: flex;
    align-items: flex-start;

    input {
      width: 80px;
      height: 30px;
      padding: 0px 5px;
    }
  }
}
</style>
