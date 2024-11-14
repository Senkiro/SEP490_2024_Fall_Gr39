<template>
    <div class="container">
        <div class="headContent">
            <h1>Exam list</h1>
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
                        <th>Chapter</th>
                        <th class="center">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr style="cursor: pointer;" @click="toggleExpand()">
                        <td class="center">1</td>
                        <td>Chapter 1: Introduction to Dekiru</td>
                        <td class="center">
                            <div class="icon-group">
                                <VsxIcon iconName="Edit2" :size="25" color="#171717" type="linear" />
                                <VsxIcon iconName="Slash" :size="25" color="#171717" type="linear" />
                            </div>
                        </td>
                    </tr>
                    <template v-if="isExpanded">
                        <tr class="expandable-rows">
                            <td class="center"></td>
                            <td class="tab-index">Chapter 1: Vocabulary</td>
                            <td class="center">
                                <div class="icon-group">
                                    <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear" />
                                    <VsxIcon iconName="Slash" :size="25" color="#171717" type="linear" />
                                </div>
                            </td>
                        </tr>
                        <tr class="expandable-rows">
                            <td class="center"></td>
                            <td class="tab-index">Chapter 1: Kanji</td>
                            <td class="center">
                                <div class="icon-group">
                                    <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear" />
                                    <VsxIcon iconName="Slash" :size="25" color="#171717" type="linear" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="center"></td>
                            <td class="tab-index">Chapter 1: Grammar</td>
                            <td class="center">
                                <div class="icon-group">
                                    <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear" />
                                    <VsxIcon iconName="Slash" :size="25" color="#171717" type="linear" />
                                </div>
                            </td>
                        </tr>
                    </template>
                    <tr style="cursor: pointer;" @click="toggleExpand()">
                        <td class="center">2</td>
                        <td>Chapter 2: Introduction to Dekiru</td>
                        <td class="center">
                            <div class="icon-group">
                                <VsxIcon iconName="Edit2" :size="25" color="#171717" type="linear" />
                                <VsxIcon iconName="Slash" :size="25" color="#171717" type="linear" />
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="pagination">
                <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">‹</button>
                <button v-for="page in displayedPages" :key="page" :class="{ active: page === currentPage }"
                    @click="changePage(page)">
                    {{ page }}
                </button>
                <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">›</button>
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
        }
    }

}
</script>

<style lang="scss" scoped></style>