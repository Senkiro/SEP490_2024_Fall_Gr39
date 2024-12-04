<template>
  <div class="container">
    <div class="headContent">
      <h1>Schedule Management</h1>
    </div>

    <div v-if="notification.message" class="notification" :class="notification.type">
      {{ notification.message }}
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
          <template v-for="(sessions, date) in groupedSessions" :key="date">
            <tr v-if="sessions.some(session => session.note)" :key="date + '-note'">
              <!-- Cột Ngày -->
              <td :rowspan="1">
                <div class="schedule-date">
                  <h1>{{ new Date(date).getDate() }}</h1>
                  <p>{{ sessions[0].dayOfWeek }}</p>
                </div>
              </td>

              <!-- Gộp các cột khi có note -->
              <td colspan="7" class="note-content">
                {{ sessions.find(session => session.note)?.note }}
              </td>
            </tr>
            <template v-else>
              <tr v-for="(session, index) in sessions" :key="session.sessionId">
                <!-- Cột Ngày -->
                <td v-if="index === 0" :rowspan="sessions.length">
                  <div class="schedule-date">
                    <h1>{{ new Date(session.date).getDate() }}</h1>
                    <p>{{ session.dayOfWeek }}</p>
                  </div>
                </td>

                <!-- Hiển thị các cột bình thường -->
                <td>{{ session.timeSlotResponse?.name || "N/A" }}</td>
                <td id="teacher">
                  <template v-if="!isEditing[session.sessionId]">{{ session.fullName || "-" }}</template>
                  <template v-else>
                    <select v-model="selectedTeacher[session.sessionId]" class="filter-select">
                      <option value="" disabled>Select Teacher</option>
                      <option v-for="teacher in teachers" :key="teacher.id" :value="teacher.id">
                        {{ teacher.name }}
                      </option>
                    </select>
                  </template>
                </td>
                <td id="room">
                  <template v-if="!isEditing[session.sessionId]">{{ session.roomNumber || "-" }}</template>
                  <template v-else>
                    <select v-model="selectedRoomTable[session.sessionId]" class="filter-select">
                      <option value="" disabled>Select Room</option>
                      <option v-for="room in rooms" :key="room.number" :value="room.number">
                        {{ room.number }}
                      </option>
                    </select>
                  </template>
                </td>
                <td id="lesson">
                  <div>{{ session.lessonResponse || "-" }}</div>
                </td>
                <td id="exam">
                  <div>{{ session.examResponse || "-" }}</div>
                </td>
                <td id="event">
                  <template v-if="!isEditing[session.sessionId]">{{ session.eventName || "-" }}</template>
                  <template v-else>
                    <select v-model="selectedEventTable[session.sessionId]" class="filter-select">
                      <option value="" disabled>Select Event</option>
                      <option v-for="event in events" :key="event.id" :value="event.id">
                        {{ event.title }}
                      </option>
                    </select>
                  </template>
                </td>
                <td>
                  <template v-if="session.eventName || session.lessonResponse">
                    <div v-if="!isEditing[session.sessionId]" class="icon-group">
                      <VsxIcon iconName="Edit2" size="25" type="linear" @click="toggleEdit(session.sessionId)" />
                      <VsxIcon
                          iconName="ArrowSwapVertical"
                          size="25"
                          type="linear"
                          @click="openChangeDatePopup(session.sessionId)"
                      />

                    </div>
                    <div v-else class="icon-group">
                      <VsxIcon iconName="TickCircle" size="25" type="bold" color="#6ECBB8"
                        @click="editSession(session.sessionId)" />
                      <!-- Cancel Button -->
                      <VsxIcon iconName="CloseCircle" size="25" type="bold" color="#979B9F"
                        @click="cancelEdit(session.sessionId)" />
                    </div>
                  </template>
                </td>
              </tr>
            </template>
          </template>
          <tr v-if="Object.keys(groupedSessions).length === 0">
            <td colspan="8" class="center">No record.</td>
          </tr>
        </tbody>

      </table>
    </div>

    <div v-if="showAddSchedulePopup" class="popup-overlay">
      <div class="popup">
        <div class="exit-icon">
          <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold"
            @click="showAddSchedulePopup = false" />
        </div>
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
            </div>
          </div>
          <div class="form-group">
            <label for="curriculum">Curriculum <span class="required">*</span></label>
            <div class="filters">
              <select id="curriculum-filter" class="filter-select" v-model="selectedCurriculumId">
                <option value="" disabled>Select Curriculum</option>
                <option v-for="curriculum in curriculums" :key="curriculum.curriculumListId"
                  :value="curriculum.curriculumListId">
                  {{ curriculum.curriculumTitle }}
                </option>
              </select>
            </div>
          </div>

          <div class="actions">
            <button class="btn-submit" type="submit"> Create</button>
          </div>
        </form>
        <p v-if="errorMessage" class="error"></p>
      </div>
    </div>

    <div v-if="showChangeDatePopup" class="popup-overlay">
      <div class="popup">
        <div class="exit-icon">
          <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="showChangeDatePopup = false" />
        </div>
        <div class="popup-title">
          <h2>Change date</h2>
        </div>
        <form @submit.prevent="changeDate">
          <!-- Thông tin hiện tại -->
          <b>From:</b>
          <div class="form-group">
            <label>Week: </label>
            <div class="information"> Week 2 (dd/mm/yyyy - dd/mm/yyyy)</div>
          </div>
          <div class="form-group">
            <label>Date: </label>
            <div class="information"> Friday (dd/mm/yyyy)</div>
          </div>
          <div class="form-group">
            <label>Slot: </label>
            <div class="information"> Morning (8:30 - 12:30)</div>
          </div>
          <b>To:</b>
          <div class="form-group">
            <label for="Week">Week <span class="required">*</span></label>
            <div class="filters">
              <select id="week-filter" class="filter-select">
                <!-- Tuần đã qua bị disabled -->
                <option disabled>
                  Week 1 (2/9/2024 - 8/9/2024)
                </option>
                <option>
                  Week 2 (9/9/2024 - 15/9/2024)
                </option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <label for="date">Date <span class="required">*</span></label>
            <div class="filters">
              <select id="date-filter" class="filter-select">
                <!-- Ngày từ hôm nay trở về trước bị disabled -->
                <option disabled>
                  Tuesday, 3/9/2024
                </option>
                <option>
                  Wednesday, 4/9/2024
                </option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <label for="time-slot">Slot <span class="required">*</span></label>
            <div class="filters">
              <select id="time-slot-filter" class="filter-select" v-model="selectedTimeSlotId">
                <!-- Time slot đã có lịch bị disabled -->
                <option v-for="slot in timeSlots" :key="slot.id" :value="slot.id">
                  {{ slot.name }} ({{ slot.start }} - {{ slot.end }})
                </option>
              </select>
            </div>
          </div>

          <div class="actions">
            <button class="btn-submit" type="submit"> Change</button>
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
      rooms: [],
      sessions: [],
      teachers: [],
      curriculums: [],
      timeSlots: [],

      selectedBatch: '',
      selectedTimeSlot: '',
      selectedClassId: '',
      selectedRoomId: '',
      selectedTimeSlotId: '',
      selectedCurriculumId: '',
      selectedEventId: '',

      showChangeDatePopup: false,
      showEventListPopup: false,
      showAddSchedulePopup: false,
      isLoadingClasses: false,
      isLoadingRooms: false,
      isLoadingCurriculums: false,

      selectedTeacher: {},
      selectedRoomTable: {},
      selectedEventTable: {},
      isEditing: {},

      notification: {
        message: "",
        type: "" // "success" or "error"
      },
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
            weekEnd.setDate(weekEnd.getDate() + 6);
            if (weekEnd > end) weekEnd.setDate(end.getDate());

            weeks.push({
              start: weekStart.toISOString().split('T')[0],
              end: weekEnd.toISOString().split('T')[0]
            });

            current.setDate(current.getDate() + 7);
          }

          return {
            ...batch,
            weeks,
          };
        });
      } catch (error) {
        console.error('Error fetching batches:', error);
        this.batches = [];
      }
    },
    async fetchClassesByBatch(batchName) {
      this.isLoadingClasses = true;
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
        if (response.data && response.data.result && response.data.result.content) {
          this.classes = response.data.result.content.map((classItem) => ({
            id: classItem.classId,
            name: classItem.className,
            colour: classItem.classColour,
          }));
        } else {
          this.classes = [];
          console.error('Unexpected response format', response.data);
        }
      } catch (error) {
        console.error('Error fetching classes:', error);
        this.classes = [];
      } finally {
        this.isLoadingClasses = false;
      }
    },
    async fetchAvailableRooms(sessionId) {
      this.isLoadingRooms = true;
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
          `http://localhost:8088/fja-fap/staff/get-available-room/${sessionId}`, {
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
        this.rooms = [];
      } finally {
        this.isLoadingRooms = false;
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
    async fetchCurriculums() {
      this.isLoadingCurriculums = true; // Bắt đầu hiển thị loading
      try {
        const token = sessionStorage.getItem('jwtToken'); // Lấy token từ sessionStorage
        const response = await axios.get(
          `http://localhost:8088/fja-fap/staff/get-all-curriculumn-list`, {
          params: {
            page: 0,
            size: 100,
          },
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
        );

        if (response.data && response.data) {
          this.curriculums = response.data.result.map(curriculum => ({
            curriculumListId: curriculum.curriculumnListId,
            curriculumTitle: curriculum.curriculumnTitle,
          }));
          console.log("Mapped Curriculums:", this.curriculums);
        } else {
          console.error('Unexpected response structure:', response.data);
          this.curriculums = [];
        }
      } catch (error) {
        console.error('Error fetching curriculums:', error);
        this.curriculums = [];
      } finally {
        this.isLoadingCurriculums = false; // Kết thúc loading
      }
    },
    async createSchedule() {
      console.log('Class ID:', this.selectedClassId);
      console.log('Room ID:', this.selectedRoomId);
      console.log('Time Slot ID:', this.selectedTimeSlotId);
      if (!this.selectedClassId || !this.selectedRoomId || !this.selectedTimeSlotId || !this.selectedCurriculumId) {
        alert('Please select batch, class, room, time slot and curiculumn before submitting.');
        return;
      }

      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.post(
          `http://localhost:8088/fja-fap/staff/create-schedule/${this.selectedClassId}`,
          {
            timeSlotId: this.selectedTimeSlotId,
            roomNumber: this.selectedRoomId,
            curriculumnListId: this.selectedCurriculumId
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (response.data && response.data.code === 0) {
          this.showNotification('Schedule created successfully!', 'success');
          this.selectedRoomId = '';
          this.selectedTimeSlotId = '';
          this.selectedCurriculumId = '';
        } else {
          this.showNotification('Failed to create the schedule. Please try again.', 'error');
        }
      } catch (error) {
        console.error('Error creating schedule:', error);
        this.showNotification(error.response?.data?.message, 'error');
      }
    },
    async fetchSessions() {
      if (this.selectedWeekIndex === "" || !this.selectedClassId) {
        return;
      }

      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get("http://localhost:8088/fja-fap/staff/get-session-week", {
          params: {
            week: this.selectedWeekIndex,
            class_id: this.selectedClassId,
          },
          headers: { Authorization: `Bearer ${token}` },
        });
        this.sessions = response.data.result.map((session) => {
          //console.log("Fetched session:", session);
          return {
            curriculumnResponse: session.curriculumnResponse,
            sessionId: session.sessionId,
            sessionNumber: session.sessionNumber,
            date: session.date,
            dayOfWeek: session.dayOfWeek,
            timeSlotResponse: session.timeSlotResponse,
            fullName: session.fullName,
            roomNumber: session.roomNumber,
            lessonResponse: session.curriculumnResponse?.lessonResponse?.lessonTitle,
            examResponse: session.curriculumnResponse?.examResponse?.examTitle,
            eventName: session.eventName,
            note: session.note,
            eventId: session.eventId,
            userId: session.userId,

          };
        });
      } catch (error) {
        console.error("Error fetching sessions:", error);
      }
    },
    async fetchAvailableTeachers(sessionId) {
      if (!sessionId) {
        console.error("sessionId không hợp lệ");
        return;
      }

      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(`http://localhost:8088/fja-fap/staff/get-available-teacher/${sessionId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.data && response.data.result) {
          this.teachers = response.data.result.map((teacher) => ({
            id: teacher.userId,
            name: teacher.fullName,
          }));
        } else {
          console.error("Unexpected response structure:", response.data);
          this.teachers = [];
        }
      } catch (error) {
        console.error("Error fetching available teachers:", error);
        this.teachers = [];
      }
    },
    async fetchEvents() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get("http://localhost:8088/fja-fap/staff/event", {
          params: { page: 0, size: 100 },
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.data && response.data.result && response.data.result.content) {
          this.events = response.data.result.content.map(event => ({
            id: event.eventId,
            title: event.eventName,
            address: event.address,
            image: event.imagePath,
            status: event.status,
          }));
        } else {
          console.error("Unexpected response structure:", response.data);
          this.events = [];
        }
      } catch (error) {
        console.error("Error fetching events:", error);
        this.events = [];
      }
    },
    toggleEdit(sessionId) {
      if (!sessionId) {
        console.error("Invalid sessionId");
        return;
      }

      if (!this.isEditing[sessionId]) {
        // Enter edit mode: Initialize temporary data
        this.selectedTeacher[sessionId] = this.sessions.find(
          (session) => session.sessionId === sessionId
        )?.teacherId || null;
        this.selectedRoomTable[sessionId] = this.sessions.find(
          (session) => session.sessionId === sessionId
        )?.roomId || null;
        this.selectedEventTable[sessionId] = this.sessions.find(
          (session) => session.sessionId === sessionId
        )?.eventId || null;

        this.fetchAvailableTeachers(sessionId);
        this.fetchAvailableRooms(sessionId);
      }

      // Toggle edit mode
      this.isEditing[sessionId] = !this.isEditing[sessionId];
    },
    async editSession(sessionId) {
      if (!sessionId) {
        console.error("Invalid session ID");
        return;
      }

      const sessionData = this.sessions.find((session) => session.sessionId === sessionId);
      if (!sessionData) {
        console.error("Session data not found for sessionId:", sessionId);
        return;
      }

      console.log(sessionData);

      const updatedData = {
        date: sessionData.date,
        status: 0,
        sessionNumber: sessionData.sessionNumber,
        sessionWeek: this.selectedWeekIndex,
        sessionAvailable: sessionData.sessionAvailable || 1,
        curriculumnId: sessionData.curriculumnResponse?.curriculumnId,
        timeSlotId: sessionData.timeSlotResponse?.timeSLotId,
        roomNumber: this.selectedRoomTable[sessionId] || sessionData.roomNumber || "",
        eventId: this.selectedEventTable[sessionId] || sessionData.eventId || "",
        userId: this.selectedTeacher[sessionId] || sessionData.userId || "",
      };

      console.log("Updated Data:", updatedData);

      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.post(
          `http://localhost:8088/fja-fap/staff/update-session/${sessionId}`,
          updatedData,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (response.data && response.data.code === 0) {
          this.showNotification("Session updated successfully!", "success");
          this.isEditing[sessionId] = false;
          this.fetchSessions();
        } else {
          this.showNotification("Failed to update session. Please try again.", "error");
        }
      } catch (error) {
        console.error("Error updating session:", error);
        this.showNotification("An error occurred. Please try again.", "error");
      }
    },
    cancelEdit(sessionId) {
      if (!sessionId) {
        console.error("Invalid sessionId");
        return;
      }

      // Reset temporary changes
      this.selectedTeacher[sessionId] = null;
      this.selectedRoomTable[sessionId] = null;
      this.selectedEventTable[sessionId] = null;

      // Exit edit mode
      this.isEditing[sessionId] = false;
    },
    showNotification(message, type) {
      this.notification = { message, type };
      setTimeout(() => {
        this.notification.message = "";
      }, 3000);
    },
    openChangeDatePopup(sessionId) {
      if (!sessionId) {
        console.error("Session ID is invalid.");
        return;
      }

      this.currentSessionId = sessionId; // Lưu lại sessionId hiện tại
      this.showChangeDatePopup = true; // Hiển thị popup đổi ngày
    },
  },
  computed: {
    groupedSessions() {
      const grouped = {};
      this.sessions.forEach((session) => {
        if (session && session.date) {
          if (!grouped[session.date]) {
            grouped[session.date] = [];
          }
          grouped[session.date].push(session);
        } else {
          console.warn("Invalid session data:", session);
        }
      });
      return grouped;
    }
  },
  mounted() {
    this.fetchRooms();
    this.fetchBatches();
    this.fetchTimeSlots();
    this.fetchCurriculums();
    this.fetchEvents();
  },
};
</script>

<style lang="scss" scoped>
.filters-actions {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.information {
  width: 250px;
  font-size: 14px;
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

      .filter-select {
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