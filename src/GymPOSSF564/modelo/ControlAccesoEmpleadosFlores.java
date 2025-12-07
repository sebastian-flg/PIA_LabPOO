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

public class ControlAccesoEmpleadosFlores
{
    public void registrarEntrada(int id, String nombre, String apellido) 
    {
        String timestamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String nombreArchivo = "entrada_empleados_" + timestamp + ".txt";
        Acceso ae = new Acceso(id, nombre, apellido);
        
        try (FileWriter fw = new FileWriter(nombreArchivo, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            ae.setFecha_entrada(LocalDateTime.now());
            out.printf("ID del empleado: " + ae.getId() + ", Nombre: " + ae.getNombre() + ", Apellidos: " + ae.getApellidos() + ", Hora de entrada: " + ae.getFecha_entrada());
        } catch (IOException e) {
            System.out.println("Error al guardar al empleado: " + e.getMessage());
        }
        moverArchivo(nombreArchivo, "AccesoEmpleados/Entrada/" + nombreArchivo, "Entrada");
    }
    
    public void registrarSalida(int id, String nombre, String apellido) 
    {
        String timestamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String nombreArchivo = "salida_empleados_" + timestamp + ".txt";
        Acceso ae = new Acceso(id, nombre, apellido);
        
        try (FileWriter fw = new FileWriter(nombreArchivo, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            ae.setFecha_salida(LocalDateTime.now());
            out.printf("ID del empleado: " + ae.getId() + ", Nombre: " + ae.getNombre() + ", Apellidos: " + ae.getApellidos() + ", Hora de salida: " + ae.getFecha_salida());
        } catch (IOException e) {
            System.out.println("Error al guardar al empleado: " + e.getMessage());
        }
        moverArchivo(nombreArchivo, "AccesoEmpleados/Salida/" + nombreArchivo, "Salida");
    }
    
    public void moverArchivo(String inicio, String fin, String carpeta)
    {
        Path origen = Paths.get(inicio);
        Path destino = Paths.get(fin);
        File carpetaBackup = new File("AccesoEmpleados/" + carpeta);
        if (!carpetaBackup.exists()) 
        {
            boolean creada = carpetaBackup.mkdirs();
            if (creada) 
                System.out.println("Carpeta 'AccesoEmpleados' creada.");
            else 
                System.out.println("No se pudo crear la carpeta 'AccesoEmpleados'.");
        }
        try {
            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("Error al mover el archivo: " + e.getMessage());
        }
    }
}
