<template>
    <div class="container">
        <div class="headContent">
            <template v-if="isActive">
                <div class="input-group">
                    <label for="lessonTitle">Title <span class="required">*</span></label>
                    <input v-model="lessonDetails.lessonTitle" id="lessonTitle" class="input-field"
                        placeholder="Enter exam name" />
                </div>
            </template>
            <template v-else>
                <h1>{{ lessonDetails.lessonTitle }}</h1>
            </template>     
        </div>

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

        <div class="tab-container" v-if="tabs.vocabulary">
            <p v-if="!isActive" v-html="lessonDetails.vocabulary || 'No informarion'"></p>
            <TextEditor v-if="isActive" v-model="lessonDetails.vocabulary" id="description" />

            <div class="actions">
                <button v-if="!isActive" @click="openTextEditor('vocabulary')">
                    <VsxIcon iconName="Edit2" color="#fff" type="bold" />
                    Edit
                </button>
                <button v-if="isActive" @click="updateLesson">
                    <VsxIcon iconName="Save2" color="#fff" type="bold" />
                    Save
                </button>
                <button v-if="isActive" @click="cancelUpdate">
                    <VsxIcon iconName="CloseCircle" color="#fff" type="bold" />
                    Cancel
                </button>
            </div>
        </div>

        <div class="tab-container" v-if="tabs.kanji">
            <p v-if="!isActive" v-html="lessonDetails.kanji || 'No information'"></p>
            <TextEditor v-if="isActive" v-model="lessonDetails.kanji" id="description" />

            <div class="actions">
                <button v-if="!isActive" @click="openTextEditor('vocabulary')">
                    <VsxIcon iconName="Edit2" color="#fff" type="bold" />
                    Edit
                </button>
                <button v-if="isActive" @click="updateLesson">
                    <VsxIcon iconName="Save2" color="#fff" type="bold" />
                    Save
                </button>
                <button v-if="isActive" @click="cancelUpdate">
                    <VsxIcon iconName="CloseCircle" color="#fff" type="bold" />
                    Cancel
                </button>
            </div>
        </div>

        <div class="tab-container" v-if="tabs.grammar">
            <p v-if="!isActive" v-html="lessonDetails.grammar || 'No information'"></p>
            <TextEditor v-if="isActive" v-model="lessonDetails.grammar" id="description" />

            <div class="actions">
                <button v-if="!isActive" @click="openTextEditor('vocabulary')">
                    <VsxIcon iconName="Edit2" color="#fff" type="bold" />
                    Edit
                </button>
                <button v-if="isActive" @click="updateLesson">
                    <VsxIcon iconName="Save2" color="#fff" type="bold" />
                    Save
                </button>
                <button v-if="isActive" @click="cancelUpdate">
                    <VsxIcon iconName="CloseCircle" color="#fff" type="bold" />
                    Cancel
                </button>
            </div>
        </div>

        <div v-if="notification.message" :class="['notification', notification.type]">
            {{ notification.message }}
        </div>

    </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";
import TextEditor from "../../components/text-editor.vue";
import axios from "axios";

export default {
    name: "LessonDetail",
    components: {
        VsxIcon,
        TextEditor,
    },
    data() {
        return {
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
        async updateLesson() {
            const id = this.$route.params.id;
            if (!id) {
                console.error("No lesson ID provided in the route.");
                this.notification = {
                    message: "Invalid Lesson ID!",
                    type: "error",
                };
                return;
            }

            try {
                const token = sessionStorage.getItem("jwtToken");
                const payload = {
                    lessonTitle: this.lessonDetails.lessonTitle || "Default Title",
                    vocabulary: this.lessonDetails.vocabulary || "",
                    kanji: this.lessonDetails.kanji || "",
                    grammar: this.lessonDetails.grammar || "",
                };

                const response = await axios.post(
                    `http://localhost:8088/fja-fap/staff/update-lesson/${id}`,
                    payload,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "application/json",
                        },
                    }
                );

                if (response.status === 200) {
                    // Show success notification
                    this.notification = {
                        message: "Lesson updated successfully!",
                        type: "success",
                    };
                    this.isActive = false; // Exit edit mode
                } else {
                    // Show error notification
                    this.notification = {
                        message: "Failed to update lesson. Please try again.",
                        type: "error",
                    };
                }
            } catch (error) {
                console.error("Error updating lesson:", error);
                // Show error notification
                this.notification = {
                    message: "An error occurred while updating the lesson.",
                    type: "error",
                };
            }

            // Clear notification after a timeout
            setTimeout(() => {
                this.notification.message = "";
            }, 3000);
        },
        openTextEditor() {
            this.isActive = true;
        },
        cancelUpdate() {
            this.isActive = false;
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
.notification {
    padding: 10px 20px;
    border-radius: 5px;
    margin: 10px 0;
    font-size: 16px;
    font-weight: bold;
}

.notification.success {
    color: #155724;
    background-color: #d4edda;
    border: 1px solid #c3e6cb;
}

.notification.error {
    color: #721c24;
    background-color: #f8d7da;
    border: 1px solid #f5c6cb;
}
</style>