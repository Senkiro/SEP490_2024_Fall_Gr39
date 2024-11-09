<template>
  <div class="time-slot-container">
    <h1>Time Slot List</h1>
    <div class="button-container">
      <button class="add-time-slot" @click="showAddModal = true">
        <VsxIcon iconName="AddCircle" size="20" type="bold" class="add-icon" />
        <i class="icon-add"></i> Add time slot
      </button>
    </div>

    <!-- Add Time Slot Modal -->
    <div v-if="showAddModal" class="popup-overlay">
      <div class="popup">
        <h2>Add New Time Slot</h2>
        <form @submit.prevent="submitNewTimeSlot">
          <div class="form-group">
            <label for="slotName">Slot Name</label>
            <input type="text" v-model="newSlot.name" id="slotName" required />
          </div>

          <div class="form-group">
            <label for="startTime">Start Time</label>
            <input type="time" v-model="newSlot.startTime" id="startTime" required />
          </div>

          <div class="form-group">
            <label for="endTime">End Time</label>
            <input type="time" v-model="newSlot.endTime" id="endTime" required />
          </div>

          <div class="button-group">
            <button type="submit" class="btn btn-create">Save</button>
            <button type="button" class="btn btn-cancel" @click="confirmCancel">Cancel</button>
          </div>
        </form>
      </div>
    </div>

    <table class="slot-table">
      <thead>
      <tr>
        <th>No</th>
        <th>Slot</th>
        <th>Start Time</th>
        <th>End Time</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(slot, index) in slots" :key="slot.id">
        <td>{{ index + 1 }}</td>
        <td>{{ slot.name }}</td>
        <td>{{ formatTime(slot.startTime) }}</td>
        <td>{{ formatTime(slot.endTime) }}</td>
        <td class="action-buttons">
          <button class="icon-button" aria-label="Edit time slot">
            <VsxIcon iconName="Edit" :size="20" color="#5584FF" type="linear" />
          </button>
          <button class="icon-button" aria-label="Delete time slot">
            <VsxIcon iconName="Slash" :size="20" color="#5584FF" type="linear" />
          </button>
        </td>
      </tr>
      </tbody>
    </table>

    <div v-if="notification.message" :class="['notification', notification.type]">
      {{ notification.message }}
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      slots: [],
      showAddModal: false,
      newSlot: {
        name: '',
        startTime: '',
        endTime: ''
      },
      isLoading: false,
      notification: {
        message: "",
        type: "" // "success" or "error"
      },
    };
  },
  methods: {
    async fetchTimeslot() {
      this.isLoading = true;
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/time-slot-list`,
            {headers: {Authorization: `Bearer ${token}`}}
        );

        // Kiểm tra mã code và cập nhật slots nếu thành công
        if (response.data.code === 0) {
          this.slots = response.data.result.map((slot) => {

            return {
              id: slot.timeSLotId,
              name: slot.name,
              startTime: slot.startTime,
              endTime: slot.endTime,
            };
          });
        } else {
          // Hiển thị thông báo nếu có lỗi
          this.showNotification("Unable to retrieve the time slot list. Please try again.", "error");
        }
      } catch (error) {
        console.error('Error fetching time slots:', error);
        this.showNotification("Error retrieving time slot list. Please try again.", "error");
      } finally {
        this.isLoading = false;
      }
    },
    async submitNewTimeSlot() {
      if (!this.newSlot.name) {
        this.showNotification("Time slot name cannot be null", "error");
        return;
      }
      if (!this.newSlot.startTime || !this.newSlot.endTime) {
        this.showNotification("Start time and end time must be in the format HH:mm", "error");
        return;
      }

      // Ensure endTime is greater than startTime
      if (this.newSlot.endTime <= this.newSlot.startTime) {
        this.showNotification("End time must be later than start time", "error");
        return;
      }

      try {
        const token = sessionStorage.getItem('jwtToken');
        await axios.post(
            'http://localhost:8088/fja-fap/staff/create-time-slot',
            {
              name: this.newSlot.name,
              startTime: this.newSlot.startTime,
              endTime: this.newSlot.endTime,
            },
            {headers: {Authorization: `Bearer ${token}`}}
        );

        // Refresh the slots list after adding a new one
        await this.fetchTimeslot();

        // Close the modal and reset form data
        this.showAddModal = false;
        this.newSlot = {name: '', startTime: '', endTime: ''};

        // Show success notification
        this.showNotification("Time slot created successfully!", "success");
      } catch (error) {
        console.error('Error creating time slot:', error);
        this.showNotification(
            error.response?.data?.message || "Error creating time slot. Please try again.",
            'error'
        );
      }
    },
    confirmCancel() {
      this.showAddModal = false;
      this.newSlot = {name: '', startTime: '', endTime: ''};
    },
    showNotification(message, type) {
      this.notification = {message, type};
      setTimeout(() => {
        this.notification.message = "";
      }, 3000);
    },
    formatTime(time) {
      return time ? time.slice(0, 5) : '';
    },
  },

  created() {
    this.fetchTimeslot();
  }
};
</script>

<style scoped>
.time-slot-container {
  padding: 30px;
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

.button-container {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 15px;
}

.add-time-slot {
  background-color: #007bff;
  border: none;
  color: #ffffff;
  padding: 10px 20px;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  border-radius: 5px;
}

.add-icon {
  margin-right: 10px;
}

.slot-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.slot-table th,
.slot-table td {
  border: none;
  padding: 8px;
  text-align: center;
}

.slot-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}

.action-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
}

.icon-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  margin: 0 10px;
}

.icon-button:last-child {
  margin-right: 0;
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
