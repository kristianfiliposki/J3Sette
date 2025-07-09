package model;

import java.util.List;

/**
 * Implementazione della strategia di gioco per un giocatore umano.
 * La strategia consiste nel rimuovere dalla mano tutte le carte che corrispondono
 * per valore e seme alla carta specificata.
 */
public class StrategiaUmano implements Strategia {
    /**
     * Scarta dalla mano del giocatore tutte le carte che hanno lo stesso valore e seme della carta passata.
     *
     * @param manoGiocatore La lista delle carte del giocatore umano.
     * @param carta         La carta da confrontare per la scartata.
     */
    @Override
    public void scarta(List<Carta> manoGiocatore, CartaBanco carta) {
        manoGiocatore.removeIf(c ->
                c.getValore() == carta.getValore() &&
                        c.getSeme().equals(carta.getSeme())
        );
    }
}
