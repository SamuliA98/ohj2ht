package rekisteri;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Comparator;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.IntKentta;
import kanta.JonoKentta;
import kanta.Kentta;
import kanta.SisaltaaTarkastaja;
import kanta.Tietue;

/**
 * Rekisteri -luokan jäsen, joka osaa itse huolehtia tiedoistaan esim.
 * merkki/malli ja rekisterinumero.
 * 
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 02062018
 */
public class Auto implements Cloneable, Tietue {
    private Kentta kentat[] = {

            new IntKentta("id"), new JonoKentta("merkkiMalli"), new JonoKentta("rekisteriNro"),
            new JonoKentta("vuosimalli"), new JonoKentta("mittarilukema"), new JonoKentta("polttoaine"),
            new JonoKentta("vaihteisto"),
            new JonoKentta("iskutilavuus", new SisaltaaTarkastaja(SisaltaaTarkastaja.DOUBLENUMEROT)),
            new JonoKentta("hinta") };
    private static int seuraavaNro = 1;

    public Auto() {
        // ei tarvi mitään
    }

    @Override
    public int getKenttia() {
        return kentat.length;
    }

    @Override
    public int ekaKentta() {
        return 1;
    }

    /**
     * Antaa k:n kentän sisällön merkkijonona
     * 
     * @param k
     *            monenenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     * @example
     * 
     *          <pre name="test">
     *   Auto audi = new Auto();
     *   audi.parse("1|Audi A4|AYZ-585|2013|56000|Diesel|Automaatti|3.0|52000");
     *   audi.getAvain(0) === "         1";
     *   audi.getAvain(1) === "AUDI A4";
     *   audi.getAvain(2) === "AYZ-585";
     *   audi.getAvain(15) === "";
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
     * @return Auton merkki ja malli. <@example
     * 
     *         <pre name="test">
     * Auto audi = new Auto();
     * audi.vastaaAudi();
     * audi.getMerkkiMalli() === "Audi A7";
     *         </pre>
     */
    public String getMerkkiMalli() {
        return anna(1);
    }

    /**
     * luokka joka vertaa kahta autoa
     */
    public static class Vertailija implements Comparator<Auto> {
        private final int kenttanro;

        /**
         * alustetaan vertailu tietyn kentän perusteella
         * 
         * @param k
         *            kentän indeksi
         */
        public Vertailija(int k) {
            this.kenttanro = k;
        }

        /**
         * verrataan autoja keskenään
         * 
         * @param a1
         *            verrattava auto
         * @param a2
         *            verrattava auto
         * @return <0 jos j1 < j2, == 0 jos j1 == j2 ja muuten >0
         */
        @Override
        public int compare(Auto a1, Auto a2) {
            String s1 = a1.getAvain(kenttanro);
            String s2 = a2.getAvain(kenttanro);

            return s1.compareTo(s2);
        }
    }

    /**
     * Antaa autolle seuraavan numeron rekisterissä
     * 
     * @return auton uusi tunnusNro <@example
     * 
     *         <pre name="test">
     * Auto audi = new Auto();
     * audi.getTunnusNro() === 0;
     * audi.rekisteroi();
     * Auto audi2 = new Auto();
     * audi2.rekisteroi();
     * int n1 = audi.getTunnusNro();
     * int n2 = audi2.getTunnusNro();
     * n1 === n2 - 1;
     *         </pre>
     */
    public int rekisteroi() {
        return setTunnusNro(seuraavaNro);
    }

    /**
     * @return auton tunnusNro.
     */
    public int getTunnusNro() {
        return ((IntKentta) (kentat[0])).getValue();
    }

    @Override
    public String aseta(int k, String jono) {
        try {
            String virhe = kentat[k].aseta(jono.trim());
            if (virhe == null && k == 0)
                setTunnusNro(getTunnusNro());
            return virhe;
        } catch (Exception ex) {
            return "Virhe: " + ex.getMessage();
        }
    }

