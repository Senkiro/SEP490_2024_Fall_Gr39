<template>
    <VaCard class="p">
    <div class="row">
      <div class="flex flex-col md2 sidebar">
        <Sidebar />
      </div>
      <div class="flex flex-col md10 content">
        <Header />
        <router-view></router-view>
        <Footer />
      </div>
    </div>
  </VaCard>
</template>

<script>
// Import các component
import Sidebar from '@/components/layout/sidebar.vue';
import Header from '@/components/layout/header.vue';
import Footer from '@/components/layout/footer.vue';
import { VaCard } from "vuestic-ui";

export default {
  components: {
    Sidebar,
    Header,
    Footer
  },
};
export{ VaCard};
</script>

<style scoped>
.content{
  padding-right: 24px;
}
/* Đảm bảo bố cục hiển thị đúng */
</style>
