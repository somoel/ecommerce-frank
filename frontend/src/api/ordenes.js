import axios from 'axios';

const API_URL = import.meta.env.VITE_API_ORDENES_URL || 'http://localhost:6769';

const api = axios.create({ baseURL: API_URL });

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const crearOrden = (orden) => api.post('/api/ecommerce/orden/crear', orden);
export const getOrden = (id) => api.get(`/api/ecommerce/orden/${id}`);
export const getOrdenesByUsuario = (cedula) => api.get(`/api/ecommerce/orden/usuario/${cedula}`);
export const updateEstado = (id, estado) => api.put(`/api/ecommerce/orden/${id}/estado`, { estado });
export const deleteOrden = (id) => api.delete(`/api/ecommerce/orden/${id}`);
