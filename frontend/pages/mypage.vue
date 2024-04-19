<script setup lang="ts">
const router = useRouter();
const user = reactive({
  userId: null,
  name: "",
  email: "",
});

const getUser = () => {
  fetch("http://localhost:8181/user", {
    credentials: "include",
  })
      .then((response) => response.json())
      .then((data) => {
        user.userId = data.userId;
        user.name = data.name;
        user.email = data.email;

        userUpdatingForm.name = data.name;
        userUpdatingForm.email = data.email;
      })
      .catch((_) => {
        router.push("/login");
      });
}
getUser()

const logout = () => {
  fetch("http://localhost:8181/logout", {
    credentials: "include",
  });

  router.push("/");
};

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

const userUpdatingForm = reactive({
  name: "",
  email: "",
  password: "",
});
const updateUser = () => {
  fetch("http://localhost:8181/user", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify(userUpdatingForm),
  }).then((_) => getUser());
};

const deleteUser = () => {
  fetch("http://localhost:8181/user", {
    method: "DELETE",
    credentials: "include",
  }).then((_) => {
    logout();
    router.push("/");
  });
};
</script>

<template>
  <h1>MyPage</h1>

  <div>
    <div>userId: {{ user.userId }}</div>
    <div>name: {{ user.name }}</div>
    <div>email: {{ user.email }}</div>

    <div>
      <button @click="logout()">logout</button>
    </div>
  </div>

  <hr/>
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

  <hr/>
  <div>
    <h2>Update User</h2>
    <div>
      <div>name: <v-text-field type="text" v-model="userUpdatingForm.name"/></div>
      <div>email: <v-text-field type="email" v-model="userUpdatingForm.email"/></div>
      <div>
        password: <v-text-field type="password" v-model="userUpdatingForm.password"/>
      </div>
      <v-btn @click="updateUser()">update</v-btn>
    </div>
  </div>

  <hr/>
  <div>
    <h2>Delete User</h2>
    <v-btn @click="deleteUser()">delete me</v-btn>
  </div>
</template>