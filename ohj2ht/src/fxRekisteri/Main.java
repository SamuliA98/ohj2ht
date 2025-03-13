package fxRekisteri;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import rekisteri.Rekisteri;

/**
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 04062018
 *
 * Ohjelman käynnistämiseen tarvittava pääohjelma.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("KauppaGUIView.fxml"));
            final Pane root = (Pane) ldr.load();
            final KauppaGUIController kauppaCtrl = (KauppaGUIController) ldr.getController();
            final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("kauppa.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Rekisteri");

            primaryStage.setOnCloseRequest((event) -> {
                if (!kauppaCtrl.voikoSulkea()) {
                    event.consume();
                }
            });

            Rekisteri rekisteri = new Rekisteri();
            kauppaCtrl.setRekisteri(rekisteri);

            primaryStage.show();

            Application.Parameters params = getParameters();
            if (params.getRaw().size() > 0) {
                kauppaCtrl.lueTiedosto(params.getRaw().get(0));
            } else if (!kauppaCtrl.Avaa()) {
                Platform.exit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Käyttöliittymän käynnistys
     * @param args komentorivin parametreja
     */
    public static void main(String[] args) {
        launch(args);
    }
}