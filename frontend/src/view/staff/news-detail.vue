<template>
  <div class="container">
    <div class="headContent">
      <h1>News Detail</h1>
    </div>

    <form>
      <!-- Input Title -->
      <div class="input-group">
        <label>News title <span class="required">*</span></label>
        <input v-model="news.newContent" placeholder="Enter news title" required />
      </div>

      <!-- Text Editor Content -->
      <div class="input-group">
        <label>News content <span class="required">*</span></label>
        <div class="text-editor-container">
          <TextEditor v-model="news.newTitle" />
        </div>
      </div>

      <!-- Action Buttons -->
      <div class="actions">
        <!-- Save as Draft -->
        <button
            type="button"
            class="action-button save-draft"
            @click="updateNews(false)"
            :disabled="isLoading"
        >
          <VsxIcon iconName="Save2" :size="25" color="#fff" type="bold" />
          <span v-if="isLoading">Saving...</span>
          <span v-else>Save Draft</span>
        </button>

        <!-- Publish News -->
        <button
            type="button"
            class="action-button publish-news"
            @click="updateNews(true)"
            :disabled="isLoading"
        >
          <VsxIcon iconName="ExportCircle" :size="25" color="#fff" type="bold" />
          <span v-if="isLoading">Publishing...</span>
          <span v-else>Publish News</span>
        </button>
      </div>
    </form>
  </div>
</template>


<script>
import TextEditor from "@/components/text-editor.vue";
import { VsxIcon } from "vue-iconsax";
import axios from "axios";
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

export default {
  name: "NewsDetail",
  components: { TextEditor, VsxIcon },
  setup() {
    const route = useRoute(); // Lấy ID từ route
    const router = useRouter();
    const news = ref({
      newId: "",
      newTitle: "",
      newContent: "",
      status: false, // false = Draft, true = Published
      createDate: "",
      createdBy: "",
    });
    const isLoading = ref(false);

    // Lấy dữ liệu từ API
    const fetchNewsDetail = async () => {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-news?newsId=${route.params.id}`,
            {
              headers: { Authorization: `Bearer ${token}` },
            }
        );
        if (response.data && response.data.result) {
          news.value = response.data.result;
        }
      } catch (error) {
        console.error("Error fetching news detail:", error);
        alert("Failed to fetch news detail. Please try again.");
      }
    };

    // Phương thức cập nhật bài viết
    const updateNews = async (status) => {
      if (!validateForm()) return; // Kiểm tra dữ liệu form

      isLoading.value = true;
      try {
        const token = sessionStorage.getItem("jwtToken");
        await axios.post(
            `http://localhost:8088/fja-fap/staff/update-news/${news.value.newId}`,
            {
              newTitle: news.value.newTitle,
              newContent: news.value.newContent,
              status,
              createdBy: sessionStorage.getItem("userName") || "Unknown",
              createDate: new Date().toISOString(),
            },
            {
              headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
              },
            }
        );
        const action = status ? "published" : "saved as draft";
        alert(`News ${action} successfully!`);
        router.push({ name: "News" }); // Quay lại danh sách tin tức
      } catch (error) {
        console.error("Error updating news:", error);
        alert("Failed to update news. Please try again.");
      } finally {
        isLoading.value = false;
      }
    };

    // Xác thực form
    const validateForm = () => {
      if (!news.value.newTitle.trim()) {
        alert("Title is required!");
        return false;
      }
      if (!news.value.newContent.trim()) {
        alert("Content is required!");
        return false;
      }
      return true;
    };

    onMounted(() => {
      fetchNewsDetail();
    });

    return { news, isLoading, updateNews };
  },
};
</script>



<style scoped>
</style>
