package GymPOSSF564.modelo;

import java.io.Serializable;

public class Empleado implements Serializable
{
    private String nombre;
    private String apellido;
    private int edad;
    private String direccion;
    private int id;
    private String password;
    private String puesto;
    private double salario;
    private boolean acceso = false;

    public Empleado(String nombre, String apellido, int edad, String direccion, int id, String password, String puesto, double salario, boolean acceso) 
    {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.direccion = direccion;
        this.id = id;
        this.password = password;
        this.puesto = puesto;
        this.salario = salario;
        this.acceso = acceso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPasswrd() {
        return password;
    }

    public void setPasswrd(String passwrd) {
        this.password = passwrd;
    }
    
    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public boolean isAcceso() {
        return acceso;
    }

    public void setAcceso(boolean acceso) {
        this.acceso = acceso;
    }
    
    @Override
    public String toString()
    {
        return "\nNombre: " + nombre +
                "\nApellido: " + apellido +
                "\nEdad: " + edad +
                "\nID: " + id +
                "\nPuesto: " + puesto;
    }
}
