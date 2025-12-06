package GymPOSSF564.controlador;

import GymPOSSF564.modelo.Inventario;
import GymPOSSF564.modelo.ProductoVendido;
import GymPOSSF564.modelo.GestionInventarioFlores;
import GymPOSSF564.modelo.ControlVentasProductosFlores;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class InventarioMenu 
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

    private List<Inventario> inventario = new ArrayList<>();
    private GestionInventarioFlores gestor = new GestionInventarioFlores();
    private ControlVentasProductosFlores controlVentas = new ControlVentasProductosFlores();

    public void initialize() 
    {
        gestor.cargarInventario(inventario);
        Inventario.inicializarContador(inventario);

        registrar.setOnAction(e -> mostrarRegistrar());
        actualizar.setOnAction(e -> mostrarActualizar());
        buscar.setOnAction(e -> mostrarBuscar());
        eliminar.setOnAction(e -> mostrarEliminar());
        entradaSalida.setOnAction(e -> mostrarVentas());
        salir.setOnAction(evento -> 
        {
            try 
            {
                Parent menuPrincipal = FXMLLoader.load(getClass().getResource("/GymPOSSF564/vista/guifxml.fxml"));
                Scene escena = salir.getScene();
                escena.setRoot(menuPrincipal);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    private void mostrarRegistrar() 
    {
        contenido.getChildren().clear();
        Label titulo = new Label("Registrar producto");
        Label id = new Label("ID: " + Inventario.getContador_id());
        TextField nombre = new TextField();
        TextField descripcion = new TextField();
        TextField cantidad = new TextField();
        TextField precio = new TextField();

        MenuItem cat1 = new MenuItem("Suplementos");
        MenuItem cat2 = new MenuItem("Accesorios");
        MenuItem cat3 = new MenuItem("Higiene");
        
        MenuButton categoria = new MenuButton("Categoría", null, cat1, cat2, cat3);
        
        Button aceptar = new Button("Aceptar");
        Button cancelar = new Button("Cancelar");
        
        String[] categoriaSeleccionada = {"nulo"};
        nombre.setPromptText("Nombre del producto");
        descripcion.setPromptText("Descripcion");
        cantidad.setPromptText("Cantidad");
        precio.setPromptText("Precio unitario");

        cantidad.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) cantidad.setText(oldValue);
        });

        precio.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) precio.setText(oldValue);
        });

        cat1.setOnAction(e -> {
            categoria.setText("Suplementos");
            categoriaSeleccionada[0] = "Suplementos";
        });
        cat2.setOnAction(e -> {
            categoria.setText("Accesorios");
            categoriaSeleccionada[0] = "Accesorios";
        });
        cat3.setOnAction(e -> {
            categoria.setText("Higiene");
            categoriaSeleccionada[0] = "Higiene";
        });

        contenido.getChildren().addAll(titulo, id, nombre, descripcion, categoria, cantidad, precio, aceptar, cancelar);

        aceptar.setOnAction(e -> 
        {
            if (nombre.getText().isEmpty()
                || descripcion.getText().isEmpty()
                || categoriaSeleccionada[0].equals("nulo")
                || cantidad.getText().isEmpty()
                || precio.getText().isEmpty()) 
            {

                mostrarAlerta("Error", "Todos los campos son obligatorios.");
                return;
            }

            gestor.agregarInventario(
                inventario,
                nombre.getText(),
                descripcion.getText(),
                categoriaSeleccionada[0],
                Integer.parseInt(cantidad.getText()),
                Double.parseDouble(precio.getText())
            );

            Inventario.incrementarContador();
            mostrarAlerta("Exito", "Producto agregado correctamente.");
            contenido.getChildren().clear();
        });

        cancelar.setOnAction(e -> contenido.getChildren().clear());
    }
    
    private void mostrarActualizar() 
    {
        contenido.getChildren().clear();

        Label titulo = new Label("Actualizar producto");
        TextField idBuscar = new TextField();
        idBuscar.setPromptText("ID del producto");

        Button buscarBtn = new Button("Buscar");
        contenido.getChildren().addAll(titulo, idBuscar, buscarBtn);

        buscarBtn.setOnAction(e -> {
            int id;
            try { id = Integer.parseInt(idBuscar.getText()); }
            catch (Exception ex) { mostrarAlerta("Error", "ID no válido."); return; }

            Inventario encontrado = inventario.stream()
                    .filter(p -> p.getProducto_id() == id)
                    .findFirst().orElse(null);

            if (encontrado == null) {
                mostrarAlerta("Error", "Producto no encontrado.");
                return;
            }

            mostrarFormularioActualizar(encontrado);
        });
    }

    private void mostrarFormularioActualizar(Inventario prod) 
    {
        contenido.getChildren().clear();

        Label titulo = new Label("Editar producto ID " + prod.getProducto_id());

        TextField nombre = new TextField(prod.getNombre());
        TextField descripcion = new TextField(prod.getDescripcion());
        TextField cantidad = new TextField(String.valueOf(prod.getCantidad()));
        TextField precio = new TextField(String.valueOf(prod.getPrecioUnitario()));

        MenuItem c1 = new MenuItem("Suplementos");
        MenuItem c2 = new MenuItem("Accesorios");
        MenuItem c3 = new MenuItem("Higiene");

        MenuButton categoria = new MenuButton(prod.getCategoria(), null, c1, c2, c3);

        String[] categoriaSeleccionada = {prod.getCategoria()};

        c1.setOnAction(e -> {
            categoria.setText("Suplementos");
            categoriaSeleccionada[0] = "Suplementos";
        });
        c2.setOnAction(e -> {
            categoria.setText("Accesorios");
            categoriaSeleccionada[0] = "Accesorios";
        });
        c3.setOnAction(e -> {
            categoria.setText("Higiene");
            categoriaSeleccionada[0] = "Higiene";
        });

        Button aceptar = new Button("Guardar cambios");
        Button cancelar = new Button("Cancelar");

        contenido.getChildren().addAll(
            titulo, nombre, descripcion, categoria, cantidad, precio, aceptar, cancelar
        );

        aceptar.setOnAction(e -> {
            gestor.actualizarInventario(
                inventario,
                prod.getProducto_id(),
                nombre.getText(),
                descripcion.getText(),
                categoriaSeleccionada[0],
                Integer.parseInt(cantidad.getText()),
                Double.parseDouble(precio.getText())
            );

            mostrarAlerta("Exito", "Producto actualizado.");
            contenido.getChildren().clear();
        });

        cancelar.setOnAction(e -> contenido.getChildren().clear());
    }

    
    private void mostrarBuscar() 
    {
        contenido.getChildren().clear();

        Label titulo = new Label("Buscar producto");
        TextField idBuscar = new TextField();
        idBuscar.setPromptText("ID del producto");
        Button aceptar = new Button("Buscar");

        contenido.getChildren().addAll(titulo, idBuscar, aceptar);

        aceptar.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idBuscar.getText());
                Inventario encontrado = inventario.stream()
                        .filter(p -> p.getProducto_id() == id)
                        .findFirst().orElse(null);

                if (encontrado == null) {
                    mostrarAlerta("No encontrado", "El producto no existe.");
                    return;
                }

                mostrarAlerta("Producto encontrado", encontrado.toString());
            } catch (Exception ex) {
                mostrarAlerta("Error", "ID no valido.");
            }
        });
    }
    
    private void mostrarEliminar() 
    {
        contenido.getChildren().clear();

        Label titulo = new Label("Eliminar producto");
        TextField idBuscar = new TextField();
        idBuscar.setPromptText("ID del producto");
        Button aceptar = new Button("Eliminar");

        contenido.getChildren().addAll(titulo, idBuscar, aceptar);

        aceptar.setOnAction(e -> {

            int id;
            try { id = Integer.parseInt(idBuscar.getText()); }
            catch (Exception ex) { mostrarAlerta("Error", "ID no valido."); return; }

            Iterator<Inventario> it = inventario.iterator();
            boolean eliminado = false;

            while (it.hasNext()) 
            {
                if (it.next().getProducto_id() == id) {
                    it.remove();
                    eliminado = true;
                    break;
                }
            }

            if (!eliminado) {
                mostrarAlerta("Error", "Producto no encontrado.");
                return;
            }

            gestor.serializarInventario(inventario);
            mostrarAlerta("Exito", "Producto eliminado.");
            contenido.getChildren().clear();
        });
    }

    private void mostrarVentas() 
    {
        List<ProductoVendido> ventas = new ArrayList<>();
        contenido.getChildren().clear();

        Label titulo = new Label("Registrar venta");
        TextField cliente = new TextField();
        cliente.setPromptText("Nombre del cliente");

        TextField idProducto = new TextField();
        idProducto.setPromptText("ID del producto");
        TextField cantidad = new TextField();
        cantidad.setPromptText("Cantidad");

        Button registrar = new Button("Registrar");

        contenido.getChildren().addAll(titulo, cliente, idProducto, cantidad, registrar);

        registrar.setOnAction(e -> {
            if (cliente.getText().isEmpty() || idProducto.getText().isEmpty() || cantidad.getText().isEmpty()) {
                mostrarAlerta("Error", "Todos los campos son obligatorios.");
                return;
            }

            try {
                int id = Integer.parseInt(idProducto.getText());
                int cant = Integer.parseInt(cantidad.getText());

                Inventario prod = inventario.stream()
                        .filter(p -> p.getProducto_id() == id)
                        .findFirst().orElse(null);

                if (prod == null) {
                    mostrarAlerta("Error", "Producto no encontrado.");
                    return;
                }

                if (prod.getCantidad() < cant) {
                    mostrarAlerta("Error", "No hay suficiente inventario.");
                    return;
                }
                
                ProductoVendido venta = new ProductoVendido(prod, cant);
                ventas.add(venta);

                controlVentas.registrarVenta(1, cliente.getText(), ventas);

                mostrarAlerta("Exito", "Venta registrada correctamente.");
                contenido.getChildren().clear();

            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "ID y cantidad deben ser numeros.");
            }
        });
    }
    
    private void mostrarAlerta(String titulo, String msg) 
    {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
}