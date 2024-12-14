<template>
  <div class="container">
    <h2>Upcoming activities</h2>
    <!-- Schedule -->
    <div class="activities horizontal-activities">
      <div v-for="(sessions, date) in groupedSessionsByDate" :key="date" class="daily-activities">

        <h3 :class="date === Object.keys(groupedSessionsByDate)[0] ? 'no': ''">{{ date === Object.keys(groupedSessionsByDate)[0] ? 'Tomorrow ('+ new Date(date).toLocaleDateString() +')' : new Date(date).toLocaleDateString() }}</h3>

        <div class="activities-container">
          <div v-if="sessions.morning" class="activity">
            <template v-if="sessions.morning.curriculumnResponse">
              <div class="thumbnail">
                <p id="lesson">Lesson</p>
                <p id="number">{{ sessions.morning.curriculumnResponse?.lessonResponse?.lessonId || 'N/A' }}</p>
              </div>
              <div class="information">
                <b>{{ sessions.morning.curriculumnResponse?.lessonResponse?.lessonTitle || 'No Title' }}</b>
                <span>Exam: <b>{{ sessions.morning.curriculumnResponse?.examResponse?.examTitle || 'N/A' }}</b></span>
                <span>Teacher: <b>{{ sessions.morning.fullName || 'Unknown' }}</b></span>
              </div>
            </template>

            <template v-else-if="sessions.morning.eventResponse">
              <div class="thumbnail">
                <p id="event">Event</p>
              </div>
              <div class="information">
                <b>{{ sessions.morning.eventResponse.eventName || 'No Title' }}</b>
                <p>Destination: <b>{{ sessions.morning.eventResponse.destination || 'Unknown' }}</b></p>
              </div>
            </template>
          </div>

          <div v-if="sessions.afternoon" class="activity">
            <template v-if="sessions.afternoon.curriculumnResponse">
              <div class="thumbnail">
                <p id="lesson">Lesson</p>
                <p id="number">{{ sessions.afternoon.curriculumnResponse?.lessonResponse?.lessonId || 'N/A' }}</p>
              </div>
              <div class="information">
                <b>{{ sessions.afternoon.curriculumnResponse?.lessonResponse?.lessonTitle || 'No Title' }}</b>
                <span>Exam: <b>{{ sessions.afternoon.curriculumnResponse?.examResponse?.examTitle || 'N/A' }}</b></span>
                <span>Teacher: <b>{{ sessions.afternoon.fullName || 'Unknown' }}</b></span>
              </div>
            </template>

            <template v-else-if="sessions.afternoon.eventResponse">
              <div class="thumbnail">
                <p id="event">Event</p>
              </div>
              <div class="information">
                <b>{{ sessions.afternoon.eventResponse.eventName || 'No Title' }}</b>
                <p>Destination: <b>{{ sessions.afternoon.eventResponse.address || 'Unknown' }}</b></p>
              </div>
            </template>
          </div>
        </div>
      </div>
    </div>

    <!-- News Section -->
    <div class="row2">
      <div class="chart-container">
        <Line id="mark-report" :options="options" :data="chartData" />
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
  </div>
</template>

<script>
import axios from "axios";
import { Line } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, PointElement, CategoryScale, LineController, LineElement, LinearScale } from 'chart.js'

ChartJS.register(Title, Tooltip, Legend, PointElement, CategoryScale, LineController, LineElement, LinearScale)


