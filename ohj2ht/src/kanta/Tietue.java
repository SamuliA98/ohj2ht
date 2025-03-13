package kanta;

/**
 * Tietueen rajapinta
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 05062018
 * 
 */

public interface Tietue {
    
    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k kenttä jonka sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    String anna(int k);
    
    /**
     * Palauttaa k:tta jäsenen kenttää vastaavan kysymyksen
     * @param k kuinka monennen kentän kysymys palautetaan
     * @return k:netta kenttää vastaava kysymys
     */
    String getKysymys(int k);
    
    /**
     * Palauttaa auton kenttien lukumäärän
     * @return kenttien lukumäärä
     */
    int getKenttia();

    /**
     * ensimmäinen kysyttävä kenttä
     * @return ensimmäisen kentän indeksi
     */
    int ekaKentta();

    
    /**
     * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
     * @param k kenttä jonka arvo asetetaan
     * @param jono jonoa joka asetetaan kentän arvoksi
     * @return null jos asettaminen onnistuu
     */
    String aseta(int k, String jono);

}