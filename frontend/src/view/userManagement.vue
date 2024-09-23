<template>
  <div>
    <h1>User Management</h1>
    <button @click="fetchUsers">Load Users</button>

    <table v-if="users.length > 0">
      <thead>
      <tr>
        <th>User ID</th>
        <th>Username</th>
        <th>Email</th>
        <th>Roles</th>
        <th>Active</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in users" :key="user.userId">
        <td>{{ user.userId }}</td>
        <td>{{ user.username }}</td>
        <td>{{ user.email }}</td>
        <td>{{ user.roles }}</td>
        <td>{{ user.active ? 'Yes' : 'No' }}</td>
      </tr>
      </tbody>
    </table>

    <p v-else>No users found</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      users: []
    };
  },
  methods: {
    async fetchUsers() {
      const token = localStorage.getItem('jwtToken');
      if (!token) {
        console.error('Token not found in localStorage');
        return;
      }

      try {
        const response = await fetch('/api/fja-fap/user', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });

        const data = await response.json();
        if (response.ok) {
          this.users = data;
        } else {
          console.error('Failed to fetch users:', data);
        }
      } catch (error) {
        console.error('Error fetching users:', error);
      }
    }
  }
};
</script>
