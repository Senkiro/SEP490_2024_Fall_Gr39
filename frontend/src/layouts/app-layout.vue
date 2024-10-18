<template>
  <div class="layout">
    <Sidebar />
    <div class="content">
      <Header />
      <router-view></router-view>
      <Footer />
    </div>
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
  }
};
</script>

<style scoped>
/* Đảm bảo bố cục hiển thị đúng */
.sidebar {
  width: 200px; /* Đặt kích thước cụ thể cho Sidebar */
}
.layout {
  display: flex;
}

.content {
  flex: 1;
  margin-left: 236px; /* Đảm bảo phù hợp với chiều rộng của Sidebar */
  padding: 20px;
}

.content > Header {
  padding-bottom: 20px;
}
</style>
