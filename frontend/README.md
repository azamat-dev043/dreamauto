# DreamAuto Frontend

Vue 3 single-page application for the DreamAuto marketplace backend. The UI covers authentication, public ads feed, ad management for logged-in users, favorites, and the admin moderation dashboard.

## Tech Stack
- Vue 3 + Vite
- Pinia for state management
- Vue Router for SPA navigation
- Axios with interceptors for JWT handling

## Project Structure
```
src/
 ├─ api/               # Axios instance + domain-specific API helpers
 ├─ assets/            # Global styles
 ├─ components/        # Reusable UI building blocks
 ├─ router/            # Route definitions & guards
 ├─ store/             # Pinia stores (auth, filters)
 └─ views/             # Page-level components
```

## Configuration
1. Duplicate `.env.example` → `.env`.
2. Adjust `VITE_API_BASE_URL` if your backend URL differs from `http://localhost:8080`.

## Scripts
- `npm install` – install dependencies.
- `npm run dev` – start the Vite dev server (default http://localhost:5173).
- `npm run build` – create a production build in `dist/`.
- `npm run preview` – locally preview the production build.

## Features
- Registration & login with JWT persistence (localStorage).
- Axios interceptors add `Authorization: Bearer` headers and auto-redirect on 401/403.
- Public ads feed with filtering, sorting, and pagination.
- Ad creation/editing with photo URL uploads for authenticated users.
- Favorites management.
- Admin dashboard for activating/deactivating ads and blocking/unblocking users.
