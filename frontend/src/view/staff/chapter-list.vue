<template>
    <div class="container">
        <div class="headContent">
            <h1>Chapter list</h1>
        </div>

        <div class="actions">
            <button @click="showAddChapterPopup = true">
                <VsxIcon iconName="AddCircle" size="20" type="bold" />
                Add chapter
            </button>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th class="center">No</th>
                        <th>Chapter</th>
                        <th class="center">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="center">1</td>
                        <td>Chapter 1: Introduction to Dekiru</td>
                        <td class="center">
                            <div class="icon-group">
                                <VsxIcon iconName="Edit2" :size="25" color="#171717" type="linear" />
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="center">2</td>
                        <td>Chapter 2: Introduction to Dekiru</td>
                        <td class="center">
                            <div class="icon-group">
                                <VsxIcon iconName="Edit2" :size="25" color="#171717" type="linear" />
                            </div>
                        </td>
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

        <div v-if="showAddChapterPopup" class="popup-overlay">
            <div class="popup">
                <div class="popup-title">
                    <h2>Add Chapter</h2>
                </div>
                <form @submit.prevent="addChapter">
                    <div class="form-group">
                        <label for="chapterName">Name <span class="required">*</span></label>
                        <input type="text" id="chapterName" required />
                    </div>
                    <div class="actions">
                        <button class="btn-cancel" @click="confirmCancel">Cancel</button>
                        <button type="submit"> Create</button>
                    </div>
                </form>
                <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
            </div>
        </div>
    </div>
</template>

<script>
import { VsxIcon } from 'vue-iconsax';

export default {
    components: {
        VsxIcon
    },
    data() {
        return {
            isExpanded: true,
            showAddChapterPopup: false
        }
    },
    methods: {
        toggleExpand() {
            this.isExpanded = !this.isExpanded;
        },
        viewExamDetail() {
            this.$router.push({ name: 'ExamDetail' });
        },
        confirmCancel() {
            this.showAddChapterPopup = false;
        },
    }

}
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