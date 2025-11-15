<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import {
  activateAd,
  blockUser,
  deactivateAd,
  fetchAdminAds,
  unblockUser,
} from '../api/adminApi'
import AdCard from '../components/AdCard.vue'
import Pagination from '../components/Pagination.vue'

const ads = ref([])
const loading = ref(false)
const error = ref('')
const filters = reactive({
  page: 0,
  includeInactive: true,
})
const totalPages = ref(0)
const showInactiveOnly = ref(false)

const userIdInput = ref('')
const userActionStatus = ref('')

const loadAdminAds = async () => {
  loading.value = true
  error.value = ''
  try {
    const { data } = await fetchAdminAds({
      page: filters.page,
      includeInactive: filters.includeInactive,
    })
    ads.value = data.content ?? []
    totalPages.value = data.totalPages ?? 0
  } catch (err) {
    error.value = err.response?.data?.message || 'Failed to load admin feed.'
  } finally {
    loading.value = false
  }
}

const filteredAds = computed(() => {
  if (!showInactiveOnly.value) return ads.value
  return ads.value.filter((ad) => !ad.active)
})

const toggleAdState = async (ad) => {
  try {
    if (ad.active) {
      await deactivateAd(ad.id)
    } else {
      await activateAd(ad.id)
    }
    await loadAdminAds()
  } catch (err) {
    error.value = err.response?.data?.message || 'Unable to change ad status.'
  }
}

const handleUserAction = async (action) => {
  if (!userIdInput.value) {
    userActionStatus.value = 'Enter user ID first.'
    return
  }
  userActionStatus.value = ''
  try {
    if (action === 'block') {
      await blockUser(userIdInput.value)
      userActionStatus.value = `User ${userIdInput.value} blocked.`
    } else {
      await unblockUser(userIdInput.value)
      userActionStatus.value = `User ${userIdInput.value} unblocked.`
    }
    userIdInput.value = ''
  } catch (err) {
    userActionStatus.value = err.response?.data?.message || 'Could not update user.'
  }
}

const handlePageChange = (page) => {
  filters.page = page
  loadAdminAds()
}

onMounted(loadAdminAds)
</script>

<template>
  <section class="page admin-page">
    <div class="page-header">
      <div>
        <h1>Admin dashboard</h1>
        <p>Moderate ads, handle escalations, and manage users.</p>
      </div>
    </div>

    <div class="panel user-actions">
      <h3>User moderation</h3>
      <div class="form-grid">
        <div class="form-control">
          <label for="userId">User ID</label>
          <input id="userId" v-model="userIdInput" placeholder="Enter user ID" />
        </div>
      </div>
      <div class="actions">
        <button class="btn btn-danger" type="button" @click="handleUserAction('block')">
          Block user
        </button>
        <button class="btn btn-secondary" type="button" @click="handleUserAction('unblock')">
          Unblock user
        </button>
      </div>
      <p v-if="userActionStatus" class="status">{{ userActionStatus }}</p>
    </div>

    <div class="panel">
      <div class="admin-controls">
        <label class="toggle">
          <input v-model="showInactiveOnly" type="checkbox" />
          Show only inactive ads
        </label>
        <button class="btn btn-secondary" type="button" @click="loadAdminAds">
          Refresh
        </button>
      </div>

      <p v-if="loading">Loading ads...</p>
      <p v-else-if="error" class="error">{{ error }}</p>
      <p v-else-if="filteredAds.length === 0">No ads to moderate.</p>

      <div v-else class="card-grid">
        <AdCard
          v-for="ad in filteredAds"
          :key="ad.id"
          :ad="ad"
          :show-status="true"
        >
          <template #actions>
            <button class="btn btn-secondary" type="button" @click="toggleAdState(ad)">
              {{ ad.active ? 'Deactivate' : 'Activate' }}
            </button>
          </template>
        </AdCard>
      </div>

      <Pagination :page="filters.page" :total-pages="totalPages" @change="handlePageChange" />
    </div>
  </section>
</template>

<style scoped>
.admin-page .panel + .panel {
  margin-top: 1.5rem;
}

.user-actions .actions {
  display: flex;
  gap: 0.75rem;
  margin-top: 1rem;
}

.admin-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.toggle {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.error {
  color: #b91c1c;
}

.status {
  color: #2563eb;
  font-weight: 600;
}
</style>
