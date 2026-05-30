import { useState, useEffect } from 'react';
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
                if (!cancelled) {
                    setItems(invRes.data);
                    setAlertas(alertRes.data);
                }
            })
            .catch(() => {
                if (!cancelled) { setItems([]); setAlertas([]); }
            })
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
        <div className="page-container">
            <h2>Inventario</h2>
            {error && <p className="error-text">{error}</p>}

            <form onSubmit={handleRegistrar} className="form-row">
                <input placeholder="Producto ID" value={form.productoId} onChange={(e) => setForm({ ...form, productoId: e.target.value })} required className="form-input" />
                <input placeholder="Nombre" value={form.nombreProducto} onChange={(e) => setForm({ ...form, nombreProducto: e.target.value })} required className="form-input" />
                <input placeholder="Stock Actual" type="number" value={form.stockActual} onChange={(e) => setForm({ ...form, stockActual: e.target.value })} required className="form-input" />
                <input placeholder="Stock Mínimo" type="number" value={form.stockMinimo} onChange={(e) => setForm({ ...form, stockMinimo: e.target.value })} required className="form-input" />
                <button type="submit" className="button">Registrar</button>
            </form>

            <div className="stock-actions">
                <input placeholder="Producto ID" value={stockForm.productoId} onChange={(e) => setStockForm({ ...stockForm, productoId: e.target.value })} className="form-input" />
                <input placeholder="Cantidad" type="number" value={stockForm.cantidad} onChange={(e) => setStockForm({ ...stockForm, cantidad: e.target.value })} className="form-input" />
                <button onClick={handleAumentar} className="button-success">Aumentar</button>
                <button onClick={handleReducir} className="button-warning">Reducir</button>
            </div>

            {alertas.length > 0 && (
                <div className="alert-box">
                    <h3>Alertas de Stock Bajo</h3>
                    {alertas.map((a) => (
                        <p key={a.productoId}>{a.nombreProducto} - Stock: {a.stockActual} (mínimo: {a.stockMinimo})</p>
                    ))}
                </div>
            )}

            {loading ? (
                <p className="loading-text">Cargando inventario...</p>
            ) : (
                <table className="data-table">
                    <thead>
                        <tr>
                            <th>Producto ID</th>
                            <th>Nombre</th>
                            <th>Stock Actual</th>
                            <th>Stock Mínimo</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {items.map((item) => (
                            <tr key={item.productoId}>
                                <td>{item.productoId}</td>
                                <td>{item.nombreProducto}</td>
                                <td>{item.stockActual}</td>
                                <td>{item.stockMinimo}</td>
                                <td className="actions-cell">
                                    <button onClick={() => handleDelete(item.productoId)} className="button-danger">Eliminar</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}
