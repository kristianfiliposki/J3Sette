package model;

import java.util.List;
import java.util.Optional;

public class Tavolo extends Observable {
    private String immagine;
    private int numeroPartita;
    private List<Giocatore> giocatori;
    private Mazzo mazzo;
    private StatoMano statoMano;

    public Tavolo(String immagine, int numeroPartita, List<Giocatore> giocatori, Mazzo mazzo) {
        this.immagine = immagine;
        this.numeroPartita = numeroPartita;
        this.giocatori = giocatori;
        this.mazzo = mazzo;
        this.statoMano = new StatoMano();
    }

    public String getImmagine() {
        return immagine;
    }

    public List<Giocatore> getGiocatori() {
        return giocatori;
    }

    public StatoMano getStatoMano() {
        return statoMano;
    }

    public void reset() {
        for (Giocatore g : giocatori) {
            g.setPunteggio(0.0); // <-- CORRETTO!
            g.resetMano();
        }
        mazzo.reset();
        statoMano.reset();
        notifyObservers();
    }


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

    public void controllaPresa() {
        statoMano.ControllaPresa(giocatori);
        notifyObservers();
    }
}
