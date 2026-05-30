import { createClient } from './client';

const api = createClient(import.meta.env.VITE_API_CATALOGO_URL || 'http://localhost:8080');

export const createProducto = (producto) => api.post('/api/ecommerce/producto/save', producto);
export const getAllProductos = () => api.get('/api/ecommerce/producto/all');
export const getProducto = (id) => api.get(`/api/ecommerce/producto/${id}`);
export const updateProducto = (id, producto) => api.put(`/api/ecommerce/producto/${id}`, producto);
export const deleteProducto = (id) => api.delete(`/api/ecommerce/producto/${id}`);
