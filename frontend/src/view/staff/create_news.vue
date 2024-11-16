<template>
  <div class="container">
    <div class="headContent">
      <h1>Create News</h1>
    </div>

    <form>
      <TextEditor />
    </form>

    <div class="actions">
      <button class="action-button save-draft">
        <VsxIcon iconName="Save2" :size="25" color="#fff" type="bold" />
        Save draft
      </button>
      <button class="action-button publish-news">
        <VsxIcon iconName="ExportCircle" :size="25" color="#fff" type="bold" />
        Publish news
      </button>
    </div>
  </div>
</template>

<script>
import TextEditor from "@/components/text-editor.vue";

export default {
  name: "CreateNews",
  components: { TextEditor },
};
</script>

<style scoped>
</style>
