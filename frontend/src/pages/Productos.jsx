import { useState, useEffect } from 'react';
import {
    Box, Typography, Paper, TextField, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    IconButton, Tooltip, InputAdornment, CircularProgress, Alert, Stack, Dialog, DialogTitle, DialogContent, DialogActions,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import SearchIcon from '@mui/icons-material/Search';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import StorefrontIcon from '@mui/icons-material/Storefront';
import { createProducto, getAllProductos, getProducto, updateProducto, deleteProducto } from '../api/catalogo';

function useProductos(searchId) {
    const [productos, setProductos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        let cancelled = false;
        const fetchProductos = searchId
            ? getProducto(searchId).then((res) => Array.isArray(res.data) ? res.data : [res.data])
            : getAllProductos().then((res) => res.data);
        fetchProductos
            .then((data) => { if (!cancelled) setProductos(data); })
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

    const [editOpen, setEditOpen] = useState(false);
    const [editForm, setEditForm] = useState({ nombre: '', descripcion: '', precio: '' });
    const [editId, setEditId] = useState('');

    const reload = async () => {
        try {
            if (searchId) {
                const res = await getProducto(searchId);
                setProductos(Array.isArray(res.data) ? res.data : [res.data]);
            } else {
                const res = await getAllProductos();
                setProductos(res.data);
            }
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

    const openEdit = (producto) => {
        setEditId(producto.id);
        setEditForm({ nombre: producto.nombre, descripcion: producto.descripcion || '', precio: producto.precio });
        setEditOpen(true);
    };

    const handleEdit = async () => {
        setError('');
        try {
            await updateProducto(editId, {
                nombre: editForm.nombre,
                descripcion: editForm.descripcion,
                precio: parseFloat(editForm.precio),
            });
            setEditOpen(false);
            reload();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al actualizar');
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
                                            <Stack direction="row" spacing={0.5} justifyContent="center">
                                                <Tooltip title="Editar">
                                                    <IconButton color="primary" onClick={() => openEdit(p)}><EditIcon /></IconButton>
                                                </Tooltip>
                                                <Tooltip title="Eliminar">
                                                    <IconButton color="error" onClick={() => handleDelete(p.id)}><DeleteIcon /></IconButton>
                                                </Tooltip>
                                            </Stack>
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                )}
            </Paper>

            <Dialog open={editOpen} onClose={() => setEditOpen(false)} maxWidth="sm" fullWidth>
                <DialogTitle>Editar Producto</DialogTitle>
                <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: '16px !important' }}>
                    <TextField label="Nombre" value={editForm.nombre} onChange={(e) => setEditForm({ ...editForm, nombre: e.target.value })} fullWidth size="small" />
                    <TextField label="Descripción" value={editForm.descripcion} onChange={(e) => setEditForm({ ...editForm, descripcion: e.target.value })} fullWidth size="small" multiline rows={2} />
                    <TextField label="Precio" type="number" value={editForm.precio} onChange={(e) => setEditForm({ ...editForm, precio: e.target.value })} fullWidth size="small"
                        slotProps={{ input: { startAdornment: <InputAdornment position="start">$</InputAdornment> } }} />
                </DialogContent>
                <DialogActions sx={{ px: 3, pb: 2 }}>
                    <Button onClick={() => setEditOpen(false)} sx={{ borderRadius: 3 }}>Cancelar</Button>
                    <Button variant="contained" onClick={handleEdit} sx={{ borderRadius: 3 }}>Guardar</Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
}
