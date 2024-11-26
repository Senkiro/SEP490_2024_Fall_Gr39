<template>
  <div class="container">
    <div class="headContent">
      <h1 class="title-large">Student’s attendance report</h1>
      <span class="student-name">Student: <b>{{ student.name }} - {{ student.id }}</b></span>
    </div>
    
    <div class="actions">
      <span>Attended: <b>{{ attendanceSummary }}</b></span>
    </div>

    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th class="center">Session No</th>
          <th>Date</th>
          <th>Slot</th>
          <th>Teacher</th>
          <th>Attendance</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(session, index) in student.attendance" :key="index">
          <td class="center">{{ session.number }}</td>
          <td>{{ session.date }}</td>
          <td>{{ session.slot }}</td>
          <td>{{ session.teacher }}</td>
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
          { number: 1, date: '2/9/2024', slot: 'Morning (8:30 - 12:30)', teacher: 'Ikeda Yuri', status: 'Attend' },
          { number: 2, date: 'x/5/2024', slot: 'Morning (8:30 - 12:30)', teacher: 'Hiroto', status: 'Attend' },
          { number: 3, date: 'x/1/2024', slot: 'Morning (8:30 - 12:30)', teacher: 'Kazuki', status: 'Attend' },
          { number: 4, date: '2/9/2023', slot: 'Morning (8:30 - 12:30)', teacher: 'Kuro Neko', status: 'Absent' },
          { number: 6, date: 'x/1/2023', slot: 'Afternoon (1:30 - 5:30)', teacher: 'Shiro Neko', status: 'Not happen' },
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
          return 'no'; // Màu đỏ cho Absent
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
