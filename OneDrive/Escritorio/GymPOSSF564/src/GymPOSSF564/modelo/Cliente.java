package GymPOSSF564.modelo;

import java.io.Serializable;
import java.util.List;

public class Cliente implements Serializable
{
    private static int contador_id = 1;
    private int cliente_id;
    private String nombres;
    private String apellidos;
    private String num_telefono;
    private int puntos;
    
    public Cliente() {
    }
    
    public Cliente(String nombres, String apellidos, String num_telefono) 
    {
        this.cliente_id = contador_id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.num_telefono = num_telefono;
        System.out.println("ID: " + this.cliente_id);
    }
    
    public static synchronized void incrementarContador() {
        contador_id++;
    }
    
    public static void eliminarContadorClienteNoUsado(){
        contador_id--;
    }
    
    public static void inicializarContador(List<Cliente> clientes) 
    {
        if (!clientes.isEmpty()) 
        {
            int max_id = clientes.stream().mapToInt(Cliente::getCliente_id).max().orElse(0);
            contador_id = max_id + 1;
        }
    }
    
    public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNum_telefono() {
        return num_telefono;
    }

    public void setNum_telefono(String num_telefono) {
        this.num_telefono = num_telefono;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public static int getContadorId() {
        return contador_id;
    }
    
    @Override
    public String toString()
    {
        return "ID: " + cliente_id +
                "\nNombre: " + nombres +
                "\nApellidos: " + apellidos +
                "\nTelefono: " + num_telefono +
                "\nPuntos: " + puntos;
    }
    
}
