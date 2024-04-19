<script setup lang="ts">

const userCreationForm = reactive({
  name: "",
  email: "",
  password: "",
});
const addUser = () => {
  fetch("http://localhost:8181/user", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify(userCreationForm),
  }).then((_)=> {
    userCreationForm.name = ""
    userCreationForm.email = ""
    userCreationForm.password = ""
  });
};



type UserType = {
  userId: number;
  name: string;
  email: string;
};
const users = ref<UserType[]>([]);

fetch("http://localhost:8181/users")
    .then((response) => response.json())
    .then((data) => {
      users.value = data;
    })
    .catch((_) => {});
</script>
<template>
  <div>
    <h1>Top</h1>
    <div>
      <ul>
        <li v-for="user in users">
          {{ user.userId }}/{{ user.name }}/{{ user.email }}
        </li>
      </ul>
    </div>
  </div>

  <div>
    <h2>Add User</h2>
    <div>
      <div>name: <v-text-field type="text" v-model="userCreationForm.name"/></div>
      <div>email: <v-text-field type="email" v-model="userCreationForm.email"/></div>
      <div>
        password: <v-text-field type="password" v-model="userCreationForm.password"/>
      </div>
      <v-btn @click="addUser()">add</v-btn>
    </div>
  </div>

</template>