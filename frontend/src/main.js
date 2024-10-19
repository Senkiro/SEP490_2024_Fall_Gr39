import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { VsxIcon } from "vue-iconsax";

const app = createApp(App);
app.component("VsxIcon", VsxIcon);
app.use(router);
app.mount('#app');
