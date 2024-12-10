import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class Game extends JPanel implements KeyListener {

    private Player player;
    private Obstacle obstacle;
    private Scoreboard scoreboard;
    private ThreadManager threadManager;
    private boolean isGameRunning;
    private boolean isGameOverDialogShown;
    private final Runnable mainMenuHandler;

    public Game(Runnable mainMenuHandler) {
        this.mainMenuHandler = mainMenuHandler;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        initGameComponents();
    }

    private void initGameComponents() {
        this.player = new Player(50, 300);
        this.obstacle = new Obstacle(600, 300, 50, 50);
        this.scoreboard = new Scoreboard();
        this.threadManager = new ThreadManager(this); // Perbaikan: Kirim `this` ke konstruktor
        this.isGameRunning = false;
        this.isGameOverDialogShown = false;
    }

    public void startGame() {
        isGameRunning = true;
        isGameOverDialogShown = false;
        threadManager.startGameLoop(); // Perbaikan: Tidak ada argumen
    }

    public void pauseGame() {
        isGameRunning = false;
        threadManager.stopGameLoop();
    }

    public void endGame() {
        if (isGameOverDialogShown) return; // Prevent multiple dialogs
        isGameRunning = false;
        isGameOverDialogShown = true;
        threadManager.stopGameLoop();

        SwingUtilities.invokeLater(() -> {
            int option = JOptionPane.showOptionDialog(
                this,
                "Want to try again?\nScore: " + scoreboard.getCurrentScore(),
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Play Again", "No"},
                null
            );

            if (option == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                mainMenuHandler.run();
            }

            isGameOverDialogShown = false; // Reset after dialog is closed
        });
    }

    public void resetGame() {
        pauseGame(); // Stop any running game loop
        initGameComponents(); // Reinitialize game components
        startGame(); // Restart game loop
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isGameRunning) {
            player.draw(g);
            obstacle.draw(g);
            scoreboard.display(g);
        } else {
            g.setColor(Color.WHITE);
            g.drawString("Press Enter to Start", 350, 200);
        }
    }

    public void update() {
        if (!isGameRunning) return;

        player.move();
        obstacle.move();

        if (player.checkCollision(obstacle)) {
            endGame();
        }

        scoreboard.updateScore();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && isGameRunning) {
            player.jump();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER && !isGameRunning && !isGameOverDialogShown) {
            startGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
