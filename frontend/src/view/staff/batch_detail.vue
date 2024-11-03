<template>
  <div class="container">
    <div class="batch-title-container">
      <h1 class="batch-title">{{ batchEntity.name }}</h1>
    </div>
    <div class="headContent">
      <div class="tab">
        <VaButtonGroup>
          <VaButton @click="switchTab('student')" :class="{'active-tab': activeTab === 'student'}" class="buttonGroup" style="--va-background-color: none; padding: 0px 30px;">Student record</VaButton>
          <VaButton @click="switchTab('class')" :class="{'active-tab': activeTab === 'class'}" class="buttonGroup" style="--va-background-color: none; padding: 0px 30px;">Class record</VaButton>
        </VaButtonGroup>
      </div>
      <div class="actions">
        <button class="btn btn-add-student">
          <VsxIcon iconName="Import" size="20" type="bold" />
          Import student
        </button>
        <button class="btn btn-import-student">
          <VsxIcon iconName="AddCircle" size="20" type="bold" />
          Add student
        </button>
      </div>
    </div>

    <div class="batchEntity-detail-content">
      <div v-if="activeTab === 'student'" class="student-record">
        <table class="student-table">
          <thead>
          <tr>
            <th>No</th>
            <th>Fullname</th>
            <th>Roll number</th>
            <th>Japanese name</th>
            <th>Class</th>
            <th>Email</th>
            <th>Action</th>
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
            <td>
              <VsxIcon iconName="Eye" :size="24" color="#5584FF" type="linear" @click="viewStudentDetail(student)" />
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div v-if="activeTab === 'class'" class="class-record">
        <!-- ClassEntity record content goes here -->
      </div>
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
      students: [
        { id: 1, fullname: 'Pham The Minh', rollNumber: 'FA171392', japaneseName: 'ファム・テ・ミン', class: 'Blue', classColor: 'blue', email: 'minhpthe171392@fpt.edu.vn' },
        { id: 2, fullname: 'Ngo Quoc Dat', rollNumber: 'FA171288', japaneseName: '...', class: 'Red', classColor: 'red', email: 'datdqhe163173@fpt.edu.vn' },
        { id: 3, fullname: 'Mai The Nam', rollNumber: 'FA162133', japaneseName: '...', class: 'Green', classColor: 'green', email: 'namtthe161170@fpt.edu.vn' },
        { id: 4, fullname: 'Hoang Thai Son', rollNumber: 'FA123456', japaneseName: '...', class: 'Yellow', classColor: 'yellow', email: 'sonhthe163355@fpt.edu.vn' },
        { id: 5, fullname: 'Phan Khanh Hoang', rollNumber: 'FA122233', japaneseName: '...', class: 'Purple', classColor: 'purple', email: 'hoangkhe170940@fpt.edu.vn' },
        { id: 6, fullname: 'Nguyen Ha Phuong', rollNumber: 'FA000001', japaneseName: '...', class: 'Blue', classColor: 'blue', email: 'phuonghhe162120@fpt.edu.vn' }
      ]
    };
  },
  methods: {
    switchTab(tab) {
      this.activeTab = tab;
    },
    viewStudentDetail(student) {
      // Logic for viewing student details
      console.log("Viewing student: ", student);
    }
  }
};
</script>

<style scoped>
.batch-title-container {
  text-align: center; /* Căn giữa tiêu đề */
  margin-bottom: 20px; /* Khoảng cách giữa tiêu đề và phần nội dung dưới */
}

.batch-title {
  font-size: 24px; /* Kích thước chữ tiêu đề */
  font-weight: bold; /* Làm đậm chữ */
  color: #304CB2; /* Màu sắc tùy chỉnh cho tiêu đề */
}

.headContent {
  display: flex;
  justify-content: space-between; /* Căn giữa nội dung bên trong */
  align-items: center; /* Căn chỉnh theo chiều dọc */
  margin-bottom: 20px;
}

.tab {
  background-image: linear-gradient(-90deg, #304CB2, #1A2C6F);
  padding: 5px;
  width: fit-content;
  border-radius: 8px;
  position: relative; /* Thêm vị trí tương đối cho tab */
}

.actions {
  display: flex; /* Sử dụng flexbox cho các nút */
  gap: 10px; /* Khoảng cách giữa các nút */
  margin-left: auto; /* Đẩy các nút sang bên phải */
}

.btn-add-student,
.btn-import-student {
  background-color: #4a90e2;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.student-table {
  width: 100%;
  border-collapse: collapse;
}

.student-table th,
.student-table td {
  padding: 12px;
  border-bottom: 1px solid #ddd;
  text-align: left;
}

.student-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}
</style>
