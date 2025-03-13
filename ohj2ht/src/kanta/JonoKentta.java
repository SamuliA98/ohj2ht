package kanta;

/**
 * Kenttä perusmerkkijonoja varten.
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 05062018
 *
 */
public class JonoKentta extends PerusKentta {
    
    private String jono = "";

    /**
     * Alustetaan kenttä 
     * @param kysymys kysymys joka esitetään
     * 
     * @example
     * <pre name="test">
     * JonoKentta j = new JonoKentta("merkki");
     * j.getKysymys() === "merkki";
     * j.toString() === "";
     * j.aseta("Audi");
     * j.toString() === "Audi";
     * </pre>
     */
    public JonoKentta(String kysymys) {
        super(kysymys);
    }

    /**
     * Alustetaan kysymyksellä ja tarkistajalla.
     * @param kysymys kysymys joka esitetään
     * @param tarkistaja tarkistajaluokka joka tarkistaa syötön oikeellisuuden
     */
    public JonoKentta(String kysymys, Tarkistaja tarkistaja) {
        super(kysymys, tarkistaja);
    }

    /**
     * @param s merkkijono joka asetetaan kentän arvoksi
     */
    @Override
    public String aseta(String s) {
        if (tarkistaja == null) {
            this.jono = s;
            return null;
        }

        String virhe = tarkistaja.tarkista(s);
        if (virhe == null) {
            this.jono = s;
            return null;
        }
        return virhe;
    }
    
    /**
     * @return Palauetaan kentän sisältö
     */
    @Override
    public String toString() {
        return jono;
    }

}