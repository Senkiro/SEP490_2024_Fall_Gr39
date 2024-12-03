<template>
  <div class="container">
    <div class="headContent">
      <h1 class="title-large">Student’s attendance report</h1>
      <span class="student-name">Student: <b>{{ student.name }} - {{ student.id }}</b></span>
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
          <th>Lesson/Event</th>
          <th>Attendance</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(session, index) in student.attendance" :key="index">
          <td class="center">{{ index + 1 }}</td>
          <td>{{ session.date }}</td>
          <td>{{ session.slot }}</td>
          <td>{{ session.teacher }}</td>
          <td>{{ session.lesson || session.event }}</td>
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
      const attendedSessions = this.student.attendance.filter(session => session.status === 'Attend').length;
      return `${attendedSessions}/${totalSessions}`;
    }
  },
  methods: {
    async fetchAttendanceData(studentId) {
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-attendance-student/${studentId}`, {
              params: {
                page: 0,
                size: 10
              },
              headers: {
                Authorization: `Bearer ${token}`
              }
            }
        );
        const result = response.data.result;
        if (result && result.content) {
          this.student = {
            name: result.content[0].studentResponse.userInforResponse.fullName,
            id: result.content[0].studentResponse.rollNumber,
            attendance: result.content.map(session => ({
              number: session.attendanceId,
              date: session.date,
              slot: session.sessionResponse ? session.sessionResponse.slot : 'N/A',
              teacher: session.teacher || 'N/A',
              lesson: session.sessionResponse ? session.sessionResponse.lesson : '',
              event: session.sessionResponse ? session.sessionResponse.event : '',
              status: session.status
            }))
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
        case 'Attend':
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
    const studentId = this.$route.params.id;
    this.fetchAttendanceData(studentId);
  }
};
</script>

<style scoped>

.attendance-summary {
  text-align: right;
  margin-top: 5px;
  color: #555;
}

.absent {
  color: red;
}
</style>
