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

      <div class="pagination" v-if="classPagination.totalElements > 5">
        <button @click="changeClassPage(classPagination.currentPage - 1)" :disabled="classPagination.currentPage <= 1">
          <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717" />
        </button>
        <button v-for="page in classPagination.displayedPages" :key="page"
                :class="{ active: page === classPagination.currentPage }" @click="changeClassPage(page)">
          {{ page }}
        </button>
        <button @click="changeClassPage(classPagination.currentPage + 1)"
                :disabled="classPagination.currentPage >= classPagination.totalPages">
          <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717" />
        </button>
      </div>
    </div>

    <div v-if="notification.message" :class="['notification', notification.type]">
      {{ notification.message }}
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
      batchName: "Fall24",
      showAddClassPopup: false,
      classPagination: {
        currentPage: 1,
        itemsPerPage: 10,
        totalElements: 0,
        totalPages: 0,
        displayedPages: [],
      },
      newClass: {
        name: "",
        color: "#000000",
      },
      classes: [],
      notification: {
        message: "",
        type: "",
      },
    };
  },
  methods: {
    openAddClassPopup() {
      this.showAddClassPopup = true;
    },

    resetNewClass() {
      this.newClass = {
        name: "",
        color: "#000000",
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
        const token = sessionStorage.getItem("jwtToken");

        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-class-by-batch?batch_name=${this.batchName}&page=${this.classPagination.currentPage - 1}&size=${this.classPagination.itemsPerPage}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );

        if (response.data.result) {
          this.classes = await Promise.all(
              response.data.result.content.map(async (classItem) => {
                const studentCount = await this.fetchStudentCountByClass(classItem.classId);
                return {
                  id: classItem.classId,
                  name: classItem.className,
                  color: classItem.classColour,
                  numberOfStudents: studentCount,
                };
              })
          );

          this.classPagination.totalElements = response.data.result.totalElements;
          this.classPagination.totalPages = response.data.result.totalPages;
          this.updateClassDisplayedPages();
        }
      } catch (error) {
        console.error("Error fetching classes:", error);
        alert("Failed to fetch classes.");
      }
    },

    updateClassDisplayedPages() {
      const pages = [];
      const {currentPage, totalPages} = this.classPagination;
      if (totalPages <= 5) {
        for (let i = 1; i <= totalPages; i++) {
          pages.push(i);
        }
      } else {
        if (currentPage <= 3) {
          pages.push(1, 2, 3, "...", totalPages);
        } else if (currentPage >= totalPages - 2) {
          pages.push(1, "...", totalPages - 2, totalPages - 1, totalPages);
        } else {
          pages.push(1, "...", currentPage - 1, currentPage, currentPage + 1, "...", totalPages);
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


    async fetchStudentCountByClass(classId) {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-student-by-batch-class`,
            {
              params: {
                batch_name: this.batchName,
                class_id: classId,
                page: 0,
                size: 1000,
              },
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );

        if (response.status === 200 && response.data.result) {
          return response.data.result.totalElements || 0;
        }
      } catch (error) {
        console.error(`Error fetching student count for class ${classId}:`, error);
        return 0;
      }
    },
  },
  mounted() {
    this.fetchClass();
  },
};
</script>

<style scoped lang="scss">
#color-picker {
  width: 250px;
  display: flex;
  align-items: flex-start;

  input {
    width: 80px;
    height: 30px;
    padding: 0px 5px;
  }
}
</style>
