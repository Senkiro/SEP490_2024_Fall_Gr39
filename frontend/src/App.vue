<template>
  <component :is="layout"></component>
  <VsxIcon :iconName="iconName" color="blue" size="50" type="linear" />
</template>

<style>
.headContent{
  margin:20px;
}

.pageTitle {
  display: block;
}

h1 {
  width: fit-content;
  font-size: 36px;
  background: -webkit-linear-gradient(180deg, #304CB2, #1A2C6F);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-weight: bold;
}

.container {
  padding: 20px;
}

button{
  background-image: linear-gradient(90deg, #3E5DD4, #223374);
  padding: 10px, 20px;
  color: #fff;
  display: flex;
  align-items: center;
  padding: 10px 20px;
  font-size: 14px;
  font-weight: semibold;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>

<script>
// layouts
import appLayout from "./layouts/app-layout.vue";
import authLayout from "./layouts/auth-layout.vue";

export default {
  props: ['iconName'],
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


