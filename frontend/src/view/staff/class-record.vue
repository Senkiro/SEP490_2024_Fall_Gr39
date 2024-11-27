<template>
  <div class="container">
    <div class="headContent">
      <h1>Class Record</h1>
    </div>
    <div class="actions">
      <button @click="openAddClassPopup">
        <VsxIcon iconName="AddCircle" size="20" type="bold" />
        Add class
      </button>
    </div>
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th class="center">No</th>
            <th>Class Name</th>
            <th class="center">Number of students</th>
            <th class="center">Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(classItem, index) in classes" :key="classItem.id">
            <td class="center">{{ index + 1 }}</td>
            <td :style="{ color: classItem.color }">{{ classItem.name }}</td>
            <td class="center">{{ classItem.numberOfStudents }}</td>
            <td class="center">
              <VsxIcon iconName="Edit2" :size="30" color="#171717" type="linear"
                @click="openEditClassPopup(classItem)" />
            </td>
          </tr>
          <tr v-if="classes.length === 0">
            <td colspan="4" class="center">No record.</td>
          </tr>
        </tbody>
      </table>

      <div class="pagination" v-if="classPagination.totalElements > 0">
        <button @click="changeClassPage(classPagination.currentPage - 1)" :disabled="classPagination.currentPage <= 1">
          <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717" />
        </button>
        <button v-for="page in classPagination.displayedPages" :key="page"
          :class="{ active: page === classPagination.currentPage }" @click="changeClassPage(page)">
          {{ page }}
        </button>
        <button @click="changeClassPage(classPagination.currentPage + 1)"
          :disabled="classPagination.currentPage >= studentPagination.totalPages">
          <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717" />
        </button>
      </div>
    </div>

    <div v-if="showEditClassPopup" class="popup-overlay">
      <div class="popup">
        <div class="exit-icon">
          <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="showEditClassPopup = false" />
        </div>
        <div class="popup-title">
          <h2>Edit Class</h2>
        </div>
        <form @submit.prevent="editClass">
          <div class="form-group">
            <label for="className">Class Name <span class="required">*</span></label>
            <input type="text" id="className" v-model="editedClass.name" required />
          </div>
          <div class="form-group">
            <label for="classColor">Class Color <span class="required">*</span></label>
            <div id="color-picker">
              <input type="color" id="classColor" v-model="editedClass.color" required />
            </div>
          </div>
          <div class="actions">
            <button type="submit" class="btn-submit">Save</button>
          </div>
        </form>
      </div>
    </div>

    <div v-if="notification.message" :class="['notification', notification.type]">
      {{ notification.message }}
    </div>
  </div>

  <!-- Popup Add Class -->
  <div v-if="showAddClassPopup" class="popup-overlay">
    <div class="popup">
      <div class="exit-icon">
        <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="showAddClassPopup = false" />
      </div>
      <div class="popup-title">
        <h2>Add class</h2>
      </div>

      <form @submit.prevent="addClass">
        <div class="form-group">
          <label for="className">Class name <span class="required">*</span></label>
          <input type="text" id="className" v-model="newClass.name" required />
        </div>
        <div class="form-group">
          <label for="classColor">Color <span class="required">*</span></label>
          <div id="color-picker">
            <input type="color" id="classColor" v-model="newClass.color" required />
          </div>

        </div>
        <div class="actions">
          <button class="btn-submit" type="submit">Create</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";
import axios from "axios";

