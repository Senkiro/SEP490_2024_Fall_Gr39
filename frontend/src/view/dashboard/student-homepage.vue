<template>
    <div class="container">
        <h2>Upcoming activities</h2>
        <div class="activities">
            <div class="daily-activities">
                <h3>Date: 4/9/2024 - Tomorrow</h3>
                <div class="activities-container">
                    <div class="activity">
                        <div class="thumbnail">
                            <p id="lesson">Lesson</p>
                            <p id="number">1</p>
                        </div>

                        <div class="information">
                            <span>
                                <p>Chapter 2 - Lesson 1</p>
                            </span>
                            <span>Exam: <b>Chapter 1 - Grammar</b></span>
                            <span>Teacher: <b>Yuri Ikeda</b></span>
                        </div>
                    </div>
                    <div class="activity">
                        <div class="thumbnail">
                            <p id="lesson">Lesson</p>
                            <p id="number">1</p>
                        </div>

                        <div class="information">
                            <span>
                                <p>Chapter 2 - Lesson 1</p>
                            </span>
                            <span>Exam: <b>Chapter 1 - Grammar</b></span>
                            <span>Teacher: <b>Yuri Ikeda</b></span>
                        </div>
                    </div>
                </div>

            </div>

            <div class="daily-activities">
                <h3>Date: 5/9/2024</h3>
                <div class="activities-container">
                    <div class="activity">
                        <div class="thumbnail">
                            <p id="lesson">Lesson</p>
                            <p id="number">1</p>
                        </div>

                        <div class="information">
                            <span>
                                <p>Chapter 2 - Lesson 1</p>
                            </span>
                            <span>Exam: <b>Chapter 1 - Grammar</b></span>
                            <span>Teacher: <b>Yuri Ikeda</b></span>
                        </div>

                    </div>
                </div>

            </div>

            <div class="daily-activities">
                <h3>Date: 6/9/2024</h3>
                <div class="activities-container">
                    <div class="activity">
                        <div class="thumbnail">
                            <p id="lesson">Lesson</p>
                            <p id="number">1</p>
                        </div>

                        <div class="information">
                            <span>
                                <p>Chapter 2 - Lesson 1</p>
                            </span>
                            <span>Exam: <b>Chapter 1 - Grammar</b></span>
                            <span>Teacher: <b>Yuri Ikeda</b></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row2">
            <div class="graph"></div>
          <div id="news-container">
            <div class="news-upper">
              <h2>News</h2>
              <p id="see-all" @click="navigateToNewsRecord">See all</p>
            </div>

            <div class="news-lower">
              <div v-for="news in newsList" :key="news.newId" class="news-row">
                <p class="date">{{ new Date(news.createDate).toLocaleDateString() }}</p>
                <p class="title">{{ news.newTitle }}</p>
              </div>
            </div>
          </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      newsList: [],
    }
  },
  methods: {
    async fetchAllNews() {
      try {
        const token = sessionStorage.getItem("jwtToken");
        const response = await axios.get(
            "http://localhost:8088/fja-fap/staff/get-all-publish-news",
            {
              params: {
                page: 0,
                size: 10,
              },
              headers: { Authorization: `Bearer ${token}` },
            }
        );

        if (response.data.code === 0) {
          this.newsList = response.data.result.content || [];
        } else {
          this.showNotification(
              "Unable to load news: " + response.data.message,
              "error"
          );
        }
      } catch (error) {
        console.error("Error fetching news:", error);
        this.showNotification(
            "An error occurred while loading news.",
            "error"
        );
      }
    },
    navigateToNewsRecord() {
      this.$router.push({ name: "StudentNewsList" });
    },
  },
  mounted() {
    this.fetchAllNews();
  },
}
</script>

<style lang="scss">
h2 {
    margin: 10px 0;
}

.activities {
    display: flex;
    flex-direction: row;
    gap: 20px;

    .daily-activities {
        display: flex;
        flex-direction: column;
        width: 33%;
        gap: 10px;
        border-radius: 20px;

        h3 {
            font-weight: bold;
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
                    padding: 10px;
                    gap: 5px;
                    width: 80px;
                    height: 80px;
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
                }

            }
        }

    }
}

.row2 {
    display: flex;
    flex-direction: row;
    gap: 20px;

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

                .title {
                    white-space: nowrap;
                    width: 100%;
                    text-overflow: ellipsis;
                    overflow: hidden;
                }
            }
        }
    }
}
</style>