package fxRekisteri;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Luokka, joka hoitaa tulostamisen
 * 
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 05062018
 *
 */
public class TulostusController implements ModalControllerInterface<String> {

    @FXML
    TextArea tulostusAlue;

    @FXML
    private void handleOK() {
        ModalController.closeStage(tulostusAlue);
    }

    @FXML
    private void handleTulosta() {
        Dialogs.showMessageDialog("Ei osata vielä tulostaa");
    }

    @Override
    public String getResult() {
        return null;
    }

    @Override
    public void setDefault(String oletus) {
        // if ( oletus == null ) return;
        tulostusAlue.setText(oletus);
    }

    /**
     * Mitä tehdään kun ikkuna näytetty
     */
    @Override
    public void handleShown() {
        //
    }

    /**
     * @return Tulostettava alue
     */
    public TextArea getTextArea() {
        return tulostusAlue;
    }

    /**
     * Näyttää tekstin tulostusalueessa
     * 
     * @param tulostus
     *            tulostettava teksti
     * @return kontrolleri, jolta voidaan pyytää lisää tietoa
     */
    public static TulostusController tulosta(String tulostus) {
        TulostusController tulostusCtrl = (TulostusController) ModalController
                .showModeless(TulostusController.class.getResource("TulostusView.fxml"), "Tulostus", tulostus);
        return tulostusCtrl;
    }

}