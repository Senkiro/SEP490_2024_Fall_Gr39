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

<style lang="scss">
.container {
  padding: 20px;

  .headContent {
    margin: 20px 0px 50px 0px;
  }

  .actions {
    display: flex;
    flex-direction: row;
    gap: 20px;
    justify-content: flex-end;
    margin: 20px 0px;
  }

  .filters {
    display: flex;
    flex-direction: row;
    gap: 40px;
    margin: 20px 0px;
    width: fit-content;

    select {
      font-size: 14px;
      font-weight: normal;
      padding: 10px 10px;
      border-radius: 10px;
      transition: 0.3s, ease-out;
      border: 1px solid #000000;
    }
  }

  .search {}

  .table-container {
    display: flex;
    flex-direction: column;
    gap: 40px;

    table {
      border-collapse: separate;
      border-radius: 20px;

      .center {
        text-align: center;
      }

      th {
        text-align: left;
        padding: 10px 20px;
        background: #dae4f3;
      }

      tr {
        text-align: left;

        td {
          padding: 5px 20px;
          border-radius: 20px;
        }
      }

      thead:first-child tr:first-child th:first-child,
      tbody:first-child tr:first-child td:first-child {
        border-radius: 20px 0 0 0;
      }

      thead:first-child tr:last-child th:last-child,
      tbody:last-child tr:last-child td:first-child {
        border-radius: 0 20px 0 0;
      }

      .status-progress {
        color: #304CB2;
      }

      .status-graduated {
        color: #6ECBB8;
      }

    }

    .pagination {
      display: flex;
      flex-direction: row;
      justify-content: center;
      gap: 10px;

      button {
        display: flex;
        width: 40px;
        height: 40px;
        padding: 0px;
        justify-content: center;
        align-content: center;
        border: 1px solid #000000;
        background: none;
        color: #171717;
      }
    }
  }
}
</style>
