package kanta;

/**
 * Tarkistajaluokka joka tarkistaa, sisältääkö jono vain valittuja merkkejä.
 * 
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 05062018
 * 
 */
public class SisaltaaTarkastaja implements Tarkistaja {

    public static final String NUMEROT = "0123456789"; // Numeroita vastaavat kirjaimet

    public static final String DOUBLENUMEROT = "0123456789."; // Doublemuotoon sopivat numerot

    private final String sallitut;

    /**
     * Luo tarkistajan, joka hyväksyy merkit, jotka sallitaan
     * 
     * @param sallitut
     *            sallittavat merkit
     */
    public SisaltaaTarkastaja(String sallitut) {
        this.sallitut = sallitut;
    }

    /**
     * Tarkistaa, onko jonossa vain sallittuja merkkejä
     * 
     * @param jono
     *            jono, jota tutkitaan
     * @param sallitut
     *            sallittavat merkit
     * @return true jos vain sallittuja merkkejä, muuten false
     * 
     * @example
     * 
     *          <pre name="test">
     * onkoVain("656", "2") === false;
     * onkoVain("65", "656") === true;
     *          </pre>
     */
    public static boolean onkoVain(String jono, String sallitut) {
        for (int i = 0; i < jono.length(); i++)
            if (sallitut.indexOf(jono.charAt(i)) < 0)
                return false;
        return true;
    }

    /**
     * Tarkistaa, että jono sisältää pelkästään valittuja numeroita
     * 
     * @param jono
     *            jono jota tutkitaan
     * @return null jos pelkästään valittuja numeroita, muuten virhe
     * 
     * @example
     * 
     *          <pre name="test">
     * SisaltaaTarkastaja tarkistaja = new SisaltaaTarkastaja("656");
     * tarkistaja.tarkista("6") === null;
     * tarkistaja.tarkista("4") === "Saa olla vain merkkejä: 656";
     *          </pre>
     */
    @Override
    public String tarkista(String jono) {
        if (onkoVain(jono, sallitut))
            return null;
        return "Saa olla vain merkkejä: " + sallitut;
    }
}