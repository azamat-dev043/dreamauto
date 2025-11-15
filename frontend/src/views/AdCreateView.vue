<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import AdForm from '../components/AdForm.vue'
import { addPhotos, createAd } from '../api/adsApi'

const router = useRouter()
const saving = ref(false)
const successMessage = ref('')
const error = ref('')
const createdAdId = ref(null)
const photoInput = ref('')
const photoStatus = ref('')

const handleCreate = async (payload) => {
  error.value = ''
  successMessage.value = ''
  saving.value = true
  try {
    const { data } = await createAd(payload)
    createdAdId.value = data.id
    successMessage.value = 'Ad created successfully. Add photos or continue editing.'
  } catch (err) {
    error.value = err.response?.data?.message || 'Could not create ad.'
  } finally {
    saving.value = false
  }
}

const handleAddPhotos = async () => {
  if (!createdAdId.value) return
  const urls = photoInput.value
    .split('\n')
    .map((line) => line.trim())
    .filter(Boolean)
  if (!urls.length) {
    photoStatus.value = 'Add at least one URL.'
    return
  }
  photoStatus.value = ''
  try {
    await addPhotos(createdAdId.value, urls)
    photoStatus.value = 'Photos attached successfully.'
    photoInput.value = ''
  } catch (err) {
    photoStatus.value = err.response?.data?.message || 'Could not upload photos.'
  }
}

const goToAd = () => {
  if (createdAdId.value) {
    router.push({ name: 'ad-details', params: { id: createdAdId.value } })
  }
}
</script>

<template>
  <section class="page">
    <div class="page-header">
      <div>
        <h1>Create a new listing</h1>
        <p>Share the story, specs, and highlights of your vehicle.</p>
      </div>
    </div>

    <AdForm :loading="saving" submit-label="Publish ad" @submit="handleCreate" />

    <p v-if="error" class="error">{{ error }}</p>
    <p v-if="successMessage" class="success">
      {{ successMessage }}
      <button class="btn-link" type="button" @click="goToAd">Open ad</button>
    </p>

    <div v-if="createdAdId" class="panel photo-uploader">
      <h3>Add photo URLs</h3>
      <p>Paste one URL per line and click “Attach photos”.</p>
      <textarea v-model="photoInput" placeholder="https://img.host/camry/front.jpg"></textarea>
      <div class="form-actions">
        <button class="btn btn-primary" type="button" @click="handleAddPhotos">
          Attach photos
        </button>
      </div>
      <p v-if="photoStatus" class="status">{{ photoStatus }}</p>
    </div>
  </section>
</template>

<style scoped>
.success {
  color: #0f5132;
  font-weight: 600;
  display: flex;
  gap: 0.75rem;
  align-items: center;
}

.error {
  color: #b91c1c;
  font-weight: 600;
}

.photo-uploader textarea {
  width: 100%;
  min-height: 120px;
  padding: 0.75rem;
  border-radius: 0.75rem;
  border: 1px solid #d5d8df;
}

.btn-link {
  background: none;
  border: none;
  color: #2563eb;
  cursor: pointer;
  padding: 0;
}

.status {
  color: #2563eb;
  font-weight: 600;
}
</style>
