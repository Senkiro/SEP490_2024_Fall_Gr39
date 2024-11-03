<template>
  <div class="container">
    <div class="batch-detail-content">
      <h1>{{ batchName }}</h1>
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
</template>

<script>
export default {
  name: "BatchDetail",
  data() {
    return {
      batchName: "FALL2024",
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
    navigateToProfile(studentId) {
      this.$router.push({ name: "StudentProfile", params: { id: studentId } });
    }
  }
};
</script>

<style scoped>
.student-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
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
.icon-button {
  cursor: pointer;
  transition: transform 0.2s;
}
.icon-button:hover {
  transform: scale(1.1);
}
</style>
