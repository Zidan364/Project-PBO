import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Connection;

public class Game extends JPanel implements KeyListener {

    private Player player;
    private Obstacle obstacle;
    private Scoreboard scoreboard;
    private ThreadManager threadManager;
    private boolean isGameRunning;

    // private Connection DatabaseConnector; // Database connection

    public Game() {
        this.player = new Player(50, 300);
        this.scoreboard = new Scoreboard();
        this.obstacle = new Obstacle(600, 300, 50, 50, this.scoreboard);
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
    
        // new code to Save score to database upon game end
        saveScoreToDatabase(scoreboard.getCurrentScore());
    }

    //new code
    private void saveScoreToDatabase(int score) {
    try (Connection connection = DatabaseConnector.getConnection()) {
        // Cek jumlah data di tabel
        String countQuery = "SELECT COUNT(*) FROM nilai";
        PreparedStatement countStatement = connection.prepareStatement(countQuery);
        ResultSet countResult = countStatement.executeQuery();
        countResult.next();
        int rowCount = countResult.getInt(1);

        if (rowCount < 3) {
            // Jika kurang dari 3 data, langsung tambahkan data baru
            String insertQuery = "INSERT INTO nilai (name, score) VALUES (?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, "Player"); // Ganti dengan nama pemain dinamis, jika diperlukan
            insertStatement.setInt(2, score);
            insertStatement.executeUpdate();
            System.out.println("Skor baru ditambahkan ke database!");
        } else {
            // Jika sudah ada 3 data, periksa apakah skor baru lebih besar dari skor terkecil
            String minScoreQuery = "SELECT MIN(score) FROM nilai";
            PreparedStatement minScoreStatement = connection.prepareStatement(minScoreQuery);
            ResultSet minScoreResult = minScoreStatement.executeQuery();
            if (minScoreResult.next()) {
                int minScore = minScoreResult.getInt(1);
                if (score > minScore) {
                    // Hapus skor terkecil
                    String deleteQuery = "DELETE FROM nilai WHERE score = ? LIMIT 1";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setInt(1, minScore);
                    deleteStatement.executeUpdate();

                    // Tambahkan skor baru
                    String insertQuery = "INSERT INTO nilai (name, score) VALUES (?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setString(1, "Player");
                    insertStatement.setInt(2, score);
                    insertStatement.executeUpdate();
                    System.out.println("Skor lama diganti dengan skor baru di database!");
                } else {
                    System.out.println("Skor baru lebih kecil dari skor terkecil di database, tidak ditambahkan.");
                }
            }
        }
    } catch (SQLException e) {
        System.out.println("Gagal menyimpan skor ke database: " + e.getMessage());
    }
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