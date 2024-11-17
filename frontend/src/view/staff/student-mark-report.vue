<template>
  <div class="container">
    <div class="headContent">
      <h1>Student's mark report</h1>
      <div class="student-info">
        <p><strong>Student:</strong> {{ student.fullname }} - {{ student.rollNumber }}</p>
        <p><strong>Batch:</strong> {{ batch }} <strong>Class:</strong> {{ className }} <strong>Current GPA:</strong> {{ currentGPA }}</p>
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
          <tr v-for="(grade, index) in grades" :key="index">
            <td>{{ grade.category }}</td>
            <td>{{ grade.item }}</td>
            <td>{{ grade.weight }} %</td>
            <td>{{ grade.value }}</td>
            <td>{{ grade.comment || '' }}</td>
          </tr>
          <tr class="total-row">
            <td colspan="3"><strong>Total</strong></td>
            <td colspan="2">{{ totalGPA }}</td>
          </tr>
          </tbody>
        </table>
        <div class="gpa-info">
          <p><strong>Current GPA:</strong> {{ currentGPA }}</p>
          <p class="grade-remark"><strong>Grade:</strong> <span class="remark">{{ gradeRemark }}</span></p>
        </div>
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
        { category: "Participation", item: "Participation", weight: 10, value: 8.2 },
        { category: "Daily Exam", item: "Chapter 1 - Vocabulary", weight: 10, value: 8.2 },
        { category: "Daily Exam", item: "Chapter 1 - Kanji", weight: 10, value: 8.7 },
        { category: "Daily Exam", item: "Chapter 1 - Grammar", weight: 10, value: 7.8 },
        { category: "Daily Exam", item: "Chapter 2 - Vocabulary", weight: 10, value: 8.5 },
        { category: "Daily Exam", item: "Chapter 2 - Kanji", weight: 10, value: 8.0 },
        { category: "Daily Exam", item: "Chapter 2 - Grammar", weight: 10, value: 9.0 },
        { category: "Daily Exam", item: "Chapter 3 - Vocabulary", weight: 10, value: 10.0 },
        { category: "Mid-term Exam", item: "Mid-term Exam", weight: 10, value: 9.0 },
        { category: "Final Exam", item: "Final Exam", weight: 10, value: 9.5 },
      ],
      totalGPA: 8.6,
      gradeRemark: "Very Good",
    };
  },
};
</script>

<style scoped>

</style>
