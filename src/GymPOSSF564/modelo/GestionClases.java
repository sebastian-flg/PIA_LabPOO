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

public class GestionClases 
{
    
    public List<ClaseGrupal> cargarClases(List<ClaseGrupal> clases) 
    {
        File carpeta = new File("Calendario");
        if (!carpeta.exists() || !carpeta.isDirectory()) {
            System.out.println("La carpeta 'Calendario' no existe o no es un directorio.");
            return null;
        }

        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".dat") || name.endsWith(".ser"));
        if (archivos == null || archivos.length == 0) {
            System.out.println("No hay archivos en la carpeta 'Calendario'.");
            return null;
        }

        Arrays.sort(archivos, Comparator.comparingLong(File::lastModified));

        File ultimoArchivo = archivos[archivos.length - 1];
        System.out.println("Leyendo archivo de calendario: " + ultimoArchivo.getName());

        try (FileInputStream fis = new FileInputStream(ultimoArchivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) 
        {

            @SuppressWarnings("unchecked")
            List<ClaseGrupal> c = (List<ClaseGrupal>) ois.readObject();

            if (clases != null) {
                clases.addAll(c);
            }
            return c;

        } catch (FileNotFoundException e) {
            System.out.println("No se encontro el archivo de calendario. " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de calendario. " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Error de clase al leer el archivo de calendario. " + e.getMessage());
            return null;
        }
    }
    
    public void limpiarExcepto(String archivoActual) 
    {
        File carpeta = new File("Calendario");

        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".dat") || name.endsWith(".ser"));
        if (archivos == null) return;

        for (File f : archivos) 
        {
            if (!f.getName().equals(archivoActual)) 
                f.delete();
        }
    }
    
    public String serializarClases(List<ClaseGrupal> clases) 
    {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreArchivo = "calendario_" + timestamp + ".dat";

        try (FileOutputStream fos = new FileOutputStream(nombreArchivo);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) 
        {

            oos.writeObject(new ArrayList<>(clases));
            System.out.println("Clases guardadas en archivo " + nombreArchivo);

        } catch (IOException e) {
            System.out.println("Error al guardar las clases: " + e.getMessage());
            return null;
        }
        
        moverArchivo(nombreArchivo, "Calendario/" + nombreArchivo);
        limpiarExcepto(nombreArchivo);
        return nombreArchivo;
    }
    
    public void moverArchivo(String inicio, String fin)
    {
        Path origen = Paths.get(inicio);
        Path destino = Paths.get(fin);
        File carpetaBackup = new File("Calendario");
        if (!carpetaBackup.exists()) {
            boolean creada = carpetaBackup.mkdirs();
            if (creada) {
                System.out.println("Carpeta 'Calendario' creada.");
            } else {
                System.out.println("No se pudo crear la carpeta 'Calendario'.");
            }
        }
        try {
            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("Error al mover el archivo: " + e.getMessage());
        }
    }
    
    public ClaseGrupal buscarClasePorId(List<ClaseGrupal> clases, int id) 
    {
        if (clases == null) return null;
        for (ClaseGrupal c : clases) {
            if (c.getClase_id() == id) {
                return c;
            }
        }
        return null;
    }
    
    public void eliminarClase(List<ClaseGrupal> clases, int id) 
    {
        if (clases == null) return;
        Iterator<ClaseGrupal> it = clases.iterator();
        while (it.hasNext()) 
        {
            ClaseGrupal c = it.next();
            if (c.getClase_id() == id) {
                it.remove();
                return;
            }
        }
    }
}
