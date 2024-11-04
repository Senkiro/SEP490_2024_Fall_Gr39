<template>
  <div class="import-student-page">
    <h1 class="page-title">Import student from Excel file</h1>
    <p class="description">
      This is the template file to import student to the system.
      <a :href="templateUrl" class="download-link" download>Download here</a> and modify with your student list.
      After finished, upload file:
    </p>
    <div class="file-upload-section">
      <label for="file-input" class="file-button">Choose File</label>
      <span class="file-name">{{ fileName}}</span>
      <input type="file" id="file-input" @change="handleFileUpload" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'ImportStudentPage',
  data() {
    return {
      fileName: '',
      templateUrl:'/templates/students_list.xlsx',
    };
  },
  methods: {
    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file) {
        this.fileName = file.name;
      }
    },
  },
};
</script>

<style scoped>
.import-student-page {
  margin: 0 auto;
  padding: 20px;
  text-align: left;
  font-family: Arial, sans-serif;
}

.page-title {
  font-size: 24px;
  color: #304cb2;
  margin-bottom: 10px;
}

.description {
  font-size: 16px;
  margin-bottom: 10px;
}

.download-link {
  color: #304cb2;
  text-decoration: underline;
  cursor: pointer;
}

.file-upload-section {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
}

.file-button {
  padding: 5px 10px;
  background-color: #304cb2;
  color: white;
  border-radius: 5px;
  cursor: pointer;
  margin-right: 10px;
  text-align: center;
}

.file-name {
  color: #555;
  font-size: 14px;
}

#file-input {
  display: none;
}
</style>
