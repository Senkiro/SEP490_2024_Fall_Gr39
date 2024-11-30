<template>
  <div class="container">
    <div class="headContent">
      <h1>Time Slot List</h1>
    </div>

    <!-- Update Slot Modal -->
    <div v-if="showUpdateModal" class="popup-overlay">
      <div class="popup">
        <div class="exit-icon">
          <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="closeUpdateModal"/>
        </div>
        <h2>Update Time Slot {{ selectedSlot.name }}</h2>
        <form @submit.prevent="submitUpdateTimeSlot">
          <div class="form-group">
            <label for="startTime">Start Time</label>
            <input
                type="time"
                v-model="selectedSlot.startTime"
                id="startTime"
                required
            />
          </div>

          <div class="form-group">
            <label for="endTime">End Time</label>
            <input
                type="time"
                v-model="selectedSlot.endTime"
                id="endTime"
                required
            />
          </div>

          <div class="actions">
            <button type="submit" class="btn btn-create">Save</button>
          </div>
        </form>
      </div>
    </div>

    <div class="table-container">
      <table>
        <thead class="center">
          <tr>
            <th class="center">No</th>
            <th>Slot</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th class="center">Actions</th>
          </tr>
        </thead>
        <tbody class="center">
          <tr v-for="(slot, index) in slots" :key="slot.id">
            <td class="center">{{ index + 1 }}</td>
            <td>{{ slot.name }}</td>
            <td>{{ formatTime(slot.startTime) }}</td>
            <td>{{ formatTime(slot.endTime) }}</td>
            <td class="center">
              <div class="icon-group">
                <VsxIcon iconName="Edit2" :size="25" color="#171717" type="linear" @click="openUpdateModal(slot)"/>
              </div>
            </td>
          </tr>
          <tr v-if="slots.length === 0">
            <td colspan="5" class="center">No record.</td>
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
import {VsxIcon} from "vue-iconsax";

export default {
  components: {VsxIcon},
  data() {
    return {
      slots: [],
      showAddModal: false,
      showUpdateModal: false,
      newSlot: {
        name: '',
        startTime: '',
        endTime: '',
      },
      selectedSlot: {
        id: '',
        name: '',
        startTime: '',
        endTime: '',
      },
      notification: {
        message: '',
        type: '',
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
    openUpdateModal(slot) {
      this.selectedSlot = { ...slot }; // Gán dữ liệu slot vào selectedSlot
      this.showUpdateModal = true;
    },
    closeUpdateModal() {
      this.selectedSlot = { id: '', name: '', startTime: '', endTime: '' };
      this.showUpdateModal = false;
    },
    async submitUpdateTimeSlot() {
      if (!this.selectedSlot.name) {
        this.showNotification('Time slot name cannot be null', 'error');
        return;
      }
      if (!this.selectedSlot.startTime || !this.selectedSlot.endTime) {
        this.showNotification('Start time and end time must be in the format HH:mm', 'error');
        return;
      }

      // Ensure endTime is greater than startTime
      if (this.selectedSlot.endTime <= this.selectedSlot.startTime) {
        this.showNotification('End time must be later than start time', 'error');
        return;
      }

      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.post(
            `http://localhost:8088/fja-fap/staff/update-time-slot/${this.selectedSlot.id}`,
            {
              name: this.selectedSlot.name,
              startTime: this.selectedSlot.startTime,
              endTime: this.selectedSlot.endTime,
            },
            { headers: { Authorization: `Bearer ${token}` } }
        );

        if (response.data.code === 0) {
          this.showNotification('Time slot updated successfully!', 'success');
          this.fetchTimeslot(); // Cập nhật danh sách
          this.closeUpdateModal();
        } else {
          this.showNotification('Failed to update time slot. Please try again.', 'error');
        }
      } catch (error) {
        console.error('Error updating time slot:', error);
        this.showNotification(
            error.response?.data?.message || 'Error updating time slot. Please try again.',
            'error'
        );
      }
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
