<template>
  <component :is="layout"></component>
  <VsxIcon :iconName="iconName" color="blue" size="50" type="linear" />
</template>
<script>
// layouts
import appLayout from "./layouts/app-layout.vue";
import authLayout from "./layouts/auth-layout.vue";

export default {
  props:['iconName'],
  metaInfo: {
    title: "Nihon Study GuideEntity",
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
