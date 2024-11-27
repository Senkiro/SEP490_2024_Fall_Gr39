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
          <td class="center">{{ session.number }}</td>
          <td>{{ session.date }}</td>
          <td>{{ session.slot }}</td>
          <td>{{ session.teacher }}</td>
          <td>{{ session.lesson || session.event }}</td>
          <td :class="getAttendanceClass(session.status)">
            {{ session.status }}
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      student: {
        name: 'Pham The Minh',
        id: 'FA171392',
        attendance: [
          { number: 1, date: 'dd/mm/yyyy', slot: 'Morning (8:30 - 12:30)', teacher: 'Ikeda Yuri', lesson: 'Chapter 1 - Lesson 1', event: '', status: 'Attend' },
          { number: 2, date: 'dd/mm/yyyy', slot: 'Morning (8:30 - 12:30)', teacher: 'Hiroto', lesson: '', event: 'Trip to Tokyo Tower', status: 'Not happen' },
        ]
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
