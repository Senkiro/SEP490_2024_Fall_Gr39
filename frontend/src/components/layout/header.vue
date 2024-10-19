<template>
  <header class="header">
    <div class="header-left">
      <slot name="breadcrumb">
        <span class="breadcrumb">Trang chủ</span>
        <span class="separator">|</span>
        <span class="breadcrumb">Bản ghi lô hàng</span>
      </slot>
    </div>
    <div class="header-right">
      <slot name="user-actions">
        <VsxIcon iconName="User" :size="32" color="#5584FF" type="linear" />
        <VsxIcon iconName="Logout" :size="32" color="#5584FF" type="linear" />
      </slot>
    </div>

  </header>
</template>

<script>
import {VsxIcon} from "vue-iconsax";

export default {
  name: "AppHeader",
  components: {
    VsxIcon
  },
  props:['iconName']
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
  width: 32px; /* tăng kích thước để dễ kiểm tra */
  height: 32px;
  color: #000; /* thử thêm màu đen để dễ nhận thấy */
}

.icon:hover {
  opacity: 0.7;
}
</style>
