<template>
    <div class="container">
        <div class="headContent">
            <h1>Schedule Management</h1>
        </div>

        <div class="filters-actions">
            <div class="filters">
                <select v-model="selectedBatch" id="batch-filter" class="filter-select"
                    @change="fetchClassesByBatch(selectedBatch.batchName)">
                    <option value="" disabled>Select Batch</option>
                    <option v-for="batch in batches" :key="batch.id" :value="batch">
                        {{ batch.batchName }}
                    </option>
                </select>

                <select id="class-filter" class="filter-select" v-model="selectedClassId">
                    <option value="" disabled>Select Class</option>
                    <option v-for="classItem in classes" :key="classItem.id" :value="classItem.id">
                        {{ classItem.name }}
                    </option>
                </select>

                <div v-if="isLoadingClasses" class="loading-indicator">Loading classes...</div>

            </div>

            <div class="actions">
                <button @click="showAddSchedulePopup = true">
                    <VsxIcon iconName="AddCircle" size="20" type="bold" />
                    Create new schedule
                </button>
                <button>
                    <VsxIcon iconName="Trash" size="20" type="bold" />
                    Delete schedule
                </button>
            </div>
        </div>

        <div class="filters">
            <select id="week-filter" class="filter-select" v-model="selectedWeekIndex" @change="fetchSessions">
                <option value="" disabled>Select Week</option>
                <option v-for="(week, index) in selectedBatch?.weeks" :key="index" :value="index">
                    Week {{ index + 1 }} ({{ week.start }} to {{ week.end }})
                </option>
            </select>
        </div>

        <div class="table-container">
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
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <template v-for="session in sessions" :key="session.sessionId">
                        <!-- Hàng Sáng -->
                        <tr>
                            <!-- Cột Ngày (chỉ hiển thị ở hàng đầu tiên trong ngày) -->
                            <td v-if="!session.morningProcessed" :rowspan="2">
                                <div class="schedule-date">
                                    <h1>{{ new Date(session.date).getDate() }}</h1>
                                    <p>{{ session.dayOfWeek }}</p>
                                </div>
                            </td>
                            <!-- Cột Slot -->
                            <td>Morning</td>
                            <!-- Dữ liệu nếu Slot là Morning -->
                            <td id="teacher">
                                <div v-if="!isEditing[session.sessionId]">
                                    {{ session.fullName || "N/A" }}
                                </div>
                                <div v-else>
                                    <select v-model="selectedTeacher[session.sessionId]"
                                        @change="assignTeacher(session.sessionId, selectedTeacher[session.sessionId])"
                                        class="filter-select">
                                        <option value="" disabled>Select Teacher</option>
                                        <option v-for="teacher in teachers" :key="teacher.id" :value="teacher.id">
                                            {{ teacher.name }}
                                        </option>
                                    </select>
                                </div>
                            </td>

                            <td id="room">
                                <div v-if="!isEditing[session.sessionId]">
                                    {{ session.roomNumber || "N/A" }}
                                </div>
                                <div v-else>
                                    <select v-model="selectedRoomTable[session.sessionId]"
                                        @change="assignRoom(session.sessionId, selectedRoomTable[session.sessionId])"
                                        class="filter-select">
                                        <option value="" disabled>Select Room</option>
                                        <option v-for="room in rooms" :key="room.id" :value="room.id">
                                            Room {{ room.number }}
                                        </option>
                                    </select>
                                </div>
                            </td>


                            <td>{{ session.timeSlotResponse?.name === "Morning" ? session.lessonResponse || "N/A" :
                                "N/A" }}</td>
                            <td>{{ session.timeSlotResponse?.name === "Morning" ? session.examResponse || "N/A" : "N/A"
                                }}</td>
                            <td>{{ session.timeSlotResponse?.name === "Morning" ? session.eventName || "N/A" : "N/A" }}
                            </td>
                            <td>
                                <div v-if="!isEditing[session.sessionId]">
                                    <div class="button-group">
                                        <VsxIcon iconName="Edit2" size="25" type="linear"
                                            @click="toggleEdit(session.sessionId)" />
                                        <VsxIcon iconName="ArrowSwapVertical" size="25" type="linear" />
                                    </div>
                                </div>
                                <div v-else>
                                    <VsxIcon iconName="TickCircle" size="25" type="linear"
                                        @click="toggleEdit(session.sessionId)" />
                                </div>
                            </td>
                        </tr>

                        <!-- Hàng Chiều -->
                        <tr>
                            <!-- Slot Chiều -->
                            <td>Afternoon</td>
                            <!-- Dữ liệu nếu Slot là Afternoon -->
                            <td id="teacher">
                                <div v-if="!isEditing[session.sessionId]">
                                    {{ session.teacherName || "N/A" }}
                                </div>
                                <div v-else>
                                    <select v-model="selectedTeacher[session.sessionId]"
                                        @change="assignTeacher(session.sessionId, selectedTeacher[session.sessionId])"
                                        class="filter-select">
                                        <option value="" disabled>Select Teacher</option>
                                        <option v-for="teacher in teachers" :key="teacher.id" :value="teacher.id">
                                            {{ teacher.name }}
                                        </option>
                                    </select>
                                </div>
                            </td>

                            <td id="room">
                                <div v-if="!isEditing[session.sessionId]">
                                    {{ session.roomName || "N/A" }}
                                </div>
                                <div v-else>
                                    <select v-model="selectedRoomTable[session.sessionId]"
                                        @change="assignRoom(session.sessionId, selectedRoomTable[session.sessionId])"
                                        class="filter-select">
                                        <option value="" disabled>Select Room</option>
                                        <option v-for="room in rooms" :key="room.id" :value="room.id">
                                            Room {{ room.number }}
                                        </option>
                                    </select>
                                </div>
                            </td>
                            <td>{{ session.timeSlotResponse?.name === "Afternoon" ? session.lessonResponse || "N/A" :
                                "N/A" }}</td>
                            <td>{{ session.timeSlotResponse?.name === "Afternoon" ? session.examResponse || "N/A" :
                                "N/A" }}</td>
                            <td>{{ session.timeSlotResponse?.name === "Afternoon" ? session.eventName || "N/A" : "N/A"
                                }}</td>
                            <td>
                                <div v-if="!isEditing[session.sessionId]">
                                    <div class="button-group">
                                        <VsxIcon iconName="Edit2" size="25" type="linear"
                                            @click="toggleEdit(session.sessionId)" />
                                        <VsxIcon iconName="ArrowSwapVertical" size="25" type="linear" />
                                    </div>
                                </div>
                                <div v-else>
                                    <VsxIcon iconName="TickCircle" size="25" type="linear"
                                        @click="toggleEdit(session.sessionId)" />
                                </div>
                            </td>
                        </tr>
                    </template>
                    <tr v-if="sessions.length === 0">
                        <td colspan="8" class="center">No record.</td>
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

        <div v-if="showAddSchedulePopup" class="popup-overlay">
            <div class="popup">
                <div class="popup-title">
                    <h2>Add Schedule</h2>
                </div>
                <form @submit.prevent="createSchedule">
                    <div class="form-group">
                        <label for="endTime">Room <span class="required">*</span></label>
                        <div class="filters">
                            <select id="room-filter" class="filter-select" v-model="selectedRoomId">
                                <option value="" disabled>Select Room</option>
                                <option v-for="room in rooms" :key="room.number" :value="room.number">
                                    Room {{ room.number }}
                                </option>
                            </select>

                            <div v-if="isLoadingRooms" class="loading-indicator">Loading rooms...</div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="year">Slot <span class="required">*</span></label>
                        <div class="filters">
                            <select id="time-slot-filter" class="filter-select" v-model="selectedTimeSlotId">
                                <option value="" disabled>Select Time Slot</option>
                                <option v-for="slot in timeSlots" :key="slot.id" :value="slot.id">
                                    {{ slot.name }} ({{ slot.start }} - {{ slot.end }})
                                </option>
                            </select>
                            <div v-if="isLoadingTimeSlots" class="loading-indicator">Loading time slots...</div>
                        </div>
                    </div>
                    <div class="actions">
                        <button class="btn-cancel" @click="showAddSchedulePopup = false">Cancel</button>
                        <button type="submit"> Create</button>
                    </div>
                </form>
                <p v-if="errorMessage" class="error"></p>
            </div>
        </div>
    </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";
