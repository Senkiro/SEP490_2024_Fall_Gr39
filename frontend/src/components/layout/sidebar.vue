<template>
  <nav class="sidebar">
    <ul class="sidebar-menu">
      <li class="sidebar-item active">
        <router-link to="/homepage">
           Homepage
        </router-link>
      </li>
      <li class="sidebar-item">
        <router-link to="/record">
           Record
        </router-link>
      </li>
      <li class="sidebar-item">
        <router-link to="/batch-record">
           Batch Record
        </router-link>
      </li>
      <li class="sidebar-item">
        <router-link to="/schedule">
           Schedule Management
        </router-link>
      </li>
      <!-- Add more sidebar items here -->
    </ul>
  </nav>
</template>

<script>
export default {
  name: "AppSidebar",
};
</script>

<style scoped>
.sidebar {
  width: 250px;
  height: 100vh;
  background-color: #e0ecff;
  padding: 20px;
  position: fixed;
}

.sidebar-menu {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar-item {
  margin: 20px 0;
  display: flex;
  align-items: center;
}

.sidebar-item .icon {
  width: 24px;
  height: 24px;
  margin-right: 10px;
}

.sidebar-item.active {
  background-color: #d1e0ff;
  border-radius: 10px;
  padding: 10px;
}

.sidebar-item:hover {
  background-color: #c0d0ff;
  border-radius: 10px;
  padding: 10px;
}

.sidebar-item a {
  text-decoration: none;
  color: #333;
  font-size: 16px;
  font-weight: bold;
}
</style>
