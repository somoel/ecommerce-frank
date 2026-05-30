import { useState, useEffect } from 'react';
import { getAllInventario, getAlertas, registrarInventario, aumentarStock, reducirStock, deleteInventario } from '../api/inventario';

export default function Inventario() {
    const [items, setItems] = useState([]);
    const [alertas, setAlertas] = useState([]);
    const [form, setForm] = useState({ productoId: '', nombreProducto: '', stockActual: '', stockMinimo: '' });
    const [stockForm, setStockForm] = useState({ productoId: '', cantidad: '' });
    const [error, setError] = useState('');

    const loadAll = async () => {
        try {
            const res = await getAllInventario();
            setItems(res.data);
        } catch { setItems([]); }
    };

    const loadAlertas = async () => {
        try {
            const res = await getAlertas();
            setAlertas(res.data);
        } catch { setAlertas([]); }
    };

    const handleRegistrar = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await registrarInventario({ ...form, stockActual: parseInt(form.stockActual), stockMinimo: parseInt(form.stockMinimo) });
            setForm({ productoId: '', nombreProducto: '', stockActual: '', stockMinimo: '' });
            loadAll();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al registrar');
        }
    };

    const handleAumentar = async () => {
        try {
            await aumentarStock(stockForm.productoId, parseInt(stockForm.cantidad));
            setStockForm({ productoId: '', cantidad: '' });
            loadAll();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al aumentar stock');
        }
    };

    const handleReducir = async () => {
        try {
            await reducirStock(stockForm.productoId, parseInt(stockForm.cantidad));
            setStockForm({ productoId: '', cantidad: '' });
            loadAll();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al reducir stock');
        }
    };

    const handleDelete = async (productoId) => {
        try {
            await deleteInventario(productoId);
            loadAll();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al eliminar');
        }
    };

    useEffect(() => { loadAll(); loadAlertas(); }, []);

    return (
        <div style={{ maxWidth: 800, margin: '40px auto', padding: 20 }}>
            <h2>Inventario</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}

            <form onSubmit={handleRegistrar} style={{ marginBottom: 20, display: 'flex', gap: 8, flexWrap: 'wrap' }}>
                <input placeholder="Producto ID" value={form.productoId} onChange={(e) => setForm({ ...form, productoId: e.target.value })} required style={{ padding: 8 }} />
                <input placeholder="Nombre" value={form.nombreProducto} onChange={(e) => setForm({ ...form, nombreProducto: e.target.value })} required style={{ padding: 8 }} />
                <input placeholder="Stock Actual" type="number" value={form.stockActual} onChange={(e) => setForm({ ...form, stockActual: e.target.value })} required style={{ padding: 8 }} />
                <input placeholder="Stock Mínimo" type="number" value={form.stockMinimo} onChange={(e) => setForm({ ...form, stockMinimo: e.target.value })} required style={{ padding: 8 }} />
                <button type="submit" style={{ padding: '8px 16px' }}>Registrar</button>
            </form>

            <div style={{ marginBottom: 20, display: 'flex', gap: 8, alignItems: 'center' }}>
                <input placeholder="Producto ID" value={stockForm.productoId} onChange={(e) => setStockForm({ ...stockForm, productoId: e.target.value })} style={{ padding: 8 }} />
                <input placeholder="Cantidad" type="number" value={stockForm.cantidad} onChange={(e) => setStockForm({ ...stockForm, cantidad: e.target.value })} style={{ padding: 8 }} />
                <button onClick={handleAumentar}>Aumentar</button>
                <button onClick={handleReducir}>Reducir</button>
            </div>

            {alertas.length > 0 && (
                <div style={{ marginBottom: 20, padding: 12, background: '#fff3cd', borderRadius: 4 }}>
                    <h3>Alertas de Stock Bajo</h3>
                    {alertas.map((a) => (
                        <p key={a.productoId} style={{ margin: 4 }}>{a.nombreProducto} - Stock: {a.stockActual} (mínimo: {a.stockMinimo})</p>
                    ))}
                </div>
            )}

            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                    <tr style={{ borderBottom: '2px solid #333' }}>
                        <th style={{ textAlign: 'left', padding: 8 }}>Producto ID</th>
                        <th style={{ textAlign: 'left', padding: 8 }}>Nombre</th>
                        <th style={{ textAlign: 'left', padding: 8 }}>Stock Actual</th>
                        <th style={{ textAlign: 'left', padding: 8 }}>Stock Mínimo</th>
                        <th style={{ padding: 8 }}>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {items.map((item) => (
                        <tr key={item.productoId} style={{ borderBottom: '1px solid #ddd' }}>
                            <td style={{ padding: 8 }}>{item.productoId}</td>
                            <td style={{ padding: 8 }}>{item.nombreProducto}</td>
                            <td style={{ padding: 8 }}>{item.stockActual}</td>
                            <td style={{ padding: 8 }}>{item.stockMinimo}</td>
                            <td style={{ padding: 8, textAlign: 'center' }}>
                                <button onClick={() => handleDelete(item.productoId)} style={{ color: 'red' }}>Eliminar</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
