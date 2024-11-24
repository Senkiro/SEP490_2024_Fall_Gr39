<template>
    <div class="container">
        <div class="headContent">
            <h1>Class member</h1>
        </div>

        <div class="filters">
            <select id="class-filter" class="filter-select" v-model="selectedClass" @change="filterStudentsByClass">
                <option v-for="classItem in classList" :key="classItem.id" :value="classItem.name">
                    {{ classItem.name }}
                </option>
            </select>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th class="center">No</th>
                        <th>Fullname</th>
                        <th>Japanese name</th>
                        <th>Roll number</th>
                        <th class="center">Image</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="center">1</td>
                        <td>Pham The Minh</td>
                        <td>ファム・テ・ミン</td>
                        <td>FA171392</td>
                        <td class="center"><img src="@/assets/smiling-young-man-illustration_1308-174669.avif"></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
export default {

}
</script>

<style lang="scss">
img{
    width: 150px;
}
</style>