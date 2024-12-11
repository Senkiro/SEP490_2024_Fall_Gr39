<template>
  <div class="container">
    <div class="headContent">
      <h1>Mark report</h1>
    </div>
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>Grade category</th>
            <th>Grade item</th>
            <th>Weight</th>
            <th>Value</th>
            <th>Comment</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td class="bold">Participation</td>
            <td class="bold">Participation</td>
            <td class="bold">10%</td>
            <td></td>
            <td></td>
          </tr>
          <tr v-for="(grade, index) in grades" :key="index">
            <td v-if="index === 0 || grades[index - 1].category !== grade.category" class="bold"
              :rowspan="calculateRowspan(grades, index, 'category')">
              {{ grade.category }}
            </td>
            <td>{{ grade.item }}</td>
            <td></td>
            <td>{{ grade.value }}</td>
            <td>{{ grade.comment || "" }}</td>
          </tr>
          <!-- Tổng điểm -->
          <tr>
            <td class="bold"> </td>
            <td class="bold">Total</td>
            <td class="bold">70%</td>
            <td>{{ totalValue }}</td>
            <td></td>
          </tr>
          <tr>
            <td class="bold">Mid-term Exam</td>
            <td class="bold">Mid-term Exam</td>
            <td class="bold">10%</td>
            <td>{{ midtermValue }}</td>
            <td></td>
          </tr>
          <tr>
            <td class="bold">Final Exam</td>
            <td class="bold">Final Exam</td>
            <td class="bold">10%</td>
            <td>{{ finalValue }}</td>
            <td></td>
          </tr>
          <tr>
            <td class="bold">Course total</td>
            <td class="bold" colspan="2">Average</td>
          </tr>
        </tbody>
      </table>
      <div class="actions">
        <p>Current GPA: <strong>{{ currentGPA }}</strong></p>
        <p class="grade-remark">
          <strong>Grade:</strong> <span class="remark">{{ gradeRemark }}</span>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "StudentMarkReport",
  data() {
    return {
      student: {
        fullname: "Pham The Minh",
        rollNumber: "FA171392",
      },
      batch: "FALL2024",
      className: "Blue",
      currentGPA: 7.6,
      grades: [],
      totalValue: 0.0,
      totalGPA: 8.6,
      gradeRemark: "Very Good",
    };
  },
  methods: {
    getGradeRemark(gpa) {
      if (gpa < 5) {
        return "Poor";
      } else if (gpa >= 5 && gpa < 7) {
        return "Fair";
      } else if (gpa >= 7 && gpa < 8) {
        return "Good";
      } else if (gpa >= 8 && gpa < 9) {
        return "Very Good";
      } else if (gpa >= 9 && gpa <= 10) {
        return "Excellent";
      } else {
        return "Invalid GPA";
      }
    },
    calculateRowspan(grades, index, property) {
      let count = 1;
      while (index + count < grades.length && grades[index + count][property] === grades[index][property]) {
        count++;
      }
      return count;
    },
    async fetchData() {
      try {
        const studentId = sessionStorage.getItem("userId");
        if (!studentId) {
          console.error("studentId không tồn tại trong URL");
          return;
        }

        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
          `http://localhost:8088/fja-fap/staff/get-student-mark/${studentId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        const { result } = response.data;

        if (result && result.length) {
          // Lọc Daily Exam
          const dailyExams = result
            .filter(item => item.mark > 0)
            .filter(item => item.examResponse.examTypeRate.examCategory === "Daily")
            .sort((a, b) => a.examResponse.examId - b.examResponse.examId);

          // Map dữ liệu Daily Exam
          this.grades = dailyExams.map(item => ({
            category: item.examResponse.examTypeRate.examCategory + " Exam",
            item: item.examResponse.examTitle,
            weight: `${item.examResponse.examTypeRate.examRate}%`,
            value: item.mark || 0.0,
            comment: item.comment,
          }));

          // Tính tổng trung bình của Daily Exam
          this.totalValue = (
            dailyExams.reduce((sum, item) => sum + (item.mark || 0), 0) / dailyExams.length
          );

          // Lấy điểm của Mid-term Exam
          const midtermExam = result.find(
            item => item.examResponse.examTypeRate.examCategory === "Mid-term Exam"
          );
          this.midtermValue = midtermExam ? midtermExam.mark || 0.0 : null;

          // Lấy điểm của Final Exam
          const finalExam = result.find(
            item => item.examResponse.examTypeRate.examCategory === "Final Exam"
          );
          this.finalValue = finalExam ? finalExam.mark || 0.0 : null;

          // Cập nhật thông tin sinh viên
          const studentInfo = result[0].studentResponse;
          this.student = {
            fullname: studentInfo.userInforResponse.fullName,
            rollNumber: studentInfo.rollNumber,
          };
          this.batch = studentInfo.batchName;
          this.className = studentInfo.classResponse.className;
          this.currentGPA = studentInfo.avgMark;
          this.gradeRemark = this.getGradeRemark(this.currentGPA);
        }
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    },
  },
  mounted() {
    this.fetchData();
  }
};
</script>
<style lang="scss" scoped>
.bold {
  color: #171717 !important;
  font-weight: bold !important;
}

.container {
  .table-container {
    table {

      // tr,
      // td {
      //   border: 2px solid #DFE7FB;
      // }

      tr {
        color: #979B9F;

        .chapter {
          cursor: pointer;
        }

        td:first-child,
        th:first-child {
          max-width: 100px;
        }

        td:first-child,
        td:nth-child(2) {
          color: #979B9F;
          font-weight: normal;
        }

      }

    }
  }
}
</style>
