package kanta;

/**
 * Tarkistin joka tarkistaa, että jono on desimaalilukuna
 * 
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 05062018
 */
public class DesimaaliTarkistaja implements Tarkistaja {

    /**
     * tarkistetaan onko desimaaliluku
     * 
     * @param jono
     *            tarkistettava merkkijon
     * @example
     * 
     *          <pre name="test">
     *   DesimaaliTarkistaja desim = new DesimaaliTarkistaja();
     *   desim.tarkista("") === null;
     *   desim.tarkista("54.3") === null;
     *   desim.tarkista("21,8") === null;
     *   desim.tarkista("89") === null;
     *   desim.tarkista("1pf") === "Ei desimaaliluku (1pf)"; 
     *   desim.tarkista("7..4") === "Ei desimaaliluku (7..4)";
     *          </pre>
     */
    @Override
    public String tarkista(String jono) {
        if (jono.length() == 0)
            return null;
        if (!jono.matches("[0-9]*[.,]?[0-9]*"))
            return "Ei desimaaliluku (" + jono + ")";
        return null;
    }
}