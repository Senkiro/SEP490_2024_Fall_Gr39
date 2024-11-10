<template>
  <div class="app">
    <component :is="layout"></component>
  </div>
</template>



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

<style lang="scss">
:root {
  --primary: #1A2C6F;
  --primary-alt: #253f9f;
  --light: #f1f5f9;
  --side-background: #DFE7FB;
  --sidebar-width: 250px;
  --border: #495057;
  --linear: linear-gradient(90deg, #3E5DD4, #223374);
}

*{
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: sans-serif;
}

body{
  background: #fff;
}

button{
  cursor: pointer;
}

.app{
  display: flex;

  main{
    flex: 1 1 0;
    padding: 2rem;

    @media(max-width: 768px){
      padding-left: 6rem;
    }
  }
}

h1{
  width: fit-content;
  font-size: 36px;
  background: -webkit-linear-gradient(180deg, #304CB2, #1A2C6F);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-weight: bold;
  margin: 20px 0px;
}

h2{
  width: fit-content;
  font-size: 26px;
  background: -webkit-linear-gradient(180deg, #304CB2, #1A2C6F);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-weight: bold;
  margin: 20px 0px;
}

h3{
  font-size: 16px;
  font-weight: normal;
}

button {
  background-image: linear-gradient(90deg, #3E5DD4, #223374);
  padding: 10px 20px;
  color: #fff;
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: normal;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  gap: 10px;
}


.btn-cancel {
  background: #ccc;
  color: #fff;
}
</style>