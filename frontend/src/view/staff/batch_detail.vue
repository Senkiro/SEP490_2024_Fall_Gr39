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
        <button class="btn btn-add-student" @click="openAddStudentPopup">
          <VsxIcon iconName="AddCircle" size="20" type="bold" />
          Add student
        </button>
        <button class="btn btn-import-student" @click="navigateToImportStudent">
          <VsxIcon iconName="Import" size="20" type="bold" />
          Import student
        </button>

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
          <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">1</button>
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
  <div v-if="showAddClassPopup" class="popup-overlay">
    <div class="popup">
      <h2>Add class</h2>
      <form @submit.prevent="addClass">
        <div class="form-group">
          <label for="className">Class name *</label>
          <input type="text" id="className" v-model="newClass.name" required />
        </div>
        <div class="form-group">
          <label for="classColor">Color *</label>
          <input type="color" id="classColor" v-model="newClass.color" required />
        </div>
        <div class="actions">
          <button type="submit" class="btn btn-create">Create</button>
          <button type="button" class="btn btn-cancel" @click="showAddClassPopup = false">Cancel</button>
        </div>
      </form>
    </div>
  </div>

  <!-- <div v-if="notification.message" :class="['notification', notification.type]">
    {{ notification.message }}
  </div> -->
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
      batchName: this.$route.params.batchName,
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
      this.showAddStudentPopup = false;
      this.resetNewStudent();
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
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  .btn-group {
    display: flex;
    flex-direction: row;
    padding: 5px 5px;
    margin: 20px 0px;
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
