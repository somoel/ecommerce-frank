import axios from 'axios';

const API_URL = import.meta.env.VITE_API_AUTH_URL || 'http://localhost:6767';

const api = axios.create({ baseURL: API_URL });

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const login = (email, password) => api.post('/api/ecommerce/usuario/login', { email, password });
export const register = (usuario) => api.post('/api/ecommerce/usuario/save', usuario);
export const getUsuario = (cedula) => api.get(`/api/ecommerce/usuario/${cedula}`);
export const deleteUsuario = (cedula) => api.delete(`/api/ecommerce/usuario/${cedula}`);
