package GymPOSSF564.controlador;

import GymPOSSF564.modelo.Empleado;
import GymPOSSF564.modelo.GestionEmpleadosFlores;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Registro
{
    @FXML
    private TextField nombre;

    @FXML
    private TextField apellidos;

    @FXML
    private TextField edad;

    @FXML
    private TextField direccion;

    @FXML
    private TextField id;

    @FXML
    private ChoiceBox<String> puesto;

    @FXML
    private TextField salario;

    @FXML
    private  TextField contrasena;
    
    boolean nombreValido = false, apellidosValido = false,
            edadValido = false, direccionValido = false,
            idValido = false, puestoValido = true, salarioValido = false, contrasenaValida = false;

    private List<Empleado> emp = new ArrayList<>();
    private GestionEmpleadosFlores empleados = new GestionEmpleadosFlores();

    public void setEmp(List<Empleado> emp) {
        this.emp = emp;
    }

    public void setEmpleados(GestionEmpleadosFlores empleados) {
        this.empleados = empleados;
    }

    public void initialize()
    {
        System.out.println("RegistroController inicializado.");
        puesto.getItems().addAll("Recepcionista", "Entrenador", "Gerente", "Limpieza");
        puesto.setValue("Recepcionista");
    }

    public void regresarScreen(ActionEvent e) throws IOException {
        cambiarDeEscena(e,"login.fxml","GymPOSSF564");
    }

    public void cambiarDeEscena(ActionEvent event, String fxmlFile, String newTitle) throws IOException 
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../vista/" + fxmlFile));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = Objects.requireNonNull(this.getClass().getResource("/Recursos1/style/login.css")).toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.setTitle(newTitle);
        stage.centerOnScreen();
        stage.show();
    }
    
    public void registrarUsuario(ActionEvent event)
    {
        int campo_id;
        try {
            campo_id = Integer.parseInt(id.getText());
        } catch (NumberFormatException e){
            id.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            mostrarAlerta("Error en el Registro", "El ID debe ser un numero valido.", Alert.AlertType.ERROR);
            return;
        }

        boolean found = false;
        for(Empleado e: emp)
        {
            if(campo_id == e.getId())
            {
                found = true;
                Label error = new Label();
                mostrarAlerta("ID ya ingresado", "Favor de ingresar uno nuevo.", Alert.AlertType.ERROR);
                break;
            }
        }

        if(!found)
        {
            String campo_nombre = nombre.getText();
            String campo_apellido = apellidos.getText();
            String campo_edad = edad.getText();
            String campo_direccion = direccion.getText();
            String campo_puesto = puesto.getValue();
            double campo_salario;
            try {
                campo_salario = Double.parseDouble(salario.getText());
            }
            catch (Exception e)
            {
                salario.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                mostrarAlerta("Error en el Registro", "El salario debe ser un numero valido.", Alert.AlertType.ERROR);
                return;
            }

            String campo_password = contrasena.getText();

            if(nombreValido && apellidosValido && edadValido && direccionValido && idValido && puestoValido && salarioValido && contrasenaValida){
                mostrarAlerta("Registro Exitoso", "El empleado ha sido registrado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error en el Registro", "Por favor, revise los campos marcados en rojo o aquellos vacios", Alert.AlertType.ERROR);
                return;
            }

            Empleado emp1 = new Empleado(campo_nombre, campo_apellido, Integer.parseInt(campo_edad), campo_direccion, campo_id, campo_password, campo_puesto, campo_salario, true);
            empleados.agregarEmpleado(emp, emp1);

        }
    }

    public void validarID(javafx.scene.input.KeyEvent keyEvent) 
    {
        String texto = id.getText().trim();
        if (texto.isEmpty()) 
        {
            id.setStyle("");
            return;
        }
        int valor;
        try {
            valor = Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            idValido = false;
            id.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            return;
        }
        boolean existe = emp.stream().anyMatch(e -> e.getId() == valor);
        if (existe) {
            idValido = false;
            id.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        } else {
            id.setStyle("");
            idValido = true;
        }
    }

    public void validarSalario(javafx.scene.input.KeyEvent keyEvent) 
    {
        String texto = salario.getText().trim();
        if (texto.isEmpty()) 
        {
            salario.setStyle("");
            salarioValido = false;
            return;
        }

        double valor;
        try
        {
            valor = Double.parseDouble(texto);
            salario.setStyle("");
            salarioValido = true;
        } catch (NumberFormatException ex){
            salario.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            salarioValido = false;
        }
    };

    public void validarContrasena(javafx.scene.input.KeyEvent keyEvent) 
    {
        String txt = contrasena.getText();
        if (txt == null || txt.trim().isEmpty()) 
        {
            contrasena.setStyle("");
            contrasenaValida = false;
            return;
        }
        boolean cumple = txt.length() >= 8
                && txt.matches(".*\\d.*")
                && txt.matches(".*[A-Z].*")
                && txt.matches(".*[^a-zA-Z0-9].*");
        if (cumple) {
            contrasena.setStyle("");
            contrasenaValida = true;
        } else {
            contrasenaValida = false;
            contrasena.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        }
    };


    public void validarNombres(javafx.scene.input.KeyEvent keyEvent) 
    {
        TextField campo = (TextField) keyEvent.getSource();
        String texto = campo.getText().trim();

        if (texto.isEmpty()) 
        {
            campo.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

            if(campo.getId().equals("nombre")){
                nombreValido = false;
            } else if (campo.getId().equals("apellidos")){
                apellidosValido = false;
            }
            return;
        }
 
        if (texto.matches("^[\\p{L} \\-']+$")) 
        {
            System.out.println(campo.getId());
            if(campo.getId().equals("nombre")){
                nombreValido = true;
            } else if (campo.getId().equals("apellidos")){
                apellidosValido = true;
            }
            campo.setStyle("");

        } else {
            if(campo.getId().equals("nombre")){
                nombreValido = false;
            } else if (campo.getId().equals("apellidos")){
                apellidosValido = false;
            }
            campo.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        }
    };

    public void validarEdad(javafx.scene.input.KeyEvent keyEvent) 
    {
        String texto = edad.getText().trim();
        if (texto.isEmpty()) 
        {
            edad.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            edadValido = false;
            return;
        }
        int valor;
        try 
        {
            valor = Integer.parseInt(texto);
            if (valor > 0 && valor < 120) 
            {
                edad.setStyle("");
                edadValido = true;
            } else {
                edad.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                edadValido = false;
            }
        } catch (NumberFormatException ex) {
            edad.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            edadValido = false;
        }
    };

    public void validarDireccion(javafx.scene.input.KeyEvent keyEvent) 
    {
        String texto = direccion.getText().trim();
        if (texto.isEmpty()) 
        {
            direccion.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            direccionValido = false;
        } else {
            direccion.setStyle("");
            direccionValido = true;
        }
    };

    public void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) 
    {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
