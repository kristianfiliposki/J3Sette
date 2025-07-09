package model;

import java.util.List;

/**
 * Interfaccia che definisce la strategia di gioco per scartare una carta dalla mano del giocatore.
 */
public interface Strategia {
    /**
     * Metodo che implementa la logica per scartare una carta dalla mano del giocatore.
     *
     * @param manoGiocatore La lista delle carte attualmente in mano al giocatore.
     * @param carta         La carta da considerare per la scartata (es. carta giocata sul banco).
     */
    void scarta(List<Carta> manoGiocatore, CartaBanco carta);
}
