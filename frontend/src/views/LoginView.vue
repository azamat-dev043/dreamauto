<script setup>
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const form = reactive({
  email: '',
  password: '',
})

const error = ref('')
const loading = ref(false)

const submit = async () => {
  error.value = ''
  loading.value = true
  try {
    await authStore.login(form)
    const redirect = route.query.redirect || '/'
    router.push(redirect)
  } catch (err) {
    error.value = err.response?.data?.message || 'Login failed. Check credentials.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <section class="page auth-page">
    <div class="panel auth-card">
      <h1>Welcome back</h1>
      <p>Login to manage your ads, favorites, and account.</p>
      <form @submit.prevent="submit">
        <div class="form-control">
          <label for="email">Email</label>
          <input id="email" v-model="form.email" type="email" required autocomplete="email" />
        </div>
        <div class="form-control">
          <label for="password">Password</label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            required
            autocomplete="current-password"
          />
        </div>
        <p v-if="error" class="error">{{ error }}</p>
        <button class="btn btn-primary" type="submit" :disabled="loading">
          {{ loading ? 'Logging in...' : 'Login' }}
        </button>
      </form>
      <p class="alt-link">
        Need an account?
        <RouterLink :to="{ name: 'register' }">Create one</RouterLink>
      </p>
    </div>
  </section>
</template>

<style scoped>
.auth-page {
  display: flex;
  justify-content: center;
}

.auth-card {
  max-width: 420px;
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.auth-card form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.error {
  color: #b91c1c;
  font-weight: 600;
}

.alt-link {
  text-align: center;
  margin-top: 0.5rem;
}
</style>