export default {
  components:{
    Line
  },
  data() {
    return {
      chartData: {
        labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'],
        datasets: [{
          label: "Mark",
          data: [3, 5, 2, 8, 8, 1, 3, 5, 9, 10],
          fill: true,
          borderColor: '#AF52DE',
          tension: 0.5,
          pointBackgroundColor: '#AF52DE',
          backgroundColor: '#AF52DE',
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
              text: "Mark"
            }
          },
          x: {
            title: {
              display: true,
              text: "Exam no"
            }
          }
        }
      },
      newsList: [], // Danh sách tin tức
      sessions: [], // Tất cả hoạt động (bài học và sự kiện)
    };
  },
  computed: {
    groupedSessionsByDate() {
      const grouped = {};

      this.sessions.forEach((session) => {
        const date = session.date;
        if (!grouped[date]) {
          grouped[date] = { morning: null, afternoon: null };
        }
        if (session.timeSlotResponse?.name === "Morning") {
          grouped[date].morning = session;
        }
        if (session.timeSlotResponse?.name === "Afternoon") {
          grouped[date].afternoon = session;
        }
        if (session.note) {
          grouped[date].note = session.note;
        }
      });
      return grouped;
    },
  },
  methods: {
    fetchSessions() {
      const studentId = sessionStorage.getItem("userId");
      const token = sessionStorage.getItem("jwtToken");

      if (!studentId || !token) {
        console.error("Student ID hoặc JWT token không tồn tại trong session storage");
        return;
      }

      axios
        .get(`http://localhost:8088/fja-fap/staff/get-session-by-student?student_id=${studentId}`, {
          headers: { Authorization: `Bearer ${token}` },
        })
        .then((response) => {
          if (response.data.code === 0) {
            const allSessions = response.data.result;

            // Lọc 3 ngày gần nhất
            const today = new Date();
            const threeDaysLater = new Date();
            threeDaysLater.setDate(today.getDate() + 3);

            this.sessions = allSessions.filter((session) => {
              const sessionDate = new Date(session.date);
              return sessionDate >= today && sessionDate <= threeDaysLater;
            });
          } else {
            console.error(
              "Lỗi khi fetch dữ liệu sessions:",
              response.data.message || "Lỗi không xác định"
            );
          }
        })
        .catch((error) => {
          console.error("Lỗi khi gọi API:", error);
        });
    },
    isFirstDate(date, groupedSessionsByDate) {
      const dates = Object.keys(groupedSessionsByDate);
      return date === dates[0];
    },
    async fetchData() {
      try {
        // Lấy studentId từ sessionStorage
        const studentId = sessionStorage.getItem("userId");
        if (!studentId) {
          console.error("Student ID không tồn tại trong sessionStorage.");
          return;
        }

        const token = sessionStorage.getItem("jwtToken");

        // Gọi API để lấy dữ liệu điểm của sinh viên
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-student-mark/${studentId}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );

        const { result } = response.data;

        if (result && result.length) {
          // Lọc các bài kiểm tra thuộc loại "Daily" và có điểm > 0
          const dailyExams = result
              .filter(item => item.mark > 0) // Lấy bài kiểm tra có điểm
              .filter(item => item.examResponse.examTypeRate.examCategory === "Daily") // Lọc "Daily Exam"
              .sort((a, b) => a.examResponse.examId - b.examResponse.examId); // Sắp xếp theo examId

          // **Mapping dữ liệu để hiển thị lên biểu đồ**
          this.chartData = {
            labels: dailyExams.map(item => item.examResponse.examTitle), // Tiêu đề bài kiểm tra
            datasets: [
              {
                label: "Mark",
                data: dailyExams.map(item => item.mark), // Điểm bài kiểm tra
                fill: true,
                borderColor: "#AF52DE",
                tension: 0.5,
                pointBackgroundColor: "#AF52DE",
                backgroundColor: "#AF52DE",
              },
            ],
          };

          // Lưu thông tin sinh viên từ API
          const studentInfo = result[0].studentResponse;
          this.student = {
            fullname: studentInfo.userInforResponse.fullName,
            rollNumber: studentInfo.rollNumber,
          };
          this.batch = studentInfo.batchName;
          this.className = studentInfo.classResponse.className;

          // Tính điểm trung bình hiện tại
          this.currentGPA = studentInfo.avgMark;
          this.gradeRemark = this.getGradeRemark(this.currentGPA);

          // Lấy điểm của bài kiểm tra giữa kỳ (Mid-term Exam)
          const midtermExam = result.find(
              item => item.examResponse.examTypeRate.examCategory === "Mid-term Exam"
          );
          this.midtermValue = midtermExam ? midtermExam.mark || 0.0 : null;

          // Lấy điểm của bài kiểm tra cuối kỳ (Final Exam)
          const finalExam = result.find(
              item => item.examResponse.examTypeRate.examCategory === "Final Exam"
          );
          this.finalValue = finalExam ? finalExam.mark || 0.0 : null;

          console.log("Dữ liệu sinh viên:", this.student);
          console.log("Dữ liệu biểu đồ:", this.chartData);
        } else {
          console.warn("Không có dữ liệu điểm nào khả dụng.");
        }
      } catch (error) {
        console.error("Lỗi khi lấy dữ liệu điểm của sinh viên:", error);
      }
    },
    async fetchAllNews() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
          "http://localhost:8088/fja-fap/staff/get-all-publish-news",
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

    navigateToNewsRecord() {
      this.$router.push({ name: "StudentNewsList" });
    },
  },
  mounted() {
    this.fetchAllNews();
    this.fetchSessions();
    this.fetchData();
  },
};
</script>

<style lang="scss">

h2 {
  margin: 10px 0;
}

.activities {
  display: flex;
  flex-direction: row;
  gap: 20px;

  .daily-activities {
    display: flex;
    flex-direction: column;
    width: 33%;
    gap: 10px;
    border-radius: 20px;

    h3 {
      font-weight: bold;
      font-size: 16px;
    }

    .activities-container {
      display: flex;
      flex-direction: column;
      gap: 20px;

      .activity {
        display: flex;
        flex-direction: row;
        gap: 20px;
        color: #1A2C6F;

        b {
          cursor: pointer;
        }

        .thumbnail {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          background-color: #3E5DD4;
          color: #fff;
          padding: 10px;
          gap: 5px;
          width: 80px;
          height: 80px;
          border-radius: 20px;

          #lesson {
            font-size: 16px;
            font-weight: bold;
          }

          #number {
            font-size: 30px;
            font-weight: bold;
          }

          #event {
            font-weight: bold;
            font-size: 24px;
          }
        }

        .information {
          display: flex;
          flex-direction: column;
          justify-content: center;
          font-size: 16px;
          gap: 5px;
        }

      }
    }

  }
}

.row2 {
  display: flex;
  flex-direction: row;
  gap: 20px;

  .chart-container{
    width: 70%;

    canvas{
      width: 100%;
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
</style>