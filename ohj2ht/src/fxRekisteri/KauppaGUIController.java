package fxRekisteri;

import static fxRekisteri.TietueDialogController.getFieldId;
import static fxRekisteri.TietueDialogController.kysyTietue;
import static fxRekisteri.TietueDialogController.luoKentat;
import static fxRekisteri.TietueDialogController.naytaTietue;
import static fxRekisteri.TietueDialogController.tyhjenna;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.SwingConstants;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import kanta.Tietue;
import rekisteri.Auto;
import rekisteri.Huolto;
import rekisteri.Rekisteri;
import rekisteri.SailoException;

/**
 * Kontrolleri, joka hoitaa käyttöliittymän tapahtumia.
 * 
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 04062018
 */
public class KauppaGUIController implements Initializable {

    @FXML
    private TextField hakuehto;
    @FXML
    private ComboBoxChooser<String> cbKentat;
    @FXML
    private ScrollPane panelAuto;
    @FXML
    private ListChooser<Auto> chooserAutot;
    @FXML
    private Label labelVirhe;
    @FXML
    private GridPane gridAuto;
    @FXML
    private StringGrid<Huolto> tableHuollot;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }

    @FXML
    private void handleHakuehto() {
        if (autoKohdalla != null)
            hae(autoKohdalla.getTunnusNro());
    }

    @FXML
    private void handleApua(ActionEvent event) {
        Apua();
    }

    @FXML
    private void handleAvaa(ActionEvent event) {
        Avaa();
    }

    @FXML
    private void handleLisaaAuto(ActionEvent event) {
        lisaaAuto();
    }

    @FXML
    private void handleLopeta(ActionEvent event) {
        tallenna();
        Platform.exit();
    }

    @FXML
    private void handlePoistaAuto(ActionEvent event) {
        poistaAuto();
    }

    @FXML
    private void handlepoistaHuolto(ActionEvent event) {
        poistaHuolto();
    }

    @FXML
    private void handleTietoja(ActionEvent event) {
        ModalController.showModal(KauppaGUIController.class.getResource("AboutView.fxml"), "Kauppa", null, "");
    }

    @FXML
    private void handleTallenna(ActionEvent event) {
        tallenna();
    }

    @FXML
    void handleTulosta(ActionEvent event) {
        TulostusController tulostusCtrl = TulostusController.tulosta(null);
        tulostaValitut(tulostusCtrl.getTextArea());
    }

    @FXML
    private void handleUusiHuolto(ActionEvent event) {
        LisaaHuolto();
    }

    @FXML
    private void handleMuokkaaAuto() {
        muokkaa(kentta);
    }

    @FXML
    private void handleMuokkaaHuolto() {
        muokkaaHuoltoa();
    }

    // ==================================================

    private String kaupannimi = "Autokauppa";
    private Rekisteri autokaupparekisteri;
    private Auto autoKohdalla;
    private Tietue apuAuto = new Auto(); // Auto, voidaan kysellä tietoja
    private Huolto apuHuolto = new Huolto(); //Huolto, voidaan kysellä tietoja
    private TextField[] edits;
    private int kentta = 0;

    /**
     * Ohjelmointityön suunnitelma avataan selaimeen.
     */
    private void Apua() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2018k/ht/samakrap%22");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }

    private void muokkaaHuoltoa() {
        try {
            int c = tableHuollot.getColumnNr();
            if (c < 0)
                c = 0;
            int k = c + apuHuolto.ekaKentta();
            Huolto huolto = tableHuollot.getObject();
            if (huolto == null)
                return;
            huolto = kysyTietue(null, huolto.clone(), k, "Huollon muokkaus");
            if (huolto == null)
                return;
            autokaupparekisteri.korvaaTaiLisaa(huolto);
            int r = tableHuollot.getRowNr();
            naytaHuollot(autoKohdalla);
            tableHuollot.select(r, c);
        } catch (CloneNotSupportedException e) {
            //
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }

    }

    /**
     * Kysyy tiedoston nimen, jos onnistuu niin lukee sen.
     * @return true onnistui, false jos ei.
     */
    public boolean Avaa() {
        String uusinimi = KaupanNimiController.kysyNimi(null, kaupannimi);
        if (uusinimi == null)
            return false;
        lueTiedosto(uusinimi);
        return true;
    }

    private void setTitle(String title) {
        ModalController.getStage(hakuehto).setTitle(title);
    }

    /**
     * Alustaa rekisterin, lukee sen valitusta tiedostosta.
     * @param nimi tiedosto, josta luetaan tiedot
     * @return null jos lukeminen onnistui, muuten virhe
     */
    protected String lueTiedosto(String nimi) {
        kaupannimi = nimi;
        setTitle("Autokauppa - " + kaupannimi);
        try {
            autokaupparekisteri.lueTiedostosta(nimi);
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage();
            if (virhe != null)
                Dialogs.showMessageDialog(virhe);
            return virhe;
        }
    }

    /**
     * Luo uuden auton.
     */
    private void lisaaAuto() {
        try {
            Auto uusi = new Auto();
            uusi = kysyTietue(null, uusi, 0, "Uusi auto");
            if (uusi == null)
                return;
            uusi.rekisteroi();
            autokaupparekisteri.lisaa(uusi);
            hae(uusi.getTunnusNro());
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
        }
    }

    /**
     * Hakee autojen tiedot listaan.
     * @param tnro auton tunnusnumero, aktivoidaan kun haettu.
     */
    protected void hae(int tnro) {
        int k = cbKentat.getSelectedIndex();
        String ehto = hakuehto.getText();
        if (ehto.indexOf('*') < 0)
            ehto = "*" + ehto + "*";
        naytaVirhe(null);

        chooserAutot.clear();

        int index = 0;
        Collection<Auto> autot;
        try {
            autot = autokaupparekisteri.etsi(ehto, k);
            int i = 0;
            for (Auto auto : autot) {
                if (auto.getTunnusNro() == tnro)
                    index = i;
                chooserAutot.add(auto.getMerkkiMalli(), auto);
                i++;
            }
            if (i == 0) {
                tyhjenna(edits);
                tableHuollot.clear();
            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Auton hakemisessa ongelma: " + ex.getMessage());
        }
        chooserAutot.setSelectedIndex(index);
    }

    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }

    /**
     * Poistetaan valittu auto listasta.
     */
    private void poistaAuto() {
        if (autoKohdalla == null)
            return;
        if (!Dialogs.showQuestionDialog("Poisto", "Poistetaanko auto: " + autoKohdalla.getMerkkiMalli(), "Kyllä", "Ei"))
            return;
        autokaupparekisteri.poista(autoKohdalla);
        int index = chooserAutot.getSelectedIndex();
        hae(0);
        chooserAutot.setSelectedIndex(index);
    }

    /**
     * Poistetaan huoltotaulukosta valittu huolto.
     */
    private void poistaHuolto() {
        Huolto huolto = tableHuollot.getObject();
        if (huolto == null)
            return;
        int rivi = tableHuollot.getRowNr();
        autokaupparekisteri.poistaHuolto(huolto);
        naytaHuollot(autoKohdalla);
        tableHuollot.selectRow(rivi);
    }

    /**
     * Tulostaa listan autot tekstialueeseen.
     * @param text alue, johon autot tulostetaan.
     */
    public void tulostaValitut(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Tulostus");
            Collection<Auto> autot = autokaupparekisteri.etsi("", -1);
            for (Auto auto : autot) {
                tulosta(os, auto);
                os.println("\n\n");
            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Ongelmia auton hakemisessa!" + ex.getMessage());
        }
    }

    /**
     * Rekisterin tietojen tallentaminen
     * @return null jos onnistui, muuten virhe
     */
    private String tallenna() {
        try {
            autokaupparekisteri.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }

    /**
     * Tarkistaa, onko tallennettu
     * @return true jos sovelluksen saa sulkea, false jos ei saa sulkea.
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }

    /**
     * Luo tyhjän huollon.
     */
    public void LisaaHuolto() {
        try {
            if (autoKohdalla == null)
                return;
            Huolto huolto = new Huolto(autoKohdalla.getTunnusNro());
            huolto = kysyTietue(null, huolto, 0, "Uusi huolto");
            if (huolto == null)
                return;
            huolto.rekisteroi();
            autokaupparekisteri.lisaa(huolto);
            naytaHuollot(autoKohdalla);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia lisäämisessä! " + e.getMessage());
        }
    }

    /**
     * Näytetään taulukkomuodossa huollot.
     * @param auto
     */
    private void naytaHuollot(Auto auto) {
        tableHuollot.clear();
        if (auto == null)
            return;

        List<Huolto> huollot;
        try {
            huollot = autokaupparekisteri.annaHuollot(auto);
            if (huollot.size() == 0)
                return;
            tableHuollot.add(huollot);
        } catch (SailoException e) {
            naytaVirhe(e.getMessage());
        }
    }

    /**
     * @param autokaupparekisteri rekisteri jota käsitellään
     */
    public void setRekisteri(Rekisteri autokaupparekisteri) {
        this.autokaupparekisteri = autokaupparekisteri;
        naytaAuto();
    }

    /**
     * Näyttää valitun auton tiedot.
     */
    protected void naytaAuto() {
        autoKohdalla = chooserAutot.getSelectedObject();
        naytaTietue(edits, autoKohdalla);
        naytaHuollot(autoKohdalla);
        gridAuto.setVisible(autoKohdalla != null);
        naytaVirhe(null);
    }

    /**
     * Tulostaa auton tiedot
     * @param os tietovirta johon tulostetaan
     * @param auto tulostettava auto
     */
    public void tulosta(PrintStream os, final Auto auto) {
        os.println("----------------------------------------------");
        auto.tulosta(os);
        os.println("----------------------------------------------");
        try {
            List<Huolto> huollot = autokaupparekisteri.annaHuollot(auto);
            for (Huolto huolto : huollot)
                huolto.tulosta(os);
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Huoltojen' hakemisessa ongelmia! " + ex.getMessage());
        }
    }

    private void muokkaa(int k) {
        if (autoKohdalla == null)
            return;
        try {
            Auto auto = kysyTietue(null, autoKohdalla.clone(), k, "Auton muokkaus");
            if (auto == null)
                return;
            autokaupparekisteri.korvaaTaiLisaa(auto);
            hae(auto.getTunnusNro());
        } catch (CloneNotSupportedException e) {
            //
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
    }

    /**
     * Tekee tarvittavat alustukset. Luo tekstikenttiä ja asettaa niihin kuuntelijat.
     * Kentät readonly-tilaan.
     * Alustetaan autolistan kuuntelija.
     */
    protected void alusta() {
        panelAuto.setFitToHeight(true);
        chooserAutot.clear();
        chooserAutot.addSelectionListener(e -> naytaAuto());
        edits = luoKentat(apuAuto, gridAuto);
        for (TextField edit : edits)
            if (edit != null) {
                edit.setEditable(false);
                edit.setOnMouseClicked(e -> {
                    if (e.getClickCount() > 1)
                        muokkaa(getFieldId(e.getSource(), 0));
                });
                edit.focusedProperty().addListener((a, o, n) -> kentta = getFieldId(edit, 0));
            }

        cbKentat.clear();
        for (int k = apuAuto.ekaKentta(); k < apuAuto.getKenttia(); k++)
            cbKentat.add(apuAuto.getKysymys(k), null);
        cbKentat.setSelectedIndex(0);

        // Otsikot, solujen leveydet ja tasaukset
        String otsikot[] = new String[apuHuolto.getKenttia() - apuHuolto.ekaKentta()];
        for (int i = 0, k = apuHuolto.ekaKentta(); k < apuHuolto.getKenttia(); i++, k++)
            otsikot[i] = apuHuolto.getKysymys(k);
        tableHuollot.initTable(otsikot);
        for (int i = 0, k = apuHuolto.ekaKentta(); k < apuHuolto.getKenttia(); i++, k++) {
            int pos = apuHuolto.getKentta(k).getSijainti();
            tableHuollot.setAlignment(i, pos);
            if (pos == SwingConstants.RIGHT)
                tableHuollot.setColumnWidth(i, 50);
        }

        // tekstit, lajitteluehdot, muokkaus, tuplaklikkauskuuntelu
        tableHuollot.setPlaceholder(new Label("Ei vielä huoltoja"));
        tableHuollot.setOnCellString((g, huolto, defValue, r, c) -> huolto.anna(c + huolto.ekaKentta()));
        tableHuollot.setOnCellValue((g, huolto, defValue, r, c) -> huolto.getAvain(c + huolto.ekaKentta()));

        tableHuollot.setOnGridLiveEdit((g, huolto, defValue, r, c, edit) -> {
            String virhe = huolto.aseta(c + huolto.ekaKentta(), defValue);
            if (virhe == null) {
                edit.setStyle(null);
                autokaupparekisteri.setHuoltoMuutos();
                Dialogs.setToolTipText(edit, "");
            } else {
                edit.setStyle("-fx-background-color: red");
                Dialogs.setToolTipText(edit, virhe);
            }
            return defValue;
        });

        tableHuollot.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2)
                muokkaaHuoltoa();
        });

        Platform.runLater(() -> hakuehto.requestFocus());
    }

}