<template>
    <div class="container">
        <div class="headContent">
            <h1>Holiday list</h1>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>No</th>
                        <th>Holiday</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(holiday, index) in holidayList" :key="holiday.id">
                        <td>{{ index + 1 }}</td>
                        <td>{{ holiday.title }}</td>
                        <td>{{ formatDate(holiday.date) }}</td>
                    </tr>
                    <tr v-if="holidayList.length === 0">
                        <td colspan="3" class="center">No record.</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    data() {
        return {
            holidayList: [],
            token: "your-auth-token",
        };
    },
    methods: {
        fetchHoliday() {
            const token = sessionStorage.getItem("jwtToken");
            axios
                .get("http://localhost:8088/fja-fap/student/get-all-holiday", {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                    params: {
                        page: 0,
                        size: 100,
                    },
                })
                .then((response) => {
                    if (response.data && response.data.result) {
                        this.holidayList = response.data.result.content
                            .map((item) => ({
                                id: item.holidayId,
                                title: item.title,
                                date: item.holidayDate,
                            }))
                            .sort((a, b) => new Date(a.date) - new Date(b.date)); // Sắp xếp tăng dần
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
        this.fetchHoliday();
    },
};
</script>

<style lang="scss" scoped>
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