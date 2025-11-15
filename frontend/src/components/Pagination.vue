<script setup>
import { computed } from 'vue'

const props = defineProps({
  page: {
    type: Number,
    default: 0,
  },
  totalPages: {
    type: Number,
    default: 0,
  },
})

const emit = defineEmits(['change'])

const pagesToShow = computed(() => {
  const total = props.totalPages
  if (total <= 1) return []
  const maxVisible = 5
  const start = Math.max(0, props.page - Math.floor(maxVisible / 2))
  const end = Math.min(total - 1, start + maxVisible - 1)
  const items = []
  for (let i = start; i <= end; i += 1) {
    items.push(i)
  }
  return items
})

const goTo = (page) => {
  if (page < 0 || page >= props.totalPages || page === props.page) return
  emit('change', page)
}
</script>

<template>
  <div v-if="totalPages > 1" class="pagination">
    <button class="btn btn-secondary" :disabled="page === 0" @click="goTo(page - 1)">
      Prev
    </button>

    <button
      v-for="pageNumber in pagesToShow"
      :key="pageNumber"
      class="page-number"
      :class="{ active: pageNumber === page }"
      @click="goTo(pageNumber)"
    >
      {{ pageNumber + 1 }}
    </button>

    <button
      class="btn btn-secondary"
      :disabled="page >= totalPages - 1"
      @click="goTo(page + 1)"
    >
      Next
    </button>
  </div>
</template>

<style scoped>
.pagination {
  margin-top: 1.5rem;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 0.5rem;
  justify-content: center;
}

.page-number {
  border: none;
  border-radius: 0.5rem;
  padding: 0.5rem 0.85rem;
  background: #e2e8f0;
  cursor: pointer;
  font-weight: 600;
}

.page-number.active {
  background: #2563eb;
  color: #fff;
}
</style>
