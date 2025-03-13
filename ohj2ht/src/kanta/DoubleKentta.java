package kanta;

import javax.swing.SwingConstants;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Kenttä reaalilukuja varten
 * 
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 04062018
 */
public class DoubleKentta extends PerusKentta {

    private double arvo;

    /**
     * Alustetaan kenttään kysymys
     * 
     * @param kysymys
     *            kentässä esitettävä kysymys
     */
    public DoubleKentta(String kysymys) {
        super(kysymys, new DesimaaliTarkistaja());
    }

    /**
     * @return Kentän arvo
     */
    public double getValue() {
        return arvo;
    }

    /**
     * Asetetaan kentälle arvo
     * 
     * @param value
     *            kentälle asetettava arvo
     */
    public void setValue(double value) {
        arvo = value;
    }

    /**
     * @return kentän arvo merkkijonona
     */
    @Override
    public String toString() {
        return "" + arvo;
    }

    /**
     * @param jono
     *            asetettava jono
     * @example
     * 
     *          <pre name="test">
     * DoubleKentta kentta = new DoubleKentta("summa");
     * kentta.aseta("moi") === "Ei desimaaliluku (moi)";  kentta.getValue() ~~~ 0.0;  
     * kentta.aseta("17.5")  === null;  kentta.getValue() ~~~ 17.5; 
     * kentta.aseta("4re")  === "Ei desimaaliluku (4re)";  kentta.getValue() ~~~ 17.5;
     *          </pre>
     */
    @Override
    public String aseta(String jono) {
        String virhe = null;
        if (tarkistaja != null)
            virhe = tarkistaja.tarkista(jono);
        if (virhe != null)
            return virhe;
        this.arvo = Mjonot.erotaDouble(jono, 0.0);
        return null;
    }

    /**
     * Palauttaa kentän tiedot
     * 
     * @return merkkijono kentästä
     * @example
     * 
     *          <pre name="test">
     * DoubleKentta kentta = new DoubleKentta("määrä");
     *                                         //  123456789012345678
     * kentta.aseta("9");  kentta.getAvain() === "          9.000000";
     * kentta.aseta("17");   kentta.getAvain() === "         17.000000";
     * kentta.aseta("798"); kentta.getAvain() === "        798.000000";
     * 
     *          </pre>
     */
    @Override
    public String getAvain() {
        return Mjonot.fmt(arvo, 18, 6);
    }

    /**
     * @return sijainti kentälle
     */
    @Override
    public int getSijainti() {
        return SwingConstants.RIGHT;
    }
}