    @Override
    public String anna(int k) {
        try {
            return kentat[k].toString();
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Metodi, jolla saadaan täytettyä testiarvot autolle.
     */
    public void vastaaAudi() {

        aseta(1, "Audi A7");
        aseta(2, "UYZ-585");
        aseta(3, "2013");
        aseta(4, "56000");
        aseta(5, "Diesel");
        aseta(6, "Automaatti");
        aseta(7, "3.0");
        aseta(8, "52000");
    }

    /**
     * Tulostetaan auton tiedot.
     * 
     * @param out
     *            Tietovirta johon tulostetaan.
     */
    public void tulosta(PrintStream out) {

        int pisin = 0;
        for (Kentta kentta : kentat)
            if (kentta.getKysymys().length() > pisin)
                pisin = kentta.getKysymys().length();

        for (Kentta kentta : kentat)
            out.println(Mjonot.fmt(kentta.getKysymys(), -pisin - 1) + ": " + kentta.toString());
    }

    /**
     * Tulostetaan auton tiedot
     * 
     * @param os
     *            tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
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
     * Palauttaa jäsenen tiedot merkkijonona jonka voi tallentaa tiedostoon
     * 
     * @example
     * 
     *          <pre name="test">
     *   Auto auto = new Auto();
     *   auto.parse("1|Audi A4|AYZ-585|2013|56000|Diesel|Automaatti|3.0|52000");
     *   auto.toString().startsWith("1|Audi A4|AYZ-585|") === true;
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
     * selvittää jäsenen tiedot | merkein erotellusta merkkijonosta
     * 
     * @param rivi
     *            rivi josta jäsenen tiedot erotellaan
     * @example
     * 
     *          <pre name="test">
     *   Auto auto = new Auto();
     *   auto.parse("1|Audi A4|AYZ-585|2013|56000|Diesel|Automaatti|3.0|52000");
     *   auto.getTunnusNro() === 1;
     *   auto.toString().startsWith("1|Audi A4|AYZ-585|") === true;
     *
     *   auto.rekisteroi();
     *   int n = auto.getTunnusNro();
     *   auto.parse(""+(n+20));
     *   auto.rekisteroi();
     *   auto.getTunnusNro() === n+20+1;
     * 
     *          </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++) {
            aseta(k, Mjonot.erota(sb, '|'));
        }
    }

    /**
     * Tutkii onko autojen tiedot samat
     * 
     * @param auto
     *            auto johon verrataan
     * @return true jos samat muuten false
     * @example
     * 
     *          <pre name="test">
     *   Auto auto1 = new Auto();
     *   auto1.parse("1|Audi A4|AYZ-585|2013|56000|Diesel|Automaatti|3.0|52000");
     *   Auto auto2 = new Auto();
     *   auto2.parse("1|Audi A4|AYZ-585|2013|56000|Diesel|Automaatti|3.0|52000");
     *   Auto auto3 = new Auto();
     *   auto3.parse("1|Audi A5|AYZ-585|2013|56000|Diesel|Automaatti|3.0|52000");
     *   
     *   auto1.equals(auto2) === true;
     *   auto2.equals(auto1) === true;
     *   auto1.equals(auto3) === false;
     *   auto3.equals(auto2) === false;
     *          </pre>
     */
    public boolean equals(Tietue auto) {
        if (auto == null) {
            return false;
        }
        for (int k = 0; k < getKenttia(); k++) {
            if (!anna(k).equals(auto.anna(k)))
                return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object auto) {
        if (auto instanceof Auto)
            return equals((Tietue) auto);
        return false;
    }

    @Override
    public int hashCode() {
        return getTunnusNro();
    }

    @Override
    public String getKysymys(int k) {
        try {
            return kentat[k].getKysymys();
        } catch (Exception ex) {
            return "voi ei";
        }
    }

    /**
     * tehdään klooni autosta
     * 
     * @return kloonattu auto
     * @example
     * 
     *          <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Auto auto = new Auto();
     *   auto.parse("1|Audi A4|AYZ-585|2013|56000|Diesel|Automaatti|3.0|52000");
     *   Auto kopio = auto.clone();
     *   kopio.toString() === auto.toString();
     *   auto.parse("4|BMW X4|AYZ-585|2013|56000|Diesel|Automaatti|3.0|52000");
     *   kopio.toString().equals(auto.toString()) === false;
     *          </pre>
     */
    @Override
    public Auto clone() throws CloneNotSupportedException {
        Auto uusi;
        uusi = (Auto) super.clone();
        uusi.kentat = kentat.clone();

        for (int k = 0; k < getKenttia(); k++)
            uusi.kentat[k] = kentat[k].clone();
        return uusi;
    }

    /**
     * Testiohjelma autolle.
     * 
     * @param args
     *            ei käytössä.
     */
    public static void main(String[] args) {

        Auto audi = new Auto(), audi2 = new Auto();
        audi.rekisteroi();
        audi2.rekisteroi();
        audi.tulosta(System.out);
        audi.vastaaAudi();
        audi.tulosta(System.out);
    }

}