package GymPOSSF564.modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class ControlVentasProductosFlores 
{
    public void registrarVenta(int cliente_id, String cliente_nombre, List<ProductoVendido> productos_vendidos) 
    {
        GestionInventarioFlores gestor = new GestionInventarioFlores();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreArchivo = "venta_" + timestamp + ".txt";

        List<Inventario> productos = new ArrayList<>();
        double total = 0;
        
        try (FileWriter fw = new FileWriter(nombreArchivo, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) 
        {
            LocalDateTime ahora = LocalDateTime.now();
            
            out.printf("VENTA | Fecha: %s | Cliente: %s | FechaHora: %s\n",
                    new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                    cliente_nombre,
                    ahora);

            for (ProductoVendido pv : productos_vendidos) 
            {
                Inventario p = pv.getProducto();
                int cantidad_vendida = pv.getCantidadVendida();
                int stock = p.getCantidad();
                  
                out.printf("ID: %d | Nombre: %s | Categoria: %s | Cantidad vendida: %d | Precio: %.2f\n",
                p.getProducto_id(),
                p.getNombre(),
                p.getCategoria(),
                cantidad_vendida,
                p.getPrecioUnitario());
                
                p.setCantidad(stock - cantidad_vendida);
                total += cantidad_vendida * p.getPrecioUnitario();
                productos.add(p);
            }
            
            gestor.actualizarInventarioArchivo(productos);
            out.printf("\nTotal: %.2f\n", total);

        } catch (IOException e) {
            System.out.println("Error al guardar la venta: " + e.getMessage());
        }

        moverArchivo(nombreArchivo, "Ventas/" + nombreArchivo, "Ventas");
    }

    private void moverArchivo(String inicio, String fin, String carpeta)
    {
        Path origen = Paths.get(inicio);
        Path destino = Paths.get(fin);
        File carpetaBackup = new File(carpeta);

        if (!carpetaBackup.exists()) 
        {
            boolean creada = carpetaBackup.mkdirs();
            if (creada) 
                System.out.println("Carpeta '" + carpeta + "' creada.");
            else 
                System.out.println("No se pudo crear la carpeta '" + carpeta + "'.");
        }

        try {
            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("Error al mover el archivo: " + e.getMessage());
        }
    }
}