<template>
  <Sidebar />
  <div class="content">
    <Header />
    <router-view></router-view>
    <Footer />
  </div>
</template>

<script>
// Import các component
import Sidebar from '@/components/layout/sidebar.vue';
import Header from '@/components/layout/header.vue';
import Footer from '@/components/layout/footer.vue';
export default {
  components: {
    Sidebar,
    Header,
    Footer
  },
};
</script>

<style scoped>
.content {
  width: 100%;
  display: flex;
  padding: 0px 24px;
  flex-direction: column;
}

/* Đảm bảo bố cục hiển thị đúng */
</style>

<style>
.row {
  overflow-y: hidden;
}
</style>
