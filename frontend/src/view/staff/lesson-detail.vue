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

        <div v-if="tabs.vocabulary">
            <p>Vocabulary</p>
            <div class="actions">
                <button @click="showAddBatchPopup = true">
                    <VsxIcon iconName="Edit" size="20" type="linear" />
                    Edit
                </button>
            </div>

            <div class="editor-container">
                <TextEditor :v-model="content" />
            </div>
            <div class="output-group">
                <label>Content</label>
                <code>{{ content }}</code>
            </div>
        </div>


        <div v-if="tabs.kanji">
            <p>Kanji</p>
        </div>

        <div v-if="tabs.grammar">
            <p>Grammar</p>
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
            tabs: {
                vocabulary: true,
                kanji: false,
                grammar: false,
            },
            content: '<p>A Vue.js wrapper component for Tiptap to use <code>v-model</code>.</p>,'
        }
    },
    methods: {
        showTab(open, close1, close2) {
            this.tabs[open] = true;
            this.tabs[close1] = false;
            this.tabs[close2] = false;
        }
    }
}
</script>

<style></style>