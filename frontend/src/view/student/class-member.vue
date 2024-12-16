<template>
  <div class="container">
    <div class="headContent">
      <h1>Class member</h1>
    </div>

    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th class="center">No</th>
          <th>Fullname</th>
          <th>Japanese Name</th>
          <th>Roll Number</th>
          <th class="center">Image</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(student, index) in students" :key="student.studentId"
            :class="{ highlight: student.studentId === currentUserId }">
          <td class="center">{{ index + 1 + (currentPage - 1) * itemsPerPage }}</td>
          <td>{{ student.userInforResponse.fullName }}</td>
          <td>{{ student.userInforResponse.japaneseName }}</td>
          <td>{{ student.rollNumber }}</td>
          <td class="center">
            <img v-if="student.userInforResponse.img"
                 :src="`/${student.userInforResponse.img}`"
                 alt="Student Image"/>
            <span v-else>
              <img src="@/assets/smiling-young-man-illustration_1308-174669.avif"
                   alt="Default Student Image"/>
            </span>
          </td>
        </tr>
        <tr v-if="students.length === 0">
          <td colspan="5" class="center">No record.</td>
        </tr>
        </tbody>
      </table>

      <div class="pagination" v-if="totalPages > 1">
        <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">
          <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717"/>
        </button>
        <button v-for="page in displayedPages" :key="page" :class="{ active: page === currentPage }"
                @click="changePage(page)">
          {{ page }}
        </button>
        <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">
          <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717"/>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {VsxIcon} from "vue-iconsax";

export default {
  components: {VsxIcon},
  data() {
    return {
      students: [],
      currentPage: 1,
      itemsPerPage: 10,
      totalPages: 0,
      currentUserId: "", // ID của người dùng hiện tại
    };
  },
  computed: {
    displayedPages() {
      const range = [];
      for (let i = 1; i <= this.totalPages; i++) {
        range.push(i);
      }
      return range;
    },
  },
  methods: {
    async fetchStudents() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        this.currentUserId = sessionStorage.getItem("userId");

        // Send API request
        const response = await axios.get(`http://localhost:8088/fja-fap/student/get-student-class`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          params: {
            studentId: this.currentUserId,
            page: this.currentPage - 1,
            size: this.itemsPerPage,
          },
        });

        this.students = response.data.result.content;
        this.totalPages = response.data.result.totalPages;
      } catch (error) {
        console.error("Error fetching students:", error);
      }
    },
    changePage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
        this.fetchStudents();
      }
    },
  },
  mounted() {
    this.fetchStudents();
  },
};
</script>

<style lang="scss">
img {
  width: 150px;
}

tr.highlight {
  background-color: #d6f7ff;
  /* Màu nền nổi bật */
}
</style>