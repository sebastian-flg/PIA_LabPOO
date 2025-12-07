package GymPOSSF564.controlador;

import GymPOSSF564.modelo.Cliente;
import GymPOSSF564.modelo.ControlAccesoClientesFlores;
import GymPOSSF564.modelo.GestionClientesFlores;
import GymPOSSF564.modelo.Membresia;
import GymPOSSF564.modelo.ProcesadorPagos564;
import GymPOSSF564.modelo.SistemaMembresias0112;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ClientesMenu
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
    
    private List<Cliente> clientes = new ArrayList<>();
    private GestionClientesFlores gestor = new GestionClientesFlores();
    
    public void initialize() 
    {
        gestor.cargarClientes(clientes);
        
        Cliente.inicializarContador(clientes);
        
        registrar.setOnAction(evento -> {
            contenido.getChildren().clear();
            
            MenuItem bronce = new MenuItem("Membresia bronce");
            MenuItem plata = new MenuItem("Membresia plata");
            MenuItem oro = new MenuItem("Membresia oro");
            MenuItem efectivo = new MenuItem("Efectivo");
            MenuItem tarjeta = new MenuItem("Tarjeta");
            
            Label indicador_menu_clientes = new Label("Registrar cliente");
            Label id = new Label("ID: " + String.valueOf(Cliente.getContadorId()));
            TextField nombres = new TextField();
            TextField apellidos = new TextField();
            TextField telefono = new TextField();
            MenuButton membresia = new MenuButton("Membresia", null, bronce, plata, oro);
            MenuButton pago = new MenuButton("Metodo de pago", null, efectivo, tarjeta);
            Button aceptar = new Button("Aceptar");
            Button cancelar = new Button("Cancelar");
            String[] membresia_seleccionada = {"nulo"};
            String[] pago_seleccionado = {"nulo"};
            
            nombres.setPromptText("Nombre");
            apellidos.setPromptText("Apellidos");
            telefono.setPromptText("Telefono");
            
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
            
            telefono.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    telefono.setText(oldValue);
                } else if (newValue.length() > 10) {
                    telefono.setText(oldValue);
                }
            });
            
            contenido.getChildren().add(indicador_menu_clientes);
            contenido.getChildren().add(id);
            contenido.getChildren().add(nombres);
            contenido.getChildren().add(apellidos);
            contenido.getChildren().add(telefono);
            contenido.getChildren().add(membresia);
            contenido.getChildren().add(pago);
            contenido.getChildren().add(aceptar);
            contenido.getChildren().add(cancelar);
            
            
            bronce.setOnAction(event -> {
                membresia.setText("Membresia bronce");
                membresia_seleccionada[0] = "Bronce";
            });
            
            plata.setOnAction(event -> {
                membresia.setText("Membresia plata");
                membresia_seleccionada[0] = "Plata";
            });
            
            oro.setOnAction(event -> {
                membresia.setText("Membresia oro");
                membresia_seleccionada[0] = "Oro";
            });
            
            efectivo.setOnAction(event -> {
                pago.setText("Efectivo");
                pago_seleccionado[0] = "efectivo";
            });
            
            tarjeta.setOnAction(event -> {
                pago.setText("Tarjeta");
                pago_seleccionado[0] = "tarjeta";
            });
            
            aceptar.setOnAction(event -> {
                String nombre_cliente = nombres.getText();
                String apellidos_cliente = apellidos.getText();
                String telefono_cliente = telefono.getText();
                int id_cliente = Cliente.getContadorId();
                boolean find = false;
                
                if (nombre_cliente.trim().isEmpty()) {
                    mostrarAlerta("Error", "El nombre es obligatorio");
                    return;
                }
                
                if (apellidos_cliente.trim().isEmpty()) {
                    mostrarAlerta("Error", "Los apellidos son obligatorios");
                    return;
                }
                
                if (telefono_cliente.trim().isEmpty()) {
                    mostrarAlerta("Error", "El teléfono es obligatorio");
                    return;
                }
                
                if (telefono_cliente.length() < 10) {
                    mostrarAlerta("Error", "El teléfono debe tener 10 dígitos");
                    return;
                }
                
                id.setText("ID: " + String.valueOf(id_cliente));
                

                for(Cliente c: clientes){
                    System.out.println("c.getId_cliente(): " + c.getCliente_id() + ", id_cliente: " + id_cliente);
                    if(c.getCliente_id() == id_cliente){
                        find = true;
                        System.out.println("ID ya ingresado");
                        break;
                    }
                }
                
                if(!find && !(pago_seleccionado[0].equalsIgnoreCase("nulo") || membresia_seleccionada[0].equalsIgnoreCase("nulo")) ){
                    contenido.getChildren().removeIf(node -> 
                        (node instanceof TextField || node instanceof Button || node instanceof MenuButton)
                    );
                    
                    SistemaMembresias0112 sistema_m = new SistemaMembresias0112();
                    List<Membresia> membresias = new ArrayList<>();
                    double precio;
                    int puntos;

                    if(membresia_seleccionada[0].equals("Oro")){
                        precio = sistema_m.precio_oro;
                        puntos = sistema_m.puntos_oro;
                    } else {
                        if(membresia_seleccionada[0].equals("Plata")){
                            precio = sistema_m.precio_plata;
                            puntos = sistema_m.puntos_plata;
                        } else {
                            precio = sistema_m.precio_bronce;
                            puntos = sistema_m.puntos_bronce;
                        }
                    }
                    
                    Label monto = new Label("Monto: $" + String.valueOf(precio));
                    TextField descuento = new TextField("0"); 
                    Button aceptar_monto = new Button("Aceptar");
                    Button cancelar_monto = new Button("Cancelar");
                    
                    descuento.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (!newValue.matches("\\d*(\\.\\d*)?")) {
                            descuento.setText(oldValue);
                        }
                    });
                    
                    contenido.getChildren().add(monto);
                    contenido.getChildren().add(descuento);
                    
                    if(pago_seleccionado[0].equals("tarjeta")){
                        TextField num_tarjeta = new TextField();
                        TextField fecha_cad = new TextField();
                        TextField nombre_titular = new TextField();
                        
                        num_tarjeta.setPromptText("Número de tarjeta (16 dígitos)");
                        fecha_cad.setPromptText("MM/YY");
                        nombre_titular.setPromptText("Nombre del titular");
                        
                        num_tarjeta.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!newValue.matches("\\d*")) {
                                num_tarjeta.setText(oldValue);
                            } else if (newValue.length() > 16) {
                                num_tarjeta.setText(oldValue);
                            }
                        });
                        
                        fecha_cad.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!newValue.matches("\\d{0,2}/?\\d{0,2}")) {
                                fecha_cad.setText(oldValue);
                            } else if (newValue.length() > 5) {
                                fecha_cad.setText(oldValue);
                            }
                        });
                        
                        nombre_titular.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                                nombre_titular.setText(oldValue);
                            }
                        });
                        
                        contenido.getChildren().add(num_tarjeta);
                        contenido.getChildren().add(fecha_cad);
                        contenido.getChildren().add(nombre_titular);
                    }
                    
                    contenido.getChildren().add(aceptar_monto);
                    contenido.getChildren().add(cancelar_monto);

                    aceptar_monto.setOnAction(even -> {
                        
                        if (pago_seleccionado[0].equals("tarjeta")) 
                        {
                            Node numTarjetaNode = contenido.getChildren().stream()
                                .filter(node -> node instanceof TextField && ((TextField)node).getPromptText() != null 
                                    && ((TextField)node).getPromptText().contains("tarjeta"))
                                .findFirst().orElse(null);
                            Node fechaCadNode = contenido.getChildren().stream()
                                .filter(node -> node instanceof TextField && ((TextField)node).getPromptText() != null 
                                    && ((TextField)node).getPromptText().contains("MM/YY"))
                                .findFirst().orElse(null);
                            Node nombreTitularNode = contenido.getChildren().stream()
                                .filter(node -> node instanceof TextField && ((TextField)node).getPromptText() != null 
                                    && ((TextField)node).getPromptText().contains("titular"))
                                .findFirst().orElse(null);
                            
                            if (numTarjetaNode != null && fechaCadNode != null && nombreTitularNode != null) 
                            {
                                TextField numTarjetaField = (TextField) numTarjetaNode;
                                TextField fechaCadField = (TextField) fechaCadNode;
                                TextField nombreTitularField = (TextField) nombreTitularNode;
                                
                                if (numTarjetaField.getText().length() != 16) 
                                {
                                    mostrarAlerta("Error", "El número de tarjeta debe tener 16 dígitos");
                                    return;
                                }
                                
                                if (fechaCadField.getText().length() != 5 || !fechaCadField.getText().matches("\\d{2}/\\d{2}")) 
                                {
                                    mostrarAlerta("Error", "La fecha de caducidad debe tener el formato MM/YY");
                                    return;
                                }
                                
                                if (nombreTitularField.getText().trim().isEmpty()) 
                                {
                                    mostrarAlerta("Error", "El nombre del titular es obligatorio");
                                    return;
                                }
                            }
                        }
                        
                        double descuentoValue;
                        try {
                            descuentoValue = Double.parseDouble(descuento.getText());
                            if (descuentoValue < 0 || descuentoValue > 100) {
                                mostrarAlerta("Error", "El descuento debe estar entre 0 y 100");
                                return;
                            }
                        } catch (NumberFormatException e) {
                            mostrarAlerta("Error", "El descuento debe ser un número valido");
                            return;
                        }
                        
                        Cliente c_nuevo = new Cliente(nombre_cliente, apellidos_cliente, telefono_cliente);
                        System.out.println("Puntos de 'puntos': " + puntos);
                        c_nuevo.setPuntos(puntos);
                        System.out.println("Puntos de 'c_nuevo': " + c_nuevo.getPuntos());
                        ProcesadorPagos564 procesar_pago = new ProcesadorPagos564();
                        String total = String.valueOf(precio * (1 - (Double.parseDouble(descuento.getText()) / 100)));
                        

                        sistema_m.cargarMembresias(membresias);
                        sistema_m.agregarMembresia(membresias, id_cliente, membresia_seleccionada[0]);
                        procesar_pago.procesarPago(Double.parseDouble(total));
                        procesar_pago.guardarFactura(c_nuevo.getCliente_id(), nombre_cliente, apellidos_cliente, String.valueOf(precio), membresia_seleccionada[0], descuento.getText(), total, pago_seleccionado[0]);
                        gestor.agregarClientes(clientes, c_nuevo);
                        Cliente.incrementarContador();
                        cancelar.fire();
                    });

                    cancelar_monto.setOnAction(even -> {
                        cancelar.fire();
                    });
                } else {
                    if (membresia_seleccionada[0].equalsIgnoreCase("nulo")) {
                        mostrarAlerta("Error", "Debe seleccionar una membresia");
                    }
                    if (pago_seleccionado[0].equalsIgnoreCase("nulo")) {
                        mostrarAlerta("Error", "Debe seleccionar un metodo de pago");
                    }
                }
            });
            
            cancelar.setOnAction(event -> {
                contenido.getChildren().clear();
            });
            
        });
        
        actualizar.setOnAction(evento -> {
            contenido.getChildren().clear();
            
            Label tituloBusqueda = new Label("Buscar cliente a actualizar");
            TextField campoBusqueda = new TextField();
            campoBusqueda.setPromptText("Ingrese nombre del cliente...");
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

                List<Cliente> clientesEncontrados = buscarClientesPorNombre(clientes, nombreBuscado);

                if (clientesEncontrados.isEmpty()) 
                {
                    Label sinResultados = new Label("No se encontraron clientes con el nombre: " + nombreBuscado);
                    resultadosContainer.getChildren().add(sinResultados);
                } else 
                {
                    Label tituloResultados = new Label("Seleccione el cliente a actualizar:");
                    tituloResultados.setStyle("-fx-font-weight: bold;");
                    resultadosContainer.getChildren().add(tituloResultados);

                    for (Cliente cliente : clientesEncontrados) 
                    {
                        HBox clienteRow = crearFilaClienteParaActualizar(cliente);
                        resultadosContainer.getChildren().add(clienteRow);
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
            campoBusqueda.setPromptText("Ingrese nombre del cliente...");
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
                
                List<Cliente> clientesEncontrados = buscarClientesPorNombre(clientes, nombreBuscado);

                if (clientesEncontrados.isEmpty()) 
                {
                    Label sinResultados = new Label("No se encontraron clientes con el nombre: " + nombreBuscado);
                    resultadosContainer.getChildren().add(sinResultados);
                } else 
                {
                    Label tituloResultados = new Label("Resultados encontrados (" + clientesEncontrados.size() + "):");
                    tituloResultados.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
                    resultadosContainer.getChildren().add(tituloResultados);

                    for (Cliente cliente : clientesEncontrados) 
                    {
                        VBox clienteCard = crearTarjetaCliente(cliente);
                        resultadosContainer.getChildren().add(clienteCard);
                    }
                }
            });

            botonCancelar.setOnAction(event -> {
                contenido.getChildren().clear();
            });
        });
        
        eliminar.setOnAction(evento -> {
            contenido.getChildren().clear();
            
            Label tituloBusqueda = new Label("Buscar cliente a eliminar");
            TextField campoBusqueda = new TextField();
            campoBusqueda.setPromptText("Ingrese nombre del cliente...");
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

                List<Cliente> clientesEncontrados = buscarClientesPorNombre(clientes, nombreBuscado);

                if (clientesEncontrados.isEmpty()) 
                {
                    Label sinResultados = new Label("No se encontraron clientes con el nombre: " + nombreBuscado);
                    resultadosContainer.getChildren().add(sinResultados);
                } else 
                {
                    Label tituloResultados = new Label("Seleccione el cliente a eliminar:");
                    tituloResultados.setStyle("-fx-font-weight: bold;");
                    resultadosContainer.getChildren().add(tituloResultados);

                    for (Cliente cliente : clientesEncontrados) 
                    {
                        HBox clienteRow = crearFilaClienteEliminar(cliente);
                        resultadosContainer.getChildren().add(clienteRow);
                    }
                }
            });

            botonCancelar.setOnAction(event -> {
                contenido.getChildren().clear();
            });
        });
        
        entradaSalida.setOnAction(evento -> {
            contenido.getChildren().clear();
            
            Label tituloBusqueda = new Label("Entrada / Salida del cliente");
            TextField campoBusqueda = new TextField();
            campoBusqueda.setPromptText("Ingrese nombre del cliente...");
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

                List<Cliente> clientesEncontrados = buscarClientesPorNombre(clientes, nombreBuscado);

                if (clientesEncontrados.isEmpty()) 
                {
                    Label sinResultados = new Label("No se encontraron clientes con el nombre: " + nombreBuscado);
                    resultadosContainer.getChildren().add(sinResultados);
                } else 
                {
                    Label tituloResultados = new Label("Seleccione el cliente:");
                    tituloResultados.setStyle("-fx-font-weight: bold;");
                    resultadosContainer.getChildren().add(tituloResultados);

                    for (Cliente cliente : clientesEncontrados) 
                    {
                        HBox clienteRow = registrarEntradaSalida(cliente);
                        resultadosContainer.getChildren().add(clienteRow);
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
    
    private List<Cliente> buscarClientesPorNombre(List<Cliente> clientes, String nombre) 
    {
        List<Cliente> resultados = new ArrayList<>();
        for (Cliente cliente : clientes) 
        {
            if (cliente.getNombres().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(cliente);
            }
        }
        return resultados;
    }

    private VBox crearTarjetaCliente(Cliente cliente) 
    {
        VBox tarjeta = new VBox(5);
        tarjeta.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        tarjeta.setPrefWidth(400);
        
        SistemaMembresias0112 sistema_m = new SistemaMembresias0112();
        List<Membresia> membresia = new ArrayList<>();
        
        sistema_m.cargarMembresias(membresia);
        Membresia membresia_cli = sistema_m.buscarMembresia(membresia, cliente.getCliente_id());
        
        Label idLabel = new Label("ID: " + cliente.getCliente_id());
        Label nombreLabel = new Label("Nombre: " + cliente.getNombres() + " " + cliente.getApellidos());
        Label telefonoLabel = new Label("Telefono: " + cliente.getNum_telefono());
        Label membresiaLabel = new Label("Membresia: " + membresia_cli.getTipo_membresia());
        Label puntosLabel = new Label("Puntos: " + cliente.getPuntos());

        idLabel.setStyle("-fx-font-weight: bold;");
        nombreLabel.setStyle("-fx-font-size: 13;");

        tarjeta.getChildren().addAll(idLabel, nombreLabel, telefonoLabel, membresiaLabel, puntosLabel);

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
    
    private HBox crearFilaClienteParaActualizar(Cliente cliente) 
    {
        HBox fila = new HBox(10);
        fila.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        fila.setPrefWidth(400);

        VBox infoCliente = new VBox(5);
        Label idLabel = new Label("ID: " + cliente.getCliente_id());
        Label nombreLabel = new Label("Nombre: " + cliente.getNombres() + " " + cliente.getApellidos());
        Label telefonoLabel = new Label("Telefono: " + cliente.getNum_telefono());

        idLabel.setStyle("-fx-font-weight: bold;");

        infoCliente.getChildren().addAll(idLabel, nombreLabel, telefonoLabel);

        Button botonSeleccionar = new Button("Seleccionar");
        botonSeleccionar.setOnAction(event -> {
            mostrarFormularioActualizacion(cliente);
        });

        fila.getChildren().addAll(infoCliente, botonSeleccionar);

        return fila;
    }
    
    private HBox registrarEntradaSalida(Cliente cliente) 
    {
        HBox fila = new HBox(10);
        fila.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        fila.setPrefWidth(400);

        VBox infoCliente = new VBox(5);
        Label idLabel = new Label("ID: " + cliente.getCliente_id());
        Label nombreLabel = new Label("Nombre: " + cliente.getNombres() + " " + cliente.getApellidos());
        Label telefonoLabel = new Label("Telefono: " + cliente.getNum_telefono());

        idLabel.setStyle("-fx-font-weight: bold;");

        infoCliente.getChildren().addAll(idLabel, nombreLabel, telefonoLabel);
        
        ControlAccesoClientesFlores acceso_clientes = new ControlAccesoClientesFlores();
        
        Button botonEntrada = new Button("Entrada");
        Button botonSalida = new Button("Salida");
        botonEntrada.setOnAction(event -> {
            acceso_clientes.registrarEntrada(cliente.getCliente_id(), cliente.getNombres(), cliente.getApellidos());
            contenido.getChildren().clear();
        });
        botonSalida.setOnAction(event -> {
            acceso_clientes.registrarSalida(cliente.getCliente_id(), cliente.getNombres(), cliente.getApellidos());
            contenido.getChildren().clear();
        });

        fila.getChildren().addAll(infoCliente, botonEntrada, botonSalida);

        return fila;
    }
    
    private HBox crearFilaClienteEliminar(Cliente cliente) 
    {
        HBox fila = new HBox(10);
        fila.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        fila.setPrefWidth(400);

        VBox infoCliente = new VBox(5);
        Label idLabel = new Label("ID: " + cliente.getCliente_id());
        Label nombreLabel = new Label("Nombre: " + cliente.getNombres() + " " + cliente.getApellidos());
        Label telefonoLabel = new Label("Telefono: " + cliente.getNum_telefono());

        idLabel.setStyle("-fx-font-weight: bold;");

        infoCliente.getChildren().addAll(idLabel, nombreLabel, telefonoLabel);

        Button botonSeleccionar = new Button("Seleccionar");
        botonSeleccionar.setOnAction(event -> {
            mostrarFormularioEliminar(cliente);
        });

        fila.getChildren().addAll(infoCliente, botonSeleccionar);

        return fila;
    }
    
    private void mostrarFormularioActualizacion(Cliente cliente) 
    {
        contenido.getChildren().clear();
        
        SistemaMembresias0112 sistema_m = new SistemaMembresias0112();
        List<Membresia> membresias = new ArrayList<>();
        
        sistema_m.cargarMembresias(membresias);
        
        Membresia membresia_cli = sistema_m.buscarMembresia(membresias, cliente.getCliente_id());
        
        Label titulo = new Label("Actualizar Cliente - ID: " + cliente.getCliente_id());
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        TextField campoNombres = new TextField(cliente.getNombres());
        campoNombres.setPromptText("Nombres");

        TextField campoApellidos = new TextField(cliente.getApellidos());
        campoApellidos.setPromptText("Apellidos");

        TextField campoTelefono = new TextField(cliente.getNum_telefono());
        campoTelefono.setPromptText("Telefono");
        
        campoNombres.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                campoNombres.setText(oldValue);
            }
        });
        
        campoApellidos.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                campoApellidos.setText(oldValue);
            }
        });
        
        campoTelefono.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                campoTelefono.setText(oldValue);
            } else if (newValue.length() > 10) {
                campoTelefono.setText(oldValue);
            }
        });
        
        MenuItem bronce = new MenuItem("Membresia bronce");
        MenuItem plata = new MenuItem("Membresia plata");
        MenuItem oro = new MenuItem("Membresia oro");
        MenuButton membresia = new MenuButton("Membresia " + membresia_cli.getTipo_membresia(), null, bronce, plata, oro);
        String[] membresia_seleccionada = {membresia_cli.getTipo_membresia()};
        
        bronce.setOnAction(event -> {
            membresia.setText("Membresia bronce");
            membresia_seleccionada[0] = "Bronce";
        });

        plata.setOnAction(event -> {
            membresia.setText("Membresia plata");
            membresia_seleccionada[0] = "Plata";
        });

        oro.setOnAction(event -> {
            membresia.setText("Membresia oro");
            membresia_seleccionada[0] = "Oro";
        });
        
        Button botonGuardar = new Button("Guardar Cambios");
        Button botonCancelar = new Button("Cancelar");

        HBox botonesLayout = new HBox(10);
        botonesLayout.getChildren().addAll(botonGuardar, botonCancelar);

        contenido.getChildren().addAll(
            titulo, campoNombres, campoApellidos, campoTelefono, membresia, botonesLayout
        );

        botonGuardar.setOnAction(event -> {
            String nuevosNombres = campoNombres.getText().trim();
            String nuevosApellidos = campoApellidos.getText().trim();
            String nuevoTelefono = campoTelefono.getText().trim();

            if (nuevosNombres.isEmpty() || nuevosApellidos.isEmpty() || nuevoTelefono.isEmpty()) 
            {
                mostrarAlerta("Error", "Todos los campos son obligatorios");
                return;
            }
            
            if (nuevoTelefono.length() < 10) 
            {
                mostrarAlerta("Error", "El telefono debe tener 10 digitos");
                return;
            }

            for (int i = 0; i < clientes.size(); i++) 
            {
                if (clientes.get(i).getCliente_id() == cliente.getCliente_id()) 
                {
                    Cliente clienteActualizado = new Cliente(nuevosNombres, nuevosApellidos, nuevoTelefono);
                    Membresia membresiaNueva = new Membresia(cliente.getCliente_id(), membresia_seleccionada[0], membresia_cli.getInicio(), membresia_cli.getFin());
                    clienteActualizado.setCliente_id(cliente.getCliente_id());
                    clienteActualizado.setPuntos(cliente.getPuntos());
                    
                    clientes.set(i, clienteActualizado);
                    for (int j = 0; j < membresias.size(); j++) 
                    {
                        if (membresias.get(j).getCliente_id() == cliente.getCliente_id())
                            membresias.set(j, membresiaNueva);
                    }
                    
                    gestor.actualizarCliente(clientes, cliente.getCliente_id(), nuevosNombres, nuevosApellidos, nuevoTelefono);
                    gestor.serializarCliente(clientes);
                    sistema_m.actualizarMembresia(membresias, cliente.getCliente_id(), membresia_seleccionada[0], membresia_cli.getInicio(), membresia_cli.getFin());
                    sistema_m.serializarMembresia(membresias);

                    System.out.println("Cliente actualizado exitosamente");
                    break;
                }
            }

            contenido.getChildren().clear();
        });

        botonCancelar.setOnAction(event -> {
            contenido.getChildren().clear();
        });
    }
    
    private void mostrarFormularioEliminar(Cliente cliente) 
    {
        contenido.getChildren().clear();

        Label titulo = new Label("Eliminar Cliente - ID: " + cliente.getCliente_id());
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        
        Label campoNombres = new Label("Nombre: " + cliente.getNombres());
        Label campoApellidos = new Label("Apellidos: " + cliente.getApellidos());
        Label campoTelefono = new Label("Telefono: " + cliente.getNum_telefono());

        Button botonEliminar = new Button("Eliminar");
        Button botonCancelar = new Button("Cancelar");

        HBox botonesLayout = new HBox(10);
        botonesLayout.getChildren().addAll(botonEliminar, botonCancelar);

        contenido.getChildren().addAll(
            titulo, campoNombres, campoApellidos, campoTelefono, botonesLayout
        );

        botonEliminar.setOnAction(event -> {
            clientes.remove(cliente);
            gestor.serializarCliente(clientes);
            
            contenido.getChildren().clear();
        });

        botonCancelar.setOnAction(event -> {
            contenido.getChildren().clear();
        });
    }
    
}
