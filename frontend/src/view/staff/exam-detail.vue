<template>
  <div class="container">
    <div class="headContent">
      <h1>{{ examDetails?.examTitle || "Exam Title Unavailable" }}</h1>
    </div>

    <p v-if="!isActive" v-html="examDetails?.examContent || 'No content available'"></p>
    <TextEditor v-if="isActive" v-model="examDetails.examContent" id="examContentEditor" />

    <div class="actions">
      <!-- Edit Button -->
      <button v-if="!isActive" @click="openTextEditor()">
        <VsxIcon iconName="Edit2" color="#fff" type="bold" />
        Edit
      </button>
      <!-- Save and Cancel Buttons -->
      <div v-if="isActive">
        <button @click="saveExamContent()">
          <VsxIcon iconName="Save2" color="#fff" type="bold" />
          Save
        </button>
        <button @click="closeTextEditor()">
          <VsxIcon iconName="CloseCircle" color="#fff" type="bold" />
          Cancel
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";
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
      examDetails: {},
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
    openTextEditor() {
      this.isActive = true;
    },
    closeTextEditor() {
      this.isActive = false;
    },
    async saveExamContent() {
      const id = this.$route.params.id;
      if (!id || !this.examDetails.examContent) {
        console.error("Invalid ID or content.");
        this.notification = "Invalid ID or content.";
        return;
      }

      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.post(
            `http://localhost:8088/fja-fap/staff/update-exam/${id}`,
            {
              examContent: this.examDetails.examContent,
            },
            {
              headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
              },
            }
        );

        if (response.status === 200) {
          console.log("Exam content updated successfully.");
          this.notification = "Exam content updated successfully.";
          this.isActive = false; // Exit edit mode
        } else {
          console.error("Error updating exam content:", response.data);
          this.notification = "Failed to update exam content.";
        }
      } catch (error) {
        console.error("Error updating exam content:", error);
        this.notification = "An error occurred while updating the content.";
      }
    },
  },
  mounted() {
    this.fetchExamDetails();
  },
};
</script>


<style></style>