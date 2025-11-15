<script setup>
import { reactive, watch } from 'vue'

const props = defineProps({
  initialData: {
    type: Object,
    default: () => ({}),
  },
  loading: {
    type: Boolean,
    default: false,
  },
  submitLabel: {
    type: String,
    default: 'Save ad',
  },
})

const emit = defineEmits(['submit'])

const defaults = {
  title: '',
  description: '',
  brand: '',
  model: '',
  year: '',
  price: '',
  mileage: '',
  bodyType: '',
  engineType: '',
  transmission: '',
  driveType: '',
  city: '',
}

const form = reactive({ ...defaults })

const syncForm = (data = {}) => {
  Object.assign(form, defaults, data)
}

watch(
  () => props.initialData,
  (value) => {
    syncForm(value)
  },
  { immediate: true },
)

const submit = () => {
  const payload = {
    ...form,
    year: form.year ? Number(form.year) : null,
    price: form.price ? Number(form.price) : null,
    mileage: form.mileage ? Number(form.mileage) : null,
  }
  emit('submit', payload)
}
</script>

<template>
  <form class="panel ad-form" @submit.prevent="submit">
    <div class="form-grid">
      <div class="form-control">
        <label for="title">Title</label>
        <input id="title" v-model="form.title" required placeholder="Toyota Camry 70" />
      </div>
      <div class="form-control">
        <label for="brand">Brand</label>
        <input id="brand" v-model="form.brand" required placeholder="Toyota" />
      </div>
      <div class="form-control">
        <label for="model">Model</label>
        <input id="model" v-model="form.model" required placeholder="Camry" />
      </div>
      <div class="form-control">
        <label for="city">City</label>
        <input id="city" v-model="form.city" required placeholder="Astana" />
      </div>
      <div class="form-control">
        <label for="year">Year</label>
        <input id="year" v-model.number="form.year" type="number" min="1980" max="2100" required />
      </div>
      <div class="form-control">
        <label for="price">Price (USD)</label>
        <input id="price" v-model.number="form.price" type="number" min="0" required />
      </div>
      <div class="form-control">
        <label for="mileage">Mileage (km)</label>
        <input id="mileage" v-model.number="form.mileage" type="number" min="0" />
      </div>
      <div class="form-control">
        <label for="bodyType">Body type</label>
        <select id="bodyType" v-model="form.bodyType">
          <option value="">Select</option>
          <option value="SEDAN">Sedan</option>
          <option value="SUV">SUV</option>
          <option value="HATCHBACK">Hatchback</option>
          <option value="COUPE">Coupe</option>
          <option value="WAGON">Wagon</option>
        </select>
      </div>
      <div class="form-control">
        <label for="engineType">Engine type</label>
        <select id="engineType" v-model="form.engineType">
          <option value="">Select</option>
          <option value="GASOLINE">Gasoline</option>
          <option value="DIESEL">Diesel</option>
          <option value="HYBRID">Hybrid</option>
          <option value="ELECTRIC">Electric</option>
        </select>
      </div>
      <div class="form-control">
        <label for="transmission">Transmission</label>
        <select id="transmission" v-model="form.transmission">
          <option value="">Select</option>
          <option value="AUTOMATIC">Automatic</option>
          <option value="MANUAL">Manual</option>
        </select>
      </div>
      <div class="form-control">
        <label for="driveType">Drive type</label>
        <select id="driveType" v-model="form.driveType">
          <option value="">Select</option>
          <option value="FWD">FWD</option>
          <option value="RWD">RWD</option>
          <option value="AWD">AWD</option>
        </select>
      </div>
    </div>

    <div class="form-control">
      <label for="description">Description</label>
      <textarea
        id="description"
        v-model="form.description"
        placeholder="Highlight maintenance history, ownership, and condition."
      />
    </div>

    <div class="form-actions">
      <button class="btn btn-primary" type="submit" :disabled="loading">
        {{ loading ? 'Saving…' : submitLabel }}
      </button>
    </div>
  </form>
</template>

<style scoped>
.ad-form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
}
</style>
