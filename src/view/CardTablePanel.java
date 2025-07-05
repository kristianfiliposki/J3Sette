package view;

import model.*;
import controller.GameController;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class CardTablePanel extends JPanel implements Observer {

    private Tavolo tavolo;
    private GameController controller;

    private JPanel campoGiocoPanel; // pannello centrale per carte scartate

    public CardTablePanel(Tavolo tavolo) {
        this.tavolo = tavolo;
        initUI();
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    private void initUI() {
        setBackground(new Color(0, 100, 0));
        setLayout(new BorderLayout(20, 20));

        JLabel titleLabel = new JLabel("Tavolo da carte", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        campoGiocoPanel = new JPanel();
        campoGiocoPanel.setBackground(new Color(0, 150, 0));
        campoGiocoPanel.setMinimumSize(new Dimension(300, 370));
        campoGiocoPanel.setBorder(new LineBorder(Color.WHITE, 3));
        campoGiocoPanel.setPreferredSize(new Dimension(400, 600));
        campoGiocoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(campoGiocoPanel);
        add(centerWrapper, BorderLayout.CENTER);

        aggiornaVista();
    }

    @Override
    public void update(Observable observable, Object arg) {
        aggiornaVista();
        // Se ricevi il nome del vincitore, mostra l’alert!
        if (arg instanceof String nomeVincitore && nomeVincitore != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Il vincitore è: " + nomeVincitore,
                    "Partita terminata",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }


    public void aggiornaVista() {
        removeAll();

        List<Giocatore> bot = tavolo.getGiocatori().stream()
                .filter(g -> "bot".equals(g.getRuolo()))
                .collect(Collectors.toList());

        List<Giocatore> umani = tavolo.getGiocatori().stream()
                .filter(g -> !"bot".equals(g.getRuolo()))
                .collect(Collectors.toList());

        JPanel leftPanel = creaPlayersPanel(bot);
        add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = creaPlayersPanel(umani);
        add(rightPanel, BorderLayout.EAST);

        // Riaggiungi il campo da gioco nel centro
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(campoGiocoPanel);
        add(centerWrapper, BorderLayout.CENTER);

        // Aggiorna il campo da gioco
        aggiornaCampoDaGioco(tavolo.getStatoMano());

        revalidate();
        repaint();
    }

    public void aggiornaCampoDaGioco(StatoMano statoMano) {
        campoGiocoPanel.removeAll();

        List<CartaBanco> carteScartate = statoMano != null ? statoMano.getCarteSulBanco() : null;

        if (carteScartate != null) {
            for (CartaBanco carta : carteScartate) {
                String path = carta.getImmagine();
                URL imgURL = getClass().getResource(path);
                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    Image scaledImage = icon.getImage().getScaledInstance(60, 90, Image.SCALE_SMOOTH);
                    JLabel cardLabel = new JLabel(new ImageIcon(scaledImage));
                    campoGiocoPanel.add(cardLabel);
                } else {
                    JLabel missingLabel = new JLabel("[img mancante]");
                    missingLabel.setForeground(Color.RED);
                    campoGiocoPanel.add(missingLabel);
                }
            }
        }

        campoGiocoPanel.revalidate();
        campoGiocoPanel.repaint();
    }

    private JPanel creaPlayersPanel(List<Giocatore> giocatori) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        for (Giocatore g : giocatori) {
            JPanel playerPanel = new JPanel();
            playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
            playerPanel.setOpaque(false);
            playerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JLabel nameLabel = new JLabel(STR."nome: \{g.getNome()} \n   punti: \{g.getPunteggio()} \n partite Vinte:  \{g.getPartiteVinte()}");
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Serif", Font.BOLD, 14));
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            playerPanel.add(nameLabel);
            playerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

            JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            cardsPanel.setOpaque(false);

            if ("bot".equals(g.getRuolo())) {
                String path = "../resources/assets/mano.jpeg";
                URL imgURL = getClass().getResource(path);
                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    Image scaledImage = icon.getImage().getScaledInstance(60, 90, Image.SCALE_SMOOTH);
                    cardsPanel.add(new JLabel(new ImageIcon(scaledImage)));
                } else {
                    JLabel missingLabel = new JLabel("[mano mancante]");
                    missingLabel.setForeground(Color.RED);
                    cardsPanel.add(missingLabel);
                }
            } else {
                List<Carta> carte = g.getCarte();
                int cardsPerRow = 5;
                JPanel currentRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                currentRow.setOpaque(false);
                playerPanel.add(currentRow);

                for (int i = 0; i < carte.size(); i++) {
                    if (i > 0 && i % cardsPerRow == 0) {
                        currentRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                        currentRow.setOpaque(false);
                        playerPanel.add(currentRow);
                    }
                    Carta carta = carte.get(i);
                    String path = carta.getImmagine();
                    URL imgURL = getClass().getResource(path);
                    if (imgURL != null) {
                        ImageIcon icon = new ImageIcon(imgURL);
                        Image scaledImage = icon.getImage().getScaledInstance(60, 90, Image.SCALE_SMOOTH);
                        JLabel cardLabel = new JLabel(new ImageIcon(scaledImage));

                        if (controller != null) {
                            cardLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            cardLabel.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    controller.giocaCarta(g, carta);
                                }
                            });
                        }

                        currentRow.add(cardLabel);
                    } else {
                        JLabel missingLabel = new JLabel("[img mancante]");
                        missingLabel.setForeground(Color.RED);
                        currentRow.add(missingLabel);
                    }
                }
            }

            playerPanel.add(cardsPanel);
            panel.add(playerPanel);
        }

        return panel;
    }
}
