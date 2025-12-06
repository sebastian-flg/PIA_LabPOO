package GymPOSSF564.modelo;

import java.time.LocalDateTime;

public class Acceso 
{
    private int id;
    private String nombre;
    private String apellidos;
    private LocalDateTime fecha_entrada;
    private LocalDateTime fecha_salida;

    public Acceso(int id, String nombre, String apellidos) 
    {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public LocalDateTime getFecha_entrada() {
        return fecha_entrada;
    }

    public void setFecha_entrada(LocalDateTime fecha_entrada) {
        this.fecha_entrada = fecha_entrada;
    }

    public LocalDateTime getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(LocalDateTime fecha_salida) {
        this.fecha_salida = fecha_salida;
    }
    
    @Override
    public String toString()
    {
        return "ID del empleado: " + id +
                ", Nombre: " + nombre +
                ", Apellidos: " + apellidos +
                ", Hora de entrada: " + fecha_entrada +
                ", Hora de salida: " + fecha_salida;
    }   
}
