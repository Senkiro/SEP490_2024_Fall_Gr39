<template>
  <div class="container">
    <div class="headContent">
        <h1>News list</h1>
    </div>

    <div class="news-container">
        <div class="news">
            <p class="date">dd/mm/yyyy</p>
            <router-link to="/news-detail">News 1</router-link>
        </div>
        <div class="news">
            <p class="date">dd/mm/yyyy</p>
            <router-link to="/news-detail">News 1</router-link>
        </div>
    </div>
  </div>
</template>

<script>
export default {

}
</script>

<style lang="scss" scoped>
.news-container{
display: flex;
flex-direction: column;
gap: 20px;
.news{
    display: flex;
    flex-direction: row;
    gap: 20px;

    .date{
        font-style: italic;
        color: var(--gray-2)
    }

    a{
        text-decoration: none;

        &:active{
            color: #6ECBB8;
        }

        &:visited{
            color: none;
        }
    }
}
}

</style>