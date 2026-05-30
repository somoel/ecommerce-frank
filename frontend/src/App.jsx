import { lazy, Suspense } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import CircularLoading from '@mui/material/CircularProgress';
import Box from '@mui/material/Box';
import theme from './theme';
import { AuthProvider } from './context/AuthContext';
import { useAuth } from './context/useAuth';
import { NotificationProvider } from './context/NotificationContext';
import Layout from './components/Layout';

const Login = lazy(() => import('./pages/Login'));
const Register = lazy(() => import('./pages/Register'));
const Productos = lazy(() => import('./pages/Productos'));
const Inventario = lazy(() => import('./pages/Inventario'));
const Ordenes = lazy(() => import('./pages/Ordenes'));
const Notificaciones = lazy(() => import('./pages/Notificaciones'));

function ProtectedRoute({ children }) {
    const { isAuthenticated } = useAuth();
    if (!isAuthenticated) return <Navigate to="/login" replace />;
    return children;
}

function Loading() {
    return (
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '50vh' }}>
            <CircularLoading size={48} />
        </Box>
    );
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
                <Route path="/notificaciones" element={<ProtectedRoute><Notificaciones /></ProtectedRoute>} />
                <Route path="*" element={<Navigate to="/login" replace />} />
            </Routes>
        </Suspense>
    );
}

export default function App() {
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <BrowserRouter>
                <AuthProvider>
                    <NotificationProvider>
                        <Layout>
                            <AppRoutes />
                        </Layout>
                    </NotificationProvider>
                </AuthProvider>
            </BrowserRouter>
        </ThemeProvider>
    );
}