<template>
  <div class="container">
    <div class="headContent">
      <h1>Attendance report</h1>
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
          v-model="selectedClassId"
          @change="fetchStudentsByClassId(selectedClassId)">
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
          <th class="center">Attended</th>
          <th class="center">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(student, index) in students" :key="index">
          <td class="center">{{ student.no }}</td>
          <td>{{ student.fullname }}</td>
          <td>{{ student.rollNumber }}</td>
          <td>{{ student.japaneseName }}</td>
          <td class="center">{{ student.attended }}</td>
          <td class="center">
            <VsxIcon
                iconName="Eye"
                :size="30"
                color="#171717"
                type="linear"
                @click="viewStudentAttendanceReport(student.studentId)"
            />
          </td>
        </tr>
        </tbody>
      </table>
      <div class="pagination" v-if="totalPages > 1">
        <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">
          <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717" />
        </button>
        <button v-for="page in totalPages" :key="page"
                :class="{ active: page === currentPage }"
                @click="changePage(page)">
          {{ page }}
        </button>
        <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">
          <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717" />
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import {VsxIcon} from "vue-iconsax";
import axios from 'axios';

export default {
  components: {
    VsxIcon
  },
  data() {
    return {
      batches: [],
      classes: [],
      currentPage: 1,
      itemsPerPage: 20,
      totalElements: 0,
      totalPages: 0,

      selectedBatch: '',
      selectedClassId: '',

      notification: {
        message: "",
        type: "" // "success" or "error"
      },
    };
  },
  methods: {
    async fetchBatches() {
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/batch`, {
              params: {
                page: 0,
                size: 1000
              },
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        this.batches = response.data.result.content.map(batch => {
          const start = new Date(batch.startTime);
          const end = new Date(batch.endTime);
          const weeks = [];

          let current = new Date(start);
          while (current <= end) {
            const weekStart = new Date(current);
            const weekEnd = new Date(current);
            weekEnd.setDate(weekEnd.getDate() + 6);
            if (weekEnd > end) weekEnd.setDate(end.getDate());

            weeks.push({
              start: weekStart.toISOString().split('T')[0],
              end: weekEnd.toISOString().split('T')[0]
            });

            current.setDate(current.getDate() + 7);
          }

          return {
            ...batch,
            weeks,
          };
        });
      } catch (error) {
        console.error('Error fetching batches:', error);
        this.batches = [];
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
    async fetchStudentsByClassId(classId) {
      if (!classId) {
        this.students = [];
        return;
      }

      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-students-class`, {
              params: {
                page: this.currentPage - 1,
                size: this.itemsPerPage,
                classId: classId,
              },
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        const result = response.data.result;
        if (result && result.content) {
          this.students = result.content.map((student, index) => ({
            no: (this.currentPage - 1) * this.itemsPerPage + index + 1,
            studentId: student.studentId,
            fullname: student.userInforResponse.fullName,
            rollNumber: student.rollNumber,
            japaneseName: student.userInforResponse.japaneseName,
            attended: `${student.attendanceStatisticsResponse.attendCount}/${student.attendanceStatisticsResponse.totalCount}`,
          }));
          this.totalElements = result.totalElements;
          this.totalPages = Math.ceil(this.totalElements / this.itemsPerPage);
        } else {
          this.students = [];
          console.error('Unexpected response format', response.data);
        }
      } catch (error) {
        console.error('Error fetching students:', error);
        this.students = [];
      }
    },
    changePage(newPage) {
      if (newPage > 0 && newPage <= this.totalPages) {
        this.currentPage = newPage;
        this.fetchStudentsByClassId(this.selectedClassId);
      }
    },
    viewStudentAttendanceReport(studentId) {
      this.$router.push({ name: 'StaffStudentAttendanceReport', params: { id: studentId } });
    }
  },
  mounted() {
    this.fetchBatches();
  },
}
</script>

<style lang="scss" scoped></style>