package fxRekisteri2;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author sampp
 * @version 12.3.2025
 *
 */
public class FxRekisteri2Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("FxRekisteri2GUIView.fxml"));
            final Pane root = ldr.load();
            //final FxRekisteri2GUIController fxrekisteri2Ctrl = (FxRekisteri2GUIController) ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("fxrekisteri2.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("fxRekisteri2");
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei kaytossa
     */
    public static void main(String[] args) {
        launch(args);
    }
}