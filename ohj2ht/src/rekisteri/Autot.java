package rekisteri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

/**
 * Rekisterin autotietokanta, joka osaa mm. lisätä uuden auton.
 * 
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 02062018
 */
public class Autot implements Iterable<Auto> {

    private static int MAX_LKM = Integer.MAX_VALUE;
    private int lkm = 0;
    private String tiedostonPerusNimi = "autot";
    private String kokoNimi = "";
    private Auto alkiot[] = new Auto[5];
    private boolean muutettu = false;

    /**
     * Oletusmuodostaja.
     */
    public Autot() {
        // Atribuuttien oma alustus riittää
    }

    /**
     * Muodostaja, jolla voidaan asettaaa maksimikoko
     * 
     * @param koko maksimikoko
     */
    public Autot(int koko) {
        MAX_LKM = koko;
        alkiot = new Auto[koko];
    }

    /**
     * Poistaa auton
     * 
     * @param id
     *            poistettavan auton tunnus numero
     * @return 1 jos poistettiin 0 jos ei onnistu
     * @example
     * 
     *          <pre name="test">
     *  
     * #THROWS SailoException  
     * Autot autot = new Autot(); 
     * Auto audi1 = new Auto(), audi2 = new Auto(), audi3 = new Auto(); 
     * audi1.rekisteroi(); audi2.rekisteroi(); audi3.rekisteroi(); 
     * int id1 = audi1.getTunnusNro(); 
     * autot.lisaa(audi1); autot.lisaa(audi2); autot.lisaa(audi3); 
     * autot.poista(id1+1) === 1; 
     * autot.annaId(id1+1) === null; autot.getLkm() === 2; 
     * autot.poista(id1) === 1; autot.getLkm() === 1; 
     * autot.poista(id1+3) === 0; autot.getLkm() === 1;
     *          </pre>
     */
    public int poista(int id) {
        int ind = etsiId(id);
        if (ind < 0)
            return 0;
        lkm--;
        for (int i = ind; i < lkm; i++)
            alkiot[i] = alkiot[i + 1];
        alkiot[lkm] = null;
        muutettu = true;
        return 1;
    }

    /**
     * Etsii auton id:n perusteella
     * 
     * @param id
     *            tunnusnumero, jonka mukaan etsitään
     * @return auto jolla etsittävä id tai null
     * @example
     * 
     *          <pre name="test">
     *  
     * #THROWS SailoException  
     * Autot autot = new Autot(); 
     * Auto audi1 = new Auto(), audi2 = new Auto(), audi3 = new Auto(); 
     * audi1.rekisteroi(); audi2.rekisteroi(); audi3.rekisteroi(); 
     * int id1 = audi1.getTunnusNro(); 
     * autot.lisaa(audi1); autot.lisaa(audi2); autot.lisaa(audi3); 
     * autot.annaId(id1  ) == audi1 === true; 
     * autot.annaId(id1+1) == audi2 === true; 
     * autot.annaId(id1+2) == audi3 === true;
     *          </pre>
     */
    public Auto annaId(int id) {
        for (Auto auto : this) {
            if (id == auto.getTunnusNro())
                return auto;
        }
        return null;
    }

    /**
     * Etsii auton id:n perusteella
     * 
     * @param id
     *            tunnusnumero, jonka mukaan etsitään
     * @return löytyneen auton indeksi tai -1 jos ei löydy
     * @example
     * 
     *          <pre name="test">
     *  
     * #THROWS SailoException  
     * Autot autot = new Autot(); 
     * Auto audi1 = new Auto(), audi2 = new Auto(), audi3 = new Auto(); 
     * audi1.rekisteroi(); audi2.rekisteroi(); audi3.rekisteroi(); 
     * int id1 = audi1.getTunnusNro(); 
     * autot.lisaa(audi1); autot.lisaa(audi2); autot.lisaa(audi3); 
     * autot.etsiId(id1+1) === 1; 
     * autot.etsiId(id1+2) === 2;
     *          </pre>
     */
    public int etsiId(int id) {
        for (int i = 0; i < lkm; i++)
            if (id == alkiot[i].getTunnusNro())
                return i;
        return -1;
    }

