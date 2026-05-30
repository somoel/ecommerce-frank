package com.chamber.ecommerce.catalogo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Producto {
    private String id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;

    public Producto(String id, String nombre, String descripcion, Object precio, Integer stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = castPrecio(precio);
        this.stock = stock;
    }

    public void setPrecio(Object precio) {
        this.precio = castPrecio(precio);
    }

    public static Double castPrecio(Object precio) {
        if (precio == null) {
            return null;
        }

        if (precio instanceof Double valorDouble) {
            return valorDouble;
        }

        if (precio instanceof Integer valorInteger) {
            return valorInteger.doubleValue();
        }

        if (precio instanceof String valorString) {
            String limpio = valorString.trim().replace(",", ".");
            return Double.parseDouble(limpio);
        }

        throw new IllegalArgumentException("Tipo de precio no soportado: " + precio.getClass().getSimpleName());
    }
}
