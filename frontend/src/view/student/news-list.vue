<template>
  <div class="container">
    <div class="headContent">
      <h1>News List</h1>
    </div>

    <div class="news-container" v-if="newsList.length">
      <div class="news" v-for="news in newsList" :key="news.newId">
        <p class="date">{{ formatDate(news.createDate) }}</p>
        <router-link :to="`/student/news-detail/${news.newId}`">{{ news.newTitle }}</router-link>
      </div>
    </div>
    <div v-else>
      <p>No news available</p>
    </div>
  </div>
</template>


<script>
import axios from "axios";

export default {
  data() {
    return {
      newsList: [],
      token: "your-auth-token",
    };
  },
  methods: {
    fetchNews() {
      const token = sessionStorage.getItem("jwtToken");
      axios
          .get("http://localhost:8088/fja-fap/student/get-all-publish-news", {
            headers: {
              Authorization: `Bearer ${token}`,
            },
            params: {
              page: 0,
              size: 20,
            },
          })
          .then((response) => {
            if (response.data && response.data.result) {
              this.newsList = response.data.result.content || [];
            }
          })
          .catch((error) => {
            console.error("Failed to fetch news:", error);
          });
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      if (isNaN(date)) {
        return "Invalid Date";
      }
      const options = { year: "numeric", month: "long", day: "numeric" };
      return date.toLocaleDateString("en-US", options);
    }

  },
  mounted() {
    this.fetchNews(); // Fetch news when the component is mounted
  },
};
</script>

<style lang="scss" scoped>
.news-container{
display: flex;
flex-direction: column;
gap: 20px;
.news{
    display: flex;
    flex-direction: row;
    gap: 20px;

    .date{
        font-style: italic;
        color: var(--gray-2)
    }

    a{
        text-decoration: none;

        &:active{
            color: #6ECBB8;
        }

        &:visited{
            color: none;
        }
    }
}
}

</style>