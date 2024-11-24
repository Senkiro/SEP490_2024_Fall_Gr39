<template>
  <div class="app">
    <component :is="layout"></component>
  </div>
</template>



<script>
// layouts
import staffLayout from "./layouts/staff-layout.vue";
import authLayout from "./layouts/auth-layout.vue";
import studentLayout from "./layouts/student-layout.vue";


export default {
  props: ['iconName'],
  metaInfo: {
    title: "Nihon Study GuideEntity",
    titleTemplate: "%s | Nihon - KKK",
  },
  components: {
    staff: staffLayout,
    auth: authLayout,
    student: studentLayout
  },
  computed: {
    layout() {
      switch (this.$route.meta.layout) {
        case 'auth':
          return authLayout;
        case 'staff':
          return staffLayout;
        case 'student':
          return studentLayout;
        default:
          return authLayout;
      }
    },
  },
  data() {
    return {};
  },
  mounted() {
  },
  methods: {},
};
</script>

<style lang="scss">
:root {
  --primary: #1A2C6F;
  --primary-alt: #253f9f;
  --light: #f1f5f9;
  --side-background: #DFE7FB;
  --sidebar-width: 250px;
  --border: #495057;
  --linear: linear-gradient(90deg, #3E5DD4, #223374);
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: sans-serif;
}

body {
  background: #fff;
}

button {
  cursor: pointer;
}

.app {
  display: flex;

  main {
    flex: 1 1 0;
    padding: 2rem;

    @media(max-width: 768px) {
      padding-left: 6rem;
    }
  }
}

h1 {
  width: fit-content;
  font-size: 36px;
  background: -webkit-linear-gradient(180deg, #304CB2, #1A2C6F);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-weight: bold;
  margin: 20px 0px;
}

h2 {
  width: fit-content;
  font-size: 26px;
  background: -webkit-linear-gradient(180deg, #304CB2, #1A2C6F);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-weight: bold;
  margin: 20px 0px;
}

h3 {
  font-size: 16px;
  font-weight: normal;
}

button {
  background-image: linear-gradient(90deg, #3E5DD4, #223374);
  padding: 10px 20px;
  color: #fff;
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: normal;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  gap: 10px;
}


.btn-cancel {
  background: #ccc;
  color: #fff;
}

.container {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;

  .actions {
    display: flex;
    flex-direction: row;
    gap: 20px;
    justify-content: flex-end;
  }

  .filters {
    display: flex;
    flex-direction: row;
    gap: 20px;
    width: fit-content;

    select {
      font-size: 14px;
      font-weight: normal;
      padding: 10px 10px;
      border-radius: 10px;
      transition: 0.3s, ease-out;
      border: 1px solid #000000;
    }
  }

  .search-container {
    padding: 10px 20px;
    display: flex;
    border: 1px solid #ADB5BD;
    border-radius: 10px;
    justify-content: space-between;

    .search-field {
      font-size: 14px;
      font-weight: normal;
      color: #171717;
      outline: none;
      border: none;
      width: 250px;
    }
  }

  .text-editor-container {
    margin: 5px 0;
  }

  .table-container {
    display: flex;
    flex-direction: column;
    gap: 40px;

    table {
      border-collapse: collapse;

      .table-button {
        background: #3E5DD4;
        padding: 5px 10px;
        color: #fff;
        display: flex;
        align-items: center;
        font-size: 14px;
        font-weight: bold;
        border: none;
        border-radius: 10px;
        cursor: pointer;
      }

      .center {
        text-align: center;
      }

      th {
        text-align: left;
        padding: 10px 20px;
        background: #dae4f3;
        color: #171717;
        border: 2px solid #fff;
        border-bottom: 2px solid #dae4f3;
      }

      tr {

        .chapter {
          font-weight: bold !important;
        }

        .chapter,
        .tab-index {
          cursor: pointer;
        }


        text-align: left;
        transition: 3s;
        color: #979B9F;
        cursor: context-menu;

        td:first-child,
        th:first-child {
          max-width: 100px;
        }

        td:nth-child(1),
        td:nth-child(2) {
          color: #171717;
          font-weight: bold;
        }

        .tab-index {
          padding-left: 80px;
          width: fit-content;
        }

        td {
          padding: 10px 20px;
          white-space: nowrap;
          text-overflow: ellipsis;
          overflow: hidden;
          max-width: 350px;

          .status-button {
            display: flex;
            flex-direction: row;
            gap: 10px;
            align-items: center;
          }

          .activities-container {
            display: flex;
            flex-direction: column;
            gap: 20px;

            .activity {
              display: flex;
              flex-direction: row;
              gap: 20px;
              color: #1A2C6F;
              cursor: context-menu;
              align-items: center;

              b {
                cursor: pointer;
              }

              .thumbnail {
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                background-color: #3E5DD4;
                color: #fff;
                padding: 20px;
                gap: 5px;
                aspect-ratio: 1 / 1;
                height: 100%;
                border-radius: 20px;

                #lesson {
                  font-size: 16px;
                }

                #number {
                  font-size: 30px;
                  font-weight: bold;
                }
              }

              .information {
                display: flex;
                flex-direction: column;
                justify-content: center;
                gap: 5px;
              }
            }
          }

          .schedule-date {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 10px;

            h1 {
              margin: 0;
            }
          }

          .icon-group {
            display: flex;
            gap: 10px;
            justify-content: center;
          }
        }
      }

      thead:first-child tr:first-child th:first-child {
        border-radius: 20px 0 0 0;
      }

      thead:first-child tr:last-child th:last-child {
        border-radius: 0 20px 0 0;
      }

      .status-progress,
      .status-pending {
        color: #304CB2;
      }

      .status-graduated,
      .status-finished {
        color: #6ECBB8;
      }

      .attended {
        color: #00B968;
      }
    }

    .pagination {
      display: flex;
      flex-direction: row;
      justify-content: center;
      gap: 10px;

      button {
        display: flex;
        width: 40px;
        height: 40px;
        padding: 0px;
        justify-content: center;
        align-content: center;
        border: 1px solid #000000;
        background: none;
        color: #171717;
      }
    }
  }

  .input-group {
    margin-bottom: 15px;
    display: flex;
    flex-direction: column;
    gap: 5px;

    label {
      display: flex;
      width: fit-content;
      gap: 5px;
    }

    input {
      width: 500px;
      padding: 10px;
      font-size: 14px;
      border: 1px solid #ccc;
      border-radius: 10px;

    }
  }

  .popup-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(217, 217, 217, 0.4);
    display: flex;
    justify-content: center;
    align-items: center;

    .popup {
      background: #fff;
      padding: 20px 40px;
      border-radius: 20px;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);

      .exit-icon {
        margin: -10px -30px 0 0;
        display: flex;
        flex-direction: row;
        justify-content: flex-end;
      }

      .popup-title {
        display: flex;
        justify-content: center;
      }

      .form-group {
        margin-bottom: 15px;
        display: flex;
        flex-direction: row;
        gap: 20px;
        justify-content: flex-end;
        align-items: center;

        label {
          display: flex;
          width: fit-content;
          gap: 5px;
        }

        input {
          width: 250px;
          padding: 10px;
          font-size: 14px;
          border: 1px solid #ccc;
          border-radius: 10px;

        }

        select {
          width: 250px;
          font-size: 14px;
          font-weight: normal;
          padding: 10px 10px;
          border-radius: 10px;
          transition: 0.3s, ease-out;
          border: 1px solid #ccc;
        }

        .gender-group {
          width: 250px;
          display: flex;
          flex-direction: row;
          gap: 50px;

          .radio {
            display: flex;
            flex-direction: row;
            justify-content: flex-start;
            gap: 10px;
            align-items: center;
          }

          input {
            width: auto;
          }
        }
      }

      .error {
        color: red;
        font-size: 14px;
        margin-top: 10px;
      }
    }
  }

  .notification {
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 15px 20px;
    border-radius: 8px;
    font-size: 16px;
    color: #fff;
    z-index: 1000;
    transition: all 0.5s ease;
  }

  .tab-buttons-container {
    width: 100%;
    border: 1px solid #ddd;
    border-radius: 1rem;
    padding: 15px;

    .tab-buttons {
      display: flex;
      gap: 10px;
      border-bottom: 1px solid #E0E0E0;

      .tab-button {
        padding: 10px;
        background: none;
        border-radius: 0;
        border: none;
        cursor: pointer;
        font-size: 14px;
        color: #726C6C;
      }

      .active {
        border-bottom: 3px solid #3E5DD4;

        h3 {
          font-weight: bold;
          color: #212529;
        }
      }
    }
  }

  .tab-container {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }

  .success {
    background-color: #4caf50;
  }

  .error {
    background-color: #f44336;
  }

  .required {
    color: red;
    font-weight: bold;
  }

  form {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
}
</style>