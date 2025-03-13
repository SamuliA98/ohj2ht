package kanta;

/**
 * Rajapinta yhdelle kentälle tietueessa
 * 
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 05062018
 */

public interface Kentta extends Cloneable, Comparable<Kentta> {

    /**
     * Kentän tiedot merkkijonona jota voi vertailla.
     * 
     * @return kentästä koostuva merkkijono
     * 
     * @example
     * 
     *          <pre name="test">
     * IntKentta k1 = new IntKentta("määrä");
     * IntKentta k2 = new IntKentta("määrä");
     * k1.aseta("30");
     * k2.aseta("500");
     * k1.getAvain().compareTo(k2.getAvain()) > 0 === false;
     *          </pre>
     */
    String getAvain();

    /**
     * Kenttään liittyvän kysymyksen palautus.
     * 
     * @return kysymys kenttään liittyen
     * 
     * @example
     * 
     *          <pre name="test">
     * IntKentta k = new IntKentta("määrä");
     * k.getKysymys() === "määrä";
     *          </pre>
     */
    String getKysymys();

    /**
     * @return kentän kopio
     * @throws CloneNotSupportedException
     * 
     * @example
     * 
     *          <pre name="test">
     * #THROWS CloneNotSupportedException
     * IntKentta k1 = new IntKentta("määrä");
     * k1.aseta("30");
     * IntKentta k2 = k1.clone();
     * k1.toString() === k2.toString();
     * k1.aseta("21");
     * k1.toString() == k2.toString() === false;
     *          </pre>
     */
    Kentta clone() throws CloneNotSupportedException;

    /**
     * Asettaa kentän sisältöä.
     * 
     * @param jono
     *            jono josta otetaan tietoja
     * @return null jos sisältö on ok, muuten virhe
     * 
     * @example
     * 
     *          <pre name="test">
     * IntKentta k = new IntKentta("määrä");
     * k.aseta("30") === null;
     * k.getValue() === 30;
     * k.aseta("j") === "Ei kokonaisluku (j)";
     * k.getValue() === 30;
     *          </pre>
     */
    String aseta(String jono);

    /**
     * @return kentälle sijainti
     * 
     * @example
     * 
     *          <pre name="test">
     * #import javax.swing.SwingConstants;
     * IntKentta k1 = new IntKentta("määrä");
     * k1.getSijainti() === SwingConstants.RIGHT;
     *          </pre>
     */
    int getSijainti();

    /**
     * Antaa kentän arvon merkkijonona.
     * 
     * @return kentän arvo merkkijonona
     * 
     * @example
     * 
     *          <pre name="test">
     * IntKentta k = new IntKentta("määrä");
     * k.aseta("20") === null;
     * k.toString() === "20";
     *          </pre>
     */
    @Override
    String toString();
}