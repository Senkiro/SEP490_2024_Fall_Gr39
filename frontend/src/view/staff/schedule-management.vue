<template>
    <div class="container">
        <div class="headContent">
            <h1>Schedule Management</h1>
        </div>

        <div class="filters-actions">
            <div class="filters">
                <select id="batch-filter" class="filter-select">
                    <option value="">Batch</option>
                    <option value="FALL2024">FALL2024</option>
                    <option value="SUMMER2024">SUMMER2024</option>
                    <option value="SPRING2024">SPRING2024</option>
                </select>

                <select id="class-filter" class="filter-select">
                    <option value="">Class</option>
                    <option value="Blue">Blue</option>
                    <option value="Red">Red</option>
                    <option value="Green">Green</option>
                    <option value="Yellow">Yellow</option>
                    <option value="Purple">Purple</option>
                </select>
            </div>

            <div class="actions">
                <button>
                    <VsxIcon iconName="AddCircle" size="20" type="bold" />
                    Create new schedule
                </button>
                <button>
                    <VsxIcon iconName="Trash" size="20" type="bold" />
                    Delete schedule
                </button>
            </div>
        </div>

        <div class="table-container">
            <div class="filters">
                <select id="week-filter" class="filter-select">
                    <option value="">Week 1 (2/9/2024 - 8/9/2024)</option>
                    <option value="">Week 2 (9/9/2024 - 15/9/2024)</option>
                    <option value="">Week 3 (16/9/2024 - 22/9/2024)</option>
                </select>
            </div>

            <table>
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Slot</th>
                        <th>Teacher</th>
                        <th>Room</th>
                        <th>Lesson</th>
                        <th>Exam</th>
                        <th>Event</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>
                            <div class="schedule-date">
                                <h2>2</h2>
                                <p>Sunday</p>
                            </div>
                        </td>
                        <td>
                            <div class="filters">
                                <select id="slot-filter" class="filter-select">
                                    <option value="">7:30 - 12:30</option>
                                    <option value="">13:30 - 17:30</option>
                                </select>
                            </div>
                        </td>
                        <td>
                            <div class="filters">
                                <select id="teacher-filter" class="filter-select">
                                    <option value="">Yuri Ikeda</option>
                                    <option value="">Nakamura Shinsudei</option>
                                </select>
                            </div>
                        </td>
                        <td>
                            <div class="filters">
                                <select id="room-filter" class="filter-select">
                                    <option value="">101</option>
                                    <option value="">102</option>
                                </select>
                            </div>
                        </td>
                        <td>
                            <div class="filters">
                                <select id="lesson-filter" class="filter-select">
                                    <option value="">Chapter 1 - Lesson 1</option>
                                    <option value="">Chapter 1 - Lesson 2</option>
                                </select>
                            </div>
                        </td>
                        <td>
                            <div class="filters">
                                <select id="exam-filter" class="filter-select">
                                    <option value="">Chapter 1 - Vocabulary</option>
                                    <option value="">Chapter 1 - Kanji</option>
                                </select>
                            </div>
                        </td>
                        <td>
                            <VsxIcon iconName="AddCircle" :size="25" color="#dae4f3" type="bold"
                                @click="openEventListPopup" />
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <div class="schedule-date">
                                <h2>3</h2>
                                <p>Tuesday</p>
                            </div>
                        </td>
                        <td>
                            <div class="filters">
                                <select id="slot-filter" class="filter-select">
                                    <option value="">7:30 - 12:30</option>
                                    <option value="">13:30 - 17:30</option>
                                </select>
                            </div>
                        </td>
                        <td>
                            <div class="filters">
                                <select id="teacher-filter" class="filter-select">
                                    <option value="">Yuri Ikeda</option>
                                    <option value="">Nakamura Shinsudei</option>
                                </select>
                            </div>
                        </td>
                        <td>
                            <div class="filters">
                                <select id="room-filter" class="filter-select">
                                    <option value="">101</option>
                                    <option value="">102</option>
                                </select>
                            </div>
                        </td>
                        <td>
                            <div class="filters">
                                <select id="lesson-filter" class="filter-select">
                                    <option value="">Chapter 1 - Lesson 1</option>
                                    <option value="">Chapter 1 - Lesson 2</option>
                                </select>
                            </div>
                        </td>
                        <td>
                            <div class="filters">
                                <select id="exam-filter" class="filter-select">
                                    <option value="">Chapter 1 - Vocabulary</option>
                                    <option value="">Chapter 1 - Kanji</option>
                                </select>
                            </div>
                        </td>
                        <td>
                            <VsxIcon iconName="AddCircle" :size="25" color="#dae4f3" type="bold"
                                @click="openEventListPopup" />
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div v-if="showEventListPopup" class="popup-overlay">
            <div class="popup">
                <div class="exit-icon">
                    <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold"
                        @click="showEventListPopup = false" />
                </div>
                <div class="popup-title">
                    <h2>Pick an event</h2>
                </div>

                <form @submit.prevent="addEvent">
                    <div class="radio">
                        <input type="radio" value="1" name="event-list" />
                        <label for="1">Trip to Tokyo Tower</label>
                    </div>
                    <div class="radio">
                        <input type="radio" value="2" name="event-list" />
                        <label for="2">Kabukicho</label>
                    </div>
                    <div class="radio">
                        <input type="radio" value="3" name="event-list" />
                        <label for="3">Event with long title so that I can test the width</label>
                    </div>
                    <div class="actions">
                        <!-- <button class="btn btn-cancel" @click="showAddStudentPopup = false">Cancel</button> -->
                        <button type="submit">Add</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";

export default {
    name: "BatchDetail",
    components: {
        VsxIcon
    },
    data() {
        return {
            showEventListPopup: false
        };
    },
    methods: {
        openEventListPopup() {
            this.showEventListPopup = true;
        }
    }
};
</script>

<style lang="scss" scoped>
.filters-actions {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}

.table-container {
    .filters {
        margin: 0;
    }

    table {
        th {
            text-align: center;
        }

        td {
            text-align: center;
            padding: 30px 0;

            .filters {
                align-items: center;
                justify-content: center;
                width: 100%;
                height: 100%;
            }

            .schedule-date {
                display: flex;
                flex-direction: column;
                align-items: center;

                h2 {
                    margin: 10px 0;
                }
            }
        }
    }
}

.radio {
    display: flex;
    flex-direction: row;
    gap: 20px;

    margin: 20px 0;
}
</style>