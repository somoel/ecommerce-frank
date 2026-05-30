import { useState } from 'react';
import {
    Box, Typography, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    Chip, CircularProgress, FormControl, InputLabel, Select, MenuItem, Stack,
} from '@mui/material';
import NotificationsIcon from '@mui/icons-material/Notifications';
import { useNotifications } from '../context/useNotifications';

const tipoColor = {
    BIENVENIDA: 'success',
    ORDEN_CREADA: 'info',
    ORDEN_PAGADA: 'primary',
    ORDEN_ENVIADA: 'secondary',
    ORDEN_CANCELADA: 'error',
};

const tipoLabels = {
    BIENVENIDA: 'Bienvenida',
    ORDEN_CREADA: 'Orden Creada',
    ORDEN_PAGADA: 'Orden Pagada',
    ORDEN_ENVIADA: 'Orden Enviada',
    ORDEN_CANCELADA: 'Orden Cancelada',
};

export default function Notificaciones() {
    const { notifications, loading } = useNotifications();
    const [filterTipo, setFilterTipo] = useState('TODAS');

    const filtered = filterTipo === 'TODAS'
        ? notifications
        : notifications.filter((n) => n.tipo === filterTipo);

    return (
        <Box>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 3 }}>
                <NotificationsIcon sx={{ fontSize: 32, color: 'primary.main' }} />
                <Typography variant="h4" fontWeight={600}>Notificaciones</Typography>
            </Box>

            <Paper sx={{ p: 3 }}>
                <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mb: 2 }}>
                    <Typography variant="subtitle1" fontWeight={600}>
                        {filtered.length} notificaci&oacute;n{filtered.length !== 1 ? 'es' : ''}
                    </Typography>
                    <FormControl size="small" sx={{ minWidth: 200 }}>
                        <InputLabel>Filtrar por tipo</InputLabel>
                        <Select value={filterTipo} label="Filtrar por tipo" onChange={(e) => setFilterTipo(e.target.value)}>
                            <MenuItem value="TODAS">Todas</MenuItem>
                            {Object.entries(tipoLabels).map(([key, label]) => (
                                <MenuItem key={key} value={key}>{label}</MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </Stack>

                {loading ? (
                    <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}><CircularProgress /></Box>
                ) : filtered.length === 0 ? (
                    <Typography variant="body2" color="text.secondary" sx={{ textAlign: 'center', py: 4 }}>
                        No hay notificaciones
                    </Typography>
                ) : (
                    <TableContainer>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>Tipo</TableCell>
                                    <TableCell>Asunto</TableCell>
                                    <TableCell>Mensaje</TableCell>
                                    <TableCell>Enviada</TableCell>
                                    <TableCell>Fecha</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {filtered.map((n) => (
                                    <TableRow key={n.id} hover>
                                        <TableCell>
                                            <Chip label={tipoLabels[n.tipo] || n.tipo} size="small" color={tipoColor[n.tipo] || 'default'} variant="outlined" />
                                        </TableCell>
                                        <TableCell sx={{ fontWeight: 500 }}>{n.asunto}</TableCell>
                                        <TableCell>{n.mensaje}</TableCell>
                                        <TableCell>
                                            <Chip label={n.enviada ? 'Sí' : 'No'} size="small" color={n.enviada ? 'success' : 'error'} variant="outlined" />
                                        </TableCell>
                                        <TableCell>{n.fechaEnvio ? new Date(n.fechaEnvio).toLocaleString('es-CO') : '—'}</TableCell>
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