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

/**
 * Pannello principale che rappresenta la tavola da gioco nel gioco di carte.
 * Visualizza il tavolo, i giocatori, le carte sul banco e gestisce l'interazione utente.
 * Implementa l'interfaccia {@link Observer} per ricevere aggiornamenti dal modello {@link Tavolo}.
 */
public class CardTablePanel extends JPanel implements Observer {

    private Tavolo tavolo;
    private GameController controller;
    private JPanel campoGiocoPanel;
    private JLabel turnoLabel;
    private JPanel rightPanel;
    private DeckPanel deckPanel;
    private PlayerInfoPanel playerInfoPanel;

    /**
     * Costruisce il pannello della tavola da gioco associato al modello {@link Tavolo}.
     *
     * @param tavolo Il modello del tavolo di gioco da visualizzare.
     */
    public CardTablePanel(Tavolo tavolo) {
        this.tavolo = tavolo;
        initUI();
    }

    /**
     * Imposta il controller di gioco associato al pannello.
     * Passa inoltre il controller e il modello {@link Tavolo} al pannello informazioni giocatore.
     *
     * @param controller Il {@link GameController} da associare.
     */
    public void setController(GameController controller) {
        this.controller = controller;
        if (playerInfoPanel != null) {
            playerInfoPanel.setController(controller);
            playerInfoPanel.setTavolo(tavolo);
        }
    }

    /**
     * Inizializza l'interfaccia utente del pannello,
     * creando i vari componenti grafici e impostando il layout.
     */
    private void initUI() {
        setBackground(new Color(0, 100, 0));
        setLayout(new BorderLayout(10, 10));

        createTopPanel();
        createCampoGiocoPanel();
        createRightPanel();

        aggiornaVista();
    }

