package rekisteri;

import java.io.OutputStream;
import java.io.PrintWriter;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.DoubleKentta;
import kanta.IntKentta;
import kanta.JonoKentta;
import kanta.Kentta;
import kanta.Tietue;

/**
 * Huolto, joka osaa itse huolehtia esim. huollon tyypistä ja päivämäärästä.
 * 
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 02062018
 */
public class Huolto implements Cloneable, Tietue {
    private Kentta kentat[] = { new IntKentta("id"), new IntKentta("autoId"), new JonoKentta("huoltotyyppi"),
            new JonoKentta("pvm"), new DoubleKentta("kesto"), new JonoKentta("lisatiedot") };

    private static int seuraavaNro = 1;

    /**
     * Alustetaan huolto. toistaiseksi ei tarvitse tehdä mitään.
     */
    public Huolto() {
        // vielä ei tarvita mitään.
    }

    private IntKentta getAutoNroKentta() {
        return (IntKentta) (kentat[1]);
    }

    /**
     * Alustetaan tietyn auton huolto.
     * 
     * @param autoNro
     *            viite autoon.
     */
    public Huolto(int autoNro) {
        getAutoNroKentta().setValue(autoNro);
    }

    /**
     * @return huollon kenttien määrä
     */
    @Override
    public int getKenttia() {
        return kentat.length;
    }

    /**
     * @return Ensimmäisen kentän indeksi
     */
    @Override
    public int ekaKentta() {
        return 2;
    }

    /**
     * @param k
     *            kenttä joka halutaan
     * @return kenttä
     */
    public Kentta getKentta(int k) {
        return kentat[k];
    }

    /**
     * @param k
     *            kenttä jonka kysymys halutaan
     * @return kentän kysymysteksti
     */
    @Override
    public String getKysymys(int k) {
        try {
            return kentat[k].getKysymys();
        } catch (Exception ex) {
            return "voi ei";
        }
    }

    /**
     * @return kysymysten otsikot merkkijonotaulukkona
     * @example
     * @example
     * 
     *          <pre name="test">
     * #import java.util.Arrays;
     *   Huolto huolto = new Huolto();
     *   Arrays.toString(huolto.getOtsikot()) =R= "\\[huoltotyyppi, pvm, kesto, lisatiedot.*";
     *          </pre>
     */
    public String[] getOtsikot() {
        int n = getKenttia() - ekaKentta();
        String[] otsikot = new String[n];
        for (int i = 0, k = ekaKentta(); i < n; i++, k++)
            otsikot[i] = getKysymys(k);
        return otsikot;
    }

