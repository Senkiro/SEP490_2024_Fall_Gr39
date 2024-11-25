<template>
  <div class="container">
    <div class="headContent">
      <h1>Attendance report</h1>
    </div>

    <div class="filters">
      <select id="batch-filter" class="filter-select">
        <option value="">Batch</option>
        <option value="FALL2024">FALL2024</option>
      </select>

      <select id="class-filter" class="filter-select">
        <option value="">Class</option>
        <option value="Blue">Blue</option>
      </select>
    </div>


    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th class="center">No</th>
          <th>Fullname</th>
          <th>Roll number</th>
          <th>Japanese name</th>
          <th class="center">Attended</th>
          <th class="center">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td class="center">1</td>
          <td>Pham The Minh</td>
          <td>FA171392</td>
          <td>ファム・テ・ミン</td>
          <td class="center">12/13</td>
          <td class="center">
            <VsxIcon iconName="Eye" :size="30" color="#171717" type="linear" />
          </td>
        </tr>
        <tr>
          <td colspan="6" class="center">No record.</td>
        </tr>
        </tbody>

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
      </table>
    </div>
  </div>
</template>

<script>
export default {

}
</script>

<style lang="scss" scoped></style>