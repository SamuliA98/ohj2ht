package fxRekisteri;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import kanta.Tietue;

/**
 * Luo tietueen tiedoille uuden dialogin ja kysyy siitä tietoja
 * 
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 04062018
 *
 * @param <TYPE> tyyppi, mitä käsitellään.
 */
public class TietueDialogController<TYPE extends Tietue> implements ModalControllerInterface<TYPE> {

    @FXML
    private GridPane gridTietue;
    @FXML
    private Label labelVirhe;
    @FXML
    private ScrollPane panelTietue;

    private TYPE vastaus = null;
    private TYPE tietue;
    private TextField[] edits;

    @FXML
    private void handleOk() {
        vastaus = tietue;
        ModalController.closeStage(labelVirhe);
    }

    @FXML
    private void handlePeruuta() {
        ModalController.closeStage(labelVirhe);
    }

    /**
     * Luodaan GridPane -paneeliin kentän tiedot.
     * @param tietue malli, josta katsotaan kentät
     * @param gridTietue GridPane, tietue johon tiedot luodaan.
     * @return palataan.
     */
    public static TextField[] luoKentat(Tietue tietue, GridPane gridTietue) {
        gridTietue.getChildren().clear();
        TextField[] edits = new TextField[tietue.getKenttia()];

        for (int i = 0, k = tietue.ekaKentta(); k < tietue.getKenttia(); k++, i++) {
            Label label = new Label(tietue.getKysymys(k));
            gridTietue.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e" + k);
            gridTietue.add(edit, 1, i);
        }
        return edits;
    }

    /**
     * Tyhjennetään tekstikentät
     * @param edits kentät, jotka tyhjennetään.
     */
    public static void tyhjenna(TextField[] edits) {
        for (TextField edit : edits)
            if (edit != null)
                edit.setText("");
    }

    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.setStyle("");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.setStyle("-fx-background-color: brown");
    }

    /**
     * Palautetaan id:stä saatava luku.
     * @param obj tutkittava asia
     * @param oletus oletusarvo siltä varalta, ettei id olisi kunnollinen
     * @return asian id int-muodossa
     */
    public static int getFieldId(Object obj, int oletus) {
        if (!(obj instanceof Node))
            return oletus;
        Node node = (Node) obj;
        return Mjonot.erotaInt(node.getId().substring(1), oletus);
    }

    /**
     * Käsitellään muutos tietueeseen.
     * @param k kenttä, jota muutetaan
     * @param edit muuttunut kenttä
     */
    protected void kasitteleMuutosTietueeseen(int k, TextField edit) {
        String s = edit.getText();
        String virhe = null;
        virhe = tietue.aseta(k, s);
        if (virhe == null) {
            Dialogs.setToolTipText(edit, "");
            edit.setStyle("");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit, virhe);
            edit.setStyle("-fx-background-color: brown");
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }

    private static int alkuFocus = 0;

    @Override
    public TYPE getResult() {
        return vastaus;
    }

    /**
     * Mitä tehdään, kun dialogi täytetty.
     */
    @Override
    public void handleShown() {
        // textVastaus.requestFocus();
    }

    @Override
    public void setDefault(TYPE oletus) {
        tietue = oletus;
        gridTietue.getChildren().clear();

        edits = new TextField[tietue.getKenttia()];

        int eka = tietue.ekaKentta();
        int lkm = tietue.getKenttia();

        for (int i = 0, k = eka; k < lkm; k++, i++) {
            String otsikko = tietue.getKysymys(k);
            Label label = new Label(otsikko);
            gridTietue.add(label, 0, i);
            TextField edit = new TextField(tietue.anna(k));
            edits[k] = edit;
            final int kk = k;
            edit.setOnKeyReleased(e -> kasitteleMuutosTietueeseen(kk, (TextField) (e.getSource())));
            gridTietue.add(edit, 1, i);
        }

        int valitse = -1;
        if (eka <= alkuFocus && alkuFocus < lkm)
            valitse = alkuFocus;
        else if (eka < lkm)
            valitse = eka;
        final int v = valitse;
        Platform.runLater(() -> edits[v].requestFocus());
        naytaVirhe(null);
    }

    /**
     * Näytetään tietueen tiedot TextFieldinä
     * @param edits taulukko TextFieldeistä joihin näytetään tietoja
     * @param tietue tietue joka näytetään
     */
    public static void naytaTietue(TextField[] edits, Tietue tietue) {
        if (tietue == null)
            return;
        for (int k = tietue.ekaKentta(); k < tietue.getKenttia(); k++) {
            edits[k].setText(tietue.anna(k));
        }
    }

    /**
     * Luodaan tietueen kysymisdialogi, palautetaan kyseinen tietue muutoksien kanssa tai null
     * @param modalityStage asia, jolle ollaan modaalisia, null on modaalinen sovellukselle
     * @param oletus data, joka oletuksena näytetään
     * @param kentta kenttä, joka laitetaan aktiivikseksi
     * @param otsikko otsikko, joka näyetään dialogiin
     * @return täytetty tietue, Peruuta-nappia painamalla null
     */
    public static <TYPE extends Tietue> TYPE kysyTietue(Stage modalityStage, TYPE oletus, int kentta, String otsikko) {
        alkuFocus = kentta;
        return ModalController.showModal(TietueDialogController.class.getResource("TietueDialogView.fxml"), otsikko,
                modalityStage, oletus);
    }

}