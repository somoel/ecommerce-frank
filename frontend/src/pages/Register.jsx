import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { register } from '../api/auth';

export default function Register() {
    const [form, setForm] = useState({ cedula: '', nombre: '', telefono: '', email: '', password: '', edad: '', rol: 'CLIENTE' });
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await register(form);
            navigate('/login');
        } catch (err) {
            setError(err.response?.data?.message || 'Error al registrar');
        }
    };

    return (
        <div style={{ maxWidth: 400, margin: '60px auto', padding: 20 }}>
            <h2>Registrarse</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <form onSubmit={handleSubmit}>
                {['cedula', 'nombre', 'telefono', 'email', 'password', 'edad'].map((field) => (
                    <div key={field} style={{ marginBottom: 12 }}>
                        <input type={field === 'password' ? 'password' : field === 'email' ? 'email' : 'text'}
                            name={field} placeholder={field.charAt(0).toUpperCase() + field.slice(1)}
                            value={form[field]} onChange={handleChange} required
                            style={{ width: '100%', padding: 8 }} />
                    </div>
                ))}
                <button type="submit" style={{ width: '100%', padding: 10 }}>Registrar</button>
            </form>
            <p style={{ textAlign: 'center', marginTop: 12 }}>
                <a href="/login">Ya tengo cuenta</a>
            </p>
        </div>
    );
}
