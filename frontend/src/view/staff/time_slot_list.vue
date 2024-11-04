<template>
  <div class="time-slot-container">
    <h1>Time Slot List</h1>
    <div class="button-container">
      <button class="add-time-slot">
        <VsxIcon iconName="AddCircle" size="20" type="bold" class="add-icon" />
        <i class="icon-add"></i> Add time slot
      </button>
    </div>
    <table class="slot-table">
      <thead>
      <tr>
        <th>No</th>
        <th>Slot</th>
        <th>Start Time</th>
        <th>End Time</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(slot, index) in slots" :key="slot.id">
        <td>{{ index + 1 }}</td>
        <td>{{ slot.name }}</td>
        <td>{{ slot.startTime }}</td>
        <td>{{ slot.endTime }}</td>
        <td class="action-buttons">
          <button class="icon-button">
            <VsxIcon iconName="Edit" :size="20" color="#5584FF" type="linear" />
          </button>
          <button class="icon-button">
            <VsxIcon iconName="Slash" :size="20" color="#5584FF" type="linear" />
          </button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      slots: [
        { id: 1, name: 'Slot 1', startTime: '08:00', endTime: '09:00' },
        { id: 2, name: 'Slot 2', startTime: '09:15', endTime: '10:15' },
        // Add more slots as needed
      ],
    };
  },
};
</script>

<style scoped>
.time-slot-container {
  padding: 30px;
}

h1 {
  width: fit-content;
  font-size: 36px;
  background: -webkit-linear-gradient(180deg, #304CB2, #1A2C6F);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-weight: bold;
  margin: 20px 0px;
}

.button-container {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 15px;
}

.add-time-slot {
  background-color: #007bff;
  border: none;
  color: #ffffff;
  padding: 10px 20px;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  border-radius: 5px;
}

.add-icon {
  margin-right: 10px;
}

.slot-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.slot-table th,
.slot-table td {
  border: none;
  padding: 8px;
  text-align: center;
}

.slot-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}

.action-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
}

.icon-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  margin: 0 10px;
}

.icon-button:last-child {
  margin-right: 0;
}
</style>
