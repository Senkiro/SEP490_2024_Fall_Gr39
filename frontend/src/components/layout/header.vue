<template>
  <header class="header">
    <div class="header-left">
      <ul class="breadcrumb">
        <li><router-link to="/homepage">Homepage</router-link></li>
        <li><router-link to="/homepage">Homepage</router-link></li>
        <li><router-link to="/homepage">Homepage</router-link></li>
        <li>Homepage</li>
      </ul>
    </div>
    <div class="header-right">
      <slot name="user-actions">
        <VsxIcon iconName="UserSquare" :size="30" color="#01447e" type="linear" class="icon"/>
        <VsxIcon iconName="Logout" :size="30" color="#01447e" type="linear" class="icon"/>
      </slot>
    </div>

  </header>
</template>

<script>
import { VsxIcon } from "vue-iconsax";

export default {
  name: "AppHeader",
  components: {
    VsxIcon
  },
  props: ['iconName']
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

ul.breadcrumb li {
  color: #979B9F;
  text-decoration: none;
}

/* Add a color on mouse-over */
ul.breadcrumb li a:hover {
  color: #01447e;
  text-decoration: underline;
}

.icon{
  padding: 10px;
}
</style>
