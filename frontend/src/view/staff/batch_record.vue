<template>
  <div class="container">
    <div class="headContent">
      <div class="pageTitle">
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
        <td id="id" style="font-weight: bold;">{{ index + 1 }}</td>
        <td id="name" style="font-weight: bold;" @click="viewBatchDetail(batchEntity)">{{ batchEntity.batchName }}</td>
        <td id="year">{{ batchEntity.year }}</td>
        <td id="startTime">{{ batchEntity.startTime }}</td>
        <td id="endTime">{{ batchEntity.endTime }}</td>
        <td id="numberOfStudents" style="text-align: center;">{{ batchEntity.numberOfStudents }}</td>
        <td id="status" :class="{'status-progress': batchEntity.status === 'On progress', 'status-graduated': batchEntity.status === 'Graduated'}">
          {{ batchEntity.status }}
        </td>
        <td id="action">
          <VsxIcon iconName="Eye" :size="24" color="#171717" type="linear" @click="viewBatchDetail(batchEntity)" style="padding-left: 5px;"/>
        </td>
      </tr>
      </tbody>
    </table>

    <div v-if="showAddBatchPopup" class="popup-overlay">
      <div class="popup">
        <h2>Add Batch Entity</h2>
        <form @submit.prevent="addBatch">
          <div class="form-group">
            <label for="batchName">Batch name <span class="required">*</span></label>
            <input type="text" id="batchName" v-model="newBatch.name" required />
          </div>
          <div class="form-group">
            <label for="startTime">Start time <span class="required">*</span></label>
            <input type="date" id="startTime" v-model="newBatch.startTime" required />
          </div>
          <div class="form-group">
            <label for="endTime">End time</label>
            <input type="date" id="endTime" v-model="newBatch.endTime" />
          </div>
          <button type="submit" class="btn btn-create">Create</button>
          <button type="button" class="btn btn-cancel" @click="showAddBatchPopup = false">Cancel</button>
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
    VsxIcon
  },
  data() {
    return {
      batches: [],
      showAddBatchPopup: false,
      newBatch: {
        name: "",
        startTime: "",
        endTime: ""
      },
      errorMessage: ""
    };
  },
  mounted() {
    this.fetchBatches();
  },
  methods: {
    async fetchBatches() {
      try {
        const response = await axios.get('http://localhost:8088/fja-fap/staff/batch');
        this.batches = response.data.result;
      } catch (error) {
        console.error('Error fetching batches:', error);
        this.errorMessage = "Error fetching batches. Please try again.";
      }
    },
    viewBatchDetail(batchEntity) {
      // Logic for navigating to batchEntity detail page
      this.$router.push({ name: 'BatchDetail', params: { batchId: batchEntity.id } });
    },
    async addBatch() {
      // Validate start and end time
      if (this.newBatch.startTime && this.newBatch.endTime && new Date(this.newBatch.startTime) > new Date(this.newBatch.endTime)) {
        this.errorMessage = "Start time must be before end time.";
        return;
      }

      try {
        const response = await axios.post('http://localhost:8088/fja-fap/staff/save-batch', {
          batchName: this.newBatch.name,
          startTime: this.newBatch.startTime,
          endTime: this.newBatch.endTime,
        });
        this.batches.push(response.data.result); // Thêm batch mới vào danh sách
        this.showAddBatchPopup = false;
        this.newBatch = { name: "", startTime: "", endTime: "" };
        this.errorMessage = ""; // Clear error message
      } catch (error) {
        console.error('Error creating batch:', error);
        this.errorMessage = "Error creating batch. Please try again.";
      }
    }
  }
};
</script>

<style scoped>
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
</style>

