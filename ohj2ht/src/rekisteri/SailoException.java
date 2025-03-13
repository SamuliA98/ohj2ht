package rekisteri;

/**
 * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille.
 * 
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 13032018
 *
 */
public class SailoException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Poikkeuksen muodostaja jolle tuodaan poikkeusviesti.
     * 
     * @param viesti
     *            poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }
}