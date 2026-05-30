import { createContext, useState, useCallback } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [token, setToken] = useState(() => localStorage.getItem('token'));

    const login = useCallback((newToken, usuario) => {
        localStorage.setItem('token', newToken);
        localStorage.setItem('usuario', JSON.stringify(usuario));
        setToken(newToken);
    }, []);

    const logout = useCallback(() => {
        localStorage.removeItem('token');
        localStorage.removeItem('usuario');
        setToken(null);
    }, []);

    return (
        <AuthContext.Provider value={{ token, isAuthenticated: !!token, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export { AuthContext };
