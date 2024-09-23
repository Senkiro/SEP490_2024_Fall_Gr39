<template>
  <div id="app">
    <Header />
    <router-view></router-view>
  </div>
</template>

<script>
import Header from './components/layout/header.vue';

export default {
  name: 'App',
  components: {
    Header
  }
}
</script>

<style>
/* CSS nếu có */
</style>
