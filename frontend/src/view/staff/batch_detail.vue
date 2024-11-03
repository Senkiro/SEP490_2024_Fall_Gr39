<template>
  <div class="container">
    <div class="batch-title-container">
      <h1 class="batch-title">{{ batchEntity.name }}</h1>
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
        <div class="form-actions">
          <button type="submit" class="btn btn-create">Create</button>
          <button type="button" class="btn btn-cancel" @click="showAddStudentPopup = false">Cancel</button>
        </div>
      </form>
    </div>
  </div>

  <!-- Popup Add Class -->
  <div v-if="showAddClassPopup" class="add-class-popup-overlay">
    <div class="add-class-popup">
      <h2 class="popup-title">Add class</h2>
      <form @submit.prevent="addClass">
        <div class="form-group">
          <label for="className">Class name *</label>
          <input type="text" id="className" v-model="newClass.name" required />
        </div>
        <div class="form-group">
          <label for="classColor">Color *</label>
          <input type="color" id="classColor" v-model="newClass.color" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-create">Create</button>
          <button type="button" class="btn btn-cancel" @click="showAddClassPopup = false">Cancel</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";

export default {
  name: "BatchDetail",
  components: {
    VsxIcon
  },
  data() {
    return {
      batchEntity: {
        name: 'FALL2024'
      },
      activeTab: 'student',
      showAddStudentPopup: false,
      showAddClassPopup: false,
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
      students: [
        { id: 1, fullname: 'Pham The Minh', rollNumber: 'FA171392', japaneseName: 'ファム・テ・ミン', class: 'Blue', classColor: 'blue', email: 'minhpthe171392@fpt.edu.vn' },
        { id: 2, fullname: 'Ngo Quoc Dat', rollNumber: 'FA171288', japaneseName: '...', class: 'Red', classColor: 'red', email: 'datdqhe163173@fpt.edu.vn' },
        { id: 3, fullname: 'Mai The Nam', rollNumber: 'FA162133', japaneseName: '...', class: 'Green', classColor: 'green', email: 'namtthe161170@fpt.edu.vn' },
        { id: 4, fullname: 'Hoang Thai Son', rollNumber: 'FA123456', japaneseName: '...', class: 'Yellow', classColor: 'yellow', email: 'sonhthe163355@fpt.edu.vn' },
        { id: 5, fullname: 'Phan Khanh Hoang', rollNumber: 'FA122233', japaneseName: '...', class: 'Purple', classColor: 'purple', email: 'hoangkhe170940@fpt.edu.vn' },
        { id: 6, fullname: 'Nguyen Ha Phuong', rollNumber: 'FA000001', japaneseName: '...', class: 'Blue', classColor: 'blue', email: 'phuonghhe162120@fpt.edu.vn' }
      ],
      classes: [
        { id: 1, name: 'Blue', classColor: 'blue', studentCount: 30 },
        { id: 2, name: 'Red', classColor: 'red', studentCount: 30 },
        { id: 3, name: 'Green', classColor: 'green', studentCount: 30 },
        { id: 4, name: 'Yellow', classColor: 'yellow', studentCount: 30 },
        { id: 5, name: 'Purple', classColor: 'purple', studentCount: 30 }
      ]
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
    addStudent() {
      // Thêm sinh viên vào danh sách
      this.students.push({
        id: this.students.length + 1,
        fullname: this.newStudent.fullname,
        japaneseName: this.newStudent.japaneseName,
        email: this.newStudent.email,
        class: this.newStudent.class,
        classColor: this.getClassColor(this.newStudent.class),
        rollNumber: `FA${100000 + this.students.length + 1}`, // Roll number tự động sinh
        gender: this.newStudent.gender,
        dob: this.newStudent.dob
      });
      this.showAddStudentPopup = false; // Đóng popup sau khi thêm
      this.resetNewStudent(); // Đặt lại dữ liệu của form
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
      // Thêm lớp mới vào danh sách
      this.classes.push({
        id: this.classes.length + 1,
        name: this.newClass.name,
        classColor: this.newClass.color,
        studentCount: 0
      });
      this.showAddClassPopup = false; // Đóng popup sau khi thêm lớp
      this.resetNewClass(); // Đặt lại dữ liệu của form
    },
    resetNewClass() {
      this.newClass = {
        name: '',
        color: '#000000'
      };
    },
    navigateToImportStudent() {
      this.$router.push({ name: 'ImportStudentPage' });
    }
  }
};
</script>

<style scoped>
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
  margin-left: auto; /* Đẩy phần này sang bên phải */
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
  overflow-x: auto; /* Để bảng không bị tràn màn hình */
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
  background-color: rgba(0, 0, 0, 0.5); /* Nền mờ cho overlay */
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000; /* Đảm bảo popup ở trên cùng */
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
