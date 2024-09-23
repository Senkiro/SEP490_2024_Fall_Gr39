<template>
  <header class="app-header">
    <img src="@/assets/logo.png" alt="App Logo" class="logo" />
    <h1>Login Page</h1>
  </header>
</template>

<script>
export default {
  name: "AppHeader",
};
</script>

<style scoped>
.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  background-color: #3eaf7c;
  color: white;
}

.logo {
  height: 40px;
}

h1 {
  margin: 0;
  font-size: 24px;
}
</style>
