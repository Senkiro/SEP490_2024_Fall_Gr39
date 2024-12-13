<template>
  <div class="container">
    <div class="headContent">
      <h1>{{ news.newContent || "News Detail" }}</h1>
    </div>

    <div v-if="isLoading">
      <p>Loading news details...</p>
    </div>
    <div v-else>
      <div v-html="news.newTitle"></div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { useRoute } from "vue-router";

export default {
  data() {
    return {
      news: {}, // Initialized as an object
      isLoading: true, // Added loading state
      token: "your-auth-token",
    };
  },
  methods: {
    async fetchNews() {
      const route = useRoute(); // Access route parameters
      const token = sessionStorage.getItem("jwtToken");

      try {
        const response = await axios.get(
            "http://localhost:8088/fja-fap/student/get-news",
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
              params: {
                newsId: route.params.id,
              },
            }
        );

        if (response.data && response.data.result) {
          this.news = response.data.result;
        } else {
          alert("No news details found.");
        }
      } catch (error) {
        console.error("Error fetching news detail:", error);
        alert("Failed to fetch news detail. Please try again.");
      } finally {
        this.isLoading = false; // Stop the loading indicator
      }
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      if (isNaN(date)) {
        return "Invalid Date";
      }
      const options = { year: "numeric", month: "long", day: "numeric" };
      return date.toLocaleDateString("en-US", options);
    },

  },
  mounted() {
    this.fetchNews(); // Fetch news when the component is mounted
  },
};
</script>

<style lang="scss" scoped>
.headContent {
  i {
    color: #555555;
  }
}
</style>
