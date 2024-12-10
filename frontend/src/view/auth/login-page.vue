<template>
  <div class="auth-page">
    <div class="login-side">
      <div class="title">
        <h2>Welcome</h2>
        <p>to Nihon Study Guide</p>
      </div>
      <form @submit.prevent="login">
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        <div>
          <input v-model="username" @input="errorMessage = ''" type="text" id="email" placeholder="Email" required>
        </div>
        <div>
          <input v-model="password" @input="errorMessage = ''" type="password" id="password" placeholder="Password"
            required>
        </div>
        <a @click.prevent="goToForgotPassword">Forgot Password?</a>
        <button type="submit" :disabled="isLoading" class="login-button">
          {{ isLoading ? 'Logging in...' : 'Login' }}
        </button>
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
      if (!this.username || !this.password) {
        this.errorMessage = 'Please fill out all fields';
        return;
      }

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
          console.log(data);

          // Lưu thông tin đăng nhập vào sessionStorage
          sessionStorage.setItem('jwtToken', data.result.token);
          sessionStorage.setItem('userRole', data.result.scope);
          sessionStorage.setItem('userName', this.username);
          sessionStorage.setItem('userId', data.result.userId);

          // Gọi API lấy thông tin sinh viên
          const studentResponse = await fetch(
              `http://localhost:8088/fja-fap/staff/get-student/${data.result.userId}`,
              {
                method: 'GET',
                headers: {
                  'Authorization': `Bearer ${data.result.token}`
                }
              }
          );

          if (studentResponse.ok) {
            const studentData = await studentResponse.json();

            // Lưu thông tin sinh viên vào sessionStorage
            sessionStorage.setItem('studentInfo', JSON.stringify(studentData.result));
          } else {
            console.error('Failed to fetch student info');
          }

          this.redirectUser(data.result.scope);
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
      } else if (role === 'TEACHER'){
        this.$router.push({ name: 'TeacherHomepage' });
      }else if (role === 'STUDENT'){
        this.$router.push({ name: 'StudentHomepage' });
      }else {
        this.$router.push({ name: 'LoginPage' }); // Default route if role is not recognized
      }
    },
    goToForgotPassword() {
      this.$router.push('/forgot-password');
    }
  }
};
</script>

<style lang="scss" scoped>
.auth-page {
  display: flex;
  flex-direction: row;
  height: 100vh;
  background-color: #f7f7f7;
  width: 100%;
  background-image: url("../../assets/login-background.jpg");
  background-size: cover;

  .login-side {
    display: flex;
    gap: 20px;
    flex-direction: column;
    justify-content: center;
    padding: 0 100px;
    background-color: white;
    width: 30rem;
    margin-left: auto;

    .title {
      margin-bottom: 2rem;

      h2 {
        font-size: 3rem;
        background: linear-gradient(90deg, #1A2C6F 0%, #3E5DD4 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        margin-bottom: 5px;
      }

      p {
        font-size: 16px;
        color: black;
        margin-bottom: 20px;
      }
    }

    form {
      width: 100%;

      input {
        width: 100%;
        padding: 10px;
        margin-bottom: 15px;
        border-radius: 0.5rem;
        border: 1px solid #ddd;
        font-size: 16px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      }

      a {
        display: flex;
        width: fit-content;
        margin-left: auto;
        font-size: 14px;
        margin-bottom: 20px;
        color: #888;
        text-decoration: none;
        cursor: pointer;
      }

      .login-button {
        width: 100%;
        padding: 10px;
        background: linear-gradient(90deg, #1A2C6F 0%, #3E5DD4 100%);
        color: white;
        border: none;
        border-radius: 0.5rem;
        font-size: 18px;
        cursor: pointer;
      }

      .error-message {
        color: rgb(95, 06, 06);
        margin-bottom: 10px;
      }
    }
  }
}
</style>
