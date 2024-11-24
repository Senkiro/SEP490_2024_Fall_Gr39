<template>
    <div class="container">
        <div class="headContent">
            <h1>Holiday list</h1>
        </div>

        <div class="actions">
            <button @click="addHolidayPopup = true">
                <VsxIcon iconName="AddCircle" size="20" type="bold" />
                Add holiday
            </button>
            <button @click="navigateToImportHoliday">
                <VsxIcon iconName="Import" size="20" type="bold" />
                Import holiday
            </button>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th class="center">No</th>
                        <th>Holiday name</th>
                        <th>Date</th>
                        <th class="center">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="center">1</td>
                        <td>New year</td>
                        <td>1/1</td>
                        <td>
                            <div class="icon-group">
                                <VsxIcon iconName="Edit2" :size="25" color="#171717" type="linear" />
                                <VsxIcon iconName="Trash" :size="25" color="#171717" type="linear" />
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div v-if="addHolidayPopup" class="popup-overlay">
            <div class="popup">
                <div class="exit-icon">
                    <VsxIcon iconName="CloseCircle" :size="25" color="#dae4f3" type="bold" @click="addHolidayPopup = false" />
                </div>
                <div class="popup-title">
                    <h2>Add Holiday</h2>
                </div>
                <form @submit.prevent="addHoliday">
                    <div class="form-group">
                        <label for="holidayName">Holiday name <span class="required">*</span></label>
                        <input type="text" id="holidayName" />
                    </div>
                    <div class="form-group">
                        <label for="address">Date <span class="required">*</span></label>
                        <input type="date" id="date" required />
                    </div>
                    <div class="actions">
                        <button type="submit">Create</button>
                    </div>
                </form>
                <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data(){
        return{
            addHolidayPopup:false
        }
    },
    methods:{
        navigateToImportHoliday(){
            this.$router.push({ name: 'ImportHoliday'})
        }
    }

}
</script>

<style></style>