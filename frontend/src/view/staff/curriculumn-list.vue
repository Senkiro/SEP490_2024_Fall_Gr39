<template>
    <div class="container">
        <div class="headContent">
            <h1>Curriculumn list</h1>
        </div>

        <div class="actions">
            <button @click="addCurriculumnPopup = true">
                <VsxIcon iconName="AddCircle" size="20" type="bold" />
                Add curriculumn
            </button>
            <button @click="navigateToImportCurriculumn">
                <VsxIcon iconName="Import" size="20" type="bold" />
                Import curriculumn
            </button>
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
                    <tr>
                        <td class="center">1</td>
                        <td>Dekiru Nihongo Pre-Intermediate</td>
                        <td class="center">
                            <div class="icon-group">
                                <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear"
                                    @click="navigateToCurriculumnDetail" />
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
                        <input type="text" id="curriculumnName" required />
                    </div>
                    <div class="actions">
                        <button type="submit"> Create</button>
                    </div>
                </form>
                <!-- <p v-if="errorMessage" class="error">{{ errorMessage }}</p> -->

            </div>
        </div>

        <!-- <div v-if="notification.message" :class="['notification', notification.type]">
            {{ notification.message }}
        </div> -->
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
            addCurriculumnPopup: false
        }
    },
    methods: {
        navigateToImportCurriculumn() {
            this.$router.push({ name: 'ImportCurriculumnPage' });
        },
        navigateToCurriculumnDetail() {
            this.$router.push({
                name: "CurriculumnDetail",
                params: { id: 1 },
            });
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