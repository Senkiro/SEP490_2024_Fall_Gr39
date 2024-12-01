<template>
    <div class="container">
        <div class="headContent">
            <h1>Curriculumn list</h1>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th class="center">No</th>
                        <th>Curriculumn</th>
                        <th class="center">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(curriculumn, index) in curriculumnList" :key="curriculumn.id">
                        <td class="center">{{ index + 1 + (currentPage - 1) * 10 }}</td>
                        <td>{{ curriculumn.curriculumnTitle }}</td>
                        <td class="center">
                            <div class="icon-group">
                                <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear"
                                    @click="navigateToCurriculumnDetail(curriculumn.curriculumnListId)" />
                            </div>
                        </td>
                    </tr>
                    <tr v-if="curriculumnList.length === 0">
                        <td colspan="3" class="center">No record.</td>
                    </tr>
                </tbody>
            </table>
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

        <div v-if="addCurriculumnPopup" class="popup-overlay">
            <div class="popup">
                <div class="exit-icon">
                    <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold"
                        @click="addCurriculumnPopup = false" />
                </div>
                <div class="popup-title">
                    <h2>Add Curriculumn</h2>
                </div>
                <form @submit.prevent="addCurriculumn">
                    <div class="form-group">
                        <label for="curriculumnName">Name <span class="required">*</span></label>
                        <input type="text" id="curriculumnName" v-model="newCurriculumnTitle" required />
                    </div>
                    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
                    <p v-if="successMessage" class="success">{{ successMessage }}</p>
                    <div class="actions">
                        <button class="btn-submit" type="submit" :disabled="isSubmitting">Create</button>
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
    components: {
        VsxIcon,
    },
    data() {
        return {
            isExpanded: true,
            addCurriculumnPopup: false,
            curriculumnList: [],
            newCurriculumnTitle: "",
            errorMessage: "",
            successMessage: "",
            totalPages: 0,
            currentPage: 1,
            itemsPerPage: 10,
            isSubmitting: false,
            notification: {
                message: "",
                type: ""
            },
        };
    },
    methods: {
        navigateToCurriculumnDetail(id) {
            this.$router.push({
                name: "TeacherCurriculumnDetail",
                params: { id },
            });
        },

        async fetchCurriculumnList() {
            try {
                const token = sessionStorage.getItem("jwtToken");
                const response = await axios.get(
                    `http://localhost:8088/fja-fap/staff/get-all-curriculumn-list?page=${this.currentPage - 1}&size=${this.itemsPerPage}`,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    }
                );

                if (response.data.code === 0) {
                    // Correctly map the API response to the curriculumnList
                    this.curriculumnList = response.data.result || [];
                    this.totalPages = Math.ceil(this.curriculumnList.length / this.itemsPerPage); // Calculate total pages if pagination data isn't provided
                } else {
                    console.error("Failed to fetch data:", response.data);
                }
            } catch (error) {
                console.error("Error fetching curriculumn list:", error);
            }
        },
        changePage(page) {
            if (page > 0 && page <= this.totalPages) {
                this.currentPage = page;
                this.fetchCurriculumnList();
            }
        },
    },
    mounted() {
        this.fetchCurriculumnList();
    },
};
</script>


<style lang="scss" scoped>
.container {
    .table-container {
        table {
            tr {
                td:nth-child(2) {
                    font-weight: normal;
                }

            }

        }
    }
}

</style>