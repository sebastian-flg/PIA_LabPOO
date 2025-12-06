package GymPOSSF564.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ClaseGrupal implements Serializable 
{
    private static int contador_id = 1;
    
    private int clase_id;
    private String nombre;
    private String instructor;
    private LocalDate fecha;
    private LocalTime hora;
    private int cupoMaximo;
    
    public ClaseGrupal() {
    }

    public ClaseGrupal(String nombre, String instructor, LocalDate fecha, LocalTime hora, int cupoMaximo) 
    {
        this.clase_id = contador_id++;
        this.nombre = nombre;
        this.instructor = instructor;
        this.fecha = fecha;
        this.hora = hora;
        this.cupoMaximo = cupoMaximo;
    }

    public static void inicializarContador(List<ClaseGrupal> clases) 
    {
        int max_id = 0;
        if (clases != null) 
        {
            for (ClaseGrupal c : clases) 
            {
                if (c.getClase_id() > max_id) {
                    max_id = c.getClase_id();
                }
            }
        }
        contador_id = max_id + 1;
    }

    public int getClase_id() {
        return clase_id;
    }

    public void setClase_id(int clase_id) {
        this.clase_id = clase_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }
    
    @Override
    public String toString() 
    {
        return "ID Clase: " + clase_id +
                "\nNombre: " + nombre +
                "\nInstructor: " + instructor +
                "\nFecha: " + fecha +
                "\nHora: " + hora +
                "\nCupo maximo: " + cupoMaximo;
    }
}
