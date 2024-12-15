<template>
  <div class="container">
    <div class="headContent">
      <h1>Manage Mark</h1>
    </div>
    <p>Exam: <b>{{ examTitle }}</b></p>

    <div class="actions">
      <p>Class: <b>{{ className }}</b></p>
    </div>

    <div v-if="notification" class="notification" :class="notificationType">
      {{ notification }}
    </div>

    <div class="table-container">
      <form>
        <table>
          <thead>
          <tr>
            <th class="center">No</th>
            <th>Japanese Name</th>
            <th>Roll Number</th>
            <th class="center">Mark</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(mark, index) in marks" :key="mark.markId">
            <td class="center">{{ index + 1 }}</td>
            <td>{{ mark.studentResponse.userInforResponse.japaneseName }}</td>
            <td>{{ mark.studentResponse.rollNumber }}</td>
            <td class="center">
              <input
                  type="number"
                  v-model="mark.mark"
                  min="0"
                  max="10"
                  @input="validateMark(mark)"
              />
            </td>
          </tr>
          </tbody>
        </table>
        <div class="actions">
          <button type="button" @click="saveMarks">
            <VsxIcon iconName="Save2" size="20" type="bold" />
            Save
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      marks: [],
      examTitle: "",
      className: "",
      examDate: "",
      examSlot: "",
      notification: "",
      notificationType: "",
      sessionId: this.$route.params.sessionId,
    };
  },
  methods: {
    async fetchMarks() {
      try {
        const exam_id = this.$route.params.examId;
        const class_id = this.$route.params.classId;

        const response = await axios.get(
            "http://localhost:8088/fja-fap/staff/get-mark-by-session-exam",
            {
              params: { exam_id, class_id },
              headers: {
                Authorization: `Bearer ${sessionStorage.getItem("jwtToken")}`,
              },
            }
        );

        this.marks = response.data.result;

        if (this.marks.length > 0) {
          const firstMark = this.marks[0];
          this.examTitle = firstMark.examResponse.examTitle;
          this.className = firstMark.studentResponse.classResponse.className;
          // this.examDate = new Date(firstMark.examResponse.examContent).toLocaleDateString("en-US");
          // this.examSlot = "8:30 - 12:30";
        }

        console.log("Marks fetched:", this.marks);
      } catch (error) {
        console.error("Error fetching marks:", error);
      }
    },
    async saveMarks() {
      try {
        const token = sessionStorage.getItem("jwtToken");

        // Lặp qua từng đối tượng marks và cập nhật
        for (const mark of this.marks) {
          const payload = {
            mark: mark.mark,
            comment: "",
            status: 0,
            updatedBy: sessionStorage.getItem("userName"),
          };

          const response = await axios.post(
              `http://localhost:8088/fja-fap/staff/update-mark/${mark.markId}`,
              payload,
              {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
              }
          );

          if (response.data.code !== 0) {
            console.error(`Failed to update mark for ID ${mark.markId}:`, response.data.message);
            alert(`Failed to update mark for ID ${mark.markId}`);
            return;
          }
        }


        await this.updateSessionMarkStatus(this.sessionId, "Added");

      } catch (error) {
        console.error("Error updating marks:", error);
        this.showNotification("Error occurred while updating marks.", "error");
      }
    },

    async updateSessionMarkStatus(sessionId, newStatus) {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.post(
            `http://localhost:8088/fja-fap/staff/update-session-mark-status/${sessionId}`,
            null,
            {
              params: { new_status: newStatus },
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );

        if (response.data.code === 0) {
          this.showNotification("All marks updated successfully and session status updated!", "success");
          console.log(`Session ${sessionId} status updated to ${newStatus}`);
        } else {
          console.error(`Failed to update session status: ${response.data.message}`);
        }
      } catch (error) {
        console.error("Error updating session mark status:", error);
      }
    },
    showNotification(message, type) {
      this.notification = message;
      this.notificationType = type;

      setTimeout(() => {
        this.notification = "";
        this.notificationType = "";
      }, 3000);
    },
    validateMark(mark) {
      if (mark.mark < 0 || mark.mark > 10) {
        alert("Marks must be between 0 and 10.");
        mark.mark = 0;
      } else if (!/^\d+(\.\d{1,2})?$/.test(mark.mark)) {
        mark.mark = Math.round(mark.mark * 100) / 100;
      }
    }

  },
  mounted() {
    this.fetchMarks();
  },
};
</script>

<style lang="scss" scoped>
input {
    border-radius: 10px;
    border: 1px solid var(--border);
    width: 100px;
    height: 30px;
    padding: 10px;
    font-size: 16px;
}

th:last-child,
td:last-child {
    width: 150px
}

input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
}
</style>