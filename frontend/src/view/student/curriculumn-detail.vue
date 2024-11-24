<template>
    <div class="container">
        <div class="headContent">
            <h1>Dekiru Nihongo Pre-Intermediate</h1>
        </div>

        <div class="btn-group">
            <button @click="switchTab('curriculumn')" :class="{ 'active-tab': activeTab === 'curriculumn' }">
                Curriculumn
            </button>
            <button @click="switchTab('lesson')" :class="{ 'active-tab': activeTab === 'lesson' }">
                Lesson
            </button>
            <button @click="switchTab('exam')" :class="{ 'active-tab': activeTab === 'exam' }">
                Exam
            </button>
        </div>

        <div v-if="activeTab === 'curriculumn'" class="record">
            <div class="table-container">
                <table id="curriculumn">
                    <thead>
                        <tr>
                            <th class="center">Session</th>
                            <th>Lesson</th>
                            <th>Exam</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="center">1</td>
                            <td>2</td>
                            <td>3</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div v-if="activeTab === 'lesson'" class="record">
            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th class="center">No</th>
                            <th>Lesson</th>
                            <th class="center">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="center">1</td>
                            <td>Chapter 1: Lesson 1</td>
                            <td class="center">
                                <div class="icon-group">
                                    <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear"
                                        @click="viewLessonDetail()" />
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

        <div v-if="activeTab === 'exam'" class="record">
            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th class="center">No</th>
                            <th>Exam</th>
                            <th class="center">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="center">1</td>
                            <td>Chapter 1: Vocabulary</td>
                            <td class="center">
                                <div class="icon-group">
                                    <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear"
                                        @click="viewExamDetail()" />
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

    </div>
</template>

<script>
export default {
    data() {
        return {
            activeTab: 'curriculumn',
        }
    },
    methods: {
        switchTab(tab) {
            this.activeTab = tab;
        },
        viewLessonDetail() {
            this.$router.push({ name: 'StudentLessonDetail' });
        },
        viewExamDetail() {
            this.$router.push({ name: 'StudentExamDetail' });
        },
    }

}
</script>

<style lang="scss" scoped>
.container {
    .btn-group {
        display: flex;
        flex-direction: row;
        padding: 5px 5px;
        width: fit-content;
        border-radius: 10px;
        background: linear-gradient(to right, #1A2C6F, #304CB2);

        button {
            background: none;
            padding: 10px 40px;
            border-radius: 6px;
            color: #fff;
            font-weight: semibold;
        }

        .active-tab {
            background: #fff;
            color: #1A2C6F;
        }
    }

    .record {
        display: flex;
        flex-direction: column;
        gap: 20px;
    }
}

table {

    tr,
    td {
        font-weight: normal !important;
    }
}

#curriculumn {
    th {
        text-align: center;
    }

    tr,
    td {

        tr:nth-child(2),
        tr:nth-child(3),
        td:nth-child(2),
        td:nth-child(3) {
            width: 45%;
        }
    }

    td {
        color: #171717;
    }
}
</style>