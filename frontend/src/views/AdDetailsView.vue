<script setup>
import { computed, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { fetchAdById } from '../api/adsApi'
import { addFavorite, listFavorites, removeFavorite } from '../api/favoritesApi'
import { useAuthStore } from '../store/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const ad = ref(null)
const loading = ref(true)
const error = ref('')
const isFavorite = ref(false)

const loadAd = async (id) => {
  loading.value = true
  error.value = ''
  try {
    const { data } = await fetchAdById(id)
    ad.value = data
    if (authStore.isAuthenticated) {
      await hydrateFavoriteState()
    }
  } catch (err) {
    error.value = err.response?.data?.message || 'Ad not found.'
  } finally {
    loading.value = false
  }
}

const hydrateFavoriteState = async () => {
  try {
    const { data } = await listFavorites({ page: 0, size: 50 })
    const list = data.content ?? []
    isFavorite.value = list.some(
      (item) => item.carAdId === ad.value?.id || item.id === ad.value?.id,
    )
  } catch {
    isFavorite.value = false
  }
}

const toggleFavorite = async () => {
  if (!authStore.isAuthenticated) {
    router.push({ name: 'login', query: { redirect: route.fullPath } })
    return
  }
  try {
    if (isFavorite.value) {
      await removeFavorite(ad.value.id)
    } else {
      await addFavorite(ad.value.id)
    }
    isFavorite.value = !isFavorite.value
  } catch (err) {
    error.value = err.response?.data?.message || 'Unable to update favorite.'
  }
}

const canEdit = computed(
  () => authStore.isAdmin || ad.value?.ownerId === authStore.userId,
)

watch(
  () => route.params.id,
  (id) => {
    if (id) loadAd(id)
  },
  { immediate: true },
)
</script>

<template>
  <section class="page">
    <div class="panel">
      <p v-if="loading">Loading ad...</p>
      <p v-else-if="error" class="error">{{ error }}</p>
      <template v-else-if="ad">
        <div class="ad-header">
          <div>
            <p class="ad-city">{{ ad.city }}</p>
            <h1>{{ ad.title }}</h1>
            <p class="ad-price">${{ ad.price?.toLocaleString?.() || ad.price }}</p>
          </div>
          <div class="ad-actions">
            <button class="btn btn-secondary" @click="toggleFavorite">
              {{ isFavorite ? 'Remove from favorites' : 'Add to favorites' }}
            </button>
            <RouterLink
              v-if="canEdit"
              class="btn btn-primary"
              :to="{ name: 'ad-edit', params: { id: ad.id } }"
            >
              Edit ad
            </RouterLink>
          </div>
        </div>

        <div class="gallery">
          <img
            v-for="(photo, index) in ad.photoUrls && ad.photoUrls.length ? ad.photoUrls : ['https://images.unsplash.com/photo-1503736334956-4c8f8e92946d?auto=format&fit=crop&w=900&q=80']"
            :key="index"
            :src="photo"
            :alt="`Photo ${index + 1}`"
          />
        </div>

        <div class="ad-details-grid">
          <div>
            <h3>Specifications</h3>
            <ul>
              <li>Brand: <strong>{{ ad.brand }}</strong></li>
              <li>Model: <strong>{{ ad.model }}</strong></li>
              <li>Year: <strong>{{ ad.year }}</strong></li>
              <li>Mileage: <strong>{{ ad.mileage }} km</strong></li>
              <li>Body: <strong>{{ ad.bodyType }}</strong></li>
              <li>Engine: <strong>{{ ad.engineType }}</strong></li>
              <li>Transmission: <strong>{{ ad.transmission }}</strong></li>
              <li>Drive: <strong>{{ ad.driveType }}</strong></li>
            </ul>
          </div>
          <div>
            <h3>Description</h3>
            <p>{{ ad.description }}</p>
          </div>
        </div>
      </template>
    </div>
  </section>
</template>

<style scoped>
.error {
  color: #b91c1c;
}

.ad-header {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
  flex-wrap: wrap;
}

.ad-city {
  text-transform: uppercase;
  letter-spacing: 0.15em;
  color: #64748b;
  font-size: 0.85rem;
}

.ad-price {
  margin: 0.5rem 0 0;
  font-size: 2rem;
  font-weight: 700;
}

.ad-actions {
  display: flex;
  gap: 0.75rem;
  align-items: center;
}

.gallery {
  margin: 1.5rem 0;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 1rem;
}

.gallery img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 0.75rem;
}

.ad-details-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 1.5rem;
}

ul {
  list-style: none;
  padding: 0;
  margin: 0.5rem 0 0;
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}
</style>
