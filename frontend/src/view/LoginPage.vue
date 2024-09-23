<template>
  <div>
    <h1>Login</h1>
    <form @submit.prevent="login">
      <div>
        <label for="username">Username:</label>
        <input v-model="username" type="text" id="username" required>
      </div>
      <div>
        <label for="password">Password:</label>
        <input v-model="password" type="password" id="password" required>
      </div>
      <button type="submit">Login</button>
    </form>
    <p v-if="errorMessage" style="color: red;">{{ errorMessage }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      password: '',
      errorMessage: ''
    };
  },
  methods: {
    async login() {
      try {
        const response = await fetch('/api/fja-fap/auth/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            userName: this.username,
            password: this.password
          })
        });

        if (response.ok) {
          const data = await response.json();
          localStorage.setItem('jwtToken', data.result.token);
          this.$router.push('/user-management');
        } else {
          const errorData = await response.json();
          this.errorMessage = errorData.message || 'Login failed';
        }
      } catch (error) {
        this.errorMessage = 'Error connecting to server';
        console.error(error);
      }
    }
  }
};
</script>
