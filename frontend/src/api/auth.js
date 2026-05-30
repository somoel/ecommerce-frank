import { createClient } from './client';

const api = createClient(import.meta.env.VITE_API_AUTH_URL || 'http://localhost:6767');

export const login = (email, password) => api.post('/api/ecommerce/usuario/login', { email, password });
export const register = (usuario) => api.post('/api/ecommerce/usuario/save', usuario);
export const getUsuario = (cedula) => api.get(`/api/ecommerce/usuario/${cedula}`);
export const deleteUsuario = (cedula) => api.delete(`/api/ecommerce/usuario/${cedula}`);
