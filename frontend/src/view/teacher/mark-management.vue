<template>
    <div class="container">
        <div class="headContent">
            <h1>Manage mark</h1>
            <p>Exam: <b>Chapter 1 - Vocabulary</b></p>
        </div>

        <div class="actions">
            <p>Class: <b>Blue</b></p>
            <p>Date: <b>3/9/2024</b></p>
            <p>Slot: <b>8:30 - 12:30</b></p>
        </div>

        <div class="table-container">
            <form>
                <table>
                    <thead>
                        <tr>
                            <th class="center">No</th>
                            <th>Japanese name</th>
                            <th>Roll number</th>
                            <th class="center">Mark</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="center">1</td>
                            <td>ファム・テ・ミン</td>
                            <td>FA171392</td>
                            <td class="center">
                                <input type="number" min="0" max="10" />
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
input {
    border-radius: 10px;
    border: 1px solid var(--border);
    width: 100px;
    height: 30px;
    padding: 10px;
    font-size: 16px;
}

th:last-child,
td:last-child {
    width: 150px
}

input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
}
</style>