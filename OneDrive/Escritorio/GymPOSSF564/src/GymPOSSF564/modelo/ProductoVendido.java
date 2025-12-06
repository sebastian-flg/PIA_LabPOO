package GymPOSSF564.modelo;

public class ProductoVendido 
{
    private Inventario producto;
    private int cantidad_vendida;

    public ProductoVendido(Inventario producto, int cantidad_vendida) 
    {
        this.producto = producto;
        this.cantidad_vendida = cantidad_vendida;
    }

    public Inventario getProducto() {
        return producto;
    }

    public int getCantidadVendida() {
        return cantidad_vendida;
    }
}
