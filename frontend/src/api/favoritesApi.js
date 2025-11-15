import httpClient from './httpClient'

export const listFavorites = (params) =>
  httpClient.get('/api/favorites', {
    params,
  })

export const addFavorite = (adId) => httpClient.post(`/api/favorites/${adId}`)

export const removeFavorite = (adId) =>
  httpClient.delete(`/api/favorites/${adId}`)
