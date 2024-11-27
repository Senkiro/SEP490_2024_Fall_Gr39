<template>
  <div class="container">
    <div class="headContent">
      <div class="input-group">
        <template v-if="isActive">
          <label for="eventName">Title <span class="required">*</span></label>
          <input v-model="eventDetail.eventName" id="eventName" class="input-field" placeholder="Enter event name" />
        </template>
        <template v-else>
          <h1>{{ eventDetail.eventName || N/A }}</h1>
        </template>
      </div>

      <div class="input-group">
        <template v-if="isActive">
          <label for="address">Address <span class="required">*</span></label>
          <input v-model="eventDetail.address" id="address" class="input-field" placeholder="Enter address" />
        </template>
        <template v-else>
          <i>{{ eventDetail.address ||N/A }}</i>
        </template>
      </div>
    </div>

    <form @submit.prevent="submitForm">
      <div class="image-container">
        <img :src="previewImage || `/${eventDetail.imagePath}` " alt="Event Image" />
        <template v-if="isActive">
          <div class="middle">
            <label for="img" class="upload-btn">
              <VsxIcon iconName="Image" type="bold" color="#fff" />
              Upload an image
              <input type="file" id="img" name="img" accept="image/*" @change="handleImageChange"
                style="display: none;" />
            </label>
          </div>
        </template>
      </div>

      <div>
        <template v-if="isActive">
          <div class="input-group">
            <label for="description">Description</label>
            <TextEditor v-model="eventDetail.description" @input="updateDescription" id="description" />
          </div>
        </template>
        <template v-else>
          <div v-html="eventDetail.description" class="description-display"></div>
        </template>
      </div>

      <div v-if="!isActive" class="actions">
        <button @click="openTextEditor" class="edit-btn">
          <VsxIcon iconName="Edit2" color="#fff" type="bold" />
          Edit
        </button>
      </div>
      <div v-if="isActive" class="actions">
        <button @click="saveChanges" class="save-btn">
          <VsxIcon iconName="Save2" color="#fff" type="bold" />
          Save
        </button>
        <button @click="cancelEdit" class="cancel-btn">
          <VsxIcon iconName="CloseCircle" color="#fff" type="bold" />
          Cancel
        </button>
      </div>
    </form>

    <h2>Feedback</h2>
    <div class="tab-buttons-container">
      <div class="tab-buttons">
        <button class="tab-button" :class="{ active: tabs.blue }" @click="showTab('blue', 'green', 'red')">
          <h4>Blue</h4>
        </button>
        <button class="tab-button" :class="{ active: tabs.green }" @click="showTab('green', 'red', 'blue')">
          <h4>Green</h4>
        </button>
        <button class="tab-button" :class="{ active: tabs.red }" @click="showTab('red', 'blue', 'green')">
          <h4>Red</h4>
        </button>
      </div>
    </div>

    <div v-if="notification.message" :class="['notification', notification.type]">
      {{ notification.message }}
    </div>
  </div>
</template>


<script>
import TextEditor from "@/components/text-editor.vue";
import { VsxIcon } from "vue-iconsax";
import axios from "axios";

