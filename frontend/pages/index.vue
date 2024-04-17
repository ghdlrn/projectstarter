<template>
  <div>
    <h1>Welcome to Our Service!</h1>
    <p v-if="auth.isAuthenticated">Thanks for logging in, {{ auth.user.name }}!</p>
    <div v-else>
      <p>Please login or register to enjoy our full services.</p>
      <!-- Login Form -->
      <form @submit.prevent="handleLogin">
        <input v-model="loginEmail" type="email" placeholder="Email">
        <input v-model="loginPassword" type="password" placeholder="Password">
        <button type="submit">Login</button>
      </form>
      <!-- Registration Form -->
      <form @submit.prevent="handleRegister">
        <input v-model="registerName" placeholder="Name">
        <input v-model="registerEmail" type="email" placeholder="Email">
        <input v-model="registerPassword" type="password" placeholder="Password">
        <button type="submit">Register</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/useAuth';
const auth = useAuthStore();

const loginEmail = ref('');
const loginPassword = ref('');
const registerName = ref('');
const registerEmail = ref('');
const registerPassword = ref('');

const handleLogin = async () => {
  try {
    await auth.login(loginEmail.value, loginPassword.value);
    alert('Login successful');
    loginEmail.value = '';
    loginPassword.value = '';
  } catch (error) {
    console.error(error.message);
    alert('Login failed: ' + error.message);
  }
}

const handleRegister = async () => {
  try {
    await auth.register({
      name: registerName.value,
      email: registerEmail.value,
      password: registerPassword.value
    });
    alert('Registration successful');
    registerName.value = '';
    registerEmail.value = '';
    registerPassword.value = '';
  } catch (error) {
    console.error(error.message);
    alert('Registration failed: ' + error.message);
  }
}
</script>
