import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener {

    private Player player;
    private Obstacle obstacle;
    private Scoreboard scoreboard;
    private ThreadManager threadManager;
    private boolean isGameRunning;

    public Game() {
        this.player = new Player(50, 300);
        this.obstacle = new Obstacle(600, 300, 50, 50);
        this.scoreboard = new Scoreboard();
        this.threadManager = new ThreadManager(this);
        this.isGameRunning = false;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
    }

    public void startGame() {
        isGameRunning = true;
        threadManager.startGameLoop();
    }

    public void pauseGame() {
        isGameRunning = false;
        threadManager.stopGameLoop();
    }

    public void endGame() {
        isGameRunning = false;
        threadManager.stopGameLoop();
        JOptionPane.showMessageDialog(this, "Game Over\nScore: " + scoreboard.getCurrentScore());
    }

    public void resetGame() {
        player.reset();
        obstacle.resetPosition();
        scoreboard.reset();
        startGame();
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
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.jump();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER && !isGameRunning) {
            resetGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
