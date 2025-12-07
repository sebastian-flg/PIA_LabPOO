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

public class GestionClientesFlores 
{
    public List<Cliente> cargarClientes(List<Cliente> clientes)
    {
        File carpeta = new File("Clientes");
        if (!carpeta.exists() || !carpeta.isDirectory()) 
        {
            System.out.println("La carpeta 'Clientes' no existe o no es un directorio.");
            return null;
        }

        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".dat") || name.endsWith(".ser"));
        if (archivos == null || archivos.length == 0) 
        {
            System.out.println("No hay archivos en la carpeta 'Clientes'.");
            return null;
        }

        Arrays.sort(archivos, Comparator.comparingLong(File::lastModified));

        File ultimoArchivo = archivos[archivos.length - 1];
        System.out.println("Leyendo archivo: " + ultimoArchivo.getName());

        try (FileInputStream fis = new FileInputStream(ultimoArchivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) 
        {

            @SuppressWarnings("unchecked")
            List<Cliente> c = (List<Cliente>) ois.readObject();

            if (clientes != null) {
                clientes.addAll(c);
            }
            return c;

        } catch (FileNotFoundException e) {
            System.out.println("\nNo se encontro el archivo. " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("Error de E/S al leer el archivo. " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Clase Cliente no encontrada: " + e.getMessage());
            return null;
        }
    }
    
    public void limpiarExcepto(String archivoActual) 
    {
        File carpeta = new File("Clientes");

        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".dat") || name.endsWith(".ser"));
        if (archivos == null) return;

        for (File f : archivos) 
        {
            if (!f.getName().equals(archivoActual)) 
                f.delete();
        }
    }
    
    public String serializarCliente(List<Cliente> clientes)
    {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreArchivo = "cliente_" + timestamp + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(clientes);
        } catch (IOException e) {
            System.out.println("Error al guardar al cliente: " + e.getMessage());
            return null;
        }
        moverArchivo(nombreArchivo, "Clientes/" + nombreArchivo);
        limpiarExcepto(nombreArchivo);
        return nombreArchivo;
    }
    
    public void moverArchivo(String inicio, String fin)
    {
        Path origen = Paths.get(inicio);
        Path destino = Paths.get(fin);
        File carpetaBackup = new File("Clientes");
        if (!carpetaBackup.exists()) {
            boolean creada = carpetaBackup.mkdirs();
            if (creada) {
                System.out.println("Carpeta 'Clientes' creada.");
            } else {
                System.out.println("No se pudo crear la carpeta 'Clientes'.");
            }
        }
        try {
            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("Error al mover el archivo: " + e.getMessage());
        }
    }
    
    public String agregarClientes(List<Cliente> clientes, String nombre, String apellido, String num_telefono)
    {
        Cliente c = new Cliente(nombre, apellido, num_telefono);
        clientes.add(c);
        String nombre_archivo = this.serializarCliente(clientes);
        return nombre_archivo;
    }
    
    public String agregarClientes(List<Cliente> clientes, Cliente c)
    {
        clientes.add(c);
        String nombre_archivo = this.serializarCliente(clientes);
        return nombre_archivo;
    }
    
    public void actualizarCliente(List<Cliente> clientes, int id, String nombre, String apellido, String num_telefono)
    {
        Iterator<Cliente> i = clientes.iterator();
        
        while(i.hasNext())
        {
            Cliente siguiente = i.next();
            if(siguiente.getCliente_id() == id)
            {
                siguiente.setNombres(nombre);
                siguiente.setApellidos(apellido);
                siguiente.setNum_telefono(num_telefono);
                return;
            }
        }
    }
    
    public List<Cliente> buscarCliente(List<Cliente> clientes, String nombre)
    {
        List<Cliente> clientes_find = new ArrayList<>();
        Iterator<Cliente> i = clientes.iterator();
        while(i.hasNext())
        {
            Cliente siguiente = i.next();
            if(siguiente.getNombres().equals(nombre)){
                clientes_find.add(siguiente);
            }
        }
        return clientes_find;
    }
    
    public String mostrarClientes(List<Cliente> clientes)
    {
        String cadena = "";
        Iterator<Cliente> i = clientes.iterator();
        while(i.hasNext())
        {
            Cliente siguiente = i.next();
            cadena += siguiente.toString() + "\n";
        }
        return cadena;
    }    
}
