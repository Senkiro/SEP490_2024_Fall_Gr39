<template>
  <div class="container">
    <div class="headContent">
      <h1>Batch Record</h1>
    </div>

    <div class="actions">
      <button @click="showAddBatchPopup = true">
        <VsxIcon iconName="AddCircle" size="20" type="bold"/>
        Add batch
      </button>
    </div>

    <div class="actions">
      <div class="search-container">
        <input
          type="text"
          placeholder="Search..."
          class="search-field"
          v-model="searchQuery"
          @input="searchBatchs"
        />
        <VsxIcon iconName="SearchNormal1" color="#ADB5BD" type="linear"/>
      </div>
    </div>

    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th id="index" class="center">No</th>
          <th>Batch</th>
          <th>Year</th>
          <th>Start time</th>
          <th>End time</th>
          <th id="number" class="center">Number of students</th>
          <th>Status</th>
          <th id="action" class="center">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(batchEntity, index) in batches" :key="batchEntity.id">
          <td id="index" class="center">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
          <td @click="viewBatchDetail(batchEntity)" class="clickable">{{ batchEntity.batchName }}</td>
          <td>{{ batchEntity.year }}</td>
          <td>{{ batchEntity.startTime }}</td>
          <td>{{ batchEntity.endTime }}</td>
          <td id="number" class="center">{{ batchEntity.studentCount || 0 }}</td>
          <td
              :class="{
                'on-progress': getStatus(batchEntity) === 'On progress',
                'not-happen': getStatus(batchEntity) === 'Not happen',
                'graduated': getStatus(batchEntity) === 'Graduated'
              }">
            {{ getStatus(batchEntity) }}
          </td>

          <td id="action" class="center">
            <VsxIcon iconName="Eye" :size="30" color="#171717" type="linear" @click="viewBatchDetail(batchEntity)"/>
          </td>
        </tr>
        <tr v-if="batches.length === 0">
          <td colspan="8" class="center">No record.</td>
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

    <div v-if="showAddBatchPopup" class="popup-overlay">
      <div class="popup">
        <div class="exit-icon">
          <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="confirmCancel"/>
        </div>
        <div class="popup-title">
          <h2>Add Batch</h2>
        </div>
        <form @submit.prevent="addBatch">
          <div class="form-group">
            <label for="batchName">Name <span class="required">*</span></label>
            <input type="text" id="batchName" v-model="newBatch.name" required/>
          </div>
          <div class="form-group">
            <label for="startTime">Start time <span class="required">*</span></label>
            <input type="date" id="startTime" v-model="newBatch.startTime" @change="handleStartDateChange"/>
          </div>
          <div class="form-group">
            <label for="endTime">End time <span class="required">*</span></label>
            <input type="date" id="endTime" v-model="newBatch.endTime"/>
          </div>
          <div class="form-group">
            <label for="year">Year <span class="required">*</span></label>
            <input type="number" id="year" v-model="newBatch.year" min="2000" max="2100"/>
          </div>
          <div class="actions">
            <button class="btn-submit" type="submit"> Create</button>
          </div>
        </form>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </div>
    </div>

    <div v-if="notification.message" class="notification" :class="notification.type">
      {{ notification.message }}
    </div>
  </div>
</template>

<script>
import {VsxIcon} from "vue-iconsax";
import axios from 'axios';

