import { lazy, Suspense } from 'react';
import { BrowserRouter, Routes, Route, Link, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { useAuth } from './context/useAuth';

const Login = lazy(() => import('./pages/Login'));
const Register = lazy(() => import('./pages/Register'));
const Productos = lazy(() => import('./pages/Productos'));
const Inventario = lazy(() => import('./pages/Inventario'));
const Ordenes = lazy(() => import('./pages/Ordenes'));

function Navbar() {
    const { isAuthenticated, logout } = useAuth();

    return (
        <nav className="navbar">
            <Link to="/" className="navbar-brand">E-Commerce</Link>
            {isAuthenticated ? (
                <>
                    <Link to="/productos" className="navbar-link">Productos</Link>
                    <Link to="/inventario" className="navbar-link">Inventario</Link>
                    <Link to="/ordenes" className="navbar-link">Órdenes</Link>
                    <button onClick={logout} className="navbar-logout">Salir</button>
                </>
            ) : (
                <>
                    <Link to="/login" className="navbar-link navbar-link-right">Login</Link>
                    <Link to="/register" className="navbar-link">Registrar</Link>
                </>
            )}
        </nav>
    );
}

function ProtectedRoute({ children }) {
    const { isAuthenticated } = useAuth();
    if (!isAuthenticated) return <Navigate to="/login" replace />;
    return children;
}

function Loading() {
    return <div className="loading">Cargando...</div>;
}

function AppRoutes() {
    return (
        <Suspense fallback={<Loading />}>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/productos" element={<ProtectedRoute><Productos /></ProtectedRoute>} />
                <Route path="/inventario" element={<ProtectedRoute><Inventario /></ProtectedRoute>} />
                <Route path="/ordenes" element={<ProtectedRoute><Ordenes /></ProtectedRoute>} />
                <Route path="*" element={<Navigate to="/login" replace />} />
            </Routes>
        </Suspense>
    );
}

export default function App() {
    return (
        <BrowserRouter>
            <AuthProvider>
                <Navbar />
                <AppRoutes />
            </AuthProvider>
        </BrowserRouter>
    );
}
