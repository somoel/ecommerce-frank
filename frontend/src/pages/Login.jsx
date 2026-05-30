import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../api/auth';

export default function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const res = await login(email, password);
            localStorage.setItem('token', res.data.token);
            localStorage.setItem('usuario', JSON.stringify(res.data));
            navigate('/productos');
        } catch (err) {
            setError(err.response?.data || 'Credenciales invalidas');
        }
    };

    return (
        <div style={{ maxWidth: 400, margin: '60px auto', padding: 20 }}>
            <h2>Login</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: 12 }}>
                    <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required
                        style={{ width: '100%', padding: 8 }} />
                </div>
                <div style={{ marginBottom: 12 }}>
                    <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required
                        style={{ width: '100%', padding: 8 }} />
                </div>
                <button type="submit" style={{ width: '100%', padding: 10 }}>Ingresar</button>
            </form>
            <p style={{ textAlign: 'center', marginTop: 12 }}>
                <a href="/register">Crear cuenta</a>
            </p>
        </div>
    );
}
