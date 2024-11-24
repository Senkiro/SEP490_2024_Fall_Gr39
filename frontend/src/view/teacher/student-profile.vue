<template>
    <div class="container">
      <div class="headContent">
        <h1>Student Profile</h1>
      </div>
  
      <section class="info-card">
        <div class="info-wrapper">
          <img :src="studentData.userInforResponse.img || require('@/assets/smiling-young-man-illustration_1308-174669.avif')"
               alt="Student Avatar"
               class="avatar">
  
          <div class="details-container">
            <div class="name-id-container">
              <div class="name-id">
                <h3><strong>{{ studentData.userInforResponse.fullName }} - {{ studentData.userInforResponse.japaneseName }}</strong></h3>
                <p>{{ studentData.rollNumber }}</p>
              </div>
            </div>
  
            <div class="details">
              <div class="column column1">
                <div class="attribute">
                  <p>Batch</p>
                  <strong>{{ studentData.batchName }}</strong>
                </div>
                <div class="attribute">
                  <p>Gender</p>
                  <strong>{{ studentData.userInforResponse.gender ? 'Male' : 'Female' }}</strong>
                </div>
                <div class="attribute">
                  <p>DOB</p>
                  <strong>{{ formatDate(studentData.userInforResponse.dob) }}</strong>
                </div>
              </div>
  
              <div class="column column2">
                <div class="attribute">
                  <p>Class</p>
                  <strong :style="{ color: studentData.classResponse.classColour }">{{ studentData.classResponse.className }}</strong>
                </div>
                <div class="attribute">
                  <p>Phone</p>
                  <strong>{{ studentData.userInforResponse.phone || 'N/A' }}</strong>
                </div>
                <div class="attribute">
                  <p>Email</p>
                  <strong>{{ studentData.userInforResponse.email }}</strong>
                </div>
              </div>
  
              <div class="column column3">
                <div class="average-score-box">
                  <div class="score-box-upper">
                    <strong>Average Score</strong>
                  </div>
                  <div class="score-box-lower">
                    <p class="score">8.6</p>
                    <strong class="score-status">Very Good</strong>
                  </div>
                </div>
              </div>
  
            </div>
          </div>
        </div>
      </section>
  
      <div class="tab-buttons-container">
        <div class="tab-buttons">
          <button class="tab-button" :class="{ active: showAttendance }" @click="showAttendance = true">
            <h3>Attendance Report</h3>
          </button>
          <button class="tab-button" :class="{ active: !showAttendance }" @click="showAttendance = false">
            <h3>Mark Report</h3>
          </button>
          <button class="tab-button">
            <h3>Essay Submission</h3>
          </button>
        </div>
      </div>
  
      <!-- Attendance Report Table -->
      <div class="table-container">
        <table v-if="showAttendance">
          <thead>
          <tr>
            <th class="center">Session No</th>
            <th>Date</th>
            <th>Slot</th>
            <th>Teacher</th>
            <th class="center">Attendance</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td class="center">1</td>
            <td>2/9/2024</td>
            <td>Morning (8:30 - 12:30)</td>
            <td>Yuri Ikeda</td>
            <td class="center attended">Attend</td>
          </tr>
          <tr>
            <td class="center">2</td>
            <td>x/5/2024</td>
            <td>Morning (8:30 - 12:30)</td>
            <td>Yuri Ikeda</td>
            <td class="center attended">Attend</td>
          </tr>
          <tr>
            <td class="center">3</td>
            <td>y/1/2024</td>
            <td>Morning (8:30 - 12:30)</td>
            <td>Yuri Ikeda</td>
            <td class="center absent">Absent</td>
          </tr>
          </tbody>
        </table>
  
        <table v-else>
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
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import axios from 'axios';
  import { useRoute } from 'vue-router';
  
  const studentId = ref('');
  const studentData = ref({
    studentId: '',
    rollNumber: '',
    userInforResponse: {
      fullName: '',
      japaneseName: '',
      dob: '',
      img: '',
      gender: '',
      email: '',
      phone: ''
    },
    batchName: '',
    classResponse: {
      classId: '',
      className: '',
      classColour: ''
    }
  });
  
  const showAttendance = ref(true);
  const markReport = ref([
    { category: 'Participation', item: 'Total', weight: '10%', value: '9.0', comment: 'Excellent' },
    { category: 'Daily Exam', item: 'Total', weight: '20%', value: '8.5', comment: 'Very Good' },
    { category: 'Mid-term Exam', item: 'Total', weight: '30%', value: '8.0', comment: 'Good' },
    { category: 'Final Exam', item: 'Average', weight: '40%', value: '7.5', comment: 'Good' },
    { category: 'Course Total', item: 'Status', weight: '-', value: '-', comment: 'Good' },
  ]);
  
  studentId.value = useRoute().params.id;
  
  const fetchStudentData = async () => {
    try {
      const token = sessionStorage.getItem('jwtToken');
      const response = await axios.get(
          `http://localhost:8088/fja-fap/staff/get-student/${studentId.value}`,
          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          }
      );
  
      if (response.data && response.data.result) {
        studentData.value = response.data.result;
      }
    } catch (error) {
      console.error('Error fetching student data:', error);
    }
  };
  
  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    const [year, month, day] = dateString.split('-');
    return `${day}/${month}/${year}`;
  };
  
  onMounted(() => {
    fetchStudentData();
  });
  </script>
  
  <style lang="scss" scoped>
  .container {
    padding: 20px;
  
    .info-wrapper {
      display: flex;
      align-items: flex-start;
      gap: 20px;
      max-height: 400px;
  
      img {
        height: 290px;
        border-radius: 20px;
      }
    }
  
    .details-container {
      display: flex;
      flex-direction: column;
      flex-grow: 1;
      border: 1px solid #ddd;
      border-radius: 20px;
      padding: 20px 30px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      gap: 30px;
  
      .name-id-container {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        padding-bottom: 20px;
  
        .btn-edit {
          display: flex;
          color: var(--border);
          background: none;
          border: 0.2px solid var(--border);
          border-radius: 5px;
          padding: 3px 8px;
          font-size: 12px;
          height: fit-content;
          align-items: center;
          gap: 5px;
        }
      }
  
      .details {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        font-size: 14px;
  
        .column3 {
          width: 350px;
          justify-content: flex-end;
        }
  
        .column {
          display: flex;
          gap: 20px;
          flex-direction: column;
  
          .attribute {
            display: flex;
            flex-direction: column;
            gap: 5px;
  
            p {
              color: #6C757D;
            }
          }
  
          .average-score-box {
            display: flex;
            flex-direction: column;
            border-radius: 12px;
            background-color: #D6EAFF;
            padding: 15px;
            width: 350px;
            text-align: center;
            font-size: 14px;
            gap: 15px;
  
            .score-box-upper {
              display: flex;
              justify-content: space-between;
  
              .btn-view-detail {
                padding: 0px 30px;
                border-radius: 10px;
                border: none;
                background-image: linear-gradient(to left, #1A2C6F 0%, #3254D5 100%);
                color: white;
                height: 50px;
              }
            }
  
            .score-box-lower {
              display: flex;
              justify-content: space-between;
  
              .score-status {
                border: 2px solid green;
                background: white;
                border-radius: 10px;
                align-content: center;
                padding: 10px;
                color: green;
              }
  
              .score {
                width: 80px;
                text-align: center;
                font-size: 20px;
                font-weight: bold;
                margin-top: auto;
              }
            }
          }
        }
      }
    }
  
    .attended {
      color: green;
      font-weight: bold;
    }
  
    .absent {
      color: red;
      font-weight: bold;
    }
  }
  </style>
  