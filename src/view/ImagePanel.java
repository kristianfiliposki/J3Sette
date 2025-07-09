package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Pannello personalizzato che disegna un'immagine di sfondo con angoli arrotondati.
 * Supporta l'anti-aliasing per bordi più morbidi e un bordo opzionale arrotondato.
 */
class ImagePanel extends JPanel {
    private Image backgroundImage;
    private int cornerRadius = 30; // Raggio degli angoli arrotondati

    /**
     * Costruisce un pannello con l'immagine di sfondo specificata.
     *
     * @param backgroundImage L'immagine da disegnare come sfondo.
     */
    public ImagePanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        setOpaque(false);
    }

    /**
     * Disegna il componente con l'immagine di sfondo arrotondata.
     *
     * @param g Il contesto grafico.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape clip = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.setClip(clip);

        g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        g2.dispose();

        // Disegna il bordo arrotondato (opzionale)
        Graphics2D gBorder = (Graphics2D) g.create();
        gBorder.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gBorder.setColor(getForeground() != null ? getForeground() : Color.WHITE);
        gBorder.setStroke(new BasicStroke(2));
        gBorder.draw(clip);
        gBorder.dispose();
    }
}
