package MainAPP;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Objects;

public class Main extends Application 
{
    private static Stage stagePrincipal;
    
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        stagePrincipal = primaryStage;

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/GymPOSSF564/vista/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        URL url = Main.class.getResource("/Recursos1/style/login.css");
        System.out.println("URL CSS = " + url);

        String css = Objects.requireNonNull(
                Main.class.getResource("/Recursos1/style/login.css")
        ).toExternalForm();

        scene.getStylesheets().add(css);

        primaryStage.setScene(scene);
        primaryStage.setTitle("GymPOSSF564");
        primaryStage.show();
    }
    
    public static void cambiarEscena(String fxml, String titulo) 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/GymPOSSF564/vista/" + fxml));
            Parent root = loader.load();
            Scene nuevaEscena = new Scene(root);
            
            stagePrincipal.setScene(nuevaEscena);
            stagePrincipal.setTitle(titulo);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
