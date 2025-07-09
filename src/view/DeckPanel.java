package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * Pannello che rappresenta il mazzo di carte nel gioco.
 * Visualizza un'immagine o un'etichetta testuale come mazzo,
 * e gestisce l'interazione di click tramite un listener personalizzato.
 */
public class DeckPanel extends JPanel {
    private JLabel mazzoLabel;
    private DeckClickListener clickListener;

    /**
     * Interfaccia per ricevere eventi di click sul mazzo.
     */
    public interface DeckClickListener {
        /**
         * Metodo chiamato quando il mazzo viene cliccato.
         */
        void onDeckClicked();
    }

    /**
     * Costruisce il pannello del mazzo e inizializza i componenti grafici.
     */
    public DeckPanel() {
        initComponents();
    }

    /**
     * Imposta il listener per gli eventi di click sul mazzo.
     *
     * @param listener L'oggetto che riceverà le notifiche di click.
     */
    public void setClickListener(DeckClickListener listener) {
        this.clickListener = listener;
    }

    /**
     * Inizializza i componenti grafici del pannello,
     * caricando l'immagine del mazzo o mostrando un fallback testuale.
     */
    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setOpaque(false);

        createMazzoLabel();
        add(mazzoLabel);
    }

    /**
     * Crea l'etichetta grafica che rappresenta il mazzo,
     * con immagine o testo di fallback, e configura l'interazione mouse.
     */
    private void createMazzoLabel() {
        URL mazzoURL = getClass().getResource("/assets/mano.jpeg");
        if (mazzoURL != null) {
            ImageIcon icon = new ImageIcon(mazzoURL);
            Image scaledImage = icon.getImage().getScaledInstance(80, 110, Image.SCALE_SMOOTH);
            mazzoLabel = new JLabel(new ImageIcon(scaledImage));
        } else {
            mazzoLabel = new JLabel("MAZZO");
            mazzoLabel.setPreferredSize(new Dimension(80, 110));
            mazzoLabel.setForeground(Color.WHITE);
            mazzoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            mazzoLabel.setVerticalAlignment(SwingConstants.CENTER);
            mazzoLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            mazzoLabel.setFont(new Font("Serif", Font.BOLD, 12));
        }

        setupInteraction();
    }

    /**
     * Configura l'interazione mouse per il mazzo,
     * inclusi click, hover e effetti visivi.
     */
    private void setupInteraction() {
        mazzoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mazzoLabel.setToolTipText("Clicca per info giocatore e opzioni");

        mazzoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickListener != null) {
                    clickListener.onDeckClicked();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                mazzoLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if ("MAZZO".equals(mazzoLabel.getText())) {
                    mazzoLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                } else {
                    mazzoLabel.setBorder(null);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mazzoLabel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mazzoLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
            }
        });
    }

    /**
     * Imposta lo stato abilitato/disabilitato del pannello e aggiorna il cursore.
     *
     * @param enabled true per abilitare, false per disabilitare.
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mazzoLabel.setEnabled(enabled);

        if (enabled) {
            mazzoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            mazzoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
