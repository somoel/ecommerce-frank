import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { register } from '../api/auth';

export default function Register() {
    const [form, setForm] = useState({ cedula: '', nombre: '', telefono: '', email: '', password: '', edad: '', rol: 'CLIENTE' });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try {
            await register(form);
            navigate('/login');
        } catch (err) {
            setError(err.response?.data?.message || 'Error al registrar');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="form-page">
            <h2>Registrarse</h2>
            {error && <p className="error-text">{error}</p>}
            <form onSubmit={handleSubmit}>
                {['cedula', 'nombre', 'telefono', 'email', 'password', 'edad'].map((field) => (
                    <div key={field} className="form-field">
                        <input
                            type={field === 'password' ? 'password' : field === 'email' ? 'email' : 'text'}
                            name={field}
                            placeholder={field.charAt(0).toUpperCase() + field.slice(1)}
                            value={form[field]}
                            onChange={handleChange}
                            required
                            className="form-input"
                        />
                    </div>
                ))}
                <button type="submit" className="form-button" disabled={loading}>
                    {loading ? 'Registrando...' : 'Registrar'}
                </button>
            </form>
            <p className="form-footer">
                <Link to="/login">Ya tengo cuenta</Link>
            </p>
        </div>
    );
}
