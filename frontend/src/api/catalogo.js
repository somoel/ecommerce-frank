import axios from 'axios';

const API_URL = import.meta.env.VITE_API_CATALOGO_URL || 'http://localhost:8080';

const api = axios.create({ baseURL: API_URL });

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const createProducto = (producto) => api.post('/api/ecommerce/producto/save', producto);
export const getProducto = (id) => api.get(`/api/ecommerce/producto/${id}`);
export const deleteProducto = (id) => api.delete(`/api/ecommerce/producto/${id}`);
