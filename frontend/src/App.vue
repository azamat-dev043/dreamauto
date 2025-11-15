<script setup>
import { RouterView } from 'vue-router'
import Navbar from './components/Navbar.vue'

const currentYear = new Date().getFullYear()
</script>

<template>
  <div class="app-shell">
    <Navbar />
    <main class="app-main">
      <RouterView />
    </main>
    <footer class="app-footer">
      <div class="footer-content">
        <p>© {{ currentYear }} DreamAuto. All rights reserved.</p>
        <p>Built with Vue 3, Pinia, and Vite.</p>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.app-shell {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(180deg, #f8fafc 0%, #f1f5f9 20%, #f5f6f8 100%);
}

.app-main {
  flex: 1;
  width: 100%;
}

.app-footer {
  background: #0f172a;
  color: #e2e8f0;
  padding: 1.5rem 1rem;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  gap: 0.5rem;
  font-size: 0.9rem;
}
</style>
