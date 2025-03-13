package kanta;

import javax.swing.SwingConstants;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Kenttä, joka tallentaa kokonaislukuja
 * 
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 04062018
 *
 */

public class IntKentta extends PerusKentta {

    private int arvo;

    /**
     * Asetetaan kentän arvo
     * 
     * @param value
     *            asetettava arvo
     */
    public void setValue(int value) {
        arvo = value;
    }

    /**
     * Alustetaan kenttä
     * 
     * @param kysymys ,
     *            joka näytetään
     */
    public IntKentta(String kysymys) {
        super(kysymys);
    }

    /**
     * Hakee kentän arvon
     * 
     * @return kentän arvo
     */
    public int getValue() {
        return arvo;
    }

    /**
     * Asetetaan kentän arvo merkkijonosta
     * 
     * @param jono
     *            kenttään asetettava arvo
     * @return null, jos hyvä arvo, muuten virhe
     * 
     * @example
     * 
     *          <pre name="test">
     * IntKentta k = new IntKentta("määrä");
     * k.aseta("3") === null;
     * k.getValue() === 3;
     * k.aseta("auto") === "Ei kokonaisluku (auto)";
     * k.getValue() === 3;
     *          </pre>
     */
    @Override
    public String aseta(String jono) {
        StringBuffer sb = new StringBuffer(jono);
        try {
            this.arvo = Mjonot.erotaEx(sb, ' ', 0);
            return null;
        } catch (NumberFormatException ex) {
            return "Ei kokonaisluku (" + jono + ")";
        }
    }

    /**
     * Palauttaa kentän tiedot merkkijonona
     * 
     * @return merkkijono kentästä
     * 
     * @example
     * 
     *          <pre name="test">
     * IntKentta k = new IntKentta("määrä");
     * k.aseta("3");
     * k.getAvain() === "         3";
     * k.aseta("50");
     * k.getAvain() === "        50";
     *          </pre>
     */
    @Override
    public String getAvain() {
        return Mjonot.fmt(arvo, 10);
    }

    /**
     * @return sijainti kentälle
     */
    @Override
    public int getSijainti() {
        return SwingConstants.RIGHT;
    }

    /**
     * @return kopio oliosta
     * 
     * @example
     * 
     *          <pre name="test">
     * #THROWS CloneNotSupportedException
     * IntKentta k = new IntKentta("määrä");
     * k.aseta("3");
     * IntKentta klooni = (IntKentta)k.clone();
     * k.getValue() === klooni.getValue();
     * k.aseta("30");
     * k.getValue() === 30;
     * klooni.getValue() === 3;
     *          </pre>
     */
    @Override
    public IntKentta clone() throws CloneNotSupportedException {
        return (IntKentta) super.clone();
    }

    /**
     * Kentän arvo merkkijonona.
     * 
     * @return kenttä merkkijonona
     * @example
     * 
     *          <pre name="test">
     * IntKentta k = new IntKentta("määrä");
     * k.aseta("3") === null;
     * k.toString() === "3";
     *          </pre>
     */
    @Override
    public String toString() {
        return "" + arvo;
    }
}