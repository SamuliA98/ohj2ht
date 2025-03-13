package rekisteri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Rekisterin huollot, joka osaa mm. lisätä uuden huollon.
 * 
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 02062018
 */
public class Huollot implements Iterable<Huolto> {

    private String tiedostonPerusNimi = "huollot";
    private boolean muutettu = false;

    /** Taulukko huolloista */
    private final List<Huolto> alkiot = new ArrayList<Huolto>();

    /**
     * Huollon alustaminen.
     */
    public Huollot() {
        // Ei tarvitse tehdä vielä mitään.
    }

    /**
     * iteraattori kaikkien huoltojen läpikäymiseen
     * 
     * @return huoltoiteraattori
     * 
     *         <@example
     * 
     *         <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Huollot huollot = new Huollot();
     * Huolto huolto6 = new Huolto(1); huollot.lisaa(huolto6);
     * Huolto huolto3 = new Huolto(2); huollot.lisaa(huolto3);
     * Huolto huolto7 = new Huolto(2); huollot.lisaa(huolto7);
     * Huolto huolto4 = new Huolto(1); huollot.lisaa(huolto4);
     * Huolto huolto9 = new Huolto(1); huollot.lisaa(huolto9);
     * 
     * Iterator<Huolto> i2 = huollot.iterator();
     * i2.next() === huolto6;
     * i2.next() === huolto3;
     * i2.next() === huolto7;
     * i2.next() === huolto4;
     * i2.next() === huolto9;
     * i2.next() === huolto4; #THROWS NoSuchElementException
     * 
     *  int n = 0;
     *  int jnrot[] = {1, 2, 2, 1, 1};
     *  
     *  for (Huolto huolto : huollot){
     *  huolto.getAutoNro() === jnrot[n];
     *  n++;
     *  }
     *  
     *  n === 5;
     * 
     *         </pre>
     */
    @Override
    public Iterator<Huolto> iterator() {
        return alkiot.iterator();
    }

    /**
     * 
     * Lukee huollot tiedostosta
     * 
     * @param tied
     *            tiedoston nimi
     * @throws SailoException
     *             jos ei onnistu
     * @example
     * 
     *          <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Huollot huollot = new Huollot();
     *  Huolto oljynvaihto1 = new Huolto(); oljynvaihto1.vastaaOljynvaihto(2);
     *  Huolto oljynvaihto2 = new Huolto(); oljynvaihto2.vastaaOljynvaihto(1);
     *  Huolto oljynvaihto3 = new Huolto(); oljynvaihto3.vastaaOljynvaihto(2); 
     *  Huolto oljynvaihto4 = new Huolto(); oljynvaihto4.vastaaOljynvaihto(1); 
     *  Huolto oljynvaihto5 = new Huolto(); oljynvaihto5.vastaaOljynvaihto(2); 
     *  String tiedNimi = "Huollot";
     *  File ftied = new File(tiedNimi+".dat");
     *  ftied.delete();
     *  huollot.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  huollot.lisaa(oljynvaihto1);
     *  huollot.lisaa(oljynvaihto2);
     *  huollot.lisaa(oljynvaihto3);
     *  huollot.lisaa(oljynvaihto4);
     *  huollot.lisaa(oljynvaihto5);
     *  huollot.tallenna();
     *  huollot = new Huollot();
     *  huollot.lueTiedostosta(tiedNimi);
     *  Iterator<Huolto> i = huollot.iterator();
     *  i.next().toString() === oljynvaihto1.toString();
     *  i.next().toString() === oljynvaihto2.toString();
     *  i.next().toString() === oljynvaihto3.toString();
     *  i.next().toString() === oljynvaihto4.toString();
     *  i.next().toString() === oljynvaihto5.toString();
     *  i.hasNext() === false;
     *  huollot.lisaa(oljynvaihto5);
     *  huollot.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     *          </pre>
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try (BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()))) {

            String rivi;
            while ((rivi = fi.readLine()) != null) {
                rivi = rivi.trim();
                if ("".equals(rivi) || rivi.charAt(0) == ';')
                    continue;
                Huolto huolto = new Huolto();
                huolto.parse(rivi);
                lisaa(huolto);
            }
            muutettu = false;
        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch (IOException e) {
            throw new SailoException("Tiedosto-ongelma. " + e.getMessage());
        }
    }

    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * 
     * @throws SailoException
     *             jos tulee virhe
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }

    /**
     * Tallentaa huollot tiedostoon
     * 
     * @throws SailoException
     *             jos tallennus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if (!muutettu)
            return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);

        try (PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))) {
            for (Huolto huolto : this) {
                fo.println(huolto.toString());
            }
        } catch (FileNotFoundException ex) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException ex) {
            throw new SailoException("Tiedostossa " + ftied.getName() + " kirjoitusongelmia");
        }
        muutettu = false;
    }

    /**
     * Palauttaa rekisterin huoltojen lukumäärän
     * 
     * @return huoltojen lukumäärän
     */
    public int getLkm() {
        return alkiot.size();
    }

