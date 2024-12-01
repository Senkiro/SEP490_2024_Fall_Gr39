<template>
  <div class="container">
    <div class="headContent">
      <h1>Attendance management</h1>
    </div>

    <div class="filters">
      <label for="week-filter">Select Week:</label>
      <select id="week-filter" class="filter-select" v-model="selectedWeek">
        <option value="" disabled>Select Week</option>
        <option value="all">All Week</option>
        <option v-for="(week, index) in uniqueWeeks" :key="index" :value="week.index">
          Week {{ index + 1 }} ({{ week.start }} to {{ week.end }})
        </option>
      </select>
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
      const teacherId = sessionStorage.getItem("userId");
      const token = sessionStorage.getItem("jwtToken");

      if (!teacherId || !token) {
        console.error("Token missing");
        return;
      }

      axios
          .get(`http://localhost:8088/fja-fap/teacher/get-session-by-teacher?teacher_id=${teacherId}`, {
            headers: { Authorization: `Bearer ${token}` },
          })
          .then((response) => {
            if (response.data.code === 0) {
              this.sessions = response.data.result;
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
    getStatusClass(item) {
      if (item.status) return "yes";
      const sessionDate = new Date(item.date);
      const today = new Date();
      if (sessionDate > today) return "not-happen";
      return "no";
    },
    getStatusText(item) {
      if (item.status) return "Taken";
      const sessionDate = new Date(item.date);
      const today = new Date();
      if (sessionDate > today) return "Not happen";
      return "Not taken";
    },
    isActionDisabled(item) {
      const sessionDate = new Date(item.date);
      const today = new Date();

      if (!item.lessonResponse || item.lessonResponse.lessonTitle === "N/A") {
        return true;
      }

      //Nếu session chưa diễn ra hoặc cách ngày hôm nay hơn 2 ngày, nút bị vô hiệu hóa
      if (sessionDate > today || (today - sessionDate) / (1000 * 60 * 60 * 24) > 2) {
        return true;
      }

    },
    getActionText(item) {
      if (item.status) {
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
    }

  },
  mounted() {
    this.fetchSessions();
  },
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