<template>
  <div class="container">
    <div class="headContent">
      <h1>Lesson list</h1>
    </div>

    <div class="actions">
      <button @click="showAddLessonPopup = true">
        <VsxIcon iconName="AddCircle" size="20" type="bold" />
        Add Lesson
      </button>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th class="center">No</th>
            <th>Chapter</th>
            <th class="center">Action</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td class="center">1</td>
            <td class="chapter" @click="toggleExpand()">Chapter 1: Introduction to Dekiru</td>
            <td class="center">
              <div class="icon-group">
                <VsxIcon iconName="Edit2" :size="25" color="#171717" type="linear" />
              </div>
            </td>
          </tr>
          <template v-if="isExpanded">
            <tr class="expandable-rows">
              <td class="center"></td>
              <td class="tab-index">Chapter 1: Lesson 1</td>
              <td class="center">
                <div class="icon-group">
                  <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear" />
                </div>
              </td>
            </tr>
            <tr class="expandable-rows">
              <td class="center"></td>
              <td class="tab-index">Chapter 1: Lesson 2</td>
              <td class="center">
                <div class="icon-group">
                  <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear" />
                </div>
              </td>
            </tr>
            <tr>
              <td class="center"></td>
              <td class="tab-index">Chapter 1: Lesson 3</td>
              <td class="center">
                <div class="icon-group">
                  <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear" @click="viewLessonDetail()" />
                </div>
              </td>
            </tr>
          </template>
          <tr>
            <td class="center">2</td>
            <td class="chapter" @click="toggleExpand()">Chapter 2: Introduction to Dekiru</td>
            <td class="center">
              <div class="icon-group">
                <VsxIcon iconName="Edit2" :size="25" color="#171717" type="linear" />
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="pagination" v-if="totalPages > 0">
        <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">
          <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717" />
        </button>
        <button v-for="page in displayedPages" :key="page" :class="{ active: page === currentPage }"
          @click="changePage(page)">
          {{ page }}
        </button>
        <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">
          <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717" />
        </button>
      </div>
    </div>

    <div v-if="showAddLessonPopup" class="popup-overlay">
      <div class="popup">
        <div class="popup-title">
          <h2>Add Lesson</h2>
        </div>
        <form @submit.prevent="addLesson">
          <div class="form-group">
            <label for="eventName">Lesson name<span class="required">*</span></label>
            <input type="text" id="lessonName" required />
          </div>
          <div class="form-group">
            <label for="address">Chapter<span class="required">*</span></label>
            <div class="filters">
              <select id="room-filter" class="filter-select">
                <option value="">Chapter 1</option>
                <option value="">Chapter 2</option>
              </select>
            </div>
          </div>
          <div class="actions">
            <button class="btn-cancel" @click="showAddLessonPopup = false">Cancel</button>
            <button type="submit">Create</button>
          </div>
        </form>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import { VsxIcon } from 'vue-iconsax';

export default {
  components: {
    VsxIcon
  },
  data() {
    return {
      isExpanded: true,
      showAddLessonPopup: false,
    }
  },
  methods: {
    toggleExpand() {
      this.isExpanded = !this.isExpanded;
    },
    viewLessonDetail() {
      this.$router.push({ name: 'LessonDetail' });
    },
  }

}
</script>

<style lang="scss" scoped>
.container {
  .table-container {
    table {
      tr {
        .chapter {
          cursor: pointer;
        }

        td:nth-child(2) {
          font-weight: normal;
        }

      }

    }
  }
}
</style>