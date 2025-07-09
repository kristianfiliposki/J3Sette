package model;

import java.util.List;

/**
 * Implementazione della strategia di gioco per un giocatore bot.
 * La strategia semplice consiste nel rimuovere la prima carta disponibile dalla mano.
 */
public class StrategiaBot implements Strategia {
    /**
     * Scarta la prima carta disponibile nella mano del bot.
     *
     * @param manoGiocatore La lista delle carte del bot.
     * @param carta         La carta da considerare (non usata in questa strategia).
     */
    @Override
    public void scarta(List<Carta> manoGiocatore, CartaBanco carta) {
        if (!manoGiocatore.isEmpty()) {
            manoGiocatore.remove(0);
        }
    }
}
