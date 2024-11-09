<template>
  <div class="container">
    <div class="headContent">
      <h1>Time Slot List</h1>
    </div>
    <div class="actions">
      <button @click="showAddModal = true">
        <VsxIcon iconName="AddCircle" size="20" type="bold" />
        Add time slot
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
    <div class="table-container">
      <table>
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
    </div>

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
          { headers: { Authorization: `Bearer ${token}` } }
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
          { headers: { Authorization: `Bearer ${token}` } }
        );

        // Refresh the slots list after adding a new one
        await this.fetchTimeslot();

        // Close the modal and reset form data
        this.showAddModal = false;
        this.newSlot = { name: '', startTime: '', endTime: '' };

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
      this.newSlot = { name: '', startTime: '', endTime: '' };
    },
    showNotification(message, type) {
      this.notification = { message, type };
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

<style lang="scss" scoped></style>
