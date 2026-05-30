import { useState, useEffect } from 'react';
import { createProducto, getProducto, deleteProducto } from '../api/catalogo';

export default function Productos() {
    const [productos, setProductos] = useState([]);
    const [form, setForm] = useState({ id: '', nombre: '', descripcion: '', precio: '', stock: '' });
    const [searchId, setSearchId] = useState('');
    const [error, setError] = useState('');

    const loadAll = async () => {
        try {
            const res = await getProducto(searchId || 'all');
            setProductos(Array.isArray(res.data) ? res.data : [res.data]);
        } catch { setProductos([]); }
    };

    const handleCreate = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await createProducto({ ...form, precio: parseFloat(form.precio), stock: parseInt(form.stock) });
            setForm({ id: '', nombre: '', descripcion: '', precio: '', stock: '' });
            loadAll();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al crear');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteProducto(id);
            loadAll();
        } catch (err) {
            setError(err.response?.data?.message || 'Error al eliminar');
        }
    };

    useEffect(() => { loadAll(); }, []);

    return (
        <div style={{ maxWidth: 700, margin: '40px auto', padding: 20 }}>
            <h2>Catálogo de Productos</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}

            <form onSubmit={handleCreate} style={{ marginBottom: 20, display: 'flex', gap: 8, flexWrap: 'wrap' }}>
                <input placeholder="ID" value={form.id} onChange={(e) => setForm({ ...form, id: e.target.value })} required style={{ padding: 8 }} />
                <input placeholder="Nombre" value={form.nombre} onChange={(e) => setForm({ ...form, nombre: e.target.value })} required style={{ padding: 8 }} />
                <input placeholder="Descripción" value={form.descripcion} onChange={(e) => setForm({ ...form, descripcion: e.target.value })} style={{ padding: 8 }} />
                <input placeholder="Precio" type="number" value={form.precio} onChange={(e) => setForm({ ...form, precio: e.target.value })} required style={{ padding: 8 }} />
                <input placeholder="Stock" type="number" value={form.stock} onChange={(e) => setForm({ ...form, stock: e.target.value })} required style={{ padding: 8 }} />
                <button type="submit" style={{ padding: '8px 16px' }}>Crear</button>
            </form>

            <div style={{ marginBottom: 12 }}>
                <input placeholder="Buscar por ID" value={searchId} onChange={(e) => setSearchId(e.target.value)} style={{ padding: 8, marginRight: 8 }} />
                <button onClick={loadAll}>Buscar</button>
            </div>

            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                    <tr style={{ borderBottom: '2px solid #333' }}>
                        <th style={{ textAlign: 'left', padding: 8 }}>ID</th>
                        <th style={{ textAlign: 'left', padding: 8 }}>Nombre</th>
                        <th style={{ textAlign: 'left', padding: 8 }}>Precio</th>
                        <th style={{ textAlign: 'left', padding: 8 }}>Stock</th>
                        <th style={{ padding: 8 }}>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {productos.map((p) => (
                        <tr key={p.id} style={{ borderBottom: '1px solid #ddd' }}>
                            <td style={{ padding: 8 }}>{p.id}</td>
                            <td style={{ padding: 8 }}>{p.nombre}</td>
                            <td style={{ padding: 8 }}>${p.precio}</td>
                            <td style={{ padding: 8 }}>{p.stock}</td>
                            <td style={{ padding: 8, textAlign: 'center' }}>
                                <button onClick={() => handleDelete(p.id)} style={{ color: 'red' }}>Eliminar</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