    /**
     * Asettaa tiedoston perusnimen
     * 
     * @param tied
     *            perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }

    /**
     * Palauttaa tiedoston nimen
     * 
     * @return tiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }

    /**
     * palauttaa tiedoston nimen, jota käytetään tallentamiseen
     * 
     * @return tiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }

    /**
     * Palauttaa varakopiotiedoston nimen
     * 
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }

    /**
     * Haetaan kaikki auton huollot
     * 
     * @param tunnusNro
     *            auton tunnusNro jonka huoltoja haetaan
     * @return tietorakenne jossa viitteet huoltoihin <@example
     * 
     *         <pre name="test">
     * #import java.util.*;
     * 
     * Huollot huollot = new Huollot();
     * Huolto huolto6 = new Huolto(1); huollot.lisaa(huolto6);
     * Huolto huolto3 = new Huolto(2); huollot.lisaa(huolto3);
     * Huolto huolto7 = new Huolto(2); huollot.lisaa(huolto7);
     * Huolto huolto4 = new Huolto(1); huollot.lisaa(huolto4);
     * Huolto huolto9 = new Huolto(1); huollot.lisaa(huolto9);
     * 
     * List<Huolto> loytyneet;
     * loytyneet = huollot.annaHuollot(3);
     * loytyneet.size() === 0;
     * loytyneet = huollot.annaHuollot(1);
     * loytyneet.size() === 3;
     * loytyneet.get(0) == huolto6 === true;
     * loytyneet.get(1) == huolto4 === true;
     * loytyneet.get(2) == huolto9 === true;
     * loytyneet = huollot.annaHuollot(2);
     * loytyneet.size() === 2;
     * loytyneet.get(0) == huolto3 === true;
     * loytyneet.get(1) == huolto7 === true;
     * 
     *         </pre>
     */
    public List<Huolto> annaHuollot(int tunnusNro) {
        List<Huolto> loydetyt = new ArrayList<Huolto>();
        for (Huolto huolto : alkiot) {
            if (huolto.getAutoNro() == tunnusNro)
                loydetyt.add(huolto);
        }
        return loydetyt;
    }

    /**
     * Lisää uuden huollon tietorakenteeseen.
     * 
     * @param huolto
     *            huolto joka lisätään
     */
    public void lisaa(Huolto huolto) {
        alkiot.add(huolto);
        muutettu = true;

    }

    /**
     * Testiohjelma huolloille
     * 
     * @param args
     *            ei käytössä
     */
    public static void main(String[] args) {
        Huollot huollot = new Huollot();
        Huolto huolto1 = new Huolto();
        huolto1.vastaaOljynvaihto(1);
        Huolto huolto2 = new Huolto();
        huolto2.vastaaOljynvaihto(2);
        Huolto huolto3 = new Huolto();
        huolto3.vastaaOljynvaihto(2);
        Huolto huolto4 = new Huolto();
        huolto4.vastaaOljynvaihto(1);

        huollot.lisaa(huolto1);
        huollot.lisaa(huolto2);
        huollot.lisaa(huolto3);
        huollot.lisaa(huolto4);

        System.out.println("============== Huollot testi ==============");

        List<Huolto> huollot2 = huollot.annaHuollot(2);

        for (Huolto huolto : huollot2) {
            System.out.print(huolto.getAutoNro() + " ");
            huolto.tulosta(System.out);
        }
    }

