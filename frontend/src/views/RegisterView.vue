<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'

const router = useRouter()
const authStore = useAuthStore()

const form = reactive({
  name: '',
  phoneNumber: '',
  email: '',
  password: '',
  confirmPassword: '',
})

const error = ref('')
const loading = ref(false)

const submit = async () => {
  error.value = ''
  if (form.password !== form.confirmPassword) {
    error.value = 'Passwords must match.'
    return
  }
  loading.value = true
  try {
    await authStore.register(form)
    router.push({ name: 'home' })
  } catch (err) {
    error.value = err.response?.data?.message || 'Could not complete registration.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <section class="page auth-page">
    <div class="panel auth-card">
      <h1>Create your account</h1>
      <p>Post ads, save favorites, and collaborate with DreamAuto community.</p>
      <form @submit.prevent="submit">
        <div class="form-control">
          <label for="name">Full name</label>
          <input id="name" v-model="form.name" required />
        </div>
        <div class="form-control">
          <label for="phone">Phone number</label>
          <input id="phone" v-model="form.phoneNumber" required placeholder="+77001234567" />
        </div>
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
            minlength="8"
            required
          />
        </div>
        <div class="form-control">
          <label for="confirm">Confirm password</label>
          <input
            id="confirm"
            v-model="form.confirmPassword"
            type="password"
            minlength="8"
            required
          />
        </div>
        <p v-if="error" class="error">{{ error }}</p>
        <button class="btn btn-primary" type="submit" :disabled="loading">
          {{ loading ? 'Creating...' : 'Register' }}
        </button>
      </form>
      <p class="alt-link">
        Already registered?
        <RouterLink :to="{ name: 'login' }">Login</RouterLink>
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
  max-width: 460px;
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
