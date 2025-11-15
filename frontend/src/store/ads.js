import { defineStore } from 'pinia'
import { reactive } from 'vue'

const defaultFilters = {
  page: 0,
  size: 9,
  brand: '',
  model: '',
  city: '',
  minPrice: '',
  maxPrice: '',
  yearFrom: '',
  yearTo: '',
  sortBy: 'createdAt',
  sortDirection: 'desc',
}

export const useAdsStore = defineStore('ads', () => {
  const filters = reactive({ ...defaultFilters })

  const setFilter = (key, value) => {
    filters[key] = value
  }

  const setFilters = (newFilters) => {
    Object.assign(filters, newFilters)
  }

  const resetFilters = () => {
    Object.assign(filters, defaultFilters)
  }

  return {
    filters,
    defaultFilters,
    setFilter,
    setFilters,
    resetFilters,
  }
})
