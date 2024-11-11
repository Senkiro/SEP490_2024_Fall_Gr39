<template>
  <div class="container">
    <div class="headContent">
      <h1>Room List</h1>
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
              <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear"/>
              <VsxIcon iconName="Slash" :size="25" color="#171717" type="linear"/>
            </div>
          </td>
        </tr>
        </tbody>
      </table>

      <div class="pagination">
        <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">‹</button>
        <button v-for="page in displayedPages" :key="page" :class="{ active: page === currentPage }"
                @click="changePage(page)">
          {{ page }}
        </button>
        <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">›</button>
      </div>
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
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      rooms: [],
      currentPage: 1,
      itemsPerPage: 5,
      totalElements: 0,
      totalPages: 0,
      displayedPages: [],
      isLoading: false,
      showAddRoomPopup: false,
      newRoom: {
        roomNumber: ''
      }
    };
  },
  methods: {

    async fetchRooms() {
      this.isLoading = true;
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/room?page=${this.currentPage - 1}&size=${this.itemsPerPage}`,
            { headers: { Authorization: `Bearer ${token}` } }
        );
        this.rooms = response.data.result.content;
        this.totalElements = response.data.result.totalElements;
        this.totalPages = Math.ceil(this.totalElements / this.itemsPerPage);
        this.updateDisplayedPages();
      } catch (error) {
        console.error("Error fetching rooms:", error);
      } finally {
        this.isLoading = false;
      }
    },


    updateDisplayedPages() {
      const pages = [];
      for (let i = 1; i <= this.totalPages; i++) {
        pages.push(i);
      }
      this.displayedPages = pages;
    },


    async changePage(page) {
      if (page > 0 && page <= this.totalPages) {
        this.currentPage = page;
        await this.fetchRooms();
      }
    },

    // Đóng cửa sổ popup
    closePopup() {
      this.showAddRoomPopup = false;
      this.newRoom = { roomNumber: '' };
    },

    // Thêm phòng mới
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
        this.fetchRooms().then(r);
      } catch (error) {
        console.error('Error adding room:', error);
      }
    }
  },

  mounted() {
    this.fetchRooms();
  }
};
</script>

<style scoped>
.expand-text {
  color: blue;
  cursor: pointer;
  text-decoration: underline;
}
</style>
