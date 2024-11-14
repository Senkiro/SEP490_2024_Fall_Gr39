<template>
    <div class="container">
        <div class="headContent">
            <h1>Chapter 1 - Lesson 1</h1>
        </div>

        <div class="tab-buttons-container">
            <div class="tab-buttons">
                <button class="tab-button" :class="{ active: tabs.vocabulary }"
                    @click="showTab('vocabulary', 'kanji', 'grammar')">
                    <h3>Vocabulary</h3>
                </button>
                <button class="tab-button" :class="{ active: tabs.kanji }"
                    @click="showTab('kanji', 'vocabulary', 'grammar')">
                    <h3>Kanji</h3>
                </button>
                <button class="tab-button" :class="{ active: tabs.grammar }"
                    @click="showTab('grammar', 'vocabulary', 'kanji')">
                    <h3>Grammar</h3>
                </button>
            </div>
        </div>

        <div class="tab-container" v-if="tabs.vocabulary">
            <p>Vocabulary</p>
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


        <div class="tab-container" v-if="tabs.kanji">
            <p>Kanji</p>
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

        <div class="tab-container" v-if="tabs.grammar">
            <p>Grammar</p>
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
        return {
            isActive: false,
            tabs: {
                vocabulary: true,
                kanji: false,
                grammar: false,
            },
        }
    },
    methods: {
        openTextEditor() {
            this.isActive = true;
        },
        closeTextEditor() {
            this.isActive = false;
        },
        showTab(open, close1, close2) {
            this.tabs[open] = true;
            this.tabs[close1] = false;
            this.tabs[close2] = false;
        }
    }
}
</script>

<style></style>