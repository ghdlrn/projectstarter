<!-- login.vue -->
<template>
  <div>
    <input v-model="email" type="email" placeholder="Email">
    <input v-model="password" type="password" placeholder="Password">
    <button @click="handleLogin">Login</button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/useAuth';

const email = ref('');
const password = ref('');
const auth = useAuthStore();

const handleLogin = async () => {
  try {
    await auth.login(email.value, password.value);
    console.log('Login successful');
    // Redirect or perform additional actions on successful login
  } catch (error) {
    console.error(error.message);
    // Handle login failure (e.g., display an error message)
  }
}
</script>
