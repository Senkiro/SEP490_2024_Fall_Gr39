<template>
  <div class="container">
    <div class="headContent">
      <h1>Create news</h1>
    </div>

    <form>
      <div class="input-group">
        <label>News title <span class="required">*</span></label>
        <input v-model="news.newContent" placeholder="Enter news title" required/>
      </div>
      <div class="input-group">
        <label>News content <span class="required">*</span></label>
        <div class="text-editor-container">
          <TextEditor v-model="news.newTitle" />
        </div>
      </div>

      <div class="actions">
        <button
            type="button"
            class="action-button save-draft"
            @click="saveDraft"
            :disabled="isLoading"
        >
          <VsxIcon iconName="Save2" :size="25" color="#fff" type="bold" />
          <span v-if="isLoading">Saving...</span>
          <span v-else>Save draft</span>
        </button>
        <button
            type="button"
            class="action-button publish-news"
            @click="publishNews"
            :disabled="isLoading"
        >
          <VsxIcon iconName="ExportCircle" :size="25" color="#fff" type="bold" />
          <span v-if="isLoading">Publishing...</span>
          <span v-else>Publish news</span>
        </button>
      </div>
    </form>
  </div>
</template>

<script>
import TextEditor from "@/components/text-editor.vue";
import { VsxIcon } from "vue-iconsax";
import axios from "axios";

export default {
  name: "CreateNews",
  components: { TextEditor, VsxIcon },
  data() {
    return {
      news: {
        newTitle: "",
        newContent: "",
        createDate: new Date().toISOString().split("T")[0],
        createdBy: sessionStorage.getItem("userName") || "Unknown",
      },
      isLoading: false,
    };
  },
  methods: {
    validateForm() {
      if (!this.news.newTitle.trim()) {
        this.showNotification("Title is required!", "error");
        return false;
      }
      if (!this.news.newContent.trim()) {
        this.showNotification("Content is required!", "error");
        return false;
      }
      return true;
    },
    async handleNewsSubmission(status) {
      if (!this.validateForm()) return;

      this.isLoading = true;
      try {
        const token = sessionStorage.getItem("jwtToken");
        await axios.post(
            "http://localhost:8088/fja-fap/staff/create-news",
            {
              ...this.news,
              status,
            },
            {
              headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
              },
            }
        );
        const action = status ? "published" : "saved as draft";
        localStorage.setItem(
            "notification",
            JSON.stringify({
              message: `News ${action} successfully!`,
              type: "success",
            })
        );
        this.resetForm();
        this.$router.push({ name: "News" });
      } catch (error) {
        const action = status ? "publish" : "save draft";
        this.showNotification(`Failed to ${action}. Please try again.`, "error");
      } finally {
        this.isLoading = false;
      }
    },
    saveDraft() {
      this.handleNewsSubmission(false);
    },
    publishNews() {
      this.handleNewsSubmission(true);
    },
    showNotification(message, type) {
      alert(`${type.toUpperCase()}: ${message}`);
    },
    resetForm() {
      this.news.newTitle = "";
      this.news.newContent = "";
    },
  },
};
</script>

<style scoped>
.textarea-field{
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
.required{
  color: red;
}
</style>

