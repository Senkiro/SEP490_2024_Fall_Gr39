<template>
  <div class="container">
    <div class="headContent">
      <h1>{{ batchName }}</h1>
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
    <div v-if="activeTab === 'student'" class="student-record">
      <div class="filters">
        <select id="class-filter" class="filter-select">
          <option value="">Class</option>
          <option value="Blue">Blue</option>
          <option value="Red">Red</option>
          <option value="Green">Green</option>
          <option value="Yellow">Yellow</option>
          <option value="Purple">Purple</option>
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
          <input type="text" placeholder="Search..." class="search-field">
          <VsxIcon iconName="SearchNormal1" color="#ADB5BD" type="linear" />
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
              <td>{{ student.fullname }}</td>
              <td>{{ student.rollNumber }}</td>
              <td>{{ student.japaneseName }}</td>
              <td :style="{ color: student.classColor }">{{ student.class }}</td>
              <td>{{ student.email }}</td>
              <td class="center">
                <VsxIcon iconName="Eye" :size="30" color="#171717" type="linear"
                  @click="navigateToProfile(student.id)" />
              </td>
            </tr>
          </tbody>
        </table>

        <div class="pagination">
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
    </div>

    <!-- Nội dung Class Record -->
    <div v-if="activeTab === 'class'">
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
              <th>Class</th>
              <th>Number of students</th>
              <th class="center">Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(classItem, index) in classes" :key="classItem.id">
              <td class="center">{{ index + 1 }}</td>
              <td :style="{ color: classItem.classColor }">{{ classItem.name }}</td>
              <td>{{ classItem.studentCount }}</td>
              <td class="center">
                <VsxIcon iconName="Eye" :size="30" color="#171717" type="linear" @click="viewClassDetail(classItem)" />
              </td>
            </tr>
          </tbody>
        </table>
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
            <input type="email" id="email" v-model="newStudent.email" required />
          </div>
          <div class="form-group">
            <label for="class">Class <span class="required">*</span></label>
            <select id="class" v-model="newStudent.class" required>
              <option value="">Choose class now or later</option>
              <option value="Blue">Blue</option>
              <option value="Red">Red</option>
              <option value="Green">Green</option>
              <option value="Yellow">Yellow</option>
              <option value="Purple">Purple</option>
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
            <!-- <button class="btn btn-cancel" @click="showAddStudentPopup = false">Cancel</button> -->
            <button type="submit">Create</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Popup Add Class -->
    <div v-if="showAddClassPopup" class="popup-overlay">
      <div class="popup">
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
            <button type="submit">Create</button>
            <button class="btn btn-cancel" @click="showAddClassPopup = false">Cancel</button>
          </div>
        </form>
      </div>
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
        gender: ''
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
        gender: ''
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
          pages.push(1, '...', this.currentPage - 1, this.currentPage, this.currentPage + 1, '...', this.totalPages);
        }
      }
      this.displayedPages = pages;
    },
    changePage(newPage) {
      if (newPage > 0 && newPage <= this.totalPages) {
        this.currentPage = newPage;
        this.fetchStudent();
      }
    },
    formatDate(dob) {
      const [day, month, year] = dob.split('/');
      return `${year}-${month}-${day}`;
    },
  },
  mounted() {
    this.fetchStudent();
  },
};
</script>

<style lang="scss" scoped>
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
      font-weight: semibold;
    }

    .active-tab {
      background: #fff;
      color: #1A2C6F;
    }
  }

  .student-record,
  .class-record {
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
