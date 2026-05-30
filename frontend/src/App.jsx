import { BrowserRouter, Routes, Route, Link, useNavigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import Productos from './pages/Productos';
import Inventario from './pages/Inventario';
import Ordenes from './pages/Ordenes';

function Navbar() {
    const navigate = useNavigate();
    const token = localStorage.getItem('token');

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('usuario');
        navigate('/login');
    };

    return (
        <nav style={{ display: 'flex', gap: 16, padding: '12px 20px', background: '#1a1a2e', color: '#fff', alignItems: 'center' }}>
            <span style={{ fontWeight: 'bold', fontSize: 18 }}>E-Commerce</span>
            {token ? (
                <>
                    <Link to="/productos" style={{ color: '#fff', textDecoration: 'none' }}>Productos</Link>
                    <Link to="/inventario" style={{ color: '#fff', textDecoration: 'none' }}>Inventario</Link>
                    <Link to="/ordenes" style={{ color: '#fff', textDecoration: 'none' }}>Órdenes</Link>
                    <button onClick={logout} style={{ marginLeft: 'auto', padding: '6px 12px', cursor: 'pointer' }}>Salir</button>
                </>
            ) : (
                <>
                    <Link to="/login" style={{ color: '#fff', textDecoration: 'none', marginLeft: 'auto' }}>Login</Link>
                    <Link to="/register" style={{ color: '#fff', textDecoration: 'none' }}>Registrar</Link>
                </>
            )}
        </nav>
    );
}

function App() {
    return (
        <BrowserRouter>
            <Navbar />
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/productos" element={<Productos />} />
                <Route path="/inventario" element={<Inventario />} />
                <Route path="/ordenes" element={<Ordenes />} />
                <Route path="*" element={<Login />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
