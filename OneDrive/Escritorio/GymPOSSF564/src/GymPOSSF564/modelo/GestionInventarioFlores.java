package GymPOSSF564.modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class GestionInventarioFlores 
{
    public List<Inventario> cargarInventario(List<Inventario> inventario)
    {
        File carpeta = new File("Inventario");
        if (!carpeta.exists() || !carpeta.isDirectory()) {
            System.out.println("La carpeta 'Inventario' no existe o no es un directorio.");
            return null;
        }

        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".dat") || name.endsWith(".ser"));
        if (archivos == null || archivos.length == 0) 
        {
            System.out.println("No hay archivos en la carpeta 'Inventario'.");
            return null;
        }

        Arrays.sort(archivos, Comparator.comparingLong(File::lastModified));

        File ultimoArchivo = archivos[archivos.length - 1];
        System.out.println("Leyendo archivo: " + ultimoArchivo.getName());

        try (FileInputStream fis = new FileInputStream(ultimoArchivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) 
        {

            @SuppressWarnings("unchecked")
            List<Inventario> inv = (List<Inventario>) ois.readObject();

            if (inventario != null) {
                inventario.addAll(inv);
            }
            return inv;

        } catch (FileNotFoundException e) {
            System.out.println("\nNo se encontro el archivo. " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("Error de E/S al leer el archivo. " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Clase Inventario no encontrada: " + e.getMessage());
            return null;
        }
    }
    
    public void limpiarExcepto(String archivoActual) 
    {
        File carpeta = new File("Inventario");

        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".dat") || name.endsWith(".ser"));
        if (archivos == null) return;

        for (File f : archivos) 
        {
            if (!f.getName().equals(archivoActual)) 
                f.delete();
        }
    }
    
    public String serializarInventario(List<Inventario> inventario)
    {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreArchivo = "inventario_" + timestamp + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(inventario);
        } catch (IOException e) {
            System.out.println("Error al guardar inventario: " + e.getMessage());
            return null;
        }
        moverArchivo(nombreArchivo, "Inventario/" + nombreArchivo);
        limpiarExcepto(nombreArchivo);
        return nombreArchivo;
    }
    
    public void moverArchivo(String inicio, String fin)
    {
        Path origen = Paths.get(inicio);
        Path destino = Paths.get(fin);
        File carpetaBackup = new File("Inventario");
        if (!carpetaBackup.exists()) {
            boolean creada = carpetaBackup.mkdirs();
            if (creada) {
                System.out.println("Carpeta 'Inventario' creada.");
            } else {
                System.out.println("No se pudo crear la carpeta 'Inventario'.");
            }
        }
        try {
            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("Error al mover el archivo: " + e.getMessage());
        }
    }
    
    public String agregarInventario(List<Inventario> inventario, String nombre, String descripcion, String categoria, int cantidad, double precioUnitario)
    {
        Inventario item = new Inventario(nombre, descripcion, categoria, cantidad, precioUnitario);
        inventario.add(item);
        return this.serializarInventario(inventario);
    }
    
    public String agregarInventario(List<Inventario> inventario, Inventario item)
    {
        inventario.add(item);
        return this.serializarInventario(inventario);
    }
    
    public void actualizarInventario(List<Inventario> inventario, int id, String nombre, String descripcion, String categoria, int cantidad, double precioUnitario)
    { 
        Iterator<Inventario> it = inventario.iterator();
        
        while(it.hasNext())
        {
            Inventario prod = it.next();
            if(prod.getProducto_id() == id)
            {
                prod.setNombre(nombre);
                prod.setDescripcion(descripcion);
                prod.setCategoria(categoria);
                prod.setCantidad(cantidad);
                prod.setPrecioUnitario(precioUnitario);
                return;
            }
        }
    }
    
    public void actualizarInventarioArchivo(List<Inventario> productos)
    {
        List<Inventario> inventario = new ArrayList<>();
        List<Inventario> inventarioFinal = new ArrayList<>();
        
        cargarInventario(inventario);
        
        Map<Integer, Inventario> mapaActualizados = new HashMap<>();
        for (Inventario p : productos) {
            mapaActualizados.put(p.getProducto_id(), p);
        }
        
        for (Inventario prod : inventario) 
        {
            if (mapaActualizados.containsKey(prod.getProducto_id())) {
                inventarioFinal.add(mapaActualizados.get(prod.getProducto_id()));
            }
            else {
                inventarioFinal.add(prod);
            }
        }   
        
        serializarInventario(inventarioFinal);
    }

    public List<Inventario> buscarProducto(List<Inventario> inventario, String nombre)
    {
        List<Inventario> encontrados = new ArrayList<>();
        for (Inventario i : inventario) 
        {
            if (i.getNombre().equalsIgnoreCase(nombre)) {
                encontrados.add(i);
            }
        }
        return encontrados;
    }

    public String mostrarInventario(List<Inventario> inventario)
    {
        StringBuilder sb = new StringBuilder();
        for (Inventario p : inventario) {
            sb.append(p.toString()).append("\n----------------\n");
        }
        return sb.toString();
    }
}