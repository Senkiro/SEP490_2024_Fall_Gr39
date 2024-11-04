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
      <button>
        <VsxIcon iconName="Chart" size="20" type="bold" />
        View statistical chart
      </button>
    </div>

    <table class="batchEntity-table">
      <thead>
      <tr>
        <th>No</th>
        <th>Batch</th>
        <th>Year</th>
        <th>Start time</th>
        <th>End time</th>
        <th style="text-align: center;">Number of students</th>
        <th>Status</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(batchEntity, index) in batches" :key="batchEntity.id">
        <td>{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
        <td @click="viewBatchDetail(batchEntity)">{{ batchEntity.batchName }}</td>
        <td>{{ batchEntity.year }}</td>
        <td>{{ batchEntity.startTime }}</td>
        <td>{{ batchEntity.endTime }}</td>
        <td style="text-align: center;">0</td>
        <td :class="{'status-progress': getStatus(batchEntity.endTime) === 'On progress', 'status-graduated': getStatus(batchEntity.endTime) === 'Graduated'}">
          {{ getStatus(batchEntity.endTime) }}
        </td>
        <td>
          <VsxIcon iconName="Eye" :size="24" @click="viewBatchDetail(batchEntity)" style="padding-left: 5px;" />
        </td>
      </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">Previous</button>
      <span>Page {{ currentPage }} of {{ totalPages }}</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">Next</button>
    </div>

    <div v-if="showAddBatchPopup" class="popup-overlay">
      <div class="popup">
        <h2>Add Batch</h2>
        <form @submit.prevent="addBatch">
          <div class="form-group">
            <label for="batchName">Name <span class="required">*</span></label>
            <input type="text" id="batchName" v-model="newBatch.name" required />
          </div>
          <div class="form-group">
            <label for="startTime">Start time <span class="required">*</span></label>
            <input type="date" id="startTime" v-model="newBatch.startTime" required />
          </div>
          <div class="form-group">
            <label for="endTime">End time <span class="required">*</span></label>
            <input type="date" id="endTime" v-model="newBatch.endTime" />
          </div>
          <div class="form-group">
            <label for="year">Year <span class="required">*</span></label>
            <input type="number" id="year" v-model="newBatch.year" min="1900" max="2100" />
          </div>
          <div class="button-group">
            <button type="submit" class="btn btn-create">Create</button>
            <button type="button" class="btn btn-cancel" @click="confirmCancel">Cancel</button>
          </div>
        </form>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";
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
      itemsPerPage: 5,
      totalElements: 0,
      totalPages: 0,
      isLoading: false,
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
            { headers: { Authorization: `Bearer ${token}` } }
        );
        this.batches = response.data.result.content;
        this.totalElements = response.data.result.totalElements;
        this.totalPages = Math.ceil(this.totalElements / this.itemsPerPage);
      } catch (error) {
        console.error('Error fetching batches:', error);
        this.errorMessage = "Error fetching batches. Please try again.";
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
      this.$router.push({ name: 'BatchDetail', params: { batchId: batchEntity.id } });
    },
    async addBatch() {
      if (this.newBatch.startTime && this.newBatch.endTime && new Date(this.newBatch.startTime) > new Date(this.newBatch.endTime)) {
        this.errorMessage = "Start time must be before end time.";
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
            { headers: { Authorization: `Bearer ${token}` } }
        );
        await this.fetchBatches();
        //this.batches.push(response.data.result);
        this.showAddBatchPopup = false;
        this.newBatch = { name: "", startTime: "", endTime: "", year: "" };
        this.errorMessage = "";
      } catch (error) {
        console.error('Error creating batch:', error);
        this.errorMessage = error.response?.data?.message || "Error creating batch. Please try again.";
      }
    },
    confirmCancel() {
      this.showAddBatchPopup = false;
      this.errorMessage = "";
      this.newBatch = { name: "", startTime: "", endTime: "", year: "" };
    },
    getStatus(endTime) {
      const currentDate = new Date();
      const batchEndDate = new Date(endTime);
      return batchEndDate < currentDate ? 'Graduated' : 'On progress';
    },
  },
  watch: {
    // Watcher để cập nhật URL khi `currentPage` thay đổi
    currentPage(newPage) {
      this.$router.push({ path: '/staff/batch-record', query: { page: newPage } }).catch(() => {});
    }
  }
};
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 20px 0;
}

.pagination button {
  padding: 10px 15px;
  border: none;
  background-color: #4a90e2;
  color: white;
  cursor: pointer;
  border-radius: 5px;
  margin: 0 5px;
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.batchEntity-table th, .batchEntity-table td {
  padding: 12px 24px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  align-items: center;
  align-content: center;
}

.batchEntity-table td:hover{
  cursor: pointer;
}

.batchEntity-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}

.status-progress {
  color: #304CB2;
}

.status-graduated {
  color: #6ECBB8;
}

.action-icon {
  cursor: pointer;
  color: #333;
}

.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.popup {
  background: #fff;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
  width: 400px;
  max-width: 90%;
}

.popup h2 {
  margin-top: 0;
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input {
  width: 100%;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.btn-create {
  background-color: #4a90e2;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
}

.btn-cancel {
  background-color: #ccc;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  margin-left: 10px;
}

.error {
  color: red;
  font-size: 14px;
  margin-top: 10px;
}

.headContent {
  margin: 20px 0px;
}

.pageTitle {
  display: block;
}

h1 {
  width: fit-content;
  font-size: 36px;
  background: -webkit-linear-gradient(180deg, #304CB2, #1A2C6F);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-weight: bold;
  margin: 20px 0px;
}

.container {
  padding: 20px;
}

button {
  background-image: linear-gradient(90deg, #3E5DD4, #223374);
  padding: 10px 20px;
  color: #fff;
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: normal;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  gap: 10px;
}

.actions {
  display: flex;
  flex-direction: row-reverse;
  gap: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

.required {
  color: red;
  font-weight: bold;
}

.button-group {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.actions {
  display: flex;
  flex-direction: row-reverse;
  gap: 20px;
  margin-bottom: 20px;
}

</style>

