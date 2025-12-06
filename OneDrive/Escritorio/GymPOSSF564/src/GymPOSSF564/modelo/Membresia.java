package GymPOSSF564.modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Membresia implements Serializable
{
    private int cliente_id;
    private String tipo_membresia;
    private LocalDate inicio;
    private LocalDate fin;

    public Membresia() {
    }

    public Membresia(int cliente_id, String tipo_membresia, LocalDate inicio, LocalDate fin) 
    {
        this.cliente_id = cliente_id;
        this.tipo_membresia = tipo_membresia;
        this.inicio = inicio;
        this.fin = fin;
    }

    public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getTipo_membresia() {
        return tipo_membresia;
    }

    public void setTipo_membresia(String tipo_membresia) {
        this.tipo_membresia = tipo_membresia;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getFin() {
        return fin;
    }

    public void setFin(LocalDate fin) {
        this.fin = fin;
    }
    
    @Override
    public String toString()
    {
        return "\nID: " + cliente_id +
                "\nTipo de membresia: " + tipo_membresia + 
                "\nFecha de inicio: " + inicio +
                "\nFecha de finalizacion: " + fin;
    }   
}
