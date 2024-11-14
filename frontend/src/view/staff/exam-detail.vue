<template>
    <div class="container">
        <div class="headContent">
            <h1>Chapter 1 - Vocabulary</h1>
        </div>

        <p>Exam Content</p>
        <TextEditor v-if="isActive" />

        <div v-if="!isActive" class="actions">
            <button @click="openTextEditor()">
                <VsxIcon iconName="Edit2" color="#fff" type="bold" />
                Edit
            </button>
        </div>
        <div v-if="isActive" class="actions">
            <button @click="closeTextEditor()">
                <VsxIcon iconName="Save2" color="#fff" type="bold" />
                Save
            </button>
        </div>

    </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";
import TextEditor from '../../components/text-editor.vue'

export default {
    name: "LessonDetail",
    components: {
        VsxIcon,
        TextEditor
    },
    data() {
        isActive = false;
    },
    methods:{
        openTextEditor() {
            this.isActive = true;
        },
        closeTextEditor() {
            this.isActive = false;
        }
    }
}
</script>

<style></style>