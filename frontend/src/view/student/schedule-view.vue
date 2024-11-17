<template>
    <div class="container">
        <div class="headContent">
            <h1>Schedule</h1>
        </div>

        <div class="filters">
            <select id="week-filter" class="filter-select">
                <option value="1">Week 1 (2/9/2024 - 8/9/2024)</option>
                <option value="2">Week 2 (9/9/2024 - 15/9/2024)</option>
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
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>
                            <div class="schedule-date">
                                <h1>3</h1>
                                <p>Monday</p>
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
                                        <b>Chapter 2 - Lesson 1</b>
                                        <span>Exam: <b>Chapter 1 - Grammar</b></span>
                                        <span>Teacher: <b>Yuri Ikeda</b></span>
                                        <b class="attended">Attended</b>
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td></td>
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

            .schedule-date {
                display: flex;
                flex-direction: column;
                align-items: center;
                gap: 10px;

                h1 {
                    margin: 0;
                }
            }
        }
    }
}

.activities-container {
    display: flex;
    flex-direction: column;
    gap: 20px;

    .activity {
        display: flex;
        flex-direction: row;
        gap: 20px;
        color: #1A2C6F;
        cursor: context-menu;

        b {
            cursor: pointer;
        }

        .thumbnail {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            background-color: #3E5DD4;
            color: #fff;
            padding: 10px;
            gap: 5px;
            width: 80px;
            height: 80px;
            border-radius: 20px;

            #lesson {
                font-size: 16px;
            }

            #number {
                font-size: 30px;
                font-weight: bold;
            }
        }

        .information {
            display: flex;
            flex-direction: column;
            justify-content: center;
            gap: 3px;
        }

    }
}
</style>