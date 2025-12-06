package GymPOSSF564.controlador;

import GymPOSSF564.modelo.ControlAccesoEmpleadosFlores;
import GymPOSSF564.modelo.Empleado;
import GymPOSSF564.modelo.GestionEmpleadosFlores;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; 
import javafx.fxml.Initializable;
import javafx.scene.Node; 
import javafx.scene.Parent; 
import javafx.scene.Scene; 
import javafx.scene.control.*; 
import javafx.stage.Stage; 


public class Login implements Initializable 
{
    @FXML
    private TextField idTextField;

    @FXML
    private TextField contrasenaTextField;

    @FXML
    private Button logginButton;
    @FXML
    private  Button registrarButton;
    @FXML
    private Label lbError; 

    
    private List<Empleado> emp = new ArrayList<>();
    private GestionEmpleadosFlores empleados = new GestionEmpleadosFlores();
    private ControlAccesoEmpleadosFlores controlAcceso = new ControlAccesoEmpleadosFlores();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) 
    {
        Empleado emp1 = new Empleado("Sebastian", "Flores", 20, "direccion", 0, "12345", "Administrador", 100000.0, true);
        empleados.agregarEmpleado(emp, emp1);
        empleados.cargarEmpleado(emp); 
    }

    public void btnIngresar(ActionEvent event) 
    {
        String e_id = idTextField.getText();
        String e_pswrd = contrasenaTextField.getText(); 
        boolean find = false;

        for(Empleado e: emp)
        {

            if(e_id.equals(String.valueOf(e.getId())) && e.isAcceso())
            {
                find = true;
                if(e_pswrd.equals(e.getPasswrd())){
                    System.out.println("Sesion iniciada");
                    controlAcceso.registrarEntrada(Integer.parseInt(e_id), e.getNombre(), e.getApellido());
                    
                    mostrarAlerta("Inicio de Sesion", "¡Inicio de sesion correcto! Bienvenido.", Alert.AlertType.INFORMATION);
                    
                    try {
                        cambiarDeEscena(event, "guifxml.fxml", "GymPOSSF - Dashboard"); 
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                } else 
                {
                    System.out.println("Acceso denegado");
                    lbError.setText("Contraseña incorrecta.");
                    mostrarAlerta("Error", "Datos invalidos. Contraseña incorrecta.", Alert.AlertType.ERROR);
                }
                return; 
            }
        }

        if(!find) 
        {
            System.out.println("No se encontro el ID o no tiene acceso");
            mostrarAlerta("Error", "Datos invalidos. ID no encontrado o sin acceso.", Alert.AlertType.ERROR);
        }
    }

    public void btnRegistrar(ActionEvent event) 
    {

        System.out.println("Boton 'Registrar' presionado. Cargando escena de registro...");
        
        try {
            cambiarDeEscena(event, "registroView.fxml", "Registro de Empleado");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void cambiarDeEscena(ActionEvent event, String fxmlFile, String newTitle) throws IOException 
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../vista/" + fxmlFile));
        Parent root = loader.load();

        if(fxmlFile.equals("registroView.fxml"))
        {
            Registro c = loader.getController();
            c.setEmp(emp);
            c.setEmpleados(empleados);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        String css = Objects.requireNonNull(this.getClass().getResource("/Recursos1/style/login.css")).toExternalForm();
        scene.getStylesheets().add(css);
        
        stage.setScene(scene);
        stage.setTitle(newTitle);
        stage.centerOnScreen();
        stage.show();
    }

    public void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) 
    {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}