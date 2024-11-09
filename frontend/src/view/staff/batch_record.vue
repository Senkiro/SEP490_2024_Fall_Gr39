<template>
  <div class="container">
    <div class="headContent">
      <h1>Batch Record</h1>
    </div>

    <div class="actions">
      <button>
        <VsxIcon iconName="Chart" size="20" type="bold" />
        View statistical chart
      </button>
      <button @click="showAddBatchPopup = true">
        <VsxIcon iconName="AddCircle" size="20" type="bold" />
        Add batch
      </button>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th class="center">No</th>
            <th>Batch</th>
            <th>Year</th>
            <th>Start time</th>
            <th>End time</th>
            <th class="center">Number of students</th>
            <th>Status</th>
            <th class="center">Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(batchEntity, index) in batches" :key="batchEntity.id">
            <td class="center">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
            <td @click="viewBatchDetail(batchEntity)" class="clickable">{{ batchEntity.batchName }}</td>
            <td>{{ batchEntity.year }}</td>
            <td>{{ batchEntity.startTime }}</td>
            <td>{{ batchEntity.endTime }}</td>
            <td class="center">0</td>
            <td
              :class="{ 'status-progress': getStatus(batchEntity.endTime) === 'On progress', 'status-graduated': getStatus(batchEntity.endTime) === 'Graduated' }">
              {{ getStatus(batchEntity.endTime) }}
            </td>
            <td class="center">
              <VsxIcon iconName="Eye" :size="30" color="#171717" type="linear" @click="viewBatchDetail(batchEntity)" />
            </td>
          </tr>
        </tbody>
      </table>
      <div class="pagination">
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

    <!-- Notification for errors or success -->
    <div v-if="notification.message" :class="['notification', notification.type]">
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
      itemsPerPage: 5,
      totalElements: 0,
      totalPages: 0,
      isLoading: false,
      notification: {
        message: "",
        type: "" // "success" or "error"
      },
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
        this.updateDisplayedPages();
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
      this.$router.push({ name: 'BatchDetail', params: { batchName: batchEntity.batchName } });
    },
    async addBatch() {
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
          { headers: { Authorization: `Bearer ${token}` } }
        );
        await this.fetchBatches();
        this.showAddBatchPopup = false;
        this.newBatch = { name: "", startTime: "", endTime: "", year: "" };
        this.errorMessage = "";

        // Show success notification
        this.showNotification("Batch created successfully!", "success");

      } catch (error) {
        console.error('Error creating batch:', error);
        this.showNotification(error.response?.data?.message || "Error creating batch. Please try again.", 'error');
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
    showNotification(message, type) {
      this.notification = { message, type };
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
    }
  },
  watch: {
    // Watcher to update URL when `currentPage` changes
    currentPage(newPage) {
      this.$router.push({ path: '/staff/batch-record', query: { page: newPage } }).catch(() => { });
    }
  }
};
</script>

<style lang="scss" scoped>
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

.notification {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 15px 20px;
  border-radius: 8px;
  font-size: 16px;
  color: #fff;
  z-index: 1000;
  transition: all 0.5s ease;
}

.notification.success {
  background-color: #4caf50;
}

.notification.error {
  background-color: #f44336;
}
</style>