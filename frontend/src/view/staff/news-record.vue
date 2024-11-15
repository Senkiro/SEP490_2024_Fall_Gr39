<template>
  <div class="container">
    <div class="headContent">
      <h1>News List</h1>


      <div class="actions">
        <button @click="openAddNewsPopup">
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


    <div v-if="showAddNewsPopup" class="popup-overlay">
      <div class="popup">
        <div class="popup-title">
          <h2>Add News</h2>
        </div>
        <form @submit.prevent="addNews">
          <div class="form-group">
            <label for="newsTitle">Title <span class="required">*</span></label>
            <input type="text" id="newsTitle" v-model="newNews.title" required />
          </div>
          <div class="form-group">
            <label for="date">Date created <span class="required">*</span></label>
            <input type="date" id="date" v-model="newNews.dateCreated" required />
          </div>
          <div class="form-group">
            <label for="createdBy">Created by <span class="required">*</span></label>
            <input type="text" id="createdBy" v-model="newNews.createdBy" required />
          </div>
          <div class="actions">
            <button class="btn btn-cancel" @click="showAddNewsPopup = false">Cancel</button>
            <button type="submit">Create</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";
import axios from "axios";

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
      newNews: {
        title: '',
        dateCreated: '',
        createdBy: ''
      },
      news: [
        { id: 1, title: "Tech Updates", dateCreated: "2024-11-01", createdBy: "Admin" },
        { id: 2, title: "Event at FPT", dateCreated: "2024-11-02", createdBy: "Editor" }
      ],
      showAddNewsPopup: false
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
    openAddNewsPopup() {
      this.showAddNewsPopup = true;
    },
    async addNews() {
      try {
        const response = await axios.post("/api/news", this.newNews);
        this.news.push(response.data);
        this.showAddNewsPopup = false;
        this.newNews = {title: '', dateCreated: '', createdBy: ''};
      } catch (error) {
        console.error("Error adding news:", error);
      }
    },
    changePage(page) {
      this.currentPage = page;
    }
  }
};
</script>
