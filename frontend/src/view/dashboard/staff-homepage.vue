<template>
  <div class="container">
    <div class="daily-information">
      <div id="daily-status">
        <div class="daily-status-bar" id="student-attendance">
          <p class="status-number">{{totalAttendance }}</p>
          <p class="status-description">students present today</p>
        </div>

        <div class="daily-status-bar">
          <p class="status-number">{{totalMarked }}</p>
          <p class="status-description">exams marked today</p>
        </div>

        <div class="daily-status-bar">
          <p class="status-number">{{unMarked }}</p>
          <p class="status-description">exams unmarked today</p>
        </div>
      </div>

      <div class="calendar">
        <div class="date-month">
          <p class="date">
            {{ currentDate }}
          </p>
          <p class="month">
            {{ currentMonth }}
          </p>
        </div>
        <div class="weekday">
          <p class="weekday">
            {{ currentWeekday }}
          </p>
        </div>
      </div>

    </div>


    <div class="row-2">
      <div id="shortcut-container">
        <div class="shortcut-row">
          <div class="shortcut">
            <div class="shortcut-upper">
              <div class="information">
                <h2>Student Record</h2>
                <p class="shortcut-description">{{ totalStudentRecords }} records</p>
              </div>
              <div class="logo">
                <VsxIcon iconName="People" size="25" type="linear" color="#fff" />
              </div>

            </div>
            <div class="shortcut-lower">
              <button @click="navigateToStudentRecord">View</button>
            </div>
          </div>

          <div class="shortcut">
            <div class="shortcut-upper">
              <div class="information">
                <h2>Teacher Record</h2>
                <p class="shortcut-description">{{ totalTeacherRecords }} records</p>
              </div>
              <div class="logo">
                <VsxIcon iconName="Teacher" size="25" type="linear" color="#fff" />
              </div>

            </div>
            <div class="shortcut-lower">
              <button @click="navigateToTeacherRecord">View</button>
            </div>
          </div>
        </div>

        <div class="shortcut-row">
          <div class="shortcut">
            <div class="shortcut-upper">
              <div class="information">
                <h2>Schedule</h2>
                <p class="shortcut-description"></p>
              </div>
              <div class="logo">
                <VsxIcon iconName="Calendar" size="25" type="linear" color="#fff" />
              </div>

            </div>
            <div class="shortcut-lower">
              <button @click="navigateToSchedule">View</button>
            </div>
          </div>

          <div class="shortcut">
            <div class="shortcut-upper">
              <div class="information">
                <h2>Mark</h2>
                <p class="shortcut-description"></p>
              </div>
              <div class="logo">
                <VsxIcon iconName="Award" size="25" type="linear" color="#fff" />
              </div>

            </div>
            <div class="shortcut-lower">
              <button @click="navigateToMarkRecord">View</button>
            </div>
          </div>
        </div>
      </div>

      <div id="news-container">
        <div class="news-upper">
          <h2>News</h2>
          <p id="see-all" @click="navigateToNewsRecord">See all</p>
        </div>

        <div class="news-lower">
          <div v-for="news in newsList" :key="news.newId" class="news-row">
            <p class="date">{{ new Date(news.createDate).toLocaleDateString() }}</p>
            <p class="title">{{ news.newTitle }}</p>
          </div>
        </div>
      </div>
    </div>

    <div class="row-3">
      <div class="chart-container">
        <h3>Daily mark report</h3>
        <Line id="mark-report" :options="options" :data="chartData" />
      </div>

    </div>
  </div>
</template>

<script>
import axios from "axios";
import { Line } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, PointElement, CategoryScale, LineController, LineElement, LinearScale } from 'chart.js'

ChartJS.register(Title, Tooltip, Legend, PointElement, CategoryScale, LineController, LineElement, LinearScale)

