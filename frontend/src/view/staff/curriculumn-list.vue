<template>
    <div class="container">
        <div class="headContent">
            <h1>Curriculumn list</h1>
        </div>

        <div class="actions">
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
                                <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear" />
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
        navigateToImportCurriculumn() {
      this.$router.push({name: 'ImportCurriculumnPage'});
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