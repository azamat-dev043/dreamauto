import httpClient from './httpClient'

export const register = (payload) => httpClient.post('/api/auth/register', payload)

export const login = (payload) => httpClient.post('/api/auth/login', payload)
