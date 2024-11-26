<template>
    <div class="container">
        <div class="headContent">
            <h1>Exam type</h1>
        </div>

        <div class="actions">
            <button @click="addExamTypePopup = true">
                <VsxIcon iconName="AddCircle" size="20" type="bold" />
                Add exam type
            </button>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th class="center">No</th>
                        <th>Exam type</th>
                        <th>Rate</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="center">1</td>
                        <td>Daily exam</td>
                        <td>1.6%</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div v-if="addExamTypePopup" class="popup-overlay">
            <div class="popup">
                <div class="exit-icon">
                    <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold"
                        @click="addExamTypePopup = false" />
                </div>
                <div class="popup-title">
                    <h2>Add exam type</h2>
                </div>
                <form @submit.prevent="addExamType">
                    <div class="form-group">
                        <label for="examtype">Exam type <span class="required">*</span></label>
                        <input type="text" id="examtype" required />
                    </div>
                    <div class="form-group">
                        <label for="rate">Rate(%) <span class="required">*</span></label>
                        <input type="number" id="rate" min="0" max="100" required />
                    </div>
                    <div class="actions">
                        <button type="submit">Add</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            addExamTypePopup: false
        }
    }

}
</script>

<style></style>