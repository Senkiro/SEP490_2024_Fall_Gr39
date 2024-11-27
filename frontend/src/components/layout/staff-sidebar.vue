<template>
  <aside :class="`${is_expanded && 'is-expanded'}`">
    <div class="sidebar-container">
      <div class="logo">
        <img src="">
      </div>

      <div class="menu-toggle-wrap">
        <button class="menu-toggle" @click="ToggleMenu">
          <span class="material-icons">keyboard_double_arrow_right</span>
        </button>
      </div>

      <div class="sidebar-menu">
        <router-link class="sidebar-item" to="/staff">
          <span class="material-icons">
            <VsxIcon iconName="Home3" :size="32" color="#1A2C6F" type="linear" />
          </span>
          <span class="text">Homepage</span>
        </router-link>

        <!-- -----Record----- -->

        <div class="dropdown" :class="{ active: dropdowns.record }">
          <button class="dropdown-header" @click="toggleDropdown('record', 'learningMaterial', 'schedule')">
            <span class="material-icons">
              <VsxIcon iconName="Note" :size="32" color="#1A2C6F" type="linear" />
            </span>
            <span class="text">Record</span>
          </button>

          <div v-if="dropdowns.record" class="dropdown-container">
            <router-link class="sidebar-item dropdown-item" to="/staff/batch-record">
              <span class="material-icons">
                <VsxIcon iconName="LanguageCircle" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Batch record</span>
            </router-link>

            <router-link class="sidebar-item dropdown-item" to="/staff/student-record">
              <span class="material-icons">
                <VsxIcon iconName="Profile2User" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Student record</span>
            </router-link>

            <router-link class="sidebar-item dropdown-item" to="/staff/class-record">
              <span class="material-icons">
                <VsxIcon iconName="People" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Class record</span>
            </router-link>

            <router-link class="sidebar-item dropdown-item" to="/staff/teacher-record">
              <span class="material-icons">
                <VsxIcon iconName="Teacher" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Teacher record</span>
            </router-link>
          </div>
        </div>

        <!-- -----Schedule Management----- -->

        <div class="dropdown" :class="{ active: dropdowns.schedule }">
          <button class="dropdown-header" @click="toggleDropdown('schedule', 'learningMaterial', 'record')">
            <span class="material-icons">
              <VsxIcon iconName="Calendar" :size="32" color="#1A2C6F" type="linear" />
            </span>
            <span class="text">Schedule</span>
          </button>

          <div v-if="dropdowns.schedule" class="dropdown-container">
            <router-link class="sidebar-item dropdown-item" to="/staff/schedule">
              <span class="material-icons">
                <VsxIcon iconName="Calendar" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Schedule</span>
            </router-link>

            <router-link class="sidebar-item dropdown-item" to="/staff/time-slot">
              <span class="material-icons">
                <VsxIcon iconName="Clock" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Time slot</span>
            </router-link>

            <router-link class="sidebar-item dropdown-item" to="/staff/event">
              <span class="material-icons">
                <VsxIcon iconName="Activity" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Event</span>
            </router-link>

            <router-link class="sidebar-item dropdown-item" to="/staff/room">
              <span class="material-icons">
                <VsxIcon iconName="House2" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Room</span>
            </router-link>
          </div>
        </div>

        <router-link class="sidebar-item" to="/staff/curriculumn">
          <span class="material-icons">
            <VsxIcon iconName="Book1" :size="32" color="#1A2C6F" type="linear" />
          </span>
          <span class="text">Curriculumn</span>
        </router-link>

        <router-link class="sidebar-item" to="/staff/holiday">
          <span class="material-icons">
            <VsxIcon iconName="Cardano" :size="32" color="#1A2C6F" type="linear" />
          </span>
          <span class="text">Holiday</span>
        </router-link>

        <router-link class="sidebar-item" to="/staff/exam-type">
          <span class="material-icons">
            <VsxIcon iconName="Category2" :size="32" color="#1A2C6F" type="linear" />
          </span>
          <span class="text">Exam type</span>
        </router-link>

        <router-link class="sidebar-item" to="/staff/mark">
          <span class="material-icons">
            <VsxIcon iconName="Award" :size="32" color="#1A2C6F" type="linear" />
          </span>
          <span class="text">Mark</span>
        </router-link>

        <router-link class="sidebar-item" to="/staff/attendance">
          <span class="material-icons">
            <VsxIcon iconName="Document" :size="32" color="#1A2C6F" type="linear" />
          </span>
          <span class="text">Attendance</span>
        </router-link>

        <router-link class="sidebar-item" to="/staff/news">
          <span class="material-icons">
            <VsxIcon iconName="Information" :size="32" color="#1A2C6F" type="linear" />
          </span>
          <span class="text">News</span>
        </router-link>

        <router-link class="sidebar-item" to="/staff/guide">
          <span class="material-icons">
            <VsxIcon iconName="BookSquare" :size="32" color="#1A2C6F" type="linear" />
          </span>
          <span class="text">Guide</span>
        </router-link>
      </div>
    </div>
  </aside>
