<template>
  <div class="student-profile-container">
    <h1 class="title">Student Profile</h1>
    <section class="student-info-card">
      <!-- Tiêu đề "Normal Information" -->
      <h2 class="info-title">Normal Information</h2>

      <div class="student-info-wrapper">
        <!-- Khung ảnh sinh viên nằm bên trái -->
        <div class="student-avatar-container">
          <img src="/path/to/student-image.png" alt="Student Avatar" class="student-avatar">
        </div>

        <!-- Thông tin sinh viên nằm bên phải -->
        <div class="student-details-container">
          <div class="name-id-container">
            <p><strong>Mai Thế Nam - マイ・テ・ナム</strong></p>
            <p>HE123456</p>
            <button class="btn-edit">Edit</button>
          </div>
          <div class="student-details">
            <p><strong>Batch:</strong> FALL2024</p>
            <p><strong>Class:</strong> Green</p>
            <p><strong>Gender:</strong> Male</p>
            <p><strong>Phone:</strong> 0123456789</p>
            <p><strong>DOB:</strong> 22/02/2001</p>
            <p><strong>Email:</strong> namthe160123@fpt.edu.vn</p>
          </div>
          <div class="average-score-box">
            <span>Average Score</span>
            <button class="btn-view-detail">View detail</button>
            <p class="score">8.6</p>
            <p class="score-status">Very Good</p>
          </div>
        </div>
      </div>
    </section>

    <div class="tab-buttons-container">
      <div class="tab-buttons">
        <button class="tab-button" :class="{ active: showAttendance }" @click="showAttendance = true">
          Attendance Report
        </button>
        <button class="tab-button" :class="{ active: !showAttendance }" @click="showAttendance = false">
          Mark Report
        </button>
        <button class="tab-button">Essay Submission</button>
      </div>
    </div>

    <!-- Attendance Report Table -->
    <table v-if="showAttendance" class="attendance-table">
      <thead>
      <tr>
        <th>Session No</th>
        <th>Date</th>
        <th>Slot</th>
        <th>Attendance</th>
      </tr>
      </thead>
      <tbody>
      <tr>
        <td>1</td>
        <td>2/9/2024</td>
        <td>Morning (8:30 - 12:30)</td>
        <td class="attended">Attend</td>
      </tr>
      <tr>
        <td>2</td>
        <td>x/5/2024</td>
        <td>Morning (8:30 - 12:30)</td>
        <td class="attended">Attend</td>
      </tr>
      <tr>
        <td>3</td>
        <td>y/1/2024</td>
        <td>Morning (8:30 - 12:30)</td>
        <td class="absent">Absent</td>
      </tr>
      </tbody>
    </table>

    <table v-else class="mark-report-table">
      <thead>
      <tr>
        <th>Grade Category</th>
        <th>Grade Item</th>
        <th>Weight</th>
        <th>Value</th>
        <th>Comment</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="grade in markReport" :key="grade.category">
        <td>{{ grade.category }}</td>
        <td>{{ grade.item }}</td>
        <td>{{ grade.weight }}</td>
        <td>{{ grade.value }}</td>
        <td>{{ grade.comment }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const showAttendance = ref(true);
const markReport = ref([
  { category: 'Participation', item: 'Total', weight: '10%', value: '9.0', comment: 'Excellent' },
  { category: 'Daily Exam', item: 'Total', weight: '20%', value: '8.5', comment: 'Very Good' },
  { category: 'Mid-term Exam', item: 'Total', weight: '30%', value: '8.0', comment: 'Good' },
  { category: 'Final Exam', item: 'Average', weight: '40%', value: '7.5', comment: 'Good' },
  { category: 'Course Total', item: 'Status', weight: '-', value: '-', comment: 'Good' },
]);
</script>

<style scoped>
.student-profile-container {
  padding: 20px;
}

.title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.student-info-card {
  border: 1px solid #ddd;
  border-radius: 12px;
  padding: 20px;
  background-color: white;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.info-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
  text-align: center;
}

.student-info-wrapper {
  display: flex;
  align-items: flex-start;
  gap: 20px;
}

.student-avatar-container {
  width: 120px;
  height: 120px;
  border: 1px solid #ddd;
  border-radius: 12px;
  overflow: hidden;
}

.student-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 12px;
}

.student-details-container {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.name-id-container {
  display: flex;
  flex-direction: column;
  position: relative;
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
}

.btn-edit {
  position: absolute;
  top: 0;
  right: 0;
  background-color: #4a90e2;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 5px 10px;
  font-size: 12px;
  cursor: pointer;
}

.student-details {
  display: grid;
  grid-template-columns: auto auto;
  gap: 5px 15px;
  font-size: 14px;
  color: #333;
}

.average-score-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 12px;
  background-color: #dce8ff;
  padding: 15px;
  width: 120px;
  text-align: center;
  font-size: 14px;
}

.average-score-box .score {
  font-size: 24px;
  font-weight: bold;
  margin-top: 10px;
}

.average-score-box .score-status {
  color: #2e7d32;
  font-weight: bold;
}

.btn-view-detail {
  margin-top: 10px;
  padding: 5px 10px;
  background-color: #4a90e2;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 12px;
  cursor: pointer;
}

.tab-buttons-container {
  width: 100%;
  margin-top: 20px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 5px;
}

.tab-buttons {
  display: flex;
  gap: 10px;
}

.tab-button {
  padding: 10px;
  background-color: transparent;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
}

.tab-button.active {
  background-color: transparent;
  color: inherit;
}

.attendance-table,
.mark-report-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.attendance-table th,
.attendance-table td,
.mark-report-table th,
.mark-report-table td {
  padding: 10px;
  border: 1px solid #ddd;
  text-align: center;
  font-size: 14px;
}

.attended {
  color: green;
  font-weight: bold;
}

.absent {
  color: red;
  font-weight: bold;
}
</style>
