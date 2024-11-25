<template>
  <div class="container">
    <div class="headContent">
      <h1>News List</h1>
    </div>

    <div class="actions">
      <button @click="goToCreateNews">
        <VsxIcon iconName="AddCircle" size="20" type="bold"/>
        Add News
      </button>
    </div>

    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th class="center">No</th>
          <th>Title</th>
          <th>Content</th>
          <th>Date Created</th>
          <th>Date Update</th>
          <th>Created By</th>
          <th>Status</th>
          <th class="center">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(newsItem, index) in paginatedNews" :key="newsItem.newId">
          <td class="center">{{ index + 1 + (currentPage - 1) * itemsPerPage }}</td>
          <td>{{ newsItem.newTitle }}</td>
          <td v-html="newsItem.newContent"></td>
          <td>{{ formatDate(newsItem.createDate) }}</td>
          <td>{{ formatDate(newsItem.updateDate) }}</td>
          <td>{{ newsItem.createdBy || "Unknown" }}</td>
          <td>{{ newsItem.status ? 'Published' : 'Draft' }}</td>
          <td class="center">
            <div class="icon-group">
              <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear"
                  @click="goToNewsDetail(newsItem.newId)"/>
              <VsxIcon iconName="Slash" :size="25" color="#171717" type="linear"
                  @click="confirmDelete(newsItem.newId)"/>
            </div>
          </td>
        </tr>
        </tbody>
      </table>

      <!-- Phân trang -->
      <div class="pagination">
        <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">
          <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717"/>
        </button>
        <button
            v-for="page in displayedPages"
            :key="page"
            :class="{ active: page === currentPage }"
            @click="changePage(page)"
        >
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
import {VsxIcon} from "vue-iconsax";
import axios from "axios";

export default {
  name: "NewsList",
  components: {
    VsxIcon
  },
  data() {
    return {
      currentPage: 1,
      itemsPerPage: 5,
      totalElements: 0,
      totalPages: 1,
      news: [],
      notification: {
        message: "",
        type: "" // "success" hoặc "error"
      }
    };
  },
  computed: {
    paginatedNews() {
      return this.news;
    },
    displayedPages() {
      const pages = [];
      for (let i = 1; i <= this.totalPages; i++) {
        pages.push(i);
      }
      return pages;
    }
  },
  methods: {
    async fetchNews() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-all-news?page=${this.currentPage - 1}&size=${this.itemsPerPage}`,
            {headers: {Authorization: `Bearer ${token}`}}
        );
        const result = response.data.result;
        this.news = result.content;
        this.totalElements = result.totalElements;
        this.totalPages = result.totalPages;
      } catch (error) {
        console.error("Error fetching news:", error);
        this.showNotification("Failed to fetch news data. Please try again.", "error");
      }
    },
    confirmDelete(newsId) {
      if (confirm("Are you sure you want to delete this news?")) {
        this.deleteNews(newsId);
      }
    },
    async deleteNews(newsId) {
      try {
        const token = sessionStorage.getItem("jwtToken");
        await axios.delete(
            `http://localhost:8088/fja-fap/staff/delete-news/${newsId}`,
            {headers: {Authorization: `Bearer ${token}`}}
        );
        this.showNotification("News deleted successfully!", "success");
        this.fetchNews(); // Tải lại danh sách tin tức sau khi xóa
      } catch (error) {
        console.error("Error deleting news:", error);
        this.showNotification("Failed to delete news. Please try again.", "error");
      }
    },
    goToNewsDetail(newsId) {
      console.log("Navigating to news detail with ID:", newsId);
      this.$router.push({
        name: "StaffNewsDetail",
        params: {id: newsId},
      });
    },
    showNotification(message, type) {
      this.notification = {message, type};
      setTimeout(() => {
        this.notification.message = "";
      }, 3000); // Ẩn thông báo sau 3 giây
    },
    formatDate(date) {
      if (!date) return "N/A";
      return new Date(date).toLocaleDateString();
    },
    goToCreateNews() {
      this.$router.push({name: "CreateNews"});
    },
    changePage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
        this.fetchNews();
      }
    }
  },
  mounted() {
    this.fetchNews();
  },
  created() {
    const savedNotification = localStorage.getItem("notification");
    if (savedNotification) {
      this.notification = JSON.parse(savedNotification);
      localStorage.removeItem("notification");
    }
  },
};
</script>


