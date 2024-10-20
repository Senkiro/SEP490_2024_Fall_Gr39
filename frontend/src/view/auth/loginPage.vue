<template>
  <div class="login-page">
    <div class="login-left">
      <!-- Blue area -->
    </div>
    <div class="login-right">
      <h2>Welcome</h2>
      <p>to Nihon Study Guide</p>
      <form @submit.prevent="login">
        <div>
          <input v-model="username" type="text" id="email" placeholder="Email" required>
        </div>
        <div>
          <input v-model="password" type="password" id="password" placeholder="Password" required>
        </div>
        <a @click.prevent="goToForgotPassword" class="forgot-password">Forgot Password?</a>
        <button type="submit" :disabled="isLoading" class="login-button">Login</button>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      password: '',
      errorMessage: '',
      isLoading: false
    };
  },
  methods: {
    async login() {
      this.isLoading = true;
      this.errorMessage = '';

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
          localStorage.setItem('userRole', data.result.scope);
          this.redirectUser(data.result.scope);
          console.log(data.result.scope);
        } else {
          const errorData = await response.json();
          this.errorMessage = errorData.message || 'Login failed';
        }
      } catch (error) {
        this.errorMessage = 'Error connecting to server';
        console.error(error);
      } finally {
        this.isLoading = false;
      }
    },
    redirectUser(role) {
      role = role.toUpperCase();
      if (role === 'MANAGER') {
        this.$router.push({ name: 'ManagerHomePage' });
      } else if (role === 'STAFF') {
        this.$router.push({ name: 'StaffHomePage' });
      } else {
        this.$router.push({ name: 'LoginPage' }); // Đường dẫn mặc định nếu không khớp role
      }
    },
    goToForgotPassword() {
      this.$router.push('/forgot-password');
    }
  }
};
</script>

<style scoped>
.login-page {
  display: flex;
  height: 100vh;
  background-color: #f7f7f7;
}

.login-left {
  flex: 2;
  background-color: #87CEFA;
}

.login-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  padding: 0 100px;
  background-color: white;
}

h2 {
  font-size: 36px;
  background: linear-gradient(90deg, #1A2C6F 0%, #3E5DD4 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 5px;
}

p {
  margin-top: 2px;
  font-size: 16px;
  color: black;
  margin-bottom: 20px;
}

form {
  width: 100%;
}

input {
  width: 100%;
  padding: 10px;
  margin-bottom: 15px;
  border-radius: 5px;
  border: 1px solid #ddd;
  font-size: 16px;
}

button.login-button {
  width: 100%;
  padding: 10px;
  background: linear-gradient(90deg, #1A2C6F 0%, #3E5DD4 100%);
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 18px;
  cursor: pointer;
}

button.login-button:hover {
  background-color: #0035a0;
}


.forgot-password {
  display: block;
  text-align: right;
  font-size: 14px;
  margin-bottom: 20px;
  color: #888;
  text-decoration: none;
}

.forgot-password:hover {
  text-decoration: underline;
}
</style>
