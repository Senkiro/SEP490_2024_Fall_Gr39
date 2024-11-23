<template>
  <div class="container">
    <div class="headContent">
      <h1>Student's mark report</h1>
      <p>Student: <strong> {{ student.fullname }} - {{ student.rollNumber }}</strong></p>
    </div>
    <div class="actions">
      <p>Batch: <strong> {{ batch }} </strong></p>
      <p>Class: <strong> {{ className }} </strong></p>
      <p>Current GPA:<strong> {{ currentGPA }}</strong></p>
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
            <td class="bold" v-if="index === 0 || grades[index - 1].category !== grade.category"
              :rowspan="calculateRowspan(grades, index, 'category')">{{ grade.category }}</td>
            <td :class="{ bold: grade.item === 'Total' }">{{ grade.item }}</td>
            <td :class="{ bold: grade.item === 'Total' }">{{ grade.weight }}</td>
            <td>{{ grade.value }}</td>
            <td></td>
          </tr>
          <tr>
            <td class="bold">Mid-term Exam</td>
            <td class="bold">Mid-term Exam</td>
            <td class="bold">10%</td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td class="bold">Final Exam</td>
            <td class="bold">Final Exam</td>
            <td class="bold">10%</td>
            <td></td>
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
        <p class="grade-remark"><strong>Grade:</strong> <span class="remark">{{ gradeRemark }}</span></p>
      </div>
    </div>
  </div>
</template>

<script>
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
      grades: [
        { category: "Daily Exam", item: "Chapter 1 - Vocabulary", weight: "", value: 8.2 },
        { category: "Daily Exam", item: "Chapter 1 - Kanji", weight: "", value: 8.7 },
        { category: "Daily Exam", item: "Chapter 1 - Grammar", weight: "", value: 7.8 },
        { category: "Daily Exam", item: "Chapter 2 - Vocabulary", weight: "", value: 8.5 },
        { category: "Daily Exam", item: "Chapter 2 - Kanji", weight: "", value: 8.0 },
        { category: "Daily Exam", item: "Chapter 2 - Grammar", weight: "", value: 9.0 },
        { category: "Daily Exam", item: "Chapter 3 - Vocabulary", weight: "", value: 10.0 },
        { category: "Daily Exam", item: "Total", weight: "70%", value: 10.0 },
      ],
      totalGPA: 8.6,
      gradeRemark: "Very Good",
    };
  },
  methods: {
    calculateRowspan(grades, index, property) {
      let count = 1;
      while (index + count < grades.length && grades[index + count][property] === grades[index][property]) {
        count++;
      }
      return count;
    }
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
