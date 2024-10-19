<template>
  <nav class="sidebar">
    <div class="sidebar-logo">
      <img src="/path/to/logo.png" alt="Logo" />
    </div>
    <ul class="sidebar-menu">
      <li class="sidebar-item" :class="{ active: $route.path === '/homepage' }">
        <router-link to="/homepage">
          <VsxIcon iconName="Home" :size="24" color="#5584FF" type="linear" class="icon" />
          Homepage
        </router-link>
      </li>

      <li class="sidebar-item" @click="toggleDropdown('record')">
        <div class="dropdown-header">
          <VsxIcon iconName="Document" :size="24" color="#5584FF" type="linear" class="icon" />
          Record
          <VsxIcon :iconName="dropdowns.record ? 'ArrowDown2' : 'ArrowDown2'" :size="16" color="#5584FF" class="dropdown-icon" />
        </div>
        <ul v-if="dropdowns.record" class="dropdown-menu">
          <li class="dropdown-item">
            <router-link to="/staff/batch-record">
              <VsxIcon iconName="Brush3" :size="20" color="#5584FF" type="linear" class="icon" />
              Batch Record
            </router-link>
          </li>
          <li class="dropdown-item">
            <router-link to="/class-record">
              <VsxIcon iconName="User" :size="20" color="#5584FF" type="linear" class="icon" />
              Class Record
            </router-link>
          </li>
          <li class="dropdown-item">
            <router-link to="/student-record">
              <VsxIcon iconName="People" :size="20" color="#5584FF" type="linear" class="icon" />
              Student Record
            </router-link>
          </li>
          <li class="dropdown-item">
            <router-link to="/teacher-record">
              <VsxIcon iconName="Teacher" :size="20" color="#5584FF" type="linear" class="icon" />
              Teacher Record
            </router-link>
          </li>
        </ul>
      </li>

      <li class="sidebar-item" @click="toggleDropdown('schedule')">
        <div class="dropdown-header">
          <VsxIcon iconName="Calendar" :size="24" color="#5584FF" type="linear" class="icon" />
          Schedule Management
          <VsxIcon :iconName="dropdowns.schedule ? 'ArrowDown2' : 'ArrowDown2'" :size="16" color="#5584FF" class="dropdown-icon" />
        </div>
        <ul v-if="dropdowns.schedule" class="dropdown-menu">
          <li class="dropdown-item">
            <router-link to="/schedule/class">
              <VsxIcon iconName="ClipboardText" :size="20" color="#5584FF" type="linear" class="icon" />
              Schedule
            </router-link>
          </li>
          <li class="dropdown-item">
            <router-link to="/schedule/time-slot">
              <VsxIcon iconName="Clock" :size="20" color="#5584FF" type="linear" class="icon" />
              Time Slot
            </router-link>
          </li>
          <li class="dropdown-item">
            <router-link to="/schedule/event">
              <VsxIcon iconName="CalendarEvent" :size="20" color="#5584FF" type="linear" class="icon" />
              Event
            </router-link>
          </li>
          <li class="dropdown-item">
            <router-link to="/schedule/room">
              <VsxIcon iconName="Home" :size="20" color="#5584FF" type="linear" class="icon" />
              Room
            </router-link>
          </li>
        </ul>
      </li>

      <li class="sidebar-item" @click="toggleDropdown('learningMaterial')">
        <div class="dropdown-header">
          <VsxIcon iconName="Book" :size="24" color="#5584FF" type="linear" class="icon" />
          Learning Material
          <VsxIcon :iconName="dropdowns.learningMaterial ? 'ArrowDown2' : 'ArrowDown2'" :size="16" color="#5584FF" class="dropdown-icon" />
        </div>
        <ul v-if="dropdowns.learningMaterial" class="dropdown-menu">
          <li class="dropdown-item">
            <router-link to="/learning-material/lesson">
              <VsxIcon iconName="BookOpen" :size="20" color="#5584FF" type="linear" class="icon" />
              Lesson
            </router-link>
          </li>
          <li class="dropdown-item">
            <router-link to="/learning-material/exam">
              <VsxIcon iconName="FileCheck" :size="20" color="#5584FF" type="linear" class="icon" />
              Exam
            </router-link>
          </li>
        </ul>
      </li>
    </ul>
  </nav>
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
  display: flex;
  flex-direction: column;
  width: 250px;
  height: 100vh;
  background: linear-gradient(to bottom, #ffffff, #e0ecff);
  padding: 20px;
  position: fixed;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
}

.sidebar-logo {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
}

.sidebar-menu {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar-item {
  margin: 15px 0;
  display: flex;
  flex-direction: column;
}

.dropdown-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
  padding: 10px;
  border-radius: 10px;
  transition: background-color 0.3s, transform 0.2s;
}

.sidebar-item .icon {
  width: 24px;
  height: 24px;
  margin-right: 10px;
}

.sidebar-item.active .dropdown-header {
  background: linear-gradient(to right, #4a90e2, #007bff);
  color: #fff;
  transform: translateX(5px);
}

.sidebar-item:hover .dropdown-header {
  background-color: #c0d0ff;
  transform: translateX(5px);
}

.dropdown-menu {
  list-style: none;
  padding-left: 20px;
  margin-top: 10px;
}

.dropdown-item {
  margin: 10px 0;
  display: flex;
  align-items: center;
}

.dropdown-item a {
  text-decoration: none;
  color: #333;
  font-size: 14px;
  font-weight: bold;
  display: flex;
  align-items: center;
}

.sidebar-item a {
  text-decoration: none;
  color: inherit;
  font-size: 16px;
  font-weight: bold;
  display: flex;
  align-items: center;
  flex-grow: 1;
}

.dropdown-icon {
  margin-left: auto;
}
</style>
