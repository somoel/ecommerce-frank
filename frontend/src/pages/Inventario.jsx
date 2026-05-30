import { useState, useEffect } from 'react';
import {
    Box, Typography, Paper, TextField, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    IconButton, Tooltip, InputAdornment, CircularProgress, Alert, Stack, Chip,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import TrendingDownIcon from '@mui/icons-material/TrendingDown';
import WarningAmberIcon from '@mui/icons-material/WarningAmber';
import Inventory2Icon from '@mui/icons-material/Inventory2';
import { getAllInventario, getAlertas, registrarInventario, aumentarStock, reducirStock, deleteInventario } from '../api/inventario';

function useInventario() {
    const [items, setItems] = useState([]);
    const [alertas, setAlertas] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        let cancelled = false;
        Promise.all([getAllInventario(), getAlertas()])
            .then(([invRes, alertRes]) => {
                if (!cancelled) { setItems(invRes.data); setAlertas(alertRes.data); }
            })
            .catch(() => { if (!cancelled) { setItems([]); setAlertas([]); } })
            .finally(() => { if (!cancelled) setLoading(false); });
        return () => { cancelled = true; };
    }, []);

    return { items, alertas, loading, error, setError, setItems, setAlertas };
}

export default function Inventario() {
    const { items, alertas, loading, error, setError, setItems } = useInventario();
    const [form, setForm] = useState({ productoId: '', nombreProducto: '', stockActual: '', stockMinimo: '' });
    const [stockForm, setStockForm] = useState({ productoId: '', cantidad: '' });

    const reload = async () => {
        try {
            const res = await getAllInventario();
            setItems(res.data);
        } catch { setItems([]); }
    };

    const handleRegistrar = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await registrarInventario({ ...form, stockActual: parseInt(form.stockActual), stockMinimo: parseInt(form.stockMinimo) });
            setForm({ productoId: '', nombreProducto: '', stockActual: '', stockMinimo: '' });
            reload();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al registrar');
        }
    };

    const handleAumentar = async () => {
        try {
            await aumentarStock(stockForm.productoId, parseInt(stockForm.cantidad));
            setStockForm({ productoId: '', cantidad: '' });
            reload();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al aumentar stock');
        }
    };

    const handleReducir = async () => {
        try {
            await reducirStock(stockForm.productoId, parseInt(stockForm.cantidad));
            setStockForm({ productoId: '', cantidad: '' });
            reload();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al reducir stock');
        }
    };

    const handleDelete = async (productoId) => {
        try {
            await deleteInventario(productoId);
            reload();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al eliminar');
        }
    };

    return (
        <Box>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 3 }}>
                <Inventory2Icon sx={{ fontSize: 32, color: 'primary.main' }} />
                <Typography variant="h4" fontWeight={600}>Inventario</Typography>
            </Box>

            {error && <Alert severity="error" sx={{ mb: 2, borderRadius: 3 }} onClose={() => setError('')}>{error}</Alert>}

            {alertas.length > 0 && (
                <Alert severity="warning" icon={<WarningAmberIcon />} sx={{ mb: 2, borderRadius: 3 }}>
                    <Typography variant="subtitle2" sx={{ mb: 0.5 }}>Alertas de Stock Bajo</Typography>
                    <Stack spacing={0.5}>
                        {alertas.map((a) => (
                            <Typography key={a.productoId} variant="body2">
                                {a.nombreProducto} — Stock: {a.stockActual} (mínimo: {a.stockMinimo})
                            </Typography>
                        ))}
                    </Stack>
                </Alert>
            )}

            <Paper sx={{ p: 3, mb: 3 }}>
                <Typography variant="subtitle1" fontWeight={600} sx={{ mb: 2 }}>Registrar Inventario</Typography>
                <Box component="form" onSubmit={handleRegistrar} sx={{ display: 'flex', gap: 1.5, flexWrap: 'wrap', alignItems: 'flex-start' }}>
                    <TextField size="small" label="Producto ID" value={form.productoId} onChange={(e) => setForm({ ...form, productoId: e.target.value })} required sx={{ minWidth: 140 }} />
                    <TextField size="small" label="Nombre" value={form.nombreProducto} onChange={(e) => setForm({ ...form, nombreProducto: e.target.value })} required sx={{ minWidth: 180 }} />
                    <TextField size="small" label="Stock Actual" type="number" value={form.stockActual} onChange={(e) => setForm({ ...form, stockActual: e.target.value })} required sx={{ minWidth: 130 }} />
                    <TextField size="small" label="Stock Mínimo" type="number" value={form.stockMinimo} onChange={(e) => setForm({ ...form, stockMinimo: e.target.value })} required sx={{ minWidth: 130 }} />
                    <Button type="submit" variant="contained" startIcon={<AddIcon />} sx={{ borderRadius: 3, height: 40 }}>Registrar</Button>
                </Box>
            </Paper>

            <Paper sx={{ p: 3, mb: 3 }}>
                <Typography variant="subtitle1" fontWeight={600} sx={{ mb: 2 }}>Ajustar Stock</Typography>
                <Stack direction="row" spacing={1.5} alignItems="center" flexWrap="wrap" sx={{ flexWrap: 'wrap' }}>
                    <TextField size="small" label="Producto ID" value={stockForm.productoId} onChange={(e) => setStockForm({ ...stockForm, productoId: e.target.value })} sx={{ minWidth: 140 }} />
                    <TextField size="small" label="Cantidad" type="number" value={stockForm.cantidad} onChange={(e) => setStockForm({ ...stockForm, cantidad: e.target.value })} sx={{ minWidth: 120 }}
                        slotProps={{ input: { startAdornment: <InputAdornment position="start">#</InputAdornment> } }} />
                    <Button variant="contained" color="success" startIcon={<TrendingUpIcon />} onClick={handleAumentar} sx={{ borderRadius: 3, height: 40 }}>Aumentar</Button>
                    <Button variant="contained" color="warning" startIcon={<TrendingDownIcon />} onClick={handleReducir} sx={{ borderRadius: 3, height: 40, color: 'white' }}>Reducir</Button>
                </Stack>
            </Paper>

            <Paper sx={{ p: 3 }}>
                {loading ? (
                    <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}><CircularProgress /></Box>
                ) : items.length === 0 ? (
                    <Typography variant="body2" color="text.secondary" sx={{ textAlign: 'center', py: 4 }}>No hay datos de inventario</Typography>
                ) : (
                    <TableContainer>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>Producto ID</TableCell>
                                    <TableCell>Nombre</TableCell>
                                    <TableCell>Stock Actual</TableCell>
                                    <TableCell>Stock Mínimo</TableCell>
                                    <TableCell align="center">Acciones</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {items.map((item) => (
                                    <TableRow key={item.productoId} hover>
                                        <TableCell sx={{ fontWeight: 500 }}>{item.productoId}</TableCell>
                                        <TableCell>{item.nombreProducto}</TableCell>
                                        <TableCell>
                                            <Chip
                                                label={item.stockActual}
                                                size="small"
                                                color={item.stockActual <= item.stockMinimo ? 'error' : 'success'}
                                                variant={item.stockActual <= item.stockMinimo ? 'filled' : 'outlined'}
                                            />
                                        </TableCell>
                                        <TableCell>{item.stockMinimo}</TableCell>
                                        <TableCell align="center">
                                            <Tooltip title="Eliminar">
                                                <IconButton color="error" onClick={() => handleDelete(item.productoId)}><DeleteIcon /></IconButton>
                                            </Tooltip>
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