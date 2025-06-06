import axios from 'axios';

// Correct backend URL (port 8088)
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8088';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 5000,
});

// Request interceptor
api.interceptors.request.use((config) => {
  const userId = localStorage.getItem('userId');
  if (userId) {
    config.headers['User-Id'] = userId;
  }
  return config;
});

// Response interceptor for error handling
api.interceptors.response.use(
    (response) => response,
    (error) => {
      console.error('API Error:', error);
      return Promise.reject(error);
    }
);

// Test API
export const testApi = {
  healthCheck: () => api.get('/actuator/health'),
  info: () => api.get('/actuator/info'),
};

// Pet API
export const petApi = {
  getAllPets: (ownerId) => api.get(`/api/pets/owner/${ownerId}`),
  getPet: (petId) => api.get(`/api/pets/${petId}`),
  createPet: (petData) => api.post('/api/pets', petData),
  feedPet: (petId, foodType = 'regular food') => api.post(`/api/pets/${petId}/feed?foodType=${foodType}`),
  playWithPet: (petId) => api.post(`/api/pets/${petId}/play`),
};

// User API
export const userApi = {
  getUser: (userId) => api.get(`/api/users/${userId}`),
  createUser: (userData) => api.post('/api/users/register', userData),
  getUserByUsername: (username) => api.get(`/api/users/username/${username}`),
};

// Game API
export const gameApi = {
  getPetTypes: () => api.get('/api/game/pet-types'),
  getDashboard: (userId) => api.get(`/api/game/dashboard/${userId}`),
  getRecommendations: (userId) => api.get(`/api/game/recommendations/${userId}`),
};

export default api;