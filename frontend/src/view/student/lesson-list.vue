<template>
    <div class="container">
        <div class="headContent">
            <h1>Lesson list</h1>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th class="center">No</th>
                        <th>Chapter</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="center">1</td>
                        <td class="chapter" @click="toggleExpand()">Chapter 1: Introduction to Dekiru</td>
                    </tr>
                    <template v-if="isExpanded">
                        <tr class="expandable-rows">
                            <td class="center"></td>
                            <td class="tab-index">Chapter 1: Lesson 1</td>
                        </tr>
                        <tr class="expandable-rows">
                            <td class="center"></td>
                            <td class="tab-index">Chapter 1: Lesson 2</td>

                        </tr>
                        <tr>
                            <td class="center"></td>
                            <td class="tab-index">Chapter 1: Lesson 3</td>
                        </tr>
                    </template>
                    <tr>
                        <td class="center">2</td>
                        <td class="chapter" @click="toggleExpand()">Chapter 2: Introduction to Dekiru</td>

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
            isExpanded: true
        }
    },
    methods: {
        toggleExpand() {
            this.isExpanded = !this.isExpanded;
        },
        viewLessonDetail() {
            this.$router.push({ name: 'LessonDetail' });
        },
    }

}
</script>

<style lang="scss" scoped>
.container {
    .table-container {
        table {
            tr {
                .chapter, .tab-index {
                    cursor: pointer;
                }

                td:nth-child(2) {
                    font-weight: normal;
                }
            }
        }
    }
}
</style>