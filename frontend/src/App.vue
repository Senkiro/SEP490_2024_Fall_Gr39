<template>
  <component :is="layout"></component>
</template>
<script>
// layouts
import appLayout from "./layouts/app-layout.vue";
import authLayout from "./layouts/auth-layout.vue";

// import "@/assets/sass/app.scss";

export default {
  metaInfo: {
    title: "Nihon Study Guide",
    titleTemplate: "%s | Nihon - KKK",
  },
  components: {
    app: appLayout,
    auth: authLayout,
  },
  computed: {
    layout() {
      return this.$route.meta.layout === "auth" ? authLayout : appLayout;
    },
  },
  data() {
    return {};
  },
  mounted() {
  },
  methods: {},
};
</script>
