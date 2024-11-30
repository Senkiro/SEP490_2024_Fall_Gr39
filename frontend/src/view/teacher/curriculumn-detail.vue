<template>
  <div class="container">
    <div class="headContent">
      <h1>{{ curriculumnTitle }}</h1>
    </div>

    <div class="btn-group">
      <button @click="switchTab('curriculumn')" :class="{ 'active-tab': activeTab === 'curriculumn' }">
        Curriculumn
      </button>
      <button @click="switchTab('lesson')" :class="{ 'active-tab': activeTab === 'lesson' }">
        Lesson
      </button>
      <button @click="switchTab('exam')" :class="{ 'active-tab': activeTab === 'exam' }">
        Exam
      </button>
    </div>

    <!-- Curriculumn Tab -->
    <div v-if="activeTab === 'curriculumn'" class="record">
      <div class="table-container">
        <table id="curriculumn">
          <thead>
          <tr>
            <th class="center">Session</th>
            <th>Lesson</th>
            <th>Exam</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(item, index) in curriculumnList" :key="index">
            <td class="center">{{ item.sessionNumber }}</td>
            <td>{{ item.lessonResponse?.lessonTitle || 'N/A' }}</td>
            <td>{{ item.examResponse?.examTitle || 'N/A' }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div v-if="activeTab === 'lesson'" class="record">
      <div class="table-container">
        <table>
          <thead>
          <tr>
            <th class="center">Session No</th>
            <th>Lesson</th>
            <!--            <th class="center">Vocabulary</th>-->
            <!--            <th class="center">Kanji</th>-->
            <!--            <th class="center">Grammar</th>-->
            <th class="center">Action</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(item, index) in curriculumnList" :key="index">
            <td class="center">{{ index + 1 }}</td>
            <td>{{ item.lessonResponse?.lessonTitle || 'N/A' }}</td>
            <!--            <td class="center">{{ item.lessonResponse?.vocabulary || 'N/A' }}</td>-->
            <!--            <td class="center">{{ item.lessonResponse?.kanji || 'N/A' }}</td>-->
            <!--            <td class="center">{{ item.lessonResponse?.grammar || 'N/A' }}</td>-->
            <td class="center">
              <div class="icon-group">
                <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear"
                         @click="viewLessonDetail(item.lessonResponse?.lessonId)"/>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
        <div class="pagination" v-if="totalPages > 0">
          <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">
            <VsxIcon iconName="ArrowLeft2" size="20" type="linear" color="#171717"/>
          </button>
          <button v-for="page in displayedPages" :key="page" :class="{ active: page === currentPage }"
                  @click="changePage(page)">
            {{ page }}
          </button>
          <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">
            <VsxIcon iconName="ArrowRight2" size="20" type="linear" color="#171717"/>
          </button>
        </div>
      </div>
    </div>

    <div v-if="activeTab === 'exam'" class="record">
      <div class="table-container">
        <table>
          <thead>
          <tr>
            <th class="center">Session No</th>
            <th>Exam</th>
            <th class="center">Action</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(item, index) in curriculumnList" :key="index">
            <td class="center">{{ index + 1 }}</td>
            <td>{{ item.examResponse?.examTitle || 'N/A' }}</td>
            <td class="center" v-if="item.examResponse">
              <div class="icon-group">
                <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear"
                         @click="viewExamDetail(item.examResponse?.examId)"/>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      activeTab: 'curriculumn',
      curriculumnTitle: "",
      curriculumnList: [],
    }
  },
  methods: {
    switchTab(tab) {
      this.activeTab = tab;
    },
    viewLessonDetail(id) {
      if (!id) {
        console.error("Lesson ID is missing.");
        return;
      }
      this.$router.push({ name: 'TeacherLessonDetail', params: { id } });
    },
    viewExamDetail(id) {
      if (!id) {
        console.error("Exam ID is missing.");
        return;
      }
      this.$router.push({ name: 'TeacherExamDetail', params: { id } });
    },
    async fetchCurriculumData() {
      const id = this.$route.params.id;

      if (!id) {
        console.error("Invalid ID: No ID provided in the route.");
        return;
      }

      try {
        const token = sessionStorage.getItem("jwtToken");
        const [curriculumnResponse, detailedResponse] = await Promise.all([
          axios.get(`http://localhost:8088/fja-fap/staff/get-curriculumn-list/${id}`, {
            headers: { Authorization: `Bearer ${token}` },
          }),
          axios.get(
              `http://localhost:8088/fja-fap/staff/get-by-curriculumn-list/${id}?page=0&size=100`,
              { headers: { Authorization: `Bearer ${token}` } }
          ),
        ]);

        if (curriculumnResponse.data.code === 0) {
          this.curriculumnTitle = curriculumnResponse.data.result.curriculumnTitle || 'Default Title';
        } else {
          console.error("Failed to fetch curriculumn title:", curriculumnResponse.data);
        }

        if (detailedResponse.data.code === 0) {
          this.curriculumnList = detailedResponse.data.result?.content || [];
        } else {
          console.error("Failed to fetch detailed data:", detailedResponse.data);
        }
      } catch (error) {
        console.error("Error fetching curriculum data:", error);
      }
    },
  },
  mounted() {
    this.fetchCurriculumData();
  },

}
</script>

<style lang="scss" scoped>
.container {
  .btn-group {
    display: flex;
    flex-direction: row;
    padding: 5px 5px;
    width: fit-content;
    border-radius: 10px;
    background: linear-gradient(to right, #1A2C6F, #304CB2);

    button {
      background: none;
      padding: 10px 40px;
      border-radius: 6px;
      color: #fff;
      font-weight: semibold;
    }

    .active-tab {
      background: #fff;
      color: #1A2C6F;
    }
  }

  .record {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
}

table {

  tr,
  td {
    font-weight: normal !important;
  }
}

#curriculumn {
  th {
    text-align: center;
  }

  tr,
  td {

    tr:nth-child(2),
    tr:nth-child(3),
    td:nth-child(2),
    td:nth-child(3) {
      width: 45%;
    }
  }

  td {
    color: #171717;
  }
}
</style>