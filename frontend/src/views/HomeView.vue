<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useAdsStore } from '../store/ads'
import { useAuthStore } from '../store/auth'
import { fetchAds } from '../api/adsApi'
import AdsFilterBar from '../components/AdsFilterBar.vue'
import AdCard from '../components/AdCard.vue'
import Pagination from '../components/Pagination.vue'

const adsStore = useAdsStore()
const authStore = useAuthStore()
const ads = ref([])
const loading = ref(false)
const error = ref('')
const pagination = reactive({
  page: 0,
  totalPages: 0,
  totalElements: 0,
})

const loadAds = async () => {
  loading.value = true
  error.value = ''
  try {
    const { data } = await fetchAds(adsStore.filters)
    ads.value = data.content ?? []
    pagination.page = data.page ?? adsStore.filters.page
    pagination.totalPages = data.totalPages ?? 0
    pagination.totalElements = data.totalElements ?? 0
  } catch (err) {
    error.value = err.response?.data?.message || 'Failed to load ads.'
  } finally {
    loading.value = false
  }
}

const handleFilters = (filters) => {
  adsStore.setFilters(filters)
  loadAds()
}

const handleReset = () => {
  adsStore.resetFilters()
  loadAds()
}

const handlePageChange = (page) => {
  adsStore.setFilter('page', page)
  loadAds()
}

onMounted(loadAds)
</script>

<template>
  <section class="page">
    <div class="page-header">
      <div>
        <h1>DreamAuto Marketplace</h1>
        <p>Browse trusted listings, filter results, and find your next car.</p>
      </div>
      <RouterLink
        v-if="authStore.isAuthenticated"
        class="btn btn-primary"
        :to="{ name: 'ad-create' }"
      >
        + Create Ad
      </RouterLink>
    </div>

    <AdsFilterBar
      :initial-filters="adsStore.filters"
      @apply="handleFilters"
      @reset="handleReset"
    />

    <div class="panel">
      <p v-if="loading">Loading ads...</p>
      <p v-else-if="error" class="error">{{ error }}</p>
      <p v-else-if="ads.length === 0">No ads match your filters yet.</p>

      <div v-else class="card-grid">
        <AdCard v-for="ad in ads" :key="ad.id" :ad="ad" />
      </div>

      <Pagination
        :page="pagination.page"
        :total-pages="pagination.totalPages"
        @change="handlePageChange"
      />
    </div>
  </section>
</template>

<style scoped>
.error {
  color: #b91c1c;
  font-weight: 600;
}
</style>
