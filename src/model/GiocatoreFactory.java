package model;

/**
 * Factory per la creazione di istanze di {@link Giocatore}.
 * Fornisce metodi statici per creare giocatori con ruoli specifici (umano o bot).
 */
public class GiocatoreFactory {

    /**
     * Crea un giocatore generico con il ruolo e nome specificati.
     * Il punteggio iniziale e le partite vinte sono impostati a zero.
     *
     * @param ruolo Il ruolo del giocatore ("user" o "bot").
     * @param nome  Il nome del giocatore.
     * @return Una nuova istanza di {@link Giocatore} con i parametri specificati.
     */
    public static Giocatore creaGiocatore(String ruolo, String nome) {
        return new Giocatore(ruolo, nome, 0, 0, null);
    }

    /**
     * Crea un giocatore umano con il nome specificato.
     * Il ruolo viene impostato automaticamente a "user".
     * Il punteggio iniziale e le partite vinte sono impostati a zero.
     *
     * @param nome Il nome del giocatore umano.
     * @return Una nuova istanza di {@link Giocatore} con ruolo "user".
     */
    public static Giocatore creaGiocatoreUmano(String nome) {
        return new Giocatore("user", nome, 0, 0, null);
    }

    /**
     * Crea un giocatore bot con il nome specificato.
     * Il ruolo viene impostato automaticamente a "bot".
     * Il punteggio iniziale e le partite vinte sono impostati a zero.
     *
     * @param nome Il nome del giocatore bot.
     * @return Una nuova istanza di {@link Giocatore} con ruolo "bot".
     */
    public static Giocatore creaGiocatoreBot(String nome) {
        return new Giocatore("bot", nome, 0, 0, null);
    }
}
