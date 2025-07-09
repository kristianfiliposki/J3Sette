package model;

import java.util.List;
import java.util.Optional;

/**
 * Rappresenta il tavolo di gioco, che gestisce lo stato della partita,
 * i giocatori, il mazzo di carte e lo stato della mano corrente.
 * Estende {@link Observable} per notificare le modifiche agli osservatori.
 */
public class Tavolo extends Observable {
    private String immagine;
    private int numeroPartita;
    private List<Giocatore> giocatori;
    private Mazzo mazzo;
    private StatoMano statoMano;

    /**
     * Costruisce un tavolo di gioco con immagine, numero partita, lista giocatori e mazzo.
     *
     * @param immagine      Il percorso o identificativo dell'immagine del tavolo.
     * @param numeroPartita Il numero progressivo della partita.
     * @param giocatori     La lista dei giocatori partecipanti.
     * @param mazzo         Il mazzo delle carte da utilizzare.
     */
    public Tavolo(String immagine, int numeroPartita, List<Giocatore> giocatori, Mazzo mazzo) {
        this.immagine = immagine;
        this.numeroPartita = numeroPartita;
        this.giocatori = giocatori;
        this.mazzo = mazzo;
        this.statoMano = new StatoMano();
    }

    /**
     * Restituisce il percorso o identificativo dell'immagine del tavolo.
     *
     * @return La stringa dell'immagine del tavolo.
     */
    public String getImmagine() {
        return immagine;
    }

    /**
     * Restituisce la lista dei giocatori partecipanti alla partita.
     *
     * @return La lista dei giocatori.
     */
    public List<Giocatore> getGiocatori() {
        return giocatori;
    }

    /**
     * Restituisce lo stato corrente della mano di gioco.
     *
     * @return Lo stato della mano ({@link StatoMano}).
     */
    public StatoMano getStatoMano() {
        return statoMano;
    }

    /**
     * Resetta lo stato del tavolo e dei giocatori:
     * azzera i punteggi, resetta le mani dei giocatori, resetta il mazzo e lo stato della mano,
     * infine notifica gli osservatori.
     */
    public void reset() {
        for (Giocatore g : giocatori) {
            g.setPunteggio(0.0);
            g.resetMano();
        }
        mazzo.reset();
        statoMano.reset();
        notifyObservers();
    }

    /**
     * Inizia una nuova partita:
     * mescola il mazzo, resetta le mani dei giocatori,
     * distribuisce le carte in base al numero di giocatori,
     * e notifica gli osservatori.
     *
     * @throws IllegalArgumentException se il numero di giocatori non è supportato.
     */
    public void iniziaPartita() {
        mazzo.mischia();

        for (Giocatore g : giocatori) {
            g.resetMano();
        }

        int numGiocatori = giocatori.size();
        int cartePerGiocatore;

        if (numGiocatori == 2) {
            cartePerGiocatore = 15; // variante per 2 giocatori, da adattare se serve
        } else if (numGiocatori == 3 || numGiocatori == 4) {
            cartePerGiocatore = 10;
        } else {
            throw new IllegalArgumentException("Numero di giocatori non supportato: " + numGiocatori);
        }

        int cartePerMano = 5;
        int mani = cartePerGiocatore / cartePerMano;

        for (int mano = 0; mano < mani; mano++) {
            for (int i = 0; i < cartePerMano; i++) {
                for (Giocatore g : giocatori) {
                    Carta carta = mazzo.distribuisciCarta();
                    if (carta != null) {
                        g.addCarta(carta);
                    }
                }
            }
        }

        notifyObservers();
    }

    /**
     * Gestisce la giocata di una carta da parte di un giocatore.
     * Utilizza la strategia di gioco del giocatore per scartare la carta,
     * aggiunge la carta scartata allo stato della mano e notifica gli osservatori.
     *
     * @param giocatore Il giocatore che gioca la carta.
     * @param carta     La carta da giocare.
     */
    public void giocaCarta(Giocatore giocatore, Carta carta) {
        if (giocatore.getStrategiaGiocata() != null) {
            CartaBanco cartaDaScartare = new CartaBanco(
                    giocatore.getNome(),
                    carta.getValore(),
                    carta.getSeme(),
                    carta.getForza(),
                    carta.getImmagine()
            );

            // Scarta la carta usando la strategia
            giocatore.getStrategiaGiocata().scarta(giocatore.getCarte(), cartaDaScartare);

            // Aggiungi la carta scartata allo stato della mano
            statoMano.aggiungiCartaSulBanco(cartaDaScartare);

            // Notifica gli observer
            notifyObservers();
        }
    }

    /**
     * Gestisce la fine della partita:
     * incrementa il numero della partita, verifica se tutte le mani sono vuote,
     * determina il vincitore con il punteggio più alto, incrementa le sue partite vinte,
     * stampa il vincitore e notifica gli osservatori passando il nome del vincitore.
     */
    public void finePartita() {
        numeroPartita++;

        boolean tutteManiVuote = giocatori.stream().allMatch(g -> g.getCarte().isEmpty());

        if (tutteManiVuote) {
            Optional<Giocatore> vincitore = giocatori.stream()
                    .max((g1, g2) -> Double.compare(g1.getPunteggio(), g2.getPunteggio()));

            vincitore.ifPresent(v -> {
                v.addPartiteVinte();
                System.out.println("Vincitore della partita: " + v.getNome());
                // Notifica la view passando il nome del vincitore
                notifyObservers(v.getNome());
            });
        }
    }

    /**
     * Controlla la presa corrente tramite lo stato della mano,
     * quindi notifica gli osservatori dell'aggiornamento.
     */
    public void controllaPresa() {
        statoMano.ControllaPresa(giocatori);
        notifyObservers();
    }
}
