package GymPOSSF564.controlador;

import GymPOSSF564.modelo.NotificadorMembresias;
import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.scene.Node; 
import javafx.scene.Scene; 
import javafx.stage.Stage; 

public class Controller 
{
    @FXML
    private Button cerrar_sesion;
    
    @FXML
    private StackPane contenido;
    
    @FXML
    private Button clientes;
    
    @FXML
    private Button empleados;
    
    @FXML
    private Button inventario;
    
    @FXML
    private Button actividades;
    
    @FXML
    private Button reportes;
    
    public void initialize() 
    {
        clientes.setOnAction(e -> mostrarMenuClientes());
        empleados.setOnAction(e -> mostrarMenuEmpleados());
        inventario.setOnAction(e -> mostrarMenuInventario());
        actividades.setOnAction(e -> mostrarCalendario());
        reportes.setOnAction(e -> mostrarMenuReportes());
        NotificadorMembresias notificador = new NotificadorMembresias();
        Thread hiloNotificaciones = new Thread(notificador);
        hiloNotificaciones.setDaemon(true);
        hiloNotificaciones.start();
    }
    
    private void cambiarContenido(String fxml) 
    {
        try 
        {
            Parent root = FXMLLoader.load(getClass().getResource("/GymPOSSF564/vista/" + fxml));
            contenido.getChildren().clear();
            contenido.getChildren().add(root);
            
            FadeTransition ft = new FadeTransition(Duration.millis(300), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void Cerrar_sesion(ActionEvent event) 
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../vista/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void mostrarMenuClientes() {
        cambiarContenido("menu_clientes.fxml");
    }
    
    private void mostrarMenuEmpleados() {
        cambiarContenido("menu_empleados.fxml");
    }
    
    private void mostrarMenuInventario() {
        cambiarContenido("menu_inventario.fxml");
    }
    
    private void mostrarMenuReportes() {
        cambiarContenido("menu_reportes.fxml");
    }
    
    private void mostrarCalendario() {
        cambiarContenido("calendarioView.fxml");
    }
}