    /**
     * Palauttaa viitteen i:teen autoon.
     * 
     * @param i
     *            monennenko auton viite halutaan
     * @return viite autoon jonka indeksi on i
     * @throws IndexOutOfBoundsException laiton indeksi
     */
    public Auto anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }

    /**
     * Lisää uuden auton tietorakenteeseen. Ottaa auton omistukseensa.
     * 
     * @param auto
     *            Lisättävän auton viite
     * @throws SailoException
     *             jos tietorakenne täynnä
     * @example
     * 
     *          <pre name="test">
     * #THROWS SailoException 
     * Autot autot = new Autot();
     * Auto audi1 = new Auto(), audi2 = new Auto();
     * autot.getLkm() === 0;
     * autot.lisaa(audi1); autot.getLkm() === 1;
     * autot.lisaa(audi2); autot.getLkm() === 2;
     * autot.lisaa(audi1); autot.getLkm() === 3;
     * Iterator<Auto> it = autot.iterator();
     * it.next() === audi1;
     * it.next() === audi2;
     * it.next() === audi1;
     * autot.lisaa(audi1); autot.getLkm() === 4;
     * autot.lisaa(audi1); autot.getLkm() === 5;
     *          </pre>
     */
    public void lisaa(Auto auto) throws SailoException {

        if (lkm >= MAX_LKM)
            throw new SailoException("Liikaa alkioita");
        if (lkm >= alkiot.length)
            alkiot = Arrays.copyOf(alkiot, lkm + 20);
        alkiot[lkm] = auto;
        lkm++;
        muutettu = true;
    }

    /**
     * Lukee autotietokannan tiedostosta
     * 
     * @param tied tiedoston hakemisto.
     * @throws SailoException
     *             jos lukeminen epäonnistuu.
     * 
     * @example
     * 
     *          <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * 
     *  Autot autot = new Autot();
     *  Auto audi1 = new Auto(), audi2 = new Auto();
     *  audi1.vastaaAudi();
     *  audi2.vastaaAudi();
     *  String hakemisto = "autot";
     *  String tiedNimi = hakemisto+"/autot";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  autot.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  autot.lisaa(audi1);
     *  autot.lisaa(audi2);
     *  autot.tallenna();
     *  autot = new Autot();           
     *  autot.lueTiedostosta(tiedNimi);  
     *  Iterator<Auto> i = autot.iterator();
     *  i.next() === audi1;
     *  i.next() === audi2;
     *  i.hasNext() === false;
     *  autot.lisaa(audi2);
     *  autot.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     *  dir.delete() === false;
     *          </pre>
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try (BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()))) {
            String rivi = "";

            while ((rivi = fi.readLine()) != null) {
                rivi = rivi.trim();
                if ("".equals(rivi) || rivi.charAt(0) == ';')
                    continue;
                Auto auto = new Auto();
                auto.parse(rivi);
                lisaa(auto);

            }
            muutettu = false;
        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch (IOException e) {
            throw new SailoException("Tiedosto-ongelma: " + e.getMessage());
        }
    }

    /**
     * Palauttaa autojen lukumäärän.
     * 
     * @return autojen lukumäärä.
     */
    public int getLkm() {
        return lkm;
    }

    /**
     * Tallentaa autojen tiedot tiedostoon.
     * 
     * @throws SailoException
     *             jos jokin menee vikaan
     */
    public void tallenna() throws SailoException {
        if (!muutettu)
            return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);

        try (PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))) {
            for (Auto auto : this) {
                fo.println(auto.toString());
            }
        } catch (FileNotFoundException ex) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException ex) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
    }

    /**
     * Palauttaa rekisterin koko nimen
     * 
     * @return koko nimi
     */
    public String getKokoNimi() {
        return kokoNimi;
    }

    /**
     * Palauttaa tallennukseen käytettävän tiedoston nimen.
     * 
     * @return tiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }

    /**
     * Asettaa perusnimen
     * 
     * @param nimi
     *            perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }

    /**
     * Palauttaa tallennukseen käytettävän tiedoston nimen
     * 
     * @return tiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }

    /**
     * palauttaa varakopiotiedoston nimen
     * 
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }

    /**
     * Testiohjelma autotietokannalle.
     * 
     * @param args
     *            ei käytössä
     */
    public static void main(String[] args) {
        Autot autot = new Autot();
        Auto audi = new Auto(), audi2 = new Auto();
        audi.rekisteroi();
        audi.vastaaAudi();
        audi2.rekisteroi();
        audi2.vastaaAudi();

        try {
            autot.lisaa(audi);
            autot.lisaa(audi2);

            System.out.println("============== Autot testi ==============");

            for (int i = 0; i < autot.getLkm(); i++) {
                Auto auto = autot.anna(i);
                System.out.println("Auto nro: " + i);
                auto.tulosta(System.out);
            }
        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * palauttaa taulukossa hakuehtoja vastaavien autojen viitteet
     * 
     * @param hakuehto hakuna käytettävä ehto
     * @param k
     *            etsittävän kentän indeksi
     * @return tietorakenne löytyneistä autoista
     * @example
     * 
     *          <pre name="test">
     *  
     * #THROWS SailoException  
     *   Autot autot = new Autot(); 
     *   Auto auto1 = new Auto(); auto1.parse("1|Audi A4|AYZ-585|2013|56000|Diesel|Automaatti|3.0|52000"); 
     *   Auto auto2 = new Auto(); auto2.parse("2|BMW X2|UYZ-585|2013|56000|Diesel|Automaatti|3.0|52000"); 
     *   Auto auto3 = new Auto(); auto3.parse("3|Volvo V70|UYZ-585|2013|56000|Diesel|Automaatti|3.0|52000"); 
     *   Auto auto4 = new Auto(); auto4.parse("4|Volvo V50|UYZ-585|2013|56000|Diesel|Automaatti|3.0|52000"); 
     *   Auto auto5 = new Auto(); auto5.parse("5|BMW X5|AYZ-585|2013|56000|Diesel|Automaatti|3.0|52000"); 
     *   autot.lisaa(auto1); autot.lisaa(auto2); autot.lisaa(auto3); autot.lisaa(auto4); autot.lisaa(auto5);
     *   List<Auto> loytyneet;  
     *   loytyneet = (List<Auto>)autot.etsi("*v*",0);  
     *   loytyneet.size() === 2;  
     *   loytyneet.get(0) == auto3 === true;  
     *   loytyneet.get(1) == auto4 === true;  
     *     
     *   loytyneet = (List<Auto>)autot.etsi("*A*",1);  
     *   loytyneet.size() === 2;  
     *   loytyneet.get(0) == auto1 === true;  
     *   loytyneet.get(1) == auto5 === true; 
     *     
     *   loytyneet = (List<Auto>)autot.etsi(null,-1);  
     *   loytyneet.size() === 5;
     *          </pre>
     */
    public Collection<Auto> etsi(String hakuehto, int k) {
        String ehto = "*";
        if (hakuehto != null && hakuehto.length() > 0)
            ehto = hakuehto;
        int hk = k + 1;
        List<Auto> loytyneet = new ArrayList<Auto>();
        for (Auto auto : this) {
            if (WildChars.onkoSamat(auto.anna(hk), ehto))
                loytyneet.add(auto);
        }

        Collections.sort(loytyneet, new Auto.Vertailija(k));
        return loytyneet;
    }

    /**
     * Palautetaan iteraattori autoista
     * 
     * @return auto iteraattori
     */
    @Override
    public Iterator<Auto> iterator() {
        return new AutotIterator();
    }

    /**
     * Luokka autojen iterointiin
     * 
     * @example
     * 
     *          <pre name="test">
     * #THROWS SailoException 
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Autot autot = new Autot();
     * Auto audi1 = new Auto(), audi2 = new Auto();
     * audi1.rekisteroi(); audi2.rekisteroi();
     *
     * autot.lisaa(audi1); 
     * autot.lisaa(audi2); 
     * autot.lisaa(audi1); 
     * 
     * StringBuffer ids = new StringBuffer(30);
     * for (Auto auto:autot)
     *   ids.append(" "+auto.getTunnusNro());           
     * 
     * String tulos = " " + audi1.getTunnusNro() + " " + audi2.getTunnusNro() + " " + audi2.getTunnusNro();
     * 
     * ids = new StringBuffer(30);
     * for (Iterator<Auto>  i=autot.iterator(); i.hasNext(); ) { 
     *   Auto auto = i.next();
     *   ids.append(" "+auto.getTunnusNro());           
     * }
     * 
     * Iterator<Auto>  i=autot.iterator();
     * i.next() == audi1  === true;
     * i.next() == audi2  === true;
     * i.next() == audi1  === true;
     * 
     * i.next();  #THROWS NoSuchElementException
     * 
     *          </pre>
     */
    public class AutotIterator implements Iterator<Auto> {
        private int kohdalla = 0;

        /**
         * katsoo onko seuraavaa autoa vielä olemassa
         * 
         * @return true jos autoja vielä jäljellä
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }

        /**
         * antaa seuraavan auton
         * 
         * @return seuraava auto
         * @throws NoSuchElementException
         *             jos jokin ei toimi
         */
        @Override
        public Auto next() throws NoSuchElementException {
            if (!hasNext())
                throw new NoSuchElementException("Ei ole");
            return anna(kohdalla++);
        }

        /**
         * Poistamista ei ole toteutettu
         * 
         * @throws UnsupportedOperationException
         *             heittää aina
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Ei poisteta");
        }
    }

    /**
     * Luetaan aikaisemmin luodusta tiedostosta
     * 
     * @throws SailoException
     *             jos tulee virhe
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }

    /**
     * Korvaa tai lisää auton tietorakenteeseen
     * 
     * @param auto
     *            lisättävä auto
     * @throws SailoException
     *             jos tietorakenne täynnä
     * @example
     * 
     *          <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Autot autot = new Autot();
     * Auto audi1 = new Auto(), audi2 = new Auto();
     * audi1.rekisteroi(); audi2.rekisteroi();
     * autot.getLkm() === 0;
     * autot.korvaaTaiLisaa(audi1); autot.getLkm() === 1;
     * autot.korvaaTaiLisaa(audi2); autot.getLkm() === 2;
     * Auto audi3 = audi1.clone();
     * audi3.aseta(3,"kkk");
     * Iterator<Auto> it = autot.iterator();
     * it.next() == audi1 === true;
     * autot.korvaaTaiLisaa(audi3); autot.getLkm() === 2;
     * it = autot.iterator();
     * Auto a0 = it.next();
     * a0 === audi3;
     * a0 == audi3 === true;
     * a0 == audi1 === false;
     *          </pre>
     */
    public void korvaaTaiLisaa(Auto auto) throws SailoException {
        int id = auto.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getTunnusNro() == id) {
                alkiot[i] = auto;
                muutettu = true;
                return;
            }
        }
        lisaa(auto);
    }
}