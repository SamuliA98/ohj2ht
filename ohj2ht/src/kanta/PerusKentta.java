package kanta;

import javax.swing.SwingConstants;

/**
 * Yleinen peruskenttä, toteuttaa tarkistuskäsittelyn ja kysymyksen käsittelyn.
 *
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 22052018
 */
public abstract class PerusKentta implements Kentta {

    private final String kysymys;

    /**
     * Viite tarkistajalle
     */
    protected Tarkistaja tarkistaja = null;

    /**
     * Kentän alustus kysymys- ja tarkistajatiedoilla.
     * @param kysymys , kun kysytään kenttää.
     * @param tarkistaja , luokka joka tarkistaa syötön
     */
    public PerusKentta(String kysymys, Tarkistaja tarkistaja) {
        this.kysymys = kysymys;
        this.tarkistaja = tarkistaja;
    }

    /**
     * Kentän alustus kysymystiedoilla.
     * @param kysymys , kun kysytään kenttää.
     */
    public PerusKentta(String kysymys) {
        this.kysymys = kysymys;
    }

    /**
     * Kentän tiedot merkkijonona jota voi vertailla-
     * @return kentästä vertailtava merkkijono
     */
    @Override
    public String getAvain() {
        return toString().toUpperCase();
    }

    /**
     * @param jono , josta otetaan kentän arvo
     */
    @Override
    public abstract String aseta(String jono);

    /**
     * @return kopio oliosta
     */
    @Override
    public Kentta clone() throws CloneNotSupportedException {
        return (Kentta) super.clone();
    }
    
    /**
     * @return kysymys, joka vastaa kenttää
     * @see kanta.Kentta#getKysymys()
     */
    @Override
    public String getKysymys() {
        return kysymys;
    }
    
    /**
     * @return kentän arvo merkkijonona
     * @see kanta.Kentta#toString()
     */
    @Override
    public abstract String toString();

    /**
     * @return kahden olion keskenään vertailu
     */
    @Override
    public int compareTo(Kentta k) {
        return getAvain().compareTo(k.getAvain());
    }
    
    /**
     * @return kentälle vaakasuuntainen sijainti
     */
    @Override
    public int getSijainti() {
        return SwingConstants.LEFT;
    }

    
}