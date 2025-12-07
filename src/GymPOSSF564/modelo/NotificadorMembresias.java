package GymPOSSF564.modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificadorMembresias implements Runnable 
{
    
    private final long intervaloMs;
    private final int diasAviso; 
    
    public NotificadorMembresias() {
        this(60_000L, 3); 
    }
    
    public NotificadorMembresias(long intervaloMs, int diasAviso) 
    {
        this.intervaloMs = intervaloMs;
        this.diasAviso = diasAviso;
    }
    
    @Override
    public void run() 
    {
        while (!Thread.currentThread().isInterrupted()) 
        {
            try {
                revisarMembresias();
                Thread.sleep(intervaloMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private void revisarMembresias() 
    {
        SistemaMembresias0112 sistema = new SistemaMembresias0112();
        List<Membresia> membresias = new ArrayList<>();
        List<Membresia> cargadas = sistema.cargarMembresias(membresias);
        
        if (cargadas == null || cargadas.isEmpty()) {
            return;
        }
        
        File carpetaNotificaciones = new File("Notificaciones");
        if (!carpetaNotificaciones.exists()) {
            carpetaNotificaciones.mkdirs();
        }
        
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File archivo = new File(carpetaNotificaciones, "notificaciones_membresias_" + timestamp + ".txt");
        
        try (FileWriter fw = new FileWriter(archivo, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) 
        {
             
            LocalDate hoy = LocalDate.now();
            boolean hayAvisos = false;
            
            for (Membresia m : cargadas) 
            {
                if (m.getFin() == null) {
                    continue;
                }
                long diasRestantes = ChronoUnit.DAYS.between(hoy, m.getFin());
                
                if (diasRestantes <= diasAviso) {
                    hayAvisos = true;
                    out.println("ID Cliente: " + m.getCliente_id() +
                            " | Tipo: " + m.getTipo_membresia() +
                            " | Vence: " + m.getFin() +
                            " | Dias restantes: " + diasRestantes);
                }
            }
            
            if (!hayAvisos) {
                out.println("Sin membresias por vencer en los proximos " + diasAviso + " dias.");
            }
            
        } catch (IOException e) {
            System.out.println("Error al escribir archivo de notificaciones: " + e.getMessage());
        }
    }
}
