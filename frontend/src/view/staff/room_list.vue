<template>
  <div class="container">
    <div class="headContent">
      <h1>Room Record</h1>
    </div>
    <div class="actions">
      <button @click="showAddRoomPopup = true">
        <VsxIcon iconName="AddCircle" size="20" type="bold"/>
        Add Room
      </button>
    </div>

    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th class="center">No</th>
          <th>Room</th>
          <th class="center">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(room, index) in rooms" :key="index">
          <td class="center">{{ index + 1 }}</td>
          <td>{{ room.roomNumber }}</td>
          <td class="center">
            <div class="icon-group">
              <VsxIcon iconName="Edit2" :size="25" color="#171717" type="linear" @click="editRoomPopup(room)"/>
              <VsxIcon iconName="Slash" :size="25" color="#171717" type="linear" @click="deleteRoom(room.roomId)"/>
            </div>
          </td>
        </tr>
        <tr v-if="rooms.length === 0">
          <td colspan="8" class="center">No record.</td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Add Room Popup -->
    <div v-if="showAddRoomPopup" class="popup-overlay">
      <div class="popup">
        <div class="popup-title">
          <h2>Add Room</h2>
        </div>
        <form @submit.prevent="addRoom">
          <div class="form-group">
            <label for="roomNumber">Room Number <span class="required">*</span></label>
            <input type="number" id="roomNumber" v-model="newRoom.roomNumber" required/>
          </div>
          <div class="actions">
            <button class="btn-cancel" @click="closePopup">Cancel</button>
            <button type="submit">Add</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Edit Room Popup -->
    <div v-if="showEditRoomPopup" class="popup-overlay">
      <div class="popup">
        <div class="popup-title">
          <h2>Edit Room - {{ editRoom.roomNumber }}</h2>
        </div>
        <form @submit.prevent="updateRoom">
          <div class="form-group">
            <label for="editRoomNumber">Room Number <span class="required">*</span></label>
            <input type="number" id="editRoomNumber" v-model="editRoom.roomNumber" required/>
          </div>
          <div class="actions">
            <button class="btn-cancel" @click="closePopup">Cancel</button>
            <button type="submit">Update</button>
          </div>
        </form>
      </div>
    </div>

    <div v-if="notification.message" :class="['notification', notification.type]">
      {{ notification.message }}
    </div>

  </div>
</template>

<script>
import axios from "axios";
import { VsxIcon } from "vue-iconsax";

export default {
  name: "RoomRecord",
  components: {
    VsxIcon,
  },
  data() {
    return {
      rooms: [],
      isLoading: false,
      showAddRoomPopup: false,
      showEditRoomPopup: false,
      newRoom: {
        roomNumber: ''
      },
      editRoom: {
        roomNumber: '' // Room name to edit
      },
      notification: {
        message: "",
        type: "" // "success" or "error"
      },
    };
  },
  methods: {
    async fetchRooms() {
      this.isLoading = true;
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-all-room`,
            { headers: { Authorization: `Bearer ${token}` } }
        );

        if (response.data.code === 0) {
          this.rooms = response.data.result; // Access result directly
          this.totalElements = this.rooms.length;
          this.totalPages = Math.ceil(this.totalElements / this.itemsPerPage);
        } else {
          console.error("Error: Unexpected response code", response.data.code);
        }
      } catch (error) {
        console.error("Error fetching rooms:", error);
      } finally {
        this.isLoading = false;
      }
    },

    // Close popup
    closePopup() {
      this.showAddRoomPopup = false;
      this.showEditRoomPopup = false;
      this.newRoom = { roomNumber: '' };
      this.editRoom = { roomNumber: '' };
      this.editRoomId = null;
    },

    // Add new room
    async addRoom() {
      if (!this.newRoom.roomNumber) {
        return;
      }

      try {
        const token = sessionStorage.getItem('jwtToken');
        await axios.post(
            'http://localhost:8088/fja-fap/staff/create-room',
            { roomNumber: this.newRoom.roomNumber },
            { headers: { Authorization: `Bearer ${token}` } }
        );

        this.closePopup();
        await this.fetchRooms(); // No need for `.then(r)`
        this.showNotification("Room added successfully!", "success");
      } catch (error) {
        console.error('Error adding room:', error);
        this.showNotification("Error adding room. Please try again.", "error");
      }
    },

    // Show Edit Room Popup
    editRoomPopup(room) {
      this.editRoomId = room.roomId; // Get room ID for editing
      this.editRoom.roomNumber = room.roomNumber;
      this.showEditRoomPopup = true;
    },

    // Update room
    async updateRoom() {
      if (!this.editRoom.roomNumber || this.editRoom.roomNumber <= 0) {
        return;
      }

      try {
        const token = sessionStorage.getItem('jwtToken');
        await axios.post(
            `http://localhost:8088/fja-fap/staff/update-room/${this.editRoomId}`,
            { roomNumber: this.editRoom.roomNumber },
            { headers: { Authorization: `Bearer ${token}` } }
        );

        this.closePopup();
        await this.fetchRooms();
        this.showNotification("Room updated successfully!", "success");
      } catch (error) {
        console.error('Error updating room:', error);
        this.showNotification("Error updating room. Please try again.", "error");
      }
    },

    // Delete room
    async deleteRoom(roomId) {
      if (!confirm("Are you sure you want to delete this room?")) {
        return;
      }

      try {
        const token = sessionStorage.getItem('jwtToken');
        await axios.delete(
            `http://localhost:8088/fja-fap/staff/delete-room/${roomId}`,
            { headers: { Authorization: `Bearer ${token}` } }
        );

        this.showNotification("Room deleted successfully!", "success");
        await this.fetchRooms(); // Refresh room list after deletion
      } catch (error) {
        console.error('Error deleting room:', error);
        this.showNotification("Error deleting room. Please try again.", "error");
      }
    },

    // Show notification
    showNotification(message, type) {
      this.notification = { message, type };
      setTimeout(() => {
        this.notification.message = "";
      }, 3000); // Clears the message after 3 seconds
    }
  },

  mounted() {
    this.fetchRooms();
  }
};
</script>

<style scoped>
</style>
