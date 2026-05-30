import { useState, useEffect } from 'react';
import {
    Box, Typography, Paper, TextField, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    IconButton, Tooltip, InputAdornment, CircularProgress, Alert, Stack,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import SearchIcon from '@mui/icons-material/Search';
import DeleteIcon from '@mui/icons-material/Delete';
import StorefrontIcon from '@mui/icons-material/Storefront';
import { createProducto, getProducto, deleteProducto } from '../api/catalogo';

function useProductos(searchId) {
    const [productos, setProductos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        let cancelled = false;
        getProducto(searchId || 'all')
            .then((res) => {
                if (!cancelled) setProductos(Array.isArray(res.data) ? res.data : [res.data]);
            })
            .catch(() => { if (!cancelled) setProductos([]); })
            .finally(() => { if (!cancelled) setLoading(false); });
        return () => { cancelled = true; };
    }, [searchId]);

    return { productos, loading, error, setError, setProductos };
}

export default function Productos() {
    const [form, setForm] = useState({ id: '', nombre: '', descripcion: '', precio: '', stock: '' });
    const [searchId, setSearchId] = useState('');
    const { productos, loading, error, setError, setProductos } = useProductos(searchId);

    const reload = async () => {
        try {
            const res = await getProducto(searchId || 'all');
            setProductos(Array.isArray(res.data) ? res.data : [res.data]);
        } catch {
            setProductos([]);
        }
    };

    const handleCreate = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await createProducto({ ...form, precio: parseFloat(form.precio), stock: parseInt(form.stock) });
            setForm({ id: '', nombre: '', descripcion: '', precio: '', stock: '' });
            reload();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al crear');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteProducto(id);
            reload();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al eliminar');
        }
    };

    return (
        <Box>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 3 }}>
                <StorefrontIcon sx={{ fontSize: 32, color: 'primary.main' }} />
                <Typography variant="h4" fontWeight={600}>Catálogo de Productos</Typography>
            </Box>

            {error && <Alert severity="error" sx={{ mb: 2, borderRadius: 3 }} onClose={() => setError('')}>{error}</Alert>}

            <Paper sx={{ p: 3, mb: 3 }}>
                <Typography variant="subtitle1" fontWeight={600} sx={{ mb: 2 }}>Nuevo Producto</Typography>
                <Box component="form" onSubmit={handleCreate} sx={{ display: 'flex', gap: 1.5, flexWrap: 'wrap', alignItems: 'flex-start' }}>
                    <TextField size="small" label="ID" value={form.id} onChange={(e) => setForm({ ...form, id: e.target.value })} required sx={{ minWidth: 100 }} />
                    <TextField size="small" label="Nombre" value={form.nombre} onChange={(e) => setForm({ ...form, nombre: e.target.value })} required sx={{ minWidth: 140 }} />
                    <TextField size="small" label="Descripción" value={form.descripcion} onChange={(e) => setForm({ ...form, descripcion: e.target.value })} sx={{ minWidth: 180 }} />
                    <TextField size="small" label="Precio" type="number" value={form.precio} onChange={(e) => setForm({ ...form, precio: e.target.value })} required sx={{ minWidth: 110 }}
                        slotProps={{ input: { startAdornment: <InputAdornment position="start">$</InputAdornment> } }} />
                    <TextField size="small" label="Stock" type="number" value={form.stock} onChange={(e) => setForm({ ...form, stock: e.target.value })} required sx={{ minWidth: 110 }} />
                    <Button type="submit" variant="contained" startIcon={<AddIcon />} sx={{ borderRadius: 3, height: 40 }}>Crear</Button>
                </Box>
            </Paper>

            <Paper sx={{ p: 3 }}>
                <Stack direction="row" spacing={1} sx={{ mb: 2 }}>
                    <TextField size="small" placeholder="Buscar por ID" value={searchId} onChange={(e) => setSearchId(e.target.value)}
                        slotProps={{ input: { startAdornment: <InputAdornment position="start"><SearchIcon fontSize="small" /></InputAdornment> } }}
                        sx={{ maxWidth: 300 }} />
                    <Button variant="outlined" onClick={reload} startIcon={<SearchIcon />} sx={{ borderRadius: 3 }}>Buscar</Button>
                </Stack>

                {loading ? (
                    <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}><CircularProgress /></Box>
                ) : productos.length === 0 ? (
                    <Typography variant="body2" color="text.secondary" sx={{ textAlign: 'center', py: 4 }}>No se encontraron productos</Typography>
                ) : (
                    <TableContainer>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>ID</TableCell>
                                    <TableCell>Nombre</TableCell>
                                    <TableCell>Precio</TableCell>
                                    <TableCell>Stock</TableCell>
                                    <TableCell align="center">Acciones</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {productos.map((p) => (
                                    <TableRow key={p.id} hover>
                                        <TableCell sx={{ fontWeight: 500 }}>{p.id}</TableCell>
                                        <TableCell>{p.nombre}</TableCell>
                                        <TableCell sx={{ fontWeight: 500 }}>${p.precio}</TableCell>
                                        <TableCell>{p.stock}</TableCell>
                                        <TableCell align="center">
                                            <Tooltip title="Eliminar">
                                                <IconButton color="error" onClick={() => handleDelete(p.id)}><DeleteIcon /></IconButton>
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