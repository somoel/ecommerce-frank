import { useState } from 'react';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import {
    Box, Card, CardContent, TextField, Button, Typography, Alert, Link, InputAdornment, CircularProgress, Grid,
} from '@mui/material';
import PersonIcon from '@mui/icons-material/Person';
import BadgeIcon from '@mui/icons-material/Badge';
import PhoneIcon from '@mui/icons-material/Phone';
import EmailIcon from '@mui/icons-material/Email';
import LockIcon from '@mui/icons-material/Lock';
import NumbersIcon from '@mui/icons-material/Numbers';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import { register } from '../api/auth';
import { enviarNotificacion } from '../api/notificaciones';

const fields = [
    { name: 'cedula', label: 'Cédula', icon: <BadgeIcon fontSize="small" />, type: 'text' },
    { name: 'nombre', label: 'Nombre', icon: <PersonIcon fontSize="small" />, type: 'text' },
    { name: 'telefono', label: 'Teléfono', icon: <PhoneIcon fontSize="small" />, type: 'text' },
    { name: 'email', label: 'Email', icon: <EmailIcon fontSize="small" />, type: 'email' },
    { name: 'password', label: 'Password', icon: <LockIcon fontSize="small" />, type: 'password' },
    { name: 'edad', label: 'Edad', icon: <NumbersIcon fontSize="small" />, type: 'text' },
];

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
            enviarNotificacion({
                destinatarioEmail: form.email,
                tipo: 'BIENVENIDA',
                asunto: 'Bienvenido a E-Commerce',
                mensaje: `Hola ${form.nombre}, tu cuenta ha sido creada exitosamente.`,
            }).catch(() => { /* fire-and-forget */ });
            navigate('/login');
        } catch (err) {
            setError(err.response?.data?.message || 'Error al registrar');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '80vh' }}>
            <Card sx={{ maxWidth: 520, width: '100%', mx: 2 }}>
                <CardContent sx={{ p: 4 }}>
                    <Box sx={{ textAlign: 'center', mb: 3 }}>
                        <PersonAddIcon sx={{ fontSize: 48, color: 'primary.main', mb: 1 }} />
                        <Typography variant="h5" fontWeight={600}>Crear Cuenta</Typography>
                        <Typography variant="body2" color="text.secondary">Completa tus datos</Typography>
                    </Box>

                    {error && <Alert severity="error" sx={{ mb: 2, borderRadius: 3 }}>{error}</Alert>}

                    <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                        <Grid container spacing={2}>
                            {fields.map((field) => (
                                <Grid item xs={12} sm={6} key={field.name}>
                                    <TextField
                                        fullWidth
                                        name={field.name}
                                        label={field.label}
                                        type={field.type}
                                        value={form[field.name]}
                                        onChange={handleChange}
                                        required
                                        slotProps={{ input: { startAdornment: <InputAdornment position="start">{field.icon}</InputAdornment> } }}
                                    />
                                </Grid>
                            ))}
                        </Grid>
                        <Button type="submit" variant="contained" size="large" disabled={loading}
                            startIcon={loading ? <CircularProgress size={20} color="inherit" /> : <PersonAddIcon />}
                            sx={{ mt: 1, py: 1.5, borderRadius: 3 }}>
                            {loading ? 'Registrando...' : 'Registrar'}
                        </Button>
                    </Box>

                    <Typography variant="body2" align="center" sx={{ mt: 3 }}>
                        ¿Ya tienes cuenta?{' '}
                        <Link component={RouterLink} to="/login" fontWeight={600}>
                            Inicia sesión
                        </Link>
                    </Typography>
                </CardContent>
            </Card>
        </Box>
    );
}