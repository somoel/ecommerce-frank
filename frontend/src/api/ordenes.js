import { createClient } from './client';

const api = createClient(import.meta.env.VITE_API_ORDENES_URL || 'http://localhost:6769');

export const crearOrden = (orden) => api.post('/api/ecommerce/orden/crear', orden);
export const getOrden = (id) => api.get(`/api/ecommerce/orden/${id}`);
export const getOrdenesByUsuario = (cedula) => api.get(`/api/ecommerce/orden/usuario/${cedula}`);
export const updateEstado = (id, estado) => api.put(`/api/ecommerce/orden/${id}/estado`, { estado });
export const deleteOrden = (id) => api.delete(`/api/ecommerce/orden/${id}`);
