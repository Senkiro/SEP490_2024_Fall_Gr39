<template>
  <div class="container">
    <div class="headContent">
      <h1>Mark Report</h1>
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
          v-model="selectedClassId">
<!--          @change="fetchSessions(selectedClassId)"-->
        <option value="" disabled>Select Class</option>
        <option v-for="classItem in classes" :key="classItem.id" :value="classItem.id">
          {{ classItem.name }}
        </option>
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
            <td></td>
            <td></td>
            <td></td>
            <td class="center"></td>
            <td class="center">
              <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear"
                       @click="viewStudentDetails(student.id)" />
            </td>
          </tr>
          </tbody>
        </table>


        <div v-if="totalPages > 1" class="pagination">
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
          params: {
            page: 0,
            size: 1000,
          },
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.batches = response.data.result.content;
      } catch (error) {
        console.error("Error fetching batches:", error);
      }
    },
    async fetchClassesByBatch(batchName) {
      this.isLoadingClasses = true;
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-class-by-batch`, {
              params: {
                batch_name: batchName,
                page: 0,
                size: 100,
              },
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        if (response.data && response.data.result && response.data.result.content) {
          this.classes = response.data.result.content.map((classItem) => ({
            id: classItem.classId,
            name: classItem.className,
            colour: classItem.classColour,
          }));
        } else {
          this.classes = [];
          console.error('Unexpected response format', response.data);
        }
      } catch (error) {
        console.error('Error fetching classes:', error);
        this.classes = [];
      } finally {
        this.isLoadingClasses = false;
      }
    },

  },
  mounted() {
    this.fetchBatches();
  },
};
</script>

<style scoped>

</style>
