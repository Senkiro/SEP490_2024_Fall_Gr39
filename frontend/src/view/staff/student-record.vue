<template>
  <div class="container">
    <div class="headContent">
      <h1>Student Record</h1>
    </div>

    <div class="actions">
      <button @click="openAddStudentPopup">
        <VsxIcon iconName="AddCircle" size="20" type="bold" />
        Add student
      </button>
      <button @click="navigateToImportStudent">
        <VsxIcon iconName="Import" size="20" type="bold" />
        Import student
      </button>
    </div>

    <div class="actions">
      <div class="search-container">
        <input type="text" placeholder="Search..." class="search-field">
        <VsxIcon iconName="SearchNormal1" color="#ADB5BD" type="linear" />
      </div>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th class="center">No</th>
            <th>Fullname</th>
            <th>Roll number</th>
            <th>Japanese name</th>
            <th>Class</th>
            <th>Email</th>
            <th class="center">Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(student, index) in students" :key="student.id">
            <td class="center">{{ index + 1 }}</td>
            <td>{{ student.fullname }}</td>
            <td>{{ student.rollNumber }}</td>
            <td>{{ student.japaneseName }}</td>
            <td :style="{ color: student.classColor }">{{ student.class }}</td>
            <td>{{ student.email }}</td>
            <td class="center">
              <VsxIcon iconName="Eye" :size="30" color="#171717" type="linear" @click="navigateToProfile(student.id)" />
            </td>
          </tr>
        </tbody>
      </table>

      <div class="pagination">
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
  </div>

  <div v-if="showAddStudentPopup" class="popup-overlay">
    <div class="popup">
      <div class="popup-title">
        <h2>Add student</h2>
      </div>
      <form @submit.prevent="addStudent">
        <div class="form-group">
          <label for="fullname">Full name <span class="required">*</span></label>
          <input type="text" id="fullname" v-model="newStudent.fullname" required />
        </div>
        <div class="form-group">
          <label for="japaneseName">Japanese name <span class="required">*</span></label>
          <input type="text" id="japaneseName" v-model="newStudent.japaneseName" required />
        </div>
        <div class="form-group">
          <label for="email">Email <span class="required">*</span></label>
          <input type="email" id="email" v-model="newStudent.email" required />
        </div>
        <div class="form-group">
          <label for="class">Class <span class="required">*</span></label>
          <select id="class" v-model="newStudent.class" required>
            <option value="">Choose class now or later</option>
            <option value="Blue">Blue</option>
            <option value="Red">Red</option>
            <option value="Green">Green</option>
            <option value="Yellow">Yellow</option>
            <option value="Purple">Purple</option>
          </select>
        </div>
        <div class="form-group">
          <label for="dob">DOB <span class="required">*</span></label>
          <input type="date" id="dob" v-model="newStudent.dob" placeholder="dd/mm/yyyy" required />
        </div>
        <div class="form-group">
          <label>Gender <span class="required">*</span></label>
          <div class="gender-group">
            <div class="radio">
              <input type="radio" id="male" value="Male" v-model="newStudent.gender" required />
              <label for="male">Male</label>
            </div>
            <div class="radio">
              <input type="radio" id="female" value="Female" v-model="newStudent.gender" required />
              <label for="female">Female</label>
            </div>
          </div>
        </div>
        <div class="form-group">
          <label for="phone">Phone <span class="required">*</span></label>
          <input type="text" id="phone" v-model="newStudent.phone" required />
        </div>
        <div class="actions">
          <button class="btn btn-cancel" @click="showAddStudentPopup = false">Cancel</button>
          <button type="submit">Create</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";

export default {
  name: "StudentRecord",
  components: { VsxIcon },
  data() {
  },
  methods: {

  }
};
</script>

<style scoped></style>
