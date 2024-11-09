<template>
  <div class="class-record-container">
    <header class="class-record-header">
      <h1>{{ batchEntity.name }}</h1>
      <div class="class-record-actions">
        <button class="btn btn-student-record" @click="switchTab('student')">Student Record</button>
        <button class="btn btn-class-record" @click="switchTab('class')">Class Record</button>
      </div>
    </header>
    <div class="class-record-content">
      <button class="btn btn-add-class">Add class</button>
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
          <td>
            <VsxIcon iconName="Eye" :size="24" color="#5584FF" type="linear" @click="viewClassDetail(classItem)" />
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";

export default {
  name: "ClassRecord",
  components: { VsxIcon },
  data() {
    return {
      batchEntity: { name: 'FALL2024' },
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
      this.$emit("switchTab", tab);
    },
    viewClassDetail(classItem) {
      console.log("Viewing class: ", classItem);
    }
  }
};
</script>

<style scoped>
.class-record-container {
  padding: 20px;
}

.class-record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.class-record-actions {
  display: flex;
  gap: 10px;
}

.btn {
  padding: 10px 20px;
  font-weight: bold;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.btn-student-record,
.btn-class-record {
  background-color: #4a90e2;
  color: #fff;
}

.class-record-content {
  margin-top: 20px;
}

.class-table {
  width: 100%;
  border-collapse: collapse;
}

.class-table th,
.class-table td {
  padding: 12px;
  border-bottom: 1px solid #ddd;
  text-align: left;
}

.class-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}

.btn-add-class {
  background-color: #6ECBB8;
  color: #fff;
  padding: 8px 16px;
  margin-bottom: 10px;
  border-radius: 5px;
  cursor: pointer;
}
</style>
