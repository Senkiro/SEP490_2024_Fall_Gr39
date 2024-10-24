import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { VsxIcon } from "vue-iconsax";
import { createVuestic } from "vuestic-ui";
import "vuestic-ui/css";


const app = createApp(App);
app.use(router);

app.use(createVuestic({
    config: {
        colors: {
          variables: {
            // Default colors
            primary: "#21A2C6F",
            normalText: "#171717",
            background: "#DFE7FB",
            blurText: "#979B9F",
            success: "#6ECBB8",
          },
        },
      },
})).mount("#app");
app.component("VsxIcon", VsxIcon);


