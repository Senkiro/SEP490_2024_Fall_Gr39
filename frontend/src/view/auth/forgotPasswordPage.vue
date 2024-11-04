<template>
  <div class="auth-page">
    <div class="form-container">
      <h2>Forgot password?</h2>
      <p>Enter your email address</p>
      <form @submit.prevent="handleForgotPassword">
        <input v-model="email" type="email" placeholder="Email" required />
        <button type="submit" class="reset-button" :disabled="loading">Reset password</button>
      </form>
      <p v-if="message" class="success-message">{{ message }}</p>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      <a href="/login" class="back-to-login">Back to login</a>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      email: '',
      message: '',
      errorMessage: '',
      loading: false
    };
  },
  methods: {
    async handleForgotPassword() {
      this.loading = true;
      this.message = '';
      this.errorMessage = '';
      try {
        const response = await fetch('/api/fja-fap/auth/forget-password', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ email: this.email })
        });

        if (response.ok) {
          this.message = 'A new password has been sent to your email.';
          this.email = '';
        } else {
          const errorData = await response.json();
          this.errorMessage = errorData.message || 'Failed to send reset link.';
        }
      } catch (error) {
        this.errorMessage = 'Error connecting to server';
        console.error(error);
      } finally {
        this.loading = false;
      }
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
  justify-content: center;
  align-items: center;

  .form-container {
    display: flex;
    flex-direction: column;
    padding: 0px 8rem;
    background-color: white;
    border-radius: 2rem;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    width: 550px;
    height: 550px;
    justify-content: center;

    h2 {
      font-size: 2rem;
      background: linear-gradient(90deg, #1A2C6F 0%, #3E5DD4 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      margin-bottom: 5px;
    }

    p {
      color: #777;
      font-size: 14px;
      margin-bottom: 30px;
    }

    input {
      width: 100%;
      padding: 10px;
      margin-bottom: 20px;
      border: 1px solid #ddd;
      border-radius: 5px;
      font-size: 16px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }

    a {
      text-align: center;
      display: block;
      margin-top: 15px;
      font-size: 14px;
      color: #888;
      text-decoration: none;
    }

    button {
      width: 100%;
      padding: 10px;
      background: linear-gradient(90deg, #1A2C6F 0%, #3E5DD4 100%);
      color: white;
      border: none;
      border-radius: 0.5rem;
      font-size: 18px;
      cursor: pointer;
    }
  }
}

.reset-button:disabled {
  background: #ddd;
  cursor: not-allowed;
}

.success-message {
  color: green;
  margin-top: 15px;
  font-size: 14px;
}

.error-message {
  color: red;
  margin-top: 15px;
  font-size: 14px;
}
</style>
