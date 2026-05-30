import axios from 'axios';

export function createClient(baseURL) {
    const api = axios.create({ baseURL });

    api.interceptors.request.use((config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    });

    return api;
}
