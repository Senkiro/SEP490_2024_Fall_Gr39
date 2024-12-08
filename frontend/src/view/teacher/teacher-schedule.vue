<template>
  <div class="container">
    <div class="headContent">
      <h1>Schedule</h1>
    </div>

    <div class="filters">
      <label for="classFilter">Select Class:</label>
      <select id="classFilter" v-model="selectedClassId">
        <option value="" disabled>Class</option>
        <option v-for="classItem in uniqueClasses" :key="classItem.classId" :value="classItem.classId">
          {{ classItem.className }}
        </option>
      </select>
    </div>

    <div class="filters">
      <label for="weekFilter">Select Week:</label>
      <select id="weekFilter" v-model="selectedWeek">
        <option value="" disabled>Week</option>
        <option v-for="(week, index) in uniqueWeeks" :key="index" :value="week.index">
          Week {{ index + 1 }} ({{ week.start }} to {{ week.end }})
        </option>
      </select>
    </div>

    <div v-if="selectedClassId && selectedWeek !== null" class="table-container">
      <table>
        <thead>
        <tr>
          <th class="center">Date</th>
          <th class="center">7:30 - 12:30</th>
          <th class="center">13:30 - 17:30</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(sessions, date) in groupedSessionsByDate" :key="date">
          <td>
            <div class="schedule-date">
              <h1>{{ formatDate(date) }}</h1>
              <p>{{ sessions.morning?.dayOfWeek || sessions.afternoon?.dayOfWeek }}</p>
            </div>
          </td>
          <!-- Cột buổi sáng -->
          <td>
            <div v-if="sessions.morning" class="activities-container">
              <div class="activity">
                <div class="thumbnail">
                  <p id="lesson">Lesson</p>
                  <p id="number">{{ sessions.morning.curriculumnResponse?.lessonResponse?.lessonId || "N/A" }}</p>
                </div>
                <div class="information">
                  <b>{{ sessions.morning.curriculumnResponse?.lessonResponse?.lessonTitle || "N/A" }}</b>
                  <span>Exam: <b>{{ sessions.morning.curriculumnResponse?.examResponse?.examTitle || "N/A" }}</b></span>
                  <span>Class: <b>{{ sessions.morning.classResponse?.className || "N/A" }}</b></span>
                  <span>Room: <b>{{ sessions.morning.roomNumber || "TBD" }}</b></span>
                  <div class="status-button">
                    <button :class="['table-button', isActionDisabled(sessions.morning.date) ? 'disabled' : '']" :disabled="isActionDisabled(sessions.morning.date)" @click="navigateToAttendance(sessions.morning.sessionId)">
                      View attendance
                    </button>
                    <p :class="getStatusAttendClass(sessions.morning.attendanceStatus)">{{ sessions.morning.attendanceStatus}}</p>
                  </div>
                  <div class="status-button" v-if="sessions.morning.curriculumnResponse?.examResponse">
                    <button :class="['table-button', isActionDisabled(sessions.morning.date) ? 'disabled' : '']" :disabled="isActionDisabled(sessions.morning.date)" @click="navigateToMarkManagement(sessions.morning.classResponse.classId, sessions.morning.curriculumnResponse.examResponse.examId, sessions.morning.sessionId)">
                      View mark
                    </button>
                    <p :class="getStatusMarkClass(sessions.morning.markStatus)">{{ sessions.morning.markStatus}}</p>
                  </div>
                </div>
              </div>
            </div>
          </td>
          <!-- Cột buổi chiều -->
          <td>
            <div v-if="sessions.afternoon" class="activities-container">
              <div class="activity">
                <div class="thumbnail">
                  <p id="lesson">Lesson</p>
                  <p id="number">{{ sessions.afternoon.curriculumnResponse?.lessonResponse?.lessonId || "N/A" }}</p>
                </div>
                <div class="information">
                  <b>{{ sessions.afternoon.curriculumnResponse?.lessonResponse?.lessonTitle || "N/A" }}</b>
                  <span>Exam: <b>{{ sessions.afternoon.curriculumnResponse?.examResponse?.examTitle || "N/A" }}</b></span>
                  <span>Class: <b>{{ sessions.afternoon.classResponse?.className || "N/A" }}</b></span>
                  <span>Room: <b>{{ sessions.afternoon.roomNumber || "TBD" }}</b></span>
                  <div class="status-button">
                    <button :class="['table-button', isActionDisabled(sessions.afternoon.date) ? 'disabled' : '']" :disabled="isActionDisabled(sessions.afternoon.date)" @click="navigateToAttendance(sessions.afternoon.sessionId)">
                      View attendance
                    </button>
                    <p :class="getStatusAttendClass(sessions.afternoon.attendanceStatus)">{{ sessions.afternoon.attendanceStatus}}</p>
                  </div>
                  <div class="status-button" v-if="sessions.afternoon.curriculumnResponse?.examResponse">
                    <button :class="['table-button', isActionDisabled(sessions.afternoon.date) ? 'disabled' : '']" :disabled="isActionDisabled(sessions.afternoon.date)" @click="navigateToMarkManagement(sessions.afternoon.classResponse.classId, sessions.afternoon.curriculumnResponse.examResponse.examId, sessions.afternoon.sessionId)">
                      View mark
                    </button>
                    <p :class="getStatusMarkClass(sessions.afternoon.markStatus)">{{ sessions.afternoon.markStatus}}</p>
                  </div>
                </div>
              </div>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      sessions: [],
      selectedClassId: "",
      selectedWeek: null,
    };
  },
  computed: {
    uniqueClasses() {
      const classMap = new Map();
      this.sessions.forEach((item) => {
        if (!classMap.has(item.classResponse.classId)) {
          classMap.set(item.classResponse.classId, {
            classId: item.classResponse.classId,
            className: item.classResponse.className.trim(),
          });
        }
      });
      return Array.from(classMap.values());
    },
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
      // Lọc theo lớp và tuần
      return this.sessions.filter(
          (session) =>
              (!this.selectedClassId || session.classResponse.classId === this.selectedClassId) &&
              (this.selectedWeek === null || session.sessionWeek === this.selectedWeek)
      );
    },
    groupedSessionsByDate() {
      const grouped = {};
      this.filteredSessions.forEach((session) => {
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
      });
      return grouped;
    },
  },
  methods: {
    isActionDisabled(sessionDate) {
      if (!sessionDate) return true;

      const sessionDateObj = new Date(sessionDate);
      const today = new Date();

      today.setHours(0, 0, 0, 0);
      sessionDateObj.setHours(0, 0, 0, 0);

      const diffInDays = (sessionDateObj - today) / (1000 * 60 * 60 * 24);

      if (diffInDays < -2 || diffInDays > 0) {
        return true;
      }

      return false;
    },
    navigateToMarkManagement(classId, examId, sessionId) {
      if (!classId || !examId) {
        console.error("Missing required parameters:", { classId, examId, sessionId });
        return;
      }

      this.$router.push({
        name: "TeacherMarkManagement",
        params: { classId, examId, sessionId },
      });
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
    getStatusAttendClass(status) {
      if (status === "Attended") return "yes";
      if (status === "Not happen") return "not-happen";
      if (status === "Not taken") return "no";
      return "";
    },
    getStatusMarkClass(status) {
      if (status === "Added") return "yes";
      if (status === "Not added") return "no";
      if (status === "Not happen") return "not-happen";
      return "";
    },
    formatDate(date) {
      if (!date) return "N/A";
      const options = { day: "numeric" };
      return new Date(date).toLocaleDateString(undefined, options);
    },
    getWeekStartDate(date) {
      const d = new Date(date);
      d.setDate(d.getDate() - d.getDay() + 1); // Bắt đầu tuần là Thứ Hai
      return d.toLocaleDateString("en-US", { day: "numeric", month: "long", year: "numeric" });
    },
    getWeekEndDate(date) {
      const d = new Date(date);
      d.setDate(d.getDate() - d.getDay() + 7); // Kết thúc tuần là Chủ Nhật
      return d.toLocaleDateString("en-US", { day: "numeric", month: "long", year: "numeric" });
    },
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
  },
  mounted() {
    this.fetchSessions();
  },
};
</script>

<style lang="scss" scoped>
.table-container {
  table {
    th {
      text-align: center;
    }

    tr,
    td {
      border: 2px solid #DFE7FB;

      tr:nth-child(2),
      tr:nth-child(3),
      td:nth-child(2),
      td:nth-child(3) {
        width: 45%;
        font-weight: normal;
      }
    }

    td {
      padding: 30px 0;
      color: #171717;
    }
  }
}
</style>
