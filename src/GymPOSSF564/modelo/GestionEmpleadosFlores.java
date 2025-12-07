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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GestionEmpleadosFlores
{ 
    public List<Empleado> cargarEmpleado(List<Empleado> empleados)
    {
        File carpeta = new File("Empleados");
        if (!carpeta.exists() || !carpeta.isDirectory()) 
        {
            System.out.println("La carpeta 'Empleado' no existe o no es un directorio.");
            return null;
        }

        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".dat") || name.endsWith(".ser"));
        if (archivos == null || archivos.length == 0) 
        {
            System.out.println("No hay archivos en la carpeta 'Empleado'.");
            return null;
        }
 
        Arrays.sort(archivos, Comparator.comparingLong(File::lastModified));

        File ultimoArchivo = archivos[archivos.length - 1];
        System.out.println("Leyendo archivo: " + ultimoArchivo.getName());

        try (FileInputStream fis = new FileInputStream(ultimoArchivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) 
        {
            @SuppressWarnings("unchecked")
            List<Empleado> c = (List<Empleado>) ois.readObject();

            if (empleados != null) {
                empleados.addAll(c);
            }

            return c;

        } catch (FileNotFoundException e) {
            System.out.println("\nNo se encontro el archivo. " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("Error de E/S al leer el archivo. " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Clase Empleado no encontrada: " + e.getMessage());
            return null;
     
        }
    }
    
    public void limpiarExcepto(String archivoActual) 
    {
        File carpeta = new File("Empleados");

        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".dat") || name.endsWith(".ser"));
        if (archivos == null) return;

        for (File f : archivos) 
        {
            if (!f.getName().equals(archivoActual)) 
                f.delete();
        }
    }
    
    public String serializarEmpleado(List<Empleado> empleados)
    {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreArchivo = "empleado_" + timestamp + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(empleados);
        } catch (IOException e) {
            System.out.println("Error al guardar al empleado: " + e.getMessage());
            return null;
        }
        moverArchivo(nombreArchivo, "Empleados/" + nombreArchivo);
        limpiarExcepto(nombreArchivo);
        return nombreArchivo;
    }
    
    public void moverArchivo(String inicio, String fin)
    {
        Path origen = Paths.get(inicio);
        Path destino = Paths.get(fin);
        File carpetaBackup = new File("Empleados");
        if (!carpetaBackup.exists()) 
        {
            boolean creada = carpetaBackup.mkdirs();
            if (creada) {
                System.out.println("Carpeta 'Empleados' creada.");
            } else {
                System.out.println("No se pudo crear la carpeta 'Empleados'.");
            }
        }
        try {
            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("Error al mover el archivo: " + e.getMessage());
        }
    }
    
    public String agregarEmpleado(List<Empleado> empleados, String nombre, String apellido, int edad, String direccion, int id, String passwrd, String puesto, double salario, boolean acceso)
    {
        Empleado e = new Empleado(nombre, apellido, edad, direccion, id, passwrd, puesto, salario, acceso);
        empleados.add(e);
        String nombre_archivo = this.serializarEmpleado(empleados);
        return nombre_archivo;
    }
    
    public String agregarEmpleado(List<Empleado> empleados, Empleado e)
    {
        empleados.add(e);
        String nombre_archivo = this.serializarEmpleado(empleados);
        return nombre_archivo;
    }
    
    public void actualizarEmpleado(List<Empleado> empleados, String nombre, String apellido, int edad, String direccion, int id, String passwrd, String puesto, double salario, boolean acceso)
    {
        Iterator<Empleado> i = empleados.iterator();
        
        while(i.hasNext())
        {
            Empleado siguiente = i.next();
            if(siguiente.getId()== id){
                siguiente.setNombre(nombre);
                siguiente.setApellido(apellido);
                siguiente.setEdad(edad);
                siguiente.setDireccion(direccion);
                siguiente.setPasswrd(passwrd);
                siguiente.setPuesto(puesto);
                siguiente.setSalario(salario);
                siguiente.setAcceso(acceso);
                return;
            }
        }
    }
    
    public String mostrarEmpleados(List<Empleado> empleados)
    {
        String cadena = "";
        Iterator<Empleado> i = empleados.iterator();
        while(i.hasNext())
        {
            Empleado siguiente = i.next();
            cadena += siguiente.toString() + "\n";
        }
        return cadena;
    }   
}
