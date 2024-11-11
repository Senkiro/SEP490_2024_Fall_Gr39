<template>
  <div class="container">
    <div class="headContent">
      <h1>Create News</h1>
    </div>


    <div class="rich-text-editor">

    </div>


    <div class="actions">
      <button class="action-button save-draft">
        <VsxIcon iconName="Save2" :size="32" color="#5584FF" type="linear" />
        Save draft
      </button>
      <button class="action-button publish-news">
        <VsxIcon iconName="ExportCircle" :size="32" color="#5584FF" type="bold" />
        Publish news
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: "CreateNews"
};
</script>

<style scoped>
.container {
  padding: 20px;
}

.headContent {
  font-size: 24px;
  color: #304CB2;
  margin-bottom: 20px;
}

.rich-text-editor {
  margin-bottom: 20px;
  min-height: 300px;
  border: 1px solid #ddd;
  padding: 10px;
}

.actions {
  display: flex;
  gap: 20px;
  justify-content: flex-end;
}

.action-button {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  background-color: #304CB2;
  color: white;
  font-size: 16px;
  cursor: pointer;
}

.save-draft {
  background-color: #5584FF;
}

.publish-news {
  background-color: #304CB2;
}
</style>
