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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class SistemaMembresias0112 
{
    public final double precio_bronce = 200.509;
    public final double precio_plata = 300.509;
    public final double precio_oro = 400.509;
    
    public final int puntos_bronce = 10;
    public final int puntos_plata = 20;
    public final int puntos_oro = 30;

    public double getPrecio_bronce() {
        return precio_bronce;
    }

    public double getPrecio_plata() {
        return precio_plata;
    }

    public double getPrecio_oro() {
        return precio_oro;
    }

    public double getPuntos_bronce() {
        return puntos_bronce;
    }

    public double getPuntos_plata() {
        return puntos_plata;
    }

    public double getPuntos_oro() {
        return puntos_oro;
    }
    
    public List<Membresia> cargarMembresias(List<Membresia> membresias)
    {
        File carpeta = new File("Membresias");
        if (!carpeta.exists() || !carpeta.isDirectory()) 
        {
            System.out.println("La carpeta 'Membresias' no existe o no es un directorio.");
            return null;
        }

        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".dat") || name.endsWith(".ser"));
        if (archivos == null || archivos.length == 0) {
            System.out.println("No hay archivos en la carpeta 'Membresias'.");
            return null;
        }

        Arrays.sort(archivos, Comparator.comparingLong(File::lastModified));

        File ultimoArchivo = archivos[archivos.length - 1];
        System.out.println("Leyendo archivo: " + ultimoArchivo.getName());

        try (FileInputStream fis = new FileInputStream(ultimoArchivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) 
        {

            @SuppressWarnings("unchecked")
            List<Membresia> c = (List<Membresia>) ois.readObject();

            if (membresias != null) 
                membresias.addAll(c);

            return c;

        } catch (FileNotFoundException e) {
            System.out.println("\nNo se encontro el archivo. " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("Error de E/S al leer el archivo. " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Clase Membresia no encontrada: " + e.getMessage());
            return null;
        }
    }
    
    public void limpiarExcepto(String archivoActual) 
    {
        File carpeta = new File("Membresias");

        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".dat") || name.endsWith(".ser"));
        if (archivos == null) return;

        for (File f : archivos) 
        {
            if (!f.getName().equals(archivoActual)) 
                f.delete();
        }
    }
    
    public String serializarMembresia(List<Membresia> membresias)
    {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreArchivo = "membresias_" + timestamp + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(membresias);
        } catch (IOException e) {
            System.out.println("Error al guardar las membresias: " + e.getMessage());
            return null;
        }
        moverArchivo(nombreArchivo, "Membresias/" + nombreArchivo);
        limpiarExcepto(nombreArchivo);
        return nombreArchivo;
    }
    
    public void moverArchivo(String inicio, String fin)
    {
        Path origen = Paths.get(inicio);
        Path destino = Paths.get(fin);
        File carpetaBackup = new File("Membresias");
        if (!carpetaBackup.exists()) 
        {
            boolean creada = carpetaBackup.mkdirs();
            if (creada) {
                System.out.println("Carpeta 'Membresias' creada.");
            } else {
                System.out.println("No se pudo crear la carpeta 'Membresias'.");
            }
        }
        try {
            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("Error al mover el archivo: " + e.getMessage());
        }
    }
    
    public String agregarMembresia(List<Membresia> membresias, int cliente_id, String tipo_membresia)
    {
        LocalDate fecha_final;
        
        if(tipo_membresia.equalsIgnoreCase("Oro"))
            fecha_final = LocalDate.now().plusMonths(6);
        else if(tipo_membresia.equalsIgnoreCase("Plata"))
            fecha_final = LocalDate.now().plusMonths(3);
        else 
            fecha_final = LocalDate.now().plusMonths(1);
           
        Membresia m = new Membresia(cliente_id, tipo_membresia, LocalDate.now(), fecha_final);
        membresias.add(m);
        String nombre_archivo = this.serializarMembresia(membresias);
        return nombre_archivo;
    }
    
    public String agregarMembresia(List<Membresia> membresias, Membresia m)
    {
        membresias.add(m);
        String nombre_archivo = this.serializarMembresia(membresias);
        return nombre_archivo;
    }
    
    public void actualizarMembresia(List<Membresia> membresias, int cliente_id, String tipo_membresia, LocalDate inicio, LocalDate fin)
    {
        Iterator<Membresia> i = membresias.iterator();
        
        while(i.hasNext())
        {
            Membresia siguiente = i.next();
            if(siguiente.getCliente_id()== cliente_id)
            {
                siguiente.setTipo_membresia(tipo_membresia);
                siguiente.setInicio(inicio);
                siguiente.setFin(fin);
                return;
            }
        }
    }
  
    public Membresia buscarMembresia(List<Membresia> membresias, int cliente_id)
    {
        Iterator<Membresia> i = membresias.iterator();
        while(i.hasNext())
        {
            Membresia siguiente = i.next();
            if(siguiente.getCliente_id() == cliente_id)
                return siguiente;
        }
        return null;
    }    
}
