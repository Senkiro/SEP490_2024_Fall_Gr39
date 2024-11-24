<template>
    <div class="container">
        <div class="headContent">
            <h1>Mark entry management</h1>
        </div>

        <div class="filters">
            <select id="class-filter" class="filter-select" v-model="selectedClass" @change="filterStudentsByClass">
                <option value="">All Class</option>
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
                        <th>Date</th>
                        <th>Slot</th>
                        <th>Class</th>
                        <th>Exam</th>
                        <th>Status</th>
                        <th class="center">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="center">1</td>
                        <td>2/9/2024</td>
                        <td>8:30 - 12:30</td>
                        <td>Red</td>
                        <td>Chapter 1 - Vocabulary</td>
                        <!-- Status: 
                        - Tiết đã diễn ra và đã thêm điểm: class="yes", nội dung là "Added"
                        - Tiết đã diễn ra nhưng chưa thêm điểm: class="no", nội dung là "Not added"
                        - Tiết chưa diễn ra: class="not-happen", nội dung là "Not happen" -->
                        <td class="yes">Added</td>
                        <td class="center">
                            <!-- - Nếu trước ngày hôm nay 2 ngày hoặc sau ngày hôm nay (session chưa diễn ra) thì class="disabled" 
                            - Nếu đã nhập điểm thì nội dung là "Edit mark", nếu chưa thì nội dung là "Add mark" -->
                            <button class="table-button disabled" disabled>Edit mark</button>
                        </td>
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

<style lang="scss" scoped>
.table-button{
    width: 100px;
    justify-content: center;
}

th:last-child,td:last-child{
    width: 130px;
}
</style>