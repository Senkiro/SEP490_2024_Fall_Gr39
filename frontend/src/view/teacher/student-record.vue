<template>
  <div class="container">
    <div class="headContent">
      <h1>Class member</h1>
      <p>Class: <b>{{ className }}</b></p>
    </div>

    <div class="actions">
      <div class="search-container">
        <input
            type="text"
            placeholder="Search..."
            class="search-field"
            v-model="searchQuery"
            @input="filterStudents"
        />
        <VsxIcon iconName="SearchNormal1" color="#ADB5BD" type="linear" />
      </div>
    </div>

    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th class="center">No</th>
          <th>Japanese name</th>
          <th>Fullname</th>
          <th>Roll number</th>
          <th class="center">Image</th>
          <th class="center">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(student, index) in filteredStudents" :key="student.studentId">
          <td class="center">{{ index + 1 }}</td>
          <td>{{ student.userInforResponse.japaneseName }}</td>
          <td>{{ student.userInforResponse.fullName }}</td>
          <td>{{ student.rollNumber }}</td>
          <td class="center">
            <img v-if="student.userInforResponse.img" :src="student.userInforResponse.img"
                 alt="Student Image" />
            <span v-else><img src="@/assets/smiling-young-man-illustration_1308-174669.avif"></span>
          </td>
          <td class="center">
            <VsxIcon
                iconName="Eye"
                :size="30"
                color="#171717"
                type="linear"
                @click="navigateToProfile(student.studentId)"
            />
          </td>
        </tr>
        </tbody>
      </table>
      <div class="pagination" v-if="totalPages > 1">
        <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">
          <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717" />
        </button>
        <button v-for="page in displayedPages" :key="page" :class="{ active: page === currentPage }"
                @click="changePage(page)">
          {{ page }}
        </button>
        <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">
          <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717" />
        </button>
      </div>
    </div>
    </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      students: [],
      filteredStudents: [],
      currentPage: 1,
      totalPages: 1,
      itemsPerPage: 10,
      searchQuery: "",
      className: "",
    };
  },
  computed: {
    displayedPages() {
      const range = [];
      for (let i = 1; i <= this.totalPages; i++) {
        range.push(i);
      }
      return range.slice(
          Math.max(0, this.currentPage - 3),
          Math.min(this.totalPages, this.currentPage + 2)
      );
    },
  },
  methods: {
    async fetchStudents() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        console.log("Fetching students with token:", token);

        const response = await axios.get(
            `http://localhost:8088/fja-fap/teacher/get-students-class`,
            {
              headers: { Authorization: `Bearer ${token}` },
              params: {
                classId: this.$route.params.classId,
                page: this.currentPage - 1,
                size: this.itemsPerPage,
              },
            }
        );

        const result = response.data.result;
        this.students = result.content || [];
        this.filteredStudents = this.students;
        this.totalPages = result.totalPages || 0;
        this.className = this.students.length ? this.students[0].classResponse.className : "Unknown";
      } catch (error) {
        console.error("Error fetching students:", error);
      }
    },
    filterStudents() {
      this.filteredStudents = this.students.filter((student) =>
          student.userInforResponse.fullName.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
          student.rollNumber.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
    changePage(page) {
      if (page > 0 && page <= this.totalPages) {
        this.currentPage = page;
        this.fetchStudents();
      }
    },
    navigateToProfile(studentId) {
      switch (this.$route.name) {
        case "TeacherStudentRecord":
          this.$router.push({ name: "TeacherStudentProfile", params: { id: studentId } });
          break;
        case "StudentStudentRecord":
          this.$router.push({ name: "StudentStudentProfile", params: { id: studentId } });
          break;
      }
    },
  },
  created() {
    this.fetchStudents();
  },
};
</script>


<style lang="scss">
img {
  width: 150px;
  height: auto;
}
</style>