</template>

<script>
import { ref } from 'vue';
import { VsxIcon } from "vue-iconsax";

const is_expanded = ref(false)
const ToggleMenu = () => (is_expanded.value = !is_expanded.value)

export default {
  name: "AppSidebar",
  components: {
    VsxIcon
  },
  is_expanded,
  ToggleMenu,
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
    toggleDropdown(menu, off1, off2) {
      this.dropdowns[menu] = !this.dropdowns[menu];
      this.dropdowns[off1] = false;
      this.dropdowns[off2] = false;
    }
  },
  setup() {
    return { is_expanded, ToggleMenu }
  }
};
</script>

<style lang="scss" scoped>
aside {
  display: flex;
  width: 72px;
  min-height: 100vh;
  background-color: var(--side-background);
  color: var(--light);
  transition: 0.2s ease-out;

  .sidebar-container {
    overflow-y: auto;
    position: fixed;
    display: flex;
    flex-direction: column;
    width: 68px;
    padding: 1rem;
    transition: 0.2s ease-out;
    height: 100%;

    a{
      &:active{
        color: #c9d6f3;
      }
    }

    button {
      border: none;
      background: none;
    }

    .logo {
      margin-bottom: 1rem;

      img {
        width: 2rem;
      }
    }

    .menu-toggle-wrap {
      display: flex;
      justify-content: flex-end;
      margin-bottom: 1rem;
      position: relative;
      top: 0;
      transition: 0.2s ease-out;

      .menu-toggle {
        transition: 0.2s ease-out;
        padding: 0px;

        .material-icons {
          font-size: 2rem;
          color: var(--primary);
        }

        &:hover {
          .material-icons {
            color: var(--primary-alt);
            transform: translateX(0.5rem);
          }
        }
      }
    }

    .text {
      display: none;
      color: var(--primary);
      padding-left: 0.5rem;
      font-size: 1rem;
    }

    .sidebar-menu {
      margin: 0 -1rem;
      display: flex;
      flex-direction: column;

      .active {
        background-color: #6281cb;

        .dropdown-header {
          .text {
            color: white;
            transition: 0.2s ease-out;
          }
        }

        .dropdown-container {
          background-color: #c9d6f3;
          transition: 0.2s ease-out;
        }
      }

      .dropdown {
        flex-direction: column;

        &:hover {
          background-color: #c8d6f8;
        }

        .dropdown-header {
          display: flex;
          flex-direction: row;
          align-items: center;
          padding: 0.5rem 1rem;
          transition: 0.2s ease-out;
          width: 100%;
          gap: 0px;
        }

        .dropdown-container {

          &:hover {
            .dropdown-item {
              background-color: #c8d6f8;
            }
          }
        }
      }

      .sidebar-item {
        display: flex;
        align-items: center;
        text-decoration: none;
        padding: 0.5rem 1rem;

        .dropdown-item {
          padding: 1rem 0rem 0rem 0rem;
          z-index: 99;
        }

        &:hover {
          background-color: #c8d6f8;

          .text {
            color: white;
          }
        }
      }
    }
  }

  &.is-expanded {
    width: 15%;

    .sidebar-container {
      width: 198px;


      .menu-toggle-wrap {
        top: -3rem;

        .menu-toggle {
          transform: rotate(-180deg);
          transition: 0.3s ease-out;
        }
      }

      .sidebar-menu {
        .dropdown {
          .dropdown-container {
            .dropdown-item {
              padding-left: 2rem;
            }
          }
        }
      }

      .text {
        width: 250px;
        display: flex;
      }
    }
  }

  @media(max-width: 768px) {
    position: fixed;
    z-index: 99;
  }
}
</style>