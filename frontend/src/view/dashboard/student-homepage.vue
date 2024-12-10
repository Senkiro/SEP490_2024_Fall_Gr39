<template>
  <div class="container">
    <h2>Upcoming activities</h2>
    <!-- Schedule -->
    <div class="activities horizontal-activities">
      <div
          v-for="lesson in lessons"
          :key="lesson.date + lesson.curriculumnResponse?.lessonResponse?.lessonId"
          class="daily-activities"
      >
        <h3>{{ lesson.date === today ? 'Tomorrow' : new Date(lesson.date).toLocaleDateString() }}</h3>
        <div class="activities-container">
          <div class="activity">
            <div class="thumbnail">
              <p id="lesson">Lesson</p>
              <p id="number">{{ lesson.curriculumnResponse?.lessonResponse?.lessonId || 'N/A' }}</p>
            </div>
            <div class="information">
              <p>{{ lesson.curriculumnResponse?.lessonResponse?.lessonTitle || 'No Title' }}</p>
              <span>Exam: <b>{{ lesson.curriculumnResponse?.examResponse?.examTitle || 'N/A' }}</b></span>
              <span>Teacher: <b>{{ lesson.fullName || 'Unknown' }}</b></span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Event  -->
    <div class="activities-container">
      <div
          v-for="event in events"
          :key="'event-' + event.eventId"
          class="event"
      >
        <div class="activity">
          <div class="thumbnail">
            <p id="lesson">Event</p>
          </div>
          <div class="information">
            <b>{{ event.eventName || 'No Event Name' }}</b>
            <span>Destination: <b>{{ event.destination || 'N/A' }}</b></span>
          </div>
        </div>
      </div>
    </div>

    <!-- News Section -->
    <div class="row2">
      <div class="graph"></div>
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
    };
  },
  computed: {
    lessons() {
      return this.sessions.filter(
          (session) =>
              !session.eventId && // Không phải sự kiện
              session.curriculumnResponse?.lessonResponse?.lessonTitle // Kiểm tra có dữ liệu bài học
      );
    },
    events() {
      return this.sessions.filter((session) => session.eventId);
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
                    }

                    #number {
                        font-size: 30px;
                        font-weight: bold;
                    }
                }

                .information {
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                }

            }
        }

    }
}

.row2 {
    display: flex;
    flex-direction: row;
    gap: 20px;

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