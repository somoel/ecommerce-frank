import { createContext, useState, useCallback } from 'react';
import { getNotificacionesByEmail } from '../api/notificaciones';

const NotificationContext = createContext(null);

const getUserEmail = () => {
    try {
        return JSON.parse(localStorage.getItem('usuario'))?.email || '';
    } catch {
        return '';
    }
};

export function NotificationProvider({ children }) {
    const [notifications, setNotifications] = useState([]);
    const [loading, setLoading] = useState(false);

    const refreshNotifications = useCallback(async () => {
        const email = getUserEmail();
        if (!email) {
            setNotifications([]);
            return;
        }
        setLoading(true);
        try {
            const res = await getNotificacionesByEmail(email);
            setNotifications(Array.isArray(res.data) ? res.data : []);
        } catch {
            setNotifications([]);
        } finally {
            setLoading(false);
        }
    }, []);

    return (
        <NotificationContext.Provider value={{ notifications, notificationCount: notifications.length, loading, refreshNotifications }}>
            {children}
        </NotificationContext.Provider>
    );
}

export { NotificationContext };