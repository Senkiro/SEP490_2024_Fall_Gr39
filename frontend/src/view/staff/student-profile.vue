<template>
  <div class="container">
    <div class="headContent">
      <h1>Student Profile</h1>
    </div>

    <section class="info-card">
      <div class="info-wrapper">
        <img
          :src="studentData.userInforResponse.img || defaultAvatar"
          alt="Student Avatar"
          class="avatar"
        />

        <div class="details-container">
          <div class="name-id-container">
            <div class="name-id">
              <h3>
                <strong>{{ studentData.userInforResponse.fullName }} - {{ studentData.userInforResponse.japaneseName }}</strong>
              </h3>
              <p>{{ studentData.rollNumber }}</p>
            </div>
            <button class="btn-edit" @click="toggleEditModal">
              <VsxIcon iconName="Edit2" :size="18" color="#495057" type="linear" />Edit
            </button>
          </div>

          <div class="details">
            <div class="column column1">
              <div class="attribute">
                <p>Batch</p>
                <strong>{{ studentData.batchName }}</strong>
              </div>
              <div class="attribute">
                <p>Gender</p>
                <strong>{{ studentData.userInforResponse.gender ? 'Male' : 'Female' }}</strong>
              </div>
              <div class="attribute">
                <p>DOB</p>
                <strong>{{ formatDate(studentData.userInforResponse.dob) }}</strong>
              </div>
            </div>

            <div class="column column2">
              <div class="attribute">
                <p>Class</p>
                <strong :style="{ color: studentData.classResponse.classColour }">{{ studentData.classResponse.className }}</strong>
              </div>
              <div class="attribute">
                <p>Phone</p>
                <strong>{{ studentData.userInforResponse.phone || 'N/A' }}</strong>
              </div>
              <div class="attribute">
                <p>Email</p>
                <strong>{{ studentData.userInforResponse.email || 'N/A' }}</strong>
              </div>
            </div>

            <div class="column column3">
              <div class="average-score-box">
                <div class="score-box-upper">
                  <strong>Average Score</strong>
                </div>
                <div class="score-box-lower">
                  <p class="score">8.6</p>
                  <strong class="score-status">Very Good</strong>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>
    </section>
    <div v-if="isEditing" class="modal-overlay">
      <div class="modal">
        <h2>Edit Student Information </h2>
        <form @submit.prevent="saveChanges">
          <div class="form-group">
            <label for="fullName">Full Name</label>
            <input id="fullName" type="text" v-model="editData.fullName" required />
          </div>

          <div class="form-group">
            <label for="japaneseName">Japanese Name</label>
            <input id="japaneseName" type="text" v-model="editData.japaneseName" required />
          </div>

          <div class="form-group">
            <label for="phone">Phone</label>
            <input id="phone" type="text" v-model="editData.phone"  />
          </div>
          <div class="form-group">
          <label for="dob">Date of Birth</label>
          <input
            id="dob"
            type="date"
            v-model="editData.dob"
            required
          />
          </div>
          <div class="form-group">
            <label for="email">Email</label>
            <input id="email" type="email" v-model="editData.email" required />
          </div>
          <div class="form-group">
            <label for="avatar">Upload Avatar</label>
            <input
              id="avatar"
              type="file"
              accept="image/png, image/jpeg"
              @change="onFileChange"
            />
            <img v-if="previewImage" :src="previewImage" alt="Preview" class="preview" />
            <p v-if="fileError" class="error">{{ fileError }}</p>
          </div>
          <div class="modal-actions">
            <button type="submit" class="btn-save">Save Changes</button>
            <button type="button" class="btn-cancel" @click="toggleEditModal">Cancel</button>
          </div>
        </form>
      </div>
    </div>
    <div class="tab-buttons-container">
      <div class="tab-buttons">
        <button class="tab-button" :class="{ active: showAttendance }" @click="showAttendance = true">
          <h3>Attendance Report</h3>
        </button>
        <button class="tab-button" :class="{ active: !showAttendance }" @click="showAttendance = false">
          <h3>Mark Report</h3>
        </button>
        <button class="tab-button">
          <h3>Essay Submission</h3>
        </button>
      </div>
    </div>

    <!-- Attendance Report Table -->
    <div class="table-container">
      <table v-if="showAttendance">
        <thead>
          <tr>
            <th class="center">Session No</th>
            <th>Date</th>
            <th>Slot</th>
            <th>Teacher</th>
            <th class="center">Attendance</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(attendance, index) in attendanceReport" :key="index">
            <td class="center">{{ index + 1 }}</td>
            <td>{{ attendance.date || 'N/A' }}</td>
            <td>{{ attendance.slot || 'N/A' }}</td>
            <td>{{ attendance.teacher || 'N/A' }}</td>
            <td class="center" :class="{ attended: attendance.status === 'Attend', absent: attendance.status === 'Absent' }">
              {{ attendance.status || 'N/A' }}
            </td>
          </tr>
          <tr v-if="attendanceReport.length === 0">
            <td colspan="5" class="center">No attendance data available.</td>
          </tr>
        </tbody>
      </table>

      <table v-else>
        <thead>
          <tr>
            <th>Grade Category</th>
            <th>Grade Item</th>
            <th>Weight</th>
            <th>Value</th>
            <th>Comment</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="grade in markReport" :key="grade.category || grade.item">
            <td>{{ grade.category || 'N/A' }}</td>
            <td>{{ grade.item || 'N/A' }}</td>
            <td>{{ grade.weight || 'N/A' }}</td>
            <td>{{ grade.value || 'N/A' }}</td>
            <td>{{ grade.comment || 'N/A' }}</td>
          </tr>
          <tr v-if="markReport.length === 0">
            <td colspan="5" class="center">No mark report data available.</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';
