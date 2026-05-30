import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/useAuth';
import { login } from '../api/auth';

export default function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const { login: authLogin } = useAuth();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try {
            const res = await login(email, password);
            authLogin(res.data.token, res.data);
            navigate('/productos');
        } catch (err) {
            setError(err.response?.data || 'Credenciales invalidas');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="form-page">
            <h2>Login</h2>
            {error && <p className="error-text">{error}</p>}
            <form onSubmit={handleSubmit}>
                <div className="form-field">
                    <input type="email" placeholder="Email" value={email}
                        onChange={(e) => setEmail(e.target.value)} required className="form-input" />
                </div>
                <div className="form-field">
                    <input type="password" placeholder="Password" value={password}
                        onChange={(e) => setPassword(e.target.value)} required className="form-input" />
                </div>
                <button type="submit" className="form-button" disabled={loading}>
                    {loading ? 'Ingresando...' : 'Ingresar'}
                </button>
            </form>
            <p className="form-footer">
                <Link to="/register">Crear cuenta</Link>
            </p>
        </div>
    );
}
