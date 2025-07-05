package controller;

import model.*;
import view.CardTablePanel;

import java.util.Observer;

public class GameController {
    private Tavolo tavolo;
    private CardTablePanel cardTablePanel;
    private int indiceCorrente = 0; // tiene traccia del turno

    public GameController(Tavolo tavolo, CardTablePanel cardTablePanel) {
        this.tavolo = tavolo;
        this.cardTablePanel = cardTablePanel;

        // Registra la view come observer del modello
        tavolo.addObserver( cardTablePanel);

        // Imposta il controller nella view
        this.cardTablePanel.setController(this);

        // Inizia la partita
        tavolo.iniziaPartita();

        indiceCorrente = 0; // inizia dal primo giocatore
    }

    public void giocaCarta(Giocatore giocatore, Carta carta) {
        // Delega al modello la logica di gioco
        tavolo.giocaCarta(giocatore, carta);

        // Passa al prossimo turno
        Giocatore prossimo = trovaProssimoGiocatore();

        if (prossimo != null) {
            if ("bot".equals(prossimo.getRuolo())) {
                // Se il bot ha carte, gioca la prima automaticamente
                if (!prossimo.getCarte().isEmpty()) {
                    Carta cartaBot = prossimo.getCarte().get(0);
                    giocaCarta(prossimo, cartaBot);
                }
            }

        }
        tavolo.getStatoMano().ControllaPresa(tavolo.getGiocatori());

        boolean tutteManiVuote = tavolo.getGiocatori().stream()
                .allMatch(g -> g.getCarte().isEmpty());
        if (tutteManiVuote) {
            tavolo.finePartita();
            tavolo.reset();
            tavolo.iniziaPartita();
        }
    }

    private Giocatore trovaProssimoGiocatore() {
        indiceCorrente = (indiceCorrente + 1) % tavolo.getGiocatori().size();
        return tavolo.getGiocatori().get(indiceCorrente);
    }

    public void controllaPresa() {
        tavolo.controllaPresa();
    }
}
