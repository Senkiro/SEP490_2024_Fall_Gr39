<template>
  <div class="container">
    <div class="headContent">
      <template v-if="isActive">
        <div class="input-group">
          <label for="examTitle">Title <span class="required">*</span></label>
          <input v-model="examDetails.examTitle" id="examTitle" class="input-field"
                 placeholder="Enter exam name"/>
        </div>
        <div class="input-group">
          <label for="type-filter">Type <span class="required">*</span></label>
          <div class="filters">
            <select id="type-filter" class="filter-select" v-model="examDetails.examTypeId">
              <option value="" disabled>Select Exam Type</option>
              <option v-for="type in examTypes" :key="type.examType" :value="type.examType">
                {{ type.examName }} (Rate: {{ type.examRate }}%)
              </option>
            </select>
          </div>
        </div>

      </template>
      <template v-else>
        <h1>{{ examDetails?.examTitle || "Exam Title Unavailable" }}</h1>
        <i>Exam type: <b>{{ examDetails?.examTypeRate?.examName}}</b></i>
      </template>
    </div>

    <p v-if="!isActive" v-html="examDetails?.examContent || 'No content available'"></p>
    <TextEditor v-if="isActive" v-model="examDetails.examContent" id="examContentEditor"/>


    <div class="actions">
      <!-- Edit Button -->
      <button v-if="!isActive" @click="openTextEditor()">
        <VsxIcon iconName="Edit2" color="#fff" type="bold"/>
        Edit
      </button>
      <!-- Save and Cancel Buttons -->
      <template v-if="isActive">
        <button @click="saveExamContent()">
          <VsxIcon iconName="Save2" color="#fff" type="bold"/>
          Save
        </button>
        <button class="btn-cancel" @click="closeTextEditor()">
          <VsxIcon iconName="CloseCircle" color="#fff" type="bold"/>
          Cancel
        </button>
      </template>
    </div>

    <div v-if="notification" :class="['notification', notificationType]">
      <p>{{ notification }}</p>
    </div>

  </div>
</template>

<script>
import {VsxIcon} from "vue-iconsax";
import TextEditor from "../../components/text-editor.vue";
import axios from "axios";

export default {
  name: "ExamDetail",
  components: {
    VsxIcon,
    TextEditor,
  },
  data() {
    return {
      isActive: false,
      examDetails: {
        examType: "",
      },
      examTypes: [],
      notification: "",
    };
  },
  methods: {
    async fetchExamDetails() {
      const id = this.$route.params.id;
      if (!id) {
        console.error("No ID provided in the route.");
        this.notification = "No ID provided.";
        return;
      }

      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-exam/${id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );

        if (response.data.code === 0) {
          this.examDetails = response.data.result;
        } else {
          console.error("Error fetching exam details:", response.data);
        }
      } catch (error) {
        console.error("API Error:", error);
      }
    },
    async fetchExamTypes() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-all-exam-type`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );

        if (response.data.code === 0) {
          this.examTypes = response.data.result.map((type) => ({
            examType: type.examType,
            examName: type.examName,
            examRate: type.examRate,
          }));
          console.log("Fetched Exam Types:", this.examTypes);
        } else {
          console.error("Error fetching exam types:", response.data);
        }
      } catch (error) {
        console.error("Error fetching exam types:", error);
      }
    },
    openTextEditor() {
      this.isActive = true;
    },
    closeTextEditor() {
      this.isActive = false;
    },
    async saveExamContent() {
      const id = this.$route.params.id;

      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.post(
            `http://localhost:8088/fja-fap/staff/update-exam/${id}`,
            {
              examTitle: this.examDetails.examTitle,
              examContent: this.examDetails.examContent,
              examType: this.examDetails.examTypeId,
            },
            {
              headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
              },
            }
        );

        if (response.status === 200 && response.data.code === 0) {
          console.log("Exam content updated successfully.");
          this.notification = "Exam content updated successfully.";
          this.notificationType = "success";
          this.isActive = false;
          this.fetchExamDetails();
        } else {
          this.notification = response.data.message || "Failed to update exam content.";
          this.notificationType = "error";
        }
      } catch (error) {
        console.error("Error updating exam content:", error);
        this.notification = "An error occurred while updating the content.";
        this.notificationType = "error";
      }
    }
  },
  mounted() {
    this.fetchExamDetails();
    this.fetchExamTypes();
  },
};
</script>


<style lang="scss" scoped>
.filter-select {
  border-color: #ccc !important;
}

.notification {
  padding: 10px;
  border-radius: 5px;
  margin-top: 10px;
  text-align: center;
  font-size: 16px;
  font-weight: bold;
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