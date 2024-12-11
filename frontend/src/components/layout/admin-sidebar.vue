<template>
    <aside :class="`${is_expanded && 'is-expanded'}`">
        <div class="sidebar-container">
            <div class="menu-toggle-wrap">
                <button class="menu-toggle" @click="ToggleMenu">
                    <span class="material-icons">keyboard_double_arrow_right</span>
                </button>
            </div>

            <div class="sidebar-menu">
                <router-link class="sidebar-item" to="/admin/account" :class="{ 'active': active === 'account' }" @click="chooseItem('account')">
                    <span class="material-icons">
                        <VsxIcon iconName="Profile2User" :size="32" color="#1A2C6F" type="linear" />
                    </span>
                    <span class="text">Account</span>
                </router-link>

                <router-link class="sidebar-item" to="/admin/mark" :class="{ 'active': active === 'mark' }" @click="chooseItem('mark')">
                    <span class="material-icons">
                        <VsxIcon iconName="Award" :size="32" color="#1A2C6F" type="linear" />
                    </span>
                    <span class="text">Mark</span>
                </router-link>

                <router-link class="sidebar-item" to="/admin/attendance" :class="{ 'active': active === 'attendance' }" @click="chooseItem('attendance')">
                    <span class="material-icons">
                        <VsxIcon iconName="Document" :size="32" color="#1A2C6F" type="linear" />
                    </span>
                    <span class="text">Attendance</span>
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
        return{
            active: "account"
        }
    },
    setup() {
        return { is_expanded, ToggleMenu }
    },
    methods:{
        chooseItem(item){
            this.active=item;
        }
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