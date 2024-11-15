<template>
  <div class="container">
    <div class="headContent">
      <h1>News List</h1>


      <div class="actions">
        <button @click="goToCreateNews">
          <VsxIcon iconName="AddCircle" size="20" type="bold" />
          Add News
        </button>
      </div>

      <div class="table-container">
        <table>
          <thead>
          <tr>
            <th class="center">No</th>
            <th>Title</th>
            <th>Date created</th>
            <th>Created by</th>
            <th class="center">Action</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(newsItem, index) in filteredNews" :key="newsItem.id">
            <td class="center">{{ index + 1 }}</td>
            <td>{{ newsItem.title }}</td>
            <td>{{ formatDate(newsItem.dateCreated) }}</td>
            <td>{{ newsItem.createdBy }}</td>
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
          <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">
            <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717" />
          </button>
          <button v-for="page in displayedPages" :key="page" :class="{ active: page === currentPage }" @click="changePage(page)">
            {{ page }}
          </button>
          <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">
            <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717" />
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";

export default {
  name: "NewsList",
  components: {
    VsxIcon
  },
  data() {
    return {
      activeTab: 'news',
      currentPage: 1,
      itemsPerPage: 5,
      news: [
        { id: 1, title: "Tech Updates", dateCreated: "2024-11-01", createdBy: "Admin" },
        { id: 2, title: "Event at FPT", dateCreated: "2024-11-02", createdBy: "Editor" }
      ]
    };
  },
  computed: {
    filteredNews() {
      return this.news;
    },
    displayedPages() {
      const totalPages = Math.ceil(this.news.length / this.itemsPerPage);
      let pages = [];
      for (let i = 1; i <= totalPages; i++) {
        pages.push(i);
      }
      return pages;
    },
    totalPages() {
      return Math.ceil(this.news.length / this.itemsPerPage);
    }
  },
  methods: {
    formatDate(date) {
      return new Date(date).toLocaleDateString();
    },
    goToCreateNews() {
      this.$router.push({ name: 'CreateNews' });
    },
    changePage(page) {
      this.currentPage = page;
    }
  }
};
</script>
