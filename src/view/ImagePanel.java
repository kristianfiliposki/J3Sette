package  view;
import javax.swing.*;
import java.awt.*;

class ImagePanel extends JPanel {
    private Image backgroundImage;

    public ImagePanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Disegna immagine scalata a tutto il pannello
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
