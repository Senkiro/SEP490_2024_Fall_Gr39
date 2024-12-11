<template>
  <aside :class="`${is_expanded && 'is-expanded'}`">
    <div class="sidebar-container">

      <div class="menu-toggle-wrap">
        <button class="menu-toggle" @click="ToggleMenu">
          <span class="material-icons">keyboard_double_arrow_right</span>
        </button>
      </div>

      <div class="sidebar-menu">
        <router-link class="sidebar-item" to="/staff" :class="{ 'active': active === 'homepage' }"
          @click="chooseItem('homepage')">
          <span class="material-icons">
            <VsxIcon iconName="Home3" :size="32" color="#1A2C6F" type="linear" />
          </span>
          <span class="text">Homepage</span>
        </router-link>

        <!-- -----Record----- -->

        <div class="dropdown">
          <button class="dropdown-header" :class="{ 'active1': dropdowns.record }"
            @click="toggleDropdown('record', 'learningMaterial', 'schedule')">
            <span class="material-icons">
              <VsxIcon iconName="Note" :size="32" color="#1A2C6F" type="linear" />
            </span>
            <span class="text">Record</span>
          </button>

          <div v-if="dropdowns.record" class="dropdown-container">
            <router-link class="sidebar-item dropdown-item" to="/staff/batch-record"
              :class="{ 'active': active === 'batch-record' }" @click="chooseItem1('batch-record')">
              <span class="material-icons">
                <VsxIcon iconName="LanguageCircle" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Batch record</span>
            </router-link>

            <router-link class="sidebar-item dropdown-item" to="/staff/student-record" :class="{ 'active': active === 'student-record' }" @click="chooseItem1('student-record')">
              <span class="material-icons">
                <VsxIcon iconName="Profile2User" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Student record</span>
            </router-link>

            <router-link class="sidebar-item dropdown-item" to="/staff/class-record" :class="{ 'active': active === 'class-record' }" @click="chooseItem1('class-record')">
              <span class="material-icons">
                <VsxIcon iconName="People" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Class record</span>
            </router-link>

            <router-link class="sidebar-item dropdown-item" to="/staff/teacher-record" :class="{ 'active': active === 'teacher-record' }" @click="chooseItem1('teacher-record')">
              <span class="material-icons">
                <VsxIcon iconName="Teacher" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Teacher record</span>
            </router-link>
          </div>
        </div>

        <!-- -----Schedule Management----- -->

        <div class="dropdown" :class="{ 'active1': dropdowns.schedule }">
          <button class="dropdown-header" @click="toggleDropdown('schedule', 'learningMaterial', 'record')">
            <span class="material-icons">
              <VsxIcon iconName="Calendar" :size="32" color="#1A2C6F" type="linear" />
            </span>
            <span class="text">Schedule</span>
          </button>

          <div v-if="dropdowns.schedule" class="dropdown-container">
            <router-link class="sidebar-item dropdown-item" to="/staff/schedule" :class="{ 'active': active === 'schedule' }" @click="chooseItem1('schedule')">
              <span class="material-icons">
                <VsxIcon iconName="Calendar" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Schedule</span>
            </router-link>

            <router-link class="sidebar-item dropdown-item" to="/staff/time-slot" :class="{ 'active': active === 'time-slot' }" @click="chooseItem1('time-slot')">
              <span class="material-icons">
                <VsxIcon iconName="Clock" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Time slot</span>
            </router-link>

            <router-link class="sidebar-item dropdown-item" to="/staff/event" :class="{ 'active': active === 'event' }" @click="chooseItem1('event')">
              <span class="material-icons">
                <VsxIcon iconName="Activity" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Event</span>
            </router-link>

            <router-link class="sidebar-item dropdown-item" to="/staff/room" :class="{ 'active': active === 'room' }" @click="chooseItem1('room')">
              <span class="material-icons">
                <VsxIcon iconName="House2" :size="32" color="#1A2C6F" type="linear" />
              </span>
              <span class="text">Room</span>
            </router-link>
          </div>
        </div>

        <router-link class="sidebar-item" to="/staff/curriculumn" :class="{ 'active': active === 'curriculum' }"
          @click="chooseItem('curriculum')">
          <span class="material-icons">
            <VsxIcon iconName="Book1" :size="32" color="#1A2C6F" type="linear" />
          </span>
          <span class="text">Curriculumn</span>
        </router-link>

        <router-link class="sidebar-item" to="/staff/holiday" :class="{ 'active': active === 'holiday' }"
          @click="chooseItem('holiday')">
          <span class="material-icons">
            <VsxIcon iconName="Cardano" :size="32" color="#1A2C6F" type="linear" />
          </span>
          <span class="text">Holiday</span>
        </router-link>

        <router-link class="sidebar-item" to="/staff/mark" :class="{ 'active': active === 'mark' }"
          @click="chooseItem('mark')">
          <span class="material-icons">
            <VsxIcon iconName="Award" :size="32" color="#1A2C6F" type="linear" />
          </span>
          <span class="text">Mark</span>
        </router-link>

        <router-link class="sidebar-item" to="/staff/attendance" :class="{ 'active': active === 'attendance' }"
          @click="chooseItem('attendance')">
          <span class="material-icons">
            <VsxIcon iconName="Document" :size="32" color="#1A2C6F" type="linear" />
          </span>
          <span class="text">Attendance</span>
        </router-link>

        <router-link class="sidebar-item" to="/staff/news" :class="{ 'active': active === 'news' }"
          @click="chooseItem('news')">
          <span class="material-icons">
            <VsxIcon iconName="Information" :size="32" color="#1A2C6F" type="linear" />
          </span>
          <span class="text">News</span>
        </router-link>
      </div>
    </div>
  </aside>
</template>

<script>
import { ref } from 'vue';

const is_expanded = ref(false)
const ToggleMenu = () => (is_expanded.value = !is_expanded.value)

export default {
  name: "AppSidebar",
  components: {
  },
  is_expanded,
  ToggleMenu,
  data() {
    return {
      active: "homepage",
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
    },
    chooseItem(item) {
      this.dropdowns.record = false;
      this.dropdowns.schedule = false;
      this.dropdowns.learningMaterial = false;
      this.active = item;
    },
    chooseItem1(item) {
      this.active = item;
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

    a {
      &:active {
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

      .active1 {
        background-color: #d0dcf4;

        .dropdown-item {
          background-color: #c9d6f3;
        }
      }

      .active {
        background-color: #c9d6f3 !important;
        border-left: 5px solid #8aa6e5;

        .dropdown-container {
          background-color: #c9d6f3;
          transition: 0.2s ease-out;
        }
      }

      .dropdown {
        flex-direction: column;

        &:hover {
          background-color: #c9d6f3;
        }

        .dropdown-header {
          display: flex;
          flex-direction: row;
          align-items: center;
          padding: 15px 1rem;
          transition: 0.2s ease-out;
          width: 100%;
          gap: 0px;
          border-radius: 0;
        }

        .dropdown-container {
            .dropdown-item {
              background-color: #d0dcf4;
            }
        }
      }

      .sidebar-item {
        display: flex;
        align-items: center;
        text-decoration: none;
        padding: 15px 1rem;

        .dropdown-item {
          padding: 1rem 0rem 0rem 0rem;
          z-index: 99;
        }

        &:hover {
          background-color: #c9d6f3;
        }
      }
    }
  }

  &.is-expanded {
    width: 15%;

    .sidebar-container {
      width: 198px;


      .menu-toggle-wrap {
        // top: -3rem;

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