<template>
  <div class="container">
    <h2>Upcoming activities</h2>
    <!-- Schedule -->
    <div class="activities horizontal-activities">
      <div v-for="(sessions, date) in groupedSessionsByDate" :key="date" class="daily-activities">

        <h3>{{new Date(date).toLocaleDateString() }}</h3>

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
                <span>Class: <b>{{ sessions.morning.classResponse.className || 'Unknown' }}</b></span>
                <span>Room: <b>{{ sessions.morning.roomNumber || 'Unknown' }}</b></span>
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
                <span>Class: <b>{{  sessions.afternoon.classResponse.className  || 'Unknown' }}</b></span>
                <span>Room: <b>{{ sessions.afternoon.roomNumber || 'Unknown' }}</b></span>
              </div>
            </template>
          </div>
        </div>
      </div>
    </div>

    <!-- News Section -->
    <div class="row2">
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
export default {
  data() {
    return {
      newsList: [], // Danh sách tin tức
      sessions: [], // Tất cả hoạt động (bài học và sự kiện)
    }
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
      const teacherId = sessionStorage.getItem("userId");
      const token = sessionStorage.getItem("jwtToken");

      if (!teacherId || !token) {
        console.error("Teacher ID hoặc JWT token không tồn tại trong session storage");
        return;
      }

      axios
        .get(`http://localhost:8088/fja-fap/teacher/get-session-by-teacher?teacher_id=${teacherId}`, {
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
    this.fetchSessions();
    this.fetchAllNews();
  },
}
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

  .chart-container {
    width: 70%;

    canvas {
      width: 100%;
    }
  }

  #news-container {
    display: flex;
    flex-direction: column;
    width: 100%;
    padding: 30px;
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