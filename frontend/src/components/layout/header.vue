<template>
  <header class="header">
    <div class="header-left">
      <ul class="breadcrumb">
        <li><router-link to="/homepage">Homepage</router-link></li>
        <li>Current Page</li>
      </ul>
    </div>
    <div class="header-right">
      <slot name="user-actions">
        <VsxIcon iconName="UserSquare" :size="30" color="#01447e" type="linear" class="icon" />
        <VsxIcon
            iconName="Logout"
            :size="30"
            color="#01447e"
            type="linear"
            class="icon logout-icon"
            @click="logout"
        style="cursor: pointer;"
        />
      </slot>
    </div>
  </header>
</template>

<script>
import { VsxIcon } from "vue-iconsax";
import { useRouter } from "vue-router";

export default {
  name: "AppHeader",
  components: {
    VsxIcon
  },
  setup() {
    const router = useRouter();

    const logout = () => {
      sessionStorage.clear();
      router.push("/login");
    };
    return { logout };
  }
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  border-bottom: 2px dashed #01447e;
  max-height: 50px;
}

ul.breadcrumb {
  padding: 10px 16px;
  list-style: none;
  margin: 0;
}

/* Display list items side by side */
ul.breadcrumb li {
  display: inline;
  font-size: 16px;
}

/* Add a slash symbol (/) before/behind each list item */
ul.breadcrumb li+li:before {
  padding: 8px;
  color: black;
  content: "|\00a0";
}

/* Add a color to all links inside the list */
ul.breadcrumb li a {
  color: #01447e;
  text-decoration: none;
}

/* Thêm kiểu cho logout icon */
.logout-icon {
  margin-left: 16px;
}
</style>
