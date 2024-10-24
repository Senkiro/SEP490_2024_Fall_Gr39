<template>
  <footer class="footer">
    <div class="footer-content">
      <p>
        FPT Japan Academy - 4-3-5 Higashi Nippori Arakawa-ku, Tokyo 116-0014 - Main (+84) 985 125 020 - Support (+84) 971 023 041
      </p>
      <p class="footer-links">
        <a href="#">Privacy Policy</a> | <a href="#">Support</a> | © 2024 XXX
      </p>
    </div>
  </footer>
</template>

<script>
export default {
  name: "AppFooter",
};
</script>

<style scoped>
.footer {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  border-top: 1px solid #01447e;
}
.footer-content {
  text-align: center;
  font-size: 12px;
  color: #01447e;
}
.footer-links a:hover {
  text-decoration: underline;
}
</style>
