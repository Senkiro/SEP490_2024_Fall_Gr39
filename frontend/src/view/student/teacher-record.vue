<template>
    <div class="container">
        <div class="headContent">
            <h1>Teacher Record</h1>
        </div>

        <div class="actions">
            <div class="search-container">
                <input type="text" placeholder="Search..." class="search-field">
                <VsxIcon iconName="SearchNormal1" color="#ADB5BD" type="linear" />
            </div>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th class="center">No</th>
                        <th>Japanese Name</th>
                        <th>Dob</th>
                        <th>Email</th>
                        <th>Gender</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(teacher, index) in teachers" :key="teacher.userId">
                        <td class="center">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
                        <td>{{ teacher.japaneseName }}</td>
                        <td>{{ teacher.dob }}</td>
                        <td>{{ teacher.email }}</td>
                        <td>{{ teacher.gender ? 'Male' : 'Female' }}</td>
                    </tr>
                    <tr v-if="teachers.length === 0">
                        <td colspan="5" class="center">No record.</td>
                    </tr>
                </tbody>
            </table>
            <div class="pagination" v-if="totalPages > 0">
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
    </div>
</template>

<script>
import axios from 'axios';
import { VsxIcon } from "vue-iconsax";

export default {
    name: "TeacherRecord",
    components: {
        VsxIcon
    },
    data() {
        return {
            teachers: [],
            currentPage: 1,
            itemsPerPage: 10,
            totalElements: 0,
            totalPages: 0,
            showAddTeacherPopup: false,
            notification: {
                message: "",
                type: "", // "success" hoặc "error"
            }
        };
    },
    mounted() {
        this.fetchTeachers();
    },
    methods: {
        async fetchTeachers() {
            try {
                const token = sessionStorage.getItem('jwtToken');
                const response = await axios.get(
                    `http://localhost:8088/fja-fap/staff/teacher?page=${this.currentPage - 1}&size=${this.itemsPerPage}`,
                    { headers: { Authorization: `Bearer ${token}` } }
                );
                if (response.data.code === 1000) {
                    this.teachers = response.data.result.teachers;
                    this.totalElements = response.data.result.totalElements;
                    this.totalPages = Math.ceil(this.totalElements / this.itemsPerPage);
                    this.updateDisplayedPages();
                } else {
                    this.showNotification('Không thể tải danh sách giáo viên: ' + response.data.message, 'error');
                }
            } catch (error) {
                console.error('Lỗi khi lấy dữ liệu giáo viên:', error);
                this.showNotification('Đã xảy ra lỗi khi tải danh sách giáo viên.', 'error');
            }
        },
        changePage(newPage) {
            if (newPage > 0 && newPage <= this.totalPages) {
                this.currentPage = newPage;
                this.fetchTeachers();
            }
        },
        showNotification(message, type) {
            this.notification = { message, type };
            setTimeout(() => {
                this.notification.message = "";
            }, 3000);
        },
        updateDisplayedPages() {
            const pages = [];
            if (this.totalPages <= 5) {
                for (let i = 1; i <= this.totalPages; i++) {
                    pages.push(i);
                }
            } else {
                if (this.currentPage <= 3) {
                    pages.push(1, 2, 3, '...', this.totalPages);
                } else if (this.currentPage >= this.totalPages - 2) {
                    pages.push(1, '...', this.totalPages - 2, this.totalPages - 1, this.totalPages);
                } else {
                    pages.push(1, '...', this.currentPage, '...', this.totalPages);
                }
            }
            this.displayedPages = pages;
        }
    },
    watch: {
        // Watcher để cập nhật URL khi `currentPage` thay đổi
        currentPage(newPage) {
            this.$router.push({ path: '/staff/teacher-record', query: { page: newPage } }).catch(() => { });
        }
    }
};
</script>

<style scoped></style>