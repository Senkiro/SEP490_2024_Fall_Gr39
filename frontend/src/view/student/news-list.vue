<template>
    <div class="container">
        <div class="headContent">
            <h1>News list</h1>
        </div>

        <div class="actions">
            <div class="search-container">
                <input type="text" placeholder="Search..." class="search-field" v-model="searchQuery"
                    @input="searchNews" />
                <VsxIcon iconName="SearchNormal1" color="#ADB5BD" type="linear" />
            </div>
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

        <div class="pagination" v-if="totalPages > 1">
            <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">
                <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717" />
            </button>
            <button v-for="page in displayedPages" :key="page" :class="{ active: page === currentPage }"
                @click="changePage(page)">
                {{ page }}
            </button>
            <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">
                <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717" />
            </button>
        </div>
    </div>
</template>


<script>
import axios from "axios";

export default {
    data() {
        return {
            currentPage: 1,
            itemsPerPage: 5,
            totalElements: 0,
            totalPages: 0,
            displayedPages: [],
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
                        this.totalElements = response.data.result.totalElements;
                        this.totalPages = Math.ceil(this.totalElements / this.itemsPerPage);
                        this.updateDisplayedPages();
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
        },
        async searchNews() {
            if (this.searchQuery.trim() === "") {
                this.fetchNews();
                return;
            }
            this.isLoading = true;
            try {
                const token = sessionStorage.getItem('jwtToken');
                const response = await axios.get(
                    `http://localhost:8088/fja-fap/staff/search-news?name=${this.searchQuery}&page=${this.currentPage - 1}&size=${this.itemsPerPage}`,
                    { headers: { Authorization: `Bearer ${token}` } }
                );
                const result = response.data.result || {};
                this.newsList = result.content || [];
                this.totalElements = result.totalElements || this.news.length;
                this.totalPages = Math.ceil(this.totalElements / this.itemsPerPage);
                this.updateDisplayedPages();
            } catch (error) {
                console.error("Error searching news:", error);
                this.showNotification("Error searching news. Please try again.", 'error');
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
                await this.fetchNews();
            }
        },
        viewNewsDetail(news) {
            console.log("News ID:", news.newsId);
            switch (this.$route.name) {
                case "StudentNewsList":
                    this.$router.push({ name: 'StudentNewsDetail', params: { id: news.newsId } });
                    break;
                case "TeacherNewsList":
                    this.$router.push({ name: 'TeacherNewsDetail', params: { id: news.newsId } });
                    break;
            }
        },
        showNotification(message, type) {
            this.notification = { message, type };
            setTimeout(() => {
                this.notification.message = "";
            }, 3000);
        }
    },
    mounted() {
        this.fetchNews(); // Fetch news when the component is mounted
    },
};
</script>

<style lang="scss" scoped>
.pagination {
    display: flex;
    flex-direction: row;
    justify-content: center;
    gap: 10px;

    button {
        display: flex;
        width: 40px;
        height: 40px;
        padding: 0px;
        justify-content: center;
        align-content: center;
        border: 1px solid #000000;
        background: none;
        color: #171717;
    }
}

.news-container {
    display: flex;
    flex-direction: column;
    gap: 20px;

    .news {
        display: flex;
        flex-direction: row;
        gap: 20px;

        .date {
            font-style: italic;
            color: var(--gray-2)
        }

        a {
            text-decoration: none;

            &:active {
                color: #6ECBB8;
            }

            &:visited {
                color: none;
            }
        }
    }
}
</style>