export default {
  components: {
    VsxIcon,
    TextEditor,
  },
  data() {
    return {
      isActive: false,
      backupEventDetail: {}, // Lưu trạng thái ban đầu để khôi phục khi cancel
      eventId: this.$route.params.eventId,
      tabs: {
        blue: true,
        green: false,
        red: false,
      },
      eventDetail: {
        eventName: "",
        address: "",
        imagePath: "",
        description: "",
        status: false,
      },
      selectedImage: null, // Lưu trữ tệp ảnh được chọn
      previewImage: "", // Lưu URL ảnh xem trước
      notification: {
        message: "",
        type: "",
      },
    };
  },
  methods: {
    openTextEditor() {
      this.isActive = true;
      this.backupEventDetail = JSON.parse(JSON.stringify(this.eventDetail)); // Lưu trạng thái ban đầu
    },
    cancelEdit() {
      this.isActive = false;
      this.eventDetail = JSON.parse(JSON.stringify(this.backupEventDetail)); // Khôi phục trạng thái ban đầu
      this.previewImage = ""; // Xóa URL xem trước nếu có
    },
    showTab(open, close1, close2) {
      this.tabs[open] = true;
      this.tabs[close1] = false;
      this.tabs[close2] = false;
    },
    updateDescription(value) {
      if (typeof value === "string") {
        this.eventDetail.description = value;
      } else {
        console.error("Invalid description format:", value);
      }
    },
    handleImageChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.selectedImage = file;
        this.previewImage = URL.createObjectURL(file); // Tạo URL ảnh xem trước
      }
    },
    async fetchEventDetail() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
          `http://localhost:8088/fja-fap/staff/get-event?eventId=${this.eventId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        this.eventDetail = response.data.result;
      } catch (error) {
        this.$emit(
          "showNotification",
          "Failed to fetch event details. Please try again.",
          "error"
        );
      }
    },
    async saveChanges() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const formData = new FormData();

        if (this.previewImage) {
          URL.revokeObjectURL(this.previewImage); // Giải phóng URL
          this.previewImage = ""; // Reset URL xem trước
        }

        // Thêm dữ liệu JSON
        formData.append(
          "eventDetail",
          new Blob(
            [
              JSON.stringify({
                eventName: this.eventDetail.eventName,
                address: this.eventDetail.address,
                description: this.eventDetail.description,
                imagePath: this.eventDetail.imagePath,
              }),
            ],
            { type: "application/json" }
          )
        );

        // Thêm file ảnh nếu có
        if (this.selectedImage) {
          formData.append("image", this.selectedImage);
        }

        const response = await axios.post(
          `http://localhost:8088/fja-fap/staff/update-event/${this.eventId}`,
          formData,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "multipart/form-data",
            },
          }
        );

        if (response.data && response.data.result) {
          this.eventDetail = response.data.result;
        }

        this.isActive = false;
        this.showNotification("Event updated successfully!", "success");
      } catch (error) {
        console.error("Error saving changes:", error);
        this.showNotification("Failed to update. Please try again.", "error");
      }
    },
    showNotification(message, type) {
      this.notification = { message, type };

      // Lưu thông báo vào sessionStorage
      sessionStorage.setItem("notification", JSON.stringify({ message, type }));

      // Xóa thông báo sau 3 giây
      setTimeout(() => {
        this.notification.message = "";
        sessionStorage.removeItem("notification");
      }, 3000);
    },
  },
  mounted() {
    this.fetchEventDetail();

    // Kiểm tra nếu có thông báo trong sessionStorage
    const storedNotification = sessionStorage.getItem("notification");
    if (storedNotification) {
      const { message, type } = JSON.parse(storedNotification);
      this.notification = { message, type };

      // Xóa thông báo sau khi hiển thị
      setTimeout(() => {
        this.notification.message = "";
        sessionStorage.removeItem("notification");
      }, 3000);
    }
  },
};
</script>


<style lang="scss" scoped>
i{
  color: #b9b9b9;
}

.image-container {
  width: 100%;
  position: relative;

  img {
    display: block;
    opacity: 1;
    width: 100%;
    border-radius: 20px;
    height: 250px;
    object-fit: cover;
    object-position: center;
    transition: .5s ease;
    backface-visibility: hidden;
  }

  .middle {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 200px;
    height: 50px;
    border-radius: 20px;
    background: #b9b9b9;
    transition: .5s ease;
    opacity: 0;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    -ms-transform: translate(-50%, -50%);
    color: #fff;
    cursor: pointer;

    #img {
      display: none;
    }

    .upload-btn {
      display: flex;
      align-items: center;
      gap: 20px;
      cursor: pointer;
    }
  }

  &:hover {
    .middle {
      opacity: 1;
    }
  }
}

.input-field,
.textarea-field {
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
</style>
