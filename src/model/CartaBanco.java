package model;

/**
 * Estende la classe {@link Carta} aggiungendo l'informazione del nome del giocatore che ha giocato la carta.
 * Rappresenta una carta sul banco di gioco con il riferimento al tiratore.
 */
public class CartaBanco extends Carta {
    private String NomeTiratore;

    /**
     * Costruisce una carta sul banco specificando il nome del tiratore e le caratteristiche della carta.
     *
     * @param NomeTiratore Il nome del giocatore che ha giocato la carta.
     * @param valore       Il valore numerico della carta.
     * @param seme         Il seme della carta.
     * @param forza        La forza della carta, usata per determinare la presa.
     * @param immagine     Il percorso dell'immagine associata alla carta (non usato direttamente).
     */
    public CartaBanco(String NomeTiratore, int valore, String seme, int forza, String immagine) {
        super(valore, seme, forza, immagine);
        this.NomeTiratore = NomeTiratore;
    }

    /**
     * Restituisce il nome del giocatore che ha giocato questa carta.
     *
     * @return Il nome del tiratore.
     */
    public String getNomeTiratore() {
        return NomeTiratore;
    }
}
