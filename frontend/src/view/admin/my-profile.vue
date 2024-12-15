<template>
    <div class="container">
      <div class="headContent">
        <h1>My Profile</h1>
      </div>

      <section class="info-card">
        <div class="info-wrapper">
          <img
              :src="
                studentData.userInforResponse?.img
                ? `${baseURL}/${studentData.userInforResponse.img}`
                : defaultAvatar"
              alt="Student Avatar"
              class="avatar"
          />

          <div class="details-container">
            <div class="name-id-container">
              <div class="name-id">
                <h3>
                  <strong>
                    {{ studentData?.fullName || 'N/A' }}
                    -
                    {{studentData?.japaneseName || 'N/A' }}
                  </strong>
                </h3>
                <p>{{ studentData?.rollNumber }}</p>
              </div>
              <button class="btn-edit" @click="toggleEditModal">
                <VsxIcon iconName="Edit2" :size="18" color="#495057" type="linear"/>
                Edit
              </button>
            </div>

            <div class="details">
              <div class="column column1">
                <div class="attribute">
                  <p>Email</p>
                  <strong>{{ studentData?.email || 'N/A' }} </strong>
                </div>
                <div class="attribute">
                  <p>Gender</p>
                  <strong>
                    {{
                      studentData?.gender === false
                          ? "Female"
                          : studentData?.gender === true
                              ? "Male"
                              : "N/A"
                    }}
                  </strong>
                </div>
                <div class="attribute">
                  <p>DOB</p>
                  <strong>{{ formatDate(studentData?.dob) }}</strong>
                </div>
              </div>

              <div class="column column2">
                <div class="attribute">
                  <p>Phone</p>
                  <strong>{{ studentData?.phone || 'N/A' }}</strong>
                </div>
                <div class="attribute">
                  <p>Password</p>
                  <strong>{{ studentData?.password || 'N/A' }}</strong>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
      <div v-if="isEditing" class="popup-overlay">
        <div class="popup">
          <div class="exit-icon">
            <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="toggleEditModal"/>
          </div>
          <div class="popup-title">
            <h2>Edit Information </h2>
          </div>
          <form @submit.prevent="saveChanges">
            <div class="form-group">
              <label for="fullName">Fullname <span class="required">*</span></label>
              <input id="fullName" type="text" v-model="editData.fullName" required/>
            </div>

            <div class="form-group">
              <label for="japaneseName">Japanese name <span class="required">*</span></label>
              <input id="japaneseName" type="text" v-model="editData.japaneseName" required/>
            </div>

            <div class="form-group">
              <label for="phone">Phone <span class="required">*</span></label>
              <input id="phone" type="text" v-model="editData.phone"/>
            </div>
            <div class="form-group">
              <label for="dob">Date of Birth <span class="required">*</span></label>
              <input
                  id="dob"
                  type="date"
                  v-model="editData.dob"
                  required
              />
            </div>
            <div class="form-group">
              <label for="email">Email <span class="required">*</span></label>
              <input id="email" type="email" v-model="editData.email" required/>
            </div>
            <div class="form-group">
              <label for="avatar">Upload Avatar</label>
              <div class="image-container">
                <input
                    id="avatar"
                    type="file"
                    accept="image/png, image/jpeg"
                    @change="onFileChange"
                />
                <img v-if="previewImage" :src="previewImage" alt="Preview" class="preview"/>
                <p v-if="fileError" class="error">{{ fileError }}</p>
              </div>

            </div>
            <div class="actions">
              <button type="submit" class="btn-submit">Save Changes</button>
            </div>
          </form>
        </div>
      </div>

      <div v-if="notification.message" class="notification" :class="notification.type">
        {{ notification.message }}
      </div>

    </div>
  </template>

  <script setup>
  import {ref, onMounted} from 'vue';
  import axios from 'axios';
  import { useRoute } from "vue-router";
  import defaultAvatar from '@/assets/smiling-young-man-illustration_1308-174669.avif';
  const route = useRoute(); // Lấy route hiện tại
  const userId = route.params.userId;
  const studentData = ref({
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

  const fetchStudentData = async () => {
    try {
      const token = sessionStorage.getItem('jwtToken');
      const response = await axios.get(
          `http://localhost:8088/fja-fap/user/${userId}`,
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
  const editData = ref({...studentData.value.userInforResponse});
  const previewImage = ref(null);
  const fileError = ref(null);
  
  const toggleEditModal = () => {
    isEditing.value = !isEditing.value;
  
    if (isEditing.value) {
      editData.value = {...studentData.value.userInforResponse};
      previewImage.value = null;
      fileError.value = null;
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
  
      editData.value.img = file;
    }
  };
  
  const saveChanges = async () => {
    try {
      const token = sessionStorage.getItem("jwtToken");
      const formData = new FormData();
  
      const userDetail = {
        fullName: editData.value.fullName,
        japaneseName: editData.value.japaneseName,
        phone: editData.value.phone,
        dob: editData.value.dob,
        email: editData.value.email,
      };
      formData.append("userDetail", new Blob([JSON.stringify(userDetail)], {type: "application/json"}));
  
      // Thêm avatar file (nếu có)
      if (editData.value.img) {
        formData.append("avatar", editData.value.img);
      }
  
      const response = await axios.put(
          `http://localhost:8088/fja-fap/staff/update-student/${userId}`,
          formData,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "multipart/form-data",
            },
          }
      );
  
      if (response.data && response.data.result) {
        studentData.value.userInforResponse = {...editData.value};
        isEditing.value = false;
        showNotification("Changes saved successfully!", "success");
      }
    } catch (error) {
      showNotification(error.response?.data?.message, "error");
    }
  };
  const notification = ref({ message: "", type: "" });
  
  const showNotification = (message, type) => {
    notification.value = { message, type };
    setTimeout(() => {
      notification.value.message = "";
    }, 3000);
  };

  </script>
  
  <style lang="scss">
  .popup {
    overflow-y: auto;
    max-height: 90vh;
  }
  
  .image-container {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
  
  .preview {
    height: 200px;
    width: 150px;
  }
  
  .container {
    padding: 20px;
  
    .info-wrapper {
      display: flex;
      align-items: flex-start;
      gap: 20px;
      max-height: 400px;
  
      img {
        height: 290px;
        border-radius: 20px;
      }
    }
  
    .details-container {
      display: flex;
      flex-direction: column;
      flex-grow: 1;
      border: 1px solid #ddd;
      border-radius: 20px;
      padding: 20px 30px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      gap: 30px;
  
      .name-id-container {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        padding-bottom: 20px;
  
        .btn-edit {
          display: flex;
          color: var(--border);
          background: none;
          border: 0.2px solid var(--border);
          border-radius: 5px;
          padding: 3px 8px;
          font-size: 12px;
          height: fit-content;
          align-items: center;
          gap: 5px;
        }
      }
  
      .details {
        display: flex;
        flex-direction: row;
        font-size: 14px;
  
        .column {
          display: flex;
          gap: 20px;
          flex-direction: column;
          width: 50%;
  
          .attribute {
            display: flex;
            flex-direction: column;
            gap: 5px;
  
            p {
              color: #6C757D;
            }
          }
  
          .average-score-box {
            display: flex;
            flex-direction: column;
            border-radius: 12px;
            background-color: #D6EAFF;
            padding: 15px;
            width: 350px;
            text-align: center;
            font-size: 14px;
            gap: 30px;
  
            .score-box-upper {
              display: flex;
              justify-content: space-between;
            }
  
            .score-box-lower {
              display: flex;
              justify-content: space-between;
  
              .score-status {
                border: 2px solid green;
                background: white;
                border-radius: 10px;
                align-content: center;
                padding: 10px;
                color: green;
              }
  
              .score {
                width: 80px;
                font-size: 20px;
                font-weight: bold;
                margin-top: auto;
              }
            }
          }
        }
      }
    }
  
    .attended {
      color: green;
      font-weight: bold;
    }
  
    .absent {
      color: red;
      font-weight: bold;
    }
  }
  
  .notification {
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 10px 20px;
    border-radius: 5px;
    font-weight: bold;
    z-index: 1000;
    transition: all 0.3s ease-in-out;
    &.success {
      background-color: #d4edda;
      color: #155724;
      border: 1px solid #c3e6cb;
    }
    &.error {
      background-color: #f8d7da;
      color: #721c24;
      border: 1px solid #f5c6cb;
    }
  }
  
  </style>
  