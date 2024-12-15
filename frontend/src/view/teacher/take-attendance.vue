<template>
  <div class="container">
    <div class="headContent">
      <h1>Take attendance</h1>
    </div>

    <!-- Thông Báo -->
    <div v-if="notification" class="notification" :class="notificationType">
      {{ notification }}
    </div>

    <div v-if="students.length > 0" class="table-container">
      <form @submit.prevent="saveAllAttendances">
        <table>
          <thead>
          <tr>
            <th class="center">No</th>
            <th>Japanese name</th>
            <th>Roll number</th>
            <th class="center">Image</th>
            <th class="center">Status</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(student, index) in students" :key="student.attendanceId">
            <td class="center">{{ index + 1 }}</td>
            <td>{{ student.studentName }}</td>
            <td>{{ student.rollNumber }}</td>
            <td class="center">
              <img :src="student.image || defaultAvatar" alt="Student Image" />
            </td>
            <td class="center">
              <div class="radio-group">
                <div class="radio">
                  <input
                      type="radio"
                      :id="'attended-' + student.attendanceId"
                      :value="'Attended'"
                      v-model="student.status"
                  />
                  <label :for="'attended-' + student.attendanceId">Attended</label>
                </div>
                <div class="radio">
                  <input
                      type="radio"
                      :id="'absent-' + student.attendanceId"
                      :value="'Absent'"
                      v-model="student.status"
                  />
                  <label :for="'absent-' + student.attendanceId">Absent</label>
                </div>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
        <div class="actions">
          <button type="submit" class="table-button">
            Save Attendance
          </button>
        </div>
      </form>
    </div>

    <div v-else>
      <p>No students found for this session.</p>
    </div>
  </div>
</template>


<script>
import axios from "axios";
import defaultAvatar from "@/assets/smiling-young-man-illustration_1308-174669.avif";

export default {
  data() {
    return {
      students: [],
      sessionId: this.$route.params.sessionId,
      notification: null, // Thông báo
    };
  },
  computed: {
    defaultAvatar() {
      return defaultAvatar;
    },
  },
  methods: {
    fetchAttendanceData() {
      const token = sessionStorage.getItem("jwtToken");

      if (!this.sessionId || !token) {
        console.error("Session ID hoặc JWT token không tồn tại");
        return;
      }

      axios
          .get(
              `http://localhost:8088/fja-fap/staff/get-attendance-session/${this.sessionId}?page=0&size=20`,
              {
                headers: { Authorization: `Bearer ${token}` },
              }
          )
          .then((response) => {
            if (response.data.code === 0) {
              const results = response.data.result.content;
              this.students = results.map((student) => ({
                attendanceId: student.attendanceId,
                studentName: student.studentResponse?.userInforResponse?.fullName || "N/A",
                rollNumber: student.studentResponse?.rollNumber || "N/A",
                image: student.studentResponse?.userInforResponse?.profilePicture || defaultAvatar,
                status: student.status || "",
                classId: student.studentResponse?.classResponse?.classId || null,
              }));
            } else {
              console.error(
                  "Error fetching students:",
                  response.data.message || "Unknown error"
              );
            }
          })
          .catch((error) => {
            console.error("Error fetching students API:", error);
          });
    },

    // Lưu điểm danh cho tất cả
    saveAllAttendances() {
      const token = sessionStorage.getItem("jwtToken");
      if (!token) {
        console.error("JWT token không tồn tại trong session storage");
        return;
      }

      // Kiểm tra học sinh chưa được điểm danh
      const unmarkedStudents = this.students.filter(
          (student) => student.status === "Not happen"
      );

      if (unmarkedStudents.length > 0) {
        this.showNotification(
            `There are ${unmarkedStudents.length} student(s) not marked attendance. Please check again.`,
            "error"
        );
        return;
      }

      const savePromises = this.students.map((student) => {
        const payload = {
          status: student.status,
          note: "",
          sessionId: this.sessionId,
          studentId: student.studentId,
        };

        return axios
            .post(
                `http://localhost:8088/fja-fap/staff/update-attendance/${student.attendanceId}`,
                payload,
                {
                  headers: { Authorization: `Bearer ${token}` },
                }
            )
            .then((response) => {
              if (response.data.code === 0) {
                return true;
              } else {
                console.error(
                    `Error updating attendance for ${student.studentName}:`,
                    response.data.message || "Unknown error"
                );
                return false;
              }
            })
            .catch((error) => {
              console.error(`Error updating attendance for ${student.studentName}:`, error);
              return false;
            });
      });

      // Chờ tất cả Promise hoàn thành
      Promise.all(savePromises).then((results) => {
        const successCount = results.filter((result) => result).length;

        if (successCount === this.students.length) {
          this.updateSessionStatus();
          this.showNotification("All attendances saved successfully!", "success");

        } else {
          this.showNotification(
              `${successCount}/${this.students.length} attendances saved successfully.`
          );
        }
      });
    },

    updateSessionStatus() {
      const token = sessionStorage.getItem("jwtToken");
      const classId = this.students[0]?.classId;
      const payload = {
        status: true,
        classId: classId,
      };
      axios
          .post(`http://localhost:8088/fja-fap/staff/update-session-attendance-status/${this.sessionId}?new_status=Attended`, payload, {
            headers: { Authorization: `Bearer ${token}` },
          })
          .then((response) => {
            if (response.data.code === 0) {
              console.log("Session status updated successfully.");
              return true;
            } else {
              console.error("Failed to update session status:", response.data.message);
              return false;
            }
          })
          .catch((error) => {
            console.error("Error updating session status:", error);
            return false;
          });
    },

    showNotification(message) {
      this.notification = message;

      setTimeout(() => {
        this.notification = null;
      }, 3000);
    },
  },
  mounted() {
    this.fetchAttendanceData();
  },
};
</script>

<style lang="scss" scoped>
img {
    width: 150px;
    border-radius: 20px;
}

th:last-child,
td:last-child {
    width: 10%;
}

.radio-group {
    display: flex;
    flex-direction: column;
    gap: 20px;
    justify-content: center;

    .radio {
        display: flex;
        width: fit-content;
        flex-direction: row;
        gap: 10px;
    }
}

.notification {
  padding: 15px;
  margin: 20px 0;
  border-radius: 5px;
  text-align: center;
  font-size: 16px;
  background-color: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

</style>