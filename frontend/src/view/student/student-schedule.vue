<template>
  <div class="container">
    <div class="headContent">
      <h1>Schedule</h1>
    </div>

    <div class="filters">
      <label for="weekFilter">Select Week:</label>
      <select id="weekFilter" v-model="selectedWeek">
        <option value="" disabled>Week</option>
        <option
            v-for="(week, index) in uniqueWeeks"
            :key="index"
            :value="week.index"
        >
          Week {{ index + 1 }} ({{ week.start }} to {{ week.end }})
        </option>
      </select>
    </div>

    <div class="table-container">
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
          <!-- Cột ngày -->
          <td>
            <div class="schedule-date">
              <h1>{{ formatDate(date) }}</h1>
              <p>{{ sessions.morning?.dayOfWeek || sessions.afternoon?.dayOfWeek }}</p>
            </div>
          </td>
          <td v-if="sessions.note" colspan="2">
            <div class="note-content" style="text-align: center">
              {{ sessions.note }}
            </div>
          </td>
          <template v-else>
          <!-- Morning -->
          <td>
            <div v-if="sessions.morning">
              <div v-if="sessions.morning.eventId" class="activities-container">
                <div class="activity">
                  <div class="thumbnail">
                    <p id="lesson">Event</p>
                  </div>
                  <div class="information">
                    <b>{{ sessions.morning.eventName || "N/A" }}</b>
                    <span>Destination: <b>{{ sessions.morning.eventResponse.address}}</b></span>
                    <b :class="getStatusAttendClass(sessions.morning.attendanceStatus)">{{ sessions.morning.attendanceStatus}}</b>
                  </div>
                </div>
              </div>
              <div v-else-if="sessions.morning.curriculumnResponse" class="activities-container">
                <div class="activity">
                  <div class="thumbnail">
                    <p id="lesson">Lesson</p>
                    <p id="number">
                      {{ sessions.morning.curriculumnResponse?.lessonResponse?.lessonId || "N/A" }}
                    </p>
                  </div>
                  <div class="information">
                    <b>{{ sessions.morning.curriculumnResponse?.lessonResponse?.lessonTitle || "N/A" }}</b>
                    <span>Exam: <b>{{ sessions.morning.curriculumnResponse?.examResponse?.examTitle || "N/A" }}</b></span>
                    <span>Teacher: <b>{{ sessions.morning.fullName || "TBD" }}</b></span>
                    <span>Room: <b>{{ sessions.morning.roomNumber || "TBD" }}</b></span>
                    <b :class="getStatusAttendClass(sessions.morning.attendanceStatus)">{{ sessions.morning.attendanceStatus}}</b>
                  </div>
                </div>
              </div>
            </div>
          </td>

          <!-- Afternoon -->
          <td>
            <div v-if="sessions.afternoon">
              <div v-if="sessions.afternoon.eventId" class="activities-container">
                <div class="activity">
                  <div class="thumbnail">
                    <p id="lesson">Event</p>
                  </div>
                  <div class="information">
                    <b>{{ sessions.afternoon.eventName || "N/A" }}</b>
                    <span>Destination: <b>{{ sessions.afternoon.eventResponse.address}}</b></span>
                    <b :class="getStatusAttendClass(sessions.afternoon.attendanceStatus)">{{ sessions.afternoon.attendanceStatus}}</b>
                  </div>
                </div>
              </div>
              <div v-else-if="sessions.afternoon.curriculumnResponse" class="activities-container">
                <div class="activity">
                  <div class="thumbnail">
                    <p id="lesson">Lesson</p>
                    <p id="number">
                      {{ sessions.afternoon.curriculumnResponse?.lessonResponse?.lessonId || "N/A" }}
                    </p>
                  </div>
                  <div class="information">
                    <b>{{ sessions.afternoon.curriculumnResponse?.lessonResponse?.lessonTitle || "N/A" }}</b>
                    <span>Exam: <b>{{ sessions.afternoon.curriculumnResponse?.examResponse?.examTitle || "N/A" }}</b></span>
                    <span>Teacher: <b>{{ sessions.afternoon.fullName || "TBD" }}</b></span>
                    <span>Room: <b>{{ sessions.afternoon.roomNumber || "TBD" }}</b></span>
                    <b :class="getStatusAttendClass(sessions.afternoon.attendanceStatus)">{{ sessions.afternoon.attendanceStatus}}</b>
                  </div>
                </div>
              </div>
            </div>
          </td>
          </template>
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
      selectedWeek: 0,
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
    // Group theo ngày
    groupedSessionsByDate() {
      const grouped = {};
      const filteredSessions = this.sessions.filter(
          (session) => this.selectedWeek === null || session.sessionWeek === this.selectedWeek
      );

      filteredSessions.forEach((session) => {
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
    getStatusAttendClass(status) {
      if (status === "Attended") return "yes";
      if (status === "Not happen") return "not-happen";
      if (status === "Not taken") return "no";
      return "";
    },
    // Gọi API để lấy sessions
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
    // Định dạng ngày
    formatDate(date) {
      if (!date) return "N/A";
      const options = { day: "numeric" };
      return new Date(date).toLocaleDateString(undefined, options);
    },
    getWeekStartDate(date) {
      const d = new Date(date);
      d.setDate(d.getDate() - d.getDay() + 1); // Thứ Hai
      return d.toLocaleDateString("en-US", { day: "numeric", month: "long", year: "numeric" });
    },
    getWeekEndDate(date) {
      const d = new Date(date);
      d.setDate(d.getDate() - d.getDay() + 7); // Chủ Nhật
      return d.toLocaleDateString("en-US", { day: "numeric", month: "long", year: "numeric" });
    },
  },
  mounted() {
    this.fetchSessions(); // Gọi API khi component được mount
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

            .schedule-date {
                display: flex;
                flex-direction: column;
                align-items: center;
                gap: 10px;

                h1 {
                    margin: 0;
                }
            }
        }
    }
}
</style>