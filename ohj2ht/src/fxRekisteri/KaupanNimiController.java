package fxRekisteri;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Dialogi autokaupan nimen kysymistä varten.
 * 
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 04062018
 */
public class KaupanNimiController implements ModalControllerInterface<String> {
    
    @FXML private TextField textVastaus;
    private String vastaus = null;
    
    @FXML private void handleOk() {
        vastaus = textVastaus.getText();
        ModalController.closeStage(textVastaus);
    }
    
    @FXML private void handlePeruuta() {
        ModalController.closeStage(textVastaus);
    }
    
    /**
     * Autokaupan nimen kysymisen dialogi, palautetaan syötetty nimi tai null.
     * @param modalityStage mille modaalisia
     * @param oletus mikä oletusnimenä
     * @return kirjoitettu nimi, Peruuta-näppäintä painettaessa null
     */
    public static String kysyNimi(Stage modalityStage, String oletus) {
         return ModalController.showModal(
                 KaupanNimiController.class.getResource("KirjautumisGUIView.fxml"),
                 "Autokauppa",
                 modalityStage, oletus);
    }

    @Override
    public String getResult() {
        return vastaus;
    }

    /**
     * Mitä tehdään, kun dialogi on täytetty.
     */
    @Override
    public void handleShown() {
        textVastaus.requestFocus();
        
    }

    @Override
    public void setDefault(String oletus) {
        textVastaus.setText(oletus);
        
    }
}