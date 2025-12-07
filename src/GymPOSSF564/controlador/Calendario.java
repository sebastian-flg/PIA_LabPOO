package GymPOSSF564.controlador;

import GymPOSSF564.modelo.ClaseGrupal;
import GymPOSSF564.modelo.GestionClases;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class Calendario 
{
    
    @FXML
    private TableView<ClaseGrupal> tablaClases;
    
    @FXML
    private TableColumn<ClaseGrupal, Integer> colId;
    
    @FXML
    private TableColumn<ClaseGrupal, String> colNombre;
    
    @FXML
    private TableColumn<ClaseGrupal, String> colInstructor;
    
    @FXML
    private TableColumn<ClaseGrupal, LocalDate> colFecha;
    
    @FXML
    private TableColumn<ClaseGrupal, LocalTime> colHora;
    
    @FXML
    private TableColumn<ClaseGrupal, Integer> colCupo;
    
    @FXML
    private TextField txtNombre;
    
    @FXML
    private TextField txtInstructor;
    
    @FXML
    private DatePicker dpFecha;
    
    @FXML
    private TextField txtHora;
    
    @FXML
    private TextField txtCupo;
    
    @FXML
    private Button btnRegistrar;
    
    @FXML
    private Button btnActualizar;
    
    @FXML
    private Button btnEliminar;
    
    @FXML
    private Button btnGuardar;
    
    @FXML
    private Button btnRegresar;
    
    private ObservableList<ClaseGrupal> listaObservable;
    private List<ClaseGrupal> clases = new ArrayList<>();
    private GestionClases gestor = new GestionClases();
    
    public void initialize() 
    {
        colId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id_clase"));
        colNombre.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nombre"));
        colInstructor.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("instructor"));
        colFecha.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("hora"));
        colCupo.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("cupoMaximo"));
        
        List<ClaseGrupal> cargadas = gestor.cargarClases(clases);
        if (cargadas != null) {
            clases = cargadas;
        }
        ClaseGrupal.inicializarContador(clases);
        
        listaObservable = FXCollections.observableArrayList(clases);
        tablaClases.setItems(listaObservable);
        
        btnRegistrar.setOnAction(e -> registrarClase());
        btnActualizar.setOnAction(e -> actualizarClaseSeleccionada());
        btnEliminar.setOnAction(e -> eliminarClaseSeleccionada());
        btnGuardar.setOnAction(e -> guardarCalendario());
        btnRegresar.setOnAction(evento -> {
            try 
            {
                Parent menuPrincipal = FXMLLoader.load(getClass().getResource("/GymPOSSF564/vista/guifxml.fxml"));
                Scene escena = btnRegresar.getScene();
                escena.setRoot(menuPrincipal);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    private void registrarClase() 
    {
        try 
        {
            String nombre = txtNombre.getText();
            String instructor = txtInstructor.getText();
            LocalDate fecha = dpFecha.getValue();
            LocalTime hora = LocalTime.parse(txtHora.getText());
            int cupo = Integer.parseInt(txtCupo.getText());
            
            if (nombre.isEmpty() || instructor.isEmpty() || fecha == null) {
                mostrarAlerta("Datos incompletos", "Llena todos los campos.");
                return;
            }
            
            ClaseGrupal nueva = new ClaseGrupal(nombre, instructor, fecha, hora, cupo);
            listaObservable.add(nueva);
            clases.add(nueva);
            limpiarCampos();
            
        } catch (DateTimeParseException ex) {
            mostrarAlerta("Formato de hora invalido", "Usa el formato HH:MM.");
        } catch (NumberFormatException ex) {
            mostrarAlerta("Cupo invalido", "El cupo debe ser un numero entero.");
        }
    }
    
    private void actualizarClaseSeleccionada() 
    {
        ClaseGrupal seleccionada = tablaClases.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Sin seleccion", "Selecciona una clase en la tabla.");
            return;
        }
        
        try 
        {
            String nombre = txtNombre.getText();
            String instructor = txtInstructor.getText();
            LocalDate fecha = dpFecha.getValue();
            LocalTime hora = LocalTime.parse(txtHora.getText());
            int cupo = Integer.parseInt(txtCupo.getText());
            
            if (nombre.isEmpty() || instructor.isEmpty() || fecha == null) {
                mostrarAlerta("Datos incompletos", "Llena todos los campos.");
                return;
            }
            
            seleccionada.setNombre(nombre);
            seleccionada.setInstructor(instructor);
            seleccionada.setFecha(fecha);
            seleccionada.setHora(hora);
            seleccionada.setCupoMaximo(cupo);
            
            tablaClases.refresh();
            limpiarCampos();
            
        } catch (DateTimeParseException ex) {
            mostrarAlerta("Formato de hora invalido", "Usa el formato HH:MM (por ejemplo 18:30).");
        } catch (NumberFormatException ex) {
            mostrarAlerta("Cupo invalido", "El cupo debe ser un número entero.");
        }
    }
    
    private void eliminarClaseSeleccionada() 
    {
        ClaseGrupal seleccionada = tablaClases.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Sin seleccion", "Selecciona una clase en la tabla.");
            return;
        }
        
        listaObservable.remove(seleccionada);
        clases.remove(seleccionada);
        limpiarCampos();
    }
    
    private void guardarCalendario() 
    {
        gestor.serializarClases(new ArrayList<>(listaObservable));
        mostrarAlerta("Informacion", "Calendario guardado en la carpeta 'Calendario'.");
    }
    
    private void limpiarCampos() 
    {
        txtNombre.clear();
        txtInstructor.clear();
        dpFecha.setValue(null);
        txtHora.clear();
        txtCupo.clear();
    }
    
    private void mostrarAlerta(String titulo, String mensaje) 
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
