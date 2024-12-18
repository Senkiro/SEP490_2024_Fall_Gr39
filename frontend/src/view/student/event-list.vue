<template>
  <div class="container">
    <div class="headContent">
      <h1>Event list</h1>
    </div>

    <div class="actions">
      <div class="search-container">
        <input type="text" placeholder="Search..." class="search-field" v-model="searchQuery"
               @input="searchEvents"/>
        <VsxIcon iconName="SearchNormal1" color="#ADB5BD" type="linear"/>
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
          <th class="center">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(event, index) in events" :key="event.eventId">
          <td class="center">{{ index + 1 }}</td>
          <td>{{ event.eventName }}</td>
          <td>{{ event.address }}</td>
          <td>
            <div v-html="event.description"></div>
          </td>
          <td class="center">
            <div class="icon-group">
              <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear"
                       @click="viewEventDetail(event)"/>
            </div>
          </td>
        </tr>
        <tr v-if="events.length === 0">
          <td colspan="5" class="center">No record.</td>
        </tr>
        </tbody>
      </table>

      <div class="pagination" v-if="totalPages > 1">
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
      itemsPerPage: 10,
      totalElements: 0,
      totalPages: 0,
      displayedPages: [],
      isLoading: false,
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
      searchQuery: ''
    };
  },
  methods: {
    async fetchEvents() {
      this.isLoading = true;
      try {
        const token = sessionStorage.getItem('jwtToken');
        const classId = JSON.parse(sessionStorage.getItem('studentInfo')).classResponse.classId;

        // Gọi API
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-events-of-one-class?class_id=${classId}`,
            {headers: {Authorization: `Bearer ${token}`}}
        );

        // Gán danh sách sự kiện từ `result` của API
        this.events = response.data.result || [];

        // Tính toán thông tin phân trang nếu cần
        this.totalElements = this.events.length;
        this.totalPages = Math.ceil(this.totalElements / this.itemsPerPage);
        this.updateDisplayedPages();

        // Khởi tạo trạng thái mở rộng cho từng sự kiện
        this.isExpanded = this.events.reduce((acc, event, index) => {
          acc[index] = false;
          return acc;
        }, {});
      } catch (error) {
        console.error("Error fetching events:", error);
        //this.showNotification("Error fetching events. Please try again.", 'error');
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
            {headers: {Authorization: `Bearer ${token}`}}
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
    viewEventDetail(event) {
      console.log("Event ID:", event.eventId);
      switch (this.$route.name) {
        case "StudentEventList":
          this.$router.push({name: 'StudentEventDetail', params: {id: event.eventId}});
          break;
        case "TeacherEventList":
          this.$router.push({name: 'TeacherEventDetail', params: {id: event.eventId}});
          break;
      }
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

<style scoped></style>