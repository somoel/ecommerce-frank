import { createClient } from './client';

const api = createClient(import.meta.env.VITE_API_INVENTARIO_URL || 'http://localhost:6771');

export const registrarInventario = (inventario) => api.post('/api/ecommerce/inventario/registrar', inventario);
export const getInventario = (productoId) => api.get(`/api/ecommerce/inventario/${productoId}`);
export const getAllInventario = () => api.get('/api/ecommerce/inventario/all');
export const getAlertas = () => api.get('/api/ecommerce/inventario/alertas');
export const aumentarStock = (productoId, cantidad) => api.patch(`/api/ecommerce/inventario/${productoId}/aumentar`, { cantidad });
export const reducirStock = (productoId, cantidad) => api.patch(`/api/ecommerce/inventario/${productoId}/reducir`, { cantidad });
export const deleteInventario = (productoId) => api.delete(`/api/ecommerce/inventario/${productoId}`);
