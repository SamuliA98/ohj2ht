package kanta;

/**
 * Yleisen tarkistajan rajapinta
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 05062018
 * 
 */

public interface Tarkistaja {
    
    /**
     * Kertoo, sopiiko syötetty merkkijono sisällöksi kentälle.
     * @param jono jono jota tutkitaan
     * @return null jos jono sopii, muuten virhe
     */
    String tarkista(String jono);
}