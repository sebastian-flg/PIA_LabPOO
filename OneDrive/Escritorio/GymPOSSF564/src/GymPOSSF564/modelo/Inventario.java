package GymPOSSF564.modelo;

import java.io.Serializable;
import java.util.List;

public class Inventario implements Serializable 
{
    private static int contador_id = 1;
    private int producto_id;
    private String nombre;
    private String descripcion;
    private String categoria;
    private int cantidad;
    private double precioUnitario;

    public Inventario() {
    }
    
    public Inventario(String nombre, String descripcion, String categoria, int cantidad, double precioUnitario) 
    {
        this.producto_id = contador_id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;

        System.out.println("ID en Inventario: " + this.producto_id);
    }

    public static synchronized void incrementarContador() {
        contador_id++;
    }

    public static void eliminarContadorProductoNoUsado() {
        contador_id--;
    }

    public static void inicializarContador(List<Inventario> productos) 
    {
        if (!productos.isEmpty()) 
        {
            int max_id = productos.stream()
                    .mapToInt(Inventario::getProducto_id)
                    .max()
                    .orElse(0);

            contador_id = max_id + 1;
        }
    }

    public int getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(int producto_id) {
        this.producto_id = producto_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public static int getContador_id() {
        return contador_id;
    }

    @Override
    public String toString() 
    {
        return "ID Producto: " + producto_id +
                "\nNombre: " + nombre +
                "\nDescripcion: " + descripcion +
                "\nCategoria: " + categoria +
                "\nCantidad: " + cantidad +
                "\nPrecio Unitario: " + precioUnitario;
    }
}