<template>
  <div class="container">
    <div class="headContent">
      <h1>Class Record</h1>
    </div>
    <div class="actions">
      <button @click="openAddClassPopup">
        <VsxIcon iconName="AddCircle" size="20" type="bold" />
        Add class
      </button>
    </div>
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th class="center">No</th>
            <th>Class</th>
            <th>Number of students</th>
            <th class="center">Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(classItem, index) in classes" :key="classItem.id">
            <td class="center">{{ index + 1 }}</td>
            <td :style="{ color: classItem.classColor }">{{ classItem.name }}</td>
            <td>{{ classItem.studentCount }}</td>
            <td class="center">
              <VsxIcon iconName="Eye" :size="30" color="#171717" type="linear" @click="viewClassDetail(classItem)" />
            </td>
          </tr>
          <tr>
            <td colspan="4" class="center">No record.</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <!-- Popup Add Class -->
  <div v-if="showAddClassPopup" class="popup-overlay">
    <div class="popup">
      <div class="exit-icon">
        <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="showAddClassPopup = false" />
      </div>
      <div class="popup-title">
        <h2>Add class</h2>
      </div>

      <form @submit.prevent="addClass">
        <div class="form-group">
          <label for="className">Class name <span class="required">*</span></label>
          <input type="text" id="className" v-model="newClass.name" required />
        </div>
        <div class="form-group">
          <label for="classColor">Color <span class="required">*</span></label>
          <input type="color" id="classColor" v-model="newClass.color" required />
        </div>
        <div class="actions">
          <button class="btn-submit" type="submit">Create</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";

export default {
  name: "ClassRecord",
  components: { VsxIcon },
  data() {
    return {
      batchEntity: { name: 'FALL2024' },
      classes: [
        { id: 1, name: 'Blue', classColor: 'blue', studentCount: 30 },
        { id: 2, name: 'Red', classColor: 'red', studentCount: 30 },
        { id: 3, name: 'Green', classColor: 'green', studentCount: 30 },
        { id: 4, name: 'Yellow', classColor: 'yellow', studentCount: 30 },
        { id: 5, name: 'Purple', classColor: 'purple', studentCount: 30 }
      ]
    };
  },
  methods: {
    viewClassDetail(classItem) {
      console.log("Viewing class: ", classItem);
    }
  }
};
</script>

<style scoped></style>
