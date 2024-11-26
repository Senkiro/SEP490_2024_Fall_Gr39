<template>
    <div class="container">
        <div class="headContent">
            <h1>Exam type</h1>
        </div>

        <div class="actions">
            <button @click="addExamTypePopup = true">
                <VsxIcon iconName="AddCircle" size="20" type="bold" />
                Add exam type
            </button>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th class="center">No</th>
                        <th>Exam type</th>
                        <th>Rate</th>
                    </tr>
                </thead>
              <tbody>
              <tr v-for="(examType, index) in examTypeRateList" :key="examType.examType">
                <td class="center">{{ index + 1 }}</td>
                <td>{{ examType.examName }}</td>
                <td>{{ examType.examRate }}%</td>
              </tr>
              </tbody>
            </table>
        </div>

        <div v-if="addExamTypePopup" class="popup-overlay">
            <div class="popup">
                <div class="exit-icon">
                    <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold"
                        @click="addExamTypePopup = false" />
                </div>
                <div class="popup-title">
                    <h2>Add exam type</h2>
                </div>
                <form @submit.prevent="addExamType">
                    <div class="form-group">
                        <label for="examtype">Exam type <span class="required">*</span></label>
                        <input type="text" id="examtype" required />
                    </div>
                    <div class="form-group">
                        <label for="rate">Rate(%) <span class="required">*</span></label>
                        <input type="number" id="rate" min="0" max="100" required />
                    </div>
                    <div class="actions">
                        <button type="submit">Add</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      addExamTypePopup: false,
      examTypeRateList: []
    }
  },
  methods: {
    async fetchExamTypeRate() {
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get('http://localhost:8088/fja-fap/staff/get-all-exam-type', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });

        if (response.data.code === 0) {
          this.examTypeRateList = response.data.result;
        } else {
          console.error('Failed to fetch data:', response.data);
        }
      } catch (error) {
        console.error('Error fetching ExamTypeRate:', error);
      }
    }
  },
  mounted() {
    this.fetchExamTypeRate();
  }
}
</script>


<style></style>