<template>
    <div class="container">
      <div class="headContent">
        <h1>News Detail</h1>
      </div>

      <div v-html="news"></div>
    </div>
  </template>
  
  
  <script>
  import axios from "axios";
  import { ref, onMounted } from "vue";
  import { useRoute } from "vue-router";
  
  export default {
    name: "NewsDetail",
    setup() {
      const route = useRoute(); // Lấy ID từ route
      const news = ref({
        newId: "",
        newTitle: "",
        newContent: "",
        status: false, // false = Draft, true = Published
        createDate: "",
        createdBy: "",
      });
      const isLoading = ref(false);
  
      // Lấy dữ liệu từ API
      const fetchNewsDetail = async () => {
        try {
          const token = sessionStorage.getItem("jwtToken");
          const response = await axios.get(
              `http://localhost:8088/fja-fap/staff/get-news?newsId=${route.params.id}`,
              {
                headers: { Authorization: `Bearer ${token}` },
              }
          );
          if (response.data && response.data.result) {
            news.value = response.data.result;
          }
        } catch (error) {
          console.error("Error fetching news detail:", error);
          alert("Failed to fetch news detail. Please try again.");
        }
      };
      onMounted(() => {
        fetchNewsDetail();
      });
  
      return { news, isLoading};
    },
  };
  </script>
  
  
  
  <style scoped>
  </style>
  