package view;

import model.Giocatore;
import controller.GameController;
import model.Tavolo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.BorderFactory.*;

/**
 * Pannello grafico che mostra le informazioni di un giocatore,
 * inclusi nome, punti, vittorie e numero di carte in mano.
 * Fornisce anche un pulsante per riavviare la partita.
 * <p>
 * Il pannello si integra con il {@link GameController} e il modello {@link Tavolo}
 * per aggiornare e gestire lo stato del gioco.
 */
public class PlayerInfoPanel extends JPanel {
    private JLabel nomeLabel;
    private JLabel puntiLabel;
    private JLabel vittorieLabel;
    private JLabel carteLabel;
    private JButton restartButton;
    private GameController controller;
    private Component parentComponent;
    private Tavolo tavolo;

    /**
     * Costruisce un pannello informazioni giocatore.
     *
     * @param parentComponent Il componente genitore usato come riferimento per i dialog.
     */
    public PlayerInfoPanel(Component parentComponent) {
        this.parentComponent = parentComponent;
        initComponents();
    }

    /**
     * Imposta il controller di gioco associato al pannello.
     *
     * @param controller Il {@link GameController} da associare.
     */
    public void setController(GameController controller) {
        this.controller = controller;
    }

    /**
     * Imposta il modello {@link Tavolo} associato al pannello.
     *
     * @param tavolo Il modello {@link Tavolo} da associare.
     */
    public void setTavolo(Tavolo tavolo) {
        this.tavolo = tavolo;
    }

    /**
     * Inizializza i componenti grafici del pannello.
     * Configura layout, etichette informative e il pulsante di riavvio partita.
     */
    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(createCompoundBorder(
                createLineBorder(Color.WHITE, 1),
                createEmptyBorder(10, 10, 10, 10)
        ));

        // Titolo
        JLabel titleLabel = new JLabel("INFO GIOCATORE");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(10));

        // Informazioni giocatore
        nomeLabel = createInfoLabel();
        add(nomeLabel);

        puntiLabel = createInfoLabel();
        add(puntiLabel);

        vittorieLabel = createInfoLabel();
        add(vittorieLabel);

        carteLabel = createInfoLabel();
        add(carteLabel);

        add(Box.createVerticalStrut(15));

        // Bottone restart
        createRestartButton();
        add(restartButton);
    }

    /**
     * Crea un'etichetta per le informazioni del giocatore con stile predefinito.
     *
     * @return La JLabel configurata.
     */
    private JLabel createInfoLabel() {
        JLabel label = new JLabel();
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Serif", Font.PLAIN, 12));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    /**
     * Crea e configura il pulsante "Riavvia Partita" con azione e effetti grafici.
     */
    private void createRestartButton() {
        restartButton = new JButton("Riavvia Partita");
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.setBackground(new Color(180, 50, 50));
        restartButton.setForeground(Color.WHITE);
        restartButton.setFont(new Font("Serif", Font.BOLD, 12));
        restartButton.setFocusPainted(false);
        restartButton.setBorder(createRaisedBevelBorder());
        restartButton.setMaximumSize(new Dimension(150, 30));

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRestartGame();
            }
        });

        // Effetto hover per il pulsante
        restartButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                restartButton.setBackground(new Color(200, 70, 70));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                restartButton.setBackground(new Color(180, 50, 50));
            }
        });
    }

    /**
     * Gestisce l'evento di riavvio partita.
     * Mostra una conferma all'utente e, se accettata,
     * resetta lo stato del tavolo e avvia una nuova partita.
     * Aggiorna inoltre le informazioni del giocatore nel pannello.
     */
    private void handleRestartGame() {
        int confirm = JOptionPane.showConfirmDialog(
                parentComponent,
                "Sei sicuro di voler riavviare la partita?\nTutti i progressi attuali verranno persi.",
                "Conferma Riavvio",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller != null && tavolo != null) {
                // Nascondi il pannello info
                setVisible(false);
                if (getParent() != null) {
                    getParent().revalidate();
                    getParent().repaint();
                }

                // Resetta lo stato del tavolo
                tavolo.reset();

                // Avvia una nuova partita (mescola e distribuisci carte)
                tavolo.iniziaPartita();

                // Reset display info giocatore
                resetInfo();

                System.out.println("Partita riavviata con successo!");
            } else {
                System.err.println("Errore: controller o tavolo non disponibili per il restart");
                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Errore durante il riavvio della partita",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    /**
     * Aggiorna le informazioni visualizzate del giocatore.
     *
     * @param giocatore Il giocatore di cui mostrare le informazioni. Se null, mostra valori vuoti.
     */
    public void updatePlayerInfo(Giocatore giocatore) {
        if (giocatore != null) {
            nomeLabel.setText("Nome: " + giocatore.getNome());
            puntiLabel.setText("Punti: " + String.format("%.1f", giocatore.getPunteggio()));
            vittorieLabel.setText("Vittorie: " + giocatore.getPartiteVinte());
            carteLabel.setText("Carte in mano: " + giocatore.getCarte().size());
        } else {
            nomeLabel.setText("Nome: -");
            puntiLabel.setText("Punti: -");
            vittorieLabel.setText("Vittorie: -");
            carteLabel.setText("Carte in mano: -");
        }
    }

    /**
     * Resetta le informazioni del giocatore visualizzate nel pannello.
     */
    public void resetInfo() {
        updatePlayerInfo(null);
    }
}
