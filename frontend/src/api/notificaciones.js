import { createClient } from './client';

const api = createClient(import.meta.env.VITE_API_NOTIFICACIONES_URL || 'http://localhost:6770');

export const enviarNotificacion = (data) => api.post('/api/ecommerce/notificacion/enviar', data);
export const getNotificacionesByEmail = (email) => api.get(`/api/ecommerce/notificacion/usuario/${email}`);