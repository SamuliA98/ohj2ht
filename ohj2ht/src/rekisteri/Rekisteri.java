package rekisteri;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Rekisteri -luokka huolehtii tietokannoista. Pääosin kaikki metodit ovat
 * "välittäjämetodeja".
 * 
 * @author Aki-Petteri Anttonen, Samuli Apell
 * @version 01062018
 * 
 * 
 * @example
 * 
 *          <pre name="testJAVA">
 * #import rekisteri.SailoException;
 *  private Rekisteri rekisteri;
 *  private Auto audi1;
 *  private Auto audi2;
 *  private int autoid1;
 *  private int autoid2;
 *  private Huolto oljynvaihto1;
 *  private Huolto oljynvaihto2;
 *  private Huolto oljynvaihto3; 
 *  private Huolto oljynvaihto4; 
 *  private Huolto oljynvaihto5;
 *  
 *  public void alustaRekisteri() {
 *    rekisteri = new Rekisteri();
 *    audi1 = new Auto(); audi1.vastaaAudi(); audi1.rekisteroi();
 *    audi2 = new Auto(); audi2.vastaaAudi(); audi2.rekisteroi();
 *    autoid1 = audi1.getTunnusNro();
 *    autoid2 = audi2.getTunnusNro();
 *    oljynvaihto1 = new Huolto(autoid2); oljynvaihto1.vastaaOljynvaihto(autoid2);
 *    oljynvaihto2 = new Huolto(autoid1); oljynvaihto2.vastaaOljynvaihto(autoid1);
 *    oljynvaihto3 = new Huolto(autoid2); oljynvaihto3.vastaaOljynvaihto(autoid2); 
 *    oljynvaihto4 = new Huolto(autoid1); oljynvaihto4.vastaaOljynvaihto(autoid1); 
 *    oljynvaihto5 = new Huolto(autoid2); oljynvaihto5.vastaaOljynvaihto(autoid2);
 *    try {
 *    rekisteri.lisaa(audi1);
 *    rekisteri.lisaa(audi2);
 *    rekisteri.lisaa(oljynvaihto1);
 *    rekisteri.lisaa(oljynvaihto2);
 *    rekisteri.lisaa(oljynvaihto3);
 *    rekisteri.lisaa(oljynvaihto4);
 *    rekisteri.lisaa(oljynvaihto5);
 *    } catch ( Exception e) {
 *       System.err.println(e.getMessage());
 *    }
 *  }
 *          </pre>
 */
public class Rekisteri {
    private Autot autot = new Autot();
    private Huollot huollot = new Huollot();

    /**
     * Haetaan kaikki auton huollot
     * 
     * @param auto
     *            auto jolle huollot haetaan
     * @return tietorakenne jossa viitteet löydettyihin huoltoihin
     * @throws SailoException jos virhe
     * @example
     * 
     *          <pre name="test">
     * #THROWS SailoException
     * #import java.util.*;
     * 
     *  alustaRekisteri();
     *  Auto audi3 = new Auto();
     *  audi3.rekisteroi();
     *  rekisteri.lisaa(audi3);
     *  
     *  List<Huolto> loytyneet;
     *  loytyneet = rekisteri.annaHuollot(audi3);
     *  loytyneet.size() === 0; 
     *  loytyneet = rekisteri.annaHuollot(audi1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == oljynvaihto2 === true;
     *  loytyneet.get(1) == oljynvaihto4 === true;
     *  loytyneet = rekisteri.annaHuollot(audi2);
     *  loytyneet.size() === 3; 
     *  loytyneet.get(0) == oljynvaihto1 === true;
     *          </pre>
     */
    public List<Huolto> annaHuollot(Auto auto) throws SailoException {
        return huollot.annaHuollot(auto.getTunnusNro());
    }

    /**
     * 
     * Asetetaan huollot muutetuksi, niin pakotetaan tallennus.
     */
    public void setHuoltoMuutos() {
        huollot.setMuutos();
    }

