package GymPOSSF564.modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeneradorReportesS implements Runnable 
{
    private final TipoReporte tipo;
    private final String nombreUsuario; 

    public GeneradorReportesS(TipoReporte tipo) {
        this(tipo, "sistema");
    }
    
    public GeneradorReportesS(TipoReporte tipo, String nombreUsuario) 
    {
        this.tipo = tipo;
        this.nombreUsuario = nombreUsuario;
    }

    @Override
    public void run() 
    {
        try {
            generarReporte();
        } catch (IOException e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }
    
    private void generarReporte() throws IOException 
    {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreArchivo = "reporte_" + tipo.name().toLowerCase() + "_" + timestamp + ".txt";
        
        File carpetaReportes = new File("Reportes");
        if (!carpetaReportes.exists()) {
            carpetaReportes.mkdirs();
        }
        
        File archivo = new File(carpetaReportes, nombreArchivo);

        try (FileWriter fw = new FileWriter(archivo, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) 
        {
            
            out.println("===== GYM POSSF 564 - REPORTE " + tipo.name() + " =====");
            out.println("Generado por: " + nombreUsuario);
            out.println("Fecha de generacion: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            out.println();

            switch (tipo) 
            {
                case CLIENTES:
                    generarReporteClientes(out);
                    break;
                case EMPLEADOS:
                    generarReporteEmpleados(out);
                    break;
                case INVENTARIO:
                    generarReporteInventario(out);
                    break;
                case MEMBRESIAS:
                    generarReporteMembresias(out);
                    break;
                default:
                    out.println("Tipo de reporte no soportado.");
            }
            
            out.println("\n===== FIN DEL REPORTE =====");
        }
        
        System.out.println("Reporte generado: " + archivo.getAbsolutePath());
    }
    
    private void generarReporteClientes(PrintWriter out) 
    {
        GestionClientesFlores gestor = new GestionClientesFlores();
        List<Cliente> clientes = new ArrayList<>();
        List<Cliente> cargados = gestor.cargarClientes(clientes);
        
        if (cargados == null || cargados.isEmpty()) 
        {
            out.println("No hay clientes registrados.");
            return;
        }
        
        out.println("Total de clientes: " + cargados.size());
        out.println("------------------------------------");
        for (Cliente c : cargados) {
            out.println(c.toString());
            out.println("------------------------------------");
        }
    }
    
    private void generarReporteEmpleados(PrintWriter out) 
    {
        GestionEmpleadosFlores gestor = new GestionEmpleadosFlores();
        List<Empleado> empleados = new ArrayList<>();
        List<Empleado> cargados = gestor.cargarEmpleado(empleados);
        
        if (cargados == null || cargados.isEmpty()) 
        {
            out.println("No hay empleados registrados.");
            return;
        }
        
        out.println("Total de empleados: " + cargados.size());
        out.println("------------------------------------");
        for (Empleado e : cargados) {
            out.println(e.toString());
            out.println("------------------------------------");
        }
    }
    
    private void generarReporteInventario(PrintWriter out) 
    {
        GestionInventarioFlores gestor = new GestionInventarioFlores();
        List<Inventario> inventario = new ArrayList<>();
        List<Inventario> cargados = gestor.cargarInventario(inventario);
        
        if (cargados == null || cargados.isEmpty()) 
        {
            out.println("No hay productos en inventario.");
            return;
        }
        
        out.println("Total de productos: " + cargados.size());
        out.println("------------------------------------");
        for (Inventario i : cargados) 
        {
            out.println(i.toString());
            out.println("------------------------------------");
        }
    }
    
    private void generarReporteMembresias(PrintWriter out) 
    {
        SistemaMembresias0112 sistema = new SistemaMembresias0112();
        List<Membresia> membresias = new ArrayList<>();
        List<Membresia> cargadas = sistema.cargarMembresias(membresias);
        
        if (cargadas == null || cargadas.isEmpty()) 
        {
            out.println("No hay membresias registradas.");
            return;
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        out.println("Total de membresias: " + cargadas.size());
        out.println("------------------------------------");
        for (Membresia m : cargadas) 
        {
            out.println("ID Cliente: " + m.getCliente_id());
            out.println("Tipo: " + m.getTipo_membresia());
            out.println("Inicio: " + (m.getInicio() != null ? m.getInicio().format(formatter) : "N/A"));
            out.println("Fin: " + (m.getFin() != null ? m.getFin().format(formatter) : "N/A"));
            out.println("------------------------------------");
        }
    }
}
