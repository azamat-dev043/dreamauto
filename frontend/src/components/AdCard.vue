<script setup>
import { computed } from 'vue'

const props = defineProps({
  ad: {
    type: Object,
    required: true,
  },
  showStatus: {
    type: Boolean,
    default: false,
  },
})

const coverPhoto = computed(
  () =>
    props.ad.photoUrls?.[0] ||
    'https://images.unsplash.com/photo-1503736334956-4c8f8e92946d?auto=format&fit=crop&w=900&q=80',
)
</script>

<template>
  <article class="ad-card">
    <div class="ad-card__image">
      <img :src="coverPhoto" :alt="ad.title" />
      <span v-if="showStatus" class="status-pill" :class="ad.active ? 'active' : 'inactive'">
        {{ ad.active ? 'Active' : 'Inactive' }}
      </span>
    </div>
    <div class="ad-card__body">
      <div class="ad-card__header">
        <RouterLink :to="{ name: 'ad-details', params: { id: ad.id } }">
          <h3>{{ ad.title }}</h3>
        </RouterLink>
        <p class="ad-card__subtitle">
          {{ ad.brand }} · {{ ad.model }}
          <span v-if="ad.year">· {{ ad.year }}</span>
        </p>
      </div>
      <p class="ad-card__price">${{ ad.price?.toLocaleString?.() || ad.price }}</p>
      <p class="ad-card__meta">
        <span>{{ ad.city }}</span>
        <span class="favorites">♥ {{ ad.favoriteCount ?? 0 }}</span>
      </p>
      <slot name="actions" />
    </div>
  </article>
</template>

<style scoped>
.ad-card {
  background: #fff;
  border-radius: 1rem;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  box-shadow: 0 15px 40px rgba(15, 23, 42, 0.1);
}

.ad-card__image {
  position: relative;
  width: 100%;
  padding-top: 60%;
  overflow: hidden;
}

.ad-card__image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.ad-card__image .status-pill {
  position: absolute;
  top: 0.75rem;
  left: 0.75rem;
}

.ad-card__body {
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}

.ad-card__header h3 {
  margin: 0;
  font-size: 1.15rem;
  color: #111827;
}

.ad-card__subtitle {
  margin: 0.2rem 0 0;
  color: #6b7280;
  font-size: 0.9rem;
}

.ad-card__price {
  margin: 0.4rem 0;
  font-weight: 700;
  font-size: 1.25rem;
  color: #111827;
}

.ad-card__meta {
  display: flex;
  justify-content: space-between;
  font-size: 0.9rem;
  color: #6b7280;
}

.favorites {
  color: #ef4444;
}
</style>
