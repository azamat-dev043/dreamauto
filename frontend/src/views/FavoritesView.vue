<script setup>
import { onMounted, reactive, ref } from 'vue'
import AdCard from '../components/AdCard.vue'
import Pagination from '../components/Pagination.vue'
import { listFavorites, removeFavorite } from '../api/favoritesApi'

const favorites = ref([])
const loading = ref(false)
const error = ref('')

const pagination = reactive({
  page: 0,
  totalPages: 0,
  size: 12,
})

const loadFavorites = async () => {
  loading.value = true
  error.value = ''
  try {
    const { data } = await listFavorites({
      page: pagination.page,
      size: pagination.size,
    })
    favorites.value = data.content ?? []
    pagination.totalPages = data.totalPages ?? 0
  } catch (err) {
    error.value = err.response?.data?.message || 'Failed to load favorites.'
  } finally {
    loading.value = false
  }
}

const handleRemove = async (adId) => {
  try {
    await removeFavorite(adId)
    loadFavorites()
  } catch (err) {
    error.value = err.response?.data?.message || 'Unable to remove favorite.'
  }
}

const handlePageChange = (page) => {
  pagination.page = page
  loadFavorites()
}

onMounted(loadFavorites)
</script>

<template>
  <section class="page">
    <div class="page-header">
      <div>
        <h1>Favorites</h1>
        <p>Your curated shortlist of cars you love.</p>
      </div>
    </div>

    <div class="panel">
      <p v-if="loading">Loading favorites...</p>
      <p v-else-if="error" class="error">{{ error }}</p>
      <p v-else-if="favorites.length === 0">You have no favorite ads yet.</p>

      <div v-else class="card-grid">
        <AdCard
          v-for="favorite in favorites"
          :key="favorite.id"
          :ad="{
            id: favorite.carAdId,
            title: favorite.title,
            brand: favorite.brand,
            model: favorite.model,
            year: favorite.year ?? '',
            price: favorite.price,
            city: favorite.city ?? '—',
            photoUrls: favorite.photoUrls,
            favoriteCount: 0,
            active: true,
          }"
        >
          <template #actions>
            <button class="btn btn-danger" type="button" @click="handleRemove(favorite.carAdId)">
              Remove
            </button>
          </template>
        </AdCard>
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
}
</style>
