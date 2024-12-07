import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Map<String, String> menuData = MenuLoader.loadMenuData(1);
            JFrame frame = new JFrame("Dino Dash");
            MainMenu mainMenu = new MainMenu(menuData);

            frame.add(mainMenu);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            // Declare the gamePanel as a holder for later initialization
            final Game[] gamePanel = new Game[1];

            mainMenu.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (mainMenu.isStartButtonClicked(e.getPoint())) {
                        // Remove main menu and initialize the game
                        frame.remove(mainMenu);
                        gamePanel[0] = new Game(() -> {
                            // Back to main menu after game over
                            frame.remove(gamePanel[0]);
                            frame.add(mainMenu);
                            frame.revalidate();
                            frame.repaint();
                        });

                        frame.add(gamePanel[0]);
                        frame.revalidate();
                        frame.repaint();

                        gamePanel[0].requestFocusInWindow();
                    }
                }
            });
        });
    }
}
