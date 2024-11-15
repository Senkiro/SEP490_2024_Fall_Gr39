<template>
  <div class="container">
    <div class="headContent">
      <h1>Mark Report</h1>
    </div>

      <div class="filters">
        <select v-model="selectedSemester" @change="fetchData">
          <option value="FALL2024">FALL2024</option>
          <option value="SPRING2024">SPRING2024</option>
          <option value="SUMMER2024">SUMMER2024</option>
        </select>

        <select v-model="selectedClass" @change="fetchData">
          <option value="Blue">Blue</option>
          <option value="Green">Green</option>
          <option value="Red">Red</option>
        </select>
      </div>

      <div class="table-container">
        <table>
          <thead>
          <tr>
            <th class="center">No</th>
            <th>Fullname</th>
            <th>Roll number</th>
            <th>Japanese name</th>
            <th class="center">Average mark</th>
            <th class="center">Action</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(student, index) in students" :key="student.id">
            <td class="center">{{ index + 1 }}</td>
            <td>{{ student.fullname }}</td>
            <td>{{ student.rollNumber }}</td>
            <td>{{ student.japaneseName || '-' }}</td>
            <td class="center">{{ student.averageMark }}</td>
            <td class="center">
              <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear"
                       @click="viewStudentDetails(student.id)" />
            </td>
          </tr>
          </tbody>
        </table>


        <div class="pagination">
          <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">
            <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717" />
          </button>
          <button v-for="page in displayedPages" :key="page" :class="{ active: page === currentPage }" @click="changePage(page)">
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
import { VsxIcon } from "vue-iconsax";


export default {
  name: "MarkReport",
  components: {
    VsxIcon
  },
  data() {
    return {
      selectedSemester: 'FALL2024',
      selectedClass: 'Blue',
      currentPage: 1,
      itemsPerPage: 5,
      students: [
        { id: 1, fullname: "Pham The Minh", rollNumber: "FA171392", japaneseName: "ファム・テ・ミン", averageMark: 8.2 },
        { id: 2, fullname: "Ngo Quoc Dat", rollNumber: "FA171288", japaneseName: "", averageMark: 9.4 },
        { id: 3, fullname: "Mai The Nam", rollNumber: "FA162133", japaneseName: "", averageMark: 8.9 },
        { id: 4, fullname: "Hoang Thai Son", rollNumber: "FA123456", japaneseName: "", averageMark: 9.7 },
        { id: 5, fullname: "Dang Hai Son", rollNumber: "FA112233", japaneseName: "", averageMark: 8.8 },
        { id: 6, fullname: "Nguyen Ha Phuong", rollNumber: "FA000001", japaneseName: "", averageMark: 9.0 }
      ]
    };
  },
  computed: {
    displayedPages() {
      const totalPages = Math.ceil(this.students.length / this.itemsPerPage);
      let pages = [];
      for (let i = 1; i <= totalPages; i++) {
        pages.push(i);
      }
      return pages;
    },
    totalPages() {
      return Math.ceil(this.students.length / this.itemsPerPage);
    },
    paginatedStudents() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.students.slice(start, end);
    }
  },
  methods: {
    changePage(page) {
      this.currentPage = page;
    },
    fetchData() {

      console.log(`Fetching data for semester: ${this.selectedSemester}, class: ${this.selectedClass}`);

    },
    viewStudentDetails(studentId) {
      this.$router.push({ name: "StudentMarkReport", params: { id: studentId } });
    }

  }
};
</script>

<style scoped>

</style>
