import httpClient from './httpClient'

export const fetchAdminAds = (params) =>
  httpClient.get('/api/admin/ads', {
    params,
  })

export const blockUser = (userId) =>
  httpClient.patch(`/api/admin/users/${userId}/block`)

export const unblockUser = (userId) =>
  httpClient.patch(`/api/admin/users/${userId}/unblock`)

export const deactivateAd = (adId) =>
  httpClient.patch(`/api/admin/ads/${adId}/deactivate`)

export const activateAd = (adId) =>
  httpClient.patch(`/api/admin/ads/${adId}/activate`)
