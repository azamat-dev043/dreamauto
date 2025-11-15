<script setup>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import AdForm from '../components/AdForm.vue'
import { addPhotos, fetchAdById, updateAd } from '../api/adsApi'

const route = useRoute()

const ad = ref(null)
const loading = ref(true)
const saving = ref(false)
const error = ref('')
const successMessage = ref('')
const photoInput = ref('')
const photoStatus = ref('')

const loadAd = async (id) => {
  loading.value = true
  error.value = ''
  try {
    const { data } = await fetchAdById(id)
    ad.value = data
  } catch (err) {
    error.value = err.response?.data?.message || 'Could not load ad.'
  } finally {
    loading.value = false
  }
}

const handleUpdate = async (payload) => {
  if (!ad.value) return
  saving.value = true
  successMessage.value = ''
  error.value = ''
  try {
    await updateAd(ad.value.id, payload)
    successMessage.value = 'Changes saved successfully.'
    await loadAd(ad.value.id)
  } catch (err) {
    error.value = err.response?.data?.message || 'Unable to update ad.'
  } finally {
    saving.value = false
  }
}

const handlePhotoUpload = async () => {
  if (!ad.value) return
  const urls = photoInput.value
    .split('\n')
    .map((line) => line.trim())
    .filter(Boolean)
  if (!urls.length) {
    photoStatus.value = 'Provide at least one URL.'
    return
  }
  photoStatus.value = ''
  try {
    await addPhotos(ad.value.id, urls)
    photoStatus.value = 'Photos added.'
    photoInput.value = ''
    await loadAd(ad.value.id)
  } catch (err) {
    photoStatus.value = err.response?.data?.message || 'Failed to attach photos.'
  }
}

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
    <div class="page-header">
      <div>
        <h1>Edit ad</h1>
        <p>Keep your listing up to date for better visibility.</p>
      </div>
    </div>

    <div class="panel" v-if="loading">Loading ad...</div>
    <div v-else-if="error" class="panel error">{{ error }}</div>
    <template v-else-if="ad">
      <AdForm :initial-data="ad" :loading="saving" submit-label="Save changes" @submit="handleUpdate" />
      <p v-if="successMessage" class="success">{{ successMessage }}</p>

      <div class="panel photo-panel">
        <h3>Current photos</h3>
        <div class="gallery">
          <img v-for="(photo, index) in ad.photoUrls ?? []" :key="index" :src="photo" :alt="`Photo ${index + 1}`" />
        </div>
        <textarea
          v-model="photoInput"
          placeholder="Append new photo URLs, one per line"
        ></textarea>
        <div class="form-actions">
          <button class="btn btn-primary" type="button" @click="handlePhotoUpload">
            Add photos
          </button>
        </div>
        <p v-if="photoStatus" class="status">{{ photoStatus }}</p>
      </div>
    </template>
  </section>
</template>

<style scoped>
.error {
  color: #b91c1c;
  font-weight: 600;
}

.success {
  color: #0f5132;
  font-weight: 600;
}

.photo-panel textarea {
  width: 100%;
  min-height: 120px;
  padding: 0.75rem;
  border-radius: 0.75rem;
  border: 1px solid #e2e8f0;
}

.gallery {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 1rem;
  margin: 1rem 0;
}

.gallery img {
  width: 100%;
  height: 120px;
  object-fit: cover;
  border-radius: 0.75rem;
}

.status {
  color: #2563eb;
  font-weight: 600;
}
</style>
