<template>
    <div class="container">
        <div class="headContent">
            <h1>Class record</h1>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th class="center">No</th>
                        <th>Class</th>
                        <th class="center">Number of students</th>
                        <th class="center">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(classItem, index) in classes" :key="classItem.id">
                        <td class="center">{{ index + 1 }}</td>
                        <td :style="{ color: classItem.classColor }">{{ classItem.name }}</td>
                        <td class="center">{{ classItem.studentCount }}</td>
                        <td class="center">
                            <VsxIcon iconName="Eye" :size="30" color="#171717" type="linear"
                                @click="viewClassDetail(classItem)" />
                        </td>
                    </tr>
                    <tr v-if="classes.length === 0">
                        <td colspan="4" class="center">No record.</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
import { VsxIcon } from "vue-iconsax";

export default {
    name: "ClassRecord",
    components: { VsxIcon },
    data() {
        return {
            batchEntity: { name: 'FALL2024' },
            classes: [
                { id: 1, name: 'Blue', classColor: 'blue', studentCount: 30 },
                { id: 2, name: 'Red', classColor: 'red', studentCount: 30 },
                { id: 3, name: 'Green', classColor: 'green', studentCount: 30 },
                { id: 4, name: 'Yellow', classColor: 'yellow', studentCount: 30 },
                { id: 5, name: 'Purple', classColor: 'purple', studentCount: 30 }
            ]
        };
    },
    methods: {
        viewClassDetail(classItem) {
            console.log("Viewing class: ", classItem);
        }
    }
};
</script>

<style scoped></style>