export default {
  name: "ClassRecord",
  components: { VsxIcon },
  data() {
    return {
      batchName: 'SPRING24',
      activeTab: 'student',
      selectedClass: "",
      showAddStudentPopup: false,
      showAddClassPopup: false,
      showEditClassPopup: false,
      studentPagination: {
        currentPage: 1,
        itemsPerPage: 5,
        totalElements: 0,
        totalPages: 0,
        displayedPages: [],
      },
      classPagination: {
        currentPage: 1,
        itemsPerPage: 5,
        totalElements: 0,
        totalPages: 0,
        displayedPages: [],
      },
      newStudent: {
        fullname: '',
        japaneseName: '',
        email: '',
        class: '',
        dob: '',
        gender: ''
      },
      newClass: {
        name: '',
        color: '#000000'
      },
      students: [],
      classes: [],
      classList: [],
      notification: {
        message: '',
        type: ''
      },
      searchQuery: '',
    };
  },
  methods: {
    openAddClassPopup() {
      this.showAddClassPopup = true;
    },

    resetNewClass() {
      this.newClass = {
        name: '',
        color: '#000000'
      };
    },

    showNotification(message, type) {
      this.notification = { message, type };
      setTimeout(() => {
        this.notification.message = "";
      }, 3000);
    },

    async fetchClass() {
      try {
        const token = sessionStorage.getItem('jwtToken');

        const response = await axios.get(`http://localhost:8088/fja-fap/staff/get-class-by-batch?batch_name=${this.batchName}&page=${this.classPagination.currentPage - 1}&size=${this.classPagination.itemsPerPage}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.status === 200 && response.data.result) {
          this.classes = await Promise.all(
            response.data.result.content.map(async (classItem) => {
              // Gọi API lấy số lượng sinh viên cho từng lớp
              const studentCount = await this.fetchStudentCountByClass(classItem.className);
              return {
                id: classItem.classId || "Unknown ID",
                name: classItem.className || "Unknown Name",
                color: classItem.classColour || "#000000",
                numberOfStudents: studentCount || 0 // Gán số lượng sinh viên
              };
            })
          );

          this.classPagination.totalElements = response.data.result.totalElements;
          this.classPagination.totalPages = response.data.result.totalPages;

          this.updateClassDisplayedPages();
        }
      } catch (error) {
        console.error('Error fetching classes:', error);
        alert('Đã có lỗi xảy ra khi lấy danh sách lớp.');
      }
    },

    async addClass() {
      try {
        const token = sessionStorage.getItem('jwtToken');

        // API call to create a new class
        const response = await axios.post(
          'http://localhost:8088/fja-fap/staff/create-class',
          {
            className: this.newClass.name,
            classColour: this.newClass.color,
            batchName: this.batchName,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        // Successfully created the class
        const addedClass = response.data;
        this.classes.push({
          id: addedClass.id,
          name: addedClass.className,
          color: addedClass.classColour,
        });

        // Reset class form and close popup
        this.resetNewClass();
        this.showAddClassPopup = false;
        this.fetchClass();
        this.fetchClassFilter();
        this.showNotification('Class created successfully!', 'success');
      } catch (error) {
        console.error('Error creating class:', error);

        if (error.response && error.response.data) {
          this.showNotification(error.response.data.message || 'Error creating class!', 'error');
        } else {
          this.showNotification('Unexpected error occurred!', 'error');
        }
      }
    },

    async editClass() {
      if (!this.editedClass.name.trim()) {
        this.showNotification("Class name is required.", "error");
        return;
      }

      try {
        const token = sessionStorage.getItem('jwtToken');
        await axios.post(
          `http://localhost:8088/fja-fap/staff/update-class/${this.editedClass.id}`,
          {
            className: this.editedClass.name,
            classColour: this.editedClass.color,
          },
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        await this.fetchClass();
        await this.fetchClassFilter();
        this.showEditClassPopup = false;

        this.editedClass = { id: "", name: "", color: "" };

        this.showNotification("Class updated successfully!", "success");
      } catch (error) {
        console.error("Error updating class:", error);
        this.showNotification(error.response?.data?.message || "Error updating class. Please try again.", "error");
      }
    },

    openEditClassPopup(classItem) {
      this.editedClass = {
        id: classItem.id,
        name: classItem.name,
        color: classItem.color,
      };
      this.showEditClassPopup = true;
    },

    updateClassDisplayedPages() {
      const pages = [];
      const { currentPage, totalPages } = this.classPagination;
      if (totalPages <= 5) {
        for (let i = 1; i <= totalPages; i++) {
          pages.push(i);
        }
      } else {
        if (currentPage <= 3) {
          pages.push(1, 2, 3, '...', totalPages);
        } else if (currentPage >= totalPages - 2) {
          pages.push(1, '...', totalPages - 2, totalPages - 1, totalPages);
        } else {
          pages.push(1, '...', currentPage - 1, currentPage, currentPage + 1, '...', totalPages);
        }
      }
      this.classPagination.displayedPages = pages;
    },

    changeClassPage(newPage) {
      if (newPage > 0 && newPage <= this.classPagination.totalPages) {
        this.classPagination.currentPage = newPage;
        this.fetchClass();
      }
    },
  },
  mounted() {
    this.fetchClass();
  },
}
  ;
</script>

<style scoped></style>
