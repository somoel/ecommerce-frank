import { useState, useCallback } from 'react';
import {
    Box, Typography, Paper, TextField, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    IconButton, Tooltip, InputAdornment, CircularProgress, Alert, Stack, Chip,
} from '@mui/material';
import AddIcon from '@mui/icons-material/AddCircleOutlined';
import SearchIcon from '@mui/icons-material/Search';
import DeleteIcon from '@mui/icons-material/Delete';
import PaidIcon from '@mui/icons-material/Paid';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import CancelIcon from '@mui/icons-material/Cancel';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import ReceiptLongIcon from '@mui/icons-material/ReceiptLong';
import { crearOrden, getOrdenesByUsuario, updateEstado, deleteOrden } from '../api/ordenes';

const estadoColor = {
    PENDIENTE: 'warning',
    PAGADA: 'success',
    ENVIADA: 'info',
    CANCELADA: 'error',
};

export default function Ordenes() {
    const [ordenes, setOrdenes] = useState([]);
    const [cedula, setCedula] = useState('');
    const [form, setForm] = useState({ cedulaUsuario: '', items: [{ productoId: '', cantidad: '', precioUnitario: '' }] });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const loadByCedula = useCallback(async () => {
        if (!cedula) return;
        setLoading(true);
        try {
            const res = await getOrdenesByUsuario(cedula);
            setOrdenes(Array.isArray(res.data) ? res.data : [res.data]);
        } catch {
            setOrdenes([]);
        } finally {
            setLoading(false);
        }
    }, [cedula]);

    const handleCrear = useCallback(async (e) => {
        e.preventDefault();
        setError('');
        try {
            const items = form.items.map((i) => ({
                productoId: i.productoId,
                cantidad: parseInt(i.cantidad),
                precioUnitario: parseFloat(i.precioUnitario)
            }));
            await crearOrden({ cedulaUsuario: form.cedulaUsuario, items });
            setForm({ cedulaUsuario: '', items: [{ productoId: '', cantidad: '', precioUnitario: '' }] });
            if (cedula) loadByCedula();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al crear orden');
        }
    }, [form, cedula, loadByCedula]);

    const handleUpdateEstado = useCallback(async (id, nuevoEstado) => {
        try {
            await updateEstado(id, nuevoEstado);
            loadByCedula();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al actualizar');
        }
    }, [loadByCedula]);

    const handleDelete = useCallback(async (id) => {
        try {
            await deleteOrden(id);
            loadByCedula();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al eliminar');
        }
    }, [loadByCedula]);

    const addItem = useCallback(() => {
        setForm((prev) => ({ ...prev, items: [...prev.items, { productoId: '', cantidad: '', precioUnitario: '' }] }));
    }, []);

    const updateItem = useCallback((index, field, value) => {
        setForm((prev) => ({
            ...prev,
            items: prev.items.map((item, i) => (i === index ? { ...item, [field]: value } : item))
        }));
    }, []);

    return (
        <Box>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 3 }}>
                <ReceiptLongIcon sx={{ fontSize: 32, color: 'primary.main' }} />
                <Typography variant="h4" fontWeight={600}>Órdenes</Typography>
            </Box>

            {error && <Alert severity="error" sx={{ mb: 2, borderRadius: 3 }} onClose={() => setError('')}>{error}</Alert>}

            <Paper sx={{ p: 3, mb: 3 }}>
                <Typography variant="subtitle1" fontWeight={600} sx={{ mb: 2 }}>Crear Orden</Typography>
                <Box component="form" onSubmit={handleCrear}>
                    <TextField fullWidth size="small" label="Cédula Usuario" value={form.cedulaUsuario}
                        onChange={(e) => setForm({ ...form, cedulaUsuario: e.target.value })} required sx={{ mb: 2 }}
                        slotProps={{ input: { startAdornment: <InputAdornment position="start"><SearchIcon fontSize="small" /></InputAdornment> } }} />

                    <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>Items de la orden</Typography>
                    {form.items.map((item, i) => (
                        <Stack key={i} direction="row" spacing={1.5} sx={{ mb: 1.5 }} flexWrap="wrap">
                            <TextField size="small" label="Producto ID" value={item.productoId}
                                onChange={(e) => updateItem(i, 'productoId', e.target.value)} required sx={{ minWidth: 140 }} />
                            <TextField size="small" label="Cantidad" type="number" value={item.cantidad}
                                onChange={(e) => updateItem(i, 'cantidad', e.target.value)} required sx={{ minWidth: 110 }} />
                            <TextField size="small" label="Precio Unitario" type="number" value={item.precioUnitario}
                                onChange={(e) => updateItem(i, 'precioUnitario', e.target.value)} required sx={{ minWidth: 140 }}
                                slotProps={{ input: { startAdornment: <InputAdornment position="start">$</InputAdornment> } }} />
                        </Stack>
                    ))}
                    <Stack direction="row" spacing={1.5} sx={{ mt: 1 }}>
                        <Button variant="outlined" startIcon={<AddIcon />} onClick={addItem} sx={{ borderRadius: 3 }}>Agregar Item</Button>
                        <Button type="submit" variant="contained" startIcon={<ShoppingCartIcon />} sx={{ borderRadius: 3 }}>Crear Orden</Button>
                    </Stack>
                </Box>
            </Paper>

            <Paper sx={{ p: 3 }}>
                <Typography variant="subtitle1" fontWeight={600} sx={{ mb: 2 }}>Buscar Órdenes</Typography>
                <Stack direction="row" spacing={1.5} sx={{ mb: 3 }}>
                    <TextField size="small" placeholder="Buscar por cédula" value={cedula}
                        onChange={(e) => setCedula(e.target.value)} sx={{ maxWidth: 300 }}
                        slotProps={{ input: { startAdornment: <InputAdornment position="start"><SearchIcon fontSize="small" /></InputAdornment> } }} />
                    <Button variant="outlined" onClick={loadByCedula} startIcon={<SearchIcon />} sx={{ borderRadius: 3 }}>Buscar</Button>
                </Stack>

                {loading ? (
                    <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}><CircularProgress /></Box>
                ) : ordenes.length === 0 ? (
                    <Typography variant="body2" color="text.secondary" sx={{ textAlign: 'center', py: 4 }}>
                        Ingresa una cédula para buscar órdenes
                    </Typography>
                ) : (
                    <TableContainer>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>ID</TableCell>
                                    <TableCell>Usuario</TableCell>
                                    <TableCell>Total</TableCell>
                                    <TableCell>Estado</TableCell>
                                    <TableCell align="center">Acciones</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {ordenes.map((o) => (
                                    <TableRow key={o.id} hover>
                                        <TableCell sx={{ fontWeight: 500 }}>{o.id}</TableCell>
                                        <TableCell>{o.cedulaUsuario}</TableCell>
                                        <TableCell sx={{ fontWeight: 500 }}>${o.total}</TableCell>
                                        <TableCell>
                                            <Chip label={o.estado} size="small" color={estadoColor[o.estado] || 'default'} variant="outlined" />
                                        </TableCell>
                                        <TableCell align="center">
                                            <Stack direction="row" spacing={0.5} justifyContent="center" flexWrap="wrap">
                                                <Tooltip title="Pagada"><IconButton size="small" color="success" onClick={() => handleUpdateEstado(o.id, 'PAGADA')}><PaidIcon /></IconButton></Tooltip>
                                                <Tooltip title="Enviada"><IconButton size="small" color="info" onClick={() => handleUpdateEstado(o.id, 'ENVIADA')}><LocalShippingIcon /></IconButton></Tooltip>
                                                <Tooltip title="Cancelar"><IconButton size="small" color="warning" onClick={() => handleUpdateEstado(o.id, 'CANCELADA')}><CancelIcon /></IconButton></Tooltip>
                                                <Tooltip title="Eliminar"><IconButton size="small" color="error" onClick={() => handleDelete(o.id)}><DeleteIcon /></IconButton></Tooltip>
                                            </Stack>
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                )}
            </Paper>
        </Box>
    );
}