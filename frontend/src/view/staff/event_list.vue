<template>
  <div class="container">
    <div class="headContent">
      <h1>Event List</h1>
    </div>
    <div class="actions">
      <button>
        <VsxIcon iconName="AddCircle" size="20" type="bold" />
        Add Event
      </button>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th class="center">No</th>
            <th>Title</th>
            <th>Destination</th>
            <th>Information</th>
            <th>Date</th>
            <th>Status</th>
            <th class="center">Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(event, index) in events" :key="event.id">
            <td class="center">{{ index + 1 }}</td>
            <td>{{ event.title }}</td>
            <td>{{ event.destination }}</td>
            <td>{{ event.information }}</td>
            <td>{{ event.date }}</td>
            <td :class="event.status === 'Not happen' ? 'status-pending' : 'status-finished'">
              {{ event.status }}
            </td>
            <td class="center">
              <div class="icon-group">
                <VsxIcon iconName="Eye" :size="25" color="#171717" type="linear" />
                <VsxIcon iconName="Slash" :size="25" color="#171717" type="linear" />
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
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
</style>
