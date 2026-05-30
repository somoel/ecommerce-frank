import axios from 'axios';

const API_URL = import.meta.env.VITE_API_INVENTARIO_URL || 'http://localhost:6771';

const api = axios.create({ baseURL: API_URL });

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const registrarInventario = (inventario) => api.post('/api/ecommerce/inventario/registrar', inventario);
export const getInventario = (productoId) => api.get(`/api/ecommerce/inventario/${productoId}`);
export const getAllInventario = () => api.get('/api/ecommerce/inventario/all');
export const getAlertas = () => api.get('/api/ecommerce/inventario/alertas');
export const aumentarStock = (productoId, cantidad) => api.patch(`/api/ecommerce/inventario/${productoId}/aumentar`, { cantidad });
export const reducirStock = (productoId, cantidad) => api.patch(`/api/ecommerce/inventario/${productoId}/reducir`, { cantidad });
export const deleteInventario = (productoId) => api.delete(`/api/ecommerce/inventario/${productoId}`);
