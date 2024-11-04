<template>
  <div class="sidebar">
    <div class="top">
      <div class="logo">
        <img src="https://png.pngtree.com/illustrations/20190322/ourmid/pngtree-japan-city-building-landmarks-png-image_37377.jpg" width="100px" height="100px">
        <span>Nihon Study Guide</span>
      </div>
      <i class="bx bx-menu" id="menu-btn"></i>
    </div>
    <ul class="sidebar-menu">
      <li class="sidebar-item" :class="{ active: $route.path === '/homepage' }">
        <router-link to="/homepage">
          <VsxIcon iconName="Home3" :size="30" color="#1A2C6F" type="linear" class="icon" />
          <p>Homepage</p>
        </router-link>
      </li>

      <li class="sidebar-item" @click="toggleDropdown('record')">
        <div class="dropdown-header">
          <VsxIcon iconName="Note" :size="30" color="#1A2C6F" type="linear" class="icon" />
          <p>Record</p>
          <VsxIcon :iconName="dropdowns.record ? 'ArrowDown2' : 'ArrowDown2'" :size="30" color="#1A2C6F"
            class="dropdown-icon" />
        </div>
        <ul v-if="dropdowns.record" class="dropdown-menu">
          <li class="dropdown-item">
            <router-link to="/staff/batch-record">
              <VsxIcon iconName="LanguageCircle" :size="30" color="#1A2C6F" type="linear" class="icon" />
              <p>Batch Record</p>
            </router-link>
          </li>
          <li class="dropdown-item">
            <router-link to="/class-record">
              <VsxIcon iconName="Profile2User" :size="30" color="#1A2C6F" type="linear" class="icon" />
              <p>Class Record</p>
            </router-link>
          </li>
          <li class="dropdown-item">
            <router-link to="/student-record">
              <VsxIcon iconName="People" :size="30" color="#1A2C6F" type="linear" class="icon" />
              <p>Student Record</p>
            </router-link>
          </li>
          <li class="dropdown-item">
            <router-link to="/teacher-record">
              <VsxIcon iconName="Teacher" :size="30" color="#1A2C6F" type="linear" class="icon" />
              <p>Teacher Record</p>
            </router-link>
          </li>
        </ul>
      </li>

      <li class="sidebar-item" @click="toggleDropdown('schedule')">
        <div class="dropdown-header">
          <VsxIcon iconName="Calendar" :size="30" color="#1A2C6F" type="linear" class="icon" />
          <p>Schedule Management</p>
          <VsxIcon :iconName="dropdowns.schedule ? 'ArrowDown2' : 'ArrowDown2'" :size="30" color="#1A2C6F"
            class="dropdown-icon" />
        </div>
        <ul v-if="dropdowns.schedule" class="dropdown-menu">
          <li class="dropdown-item">
            <router-link to="/schedule/class">
              <VsxIcon iconName="Calendar" :size="30" color="#1A2C6F" type="linear" class="icon" />
              <p>Schedule</p>
            </router-link>
          </li>
          <li class="dropdown-item">
            <router-link to="/schedule/time-slot">
              <VsxIcon iconName="Clock" :size="30" color="#1A2C6F" type="linear" class="icon" />
              <p>Time Slot</p>
            </router-link>
          </li>
          <li class="dropdown-item">
            <router-link to="/schedule/event">
              <VsxIcon iconName="Activity" :size="30" color="#1A2C6F" type="linear" class="icon" />
              <p>Event</p>
            </router-link>
          </li>
          <li class="dropdown-item">
            <router-link to="/schedule/room">
              <VsxIcon iconName="House2" :size="30" color="#1A2C6F" type="linear" class="icon" />
              <p>Room</p>
            </router-link>
          </li>
        </ul>
      </li>

      <li class="sidebar-item" @click="toggleDropdown('learningMaterial')">
        <div class="dropdown-header">
          <VsxIcon iconName="Book1" :size="30" color="#1A2C6F" type="linear" class="icon" />
          <p>Learning Material</p>
          <VsxIcon :iconName="dropdowns.learningMaterial ? 'ArrowDown2' : 'ArrowDown2'" :size="30" color="#1A2C6F"
            class="dropdown-icon" />
        </div>
        <ul v-if="dropdowns.learningMaterial" class="dropdown-menu">
          <li class="dropdown-item">
            <router-link to="/learning-material/lesson">
              <VsxIcon iconName="Book" :size="30" color="#1A2C6F" type="linear" class="icon" />
              <p>Lesson</p>
            </router-link>
          </li>
          <li class="dropdown-item">
            <router-link to="/learning-material/exam">
              <VsxIcon iconName="Task" :size="30" color="#1A2C6F" type="linear" class="icon" />
              <p>Exam</p>
            </router-link>
          </li>
        </ul>
      </li>

      <li class="sidebar-item">
        <router-link to="/mark">
          <VsxIcon iconName="Award" :size="30" color="#1A2C6F" type="linear" class="icon" />
          <p>Mark</p>
        </router-link>
      </li>

      <li class="sidebar-item">
        <router-link to="/attendance">
          <VsxIcon iconName="Document" :size="30" color="#1A2C6F" type="linear" class="icon" />
          <p>Attendance</p>
        </router-link>
      </li>

      <li class="sidebar-item">
        <router-link to="/news">
          <VsxIcon iconName="Information" :size="30" color="#1A2C6F" type="linear" class="icon" />
          <p>News</p>
        </router-link>
      </li>

      <li class="sidebar-item">
        <router-link to="/guide">
          <VsxIcon iconName="BookSquare" :size="30" color="#1A2C6F" type="linear" class="icon" />
          <p>Guide</p>
        </router-link>
      </li>
    </ul>
  </div>
