<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { fetchMyAds } from '../api/adsApi'
import { useAuthStore } from '../store/auth'
import AdCard from '../components/AdCard.vue'

const authStore = useAuthStore()
const router = useRouter()
const ads = ref([])
const loading = ref(false)
const error = ref('')

const loadMyAds = async () => {
  loading.value = true
  error.value = ''
  try {
    const { data } = await fetchMyAds()
    ads.value = data.content ?? data ?? []
  } catch (err) {
    error.value = err.response?.data?.message || 'Failed to load your ads.'
  } finally {
    loading.value = false
  }
}

const goToCreate = () => {
  router.push({ name: 'ad-create' })
}

onMounted(() => {
  if (!authStore.isAuthenticated) {
    router.push({ name: 'login', query: { redirect: '/my-ads' } })
    return
  }
  loadMyAds()
})
</script>

<template>
  <section class="page">
    <div class="page-header">
      <div>
        <h1>My ads</h1>
        <p>Manage your listings, keep them fresh, and review their status.</p>
      </div>
      <button class="btn btn-primary" @click="goToCreate">+ New ad</button>
    </div>

    <div class="panel">
      <p v-if="loading">Loading your ads...</p>
      <p v-else-if="error" class="error">{{ error }}</p>
      <p v-else-if="ads.length === 0">You have not published any listings yet.</p>

      <div v-else class="card-grid">
        <AdCard v-for="ad in ads" :key="ad.id" :ad="ad" :show-status="true">
          <template #actions>
            <RouterLink class="btn btn-secondary" :to="{ name: 'ad-edit', params: { id: ad.id } }">
              Edit
            </RouterLink>
          </template>
        </AdCard>
      </div>
    </div>
  </section>
</template>

<style scoped>
.error {
  color: #b91c1c;
}
</style>