import axios from 'axios';

export default {
    name: "BatchDetail",
    components: {
        VsxIcon
    },
    data() {
        return {
            batches: [],
            classes: [],
            selectedBatch: '',
            showEventListPopup: false,
            showAddSchedulePopup: false,
            isLoadingClasses: false,
            rooms: [],
            selectedRoom: '',
            isLoadingRooms: false,
            timeSlots: [],
            selectedTimeSlot: '',
            isLoadingTimeSlots: false,
            selectedClassId: '',
            selectedRoomId: '',
            selectedTimeSlotId: '',
            sessions: [],
            teachers: [],
            selectedTeacher: {},
            selectedRoomTable: {},
            isEditing: {},
        };
    },
    methods: {
        async fetchBatches() {
            try {
                const token = sessionStorage.getItem('jwtToken');
                const response = await axios.get(
                    `http://localhost:8088/fja-fap/staff/batch`, {
                    params: {
                        page: 0,
                        size: 1000
                    },
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
                );
                this.batches = response.data.result.content.map(batch => {
                    const start = new Date(batch.startTime);
                    const end = new Date(batch.endTime);
                    const weeks = [];

                    let current = new Date(start);
                    while (current <= end) {
                        const weekStart = new Date(current);
                        const weekEnd = new Date(current);
                        weekEnd.setDate(weekEnd.getDate() + 6); // Cộng 6 ngày cho tuần
                        if (weekEnd > end) weekEnd.setDate(end.getDate()); // Giới hạn trong endTime

                        weeks.push({
                            start: weekStart.toISOString().split('T')[0], // Chuyển thành chuỗi định dạng ngày
                            end: weekEnd.toISOString().split('T')[0]
                        });

                        current.setDate(current.getDate() + 7); // Sang tuần tiếp theo
                    }

                    return {
                        ...batch,
                        weeks, // Thêm danh sách tuần vào batch
                    };
                });
            } catch (error) {
                console.error('Error fetching batches:', error);
                this.batches = [];
                // //alert('Không thể tải dữ liệu batch. Vui lòng thử lại sau.');
            }
        },
        async fetchClassesByBatch(batchName) {
            this.isLoadingClasses = true; // Show the loading indicator
            try {
                const token = sessionStorage.getItem('jwtToken');
                const response = await axios.get(
                    `http://localhost:8088/fja-fap/staff/get-class-by-batch`, {
                    params: {
                        batch_name: batchName,
                        page: 0,
                        size: 100,
                    },
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
                );

                // Extract content array from the response
                if (response.data && response.data.result && response.data.result.content) {
                    this.classes = response.data.result.content.map((classItem) => ({
                        id: classItem.classId,
                        name: classItem.className,
                        colour: classItem.classColour,
                    }));
                } else {
                    this.classes = []; // Handle unexpected structure
                    console.error('Unexpected response format', response.data);
                }
            } catch (error) {
                console.error('Error fetching classes:', error);
                this.classes = []; // Clear the classes array in case of error
                // //alert('Failed to fetch class data. Please try again.');
            } finally {
                this.isLoadingClasses = false; // Hide the loading indicator
            }
        },
        async fetchRooms() {
            this.isLoadingRooms = true;
            try {
                const token = sessionStorage.getItem('jwtToken');
                const response = await axios.get(
                    `http://localhost:8088/fja-fap/staff/get-all-room`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
                );

                // Map the API response to the rooms array
                if (response.data && response.data.result) {
                    this.rooms = response.data.result.map((room) => ({
                        id: room.roomId,
                        number: room.roomNumber,
                    }));
                } else {
                    console.error('Unexpected response structure:', response.data);
                    this.rooms = [];
                }
            } catch (error) {
                console.error('Error fetching rooms:', error);
                // //alert('Failed to fetch room data. Please try again.');
                this.rooms = [];
            } finally {
                this.isLoadingRooms = false;
            }
        },
        async fetchTimeSlots() {
            this.isLoadingTimeSlots = true; // Bắt đầu loading
            try {
                const token = sessionStorage.getItem('jwtToken');
                const response = await axios.get(
                    `http://localhost:8088/fja-fap/staff/time-slot-list`,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    }
                );

                // Ánh xạ chính xác từ `timeSLotId` sang `id`
                if (response.data && response.data.result) {
                    this.timeSlots = response.data.result.map((slot) => ({
                        id: slot.timeSLotId, // Lấy từ `timeSLotId` trong response
                        name: slot.name,
                        start: slot.startTime,
                        end: slot.endTime,
                    }));
                } else {
                    console.error('Unexpected response structure:', response.data);
                    this.timeSlots = [];
                }
            } catch (error) {
                console.error('Error fetching time slots:', error);
                //alert('Failed to fetch time slot data. Please try again.');
                this.timeSlots = [];
            } finally {
                this.isLoadingTimeSlots = false; // Kết thúc loading
            }
        },
        async createSchedule() {
            console.log('Class ID:', this.selectedClassId);
            console.log('Room ID:', this.selectedRoomId);
            console.log('Time Slot ID:', this.selectedTimeSlotId);
            if (!this.selectedClassId || !this.selectedRoomId || !this.selectedTimeSlotId) {
                //alert('Please select a class, room, and time slot before submitting.');
                return;
            }

            try {
                const token = sessionStorage.getItem('jwtToken');
                const response = await axios.post(
                    `http://localhost:8088/fja-fap/staff/create-schedule/${this.selectedClassId}`,
                    {
                        timeSlotId: this.selectedTimeSlotId,
                        roomNumber: this.selectedRoomId,
                    },
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    }
                );

                if (response.data && response.data.code === 0) {
                    //alert('Schedule created successfully!');
                    this.selectedRoomId = '';
                    this.selectedTimeSlotId = '';
                } else {
                    //alert('Failed to create the schedule. Please try again.');
                }
            } catch (error) {
                console.error('Error creating schedule:', error);
                //alert('An error occurred while creating the schedule.');
            }
        },
        async fetchSessions() {
            if (this.selectedWeekIndex === "" || !this.selectedClassId) {
                //alert("Please select a week and a class.");
                return;
            }

            try {
                const token = sessionStorage.getItem("jwtToken");
                const response = await axios.get("http://localhost:8088/fja-fap/staff/get-session-week", {
                    params: {
                        week: this.selectedWeekIndex, // Chỉ số tuần
                        class_id: this.selectedClassId, // ID lớp
                    },
                    headers: { Authorization: `Bearer ${token}` },
                });
                this.sessions = response.data.result.map((session) => ({
                    date: session.date,
                    dayOfWeek: session.dayOfWeek,
                    timeSlotResponse: session.timeSlotResponse,
                    fullName: session.fullName,
                    roomNumber: session.roomNumber,
                    lessonResponse: session.lessonResponse,
                    examResponse: session.examResponse,
                    eventName: session.eventName,
                }));
                console.log("Fetched Sessions:", this.sessions);
            } catch (error) {
                console.error("Error fetching sessions:", error);
                //alert("Failed to fetch sessions. Please try again.");
            }
        },
        async fetchTeachers() {
            try {
                const token = sessionStorage.getItem("jwtToken");
                const response = await axios.get("http://localhost:8088/fja-fap/staff/teacher", {
                    params: { page: 0, size: 100 },
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                this.teachers = response.data.result.teachers.map((teacher) => ({
                    id: teacher.id,
                    name: teacher.fullName,
                }));
            } catch (error) {
                console.error("Error fetching teachers:", error);
                //alert("Failed to fetch teacher data. Please try again.");
            }
        },
        toggleEdit(sessionId) {
            // Chuyển đổi trạng thái chỉnh sửa cho từng sessionId
            this.isEditing = {
                ...this.isEditing,
                [sessionId]: !this.isEditing[sessionId],
            };
        },
        assignTeacher(sessionId, teacherId) {
            console.log(`Assign teacher ${teacherId} to session ${sessionId}`);
            // Sau khi gán, có thể chuyển về chế độ xem
            this.toggleEdit(sessionId);
        },
        assignRoom(sessionId, roomId) {
            console.log(`Assign room ${roomId} to session ${sessionId}`);
            // Sau khi gán, có thể chuyển về chế độ xem
            this.toggleEdit(sessionId);
        },
        openEventListPopup() {
            this.showEventListPopup = true;
        }
    },
    mounted() {
        this.fetchBatches();
        this.fetchRooms();
        this.fetchTimeSlots();
        this.fetchTeachers();
    },
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
        .filter-select {
            padding: 5px 10px;
            font-size: 16px;
            border-radius: 10px;
        }

        th {
            text-align: center;
        }

        tr td {
            height: 60px;
        }

        #teacher,
        #room {
            width: 250px;

            .filter-select{
                width: 100%;
            }
        }

        tr,
        td {
            border: 2px solid #DFE7FB;
        }

        td {
            text-align: center;
            padding: 30px 0;
            color: #171717;


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
                gap: 10px;

                h1 {
                    margin: 0;
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