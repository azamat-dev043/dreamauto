import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import AdDetailsView from '../views/AdDetailsView.vue'
import MyAdsView from '../views/MyAdsView.vue'
import AdCreateView from '../views/AdCreateView.vue'
import AdEditView from '../views/AdEditView.vue'
import FavoritesView from '../views/FavoritesView.vue'
import AdminDashboardView from '../views/AdminDashboardView.vue'
import { useAuthStore } from '../store/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/ads/:id',
      name: 'ad-details',
      component: AdDetailsView,
      props: true,
    },
    {
      path: '/ads/:id/edit',
      name: 'ad-edit',
      component: AdEditView,
      props: true,
      meta: { requiresAuth: true },
    },
    {
      path: '/ads/new',
      name: 'ad-create',
      component: AdCreateView,
      meta: { requiresAuth: true },
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { guestOnly: true },
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
      meta: { guestOnly: true },
    },
    {
      path: '/my-ads',
      name: 'my-ads',
      component: MyAdsView,
      meta: { requiresAuth: true },
    },
    {
      path: '/favorites',
      name: 'favorites',
      component: FavoritesView,
      meta: { requiresAuth: true },
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminDashboardView,
      meta: { requiresAuth: true, requiresAdmin: true },
    },
  ],
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return next({ name: 'login', query: { redirect: to.fullPath } })
  }

  if (to.meta.requiresAdmin && !authStore.isAdmin) {
    return next({ name: 'home' })
  }

  if (to.meta.guestOnly && authStore.isAuthenticated) {
    return next({ name: 'home' })
  }

  return next()
})

export default router