    /**
     * Crea il pannello superiore con titolo e indicazione del turno.
     */
    private void createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("TRESSETTE", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        turnoLabel = new JLabel("Turno: -", SwingConstants.CENTER);
        turnoLabel.setForeground(Color.YELLOW);
        turnoLabel.setFont(new Font("Serif", Font.BOLD, 16));
        topPanel.add(turnoLabel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Crea il pannello centrale che mostra il campo da gioco con le carte sul banco.
     * Se disponibile, usa un'immagine di sfondo.
     */
    private void createCampoGiocoPanel() {
        campoGiocoPanel = new JPanel();
        URL table = getClass().getResource("/assets/table.jpeg");
        if (table != null) {
            ImageIcon icon = new ImageIcon(table);
            Image image = icon.getImage();
            campoGiocoPanel = new ImagePanel(image);
            campoGiocoPanel.setMinimumSize(new Dimension(500, 300));
            campoGiocoPanel.setPreferredSize(new Dimension(500, 300));
        } else {
            System.err.println("Immagine table.jpeg non trovata");
            campoGiocoPanel = new JPanel();
        }
    }

    /**
     * Crea il pannello a destra contenente il mazzo e il pannello informazioni giocatore.
     * Il pannello info giocatore è inizialmente nascosto e può essere mostrato tramite interazione.
     */
    private void createRightPanel() {
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.setPreferredSize(new Dimension(200, 0));

        deckPanel = new DeckPanel();
        deckPanel.setClickListener(this::togglePlayerInfoPanel);
        rightPanel.add(deckPanel, BorderLayout.NORTH);

        playerInfoPanel = new PlayerInfoPanel(this);
        playerInfoPanel.setTavolo(tavolo);
        playerInfoPanel.setVisible(false);
        rightPanel.add(playerInfoPanel, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.EAST);
    }

    /**
     * Mostra o nasconde il pannello informazioni giocatore.
     * Aggiorna le informazioni del giocatore prima di mostrare.
     */
    private void togglePlayerInfoPanel() {
        if (playerInfoPanel.isVisible()) {
            playerInfoPanel.setVisible(false);
        } else {
            updatePlayerInfoPanel();
            playerInfoPanel.setVisible(true);
        }
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    /**
     * Aggiorna le informazioni visualizzate nel pannello informazioni giocatore,
     * mostrando i dati del giocatore con ruolo "user".
     */
    private void updatePlayerInfoPanel() {
        if (controller != null) {
            Giocatore user = tavolo.getGiocatori().stream()
                    .filter(g -> "user".equals(g.getRuolo()))
                    .findFirst()
                    .orElse(null);

            playerInfoPanel.updatePlayerInfo(user);
        }
    }

    /**
     * Metodo chiamato quando il modello osservato (tavolo) notifica un aggiornamento.
     * Aggiorna la vista e mostra un messaggio di fine partita se presente il nome del vincitore.
     *
     * @param observable L'oggetto osservato (tavolo).
     * @param arg        Argomento opzionale passato dalla notifica (es. nome vincitore).
     */
    @Override
    public void update(Observable observable, Object arg) {
        SwingUtilities.invokeLater(() -> {
            aggiornaVista();

            if (arg instanceof String nomeVincitore && nomeVincitore != null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Partita vinta da: " + nomeVincitore,
                        "Fine Partita",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
    }

    /**
     * Aggiorna l'intera vista del pannello, compresi i pannelli giocatori,
     * il campo da gioco e l'etichetta del turno.
     */
    public void aggiornaVista() {
        System.out.println("=== AGGIORNAMENTO VISTA ===");

        updateTurnoLabel();
        recreateLayout();
        aggiornaCampoDaGioco();

        revalidate();
        repaint();

        System.out.println("=== FINE AGGIORNAMENTO VISTA ===");
    }

    /**
     * Aggiorna l'etichetta che mostra il nome del giocatore di turno.
     */
    private void updateTurnoLabel() {
        if (controller != null) {
            Giocatore giocatoreAttuale = controller.getGiocatoreAttuale();
            if (giocatoreAttuale != null) {
                turnoLabel.setText("Turno di: " + giocatoreAttuale.getNome());
                System.out.println("Turno aggiornato: " + giocatoreAttuale.getNome());
            } else {
                turnoLabel.setText("Turno: -");
                System.out.println("Giocatore attuale null");
            }
        }
    }

    /**
     * Ricrea il layout rimuovendo e aggiungendo i componenti necessari,
     * compresi i pannelli dei giocatori e il campo da gioco.
     */
    private void recreateLayout() {
        Component topPanel = ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.NORTH);
        Component rightPanelComponent = ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.EAST);

        removeAll();
        add(topPanel, BorderLayout.NORTH);
        add(rightPanelComponent, BorderLayout.EAST);

        List<Giocatore> giocatori = tavolo.getGiocatori();

        Giocatore user = giocatori.stream()
                .filter(g -> "user".equals(g.getRuolo()))
                .findFirst()
                .orElse(null);

        List<Giocatore> altriGiocatori = giocatori.stream()
                .filter(g -> !"user".equals(g.getRuolo()))
                .collect(Collectors.toList());

        if (!altriGiocatori.isEmpty()) {
            JPanel otherPlayersPanel = creaAltriGiocatoriPanel(altriGiocatori);
            add(otherPlayersPanel, BorderLayout.WEST);
        }

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(campoGiocoPanel);
        add(centerWrapper, BorderLayout.CENTER);

        if (user != null) {
            JPanel userPanel = creaUserPanel(user);
            add(userPanel, BorderLayout.SOUTH);
        }
    }

    /**
     * Crea il pannello verticale contenente i pannelli degli altri giocatori.
     *
     * @param giocatori Lista dei giocatori da visualizzare.
     * @return Il pannello contenente i pannelli degli altri giocatori.
     */
    private JPanel creaAltriGiocatoriPanel(List<Giocatore> giocatori) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Giocatore giocatore : giocatori) {
            JPanel playerPanel = createOtherPlayerPanel(giocatore);
            panel.add(playerPanel);
        }

        return panel;
    }

    /**
     * Crea il pannello per un singolo giocatore (diverso dall'utente).
     * Evidenzia il pannello se è il suo turno.
     *
     * @param giocatore Il giocatore da rappresentare.
     * @return Il pannello del giocatore.
     */
    private JPanel createOtherPlayerPanel(Giocatore giocatore) {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        playerPanel.setOpaque(false);
        playerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 15, 5));

        boolean isTurnoCorrente = controller != null &&
                controller.getGiocatoreAttuale() != null &&
                controller.getGiocatoreAttuale().equals(giocatore);

        if (isTurnoCorrente) {
            playerPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        }

        JLabel infoLabel = new JLabel(
                "<html><center>" + giocatore.getNome() + "<br/>" +
                        "Punti: " + String.format("%.1f", giocatore.getPunteggio()) + "<br/>" +
                        "Vittorie: " + giocatore.getPartiteVinte() + "</center></html>"
        );
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Serif", Font.PLAIN, 13));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerPanel.add(infoLabel);

        JLabel carteLabel = new JLabel("Carte: " + giocatore.getCarte().size());
        carteLabel.setForeground(Color.LIGHT_GRAY);
        carteLabel.setFont(new Font("Serif", Font.PLAIN, 11));
        carteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerPanel.add(carteLabel);

        if (!giocatore.getCarte().isEmpty()) {
            JPanel dorsoPanels = createDorsoCartePanel(giocatore);
            playerPanel.add(dorsoPanels);
        }

        return playerPanel;
    }

