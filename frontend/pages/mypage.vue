<template>
  <div>
    <h1>My Page</h1>
    <form @submit.prevent="updateUser">
      <div>
        <label for="name">Name:</label>
        <input id="name" v-model="name" type="text">
      </div>
      <div>
        <label for="email">Email:</label>
        <input id="email" v-model="email" type="email">
      </div>
      <button type="submit">Update Info</button>
      <button @click="deleteUser">Delete My Account</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/useAuth';
const auth = useAuthStore();

const name = ref(auth.user?.name || '');
const email = ref(auth.user?.email || '');

const updateUser = async () => {
  try {
    await auth.updateUser({ name: name.value, email: email.value });
    alert('Your information has been updated.');
  } catch (error) {
    console.error(error.message);
    alert('Failed to update your information.');
  }
}

const deleteUser = async () => {
  try {
    if (confirm('Are you sure you want to delete your account? This cannot be undone.')) {
      await auth.deleteUser();
      alert('Your account has been deleted.');
    }
  } catch (error) {
    console.error(error.message);
    alert('Failed to delete your account.');
  }
}
</script>
