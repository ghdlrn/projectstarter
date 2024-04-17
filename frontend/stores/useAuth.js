import { defineStore } from 'pinia';
import axios from 'axios';

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: null,
        token: null
    }),
    getters: {
        isAuthenticated: (state) => !!state.user
    },
    actions: {
        async login(email, password) {
            try {
                const { data } = await axios.post('/api/login', { email, password });
                this.user = data.user;
                this.token = data.token; // Store token if you're using token-based auth
            } catch (error) {
                throw new Error('Failed to login');
            }
        },
        async register(userDetails) {
            try {
                const { data } = await axios.post('/api/register', userDetails);
                this.user = data.user;
            } catch (error) {
                throw new Error('Failed to register');
            }
        },
        async updateUser(userDetails) {
            try {
                const { data } = await axios.put(`/api/users/${this.user.id}`, userDetails);
                this.user = data.user;
            } catch (error) {
                throw new Error('Failed to update user');
            }
        },
        async deleteUser() {
            try {
                await axios.delete(`/api/users/${this.user.id}`);
                this.user = null;
            } catch (error) {
                throw new Error('Failed to delete user');
            }
        },
        logout() {
            this.user = null;
            this.token = null;
        }
    }
});
