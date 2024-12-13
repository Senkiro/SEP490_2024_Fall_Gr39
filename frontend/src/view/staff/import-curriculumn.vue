<template>
    <div class="container">
      <div class="import-student-page">
        <div class="headContent">
          <h1>Import curriculumn</h1>
        </div>
        
        <div class="description">
          <p>This is the template file to import curriculumn to the system.</p>
          <p>
            <a :href="templateUrl" class="download-link" download>
              Download here
            </a>
            and modify with your curriculumn list.
          </p>
          <p>After finishing, upload the file directly:</p>
        </div>
        <div class="file-upload-section">
          <label for="file-input" class="file-button">Choose and Upload File</label>
          <span class="file-name">{{ fileName }}</span>
          <input
              type="file"
              id="file-input"
              accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
              @change="handleFileUpload"
          />
        </div>
        <!-- Notification -->
        <div v-if="notification.message" :class="['notification', notification.type]">
          <p v-for="line in notification.message.split('\n')" :key="line">
            {{ line }}
          </p>
        </div>

      </div>
    </div>
  </template>

  <script>
  import axios from "axios";
  
  export default {
    name: "ImportStudentPage",
    data() {
      return {
        templateUrl: "/templates/curriculumn.xlsx",
        notification: {
          message: '',
          type: '',
        },
        errorMessage: "",
        selectedFile: null,
      };
    },
    methods: {
      triggerFileInput() {
        this.$refs.fileInput.click();
      },
      handleFileUpload(event) {
        const file = event.target.files[0];
        if (!file) {
          console.error("No file selected.");
          this.showNotification("No file selected. Please choose a file to upload.", "error");
          return;
        }

        this.fileName = file.name;

        const formData = new FormData();
        formData.append("file", file);

        const token = sessionStorage.getItem("jwtToken");
        axios.post('http://localhost:8088/fja-fap/staff/upload-curriculumn', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${token}`,
          },
        })
            .then((response) => {
              console.log("Upload success:", response.data);
              this.showNotification(response.data.message || "File uploaded successfully!", "success");
            })
            .catch((error) => {
              console.error("Upload failed:", error);

              if (error.response && error.response.data) {
                const errorMessage = error.response.data.message || "An error occurred during the upload.";
                this.showNotification(`Upload failed: ${errorMessage}`, "error");
              } else {
                this.showNotification("Upload failed: Unable to reach the server. Please try again.", "error");
              }
            });
      },
      showNotification(message, type) {
        this.notification.message = message;
        this.notification.type = type;
  
        setTimeout(() => {
          this.notification.message = '';
          this.notification.type = '';
        }, 3000);
      },
    },
  };
  </script>
  
  <style scoped>
  .description {
    display: flex;
    flex-direction: column;
    font-size: 16px;
    margin-bottom: 10px;
    gap: 5px;
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
  
  .notification {
    padding: 10px;
    margin-top: 10px;
    border-radius: 5px;
    font-size: 14px;
    text-align: center;
  }
  
  .notification.success {
    background-color: #d4edda;
    color: #155724;
    border: 1px solid #c3e6cb;
  }
  
  .notification.error {
    background-color: #f8d7da;
    color: #721c24;
    border: 1px solid #f5c6cb;
  }
  
  </style>
  