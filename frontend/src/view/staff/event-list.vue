<template>
  <div class="container">
    <div class="headContent">
      <h1>Event List</h1>
    </div>
    
    <div class="actions">
      <button @click="showAddEventPopup = true">
        <VsxIcon iconName="AddCircle" size="20" type="bold"/>
        Add Event
      </button>
    </div>

    <div class="actions">
      <div class="search-container">
        <input type="text" placeholder="Search..." class="search-field">
        <VsxIcon iconName="SearchNormal1" color="#ADB5BD" type="linear" />
      </div>
    </div>

    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th class="center">No</th>
          <th>Title</th>
          <th>Destination</th>
          <th>Information</th>
          <th>Status</th>
          <th>Avg Rate</th>
          <th class="center">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(event, index) in events" :key="event.eventId">
          <td class="center">{{ index + 1 }}</td>
          <td>{{ event.eventName }}</td>
          <td style="width: 250px">{{ event.address }}</td>
          <td style="width: 550px">
            <span v-if="!isExpanded[index]">
              {{ shortenText(event.description) }}
                <span v-if="event.description.length > maxDescriptionLength" class="expand-text"
                      @click="toggleExpand(index)"> Expand</span></span>
            <span v-else>
                {{ event.description }}
                <span class="expand-text" @click="toggleExpand(index)"> Collapse </span>
                </span>
          </td>
          <td :class="event.status ? 'status-finished' : 'status-pending'">
            {{ event.status ? 'Finished' : 'Not happen' }}
          </td>
          <td>{{ event.avgRate !== null ? event.avgRate : 'N/A' }}</td>
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

    <div v-if="showAddEventPopup" class="popup-overlay">
      <div class="popup">
        <div class="popup-title">
          <h2>Add Event</h2>
        </div>
        <form @submit.prevent="addEvent">
          <div class="form-group">
            <label for="eventName">Event title <span class="required">*</span></label>
            <input type="text" id="eventName" v-model="newBatch.eventName" required/>
          </div>
          <div class="form-group">
            <label for="description">Information <span class="required"> </span></label>
            <textarea id="description" v-model="newBatch.description" rows="6"></textarea>
          </div>
          <div class="form-group">
            <label for="address">Destination <span class="required">*</span></label>
            <input type="text" id="address" v-model="newBatch.address" required/>
          </div>
          <div>
            <label for="image">Image <span class="required">*</span></label>
            <div class="custom-file-input">
              <label class="file-label" for="image">Select image</label>
              <input type="file" id="image" name="image" @change="handleFileChange">
              <span v-if="fileName">{{ fileName }}</span>
            </div>
          </div>
          <div class="actions">
            <button class="btn-cancel" @click="closePopup">Cancel</button>
            <button type="submit">Create</button>
          </div>
        </form>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </div>
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
      events: [],
      currentPage: 1,
      itemsPerPage: 5,
      totalElements: 0,
      totalPages: 0,
      displayedPages: [],
      isLoading: false,
      showAddEventPopup: false,
      newBatch: {
        eventName: '',
        address: '',
        description: ''
      },
      notification: {
        message: '',
        type: ''
      },
      errorMessage: '',
      maxDescriptionLength: 100, // Giới hạn số ký tự hiển thị
      isExpanded: {} // Trạng thái mở rộng hoặc rút gọn cho từng mục
    };
  },
  methods: {
    async fetchEvents() {
      this.isLoading = true;
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/event?page=${this.currentPage - 1}&size=${this.itemsPerPage}`,
            {headers: {Authorization: `Bearer ${token}`}}
        );
        this.events = response.data.result.content;
        this.totalElements = response.data.result.totalElements;
        this.totalPages = Math.ceil(this.totalElements / this.itemsPerPage);
        this.updateDisplayedPages();

        // Khởi tạo trạng thái rút gọn cho từng mục
        this.isExpanded = this.events.reduce((acc, event, index) => {
          acc[index] = false; // ban đầu tất cả mục đều rút gọn
          return acc;
        }, {});
      } catch (error) {
        console.error("Error fetching events:", error);
        this.showNotification("Error fetching events. Please try again.", 'error');
      } finally {
        this.isLoading = false;
      }
    },
    shortenText(text) {
      if (text.length > this.maxDescriptionLength) {
        return text.slice(0, this.maxDescriptionLength);
      }
      return text;
    },
    toggleExpand(index) {
      this.isExpanded[index] = !this.isExpanded[index];
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
        await this.fetchEvents();
      }
    },
    closePopup() {
      this.showAddEventPopup = false;
      this.newBatch = {eventName: '', address: '', description: ''};
    },
    async addEvent() {
      try {
        const token = sessionStorage.getItem('jwtToken');
        await axios.post(
            'http://localhost:8088/fja-fap/staff/create-event',
            this.newBatch,
            {headers: {Authorization: `Bearer ${token}`}}
        );
        this.closePopup();
        this.fetchEvents();

        this.showNotification('Event added successfully!', 'success');
      } catch (error) {
        console.error('Error creating batch:', error);
        this.showNotification(error.response?.data?.message || "Error creating Event. Please try again.", 'error');
      }
    },
    showNotification(message, type) {
      this.notification = {message, type};
      setTimeout(() => {
        this.notification.message = "";
      }, 3000);
    },
    handleFileChange(event) {
      const file = event.target.files[0];
      this.fileName = file ? file.name : 'No file selected';
    }
  },
  mounted() {
    this.fetchEvents();
  }
};
</script>


<style scoped>
.expand-text {
  color: blue;
  cursor: pointer;
  text-decoration: underline;
}

#description {
  width: 250px;
}


.custom-file-input {
  position: relative;
  display: inline-block;
}

.custom-file-input input[type="file"] {
  display: none;
}

.file-label {
  display: inline-block;
  padding: 8px 15px;
  margin-left: 55px;
  border: 1px solid black;
  border-radius: 5px;
  background-color: white;
  cursor: pointer;
}

.file-label:hover {
  background-color: #d4d4d4;
}

</style>
