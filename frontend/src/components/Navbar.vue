<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'

const authStore = useAuthStore()
const router = useRouter()
const menuOpen = ref(false)

const navLinks = computed(() => {
  const links = [
    { label: 'Home', to: { name: 'home' } },
  ]

  if (authStore.isAuthenticated) {
    links.push({ label: 'My Ads', to: { name: 'my-ads' } })
    links.push({ label: 'Favorites', to: { name: 'favorites' } })
  }

  if (authStore.isAdmin) {
    links.push({ label: 'Admin', to: { name: 'admin' } })
  }

  return links
})

const toggleMenu = () => {
  menuOpen.value = !menuOpen.value
}

const handleLogout = () => {
  authStore.logout()
  menuOpen.value = false
  router.push({ name: 'home' })
}
</script>

<template>
  <header class="navbar">
    <div class="navbar__inner">
      <RouterLink class="brand" :to="{ name: 'home' }" @click="menuOpen = false">
        <span class="brand__logo">DreamAuto</span>
        <span class="brand__tagline">Car marketplace</span>
      </RouterLink>

      <nav :class="['nav-links', { 'nav-links--open': menuOpen }]">
        <RouterLink
          v-for="link in navLinks"
          :key="link.label"
          class="nav-link"
          active-class="nav-link--active"
          :to="link.to"
          @click="menuOpen = false"
        >
          {{ link.label }}
        </RouterLink>
      </nav>

      <div class="navbar__actions">
        <template v-if="authStore.isAuthenticated">
          <span class="user-pill">Hello, {{ authStore.userName }}</span>
          <button class="btn btn-secondary" @click="handleLogout">
            Logout
          </button>
        </template>
        <template v-else>
          <RouterLink class="btn btn-secondary" :to="{ name: 'login' }" @click="menuOpen = false">
            Login
          </RouterLink>
          <RouterLink class="btn btn-primary" :to="{ name: 'register' }" @click="menuOpen = false">
            Register
          </RouterLink>
        </template>
      </div>

      <button class="menu-toggle" @click="toggleMenu">
        ☰
      </button>
    </div>
  </header>
</template>

<style scoped>
.navbar {
  position: sticky;
  top: 0;
  z-index: 10;
  background: rgba(15, 23, 42, 0.9);
  backdrop-filter: blur(10px);
  color: #e2e8f0;
  padding: 0.75rem 1.25rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.navbar__inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.brand {
  display: flex;
  flex-direction: column;
  line-height: 1.1;
}

.brand__logo {
  font-weight: 700;
  font-size: 1.25rem;
  color: #fff;
}

.brand__tagline {
  font-size: 0.75rem;
  color: #cbd5f5;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-left: 2rem;
}

.nav-link {
  color: #cbd5f5;
  font-weight: 500;
  padding-bottom: 0.15rem;
  border-bottom: 2px solid transparent;
}

.nav-link--active {
  color: #fff;
  border-color: #38bdf8;
}

.navbar__actions {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.user-pill {
  background: rgba(255, 255, 255, 0.1);
  padding: 0.3rem 0.75rem;
  border-radius: 999px;
  font-size: 0.85rem;
}

.menu-toggle {
  display: none;
  background: transparent;
  border: none;
  font-size: 1.5rem;
  color: #fff;
  cursor: pointer;
}

@media (max-width: 900px) {
  .navbar__inner {
    flex-wrap: wrap;
  }
  .nav-links {
    display: none;
    width: 100%;
    order: 3;
    flex-direction: column;
    align-items: flex-start;
    margin-left: 0;
    padding-top: 0.5rem;
  }
  .nav-links--open {
    display: flex;
  }
  .navbar__actions {
    order: 2;
    margin-left: 0;
  }
  .menu-toggle {
    display: block;
    margin-left: auto;
  }
}
</style>
