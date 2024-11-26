<template>
  <div class="container">
    <div class="headContent">
      <h1>{{ eventDetail.eventName }}</h1>
      <p>{{ eventDetail.address }}</p>
    </div>

    <div class="image-container">
      <img :src="`/${eventDetail.imagePath}`" alt="Event Image"/>
    </div>

    <div v-html="eventDetail.description" class="description-display"></div>

    <h2>Feedback</h2>
    <div class="tab-buttons-container">
      <div class="tab-buttons">
        <button class="tab-button" :class="{ active: tabs.blue }" @click="showTab('blue', 'green', 'red')">
          <h3>Blue</h3>
        </button>
        <button class="tab-button" :class="{ active: tabs.green }" @click="showTab('green', 'red', 'blue')">
          <h3>Green</h3>
        </button>
        <button class="tab-button" :class="{ active: tabs.red }" @click="showTab('red', 'blue', 'green')">
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
import axios from "axios";

export default {
  components: {},
  data() {
    return {
      isActive: false,
      eventId: this.$route.params.id,
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
    showTab(open, close1, close2) {
      this.tabs[open] = true;
      this.tabs[close1] = false;
      this.tabs[close2] = false;
    },
    async fetchEventDetail() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
            `http://localhost:8088/fja-fap/student/get-event?eventId=${this.eventId}`,
            {headers: {Authorization: `Bearer ${token}`}}
        );
        this.eventDetail = response.data.result;
      } catch (error) {
        this.$emit("showNotification", "Failed to fetch event details. Please try again.", "error");
      }
    },

  },
  mounted() {
    this.fetchEventDetail();
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