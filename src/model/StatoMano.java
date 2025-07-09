package model;

import util.AudioManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta lo stato corrente della mano di gioco,
 * ovvero le carte giocate sul banco in una singola mano.
 * Fornisce metodi per aggiungere carte, resettare lo stato,
 * e controllare quale giocatore ha vinto la presa.
 */
public class StatoMano {
    private List<CartaBanco> carteSulBanco;

    /**
     * Costruisce un nuovo stato mano vuoto.
     */
    public StatoMano() {
        carteSulBanco = new ArrayList<>();
    }

    /**
     * Restituisce la lista delle carte attualmente sul banco.
     *
     * @return La lista delle carte sul banco.
     */
    public List<CartaBanco> getCarteSulBanco() {
        return carteSulBanco;
    }

    /**
     * Aggiunge una carta sul banco di gioco.
     * Riproduci il suono
     * Se la carta è null, non viene aggiunta.
     *
     * @param carta La carta da aggiungere sul banco.
     */
    public void aggiungiCartaSulBanco(CartaBanco carta) {
        if (carta != null) {
            AudioManager.getInstance().play("audio/scarta.wav");
            carteSulBanco.add(carta);
        }
    }

    /**
     * Resetta lo stato della mano svuotando tutte le carte sul banco.
     */
    public void reset() {
        carteSulBanco.clear();
    }

    /**
     * Controlla quale giocatore ha vinto la presa corrente.
     * La presa viene determinata dalla carta più forte dello stesso seme della prima carta giocata.
     * Assegna i punti della presa al giocatore vincitore.
     *
     * @param giocatori La lista dei giocatori partecipanti alla mano.
     * @return L'indice del giocatore vincitore nella lista {@code giocatori}.
     *         Se ci sono errori, ritorna 0 come fallback.
     */
    public int ControllaPresa(List<Giocatore> giocatori) {
        if (carteSulBanco.isEmpty()) {
            System.out.println("ERRORE: Nessuna carta sul banco!");
            return 0;
        }

        if (giocatori == null || giocatori.isEmpty()) {
            System.out.println("ERRORE: Lista giocatori vuota!");
            return 0;
        }

        System.out.println("=== CONTROLLO PRESA ===");
        System.out.println("Carte sul banco: " + carteSulBanco.size());

        // Debug: stampa tutte le carte
        for (int i = 0; i < carteSulBanco.size(); i++) {
            CartaBanco carta = carteSulBanco.get(i);
            System.out.println("Carta " + i + ": " + carta.getNomeTiratore() + " - " +
                    carta.getValore() + " di " + carta.getSeme() + " (forza: " + carta.getForza() + ")");
        }

        // Prima carta determina il seme dominante
        CartaBanco primaCarta = carteSulBanco.get(0);
        String semeDominante = primaCarta.getSeme();
        System.out.println("Seme dominante: " + semeDominante);

        // Trova la carta vincente (più forte dello stesso seme dominante)
        CartaBanco cartaVincente = primaCarta;
        for (CartaBanco carta : carteSulBanco) {
            if (carta.getSeme().equals(semeDominante) && carta.getForza() > cartaVincente.getForza()) {
                cartaVincente = carta;
            }
        }

        System.out.println("Carta vincente: " + cartaVincente.getNomeTiratore() + " - " +
                cartaVincente.getValore() + " di " + cartaVincente.getSeme());

        // Calcola i punti totali della presa secondo Tressette
        double puntiPresa = carteSulBanco.stream()
                .mapToDouble(Carta::getPuntiTressette)
                .sum();

        System.out.println("Punti della presa: " + puntiPresa);

        // Trova l'indice del giocatore vincitore
        // Gestisci Vittoria/Sconfitta sulla presa della mano
        String nomeVincitore = cartaVincente.getNomeTiratore();
        int indiceVincitore = -1;

        for (int i = 0; i < giocatori.size(); i++) {
            if (giocatori.get(i).getNome().equals(nomeVincitore)) {
                indiceVincitore = i;
                if (puntiPresa > 0){
                    if (giocatori.get(i).getRuolo().equals("user") ){
                        AudioManager.getInstance().play("audio/winHand.wav");
                    }
                    else{
                        AudioManager.getInstance().play("audio/lostHand.wav");
                    }
                }
                break;
            }
        }

        if (indiceVincitore == -1) {
            System.out.println("ERRORE CRITICO: Giocatore vincitore non trovato! Nome: " + nomeVincitore);
            System.out.println("Giocatori disponibili:");
            for (int i = 0; i < giocatori.size(); i++) {
                System.out.println("  " + i + ": " + giocatori.get(i).getNome());
            }
            // Fallback: restituisce il primo giocatore
            indiceVincitore = 0;
        } else {
            // Assegna i punti al vincitore
            try {
                giocatori.get(indiceVincitore).addPunti(puntiPresa);
                System.out.println("Giocatore " + nomeVincitore + " ha vinto la presa con " +
                        String.format("%.2f", puntiPresa) + " punti");
                System.out.println("Punteggio totale di " + nomeVincitore + ": " +
                        String.format("%.2f", giocatori.get(indiceVincitore).getPunteggio()));
            } catch (Exception e) {
                System.out.println("ERRORE nell'assegnazione punti: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Reset dello stato mano dopo aver processato la presa
        reset();

        System.out.println("=== FINE CONTROLLO PRESA ===");
        return indiceVincitore;
    }
}
