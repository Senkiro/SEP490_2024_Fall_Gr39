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
          <input
              type="text"
              placeholder="Search..."
              class="search-field"
              v-model="searchQuery"
              @input="searchEvents"
          />
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
            <span v-html="shortenText(event.description)"></span>
            <span
                v-if="event.description.length > maxDescriptionLength"
                class="expand-text"
                @click="toggleExpand(index)">
              Expand
            </span>
            </span>
              <span v-else>
            <span v-html="event.description"></span>
            <span
                class="expand-text"
                @click="toggleExpand(index)">
              Collapse
              </span>
            </span>
            </td>
            <td :class="event.status ? 'status-finished' : 'status-pending'">
              {{ event.status ? 'Finished' : 'Not happen' }}
            </td>
            <td>{{ event.avgRate !== null ? event.avgRate : 'N/A' }}</td>
            <td class="center">
              <div class="icon-group">
                <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear" @click="viewEventDetail(event)"/>
              </div>
            </td>
          </tr>
          <tr v-if="events.length === 0">
            <td colspan="8" class="center">No record.</td>
          </tr>
          </tbody>
        </table>
  
        <div class="pagination" v-if="totalPages > 0">
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
              <label for="address">Destination <span class="required">*</span></label>
              <input type="text" id="address" v-model="newBatch.address" required/>
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
  import {VsxIcon} from "vue-iconsax";
  
  export default {
    components: {VsxIcon},
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
        maxDescriptionLength: 100,
        isExpanded: {},
        searchQuery: '' // Thêm biến cho từ khóa tìm kiếm
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
  
          this.isExpanded = this.events.reduce((acc, event, index) => {
            acc[index] = false;
            return acc;
          }, {});
        } catch (error) {
          console.error("Error fetching events:", error);
          this.showNotification("Error fetching events. Please try again.", 'error');
        } finally {
          this.isLoading = false;
        }
      },
      async searchEvents() {
        if (this.searchQuery.trim() === "") {
          this.fetchEvents();
          return;
        }
        this.isLoading = true;
        try {
          const token = sessionStorage.getItem('jwtToken');
          const response = await axios.get(
              `http://localhost:8088/fja-fap/staff/search-event?name=${this.searchQuery}&page=${this.currentPage - 1}&size=${this.itemsPerPage}`,
              { headers: { Authorization: `Bearer ${token}` } }
          );
          const result = response.data.result || {};
          this.events = result.content || [];
          this.totalElements = result.totalElements || this.events.length;
          this.totalPages = Math.ceil(this.totalElements / this.itemsPerPage);
          this.updateDisplayedPages();
        } catch (error) {
          console.error("Error searching events:", error);
          this.showNotification("Error searching events. Please try again.", 'error');
        } finally {
          this.isLoading = false;
        }
      },
      async deleteEvent(eventId) {
        if (!confirm("Are you sure you want to delete this event?")) {
          return;
        }
  
        try {
          const token = sessionStorage.getItem('jwtToken');
          await axios.delete(
              `http://localhost:8088/fja-fap/staff/delete-event/${eventId}`,
              { headers: { Authorization: `Bearer ${token}` } }
          );
  
          this.showNotification("Event deleted successfully!", "success");
          await this.fetchEvents();
        } catch (error) {
          console.error('Error deleting room:', error);
          this.showNotification("Error deleting event. Please try again.", "error");
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
      viewEventDetail(event) {
        console.log("Event ID:", event.eventId);
        this.$router.push({ name: 'EventDetail', params: { eventId: event.eventId } });
      },
      showNotification(message, type) {
        this.notification = {message, type};
        setTimeout(() => {
          this.notification.message = "";
        }, 3000);
      }
    },
    mounted() {
      this.fetchEvents();
    }
  };
  </script>
  
  <style scoped>
  </style>
  