<template>
    <div class="container">
        <div class="headContent">
            <h1>Attendance report</h1>
        </div>

      <div class="actions">
        <span>Attended: <b>{{ attendanceSummary }}</b> sessions</span>
      </div>

      <div class="table-container">
        <table>
          <thead>
          <tr>
            <th class="center">Session No</th>
            <th>Date</th>
            <th>Slot</th>
            <th>Teacher</th>
            <th>Lesson</th>
            <th>Attendance</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(session, index) in student.attendance" :key="index">
            <td class="center">{{ index + 1 }}</td>
            <td>{{ session.date }}</td>
            <td>{{ session.slot }}</td>
            <td>{{ session.teacher }}</td>
            <td>{{ session.lesson}}</td>
            <td :class="getAttendanceClass(session.status)">
              {{ session.status }}
            </td>
          </tr>
          <tr v-if="student.attendance.length === 0">
            <td colspan="6" class="center">No data available</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      student: {
        name: 'Pham The Minh',
        id: 'FA171392',
        attendance: []
      }
    };
  },
  computed: {
    attendanceSummary() {
      const totalSessions = this.student.attendance.length;
      const attendedSessions = this.student.attendance.filter(session => session.status === 'Attended').length;
      return `${attendedSessions}/${totalSessions}`;
    }
  },
  methods: {
    async fetchAttendanceData(studentId) {
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-attendance-student/${studentId}`,
            {
              params: {
                page: 0,
                size: 100,
              },
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );

        const result = response.data.result;
        if (result && result.content) {
          // Xử lý dữ liệu sau khi fetch
          this.student = {
            name: result.content[0].studentResponse.userInforResponse.fullName,
            id: result.content[0].studentResponse.rollNumber,
            attendance: result.content
                .map((session) => ({
                  number: session.attendanceId,
                  date: session.date,
                  slot: session.sessionResponse
                      ? session.sessionResponse.timeSlotResponse.name
                      : 'N/A',
                  teacher: session.teacherName || 'N/A',
                  status: session.status,
                  lesson: session.sessionResponse?.curriculumnResponse?.lessonResponse?.lessonTitle || 'N/A',
                }))
                .sort((a, b) => new Date(a.date) - new Date(b.date)),
          };
        } else {
          console.error('No data returned');
        }
      } catch (error) {
        console.error('Error fetching attendance data:', error);
      }
    },
    getAttendanceClass(status) {
      switch (status) {
        case 'Attended':
          return 'yes';
        case 'Absent':
          return 'no';
        case 'Not happen':
          return 'not-happen';
        default:
          return '';
      }
    }
  },
  mounted() {
    const studentId = sessionStorage.getItem("userId");
    this.fetchAttendanceData(studentId);
  }
};
</script>

<style></style>