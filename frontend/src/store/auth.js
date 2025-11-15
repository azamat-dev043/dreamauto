import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { login, register } from '../api/authApi'

const STORAGE_KEY = 'dreamauto_auth'

const readStoredAuth = () => {
  if (typeof window === 'undefined') return null
  const raw = window.localStorage.getItem(STORAGE_KEY)
  return raw ? JSON.parse(raw) : null
}

export const useAuthStore = defineStore('auth', () => {
  const authState = ref(readStoredAuth())

  const user = computed(() => authState.value?.user ?? null)
  const token = computed(() => authState.value?.token ?? null)
  const isAuthenticated = computed(() => Boolean(token.value))
  const isAdmin = computed(() => user.value?.role === 'ADMIN')
  const userId = computed(() => user.value?.id ?? null)
  const userName = computed(
    () => user.value?.name || user.value?.email || 'DreamAuto user',
  )

  const persist = () => {
    if (typeof window === 'undefined') {
      return
    }
    if (authState.value) {
      window.localStorage.setItem(STORAGE_KEY, JSON.stringify(authState.value))
    } else {
      window.localStorage.removeItem(STORAGE_KEY)
    }
  }

  const setAuthData = (payload) => {
    authState.value = payload
    persist()
  }

  const buildAuthPayload = (data, fallback = {}) => ({
    token: data.token,
    user: {
      id: data.userId,
      role: data.role,
      email: data.email ?? fallback.email ?? '',
      name: data.name ?? fallback.name ?? '',
      phoneNumber: data.phoneNumber ?? fallback.phoneNumber ?? '',
    },
  })

  const loginUser = async (credentials) => {
    const { data } = await login(credentials)
    setAuthData(buildAuthPayload({ ...data, email: credentials.email }))
  }

  const registerUser = async (payload) => {
    const { data } = await register(payload)
    setAuthData(
      buildAuthPayload({
        ...data,
        email: payload.email,
        name: payload.name,
        phoneNumber: payload.phoneNumber,
      }),
    )
  }

  const logout = () => {
    authState.value = null
    persist()
  }

  return {
    authState,
    user,
    token,
    userId,
    userName,
    isAuthenticated,
    isAdmin,
    login: loginUser,
    register: registerUser,
    logout,
    setAuthData,
  }
})
