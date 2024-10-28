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
        <td id="name" style="font-weight: bold;" @click="viewBatchDetail(batchEntity)">{{ batchEntity.name }}</td>
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
        <h2>Add batchEntity</h2>
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
      </div>
    </div>
  </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";

export default {
  name: "BatchRecord",
  components: {
    VsxIcon
  },
  data() {
    return {
      batches: [
        { id: 1, name: "FALL2024", year: 2024, startTime: "2/9/2024", endTime: "31/11/2024", numberOfStudents: 300, status: "On progress" },
        { id: 2, name: "SUMMER2024", year: 2024, startTime: "x/5/2024", endTime: "x/7/2024", numberOfStudents: 216, status: "Graduated" },
        { id: 3, name: "SPRING2024", year: 2024, startTime: "x/1/2024", endTime: "x/3/2024", numberOfStudents: 234, status: "Graduated" },
        { id: 4, name: "FALL2023", year: 2023, startTime: "2/9/2023", endTime: "31/11/2023", numberOfStudents: 188, status: "Graduated" },
        { id: 5, name: "SUMMER2023", year: 2023, startTime: "x/5/2023", endTime: "x/7/2023", numberOfStudents: 125, status: "Graduated" },
        { id: 6, name: "SPRING2023", year: 2023, startTime: "x/1/2023", endTime: "x/3/2023", numberOfStudents: 147, status: "Graduated" }
      ],
      showAddBatchPopup: false,
      newBatch: {
        name: "",
        startTime: "",
        endTime: ""
      }
    };
  },
  methods: {
    viewBatchDetail(batchEntity) {
      // Logic for navigating to batchEntity detail page
      this.$router.push({ name: 'BatchDetail', params: { batchId: batchEntity.id } });
    },
    addBatch() {
      const newBatch = {
        id: this.batches.length + 1,
        name: this.newBatch.name,
        year: new Date(this.newBatch.startTime).getFullYear(),
        startTime: this.newBatch.startTime,
        endTime: this.newBatch.endTime,
        numberOfStudents: 0,
        status: "On progress"
      };
      this.batches.push(newBatch);
      this.showAddBatchPopup = false;
      this.newBatch = { name: "", startTime: "", endTime: "" };
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
</style>

<style>
.headContent{
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
  margin: 20px 0px;;
}

.container {
  padding: 20px;
}

button{
  background-image: linear-gradient(90deg, #3E5DD4, #223374);
  padding: 10px, 20px;
  color: #fff;
  display: flex;
  align-items: center;
  padding: 10px 20px;
  font-size: 14px;
  font-weight: normal;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  gap: 10px;
}

.actions{
  display: flex;
  flex-direction: row-reverse;
  gap: 20px;
}

table{
  width: 100%;
  border-collapse: collapse;
}
</style>
