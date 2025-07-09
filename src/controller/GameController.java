package controller;

import model.*;
import view.CardTablePanel;

import javax.swing.*;

/**
 * Controller principale per la gestione della logica di gioco.
 * Coordina le interazioni tra il modello {@link Tavolo} e la vista {@link CardTablePanel}.
 * Gestisce il turno dei giocatori, la giocata delle carte, il controllo delle prese e la fine partita.
 */
public class GameController {
    private Tavolo tavolo;
    private CardTablePanel cardTablePanel;
    private int indiceGiocatoreAttuale = 0;
    private boolean turnoAttivo = true;

    /**
     * Costruttore del controller di gioco.
     * Inizializza il modello e la vista, registra gli observer, e avvia la partita.
     *
     * @param tavolo          Il modello che rappresenta il tavolo di gioco.
     * @param cardTablePanel  La vista che mostra le carte e lo stato del gioco.
     */
    public GameController(Tavolo tavolo, CardTablePanel cardTablePanel) {
        this.tavolo = tavolo;
        this.cardTablePanel = cardTablePanel;

        // Registra la view come observer del modello
        tavolo.addObserver(cardTablePanel);

        // Imposta il controller nella view
        this.cardTablePanel.setController(this);

        // Inizia la partita
        tavolo.iniziaPartita();
        indiceGiocatoreAttuale = 0;

        System.out.println("=== PARTITA INIZIATA ===");
        stampaStatoGioco();

        // Avvia il primo turno se necessario
        processaTurnoBot();
    }

    /**
     * Metodo chiamato per far giocare una carta da un giocatore.
     * Controlla se il turno è attivo e se è il turno corretto del giocatore,
     * quindi aggiorna lo stato di gioco.
     *
     * @param giocatore Il giocatore che tenta di giocare la carta.
     * @param carta     La carta che il giocatore vuole giocare.
     */
    public void giocaCarta(Giocatore giocatore, Carta carta) {
        System.out.println("\n=== TENTATIVO GIOCATA ===");
        System.out.println("Giocatore: " + giocatore.getNome());
        System.out.println("Carta: " + carta.getValore() + " di " + carta.getSeme());
        System.out.println("Turno attivo: " + turnoAttivo);

        if (!turnoAttivo) {
            System.out.println("RESPINTA: Turno non attivo");
            return;
        }

        // Verifica che sia il turno del giocatore
        Giocatore giocatoreAttuale = tavolo.getGiocatori().get(indiceGiocatoreAttuale);
        System.out.println("Giocatore attuale: " + giocatoreAttuale.getNome());

        if (!giocatore.equals(giocatoreAttuale)) {
            System.out.println("RESPINTA: Non è il turno di " + giocatore.getNome());
            if ("user".equals(giocatore.getRuolo())) {
                JOptionPane.showMessageDialog(cardTablePanel, "Non è il tuo turno!", "Attenzione", JOptionPane.WARNING_MESSAGE);
            }
            return;
        }

        System.out.println("GIOCATA ACCETTATA");

        try {
            // Gioca la carta
            tavolo.giocaCarta(giocatore, carta);
            System.out.println("Carta giocata con successo");

            // Stampa stato dopo la giocata
            stampaStatoGioco();

            // Passa al prossimo turno
            passaAlProssimoTurno();

        } catch (Exception e) {
            System.out.println("ERRORE nella giocata: " + e.getMessage());
            e.printStackTrace();
            // Riattiva il turno in caso di errore
            turnoAttivo = true;
        }
    }

