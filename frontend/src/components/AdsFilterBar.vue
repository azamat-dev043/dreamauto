<script setup>
import { reactive, watch } from 'vue'

const props = defineProps({
  initialFilters: {
    type: Object,
    required: true,
  },
})

const emit = defineEmits(['apply', 'reset'])

const localFilters = reactive({ ...props.initialFilters })

watch(
  () => props.initialFilters,
  (newValue) => {
    Object.assign(localFilters, newValue)
  },
  { deep: true },
)

const submit = () => {
  emit('apply', { ...localFilters, page: 0 })
}

const reset = () => {
  emit('reset')
}
</script>

<template>
  <form class="filter-bar panel" @submit.prevent="submit">
    <div class="form-grid">
      <div class="form-control">
        <label for="brand">Brand</label>
        <input id="brand" v-model="localFilters.brand" placeholder="e.g. Toyota" />
      </div>
      <div class="form-control">
        <label for="model">Model</label>
        <input id="model" v-model="localFilters.model" placeholder="e.g. Camry" />
      </div>
      <div class="form-control">
        <label for="city">City</label>
        <input id="city" v-model="localFilters.city" placeholder="e.g. Astana" />
      </div>
      <div class="form-control">
        <label for="min-price">Min price</label>
        <input
          id="min-price"
          v-model.number="localFilters.minPrice"
          type="number"
          min="0"
          placeholder="10 000"
        />
      </div>
      <div class="form-control">
        <label for="max-price">Max price</label>
        <input
          id="max-price"
          v-model.number="localFilters.maxPrice"
          type="number"
          min="0"
          placeholder="40 000"
        />
      </div>
      <div class="form-control">
        <label for="year-from">Year from</label>
        <input
          id="year-from"
          v-model.number="localFilters.yearFrom"
          type="number"
          min="1980"
          max="2100"
        />
      </div>
      <div class="form-control">
        <label for="year-to">Year to</label>
        <input
          id="year-to"
          v-model.number="localFilters.yearTo"
          type="number"
          min="1980"
          max="2100"
        />
      </div>
      <div class="form-control">
        <label for="sort-by">Sort by</label>
        <select id="sort-by" v-model="localFilters.sortBy">
          <option value="createdAt">Newest</option>
          <option value="price">Price</option>
          <option value="year">Year</option>
        </select>
      </div>
      <div class="form-control">
        <label for="sort-dir">Sort direction</label>
        <select id="sort-dir" v-model="localFilters.sortDirection">
          <option value="desc">Descending</option>
          <option value="asc">Ascending</option>
        </select>
      </div>
    </div>
    <div class="filter-bar__actions">
      <button type="button" class="btn btn-secondary" @click="reset">
        Reset
      </button>
      <button type="submit" class="btn btn-primary">Apply filters</button>
    </div>
  </form>
</template>

<style scoped>
.filter-bar {
  margin-bottom: 1.5rem;
}

.filter-bar__actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  margin-top: 1rem;
}

@media (max-width: 600px) {
  .filter-bar__actions {
    flex-direction: column;
  }
}
</style>
