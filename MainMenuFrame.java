import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.*;

public class MainMenuFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Map<String, String> menuData = MenuLoader.loadMenuData(1);
            JFrame frame = new JFrame("Dino Rush - Main Menu");
            MainMenu mainMenu = new MainMenu(menuData);

            frame.add(mainMenu);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            // Add mouse listener for start button
            mainMenu.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (mainMenu.isStartButtonClicked(e.getPoint())) {
                        JOptionPane.showMessageDialog(frame, "Starting Game...");
                        // Add game starting logic here
                    }
                }
            });
        });
    }
}