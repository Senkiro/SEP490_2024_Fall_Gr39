<template>
  <div class="container">
    <div class="headContent">
      <h1>Class record</h1>
    </div>

    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th class="center">No</th>
          <th>Class</th>
          <th class="center">Number of students</th>
          <th class="center">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(classItem, index) in classes" :key="classItem.classId">
          <td class="center">{{ index + 1 }}</td>
          <td :style="{ color: classItem.classColour }">{{ classItem.className }}</td>
          <td class="center">{{ classItem.studentCount || "N/A" }}</td>
          <td class="center">
            <VsxIcon
                iconName="Eye"
                :size="30"
                color="#171717"
                type="linear"
                @click="viewStudentRecord(classItem.classId)"
            />
          </td>
        </tr>
        <tr v-if="classes.length === 0">
          <td colspan="4" class="center">No record.</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";
import axios from "axios";

export default {
  name: "ClassRecord",
  components: { VsxIcon },
  data() {
    return {
      batchEntity: { name: "FALL2024" },
      classes: [] // Initially empty, will be populated via API
    };
  },
  methods: {
    async fetchClasses() {
      try {
        const userid = sessionStorage.getItem("userId");
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
            `http://localhost:8088/fja-fap/teacher/get-class-teacher/${userid}`,
            {
              headers: {Authorization: `Bearer ${token}`},
            }
        );
        if (response.data && response.data.result) {
          this.classes = response.data.result.map((classItem) => ({
            ...classItem,
            studentCount: classItem.studentCount || 0
          }));
        }
      } catch (error) {
        console.error("Error fetching class data:", error);
      }
    },
    viewStudentRecord(classId) {
      this.$router.push({ name: "TeacherStudentRecord", params: { classId } });
    }
  },
  mounted() {
    this.fetchClasses(); // Fetch data when the component is mounted
  }
};
</script>

<style scoped></style>
