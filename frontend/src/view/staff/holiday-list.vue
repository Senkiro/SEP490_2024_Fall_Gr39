<template>
  <div class="container">
    <div class="headContent">
      <h1>Holiday list</h1>
    </div>

    <div class="actions">
      <button @click="addHolidayPopup = true">
        <VsxIcon iconName="AddCircle" size="20" type="bold" />
        Add holiday
      </button>
      <button @click="navigateToImportHoliday">
        <VsxIcon iconName="Import" size="20" type="bold" />
        Import holiday
      </button>
    </div>

    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th class="center">No</th>
          <th>Holiday name</th>
          <th>Date</th>
          <th class="center">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(holiday, index) in holidays" :key="holiday.holidayId">
          <td class="center">{{ (currentPage - 1) * itemsPerPage + (index + 1) }}</td>
          <td>{{ holiday.title }}</td>
          <td>{{ holiday.holidayDate }}</td>
          <td>
            <div class="icon-group">
              <VsxIcon iconName="Edit2" :size="25" color="#171717" type="linear" @click="editHoliday(holiday)" />
              <VsxIcon iconName="Trash" :size="25" color="#171717" type="linear" @click="confirmDelete(holiday.holidayId)" />
            </div>
          </td>
        </tr>
        <tr>
            <td colspan="4" class="center">No record.</td>
          </tr>
        </tbody>
      </table>

      <div class="pagination" v-if="totalPages > 1">
        <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">
          <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717" />
        </button>
        <button v-for="page in displayedPages" :key="page" :class="{ active: page === currentPage }" @click="changePage(page)">
          {{ page }}
        </button>
        <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">
          <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717" />
        </button>
      </div>
    </div>

    <!-- Notification Section -->
    <div v-if="notification.message" :class="['notification', notification.type]">
      {{ notification.message }}
    </div>

    <!-- Add/Edit Holiday Popup -->
    <div v-if="addHolidayPopup" class="popup-overlay">
      <div class="popup">
        <div class="exit-icon">
          <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="closePopup" />
        </div>
        <div class="popup-title">
          <h2>{{ isEdit ? 'Update Holiday' : 'Add Holiday' }}</h2>
        </div>
        <form @submit.prevent="submitHoliday">
          <div class="form-group">
            <label for="holidayName">Holiday name <span class="required">*</span></label>
            <input type="text" id="holidayName" v-model="holidayName" required />
          </div>
          <div class="form-group">
            <label for="date">Date <span class="required">*</span></label>
            <input type="date" id="date" v-model="holidayDate" required />
          </div>
          <div class="actions">
            <button type="submit">{{ isEdit ? 'Update' : 'Create' }}</button>
          </div>
        </form>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { VsxIcon } from 'vue-iconsax';

export default {
  components: { VsxIcon },
  data() {
    return {
      addHolidayPopup: false,
      holidays: [],
      holidayName: '',
      holidayDate: '',
      holidayId: '', // To hold the ID of the holiday to update or delete
      errorMessage: '',
      notification: {
        message: '',
        type: '', // success or error
      },
      currentPage: 1,
      itemsPerPage: 5,
      totalElements: 0,
      totalPages: 0,
      isEdit: false, // To track whether we're editing or adding a holiday
    };
  },
  computed: {
    // Computes the page numbers for the pagination buttons
    displayedPages() {
      let start = Math.max(1, this.currentPage - 2);
      let end = Math.min(this.totalPages, this.currentPage + 2);
      const pages = [];
      for (let i = start; i <= end; i++) {
        pages.push(i);
      }
      return pages;
    }
  },
  methods: {
    // Fetch holiday data from API
    async fetchHolidays() {
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.get('http://localhost:8088/fja-fap/staff/get-all-holiday', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          params: {
            page: this.currentPage - 1,
            size: this.itemsPerPage,
          },
        });
        this.holidays = response.data.result.content;
        this.totalElements = response.data.result.totalElements;
        this.totalPages = response.data.result.totalPages;
      } catch (error) {
        this.errorMessage = 'Failed to fetch holidays. Please try again later.';
      }
    },

    // Navigate to import holiday page
    navigateToImportHoliday() {
      this.$router.push({ name: 'ImportHoliday' });
    },

    // Open the edit popup with holiday data
    editHoliday(holiday) {
      this.isEdit = true;
      this.holidayId = holiday.holidayId;
      this.holidayName = holiday.title;
      this.holidayDate = holiday.holidayDate;
      this.addHolidayPopup = true;
    },

    // Handle add or update holiday form submission
    async submitHoliday() {
      try {
        const token = sessionStorage.getItem('jwtToken');
        let response;

        if (this.isEdit) {
          response = await axios.post(`http://localhost:8088/fja-fap/staff/update-holiday/${this.holidayId}`, {
            title: this.holidayName,
            holidayDate: this.holidayDate,
          }, {
            headers: {
              Authorization: `Bearer ${token}`,
            }
          });
        } else {
          response = await axios.post('http://localhost:8088/fja-fap/staff/create-holiday', {
            title: this.holidayName,
            holidayDate: this.holidayDate,
          }, {
            headers: {
              Authorization: `Bearer ${token}`,
            }
          });
        }

        if (response.data.code === 0) {
          this.holidays = this.holidays.map(holiday =>
              holiday.holidayId === this.holidayId ? response.data.result : holiday
          );
          this.successMessage = `${this.isEdit ? 'Holiday updated' : 'Holiday created'} successfully!`;
          this.fetchHolidays();
          this.showNotification(`${this.isEdit ? 'Holiday updated' : 'Holiday created'} successfully!`, "success");
          this.closePopup();
        }
      } catch (error) {
        this.errorMessage = 'Failed to create/update holiday. Please try again later.';
        this.showNotification('Failed to create/update holiday.', 'error');
      }
    },

    // Show a confirmation alert before deleting
    confirmDelete(holidayId) {
      const confirmed = window.confirm("Are you sure you want to delete this holiday?");
      if (confirmed) {
        this.deleteHoliday(holidayId);
      }
    },

    // Delete holiday
    async deleteHoliday(holidayId) {
      try {
        const token = sessionStorage.getItem('jwtToken');
        const response = await axios.delete(`http://localhost:8088/fja-fap/staff/delete-holiday/${holidayId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          }
        });

        if (response.data.code === 0) {
          this.holidays = this.holidays.filter(holiday => holiday.holidayId !== holidayId);
          this.fetchHolidays();
          this.showNotification("Holiday deleted successfully!", "success");
        }
      } catch (error) {
        this.showNotification("Failed to delete holiday.", "error");
      }
    },

    // Close the popup
    closePopup() {
      this.addHolidayPopup = false;
      this.isEdit = false;
      this.holidayName = '';
      this.holidayDate = '';
    },

    // Show notification method
    showNotification(message, type) {
      this.notification = { message, type };
      setTimeout(() => {
        this.notification.message = "";
      }, 3000); // Hide notification after 3 seconds
    },

    // Change page for pagination
    changePage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
        this.fetchHolidays();
      }
    },
  },
  created() {
    this.fetchHolidays();
  },
};
</script>

<style>
.success {
  color: green;
}

.error {
  color: red;
}

.notification {
  padding: 10px;
  margin: 20px 0;
  border-radius: 5px;
  font-weight: bold;
}

.success {
  background-color: #4caf50;
  color: white;
}

.error {
  background-color: #f44336;
  color: white;
}
</style>
