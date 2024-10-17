<template>
  <header class="header">
    <div class="header-left">
      <span class="breadcrumb">Homepage</span>
      <span class="separator">|</span>
      <span class="breadcrumb">Batch Record</span>
    </div>
    <div class="header-right">
<!--      <img src="@/assets/icons/user-icon.svg" alt="User Icon" class="icon" />-->
<!--      <img src="@/assets/icons/arrow-icon.svg" alt="Arrow Icon" class="icon" />-->
    </div>
  </header>
</template>

<script>
export default {
  name: "AppHeader",
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #f7f7f7;
  border-bottom: 2px solid #ccc;
}

.header-left {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #333;
}

.breadcrumb {
  margin-right: 10px;
}

.separator {
  margin-right: 10px;
}

.header-right {
  display: flex;
  align-items: center;
}

.icon {
  width: 24px;
  height: 24px;
  margin-left: 10px;
  cursor: pointer;
}

.icon:hover {
  opacity: 0.7;
}
</style>
