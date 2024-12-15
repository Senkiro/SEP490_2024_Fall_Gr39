<template>
  <div class="container">
    <div class="headContent">
      <h1>Mark Entry Management</h1>
    </div>

    <div class="filters">
      <select v-model="selectedBatch" id="batch-filter" class="filter-select"
              @change="fetchClassesByBatch(selectedBatch.batchName)">
        <option value="" disabled>Select Batch</option>
        <option v-for="batch in batches" :key="batch.id" :value="batch">
          {{ batch.batchName }}
        </option>
      </select>

      <select
          id="class-filter"
          class="filter-select"
          v-model="selectedClassId"
          @change="fetchSessions(selectedClassId)"
      >
        <option value="" disabled>Select Class</option>
        <option v-for="classItem in classes" :key="classItem.classId" :value="classItem.classId">
          {{ classItem.className }}
        </option>
      </select>
    </div>

    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th class="center">No</th>
          <th>Date</th>
          <th>Slot</th>
          <th>Class</th>
          <th>Exam</th>
          <th>Status</th>
          <th class="center">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(session, index) in sessions" :key="session.sessionId">
          <td class="center">{{ index + 1 }}</td>
          <td>{{ session.date }}</td>
          <td>{{ session.timeSlotResponse.name }}</td>
          <td>{{ session.classResponse.className }}</td>
          <td>{{ session.curriculumnResponse.examResponse.examTitle }}</td>
          <td :class="getStatusClass(session)">{{ getStatusText(session) }}</td>
          <td class="center">
            <button
                :class="buttonClass(session)"
                :disabled="isActionDisabled(session)"
                @click="navigateToMarkManagement(session.classResponse.classId, session.curriculumnResponse.examResponse.examId, session.sessionId)"
            >
              {{ getActionText(session) }}
            </button>
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
      batches: [],
      classes: [],
      sessions: [],
      selectedBatch: null,
      selectedClassId: null,
    };
  },
  methods: {
    async fetchBatches() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get("http://localhost:8088/fja-fap/staff/batch", {
          params: { page: 0, size: 1000 },
          headers: { Authorization: `Bearer ${token}` },
        });
        this.batches = response.data.result.content;
      } catch (error) {
        console.error("Error fetching batches:", error);
      }
    },
    async fetchClassesByBatch(batchName) {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const userId = sessionStorage.getItem("userId");

        const teacherClassesResponse = await axios.get(
            `http://localhost:8088/fja-fap/teacher/get-class-teacher/${userId}`,
            { headers: { Authorization: `Bearer ${token}` } }
        );
        const teacherClasses = teacherClassesResponse.data.result;

        const batchClassesResponse = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-class-by-batch`,
            {
              params: { batch_name: batchName, page: 0, size: 100 },
              headers: { Authorization: `Bearer ${token}` },
            }
        );
        const batchClasses = batchClassesResponse.data.result.content;

        this.classes = batchClasses.filter(batchClass =>
            teacherClasses.some(teacherClass => teacherClass.classId === batchClass.classId)
        );
      } catch (error) {
        console.error("Error fetching classes:", error);
        this.classes = [];
      }
    },
    async fetchSessions(classId) {
      try {
        const teacherId = sessionStorage.getItem("userId");
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
            "http://localhost:8088/fja-fap/staff/get-session-have-exam-and-teacher",
            {
              params: { class_id: classId, teacher_id: teacherId },
              headers: { Authorization: `Bearer ${token}` },
            }
        );

        // Chuẩn hóa dữ liệu
        this.sessions = response.data.result.map(session => ({
          ...session,
          classResponse: session.classResponse || {},
          curriculumnResponse: session.curriculumnResponse || { examResponse: {} },
        }));

        // Cập nhật trạng thái cho các session đã qua nhưng chưa được cập nhật
        const today = new Date().toISOString().split("T")[0];
        for (const session of this.sessions) {
          if (session.date <= today && session.markStatus === "Not happen") {
            await this.updateSessionMarkStatus(session.sessionId, "Not added");
            session.markStatus = "Not added";
          }
        }
      } catch (error) {
        console.error("Error fetching sessions:", error);
      }
    },
    async updateSessionMarkStatus(sessionId, newStatus) {
      try {
        const token = sessionStorage.getItem("jwtToken");
        await axios.post(
            `http://localhost:8088/fja-fap/staff/update-session-mark-status/${sessionId}?new_status=${newStatus}`,
            null,
            {
              headers: { Authorization: `Bearer ${token}` },
            }
        );
      } catch (error) {
        console.error("Error updating session status:", error);
      }
    },
    getStatusClass(session) {
      const status = session.markStatus;
      if (status === "Added") return "yes";
      if (status === "Not added") return "no";
      if (status === "Not happen") return "not-happen";
      return "";
    },
    getStatusText(session) {
      if (session.markStatus === "Added") return "Added";
      if (session.markStatus === "Not added") return "Not added";
      return "Not happen";
    },
    isActionDisabled(session) {
      const sessionDate = new Date(session.date); // Ngày của session
      const today = new Date(); // Ngày hiện tại

      // Kiểm tra nếu session chưa diễn ra
      if (sessionDate > today) {
        return true; // Vô hiệu hóa nút
      }

      // Tính toán số ngày giữa ngày hiện tại và ngày của session
      const daysDifference = (today - sessionDate) / (1000 * 60 * 60 * 24);

      // Kiểm tra nếu session đã qua hơn 2 ngày
      if (daysDifference > 2) {
        return true; // Vô hiệu hóa nút
      }

      return false; // Không vô hiệu hóa
    },
    getActionText(session) {
      return session.markStatus === "Added" ? "Edit mark" : "Add mark";
    },
    buttonClass(session) {
      return this.isActionDisabled(session) ? "table-button disabled" : "table-button";
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
  },
  mounted() {
    this.fetchBatches();
  },
};
</script>

<style lang="scss" scoped>
.table-button {
  width: 100px;
  justify-content: center;
}

th:last-child,
td:last-child {
  width: 130px;
}

.filter-select {
  margin-right: 15px;
}
</style>
