<template>
    <div class="container">
        <div class="headContent">
            <h1>Schedule</h1>
        </div>

        <div class="filters">
            <select id="week-filter" class="filter-select" v-model="selectedClass">
                <option value="">All Class</option>
                <option v-for="classItem in classList" :key="classItem.id" :value="classItem.name">
                    {{ classItem.name }}
                </option>
            </select>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th class="center">Date</th>
                        <th class="center">8:30 - 12:30</th>
                        <th class="center">13:30 - 17:30</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>
                            <div class="schedule-date">
                                <h1>2</h1>
                                <p>Sunday</p>
                            </div>
                        </td>
                        <td>
                            <div class="activities-container">
                                <div class="activity">
                                    <div class="thumbnail">
                                        <p id="lesson">Lesson</p>
                                        <p id="number">1</p>
                                    </div>

                                    <div class="information">
                                        <b>Chapter 1 - Lesson 1</b>
                                        <span>Exam: <b></b></span>
                                        <span>Class: <b>Red</b></span>
                                        <span>Room: <b>102</b></span>
                                        <div class="status-button">
                                            <button class="table-button">View attendance</button>
                                            <p class="attended">Attendance taken</p>
                                        </div>
                                        <div class="status-button">
                                            <button class="table-button">View mark</button>
                                            <p class="attended">Mark added</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

    </div>
</template>

<script>
export default {

}
</script>

<style lang="scss" scoped>
.table-container {
    table {
        th {
            text-align: center;
        }

        tr,
        td {
            border: 2px solid #DFE7FB;

            tr:nth-child(2),
            tr:nth-child(3),
            td:nth-child(2),
            td:nth-child(3) {
                width: 45%;
                font-weight: normal;
            }
        }

        td {
            padding: 30px 0;
            color: #171717;
        }
    }
}
</style>