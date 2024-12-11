import java.awt.*;
import java.util.Map;
import javax.swing.*;

public class MainMenu extends JPanel {

    private Image backgroundImage;
    private Image startButtonImage;

    private int startButtonX, startButtonY, startButtonWidth, startButtonHeight;

    public MainMenu(Map<String, String> menuData) {
        try {
            // Load images
            backgroundImage = new ImageIcon(menuData.get("Background")).getImage();
            startButtonImage = new ImageIcon(menuData.get("Start")).getImage();

            // Precalculate start button dimensions
            if (startButtonImage != null) {
                startButtonWidth = startButtonImage.getWidth(null);
                startButtonHeight = startButtonImage.getHeight(null);
                startButtonX = (800 - startButtonWidth) / 2; // Center horizontally
                startButtonY = 300; // Fixed position
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(800, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        // Draw start button
        if (startButtonImage != null) {
            g.drawImage(startButtonImage, startButtonX, startButtonY, null);
        }
    }

    public boolean isStartButtonClicked(Point clickPoint) {
        // Check if the click falls within the start button bounds
        Rectangle buttonBounds = new Rectangle(startButtonX, startButtonY, startButtonWidth, startButtonHeight);
        return buttonBounds.contains(clickPoint);
    }
}