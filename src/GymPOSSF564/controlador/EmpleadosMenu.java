package GymPOSSF564.controlador;

import GymPOSSF564.modelo.ControlAccesoEmpleadosFlores;
import GymPOSSF564.modelo.Empleado;
import GymPOSSF564.modelo.GestionEmpleadosFlores;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EmpleadosMenu 
{
    @FXML
    private VBox contenido;
    @FXML
    private Button registrar;
    @FXML
    private Button actualizar;
    @FXML
    private Button buscar;
    @FXML
    private Button eliminar;
    @FXML
    private Button entradaSalida;
    @FXML
    private Button salir;

    private List<Empleado> empleados = new ArrayList<>();
    private GestionEmpleadosFlores gestor = new GestionEmpleadosFlores();

    public void initialize() 
    {
        gestor.cargarEmpleado(empleados);

        registrar.setOnAction(evento -> {
            contenido.getChildren().clear();

            Label indicador_menu = new Label("Registrar empleado");
            TextField nombres = new TextField();
            TextField apellidos = new TextField();
            TextField edad = new TextField();
            TextField direccion = new TextField();
            TextField id = new TextField();
            PasswordField password = new PasswordField();
            Label lblPuesto = new Label("Puesto:");
            ChoiceBox<String> puesto = new ChoiceBox<>();
            puesto.setPrefWidth(200);
            puesto.getItems().addAll("Gerente", "Entrenador", "Recepción", "Limpieza");
            puesto.setValue("Recepcionista");
            TextField salario = new TextField();
            Button aceptar = new Button("Aceptar");
            Button cancelar = new Button("Cancelar");

            nombres.setPromptText("Nombre");
            apellidos.setPromptText("Apellidos");
            edad.setPromptText("Edad");
            direccion.setPromptText("Direccion");
            id.setPromptText("ID");
            password.setPromptText("Contraseña");
            salario.setPromptText("Salario");

            nombres.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                    nombres.setText(oldValue);
                }
            });

            apellidos.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                    apellidos.setText(oldValue);
                }
            });

            edad.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    edad.setText(oldValue);
                } else if (newValue.length() > 3) {
                    edad.setText(oldValue);
                }
            });

            id.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    id.setText(oldValue);
                }
            });

            salario.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    salario.setText(oldValue);
                }
            });

            contenido.getChildren().add(indicador_menu);
            contenido.getChildren().add(nombres);
            contenido.getChildren().add(apellidos);
            contenido.getChildren().add(edad);
            contenido.getChildren().add(direccion);
            contenido.getChildren().add(id);
            contenido.getChildren().add(password);
            contenido.getChildren().addAll(lblPuesto, puesto);
            contenido.getChildren().add(salario);
            contenido.getChildren().add(aceptar);
            contenido.getChildren().add(cancelar);

            aceptar.setOnAction(event -> {
                String nombre_empleado = nombres.getText().trim();
                String apellidos_empleado = apellidos.getText().trim();
                String edad_empleado = edad.getText().trim();
                String direccion_empleado = direccion.getText().trim();
                String id_empleado = id.getText().trim();
                String password_empleado = password.getText();
                String campo_puesto = puesto.getValue();
                String salario_empleado = salario.getText().trim();

                if (nombre_empleado.isEmpty()) {
                    mostrarAlerta("Error", "El nombre es obligatorio");
                    return;
                }

                if (apellidos_empleado.isEmpty()) {
                    mostrarAlerta("Error", "Los apellidos son obligatorios");
                    return;
                }

                if (edad_empleado.isEmpty()) {
                    mostrarAlerta("Error", "La edad es obligatoria");
                    return;
                }

                if (direccion_empleado.isEmpty()) {
                    mostrarAlerta("Error", "La dirección es obligatoria");
                    return;
                }

                if (id_empleado.isEmpty()) {
                    mostrarAlerta("Error", "El ID es obligatorio");
                    return;
                }

                if (password_empleado.isEmpty()) {
                    mostrarAlerta("Error", "La contraseña es obligatoria");
                    return;
                }

                if (salario_empleado.isEmpty()) {
                    mostrarAlerta("Error", "El salario es obligatorio");
                    return;
                }

                int id_int = Integer.parseInt(id_empleado);
                boolean idExiste = false;
                for (Empleado emp : empleados) 
                {
                    if (emp.getId() == id_int) 
                    {
                        idExiste = true;
                        mostrarAlerta("Error", "Ya existe un empleado con este ID");
                        break;
                    }
                }

                if (!idExiste) 
                {
                    try {
                        int edadInt = Integer.parseInt(edad_empleado);
                        double salarioDouble = Double.parseDouble(salario_empleado);

                        Empleado nuevoEmpleado = new Empleado(
                            nombre_empleado,
                            apellidos_empleado,
                            edadInt,
                            direccion_empleado,
                            id_int,
                            password_empleado,
                            campo_puesto,
                            salarioDouble,
                            false
                        );

                        gestor.agregarEmpleado(empleados, nuevoEmpleado);
                        mostrarAlerta("Exito", "Empleado registrado exitosamente");
                        cancelar.fire();
                    } catch (NumberFormatException e) {
                        mostrarAlerta("Error", "Formato numerico invalido en edad o salario");
                    }
                }
            });

            cancelar.setOnAction(event -> {
                contenido.getChildren().clear();
            });
        });

        actualizar.setOnAction(evento -> {
            contenido.getChildren().clear();

            Label tituloBusqueda = new Label("Buscar empleado a actualizar");
            TextField campoBusqueda = new TextField();
            campoBusqueda.setPromptText("Ingrese nombre del empleado...");
            campoBusqueda.setPrefWidth(200);

            campoBusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                    campoBusqueda.setText(oldValue);
                }
            });

            Button botonBuscar = new Button("Buscar");
            Button botonCancelar = new Button("Cancelar");

            VBox resultadosContainer = new VBox(10);
            ScrollPane scrollResultados = new ScrollPane();
            scrollResultados.setFitToWidth(true);
            scrollResultados.setPrefViewportHeight(200);
            scrollResultados.setContent(resultadosContainer);

            HBox busquedaLayout = new HBox(10);
            busquedaLayout.getChildren().addAll(campoBusqueda, botonBuscar, botonCancelar);

            contenido.getChildren().addAll(tituloBusqueda, busquedaLayout, scrollResultados);

            botonBuscar.setOnAction(event -> {
                String nombreBuscado = campoBusqueda.getText().trim();

                if (nombreBuscado.isEmpty()) {
                    mostrarAlerta("Advertencia", "Por favor ingrese un nombre para buscar");
                    return;
                }

                resultadosContainer.getChildren().clear();

                List<Empleado> empleadosEncontrados = buscarEmpleadosPorNombre(empleados, nombreBuscado);

                if (empleadosEncontrados.isEmpty()) {
                    Label sinResultados = new Label("No se encontraron empleados con el nombre: " + nombreBuscado);
                    resultadosContainer.getChildren().add(sinResultados);
                } else 
                {
                    Label tituloResultados = new Label("Seleccione el empleado a actualizar:");
                    tituloResultados.setStyle("-fx-font-weight: bold;");
                    resultadosContainer.getChildren().add(tituloResultados);

                    for (Empleado empleado : empleadosEncontrados) 
                    {
                        HBox empleadoRow = crearFilaEmpleadoParaActualizar(empleado);
                        resultadosContainer.getChildren().add(empleadoRow);
                    }
                }
            });

            botonCancelar.setOnAction(event -> {
                contenido.getChildren().clear();
            });
        });

        buscar.setOnAction(evento -> {
            contenido.getChildren().clear();

            TextField campoBusqueda = new TextField();
            campoBusqueda.setPromptText("Ingrese nombre del empleado...");
            campoBusqueda.setPrefWidth(200);

            campoBusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                    campoBusqueda.setText(oldValue);
                }
            });

            Button botonBuscar = new Button("Buscar");
            Button botonCancelar = new Button("Cancelar");

            VBox resultadosContainer = new VBox(10);
            ScrollPane scrollResultados = new ScrollPane();
            scrollResultados.setFitToWidth(true);
            scrollResultados.setPrefViewportHeight(300);
            scrollResultados.setContent(resultadosContainer);

            HBox busquedaLayout = new HBox(10);
            busquedaLayout.getChildren().addAll(campoBusqueda, botonBuscar, botonCancelar);

            contenido.getChildren().addAll(busquedaLayout, scrollResultados);

            botonBuscar.setOnAction(event -> {
                String nombreBuscado = campoBusqueda.getText().trim();

                if (nombreBuscado.isEmpty()) {
                    mostrarAlerta("Advertencia", "Por favor ingrese un nombre para buscar");
                    return;
                }

                resultadosContainer.getChildren().clear();

                List<Empleado> empleadosEncontrados = buscarEmpleadosPorNombre(empleados, nombreBuscado);

                if (empleadosEncontrados.isEmpty()) 
                {
                    Label sinResultados = new Label("No se encontraron empleados con el nombre: " + nombreBuscado);
                    resultadosContainer.getChildren().add(sinResultados);
                } else 
                {
                    Label tituloResultados = new Label("Resultados encontrados (" + empleadosEncontrados.size() + "):");
                    tituloResultados.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
                    resultadosContainer.getChildren().add(tituloResultados);

                    for (Empleado empleado : empleadosEncontrados) 
                    {
                        VBox empleadoCard = crearTarjetaEmpleado(empleado);
                        resultadosContainer.getChildren().add(empleadoCard);
                    }
                }
            });

            botonCancelar.setOnAction(event -> {
                contenido.getChildren().clear();
            });
        });

        eliminar.setOnAction(evento -> {
            contenido.getChildren().clear();

            Label tituloBusqueda = new Label("Buscar empleado a eliminar");
            TextField campoBusqueda = new TextField();
            campoBusqueda.setPromptText("Ingrese nombre del empleado...");
            campoBusqueda.setPrefWidth(200);

            campoBusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                    campoBusqueda.setText(oldValue);
                }
            });

            Button botonBuscar = new Button("Buscar");
            Button botonCancelar = new Button("Cancelar");

            VBox resultadosContainer = new VBox(10);
            ScrollPane scrollResultados = new ScrollPane();
            scrollResultados.setFitToWidth(true);
            scrollResultados.setPrefViewportHeight(200);
            scrollResultados.setContent(resultadosContainer);

            HBox busquedaLayout = new HBox(10);
            busquedaLayout.getChildren().addAll(campoBusqueda, botonBuscar, botonCancelar);

            contenido.getChildren().addAll(tituloBusqueda, busquedaLayout, scrollResultados);

            botonBuscar.setOnAction(event -> {
                String nombreBuscado = campoBusqueda.getText().trim();

                if (nombreBuscado.isEmpty()) {
                    mostrarAlerta("Advertencia", "Por favor ingrese un nombre para buscar");
                    return;
                }

                resultadosContainer.getChildren().clear();

                List<Empleado> empleadosEncontrados = buscarEmpleadosPorNombre(empleados, nombreBuscado);

                if (empleadosEncontrados.isEmpty()) 
                {
                    Label sinResultados = new Label("No se encontraron empleados con el nombre: " + nombreBuscado);
                    resultadosContainer.getChildren().add(sinResultados);
                } else 
                {
                    Label tituloResultados = new Label("Seleccione el empleado a eliminar:");
                    tituloResultados.setStyle("-fx-font-weight: bold;");
                    resultadosContainer.getChildren().add(tituloResultados);

                    for (Empleado empleado : empleadosEncontrados) 
                    {
                        HBox empleadoRow = crearFilaEmpleadoEliminar(empleado);
                        resultadosContainer.getChildren().add(empleadoRow);
                    }
                }
            });

            botonCancelar.setOnAction(event -> {
                contenido.getChildren().clear();
            });
        });

        entradaSalida.setOnAction(evento -> {
            contenido.getChildren().clear();

            Label tituloBusqueda = new Label("Entrada / Salida del empleado");
            TextField campoBusqueda = new TextField();
            campoBusqueda.setPromptText("Ingrese nombre del empleado...");
            campoBusqueda.setPrefWidth(200);

            campoBusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                    campoBusqueda.setText(oldValue);
                }
            });

            Button botonBuscar = new Button("Buscar");
            Button botonCancelar = new Button("Cancelar");

            VBox resultadosContainer = new VBox(10);
            ScrollPane scrollResultados = new ScrollPane();
            scrollResultados.setFitToWidth(true);
            scrollResultados.setPrefViewportHeight(200);
            scrollResultados.setContent(resultadosContainer);

            HBox busquedaLayout = new HBox(10);
            busquedaLayout.getChildren().addAll(campoBusqueda, botonBuscar, botonCancelar);

            contenido.getChildren().addAll(tituloBusqueda, busquedaLayout, scrollResultados);

            botonBuscar.setOnAction(event -> {
                String nombreBuscado = campoBusqueda.getText().trim();

                if (nombreBuscado.isEmpty()) 
                {
                    mostrarAlerta("Advertencia", "Por favor ingrese un nombre para buscar");
                    return;
                }

                resultadosContainer.getChildren().clear();

                List<Empleado> empleadosEncontrados = buscarEmpleadosPorNombre(empleados, nombreBuscado);

                if (empleadosEncontrados.isEmpty()) 
                {
                    Label sinResultados = new Label("No se encontraron empleados con el nombre: " + nombreBuscado);
                    resultadosContainer.getChildren().add(sinResultados);
                } else {
                    Label tituloResultados = new Label("Seleccione el empleado:");
                    tituloResultados.setStyle("-fx-font-weight: bold;");
                    resultadosContainer.getChildren().add(tituloResultados);

                    for (Empleado empleado : empleadosEncontrados) {
                        HBox empleadoRow = registrarEntradaSalida(empleado);
                        resultadosContainer.getChildren().add(empleadoRow);
                    }
                }
            });

            botonCancelar.setOnAction(event -> {
                contenido.getChildren().clear();
            });
        });

        salir.setOnAction(evento -> {
            try {
                Parent menuPrincipal = FXMLLoader.load(getClass().getResource("/GymPOSSF564/vista/guifxml.fxml"));
                Scene escena = salir.getScene();
                escena.setRoot(menuPrincipal);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private List<Empleado> buscarEmpleadosPorNombre(List<Empleado> empleados, String nombre) 
    {
        List<Empleado> resultados = new ArrayList<>();
        for (Empleado empleado : empleados) {
            if (empleado.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(empleado);
            }
        }
        return resultados;
    }

    private VBox crearTarjetaEmpleado(Empleado empleado) 
    {
        VBox tarjeta = new VBox(5);
        tarjeta.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        tarjeta.setPrefWidth(400);

        Label idLabel = new Label("ID: " + empleado.getId());
        Label nombreLabel = new Label("Nombre: " + empleado.getNombre() + " " + empleado.getApellido());
        Label edadLabel = new Label("Edad: " + empleado.getEdad());
        Label direccionLabel = new Label("Direccion: " + empleado.getDireccion());
        Label puestoLabel = new Label("Puesto: " + empleado.getPuesto());
        Label salarioLabel = new Label("Salario: $" + empleado.getSalario());

        idLabel.setStyle("-fx-font-weight: bold;");
        nombreLabel.setStyle("-fx-font-size: 13;");

        tarjeta.getChildren().addAll(idLabel, nombreLabel, edadLabel, direccionLabel, puestoLabel, salarioLabel);

        return tarjeta;
    }

    private void mostrarAlerta(String titulo, String mensaje) 
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private HBox crearFilaEmpleadoParaActualizar(Empleado empleado) 
    {
        HBox fila = new HBox(10);
        fila.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        fila.setPrefWidth(400);

        VBox infoEmpleado = new VBox(5);
        Label idLabel = new Label("ID: " + empleado.getId());
        Label nombreLabel = new Label("Nombre: " + empleado.getNombre() + " " + empleado.getApellido());
        Label puestoLabel = new Label("Puesto: " + empleado.getPuesto());

        idLabel.setStyle("-fx-font-weight: bold;");

        infoEmpleado.getChildren().addAll(idLabel, nombreLabel, puestoLabel);

        Button botonSeleccionar = new Button("Seleccionar");
        botonSeleccionar.setOnAction(event -> {
            mostrarFormularioActualizacion(empleado);
        });

        fila.getChildren().addAll(infoEmpleado, botonSeleccionar);

        return fila;
    }

    private HBox registrarEntradaSalida(Empleado empleado) 
    {
        HBox fila = new HBox(10);
        fila.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        fila.setPrefWidth(400);

        VBox infoEmpleado = new VBox(5);
        Label idLabel = new Label("ID: " + empleado.getId());
        Label nombreLabel = new Label("Nombre: " + empleado.getNombre() + " " + empleado.getApellido());
        Label puestoLabel = new Label("Puesto: " + empleado.getPuesto());

        idLabel.setStyle("-fx-font-weight: bold;");

        infoEmpleado.getChildren().addAll(idLabel, nombreLabel, puestoLabel);

        ControlAccesoEmpleadosFlores acceso_empleados = new ControlAccesoEmpleadosFlores();

        Button botonEntrada = new Button("Entrada");
        Button botonSalida = new Button("Salida");
        botonEntrada.setOnAction(event -> {
            acceso_empleados.registrarEntrada(empleado.getId(), empleado.getNombre(), empleado.getApellido());
            mostrarAlerta("Exito", "Entrada registrada para " + empleado.getNombre());
            contenido.getChildren().clear();
        });
        botonSalida.setOnAction(event -> {
            acceso_empleados.registrarSalida(empleado.getId(), empleado.getNombre(), empleado.getApellido());
            mostrarAlerta("Exito", "Salida registrada para " + empleado.getNombre());
            contenido.getChildren().clear();
        });

        fila.getChildren().addAll(infoEmpleado, botonEntrada, botonSalida);

        return fila;
    }

    private HBox crearFilaEmpleadoEliminar(Empleado empleado) 
    {
        HBox fila = new HBox(10);
        fila.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        fila.setPrefWidth(400);

        VBox infoEmpleado = new VBox(5);
        Label idLabel = new Label("ID: " + empleado.getId());
        Label nombreLabel = new Label("Nombre: " + empleado.getNombre() + " " + empleado.getApellido());
        Label puestoLabel = new Label("Puesto: " + empleado.getPuesto());

        idLabel.setStyle("-fx-font-weight: bold;");

        infoEmpleado.getChildren().addAll(idLabel, nombreLabel, puestoLabel);

        Button botonSeleccionar = new Button("Seleccionar");
        botonSeleccionar.setOnAction(event -> {
            mostrarFormularioEliminar(empleado);
        });

        fila.getChildren().addAll(infoEmpleado, botonSeleccionar);

        return fila;
    }

    private void mostrarFormularioActualizacion(Empleado empleado) 
    {
        contenido.getChildren().clear();

        Label titulo = new Label("Actualizar Empleado - ID: " + empleado.getId());
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        TextField campoNombre = new TextField(empleado.getNombre());
        campoNombre.setPromptText("Nombre");

        TextField campoApellido = new TextField(empleado.getApellido());
        campoApellido.setPromptText("Apellido");

        TextField campoEdad = new TextField(String.valueOf(empleado.getEdad()));
        campoEdad.setPromptText("Edad");

        TextField campoDireccion = new TextField(empleado.getDireccion());
        campoDireccion.setPromptText("Direccion");

        PasswordField campoPassword = new PasswordField();
        campoPassword.setText(empleado.getPasswrd());
        campoPassword.setPromptText("Contraseña");

        TextField campoPuesto = new TextField(empleado.getPuesto());
        campoPuesto.setPromptText("Puesto");

        TextField campoSalario = new TextField(String.valueOf(empleado.getSalario()));
        campoSalario.setPromptText("Salario");

        campoNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                campoNombre.setText(oldValue);
            }
        });

        campoApellido.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                campoApellido.setText(oldValue);
            }
        });

        campoEdad.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                campoEdad.setText(oldValue);
            } else if (newValue.length() > 3) {
                campoEdad.setText(oldValue);
            }
        });

        campoSalario.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                campoSalario.setText(oldValue);
            }
        });

        Button botonGuardar = new Button("Guardar Cambios");
        Button botonCancelar = new Button("Cancelar");

        HBox botonesLayout = new HBox(10);
        botonesLayout.getChildren().addAll(botonGuardar, botonCancelar);

        contenido.getChildren().addAll(
            titulo, campoNombre, campoApellido, campoEdad, campoDireccion,
            campoPassword, campoPuesto, campoSalario, botonesLayout
        );

        botonGuardar.setOnAction(event -> {
            String nuevoNombre = campoNombre.getText().trim();
            String nuevoApellido = campoApellido.getText().trim();
            String nuevaEdad = campoEdad.getText().trim();
            String nuevaDireccion = campoDireccion.getText().trim();
            String nuevaPassword = campoPassword.getText();
            String nuevoPuesto = campoPuesto.getText().trim();
            String nuevoSalario = campoSalario.getText().trim();

            if (nuevoNombre.isEmpty() || nuevoApellido.isEmpty() || nuevaEdad.isEmpty() ||
                nuevaDireccion.isEmpty() || nuevaPassword.isEmpty() || nuevoPuesto.isEmpty() || nuevoSalario.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos son obligatorios");
                return;
            }

            try {
                int edadInt = Integer.parseInt(nuevaEdad);
                double salarioDouble = Double.parseDouble(nuevoSalario);

                gestor.actualizarEmpleado(empleados, nuevoNombre, nuevoApellido, edadInt,
                    nuevaDireccion, empleado.getId(), nuevaPassword, nuevoPuesto, salarioDouble, empleado.isAcceso());
                gestor.serializarEmpleado(empleados);

                mostrarAlerta("Exito", "Empleado actualizado exitosamente");
                contenido.getChildren().clear();
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "Formato numerico invalido en edad o salario");
            }
        });

        botonCancelar.setOnAction(event -> {
            contenido.getChildren().clear();
        });
    }

    private void mostrarFormularioEliminar(Empleado empleado) 
    {
        contenido.getChildren().clear();

        Label titulo = new Label("Eliminar Empleado - ID: " + empleado.getId());
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        Label campoNombre = new Label("Nombre: " + empleado.getNombre());
        Label campoApellido = new Label("Apellidos: " + empleado.getApellido());
        Label campoPuesto = new Label("Puesto: " + empleado.getPuesto());

        Button botonEliminar = new Button("Eliminar");
        Button botonCancelar = new Button("Cancelar");

        HBox botonesLayout = new HBox(10);
        botonesLayout.getChildren().addAll(botonEliminar, botonCancelar);

        contenido.getChildren().addAll(
            titulo, campoNombre, campoApellido, campoPuesto, botonesLayout
        );

        botonEliminar.setOnAction(event -> {
            empleados.remove(empleado);
            gestor.serializarEmpleado(empleados);

            mostrarAlerta("Exito", "Empleado eliminado exitosamente");
            contenido.getChildren().clear();
        });

        botonCancelar.setOnAction(event -> {
            contenido.getChildren().clear();
        });
    }
}