    /**
     * Poistaa autoista ja huolloista auton tiedot.
     * 
     * @param auto
     *            poistettava auto.
     * @return poistettujen autojen lkm. * @example
     * 
     *         <pre name="test">
     * #THROWS Exception
     *   alustaRekisteri();
     *   rekisteri.etsi("*",0).size() === 2;
     *   rekisteri.annaHuollot(audi1).size() === 2;
     *   rekisteri.poista(audi1) === 1;
     *   rekisteri.etsi("*",0).size() === 1;
     *   rekisteri.annaHuollot(audi1).size() === 0;
     *   rekisteri.annaHuollot(audi2).size() === 3;
     *         </pre>
     */
    public int poista(Auto auto) {
        if (auto == null)
            return 0;
        int poistettu = autot.poista(auto.getTunnusNro());
        huollot.poista(auto.getTunnusNro());
        return poistettu;
    }

    /**
     * Poistaa huollon
     * 
     * @param huolto
     *            huolto joka poistetaan
     * 
     *            <pre name="test">
     * #THROWS Exception
     *   alustaRekisteri();
     *   rekisteri.annaHuollot(audi1).size() === 2;
     *   rekisteri.poistaHuolto(oljynvaihto2);
     *   rekisteri.annaHuollot(audi1).size() === 1;
     *            </pre>
     */
    public void poistaHuolto(Huolto huolto) {
        huollot.poista(huolto);
    }

    /**
     * Asettaa perusnimet tiedostoille.
     * @param nimi lisättävä nimi
     * 
     * 
     * 
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if (!nimi.isEmpty())
            hakemistonNimi = nimi + "/";
        autot.setTiedostonPerusNimi(hakemistonNimi + "autot");
        huollot.setTiedostonPerusNimi(hakemistonNimi + "huollot");
    }

    /**
     * Lukee rekisterin tiedot tiedostosta
     * 
     * @param nimi
     *            jota kätetään lukemisessa
     * @throws SailoException
     *             jos lukeminen epäonnistuu * @example
     * 
     *             <pre name="test">
     * #THROWS SailoException 
     * #import java.io.*;
     * #import java.util.*;
     * 
     *   
     *  String hakemisto = "TAutot";
     *  File dir = new File(hakemisto);
     *  File ftied  = new File(hakemisto+"/autot.dat");
     *  File fhtied = new File(hakemisto+"/huollot.dat");
     *  dir.mkdir();  
     *  ftied.delete();
     *  fhtied.delete();
     *  rekisteri = new Rekisteri();
     *  rekisteri.lueTiedostosta(hakemisto); #THROWS SailoException
     *  alustaRekisteri();
     *  rekisteri.setTiedosto(hakemisto);
     *  rekisteri.tallenna(); 
     *  rekisteri = new Rekisteri();
     *  rekisteri.lueTiedostosta(hakemisto);
     *  Collection<Auto> kaikki = rekisteri.etsi("",-1); 
     *  Iterator<Auto> it = kaikki.iterator();
     *  it.next() === audi1;
     *  it.next() === audi2;
     *  it.hasNext() === false;
     *  List<Huolto> loytyneet = rekisteri.annaHuollot(audi1);
     *  Iterator<Huolto> ih = loytyneet.iterator();
     *  ih.next() === oljynvaihto2;
     *  ih.next() === oljynvaihto4;
     *  ih.hasNext() === false;
     *  loytyneet = rekisteri.annaHuollot(audi2);
     *  ih = loytyneet.iterator();
     *  ih.next() === oljynvaihto1;
     *  ih.next() === oljynvaihto3;
     *  ih.next() === oljynvaihto5;
     *  ih.hasNext() === false;
     *  rekisteri.lisaa(audi2);
     *  rekisteri.lisaa(oljynvaihto5);
     *  rekisteri.tallenna();
     *  ftied.delete()  === true;
     *  fhtied.delete() === true;
     *  File fbak = new File(hakemisto+"/autot.bak");
     *  File fhbak = new File(hakemisto+"/huollot.bak");
     *  fbak.delete() === true;
     *  fhbak.delete() === true;
     *  dir.delete() === true;
     *             </pre>
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        autot = new Autot();
        huollot = new Huollot();

        setTiedosto(nimi);
        autot.lueTiedostosta();
        huollot.lueTiedostosta();
    }

    /**
     * Lisää rekisteriin uuden auton
     * 
     * @param auto
     *            lisättävä auto
     * @throws SailoException
     *             jos lisäystä ei voi tehdä
     * @example
     * 
     *          <pre name="test">
     * #THROWS SailoException  
     *  alustaRekisteri();
     *  rekisteri.etsi("*",0).size() === 2;
     *  rekisteri.lisaa(audi1);
     *  rekisteri.etsi("*",0).size() === 3;
     *          </pre>
     */
    public void lisaa(Auto auto) throws SailoException {
        autot.lisaa(auto);

    }

