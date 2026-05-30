import { useState, useEffect } from 'react';
import { crearOrden, getOrdenesByUsuario, updateEstado, deleteOrden } from '../api/ordenes';

export default function Ordenes() {
    const [ordenes, setOrdenes] = useState([]);
    const [cedula, setCedula] = useState('');
    const [form, setForm] = useState({ cedulaUsuario: '', items: [{ productoId: '', cantidad: '', precioUnitario: '' }] });
    const [error, setError] = useState('');

    const loadByCedula = async () => {
        if (!cedula) return;
        try {
            const res = await getOrdenesByUsuario(cedula);
            setOrdenes(Array.isArray(res.data) ? res.data : [res.data]);
        } catch { setOrdenes([]); }
    };

    const handleCrear = async (e) => {
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
    };

    const handleUpdateEstado = async (id, nuevoEstado) => {
        try {
            await updateEstado(id, nuevoEstado);
            loadByCedula();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al actualizar');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteOrden(id);
            loadByCedula();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al eliminar');
        }
    };

    const addItem = () => setForm({ ...form, items: [...form.items, { productoId: '', cantidad: '', precioUnitario: '' }] });
    const updateItem = (index, field, value) => {
        const items = [...form.items];
        items[index][field] = value;
        setForm({ ...form, items });
    };

    return (
        <div style={{ maxWidth: 800, margin: '40px auto', padding: 20 }}>
            <h2>Órdenes</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}

            <form onSubmit={handleCrear} style={{ marginBottom: 20, padding: 16, border: '1px solid #ddd', borderRadius: 4 }}>
                <h3>Crear Orden</h3>
                <input placeholder="Cédula Usuario" value={form.cedulaUsuario} onChange={(e) => setForm({ ...form, cedulaUsuario: e.target.value })} required style={{ padding: 8, marginBottom: 8, display: 'block' }} />
                {form.items.map((item, i) => (
                    <div key={i} style={{ display: 'flex', gap: 8, marginBottom: 8 }}>
                        <input placeholder="Producto ID" value={item.productoId} onChange={(e) => updateItem(i, 'productoId', e.target.value)} required style={{ padding: 8 }} />
                        <input placeholder="Cantidad" type="number" value={item.cantidad} onChange={(e) => updateItem(i, 'cantidad', e.target.value)} required style={{ padding: 8 }} />
                        <input placeholder="Precio Unitario" type="number" step="0.01" value={item.precioUnitario} onChange={(e) => updateItem(i, 'precioUnitario', e.target.value)} required style={{ padding: 8 }} />
                    </div>
                ))}
                <button type="button" onClick={addItem} style={{ marginRight: 8 }}>+ Agregar Item</button>
                <button type="submit">Crear Orden</button>
            </form>

            <div style={{ marginBottom: 12, display: 'flex', gap: 8 }}>
                <input placeholder="Buscar por cédula" value={cedula} onChange={(e) => setCedula(e.target.value)} style={{ padding: 8 }} />
                <button onClick={loadByCedula}>Buscar</button>
            </div>

            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                    <tr style={{ borderBottom: '2px solid #333' }}>
                        <th style={{ textAlign: 'left', padding: 8 }}>ID</th>
                        <th style={{ textAlign: 'left', padding: 8 }}>Usuario</th>
                        <th style={{ textAlign: 'left', padding: 8 }}>Total</th>
                        <th style={{ textAlign: 'left', padding: 8 }}>Estado</th>
                        <th style={{ padding: 8 }}>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {ordenes.map((o) => (
                        <tr key={o.id} style={{ borderBottom: '1px solid #ddd' }}>
                            <td style={{ padding: 8 }}>{o.id}</td>
                            <td style={{ padding: 8 }}>{o.cedulaUsuario}</td>
                            <td style={{ padding: 8 }}>${o.total}</td>
                            <td style={{ padding: 8 }}>{o.estado}</td>
                            <td style={{ padding: 8, textAlign: 'center', display: 'flex', gap: 4, justifyContent: 'center' }}>
                                <button onClick={() => handleUpdateEstado(o.id, 'PAGADA')}>Pagar</button>
                                <button onClick={() => handleUpdateEstado(o.id, 'ENVIADA')}>Enviar</button>
                                <button onClick={() => handleUpdateEstado(o.id, 'CANCELADA')} style={{ color: 'red' }}>Cancelar</button>
                                <button onClick={() => handleDelete(o.id)} style={{ color: 'red' }}>Eliminar</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
