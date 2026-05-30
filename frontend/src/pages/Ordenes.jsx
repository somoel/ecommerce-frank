import { useState, useCallback } from 'react';
import { crearOrden, getOrdenesByUsuario, updateEstado, deleteOrden } from '../api/ordenes';

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
        <div className="page-container">
            <h2>Órdenes</h2>
            {error && <p className="error-text">{error}</p>}

            <form onSubmit={handleCrear} className="order-form">
                <h3>Crear Orden</h3>
                <input placeholder="Cédula Usuario" value={form.cedulaUsuario}
                    onChange={(e) => setForm({ ...form, cedulaUsuario: e.target.value })}
                    required className="form-input form-input-block" />
                {form.items.map((item, i) => (
                    <div key={i} className="form-row">
                        <input placeholder="Producto ID" value={item.productoId}
                            onChange={(e) => updateItem(i, 'productoId', e.target.value)} required className="form-input" />
                        <input placeholder="Cantidad" type="number" value={item.cantidad}
                            onChange={(e) => updateItem(i, 'cantidad', e.target.value)} required className="form-input" />
                        <input placeholder="Precio Unitario" type="number" step="0.01" value={item.precioUnitario}
                            onChange={(e) => updateItem(i, 'precioUnitario', e.target.value)} required className="form-input" />
                    </div>
                ))}
                <div className="form-row">
                    <button type="button" onClick={addItem} className="button-secondary">+ Agregar Item</button>
                    <button type="submit" className="button">Crear Orden</button>
                </div>
            </form>

            <div className="search-row">
                <input placeholder="Buscar por cédula" value={cedula} onChange={(e) => setCedula(e.target.value)} className="form-input" />
                <button onClick={loadByCedula} className="button">Buscar</button>
            </div>

            {loading ? (
                <p className="loading-text">Cargando órdenes...</p>
            ) : (
                <table className="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Usuario</th>
                            <th>Total</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {ordenes.map((o) => (
                            <tr key={o.id}>
                                <td>{o.id}</td>
                                <td>{o.cedulaUsuario}</td>
                                <td>${o.total}</td>
                                <td>{o.estado}</td>
                                <td className="actions-cell">
                                    <button onClick={() => handleUpdateEstado(o.id, 'PAGADA')} className="button-success">Pagar</button>
                                    <button onClick={() => handleUpdateEstado(o.id, 'ENVIADA')} className="button-secondary">Enviar</button>
                                    <button onClick={() => handleUpdateEstado(o.id, 'CANCELADA')} className="button-danger">Cancelar</button>
                                    <button onClick={() => handleDelete(o.id)} className="button-danger">Eliminar</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}