    /**
     * Lisätään uusi huolto rekisteriin
     * 
     * @param huolto
     *            lisättävä huolto
     * @throws SailoException virhetila
     */
    public void lisaa(Huolto huolto) throws SailoException {
        huollot.lisaa(huolto);
    }

    /**
     * Palauttaa viitteet autoista, jotka vastaavat hakuehtoa.
     * 
     * @param hakuehto hakuna käytettävä ehto
     * @param k
     *            Etsittävän kentän indeksi.
     * @return Tietorakenne löydetyistä autoista.
     * @throws SailoException
     *             jos jokin ei toimi.
     * @example
     * 
     *          <pre name="test">
     *   #THROWS CloneNotSupportedException, SailoException
     *   alustaRekisteri();
     *   Auto audi3 = new Auto(); audi3.rekisteroi();
     *   audi3.aseta(1,"Audi A7");
     *   rekisteri.lisaa(audi3);
     *   Collection<Auto> loytyneet = rekisteri.etsi("*Audi*",0);
     *   loytyneet.size() === 3;
     *   Iterator<Auto> it = loytyneet.iterator();
     *   it.next() == audi1 === true;
     *          </pre>
     */
    public Collection<Auto> etsi(String hakuehto, int k) throws SailoException {
        return autot.etsi(hakuehto, k);
    }

    /**
     * Tallentaa tiedostoon rekisterin tiedot.
     * 
     * @throws SailoException
     *             jos jokin ei toimi.
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            autot.tallenna();
        } catch (SailoException ex) {
            virhe = ex.getMessage();
        }

        try {
            huollot.tallenna();
        } catch (SailoException ex) {
            virhe += ex.getMessage();
        }
        if (!"".equals(virhe))
            throw new SailoException(virhe);
    }

    /**
     * Korvaa huollon tietorakenteessa tai lisää uutena
     * 
     * @param huolto
     *            lisättävä huolto
     * @throws SailoException
     *             jos tietorakenne täynnä
     */
    public void korvaaTaiLisaa(Huolto huolto) throws SailoException {
        huollot.korvaaTaiLisaa(huolto);
    }

    /**
     * Korvaa auton tietorakenteessa tai lisää uutena.
     * 
     * @param auto
     *            lisättävä auto
     * @throws SailoException
     *             jos tietorakenne täynnä
     */
    public void korvaaTaiLisaa(Auto auto) throws SailoException {
        autot.korvaaTaiLisaa(auto);
    }

    /**
     * Testiohjelma rekisteristä
     * 
     * @param args
     *            ei käytössä
     */
    public static void main(String[] args) {
        Rekisteri rekisteri = new Rekisteri();

        try {

            Auto audi = new Auto(), audi2 = new Auto();
            audi.rekisteroi();
            audi.vastaaAudi();
            audi2.rekisteroi();
            audi2.vastaaAudi();

            rekisteri.lisaa(audi);
            rekisteri.lisaa(audi2);
            int id1 = audi.getTunnusNro();
            int id2 = audi2.getTunnusNro();
            Huollot huollot = new Huollot();
            Huolto huolto6 = new Huolto(1);
            huolto6.vastaaOljynvaihto(id1);
            huollot.lisaa(huolto6);
            Huolto huolto3 = new Huolto(2);
            huolto3.vastaaOljynvaihto(id1);
            huollot.lisaa(huolto3);
            Huolto huolto7 = new Huolto(2);
            huolto7.vastaaOljynvaihto(id2);
            huollot.lisaa(huolto7);
            Huolto huolto4 = new Huolto(1);
            huolto4.vastaaOljynvaihto(id2);
            huollot.lisaa(huolto4);
            Huolto huolto9 = new Huolto(1);
            huolto9.vastaaOljynvaihto(id2);
            huollot.lisaa(huolto9);

            System.out.println("============== Rekisterin testi ==============");
            Collection<Auto> autot = rekisteri.etsi("", -1);
            int i = 0;
            for (Auto auto : autot) {
                System.out.println("Auto paikassa: " + i);
                auto.tulosta(System.out);
                List<Huolto> loytyneet = rekisteri.annaHuollot(auto);
                for (Huolto huolto : loytyneet) {
                    huolto.tulosta(System.out);
                }
                i++;
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }

}