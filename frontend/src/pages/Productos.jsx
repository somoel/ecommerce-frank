import { useState, useEffect } from 'react';
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
        <div className="page-container">
            <h2>Catálogo de Productos</h2>
            {error && <p className="error-text">{error}</p>}

            <form onSubmit={handleCreate} className="form-row">
                <input placeholder="ID" value={form.id} onChange={(e) => setForm({ ...form, id: e.target.value })} required className="form-input" />
                <input placeholder="Nombre" value={form.nombre} onChange={(e) => setForm({ ...form, nombre: e.target.value })} required className="form-input" />
                <input placeholder="Descripción" value={form.descripcion} onChange={(e) => setForm({ ...form, descripcion: e.target.value })} className="form-input" />
                <input placeholder="Precio" type="number" value={form.precio} onChange={(e) => setForm({ ...form, precio: e.target.value })} required className="form-input" />
                <input placeholder="Stock" type="number" value={form.stock} onChange={(e) => setForm({ ...form, stock: e.target.value })} required className="form-input" />
                <button type="submit" className="button">Crear</button>
            </form>

            <div className="search-row">
                <input placeholder="Buscar por ID" value={searchId} onChange={(e) => setSearchId(e.target.value)} className="form-input" />
                <button onClick={reload} className="button">Buscar</button>
            </div>

            {loading ? (
                <p className="loading-text">Cargando productos...</p>
            ) : (
                <table className="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Precio</th>
                            <th>Stock</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {productos.map((p) => (
                            <tr key={p.id}>
                                <td>{p.id}</td>
                                <td>{p.nombre}</td>
                                <td>${p.precio}</td>
                                <td>{p.stock}</td>
                                <td className="actions-cell">
                                    <button onClick={() => handleDelete(p.id)} className="button-danger">Eliminar</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}