    /**
     * @param k
     *            kenttä jonka sisältö halutaan
     * @return kentän sisältö
     * @example
     * 
     *          <pre name="test">
     *   Huolto huolto = new Huolto();
     *   huolto.parse("   1   |  3  |   öljynvaihto  | 13.4.2017 | 3.0 | kaikki ok ");
     *   huolto.anna(0) === "1";   
     *   huolto.anna(1) === "3";   
     *   huolto.anna(2) === "öljynvaihto";   
     *   huolto.anna(3) === "13.4.2017";   
     *   huolto.anna(4) === "3.0";
     *   huolto.anna(5) === "kaikki ok";
     * 
     *          </pre>
     */
    @Override
    public String anna(int k) {
        try {
            return kentat[k].toString();
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Antaa k:n kentän sisällön merkkijonona jonka perusteella voi lajitella
     * 
     * @param k
     *            kenttä jonka sisältö palautetaan
     * @return kentän sisältö merkkijonona
     * @example
     * 
     *          <pre name="test">
     *   Huolto huolto = new Huolto();
     *   huolto.parse("   1   |  3  |   öljynvaihto  | 13.4.2017 | 3.0 | kaikki ok ");
     *   huolto.getAvain(0) === "         1";
     *   huolto.getAvain(1) === "         3";
     *   huolto.getAvain(2) === "ÖLJYNVAIHTO";
     *   huolto.getAvain(3) === "13.4.2017";
     *   huolto.getAvain(10) === "";
     *          </pre>
     */
    public String getAvain(int k) {
        try {
            return kentat[k].getAvain();
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Tehdään klooni autosta
     * 
     * @return Object kloonattu auto
     * @example
     * 
     *          <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Huolto huolto = new Huolto();
     *   huolto.parse("   1   |  3  |   öljynvaihto  | 13.4.2017 | 3.0 | kaikki ok ");
     *   Huolto kopio = huolto.clone();
     *   kopio.toString() === huolto.toString();
     *   huolto.parse("   1   |  3  |   öljynvaihto  | 13.4.2017 | 3.0 | kaikki ei ok ");
     *   kopio.toString().equals(huolto.toString()) === false;
     *          </pre>
     */
    @Override
    public Huolto clone() throws CloneNotSupportedException {
        Huolto uusi;
        uusi = (Huolto) super.clone();
        uusi.kentat = kentat.clone();

        for (int k = 0; k < getKenttia(); k++)
            uusi.kentat[k] = kentat[k].clone();
        return uusi;
    }

    /**
     * Asetetaan valitun kentän sisältö
     * 
     * @param k
     *            kenttä jonka sisältö asetetaan
     * @param s
     *            sisältö merkkijonona
     * @return null jos kaikki menee hyvi
     * @example
     * 
     *          <pre name="test">
     *   Huolto huolto = new Huolto();
     *   huolto.aseta(4,"moi") === "kesto: Ei desimaaliluku (moi)";
     *          </pre>
     */
    @Override
    public String aseta(int k, String s) {
        try {
            String virhe = kentat[k].aseta(s.trim());

            if (virhe == null && k == 0)
                setTunnusNro(getTunnusNro());
            if (virhe == null)
                return virhe;
            return getKysymys(k) + ": " + virhe;
        } catch (Exception ex) {
            return "Virhe: " + ex.getMessage();
        }

    }

    /**
     * Tulostetaan huollon tiedot
     * 
     * @param out
     *            tietovirta johon tulostetaan
     */
    public void tulosta(PrintWriter out) {
        String erotin = "";
        for (int k = ekaKentta(); k < getKenttia(); k++) {
            out.print(erotin + anna(k));
            erotin = " ";
        }
        out.println();
    }

    /**
     * Tulostaa auton tiedot
     * 
     * @param os
     *            tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintWriter(os, true));
    }

    /**
     * Metodi jolla saadaan täytettyä testiarvot huollolle.
     * 
     * @param nro
     *            Viite autoon, jonka huollosta kyse.
     */
    public void vastaaOljynvaihto(int nro) {

        aseta(1, "" + nro);
        aseta(2, "Öljynvaihto");
        aseta(3, "" + "19.3.2016");
        aseta(4, "" + 1.0);
        aseta(5, "Kaikki ok");

    }

    /**
     * Antaa autolle seuraavan numeron rekisterissä.
     * 
     * @return auton uusi tunnusNro <@example
     * 
     *         <pre name="test">
     * Huolto huolto = new Huolto();
     * huolto.getTunnusNro() === 0;
     * huolto.rekisteroi();
     * Huolto huolto2 = new Huolto();
     * huolto2.rekisteroi();
     * int n1 = huolto.getTunnusNro();
     * int n2 = huolto2.getTunnusNro();
     * n1 === n2 - 1;
     *         </pre>
     */
    public int rekisteroi() {
        return setTunnusNro(seuraavaNro);
    }

    /**
     * Palautetaan huollon oma id
     * 
     * @return huollon id
     */
    public int getTunnusNro() {
        return ((IntKentta) (kentat[0])).getValue();
    }

    /**
     * Asettaa tunnusnumeron
     * 
     * @param nro
     *            asetettava numero
     */
    private int setTunnusNro(int nro) {
        IntKentta k = ((IntKentta) (kentat[0]));
        k.setValue(nro);
        if (nro >= seuraavaNro)
            seuraavaNro = nro + 1;
        return k.getValue();
    }

    /**
     * Palautetaan mille autolle huolto kuuluu
     * 
     * @return auton id
     */
    public int getAutoNro() {
        return getAutoNroKentta().getValue();
    }

    /**
     * Palauttaa huollon tiedot merkkijonona
     * 
     * @return huollon tiedot merkkijonona
     * @example
     * 
     *          <pre name="test">
     *   Huolto huolto = new Huolto();
     *   huolto.parse("   1   |  3  |   öljynvaihto  | 13.4.2017 | 3.0 | kaikki ok ");
     *   huolto.toString()    === "1|3|öljynvaihto|13.4.2017|3.0|kaikki ok";
     *          </pre>
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) {
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|";
        }
        return sb.toString();
    }

    /**
     * Selvittää huollon tiedot | merkein erotellusta merkkijonosta
     * 
     * @param rivi
     *            merkkijono josta tiedot selvitetään
     * @example
     * 
     *          <pre name="test">
     *   Huolto huolto = new Huolto();
     *   huolto.parse("   1   |  3  |   öljynvaihto  | 13.4.2017 | 3.0 | kaikki ok ");
     *   huolto.getAutoNro() === 3;
     *   huolto.toString()    === "1|3|öljynvaihto|13.4.2017|3.0|kaikki ok";
     *   
     *   huolto.rekisteroi();
     *   int n = huolto.getTunnusNro();
     *   huolto.parse(""+(n+20));
     *   huolto.rekisteroi();
     *   huolto.getTunnusNro() === n+20+1;
     *   huolto.toString()     === "" + (n+20+1) + "|3|||0.0|";
     *          </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        return this.toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return getTunnusNro();
    }

    /**
     * Testiohjelma huollolle.
     * 
     * @param args
     *            ei käytössä.
     */
    public static void main(String[] args) {

        Huolto huolto = new Huolto();
        huolto.vastaaOljynvaihto(1);
        huolto.tulosta(System.out);

    }

}