    /**
     * Crea un pannello con i dorsi delle carte sovrapposti per un giocatore.
     *
     * @param giocatore Il giocatore di cui mostrare i dorsi delle carte.
     * @return Il pannello contenente le immagini dei dorsi.
     */
    private JPanel createDorsoCartePanel(Giocatore giocatore) {
        JPanel dorsoPanels = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        dorsoPanels.setOpaque(false);

        int numDorsi = Math.min(5, giocatore.getCarte().size());
        for (int i = 0; i < numDorsi; i++) {
            JLabel dorsoLabel = creaDorsoLabel();
            dorsoPanels.add(dorsoLabel);
        }

        return dorsoPanels;
    }

    /**
     * Crea un'etichetta con l'immagine del dorso di una carta.
     * Se l'immagine non è disponibile, mostra un'etichetta testuale.
     *
     * @return La JLabel con l'immagine del dorso o testo di fallback.
     */
    private JLabel creaDorsoLabel() {
        URL imgURL = getClass().getResource("/assets/mano.jpeg");
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image scaledImage = icon.getImage().getScaledInstance(30, 40, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(scaledImage));
        } else {
            JLabel label = new JLabel("[]");
            label.setForeground(Color.WHITE);
            label.setPreferredSize(new Dimension(30, 40));
            return label;
        }
    }

    /**
     * Crea il pannello dell'utente (giocatore con ruolo "user").
     * Evidenzia il pannello se è il suo turno attivo.
     *
     * @param user Il giocatore utente.
     * @return Il pannello contenente le informazioni e le carte dell'utente.
     */
    private JPanel creaUserPanel(Giocatore user) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        boolean isTurnoUser = controller != null &&
                controller.getGiocatoreAttuale() != null &&
                controller.getGiocatoreAttuale().equals(user) &&
                controller.isTurnoAttivo();

        if (isTurnoUser) {
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.YELLOW, 3),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
        }

        JLabel infoLabel = createUserInfoLabel(user, isTurnoUser);
        panel.add(infoLabel, BorderLayout.NORTH);

        JPanel cartePanel = creaCarteUserPanel(user);
        panel.add(cartePanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea l'etichetta con le informazioni dell'utente,
     * evidenziandola se è il suo turno.
     *
     * @param user       Il giocatore utente.
     * @param isTurnoUser True se è il turno dell'utente, false altrimenti.
     * @return L'etichetta con le informazioni.
     */
    private JLabel createUserInfoLabel(Giocatore user, boolean isTurnoUser) {
        JLabel infoLabel = new JLabel(
                user.getNome() + " - Punti: " + String.format("%.1f", user.getPunteggio()) +
                        " - Vittorie: " + user.getPartiteVinte() +
                        (isTurnoUser ? " (Il tuo turno!)" : "")
        );
        infoLabel.setForeground(isTurnoUser ? Color.YELLOW : Color.WHITE);
        infoLabel.setFont(new Font("Serif", Font.BOLD, 14));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return infoLabel;
    }

    /**
     * Crea il pannello contenente le carte dell'utente.
     *
     * @param user Il giocatore utente.
     * @return Il pannello con le carte visualizzate.
     */
    private JPanel creaCarteUserPanel(Giocatore user) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel.setOpaque(false);

        for (Carta carta : user.getCarte()) {
            JLabel cartaLabel = creaCartaLabel(carta, user);
            panel.add(cartaLabel);
        }

        return panel;
    }

    /**
     * Crea l'etichetta grafica per una singola carta dell'utente,
     * gestendo la possibilità di cliccarla se è giocabile.
     *
     * @param carta La carta da visualizzare.
     * @param user  Il giocatore utente.
     * @return L'etichetta grafica della carta.
     */
    private JLabel creaCartaLabel(Carta carta, Giocatore user) {
        URL imgURL = getClass().getResource(carta.getImmagine());

        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image scaledImage = icon.getImage().getScaledInstance(80, 110, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaledImage));

            boolean isGiocabile = isCartaGiocabile(user);

            System.out.println("Carta " + carta.getValore() + " di " + carta.getSeme() +
                    " - Giocabile: " + isGiocabile +
                    " - Turno attivo: " + (controller != null ? controller.isTurnoAttivo() : "null controller"));

            if (isGiocabile) {
                setupCartaInteraction(label, carta, user);
            } else {
                label.setEnabled(false);
                label.setIcon(new ImageIcon(createGrayImage(scaledImage)));
            }

            return label;
        } else {
            JLabel label = new JLabel("[IMG]");
            label.setPreferredSize(new Dimension(80, 110));
            label.setForeground(Color.RED);
            return label;
        }
    }

    /**
     * Verifica se la carta può essere giocata dall'utente in base al turno attivo.
     *
     * @param user Il giocatore utente.
     * @return true se la carta è giocabile, false altrimenti.
     */
    private boolean isCartaGiocabile(Giocatore user) {
        return controller != null &&
                controller.getGiocatoreAttuale() != null &&
                controller.getGiocatoreAttuale().equals(user) &&
                controller.isTurnoAttivo();
    }

    /**
     * Configura l'interazione mouse per una carta giocabile.
     * Gestisce click e hover con effetti grafici.
     *
     * @param label La JLabel della carta.
     * @param carta La carta associata.
     * @param user  Il giocatore utente.
     */
    private void setupCartaInteraction(JLabel label, Carta carta, Giocatore user) {
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Carta cliccata: " + carta.getValore() + " di " + carta.getSeme());
                controller.giocaCarta(user, carta);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                label.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setBorder(null);
            }
        });
    }

    /**
     * Crea una versione grigia e semitrasparente di un'immagine.
     *
     * @param original L'immagine originale.
     * @return L'immagine modificata in scala di grigi semitrasparente.
     */
    private Image createGrayImage(Image original) {
        int width = original.getWidth(null);
        int height = original.getHeight(null);

        java.awt.image.BufferedImage grayImage = new java.awt.image.BufferedImage(
                width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = grayImage.createGraphics();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.drawImage(original, 0, 0, null);
        g2d.dispose();

        return grayImage;
    }

    /**
     * Aggiorna il pannello del campo da gioco con le carte attualmente sul banco.
     * Se non ci sono carte, mostra un messaggio indicativo.
     */
    private void aggiornaCampoDaGioco() {
        campoGiocoPanel.removeAll();
        campoGiocoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        List<CartaBanco> carteSulBanco = tavolo.getStatoMano().getCarteSulBanco();

        if (!carteSulBanco.isEmpty()) {
            for (CartaBanco carta : carteSulBanco) {
                JPanel cartaPanel = createCartaBancoPanel(carta);
                if (cartaPanel != null) {
                    campoGiocoPanel.add(cartaPanel);
                }
            }
        } else {
            JLabel vuotoLabel = new JLabel("Campo da gioco", SwingConstants.CENTER);
            vuotoLabel.setForeground(Color.WHITE);
            vuotoLabel.setFont(new Font("Serif", Font.ITALIC, 16));
            campoGiocoPanel.add(vuotoLabel);
        }

        campoGiocoPanel.revalidate();
        campoGiocoPanel.repaint();
    }

    /**
     * Crea un pannello per visualizzare una singola carta sul banco,
     * con l'immagine e il nome del giocatore che l'ha giocata.
     *
     * @param carta La carta da visualizzare.
     * @return Il pannello contenente la carta e il nome del tiratore, oppure null se immagine non trovata.
     */
    private JPanel createCartaBancoPanel(CartaBanco carta) {
        URL imgURL = getClass().getResource(carta.getImmagine());
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image scaledImage = icon.getImage().getScaledInstance(90, 125, Image.SCALE_SMOOTH);
            JLabel cartaLabel = new JLabel(new ImageIcon(scaledImage));

            JPanel cartaPanel = new JPanel(new BorderLayout());
            cartaPanel.setOpaque(false);
            cartaPanel.add(cartaLabel, BorderLayout.CENTER);

            JLabel nomeLabel = new JLabel(carta.getNomeTiratore(), SwingConstants.CENTER);
            nomeLabel.setForeground(Color.WHITE);
            nomeLabel.setFont(new Font("Serif", Font.PLAIN, 10));
            cartaPanel.add(nomeLabel, BorderLayout.SOUTH);

            return cartaPanel;
        }
        return null;
    }
}
