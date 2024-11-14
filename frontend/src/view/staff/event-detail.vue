<template>
    <div class="container">
        <div class="headContent">
            <h1>Trip to Tokyo tower</h1>
            <p>4 Chome-2-8 Shibakoen, Minato City, Tokyo 105-0011, Japan</p>
        </div>
        <form @submit.prevent="submitForm">
            <div class="image-container">
                <img src="../../assets/tokyo-tower-japan-260nw-236583016.png">

                <div class="middle">
                    <button class="edit-btn">
                        <VsxIcon iconName="Image" type="bold" color="#fff" />
                        <label for="img">
                            Upload an image
                        </label>
                        <input type="file" id="img" name="img" accept="image/*">
                    </button>
                </div>
            </div>

            <p>Event Information</p>
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
    </div>
</template>

<script>
import TextEditor from '@/components/text-editor.vue';
import { VsxIcon } from 'vue-iconsax';


export default {
    components: {
        VsxIcon,
        TextEditor
    },
    data() {
        return {
            isActive: false,
            tabs: {
                blue: true,
                green: false,
                red: false,
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
        img {
            opacity: 0.3;
        }

        .middle {
            opacity: 1;
        }
    }
}
</style>