    /**
     * Passa il turno al prossimo giocatore in lista.
     * Se tutti i giocatori hanno giocato, avvia il controllo della presa.
     * Aggiorna la vista e gestisce il turno dei bot se necessario.
     */
    private void passaAlProssimoTurno() {
        System.out.println("\n=== PASSAGGIO TURNO ===");

        int vecchioIndice = indiceGiocatoreAttuale;
        indiceGiocatoreAttuale = (indiceGiocatoreAttuale + 1) % tavolo.getGiocatori().size();

        System.out.println("Indice giocatore: " + vecchioIndice + " -> " + indiceGiocatoreAttuale);
        System.out.println("Carte sul banco: " + tavolo.getStatoMano().getCarteSulBanco().size());
        System.out.println("Numero giocatori: " + tavolo.getGiocatori().size());

        // Se tutti hanno giocato una carta, controlla la presa
        if (tavolo.getStatoMano().getCarteSulBanco().size() == tavolo.getGiocatori().size()) {
            System.out.println("TUTTI HANNO GIOCATO - Controllo presa");
            turnoAttivo = false;

            // Aspetta un po' prima di controllare la presa per far vedere le carte
            Timer timer = new Timer(2000, e -> {
                System.out.println("Timer scaduto - Controllo presa");
                SwingUtilities.invokeLater(() -> {
                    try {
                        controllaPresa();
                        verificaFinePartita();
                    } catch (Exception ex) {
                        System.out.println("ERRORE nel controllo presa: " + ex.getMessage());
                        ex.printStackTrace();
                        // Riattiva il turno in caso di errore
                        turnoAttivo = true;
                    }
                });
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            System.out.println("TURNO CONTINUA");
            // Aggiorna la vista immediatamente
            SwingUtilities.invokeLater(() -> {
                cardTablePanel.aggiornaVista();
                // Fa giocare il bot se è il suo turno
                processaTurnoBot();
            });
        }
    }

    /**
     * Gestisce il turno automatico del bot.
     * Se è il turno di un bot e ha carte, gioca una carta dopo un breve ritardo.
     */
    private void processaTurnoBot() {
        if (!turnoAttivo) {
            System.out.println("ProcessaTurnoBot: Turno non attivo");
            return;
        }

        if (indiceGiocatoreAttuale >= tavolo.getGiocatori().size()) {
            System.out.println("ERRORE: Indice giocatore fuori range: " + indiceGiocatoreAttuale);
            return;
        }

        Giocatore giocatoreAttuale = tavolo.getGiocatori().get(indiceGiocatoreAttuale);
        System.out.println("ProcessaTurnoBot: Giocatore attuale = " + giocatoreAttuale.getNome() + " (ruolo: " + giocatoreAttuale.getRuolo() + ")");

        if ("bot".equals(giocatoreAttuale.getRuolo()) && !giocatoreAttuale.getCarte().isEmpty()) {
            System.out.println("Bot deve giocare - Avvio timer");
            // Ritarda leggermente l'azione del bot per rendere più realistico
            Timer timer = new Timer(1000, e -> {
                System.out.println("Timer bot scaduto");
                // Verifica ancora una volta che il turno sia attivo e che il bot abbia carte
                if (turnoAttivo && !giocatoreAttuale.getCarte().isEmpty()) {
                    System.out.println("Bot gioca carta");
                    Carta cartaBot = scegliCartaBot(giocatoreAttuale);
                    giocaCarta(giocatoreAttuale, cartaBot);
                } else {
                    System.out.println("Bot non può giocare - Turno attivo: " + turnoAttivo + ", Carte: " + giocatoreAttuale.getCarte().size());
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            System.out.println("Non è turno del bot o bot senza carte");
        }
    }

    /**
     * Strategia semplice per il bot.
     * Sceglie la prima carta disponibile nella mano del bot.
     *
     * @param bot Il giocatore bot.
     * @return La carta scelta dal bot da giocare.
     */
    private Carta scegliCartaBot(Giocatore bot) {
        return bot.getCarte().get(0);
    }

    /**
     * Controlla quale giocatore ha vinto la presa corrente.
     * Aggiorna l'indice del giocatore attuale per la prossima mano.
     * Aggiorna la vista e gestisce il turno successivo.
     */
    private void controllaPresa() {
        System.out.println("\n=== CONTROLLO PRESA ===");

        try {
            // Trova il vincitore della presa
            int vincitoreIndex = tavolo.getStatoMano().ControllaPresa(tavolo.getGiocatori());

            System.out.println("Vincitore index: " + vincitoreIndex);

            if (vincitoreIndex >= 0 && vincitoreIndex < tavolo.getGiocatori().size()) {
                // Il vincitore inizia la prossima mano
                indiceGiocatoreAttuale = vincitoreIndex;
                turnoAttivo = true;

                System.out.println("Prossimo turno inizia da: " + tavolo.getGiocatori().get(vincitoreIndex).getNome());

                // IMPORTANTE: Aggiorna la vista e poi processa il turno
                cardTablePanel.aggiornaVista();
                System.out.println("Vista aggiornata - Processando turno");

                // Processa il turno del bot se necessario
                processaTurnoBot();
            } else {
                System.out.println("ERRORE: Indice vincitore non valido: " + vincitoreIndex);
                turnoAttivo = true; // Riattiva il turno
            }

        } catch (Exception e) {
            System.out.println("ERRORE nel controllo presa: " + e.getMessage());
            e.printStackTrace();
            turnoAttivo = true; // Riattiva il turno in caso di errore
        }
    }

    /**
     * Verifica se la partita è terminata (tutte le mani dei giocatori sono vuote).
     * Se la partita è finita, avvia la fine partita e il reset per una nuova partita.
     */
    private void verificaFinePartita() {
        boolean tutteManiVuote = tavolo.getGiocatori().stream()
                .allMatch(g -> g.getCarte().isEmpty());

        System.out.println("Verifica fine partita - Tutte mani vuote: " + tutteManiVuote);

        if (tutteManiVuote) {
            System.out.println("FINE PARTITA");
            tavolo.finePartita();
            // Ricomincia una nuova partita dopo una pausa
            Timer timer = new Timer(3000, e -> {
                System.out.println("Riavvio nuova partita");
                try {
                    tavolo.reset();
                    tavolo.iniziaPartita();
                    indiceGiocatoreAttuale = 0;
                    turnoAttivo = true;

                    // Aggiorna la vista e poi processa il turno del bot
                    SwingUtilities.invokeLater(() -> {
                        cardTablePanel.aggiornaVista();
                        processaTurnoBot();
                    });
                } catch (Exception ex) {
                    System.out.println("ERRORE nel riavvio: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    /**
     * Stampa sul console lo stato attuale del gioco,
     * inclusi giocatore attuale, turno attivo, carte sul banco e punteggi dei giocatori.
     */
    private void stampaStatoGioco() {
        System.out.println("\n--- STATO GIOCO ---");
        System.out.println("Giocatore attuale: " + indiceGiocatoreAttuale + " (" +
                (indiceGiocatoreAttuale < tavolo.getGiocatori().size() ?
                        tavolo.getGiocatori().get(indiceGiocatoreAttuale).getNome() : "INVALID") + ")");
        System.out.println("Turno attivo: " + turnoAttivo);
        System.out.println("Carte sul banco: " + tavolo.getStatoMano().getCarteSulBanco().size());

        for (int i = 0; i < tavolo.getGiocatori().size(); i++) {
            Giocatore g = tavolo.getGiocatori().get(i);
            System.out.println("Giocatore " + i + ": " + g.getNome() + " - Carte: " + g.getCarte().size() +
                    " - Punti: " + String.format("%.2f", g.getPunteggio()));
        }
        System.out.println("-------------------");
    }

    /**
     * Restituisce il giocatore attuale il cui turno è in corso.
     *
     * @return Il giocatore attuale, oppure null se l'indice è fuori range.
     */
    public Giocatore getGiocatoreAttuale() {
        if (indiceGiocatoreAttuale >= 0 && indiceGiocatoreAttuale < tavolo.getGiocatori().size()) {
            return tavolo.getGiocatori().get(indiceGiocatoreAttuale);
        }
        System.out.println("ERRORE: getGiocatoreAttuale() - indice non valido: " + indiceGiocatoreAttuale);
        return null;
    }

    /**
     * Indica se il turno corrente è attivo e quindi se è possibile giocare.
     *
     * @return true se il turno è attivo, false altrimenti.
     */
    public boolean isTurnoAttivo() {
        return turnoAttivo;
    }
}
