<template>
  <div class="container">
    <div class="headContent">
      <h1>{{ eventDetail.eventName }}</h1>
    </div>
    <p>{{ eventDetail.address }}</p>
    <div>{{ eventDetail.eventDate }}</div>

    <div class="image-container">
      <img :src="`/${eventDetail.imagePath}`" alt="Event Image"/>
    </div>

    <div v-html="eventDetail.description" class="description-display"></div>

    <div class="actions">
      <button
          @click="openFeedbackPopup(eventDetail.eventDate)"
          class="give-feedback-button"
      >
        <VsxIcon iconName="Star" size="20" type="bold"/>
        Give Feedback
      </button>
    </div>

    <div v-if="showFeedbackPopup" class="popup-overlay">
      <div class="popup">
        <div class="exit-icon">
          <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="showFeedbackPopup=false"/>
        </div>
        <div class="popup-title">
          <h2>Add Feedback</h2>
        </div>

        <div id="rate" class="form-group">
          <label>Rate <span class="required"> *</span></label>
          <div class="rate-icon">
            <VsxIcon
                v-for="heart in 5"
                :key="heart"
                iconName="Heart"
                :style="{ color: heart <= feedbackForm.feedbackRate ? '#F28787' : '#ccc' }"
                size="24"
                type="bold"
                @click="setFeedbackRate(heart)"
            />
          </div>
        </div>

        <div class="form-group">
          <label id="label" for="feedback">Feedback </label>
          <textarea rows="10" id="feedback" v-model="feedbackForm.feedbackContent" required/>
        </div>
        <div class="actions">
          <button @click="submitFeedback">Submit</button>
        </div>
      </div>
    </div>

    <div v-if="eventFeedback" class="feedback-container">
      <h3>Your Feedback</h3>
      <div class="feedback-content">
        <div id="rate">
          <label>Rate: </label>
          <div class="rate-icon">
            {{ eventFeedback.feedbackRate }}
            <VsxIcon
                id="rate-icon"
                iconName="Heart"
                color="#F28787"
                size="24"
                type="bold"
            />
          </div>
        </div>
        <label>Content: </label>
        <div v-if="eventFeedback.feedbackContent">{{eventFeedback.feedbackContent}}</div>
        <div v-else>No feedback content</div>
      </div>
      <div class="actions">
      <button @click="editFeedback(eventFeedback.eventFeedbackId)">Edit Feedback</button>
      </div>
    </div>

    <div v-if="showEditPopup" class="popup-overlay">
      <div class="popup">
        <div class="exit-icon">
          <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="showEditPopup=false"/>
        </div>
        <div class="popup-title">
          <h2>Edit Feedback</h2>
        </div>

        <div id="rate" class="form-group">
          <label>Rate <span class="required"> *</span></label>
          <div class="rate-icon">
            <VsxIcon
                v-for="heart in 5"
                :key="heart"
                iconName="Heart"
                :style="{ color: heart <= editForm.feedbackRate ? '#F28787' : '#ccc' }"
                size="24"
                type="bold"
                @click="setEditFeedbackRate(heart)"
            />
          </div>
        </div>

        <div class="form-group">
          <label id="label" for="feedback">Feedback </label>
          <textarea rows="10" id="feedback" v-model="editForm.feedbackContent" required/>
        </div>
        <div class="actions">
          <button @click="submitEditFeedback">Save Changes</button>
        </div>
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
  data() {
    return {
      showEditPopup: false,
      editForm: {
        feedbackRate: 0,
        feedbackContent: "",
        eventId: this.$route.params.id,
        studentId: JSON.parse(sessionStorage.getItem("studentInfo")).studentId,
        event_feedback_id: "",
      },
      eventFeedback: null,
      showFeedbackPopup: false,
      eventId: this.$route.params.id,
      eventDetail: {
        eventName: "",
        address: "",
        imagePath: "",
        description: "",
        status: false,
      },
      feedbackForm: {
        feedbackRate: 0,
        feedbackContent: "",
        eventId: this.$route.params.id,
        studentId: JSON.parse(sessionStorage.getItem("studentInfo")).studentId,
      },
      notification: {
        message: "",
        type: "",
      },
    };
  },
  methods: {
    async fetchEventDetail() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
            `http://localhost:8088/fja-fap/student/get-event?eventId=${this.eventId}`,
            {headers: {Authorization: `Bearer ${token}`}}
        );
        this.eventDetail = response.data.result;
        const currentDate = new Date();
        const eventDate = new Date(this.eventDetail.eventDate);

        this.eventDetail.isEditable = currentDate > eventDate;
      } catch (error) {
        this.showNotification("Failed to fetch event details. Please try again.", "error");
      }
    },
    closeFeedbackPopup() {
      this.showFeedbackPopup = false;
      this.resetFeedbackForm();
    },
    setFeedbackRate(rate) {
      console.log("Rate selected:", rate);
      this.feedbackForm.feedbackRate = rate;
    },
    resetFeedbackForm() {
      this.feedbackForm.feedbackRate = 0;
      this.feedbackForm.feedbackContent = "";
    },
    async submitFeedback() {
      try {
        const token = sessionStorage.getItem("jwtToken");

        const response = await axios.post(
            "http://localhost:8088/fja-fap/staff/create-event-feedback",
            this.feedbackForm,
            {headers: {Authorization: `Bearer ${token}`}}
        );

        if (response.status === 200) {
          this.showNotification("Feedback submitted successfully!", "success");
          this.closeFeedbackPopup();

          // Gọi API tính toán lại avg-rate
          await this.calculateAverageRate();
        } else {
          this.showNotification("Failed to submit feedback. Please try again.", "error");
        }
      } catch (error) {
        this.showNotification("Error submitting feedback. Please try again.", "error");
        console.error("Error:", error);
      }
    },
    async calculateAverageRate() {
      try {
        const token = sessionStorage.getItem("jwtToken");

        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/calculate-avg-rate-event?event_id=${this.eventId}`,
            {headers: {Authorization: `Bearer ${token}`}}
        );

        if (response.status === 200) {
          console.log("success")
        } else {
          this.showNotification("Failed to update average rate. Please try again.", "error");
        }
      } catch (error) {
        this.showNotification("Error updating average rate. Please try again.", "error");
        console.error("Error:", error);
      }
    },
    showNotification(message, type) {
      this.notification.message = message;
      this.notification.type = type;
      setTimeout(() => {
        this.notification.message = "";
      }, 3000);
    },
    async fetchEventFeedback() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const studentId = sessionStorage.getItem("userId");
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-event-feedback-by-student?student_id=${studentId}&event_id=${this.eventId}`,
            {headers: {Authorization: `Bearer ${token}`}}
        );

        if (response.data.code === 0) {
          this.eventFeedback = response.data.result;
        } else {
          this.eventFeedback = null;
        }
      } catch (error) {
        console.error("Error fetching feedback:", error);
        this.eventFeedback = null;
        //this.showNotification("Error fetching feedback. Please try again.", "error");
      }
    },
    openFeedbackPopup(eventDate) {
      const today = new Date();
      const eventDateObj = new Date(eventDate);

      if (today < eventDateObj) {
        this.showNotification("The event hasn't started yet. You cannot submit feedback.", "error");
        return;
      }

      // Kiểm tra nếu phản hồi đã được gửi
      if (this.eventFeedback) {
        this.showNotification("You have already submitted feedback.", "error");
        return;
      }

      // Hiển thị pop-up nếu các điều kiện trên không được thỏa mãn
      this.showFeedbackPopup = true;
    },

    editFeedback(event_feedback_id) {
      this.editForm.feedbackRate = this.eventFeedback.feedbackRate;
      this.editForm.feedbackContent = this.eventFeedback.feedbackContent;
      this.editForm.event_feedback_id = event_feedback_id;
      this.showEditPopup = true;
    },
    closeEditPopup() {
      this.showEditPopup = false;
    },
    setEditFeedbackRate(rate) {
      this.editForm.feedbackRate = rate;
    },
    async submitEditFeedback() {
      try {
        const token = sessionStorage.getItem("jwtToken");

        const response = await axios.post(
            `http://localhost:8088/fja-fap/staff/update-event-feedback?event_feedback_id=${this.editForm.event_feedback_id}`, // Truyền ID trong URL
            this.editForm,
            {headers: {Authorization: `Bearer ${token}`}}
        );

        if (response.status === 200) {
          this.showNotification("Feedback updated successfully!", "success");
          this.calculateAverageRate();
          this.fetchEventFeedback(); // Tải lại feedback đã chỉnh sửa
          this.closeEditPopup();
        } else {
          this.showNotification("Failed to update feedback. Please try again.", "error");
        }
      } catch (error) {
        this.showNotification("Error updating feedback. Please try again.", "error");
        console.error("Error:", error);
      }
    },
  },
  mounted() {
    this.fetchEventDetail();
    this.fetchEventFeedback();
  },
};
</script>

<style lang="scss" scoped>
#rate-icon{
  margin-top: -3px;
}
#label {
  align-self: flex-start;
}

textarea {
  padding: 10px;
  border-radius: 5px;
  font-size: 16px;
}

#feedback {
  width: 300px;
  height: 100px;
  text-align: start;
}

#rate {
  display: flex;
  width: 100%;
  gap: 10px;
}

.rate-icon {
  display: flex;
  flex-direction: row;
  gap: 5px;
  justify-content: start;
  width: 300px;
}

.feedback-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 20px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #f9f9f9;
}

.feedback-content{
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.feedback-content p {
  margin: 5px 0;
}

.feedback-content span {
  font-weight: bold;
}

.rating {
  display: flex;
  justify-content: center;
  margin: 10px 0;

  .vsx-icon {
    cursor: pointer;
    color: #ccc;

    &.selected {
      color: #F28787;
    }
  }
}

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