<template>
    <div class="container">
        <div class="headContent">
            <h1>Class member</h1>
            <p>Class: <b>Blue</b></p>
        </div>

        <div class="actions">
            <div class="search-container">
                <input type="text" placeholder="Search..." class="search-field" />
                <VsxIcon iconName="SearchNormal1" color="#ADB5BD" type="linear" />
            </div>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th class="center">No</th>
                        <th>Japanese name</th>
                        <th>Fullname</th>
                        <th>Roll number</th>
                        <th class="center">Image</th>
                        <th class="center">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="center">1</td>
                        <td>ファム・テ・ミン</td>
                        <td>Pham The Minh</td>
                        <td>FA171392</td>
                        <td class="center"><img src="@/assets/smiling-young-man-illustration_1308-174669.avif"></td>
                        <td class="center">
                            <VsxIcon iconName="Eye" :size="30" color="#171717" type="linear"
                                @click="navigateToProfile(student.id)" />
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
export default {
    methods: {
        navigateToProfile(studentId) {
            switch (this.$route.name) {
                case "TeacherStudentRecord":
                    this.$router.push({ name: "TeacherStudentProfile", params: { id: studentId } });
                    break;
                case "StudentStudentRecord":
                    this.$router.push({ name: "StudentStudentProfile", params: { id: studentId } });
                    break;
            }
        },
    }

}
</script>

<style lang="scss">
img {
    width: 150px;
}
</style>