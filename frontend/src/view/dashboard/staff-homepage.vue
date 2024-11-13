<template>
  <div class="container">
    <div id="daily-status">
      <div class="daily-status-bar" id="student-attendance">
        <p class="status-number">298</p>
        <p class="status-description">students present today</p>
      </div>

      <div class="daily-status-bar">
        <p class="status-number">260</p>
        <p class="status-description">exams marked today</p>
      </div>

      <div class="daily-status-bar">
        <p class="status-number">38</p>
        <p class="status-description">exams unmarked today</p>
      </div>
    </div>


    <div class="row-2">
      <div id="shortcut-container">
        <div class="shortcut-row">
          <div class="shortcut">
            <div class="shortcut-upper">
              <div class="information">
                <h2>Student Record</h2>
                <p class="shortcut-description">300 records</p>
              </div>
              <div class="logo">
                <VsxIcon iconName="People" size="25" type="linear" color="#fff" />
              </div>

            </div>
            <div class="shortcut-lower">
              <button>View</button>
            </div>
          </div>

          <div class="shortcut">
            <div class="shortcut-upper">
              <div class="information">
                <h2>Teacher Record</h2>
                <p class="shortcut-description">7 records</p>
              </div>
              <div class="logo">
                <VsxIcon iconName="Teacher" size="25" type="linear" color="#fff" />
              </div>

            </div>
            <div class="shortcut-lower">
              <button>View</button>
            </div>
          </div>
        </div>

        <div class="shortcut-row">
          <div class="shortcut">
            <div class="shortcut-upper">
              <div class="information">
                <h2>Schedule</h2>
                <p class="shortcut-description">10 new updates</p>
              </div>
              <div class="logo">
                <VsxIcon iconName="Calendar" size="25" type="linear" color="#fff" />
              </div>

            </div>
            <div class="shortcut-lower">
              <button>View</button>
            </div>
          </div>

          <div class="shortcut">
            <div class="shortcut-upper">
              <div class="information">
                <h2>Mark</h2>
                <p class="shortcut-description">2 new updates</p>
              </div>
              <div class="logo">
                <VsxIcon iconName="Award" size="25" type="linear" color="#fff" />
              </div>

            </div>
            <div class="shortcut-lower">
              <button>View</button>
            </div>
          </div>
        </div>
      </div>

      <div id="news-container">
        <div class="news-upper">
          <h2>News</h2>
          <p id="see-all">See all</p>
        </div>

        <div class="news-lower">
          <div class="news-row">
            <p class="date">10/12/2024</p>
            <p class="title">Title 1</p>
          </div>

          <div class="news-row">
            <p class="date">10/12/2024</p>
            <div class="title">Longggg gggggg gggggggg gggg gggggg ggggg ggggg title</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { VsxIcon } from 'vue-iconsax';

export default {
  name: "StaffHomepage",
  components: {
    VsxIcon
  }
}
</script>

<style lang="scss" scoped>
.container {
  display: flex;
  flex-direction: column;
  gap: 20px;

  h2 {
    margin: 0;
  }

  #daily-status {
    display: flex;
    flex-direction: row;
    gap: 30px;

    .daily-status-bar {
      display: flex;
      flex-direction: column;
      padding: 20px 60px 20px 40px;
      border-radius: 10px;
      background-color: #BCC5E6;

      .status-number {
        font-size: 30px;
        font-weight: bold;
        color: #233E8B;
      }

      .status-description {
        color: #233E8B;
      }
    }

    #student-attendance {
      background-color: #233E8B;

      .status-number,
      .status-description {
        color: #fff;
      }
    }
  }

  .row-2 {
    display: flex;
    flex-direction: row;
    gap: 20px;

    #shortcut-container {
      width: 70%;
      display: flex;
      flex-direction: column;
      gap: 30px;

      .shortcut-row {
        display: flex;
        flex-direction: row;
        gap: 30px;

        .shortcut {
          width: 50%;
          display: flex;
          flex-direction: column;
          border: 2px solid #3F5FD8;
          border-radius: 10px;
          padding: 20px;
          gap: 30px;

          .shortcut-upper {
            display: flex;
            flex-direction: row;
            gap: 5px;
            justify-content: space-between;

            .information {
              display: flex;
              flex-direction: column;
              gap: 10px;

              .shortcut-description {
                font-size: 14px;
                color: #50627E;
              }
            }

            .logo {
              display: flex;
              align-items: center;
              justify-content: center;
              padding: 5px 15px;
              border-radius: 50%;
              background: #3F5FD8;
            }
          }

          .shortcut-lower {
            display: flex;

            button {
              width: 100%;
              background: #3F5FD8;
              font-weight: bold;
              justify-content: center;
            }
          }
        }
      }
    }

    #news-container {
      display: flex;
      flex-direction: column;
      padding: 30px;
      width: 30%;
      background: #DFE7FB;
      border-radius: 10px;
      gap: 20px;

      .news-upper {
        display: flex;
        flex-direction: row;
        justify-content: space-between;

        #news-title {
          font-size: 24px;
          font-weight: bold;

        }

        #see-all {
          color: #1A2C6F;
          margin: auto 0;
          cursor: pointer;
        }
      }

      .news-lower {
        display: flex;
        flex-direction: column;
        gap: 10px;

        .news-row {
          display: flex;
          flex-direction: row;
          gap: 10px;

          .title{
          white-space: nowrap;
          width: 100%;
          text-overflow: ellipsis;
          overflow: hidden;
          }
        }
      }
    }
  }
}
</style>