export default {
  name: "StaffHomepage",
  components: {
    Line
  },
  data() {
    return {
      totalAttendance:'',
      totalMarked:'',
      unMarked: '',
      currentDate: null,
      currentMonth: null,
      currentWeekday: null,
      totalTeacherRecords: 0,
      totalStudentRecords: 0,
      newsList: [],

      chartData: {
        labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'],
        datasets: [{
          label: "Purple",
          data: [3, 5, 2, 8, 12, 1, 3, 5, 9, 12],
          fill: true,
          borderColor: '#AF52DE',
          tension: 0.5,
          pointBackgroundColor: '#AF52DE',
          backgroundColor: '#AF52DE',
        },
        {
          label: "Green",
          data: [5, 2, 5, 7, 8, 9, 10, 11, 12, 13],
          fill: true,
          borderColor: '#6ECBB8',
          tension: 0.5,
          pointBackgroundColor: '#6ECBB8',
          backgroundColor: '#6ECBB8',
        }]
      },

      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: 'Daily mark report'
          },
        },
        scales: {
          y: {
            title: {
              display: true,
              text: "Number of student"
            }
          },
          x: {
            title: {
              display: true,
              text: "Mark"
            }
          }
        }
      },
    }
  },
  methods: {
    updateDate() {
      const date = new Date();
      this.currentDate = date.getDate();
      this.currentMonth = date.toLocaleString('en-US', { month: 'long' });
      this.currentWeekday = date.toLocaleString('en-US', { weekday: 'long' });
    },
    showNotification(message, type) {
      console.log(`${type.toUpperCase()}: ${message}`);
      // toast
    },
    navigateToStudentRecord() {
      this.$router.push({ name: "StudentRecord" });
    },
    navigateToTeacherRecord() {
      this.$router.push({ name: "TeacherRecord" });
    },
    navigateToSchedule() {
      this.$router.push({ name: "Schedule" });
    },
    navigateToMarkRecord() {
      this.$router.push({ name: "Mark" });
    },
    navigateToNewsRecord() {
      this.$router.push({ name: "StaffNews" });
    },
    async countTeacherRecord() {
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
          `http://localhost:8088/fja-fap/staff/get-all-teacher?`,
          {
            params: {
              page: 0,
              size: 1000
            },
            headers: { Authorization: `Bearer ${token}` }
          }
        );

        if (response.data.code === 1000) {
          this.totalTeacherRecords = response.data.result.totalElements || 0;
        } else {
          this.showNotification(
            'Unable to load teacher records: ' + response.data.message,
            'error'
          );
        }
      } catch (error) {
        console.error('Error fetching teacher records:', error);
        this.showNotification(
          'An error occurred while loading teacher records.',
          'error'
        );
      }
    },
    async countStudentRecord() {
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
          `http://localhost:8088/fja-fap/staff/get-student-by-batch`,
          {
            params: {
              page: 0,
              size: 10000,
              batch_name: "Fall24"
            },
            headers: { Authorization: `Bearer ${token}` }
          }
        );

        if (response.data.code === 0) {
          this.totalStudentRecords = response.data.result.totalElements || 0;
        } else {
          this.showNotification(
            'Unable to load student records: ' + response.data.message,
            'error'
          );
        }
      } catch (error) {
        console.error('Error fetching student records:', error);
        this.showNotification(
          'An error occurred while loading student records.',
          'error'
        );
      }
    },
    async activeData() {
      try {
        // Lấy ngày định dạng YYYY-MM-DD
        //const date = new Date().toISOString().split('T')[0];
        const date = "2024-12-16";

        // Lấy token từ sessionStorage
        const token = sessionStorage.getItem('jwtToken');
        if (!token) {
          this.showNotification('Authentication token is missing.', 'error');
          return;
        }

        // Gửi yêu cầu tới API
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-daily-report?date=${date}`,
            {
              headers: { Authorization: `Bearer ${token}` }
            }
        );

        // Xử lý kết quả trả về
        if (response.data.code === 0) {
          const { totalAttendance, totalMarked, unMarked } = response.data.result;

          // Gán giá trị trả về vào các biến (hoặc state nếu dùng Vuex)
          this.totalAttendance = totalAttendance || 0;
          this.totalMarked = totalMarked || 0;
          this.unMarked = unMarked || 0;

          // Thông báo thành công nếu cần
          this.showNotification('Student records loaded successfully.', 'success');
        } else {
          // Xử lý trường hợp API trả về lỗi
          this.showNotification(
              `Unable to load student records: ${response.data.message}`,
              'error'
          );
        }
      } catch (error) {
        console.error('Error fetching student records:', error);

        // Hiển thị lỗi chi tiết nếu có
        const errorMessage =
            error.response?.data?.message || 'An error occurred while loading student records.';
        this.showNotification(errorMessage, 'error');
      }
    },
    async fetchAllNews() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
          "http://localhost:8088/fja-fap/staff/get-all-news",
          {
            params: {
              page: 0,
              size: 10,
            },
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        if (response.data.code === 0) {
          this.newsList = response.data.result.content || [];
        } else {
          this.showNotification(
            "Unable to load news: " + response.data.message,
            "error"
          );
        }
      } catch (error) {
        console.error("Error fetching news:", error);
        this.showNotification(
          "An error occurred while loading news.",
          "error"
        );
      }
    },
  },
  mounted() {
    this.updateDate();
    this.countTeacherRecord();
    this.countStudentRecord();
    this.fetchAllNews();
    this.activeData();
  },
}
</script>

<style lang="scss" scoped>
.container {
  display: flex;
  flex-direction: column;
  gap: 20px;

  h2 {
    margin: 0;
  }

  .daily-information {
    display: flex;
    flex-direction: row;
    justify-content: space-between;

    .calendar {
      display: flex;
      flex-direction: row;
      border-radius: 10px;
      background: linear-gradient(to right, #4058ae 5%, #3F5FD8 80%, #4058ae);
      padding: 10px 20px;
      color: #fff;
      gap: 20px;

      .date-month {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding-right: 20px;
        border-right: 2px dashed #fff;

        .date {
          font-size: 48px;
          font-weight: bold;
        }
      }

      .weekday {
        font-size: 24px;
        display: flex;
        align-items: center;
      }
    }

    #daily-status {
      display: flex;
      flex-direction: row;
      gap: 30px;

      .daily-status-bar {
        display: flex;
        flex-direction: column;
        padding: 20px 60px 20px 40px;
        border-radius: 10px;
        background-color: #BCC5E6;

        .status-number {
          font-size: 30px;
          font-weight: bold;
          color: #233E8B;
        }

        .status-description {
          color: #233E8B;
        }
      }

      #student-attendance {
        background-color: #233E8B;

        .status-number,
        .status-description {
          color: #fff;
        }
      }
    }
  }

  .row-2 {
    display: flex;
    flex-direction: row;
    gap: 20px;

    #shortcut-container {
      width: 70%;
      display: flex;
      flex-direction: column;
      gap: 30px;

      .shortcut-row {
        display: flex;
        flex-direction: row;
        gap: 30px;

        .shortcut {
          width: 50%;
          display: flex;
          flex-direction: column;
          border: 2px solid #3F5FD8;
          border-radius: 10px;
          padding: 20px;
          gap: 30px;

          .shortcut-upper {
            display: flex;
            flex-direction: row;
            gap: 5px;
            justify-content: space-between;

            .information {
              display: flex;
              flex-direction: column;
              gap: 10px;

              .shortcut-description {
                font-size: 14px;
                color: #50627E;
              }
            }

            .logo {
              display: flex;
              align-items: center;
              justify-content: center;
              padding: 5px 15px;
              border-radius: 50%;
              background: radial-gradient(circle, #3F5FD8 70%, #213272);
            }
          }

          .shortcut-lower {
            display: flex;

            button {
              width: 100%;
              background: linear-gradient(to right, #3F5FD8 90%, #4058ae);
              font-weight: bold;
              justify-content: center;
            }
          }
        }
      }
    }

    #news-container {
      display: flex;
      flex-direction: column;
      padding: 30px;
      width: 30%;
      background: #DFE7FB;
      border-radius: 10px;
      gap: 20px;

      .news-upper {
        display: flex;
        flex-direction: row;
        justify-content: space-between;

        #news-title {
          font-size: 24px;
          font-weight: bold;

        }

        #see-all {
          color: #1A2C6F;
          margin: auto 0;
          cursor: pointer;
        }
      }

      .news-lower {
        display: flex;
        flex-direction: column;
        gap: 10px;

        .news-row {
          display: flex;
          flex-direction: row;
          gap: 10px;

          .title {
            white-space: nowrap;
            width: 100%;
            text-overflow: ellipsis;
            overflow: hidden;
          }
        }
      }
    }
  }

  .row-3 {
    display: flex;
    flex-direction: column;
    gap: 20px;
    justify-content: center;

    .chart-container {

      canvas {
        width: 80%;
      }
    }
  }
}
</style>