export default {
  name: "BatchRecord",
  components: {
    VsxIcon,
  },
  data() {
    return {
      batches: [],
      showAddBatchPopup: false,
      newBatch: {
        name: "",
        startTime: "",
        endTime: "",
        year: ""
      },
      errorMessage: "",
      currentPage: 1,
      itemsPerPage: 10,
      totalElements: 0,
      totalPages: 0,
      isLoading: false,
      notification: {
        message: "",
        type: "" // "success" or "error"
      },
      searchQuery: '',
    };
  },
  mounted() {
    this.fetchBatches();
  },
  methods: {
    async fetchBatches() {
      this.isLoading = true;
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/batch?page=${this.currentPage - 1}&size=${this.itemsPerPage}`,
            {headers: {Authorization: `Bearer ${token}`}}
        );
        this.batches = response.data.result.content;
        this.totalElements = response.data.result.totalElements;
        this.totalPages = Math.ceil(this.totalElements / this.itemsPerPage);

        this.updateDisplayedPages();
        await Promise.all(
            this.batches.map(async (batch) => {
              const studentCount = await this.countStudents(batch.batchName);
              batch.studentCount = studentCount;
            })
        );
      } catch (error) {
        console.error('Error fetching batches:', error);
        this.showNotification("Error fetching batches. Please try again.", 'error');
      } finally {
        this.isLoading = false;
      }
    },
    changePage(newPage) {
      if (newPage > 0 && newPage <= this.totalPages) {
        this.currentPage = newPage;
        this.fetchBatches();
      }
    },
    viewBatchDetail(batchEntity) {
      this.$router.push({name: 'BatchDetail', params: {batchName: batchEntity.batchName}});
    },
    async addBatch() {
      if (!this.validateBatch()) {
        return;
      }

      if (this.newBatch.startTime && this.newBatch.endTime && new Date(this.newBatch.startTime) > new Date(this.newBatch.endTime)) {
        this.showNotification("Start time must be before end time.", "error");
        return;
      }

      try {
        const token = sessionStorage.getItem('jwtToken');
        await axios.post('http://localhost:8088/fja-fap/staff/save-batch',
            {
              batchName: this.newBatch.name,
              startTime: new Date(this.newBatch.startTime).toISOString().split("T")[0],
              endTime: new Date(this.newBatch.endTime).toISOString().split("T")[0],
              year: this.newBatch.year,
            },
            {headers: {Authorization: `Bearer ${token}`}}
        );
        await this.fetchBatches();
        this.showAddBatchPopup = false;
        this.newBatch = {name: "", startTime: "", endTime: "", year: ""};
        this.errorMessage = "";

        // Show success notification
        this.showNotification("Batch created successfully!", "success");

      } catch (error) {
        console.error('Error creating batch:', error);
        this.showNotification(error.response?.data?.message || "Error creating batch. Please try again.", 'error');
      }
    },
    async searchBatchs() {
      if (this.searchQuery.trim() === "") {
        this.fetchBatches();
        return;
      }
      this.isLoading = true;
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/search-batch?name=${this.searchQuery}&page=${this.currentPage - 1}&size=${this.itemsPerPage}`,
            { headers: { Authorization: `Bearer ${token}` } }
        );
        this.batches = response.data.result.content;
        this.totalElements = response.data.result.totalElements;
        this.totalPages = Math.ceil(this.totalElements / this.itemsPerPage);
        this.updateDisplayedPages();

        await Promise.all(
            this.batches.map(async (batch) => {
              const studentCount = await this.countStudents(batch.batchName);
              batch.studentCount = studentCount;
            })
        );
      } catch (error) {
        console.error("Error searching events:", error);
        this.showNotification("Error searching batch. Please try again.", 'error');
      } finally {
        this.isLoading = false;
      }
    },
    handleStartDateChange() {
      const startDate = new Date(this.newBatch.startTime);

      if (!this.isWeekday(startDate)) {
        this.showNotification("Start date must be a weekday (Mon-Fri).", "error");
        return;
      }

      // Tính toán ngày kết thúc sau 45 ngày làm việc
      const endDate = this.calculateEndDate(startDate, 45);
      this.newBatch.endTime = endDate.toISOString().split("T")[0];
    },

    isWeekday(date) {
      const day = date.getDay();
      return day >= 1 && day <= 5;
    },

    // Hàm tính ngày kết thúc
    calculateEndDate(startDate, workDays) {
      let currentDate = new Date(startDate);
      let daysAdded = 0;

      while (daysAdded < workDays) {
        currentDate.setDate(currentDate.getDate() + 1);
        if (this.isWeekday(currentDate)) {
          daysAdded++;
        }
      }

      return currentDate;
    },
    validateBatch() {
      const startDate = new Date(this.newBatch.startTime);
      const endDate = new Date(this.newBatch.endTime);

      if (!this.newBatch.startTime || !this.newBatch.endTime) {
        this.showNotification("Start time and End time are required.", "error");
        return false;
      }

      if (!this.isWeekday(startDate)) {
        this.showNotification("Start date must be a weekday (Mon-Fri).", "error");
        return false;
      }

      const expectedEndDate = this.calculateEndDate(startDate, 45);
      if (expectedEndDate.getTime() !== endDate.getTime()) {
        this.showNotification(
            `End date must be ${expectedEndDate.toISOString().split("T")[0]}.`,
            "error"
        );
        return false;
      }

      return true;
    },
    confirmCancel() {
      this.showAddBatchPopup = false;
      this.errorMessage = "";
      this.newBatch = {name: "", startTime: "", endTime: "", year: ""};
    },
    getStatus(batch) {
      const currentDate = new Date();
      const startTime = new Date(batch.startTime);

      // Xử lý theo batchStatus
      if (batch.batchStatus === 2) {
        return currentDate < startTime ? 'Not happen' : 'On progress';
      }

      if (batch.batchStatus === 1) {
        return 'On progress';
      }

      if (batch.batchStatus === 0) {
        return 'Graduated';
      }

      // Trường hợp không xác định
      return 'Unknown';
    },
    showNotification(message, type) {
      this.notification = {message, type};
      setTimeout(() => {
        this.notification.message = "";
      }, 3000);
    },
    updateDisplayedPages() {
      const pages = [];
      if (this.totalPages <= 5) {
        for (let i = 1; i <= this.totalPages; i++) {
          pages.push(i);
        }
      } else {
        if (this.currentPage <= 3) {
          pages.push(1, 2, 3, '...', this.totalPages);
        } else if (this.currentPage >= this.totalPages - 2) {
          pages.push(1, '...', this.totalPages - 2, this.totalPages - 1, this.totalPages);
        } else {
          pages.push(1, '...', this.currentPage, '...', this.totalPages);
        }
      }
      this.displayedPages = pages;
    },
    async countStudents(batchName) {
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(`http://localhost:8088/fja-fap/staff/get-student-by-batch?page=0&size=1000&batch_name=${batchName}`, {
          headers: {Authorization: `Bearer ${token}`}
        });
        return response.data.result.totalElements;
      } catch (error) {
        console.error('Error fetching student count:', error);
        return 0;
      }
    }
  },
  watch: {
    // Watcher to update URL when `currentPage` changes
    currentPage(newPage) {
      this.$router.push({path: '/staff/batch-record', query: {page: newPage}}).catch(() => {
      });
    }
  }
};
</script>

<style lang="scss" scoped>
#index{
  width: 8%;
}
#number{
  width: 20%;
}
.not-happen{
  font-weight: normal !important;
}
</style>