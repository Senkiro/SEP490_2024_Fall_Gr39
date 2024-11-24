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
                <router-link class="sidebar-item" to="/teacher">
                    <span class="material-icons">
                        <VsxIcon iconName="Home3" :size="32" color="#1A2C6F" type="linear" />
                    </span>
                    <span class="text">Homepage</span>
                </router-link>

                <router-link class="sidebar-item" to="/teacher/schedule">
                    <span class="material-icons">
                        <VsxIcon iconName="Calendar" :size="32" color="#1A2C6F" type="linear" />
                    </span>
                    <span class="text">Schedule</span>
                </router-link>

                <router-link class="sidebar-item" to="/teacher/class-record">
                    <span class="material-icons">
                        <VsxIcon iconName="Profile2User" :size="32" color="#1A2C6F" type="linear" />
                    </span>
                    <span class="text">Class</span>
                </router-link>

                <router-link class="sidebar-item" to="/teacher/curriculumn">
                    <span class="material-icons">
                        <VsxIcon iconName="Book1" :size="32" color="#1A2C6F" type="linear" />
                    </span>
                    <span class="text">Curriculumn</span>
                </router-link>

                <router-link class="sidebar-item" to="/teacher/event">
                    <span class="material-icons">
                        <VsxIcon iconName="Activity" :size="32" color="#1A2C6F" type="linear" />
                    </span>
                    <span class="text">Event</span>
                </router-link>

                <router-link class="sidebar-item" to="/teacher/mark">
                    <span class="material-icons">
                        <VsxIcon iconName="Award" :size="32" color="#1A2C6F" type="linear" />
                    </span>
                    <span class="text">Mark</span>
                </router-link>

                <router-link class="sidebar-item" to="/teacher/attendance-management">
                    <span class="material-icons">
                        <VsxIcon iconName="Document" :size="32" color="#1A2C6F" type="linear" />
                    </span>
                    <span class="text">Attendance</span>
                </router-link>

                <router-link class="sidebar-item" to="/teacher/news">
                    <span class="material-icons">
                        <VsxIcon iconName="Information" :size="32" color="#1A2C6F" type="linear" />
                    </span>
                    <span class="text">News</span>
                </router-link>

                <router-link class="sidebar-item" to="/teacher/guide">
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
    overflow: hidden;
    min-height: 100vh;

    background-color: var(--side-background);
    color: var(--light);

    transition: 0.2s ease-out;

    .sidebar-container {
        position: fixed;
        display: flex;
        flex-direction: column;
        width: 68px;
        padding: 1rem;
        transition: 0.2s ease-out;



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
        width: var(--sidebar-width);

        .sidebar-container {
            width: 214px;


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