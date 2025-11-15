import axios from 'axios'

export const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

const httpClient = axios.create({
  baseURL: API_BASE_URL,
})

httpClient.interceptors.request.use((config) => {
  if (typeof window === 'undefined') return config

  const raw = window.localStorage.getItem('dreamauto_auth')
  if (raw) {
    const parsed = JSON.parse(raw)
    if (parsed?.token) {
      config.headers.Authorization = `Bearer ${parsed.token}`
    }
  }

  return config
})

httpClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status
    if (typeof window !== 'undefined' && status && [401, 403].includes(status)) {
      window.localStorage.removeItem('dreamauto_auth')
      if (window.location.pathname !== '/login') {
        window.location.href = '/login'
      }
    }
    return Promise.reject(error)
  },
)

export default httpClient
