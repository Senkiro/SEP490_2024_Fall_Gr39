<template>
    <div class="container">
        <div class="headContent">
            <h1>Take attendance</h1>
        </div>

        <div class="table-container">
            <form>
                <table>
                    <thead>
                        <tr>
                            <th class="center">No</th>
                            <th>Japanese name</th>
                            <th>Roll number</th>
                            <th class="center">Image</th>
                            <th class="center">Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="center">1</td>
                            <td>ファム・テ・ミン</td>
                            <td>FA171392</td>
                            <td class="center"><img src="@/assets/smiling-young-man-illustration_1308-174669.avif"></td>
                            <td class="center">
                                <div class="radio-group">
                                    <div class="radio">
                                        <input type="radio" id="attended" value="Attended" name="status" />
                                        <label for="attended">Attended</label>
                                    </div>
                                    <div class="radio">
                                        <input type="radio" id="absent" value="Absent" name="status" />
                                        <label for="absent">Absent</label>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <div class="actions">
                    <button>
                        <VsxIcon iconName="Save2" size="20" type="bold" />
                        Save
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>

<script>
export default {

}
</script>

<style lang="scss" scoped>
img {
    width: 150px;
    border-radius: 20px;
}

th:last-child,
td:last-child {
    width: 10%;
}

.radio-group {
    display: flex;
    flex-direction: column;
    gap: 20px;
    justify-content: center;

    .radio {
        display: flex;
        width: fit-content;
        flex-direction: row;
        gap: 10px;
    }
}
</style>