</template>


<script>
import { VsxIcon } from "vue-iconsax";

export default {
  name: "AppSidebar",
  components: {
    VsxIcon
  },
  data() {
    return {
      dropdowns: {
        record: false,
        schedule: false,
        learningMaterial: false
      }
    };
  },
  methods: {
    toggleDropdown(menu) {
      this.dropdowns[menu] = !this.dropdowns[menu];
    }
  }
};
</script>

<style scoped>
.sidebar {
  height: 100vh;
  background: #DFE7FB;
  width: auto;
  margin-right: 24px;
  overflow-y: hidden;
  overflow-x: hidden;
  scrollbar-gutter: stable;
  position: fixed;
}

.logo{
  padding: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.logo img{
  margin-bottom: 20px;
}

.sidebar:hover {
  overflow-y: auto;
}

img{
  border-radius: 50%;
}

.sidebar::-webkit-scrollbar {
  width: 16px;
}

.sidebar::-webkit-scrollbar-track {
  position: absolute;
  background: transparent;
  border-radius: 8px;
}

.sidebar::-webkit-scrollbar-thumb {
  border-radius: 8px;
  border: 3px solid transparent;
  background-clip: content-box;
  background-image: linear-gradient(180deg, #677dcf 0%, #304CB2 100%);
}

.sidebar-logo {
  height: 150px;
}

ul {
  list-style: none;
}

.sidebar-item {
  padding: 10px 0px 10px 10px;
  height: fit-content;
}

.sidebar-item:target {
  background-image: linear-gradient(90deg, #1A2C6F 0%, #3254D5 100%);
}

.sidebar-item:target p{
color: white;
font-weight: bold;
}

p {
  display: flex;
  flex-direction: column-reverse;
  justify-content: center;
  color: #1A2C6F;
  width: auto;
}

a {
  color: #1A2C6F;
  display: flex;

}

.dropdown-header {
  color: #1A2C6F;
  display: flex;
}

.dropdown-item {
  padding: 20px 0px 0px 20px;
}

.icon {
  margin-right: 10px;
}

.dropdown-icon {
  height: 30px;
  width: 30px;
  margin-left: auto;
}
.dropdown-menu{
  padding-bottom: 0px;
}
</style>