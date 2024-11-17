<template>
  <div class="container">
    <div class="headContent">
      <template v-if="isActive">
        <input v-model="eventDetail.eventName" class="input-field" placeholder="Enter event name" />
      </template>
      <template v-else>
        <h1>{{ eventDetail.eventName }}</h1>
      </template>

      <!-- Địa chỉ sự kiện -->
      <template v-if="isActive">
        <input v-model="eventDetail.address" class="input-field" placeholder="Enter address" />
      </template>
      <template v-else>
        <p>{{ eventDetail.address }}</p>
      </template>
    </div>
    <form @submit.prevent="submitForm">
      <div class="image-container">
        <img :src="`/${eventDetail.imagePath}`" alt="Event Image">
        <div class="middle">
          <template v-if="isActive">
            <button class="edit-btn">
              <VsxIcon iconName="Image" type="bold" color="#fff" />
              <label for="img">
                Upload an image
              </label>
              <input
                  type="file"
                  id="img"
                  name="img"
                  accept="image/*"
                  @change="handleImageChange"
                  style="display: none;"
              />
            </button>
          </template>
        </div>
      </div>

      <div>
        <template v-if="isActive">
          <TextEditor v-model= eventDetail.description   @input="updateDescription" />
        </template>
        <template v-else >
          <div v-html=eventDetail.description></div>
        </template>
      </div>

      <div v-if="!isActive" class="actions">
        <button v-if="!isActive" @click="openTextEditor">
          <VsxIcon iconName="Edit2" color="#fff" type="bold" />
          Edit
        </button>
      </div>
      <div v-if="isActive" class="actions">
        <button v-if="isActive" @click="saveChanges">
          <VsxIcon iconName="Save2" color="#fff" type="bold" />
          Save
        </button>
      </div>
    </form>

    <h2>Feedback</h2>
    <div class="tab-buttons-container">
      <div class="tab-buttons">
        <button class="tab-button" :class="{ active: tabs.blue }"
                @click="showTab('blue', 'green', 'red')">
          <h3>Blue</h3>
        </button>
        <button class="tab-button" :class="{ active: tabs.green }"
                @click="showTab('green', 'red', 'blue')">
          <h3>Green</h3>
        </button>
        <button class="tab-button" :class="{ active: tabs.red }"
                @click="showTab('red', 'blue', 'green')">
          <h3>Red</h3>
        </button>
      </div>
    </div>

    <div v-if="notification.message" :class="['notification', notification.type]">
      {{ notification.message }}
    </div>

  </div>
</template>

<script>
import TextEditor from '@/components/text-editor.vue';
import { VsxIcon } from 'vue-iconsax';
import axios from "axios";

export default {
  components: {
    VsxIcon,
    TextEditor
  },
  data() {
    return {
      isActive: false,
      eventId: this.$route.params.eventId,
      tabs: {
        blue: true,
        green: false,
        red: false,
      },
      eventDetail: {
        eventName: '',
        address: '',
        imagePath: '',
        description: '',
        status: false
      },
      notification: {
        message: '',
        type: ''
      },
    }
  },
  methods: {
    openTextEditor() {
      this.isActive = true;
    },
    showTab(open, close1, close2) {
      this.tabs[open] = true;
      this.tabs[close1] = false;
      this.tabs[close2] = false;
    },
    updateDescription(value) {
      if (typeof value === 'string') {
        this.eventDetail.description = value;
      } else {
        console.error("Invalid description format:", value);
      }
    },
    handleImageChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.selectedImage = file;
      }
    },
    async fetchEventDetail() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-event?eventId=${this.eventId}`,
            {headers: {Authorization: `Bearer ${token}`}}
        );
        this.eventDetail = response.data.result;
      } catch (error) {
        this.$emit("showNotification", "Failed to fetch event details. Please try again.", "error");
      }
    },
    async saveChanges() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const formData = new FormData();

        // Thêm dữ liệu JSON
        formData.append(
            "eventDetail",
            new Blob(
                [JSON.stringify({
                  eventName: this.eventDetail.eventName,
                  address: this.eventDetail.address,
                  description: this.eventDetail.description,
                  imagePath: this.eventDetail.imagePath,
                })],
                {type: "application/json"}
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

        window.location.reload();
        this.showNotification('Event update successfully!', 'success');
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
    }
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
  }
}
</script>

<style lang="scss" scoped>
.headContent {
  p {
    font-style: italic;
    color: #979B9F;
  }
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
    cursor: pointer;
  }

  .middle {
    transition: .5s ease;
    opacity: 0;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    -ms-transform: translate(-50%, -50%);

    #img {
      display: none;
    }
  }

  &:hover {
    .middle {
      opacity: 1;
    }
  }
}

.input-field,
.textarea-field{
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
</style>