    /**
     * Korvaa huollon tietorakenteesta tai lisää uutena
     * 
     * @param huolto
     *            lisättävä huolto
     * @throws SailoException
     *             jos tietorakenne täynnä * @example
     * 
     *             <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Huollot huollot = new Huollot();
     * Huolto huolto1 = new Huolto(), huolto2 = new Huolto();
     * huolto1.rekisteroi(); huolto2.rekisteroi();
     * huollot.getLkm() === 0;
     * huollot.korvaaTaiLisaa(huolto1); huollot.getLkm() === 1;
     * huollot.korvaaTaiLisaa(huolto2); huollot.getLkm() === 2;
     * Huolto huolto3 = huolto1.clone();
     * huolto3.aseta(2,"Öljynvaihto");
     * Iterator<Huolto> i2=huollot.iterator();
     * i2.next() === huolto1;
     * huollot.korvaaTaiLisaa(huolto3); huollot.getLkm() === 2;
     * i2=huollot.iterator();
     * Huolto h = i2.next();
     * h === huolto3;
     * h == huolto3 === true;
     * h == huolto1 === false;
     *             </pre>
     */
    public void korvaaTaiLisaa(Huolto huolto) throws SailoException {
        int id = huolto.getTunnusNro();
        for (int i = 0; i < getLkm(); i++) {
            if (alkiot.get(i).getTunnusNro() == id) {
                alkiot.set(i, huolto);
                muutettu = true;
                return;
            }
        }
        lisaa(huolto);
    }

    /**
     * Poistaa valitun huollon
     * 
     * @param huolto
     *            huolto joka poistetaan
     * @return montako poistettiin
     * @example
     * 
     *          <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Huollot huollot = new Huollot();
     *  Huolto oljynvaihto1 = new Huolto(); oljynvaihto1.vastaaOljynvaihto(2);
     *  Huolto oljynvaihto2 = new Huolto(); oljynvaihto2.vastaaOljynvaihto(1);
     *  Huolto oljynvaihto3 = new Huolto(); oljynvaihto3.vastaaOljynvaihto(2); 
     *  Huolto oljynvaihto4 = new Huolto(); oljynvaihto4.vastaaOljynvaihto(1); 
     *  Huolto oljynvaihto5 = new Huolto(); oljynvaihto5.vastaaOljynvaihto(2); 
     *  huollot.lisaa(oljynvaihto1);
     *  huollot.lisaa(oljynvaihto2);
     *  huollot.lisaa(oljynvaihto3);
     *  huollot.lisaa(oljynvaihto4);
     *  huollot.poista(oljynvaihto5) === true; huollot.getLkm() === 3;
     *  huollot.poista(oljynvaihto2) === true;   huollot.getLkm() === 2;
     *  List<Huolto> h = huollot.annaHuollot(1);
     *  h.size() === 1; 
     *  h.get(0) === oljynvaihto4;
     *          </pre>
     */
    public boolean poista(Huolto huolto) {
        boolean poistettu = alkiot.remove(huolto);
        if (poistettu)
            muutettu = true;
        return poistettu;
    }

    /**
     * Poistaa kaikki auton huollot
     * 
     * @param tunnusNro
     *            viite autoon jonka huollot poistetaan.
     * @return poistettujen määrä
     * @example
     * 
     *          <pre name="test">
     *  Huollot huollot = new Huollot();
     *  Huolto oljynvaihto1 = new Huolto(); oljynvaihto1.vastaaOljynvaihto(2);
     *  Huolto oljynvaihto2 = new Huolto(); oljynvaihto2.vastaaOljynvaihto(1);
     *  Huolto oljynvaihto3 = new Huolto(); oljynvaihto3.vastaaOljynvaihto(2); 
     *  Huolto oljynvaihto4 = new Huolto(); oljynvaihto4.vastaaOljynvaihto(1); 
     *  Huolto oljynvaihto5 = new Huolto(); oljynvaihto5.vastaaOljynvaihto(2); 
     *  huollot.lisaa(oljynvaihto1);
     *  huollot.lisaa(oljynvaihto2);
     *  huollot.lisaa(oljynvaihto3);
     *  huollot.lisaa(oljynvaihto4);
     *  huollot.lisaa(oljynvaihto5);
     *  huollot.poista(2) === 3;  huollot.getLkm() === 2;
     *  huollot.poista(3) === 0;  huollot.getLkm() === 2;
     *  List<Huolto> h = huollot.annaHuollot(2);
     *  h.size() === 0; 
     *  h = huollot.annaHuollot(1);
     *  h.get(0) === oljynvaihto2;
     *  h.get(1) === oljynvaihto4;
     *          </pre>
     */
    public int poista(int tunnusNro) {
        int n = 0;
        for (Iterator<Huolto> it = alkiot.iterator(); it.hasNext();) {
            Huolto huolto = it.next();
            if (huolto.getAutoNro() == tunnusNro) {
                it.remove();
                n++;
            }
        }
        if (n > 0)
            muutettu = true;
        return n;
    }

    /**
     * asetetaan muutos, jotta pakotetaan tallennus
     */
    public void setMuutos() {
        muutettu = true;
    }
}