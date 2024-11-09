<template>
  <div class="container">
    <div class="headContent">
      <h1>Student Profile</h1>
    </div>

    <section class="student-info-card">

      <div class="student-info-wrapper">
        <!-- Khung ảnh sinh viên nằm bên trái -->
        <div class="student-avatar-container">
          <img src="/path/to/student-image.png" alt="Student Avatar" class="student-avatar">
        </div>
        <!-- Thông tin sinh viên nằm bên phải -->
        <div class="student-details-container">
          <div class="name-id-container">
            <div class="name-id">
              <h3><strong>Mai Thế Nam - マイ・テ・ナム</strong></h3>
              <p>HE123456</p>
            </div>
            <button class="btn-edit">
              <VsxIcon iconName="Edit2" :size="18" color="#495057" type="linear" />Edit
            </button>
          </div>
          <div class="student-details">
            <div class="column column1">
              <div class="student-attribute">
                <p>Batch</p>
                <strong>FALL2024</strong>
              </div>

              <div class="student-attribute">
                <p>Gender</p>
                <strong>Male</strong>
              </div>

              <div class="student-attribute">
                <p>DOB</p>
                <strong>22/02/2001</strong>
              </div>
            </div>

            <div class="column column2">
              <div class="student-attribute">
                <p>Class</p>
                <strong style="color: green">Green</strong>
              </div>

              <div class="student-attribute">
                <p>Phone</p>
                <strong>0123456789</strong>
              </div>

              <div class="student-attribute">
                <p>Email</p>
                <strong>nammthe160123@fpt.edu.vn</strong>
              </div>
            </div>

            <div class="column column3">
              <div class="average-score-box">
                <div class="score-box-upper">
                  <strong>Average Score</strong>
                  <button class="btn-view-detail">View detail</button>
                </div>
                <div class="score-box-lower">
                  <strong class="score-status">Very Good</strong>
                  <p class="score">8.6</p>
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

<style lang="scss" scoped>
.container {
  padding: 20px;

  .student-info-card {
    padding: 20px;
  }

  .student-info-wrapper {
    display: flex;
    align-items: flex-start;
    gap: 20px;

  }

  .student-avatar-container {
    display: flex;
    align-self: center;
    width: 150px;
    height: 200px;
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
    border: 1px solid #ddd;
    border-radius: 20px;
    padding: 20px 30px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);

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

    .student-details {
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      font-size: 14px;

      .column3 {
        width: 350px;
      }

      .column {
        display: flex;
        gap: 20px;
        flex-direction: column;

        .student-attribute {
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
              border: 1px solid green;
              background: white;
              border-radius: 10px;
              align-content: center;
              padding: 10px;
            }

            .score {
              font-size: 20px;
              font-weight: bold;
              margin-top: auto;
            }
          }
        }
      }
    }
  }

  .tab-buttons-container {
    width: 100%;
    margin-top: 20px;
    margin-bottom: 10px;
    border: 1px solid #ddd;
    border-radius: 1rem;
    padding: 15px;

    .tab-buttons {
      display: flex;
      gap: 10px;
      border-bottom: 1px solid #E0E0E0;

      .tab-button {
        padding: 10px;
        background: none;
        border: none;
        cursor: pointer;
        font-size: 14px;
        color: #726C6C;
        border-radius: 0;
      }

      .active {
        border-bottom: 3px solid #3E5DD4;

        h3 {
          font-weight: bold;
          color: #212529;
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
