<template>
  <div class="container">
    <div class="headContent">
      <template v-if="isActive">
        <div class="input-group">
          <label for="lessonTitle">Title <span class="required">*</span></label>
          <input v-model="lessonDetails.lessonTitle" id="lessonTitle" class="input-field"
                 placeholder="Enter exam name"/>
        </div>
      </template>
      <template v-else>
        <h1>{{ lessonDetails.lessonTitle }}</h1>
      </template>
    </div>

    <!-- Tabs -->
    <div class="tab-buttons-container">
      <div class="tab-buttons">
        <button class="tab-button" :class="{ active: tabs.vocabulary }"
                @click="showTab('vocabulary', 'kanji', 'grammar')">
          <h4>Vocabulary</h4>
        </button>
        <button class="tab-button" :class="{ active: tabs.kanji }"
                @click="showTab('kanji', 'vocabulary', 'grammar')">
          <h4>Kanji</h4>
        </button>
        <button class="tab-button" :class="{ active: tabs.grammar }"
                @click="showTab('grammar', 'vocabulary', 'kanji')">
          <h4>Grammar</h4>
        </button>
      </div>
    </div>

    <!-- Tabs Content -->
    <div class="tab-container" v-if="tabs.vocabulary">
      <p v-if="!isActive" v-html="lessonDetails.vocabulary || 'No information'"></p>
      <TextEditor v-if="isActive" v-model="lessonDetails.vocabulary" id="description"/>
    </div>

    <div class="tab-container" v-if="tabs.kanji">
      <p v-if="!isActive" v-html="lessonDetails.kanji || 'No information'"></p>
      <TextEditor v-if="isActive" v-model="lessonDetails.kanji" id="description"/>
    </div>

    <div class="tab-container" v-if="tabs.grammar">
      <p v-if="!isActive" v-html="lessonDetails.grammar || 'No information'"></p>
      <TextEditor v-if="isActive" v-model="lessonDetails.grammar" id="description"/>
    </div>

    <!-- Notification -->
    <div v-if="notification.message" :class="['notification', notification.type]">
      {{ notification.message }}
    </div>

    <!-- Hidden VsxIcon -->
    <VsxIcon v-if="false" iconName="Edit2" />
  </div>
</template>

<script>
import {VsxIcon} from "vue-iconsax";
import TextEditor from "../../components/text-editor.vue";
import axios from "axios";

export default {
  name: "LessonDetail",
  components: {
    TextEditor,
    VsxIcon
  },
  data() {
    return {
      isDisabled: true,
      isActive: false,
      tabs: {
        vocabulary: true,
        kanji: false,
        grammar: false,
      },
      lessonDetails: {
        lessonTitle: "",
        vocabulary: "",
        kanji: "",
        grammar: "",
      },
      notification: {
        message: "",
        type: "",
      },
    };
  },
  methods: {
    async fetchLessonDetails() {
      const id = this.$route.params.id;
      if (!id) {
        console.error("No ID provided in the route.");
        return;
      }

      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
            `http://localhost:8088/fja-fap/staff/get-lesson/${id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );

        if (response.data.code === 0) {
          this.lessonDetails = response.data.result;
        } else {
          console.error("Error fetching lesson details:", response.data);
        }
      } catch (error) {
        console.error("API Error:", error);
      }
    },
    showTab(open, close1, close2) {
      this.tabs[open] = true;
      this.tabs[close1] = false;
      this.tabs[close2] = false;
    },
  },
  mounted() {
    this.fetchLessonDetails();
  },
};
</script>


<style>
</style>