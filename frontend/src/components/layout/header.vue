<template>
  <header class="header">
    <div class="header-left">
      <ul class="breadcrumb">
        <li v-for="breadcrumb in this.$route.meta.breadcrumbs" :key="breadcrumb.name">
          <router-link :to="breadcrumb.link">{{ breadcrumb.name }}</router-link>
        </li>
      </ul>
    </div>
    <div class="header-right">
      <slot name="user-actions">
        <VsxIcon
            iconName="UserSquare"
            :size="30"
            color="#01447e"
            type="linear"
            class="icon"
            @click="navigateToUserProfile"
        />
        <VsxIcon iconName="Logout" :size="30" color="#01447e" type="linear" class="icon logout-icon" @click="logout"
          style="cursor: pointer;" />
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

    const userId = sessionStorage.getItem("userId") || "defaultUserId";

    const logout = () => {
      sessionStorage.clear();
      router.push("/login");
    };

    const navigateToUserProfile = () => {
      router.push({ name: "UserProfile", params: { id: userId } });
    };

    return { logout , navigateToUserProfile };
  },
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
  padding: 10px 0;
  list-style: none;
  margin: 0;
}

ul.breadcrumb li {
  display: inline;
  font-size: 16px;
}

ul.breadcrumb li+li:before {
  padding: 8px;
  color: black;
  content: "|\00a0";
}

ul.breadcrumb li a {
  color: #01447e;
  text-decoration: none;
}

.logout-icon {
  margin-left: 16px;
}
</style>
