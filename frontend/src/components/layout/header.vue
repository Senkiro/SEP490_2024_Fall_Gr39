<template>
  <header class="header">
    <div class="header-left">
      <ul class="breadcrumb">
        <li><router-link to="/homepage">Homepage</router-link></li>
        <li><router-link to="/section1">Section 1</router-link></li>
        <li><router-link to="/section2">Section 2</router-link></li>
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

/* Thêm kiểu cho logout icon */
.logout-icon {
  margin-left: 16px;
}
</style>
