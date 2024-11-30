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
          <th class="center">8:30 - 12:30</th>
          <th class="center">13:30 - 17:30</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="item in filteredSessions" :key="item.sessionId">
          <td>
            <div class="schedule-date">
              <h1>{{ formatDate(item.date) }}</h1>
              <p>{{ item.dayOfWeek }}</p>
            </div>
          </td>
          <!-- Cột buổi sáng -->
          <td>
            <div v-if="item.timeSlotResponse?.name === 'Morning'" class="activities-container">
              <div class="activity">
                <div class="thumbnail">
                  <p id="lesson">Lesson</p>
                  <p id="number">{{ item.curriculumnResponse?.lessonResponse?.lessonId || "N/A" }}</p>
                </div>
                <div class="information">
                  <b>{{ item.curriculumnResponse?.lessonResponse?.lessonTitle || "N/A" }}</b>
                  <span>Exam: <b>{{ item.curriculumnResponse?.examResponse?.examTitle || "N/A" }}</b></span>
                  <span>Class: <b>{{ item.classResponse?.className || "N/A" }}</b></span>
                  <span>Room: <b>{{ item.roomNumber || "TBD" }}</b></span>
                  <div class="status-button">
                    <button class="table-button">View attendance</button>
                    <p class="attended">Attendance taken</p>
                  </div>
                  <div class="status-button">
                    <button class="table-button">View mark</button>
                    <p class="attended">Mark added</p>
                  </div>
                </div>
              </div>
            </div>
          </td>
          <!-- Cột buổi chiều -->
          <td>
            <div v-if="item.timeSlotResponse?.name === 'Afternoon'" class="activities-container">
              <div class="activity">
                <div class="thumbnail">
                  <p id="lesson">Lesson</p>
                  <p id="number">{{ item.curriculumnResponse?.lessonResponse?.lessonId || "N/A" }}</p>
                </div>
                <div class="information">
                  <b>{{ item.curriculumnResponse?.lessonResponse?.lessonTitle || "N/A" }}</b>
                  <span>Exam: <b>{{ item.curriculumnResponse?.examResponse?.examTitle || "N/A" }}</b></span>
                  <span>Class: <b>{{ item.classResponse?.className || "N/A" }}</b></span>
                  <span>Room: <b>{{ item.roomNumber || "TBD" }}</b></span>
                  <div class="status-button">
                    <button class="table-button">View attendance</button>
                    <p class="attended">Attendance taken</p>
                  </div>
                  <div class="status-button">
                    <button class="table-button">View mark</button>
                    <p class="attended">Mark added</p>
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
  },
  methods: {
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
