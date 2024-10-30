import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { VsxIcon } from "vue-iconsax";
import { createVuestic } from "vuestic-ui";
import "vuestic-ui/css";


const app = createApp(App);
app.use(router);
app.use(createVuestic()).mount("#app");
app.component("VsxIcon", VsxIcon);


