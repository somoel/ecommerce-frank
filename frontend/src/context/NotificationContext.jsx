import { createContext, useState, useCallback, useEffect, useRef } from 'react';
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
    const ignore = useRef(false);

    useEffect(() => {
        const email = getUserEmail();
        if (!email) return;

        let cancelled = false;
        ignore.current = false;

        (async () => {
            setLoading(true);
            try {
                const res = await getNotificacionesByEmail(email);
                if (!cancelled && !ignore.current) {
                    setNotifications(Array.isArray(res.data) ? res.data : []);
                }
            } catch {
                if (!cancelled && !ignore.current) {
                    setNotifications([]);
                }
            } finally {
                if (!cancelled && !ignore.current) setLoading(false);
            }
        })();

        return () => { cancelled = true; };
    }, []);

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
