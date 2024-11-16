<template>
  <div class="container">
    <div class="headContent">
    </div>

    <div class="filters">
        <option value="FALL2024">FALL2024</option>
      </select>

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
          <th class="center">Action</th>
        </tr>
        </thead>
        <tbody>
          <td class="center">
          </td>
        </tr>
        </tbody>

        <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">
          <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717" />
        </button>
          {{ page }}
        </button>
        <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">
          <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717" />
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {

  }
</script>
