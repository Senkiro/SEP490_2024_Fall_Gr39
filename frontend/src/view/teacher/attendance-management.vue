<template>
  <div class="container">
    <div class="headContent">
      <h1>Attendance management</h1>
    </div>

    <div class="filters">
      <label for="class-filter">Select Class:</label>
      <select id="class-filter" class="filter-select" v-model="selectedClassId">
        <option value="" disabled>Select Class</option>
        <option v-for="classItem in uniqueClasses" :key="classItem.classId" :value="classItem.classId">
          {{ classItem.className }}
        </option>
      </select>
    </div>

    <div class="filters">
      <label for="week-filter">Select Week:</label>
      <select id="week-filter" class="filter-select" v-model="selectedWeek">
        <option value="" disabled>Select Week</option>
        <option v-for="(week, index) in uniqueWeeks" :key="index" :value="week.index">
          Week {{ index + 1 }} ({{ week.start }} to {{ week.end }})
        </option>
      </select>
    </div>

    <div v-if="selectedClassId && filteredSessions.length > 0" class="table-container">
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
    <div v-else-if="selectedClassId" class="no-data">
      <p>No attendance records available for the selected filters.</p>
    </div>
    <div v-else class="no-data">
      <p>Please select a class to view attendance records.</p>
    </div>

  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      sessions: [], // Danh sách các phiên học
      selectedClassId: "", // Lớp học được chọn
      selectedWeek: null, // Tuần được chọn
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
        console.error("Teacher ID hoặc JWT token không tồn tại trong session storage");
        return;
      }

      axios
          .get(`http://localhost:8088/fja-fap/teacher/get-session-by-teacher?teacher_id=${teacherId}`, {
            headers: { Authorization: `Bearer ${token}` },
          })
          .then((response) => {
            if (response.data.code === 0) {
              this.sessions = response.data.result; // Lưu toàn bộ dữ liệu
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

      if (!item.timeSlotResponse || item.timeSlotResponse.name === "N/A") {
        return true;
      }

      // Nếu session chưa diễn ra hoặc cách ngày hôm nay hơn 2 ngày, nút bị vô hiệu hóa
      if (sessionDate > today || (today - sessionDate) / (1000 * 60 * 60 * 24) > 2) {
        return true;
      }

      // Nếu đã điểm danh thì nút cũng bị vô hiệu hóa
      return item.status === true;
    },
    getActionText(item) {
      if (item.status) {
        return "Edit attendance";
      }

      return "Take attendance";
    },
    navigateToAttendance(sessionId) {
      if (!sessionId) {
        console.error("Session ID không hợp lệ.");
        return;
      }

      this.$router.push({
        path: `/teacher/take-attendance/${sessionId}`,
      });
    },
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