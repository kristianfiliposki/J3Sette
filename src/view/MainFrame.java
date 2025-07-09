package view;

import controller.GameController;
import model.Giocatore;
import model.Mazzo;
import model.Tavolo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Finestra principale dell'applicazione del gioco Tressette.
 * Mostra un menu iniziale con il pulsante per avviare la partita,
 * gestisce l'inserimento dei dati dei giocatori e crea il modello, la vista e il controller del gioco.
 */
public class MainFrame extends JFrame {
    private JButton startButton;
    private JPanel menuPanel;
    private CardTablePanel cardTablePanel;
    private GameController gameController;

    /**
     * Costruisce la finestra principale con il menu di avvio.
     * Configura il layout, dimensioni e il comportamento del pulsante di start.
     */
    public MainFrame() {
        super("Menu Tressette");

        menuPanel = new JPanel(new FlowLayout());
        menuPanel.setMinimumSize(new Dimension(1000,700));
        menuPanel.setBackground(new Color(0, 100, 0));

        startButton = new JButton("Inizia");
        startButton.setPreferredSize(new Dimension(100, 40));

        menuPanel.add(startButton);

        add(menuPanel);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenInputForm form = new OpenInputForm();
                List<String> dati = form.openInputForm();
                if (!dati.isEmpty()) {
                    try {
                        int numGiocatori = Integer.parseInt(dati.get(0));
                        String nomePrincipale = dati.get(1);

                        showCardTable(numGiocatori, nomePrincipale);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Numero giocatori non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    System.out.println("Operazione annullata.");
                }
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 650);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Crea e mostra il pannello di gioco con la tavola delle carte.
     * Inizializza il modello {@link Tavolo}, la vista {@link CardTablePanel} e il controller {@link GameController}.
     *
     * @param numGiocatori  Numero di giocatori totali (incluso l'utente).
     * @param nomePrincipale Nome del giocatore principale (utente).
     */
    private void showCardTable(int numGiocatori, String nomePrincipale) {
        // Creazione del modello
        List<Giocatore> giocatori = new ArrayList<>();
        giocatori.add(new Giocatore("user", nomePrincipale, 0, 0, null));
        for (int i = 2; i <= numGiocatori; i++) {
            giocatori.add(new Giocatore("bot", "Bot " + i, 0, 0, null));
        }

        Mazzo mazzo = new Mazzo();
        Tavolo tavolo = new Tavolo("assets/deck.jpeg", 1, giocatori, mazzo);

        // Creazione della vista
        cardTablePanel = new CardTablePanel(tavolo);

        // Creazione del controller
        gameController = new GameController(tavolo, cardTablePanel);

        // Associa il controller alla vista
        cardTablePanel.setController(gameController);

        // Aggiornamento dell'interfaccia
        remove(menuPanel);
        add(cardTablePanel);
        revalidate();
        repaint();
    }
}