import defaultAvatar from '@/assets/smiling-young-man-illustration_1308-174669.avif';

const studentId = ref('');
const studentData = ref({
  studentId: '',
  rollNumber: '',
  userInforResponse: {
    fullName: '',
    japaneseName: '',
    dob: '',
    img: '',
    gender: '',
    email: '',
    phone: ''
  },
  batchName: '',
  classResponse: {
    classId: '',
    className: '',
    classColour: ''
  }
});

const showAttendance = ref(true);
const markReport = ref([
  { category: 'Participation', item: 'Total', weight: '10%', value: '9.0', comment: 'Excellent' },
  { category: 'Daily Exam', item: 'Total', weight: '20%', value: '8.5', comment: 'Very Good' },
  { category: 'Mid-term Exam', item: 'Total', weight: '30%', value: '8.0', comment: 'Good' },
  { category: 'Final Exam', item: 'Average', weight: '40%', value: '7.5', comment: 'Good' },
  { category: 'Course Total', item: 'Status', weight: '-', value: '-', comment: 'Good' },
]);

const attendanceReport = ref([
  { date: '2/9/2024', slot: 'Morning (8:30 - 12:30)', teacher: 'Yuri Ikeda', status: 'Attend' },
  { date: 'x/5/2024', slot: 'Morning (8:30 - 12:30)', teacher: 'Yuri Ikeda', status: 'Attend' },
  { date: 'y/1/2024', slot: 'Morning (8:30 - 12:30)', teacher: 'Yuri Ikeda', status: 'Absent' },
]);

studentId.value = useRoute().params.id;

const fetchStudentData = async () => {
  try {
    const token = sessionStorage.getItem('jwtToken');
    const response = await axios.get(
      `http://localhost:8088/fja-fap/staff/get-student/${studentId.value}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (response.data && response.data.result) {
      studentData.value = response.data.result;
    }
  } catch (error) {
    console.error('Error fetching student data:', error);
  }
};

const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  const [year, month, day] = dateString.split('-');
  return `${day}/${month}/${year}`;
};

onMounted(() => {
  fetchStudentData();
});
const isEditing = ref(false);
const editData = ref({ ...studentData.value.userInforResponse });
const previewImage = ref(null);
const fileError = ref(null);

const toggleEditModal = () => {
  isEditing.value = !isEditing.value;

  if (isEditing.value) {
    editData.value = { ...studentData.value.userInforResponse };
    previewImage.value = null; // Reset preview
    fileError.value = null; // Reset file error
  }
};

const onFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    // Validate file type
    if (!['image/jpeg', 'image/png'].includes(file.type)) {
      fileError.value = 'Invalid file type. Only JPEG and PNG are allowed.';
      previewImage.value = null;
      return;
    }

    // Validate file name length
    if (file.name.length > 100) {
      fileError.value = 'File name is too long. Must be under 100 characters.';
      previewImage.value = null;
      return;
    }

    fileError.value = null;

    // Generate preview
    const reader = new FileReader();
    reader.onload = () => {
      previewImage.value = reader.result;
    };
    reader.readAsDataURL(file);

    // Attach to editData for submission
    editData.value.img = file;
  }
};

const saveChanges = async () => {
  try {
    const token = sessionStorage.getItem('jwtToken');
    const formData = new FormData();
    formData.append('fullName', editData.value.fullName);
    formData.append('japaneseName', editData.value.japaneseName);
    formData.append('phone', editData.value.phone);
    formData.append('email', editData.value.email);
    if (editData.value.img) {
      formData.append('avatar', editData.value.img);
    }

    await axios.put(
      `http://localhost:8088/fja-fap/staff/update-student/${studentData.value.studentId}`,
      formData,
      {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'multipart/form-data',
        },
      }
    );

    studentData.value.userInforResponse = { ...editData.value };
    isEditing.value = false;
    alert('Changes saved successfully!');
  } catch (error) {
    console.error('Failed to save changes:', error);
    alert('Failed to save changes. Please try again.');
  }
};
</script>

<style lang="scss" >
@import '@/assets/styles/student-profile.scss';

</style>
