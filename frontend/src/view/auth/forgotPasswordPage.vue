<template>
  <div class="forgot-password-page">
    <div class="form-container">
      <h2><span class="highlight">Forgot password?</span></h2>
      <p>Enter your email address</p>
      <form @submit.prevent="handleForgotPassword">
        <input v-model="email" type="email" placeholder="Email" required />
        <button type="submit" class="reset-button">Reset password</button>
      </form>
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
      errorMessage: ''
    };
  },
  methods: {
    async handleForgotPassword() {
      try {
        const response = await fetch('/api/fja-fap/auth/forgot-password', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ email: this.email })
        });

        if (response.ok) {
          this.message = 'A reset link has been sent to your email.';
          this.email = ''; // Clear email field
        } else {
          const errorData = await response.json();
          this.errorMessage = errorData.message || 'Failed to send reset link.';
        }
      } catch (error) {
        this.errorMessage = 'Error connecting to server';
        console.error(error);
      }
    }
  }
};
</script>

<style scoped>
.forgot-password-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f7f7f7;
}

.form-container {
  background-color: white;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 300px;
}

h2 {
  font-size: 24px;
  background: linear-gradient(90deg, #1A2C6F 0%, #3E5DD4 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 10px;
}

h2 .highlight {
  color: #0046d9;
}

p {
  color: #777;
  font-size: 14px;
  margin-bottom: 20px;
}

input {
  width: 100%;
  padding: 10px;
  margin-bottom: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
}

.reset-button {
  width: 100%;
  padding: 10px;
  background: linear-gradient(90deg, #1A2C6F 0%, #3E5DD4 100%);
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

.reset-button:hover {
  background: linear-gradient(90deg, #1A6FCF 0%, #629DFF 100%);
}

.back-to-login {
  display: block;
  margin-top: 15px;
  font-size: 14px;
  color: #888;
  text-decoration: none;
}

.back-to-login:hover {
  text-decoration: underline;
}
</style>
