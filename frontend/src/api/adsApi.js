import httpClient from './httpClient'

export const fetchAds = (params) =>
  httpClient.get('/api/ads', {
    params,
  })

export const fetchAdById = (id) => httpClient.get(`/api/ads/${id}`)

export const createAd = (payload) => httpClient.post('/api/ads', payload)

export const updateAd = (id, payload) => httpClient.put(`/api/ads/${id}`, payload)

export const addPhotos = (id, photoUrls) =>
  httpClient.post(`/api/ads/${id}/photos`, {
    photoUrls,
  })

export const fetchMyAds = () => httpClient.get('/api/ads/my')
