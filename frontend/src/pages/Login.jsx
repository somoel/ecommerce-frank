import { useState } from 'react';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import {
    Box, Card, CardContent, TextField, Button, Typography, Alert, Link, InputAdornment, CircularProgress,
} from '@mui/material';
import EmailIcon from '@mui/icons-material/Email';
import LockIcon from '@mui/icons-material/Lock';
import LoginIcon from '@mui/icons-material/Login';
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
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '80vh' }}>
            <Card sx={{ maxWidth: 440, width: '100%', mx: 2 }}>
                <CardContent sx={{ p: 4 }}>
                    <Box sx={{ textAlign: 'center', mb: 3 }}>
                        <LoginIcon sx={{ fontSize: 48, color: 'primary.main', mb: 1 }} />
                        <Typography variant="h5" fontWeight={600}>Bienvenido</Typography>
                        <Typography variant="body2" color="text.secondary">Ingresa a tu cuenta</Typography>
                    </Box>

                    {error && <Alert severity="error" sx={{ mb: 2, borderRadius: 3 }}>{error}</Alert>}

                    <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                        <TextField
                            fullWidth label="Email" type="email" value={email}
                            onChange={(e) => setEmail(e.target.value)} required
                            slotProps={{ input: { startAdornment: <InputAdornment position="start"><EmailIcon fontSize="small" /></InputAdornment> } }}
                        />
                        <TextField
                            fullWidth label="Password" type="password" value={password}
                            onChange={(e) => setPassword(e.target.value)} required
                            slotProps={{ input: { startAdornment: <InputAdornment position="start"><LockIcon fontSize="small" /></InputAdornment> } }}
                        />
                        <Button type="submit" variant="contained" size="large" disabled={loading}
                            startIcon={loading ? <CircularProgress size={20} color="inherit" /> : null}
                            sx={{ mt: 1, py: 1.5, borderRadius: 3 }}>
                            {loading ? 'Ingresando...' : 'Ingresar'}
                        </Button>
                    </Box>

                    <Typography variant="body2" align="center" sx={{ mt: 3 }}>
                        ¿No tienes cuenta?{' '}
                        <Link component={RouterLink} to="/register" fontWeight={600}>
                            Regístrate
                        </Link>
                    </Typography>
                </CardContent>
            </Card>
        </Box>
    );
}