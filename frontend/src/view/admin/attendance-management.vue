<template>
  <div class="container">
    <div class="headContent">
      <h1>Attendance management</h1>
    </div>

    <div class="filters">
      <label for="week-filter">Select Week:</label>
      <select id="week-filter" class="filter-select" v-model="selectedWeek" v-if="uniqueWeeks.length > 0">
        <option value="" disabled>Select Week</option>
        <option value="all">All Week</option>
        <option v-for="(week, index) in uniqueWeeks" :key="index" :value="week.index">
          Week {{ index + 1 }} ({{ week.start }} to {{ week.end }})
        </option>
      </select>
      <p v-else>No weeks available</p>
    </div>


    <div v-if="filteredSessions.length > 0" class="table-container">
      <table>
        <thead>
        <tr>
          <th class="center">No</th>
          <th>Date</th>
          <th>Slot</th>
          <th>Class</th>
          <th>Lesson/Event</th>
          <th>Status</th>
          <th class="center">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item, index) in filteredSessions" :key="item.sessionId">
          <td class="center">{{ index + 1 }}</td>
          <td>{{ formatDate(item.date) }}</td>
          <td>{{ item.timeSlotResponse?.name || "N/A" }}</td>
          <td>{{ item.classResponse?.className || "N/A" }}</td>
          <td>{{ item.curriculumnResponse?.lessonResponse?.lessonTitle || "N/A" }}</td>
          <td :class="getStatusClass(item)">{{ getStatusText(item) }}</td>
          <td class="center">
            <button
                :class="['table-button', isActionDisabled(item) ? 'disabled' : '']"
                :disabled="isActionDisabled(item)"
                @click="navigateToAttendance(item.sessionId)"
            >
              {{ getActionText(item) }}
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div v-else-if="selectedWeek" class="no-data">
      <p>No attendance records available.</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      sessions: [],
      selectedWeek: "all",
    };
  },
  computed: {
    uniqueWeeks() {
      if (!Array.isArray(this.sessions)) return [];
      const weeks = [];
      const weekMap = new Map();

      this.sessions.forEach((session) => {
        if (!weekMap.has(session.sessionWeek)) {
          const startDate = this.getWeekStartDate(session.date);
          const endDate = this.getWeekEndDate(session.date);

          weekMap.set(session.sessionWeek, {
            index: session.sessionWeek,
            start: startDate,
            end: endDate,
          });
        }
      });

      weekMap.forEach((value) => weeks.push(value));
      return weeks;
    },
    filteredSessions() {
      return this.sessions.filter(session => {
        const hasValidLesson = session.curriculumnResponse?.lessonResponse?.lessonTitle;
        const isWeekMatched =
            this.selectedWeek === "all" ||
            this.selectedWeek === null ||
            session.sessionWeek === this.selectedWeek;
        return hasValidLesson && isWeekMatched;
      });
    }
  },
  methods: {
    formatDate(date) {
      if (!date) return "N/A";
      const options = { day: "numeric", month: "long", year: "numeric" };
      return new Date(date).toLocaleDateString("en-US", options);
    },
    getWeekStartDate(date) {
      const d = new Date(date);
      d.setDate(d.getDate() - d.getDay() + 1); // Bắt đầu tuần là Thứ Hai
      return d.toLocaleDateString("en-US", { day: "numeric", month: "short", year: "numeric" });
    },
    getWeekEndDate(date) {
      const d = new Date(date);
      d.setDate(d.getDate() - d.getDay() + 7); // Kết thúc tuần là Chủ Nhật
      return d.toLocaleDateString("en-US", { day: "numeric", month: "short", year: "numeric" });
    },
    fetchSessions() {
      return new Promise((resolve, reject) => {
        const teacherId = "e7a14939-c770-4782-9046-0aac28df341e";
        const token = sessionStorage.getItem("jwtToken");

        if (!teacherId || !token) {
          console.error("Token missing");
          reject("Token missing");
          return;
        }

        axios
            .get(`http://localhost:8088/fja-fap/teacher/get-session-by-teacher?teacher_id=${teacherId}`, {
              headers: { Authorization: `Bearer ${token}` },
            })
            .then((response) => {
              if (response.data.code === 0 && Array.isArray(response.data.result)) {
                this.sessions = response.data.result;
                resolve(); // Thành công
              } else {
                console.error(
                    "Lỗi khi fetch dữ liệu sessions:",
                    response.data.message || "Lỗi không xác định"
                );
                this.sessions = [];
                reject("Error fetching sessions");
              }
            })
            .catch((error) => {
              console.error("Lỗi khi gọi API:", error);
              this.sessions = [];
              reject(error); // Trả lỗi
            });
      });
    },
    getStatusClass(item) {
      const status = item.attendanceStatus;
      if (status === "Attended") return "yes";
      if (status === "Not happen") return "not-happen";
      if (status === "Not taken") return "no";
      return "";
    },
    getStatusText(item) {
      return item.attendanceStatus;
    },
    isActionDisabled(item) {
      const sessionDate = new Date(item.date);
      const today = new Date();

      // Nếu session đã qua 2 ngày hoặc chưa diễn ra, nút bị vô hiệu hóa
      if (sessionDate > today || (today - sessionDate) / (1000 * 60 * 60 * 24) > 2) {
        return false;
      }

      return false; // Có thể nhấn nút
    },
    getActionText(item) {
      if (item.attendanceStatus === 'Attended') {
        return "Edit attendance";
      }

      return "Take attendance";
    },
    navigateToAttendance(sessionId) {
      if (!sessionId) {
        return;
      }

      this.$router.push({
        path: `/teacher/take-attendance/${sessionId}`,
      }).then(() => {
        const session = this.sessions.find(item => item.sessionId === sessionId);
        if (session) {
          session.status = true;
        }
      });
    },
    checkAndUpdateStatus() {
      const today = new Date();
      const todayFormatted = today.toISOString().split("T")[0];
      this.sessions.forEach(session => {
        const sessionDate = new Date(session.date).toISOString().split("T")[0];
        if (session.attendanceStatus !== "Attended"  && sessionDate === todayFormatted ) {
          axios.post(
              `http://localhost:8088/fja-fap/staff/update-session-attendance-status/${session.sessionId}?new_status=Not taken`,
              {},
              {
                headers: {
                  Authorization: `Bearer ${sessionStorage.getItem("jwtToken")}`
                }
              }
          )
              .then(response => {
                if (response.status === 200) {
                  console.log(`Session ${session.sessionId} updated to 'Not taken'`);
                  session.attendanceStatus = "Not taken";
                } else {
                  console.error(`Failed to update session ${session.sessionId}:`, response.data);
                }
              })
              .catch(error => {
                console.error(`Failed to update session ${session.sessionId}:`, error);
              });
        }
      });
    }
  },
  async mounted() {
    try {
      await this.fetchSessions(); // Đợi fetchSessions hoàn thành
      this.checkAndUpdateStatus(); // Chỉ gọi khi fetchSessions đã xong
    } catch (error) {
      console.error("Error during mounted:", error);
    }
  }
};
</script>

<style lang="scss" scoped>
.table-button{
  width: 150px;
  justify-content: center;
}

th:last-child,td:last-child{
  width: 180px;
}
</style>