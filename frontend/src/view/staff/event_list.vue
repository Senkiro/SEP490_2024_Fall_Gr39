<template>
  <div class="event-list-container">
    <h1>Event List</h1>
    <div class="button-container">
      <button class="add-student">
        <VsxIcon iconName="AddCircle" size="20" type="bold" class="add-icon"/>
        Add Event
      </button>
      <button class="import-student">
        <VsxIcon iconName="Import" size="20" type="bold" class="import-icon" />
        Import student
      </button>
    </div>
    <table class="event-table">
      <thead>
      <tr>
        <th>No</th>
        <th>Title</th>
        <th>Destination</th>
        <th>Information</th>
        <th>Date</th>
        <th>Status</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(event, index) in events" :key="event.id">
        <td>{{ index + 1 }}</td>
        <td>{{ event.title }}</td>
        <td>{{ event.destination }}</td>
        <td>{{ event.information }}</td>
        <td>{{ event.date }}</td>
        <td :class="event.status === 'Not happen' ? 'status-pending' : 'status-finished'">
          {{ event.status }}
        </td>
        <td class="action-buttons">
          <button class="icon-button">
            <VsxIcon iconName="Eye" :size="20" color="#5584FF" type="linear" />
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
      events: [
        { id: 1, title: 'Trip to Tokyo tower & F...', destination: 'Arakawa-ku', information: 'Information...', date: '31/11/2024', status: 'Not happen' },
        { id: 2, title: 'Event 2', destination: 'Destination', information: 'Information...', date: 'x/7/2024', status: 'Finished' },
        { id: 3, title: 'Event 3', destination: 'Destination', information: 'Information...', date: 'x/3/2024', status: 'Finished' },
      ],
    };
  },
};
</script>

<style>
.event-list-container {
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
  gap: 10px;
  margin-bottom: 20px;
}

.add-student, .import-student {
  background-color: #304CB2;
  border: none;
  color: #ffffff;
  padding: 8px 15px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  border-radius: 6px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s ease;
}

.add-student:hover, .import-student:hover {
  background-color: #1A2C6F;
}

.event-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  font-family: Arial, sans-serif;
}

.event-table th,
.event-table td {
  border: none;
  padding: 8px;
  text-align: center;
}

.event-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}

.event-table tbody tr {
  border-bottom: 1px solid #eaeaea;
}

.event-table tbody tr:hover {
  background-color: #f9f9f9;
}

.event-table td {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.status-pending {
  color: #1A2C6F !important;
  font-weight: 600;
}

.status-finished {
  color: #007bff !important;
  font-weight: 600;
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
  padding: 5px;
  margin: 0 5px;
  transition: transform 0.2s ease;
}

.icon-button:hover {
  transform: scale(1.1);
}

.add-icon {
  margin-right: 8px; /* Adjust the value as needed */
}

.import-icon {
  margin-right: 8px; /* Adjust the value as needed */
}
</style>
