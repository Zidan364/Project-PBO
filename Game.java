import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import javax.swing.*;

public class Game extends JPanel implements KeyListener {

    private Player player;
    private Obstacle obstacle;
    private Obstacle obstacle2;
    private Obstacle obstacle3;
    private Scoreboard scoreboard;
    private ThreadManager threadManager;
    private boolean isGameRunning;
    private boolean isGameOverDialogShown;
    private final Runnable mainMenuHandler;
    private Image backgroundImage; // Gambar latar belakang
    private Font customFont; // Font khusus untuk skor
    private Random random = new Random();
    private int banyakObstacle;
    public Game(Runnable mainMenuHandler) {
        this.mainMenuHandler = mainMenuHandler;
        
        banyakObstacle = random.nextInt(3)+1;
        if (banyakObstacle == 1){
            banyakObstacle++;
        }

        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        initGameComponents();
        loadBackgroundImage(); // Panggil metode untuk memuat gambar latar belakang
        loadCustomFont(); // Panggil metode untuk memuat font khusus
    }

    private void loadCustomFont() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            // Query untuk mendapatkan path font dari database
            String query = "SELECT font FROM menu WHERE id_menu = 1"; // Sesuaikan id_menu sesuai kebutuhan
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String fontPath = resultSet.getString("font");
                File fontFile = new File(fontPath);

                if (fontFile.exists()) {
                    customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(10f); // Ukuran font default
                    System.out.println("Font berhasil dimuat: " + fontPath);
                } else {
                    System.err.println("File font tidak ditemukan: " + fontPath);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat font dari database: " + e.getMessage());
        } catch (FontFormatException | IOException e) {
            System.err.println("Gagal memuat font: " + e.getMessage());
        }
    }

    private void loadBackgroundImage() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            // Query untuk mendapatkan path background dari database
            String query = "SELECT backgroundMain FROM menu WHERE id_menu = 1"; // Ganti kondisi WHERE sesuai kebutuhan
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String backgroundPath = resultSet.getString("backgroundMain");
                File backgroundFile = new File(backgroundPath);
                if (backgroundFile.exists()) {
                    backgroundImage = new ImageIcon(backgroundFile.getAbsolutePath()).getImage();
                } else {
                    System.err.println("File background tidak ditemukan: " + backgroundPath);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat gambar latar belakang dari database: " + e.getMessage());
        }
    }

    private void initGameComponents() {
        this.player = new Player(50, 415);
        this.obstacle = new Obstacle(600, 415, 65, 65);
        this.obstacle2 = new Obstacle(600 + 30, 415, 65, 65);
        this.obstacle3 = new Obstacle(600 + 30 + 30, 415, 65, 65);
        this.scoreboard = new Scoreboard();
        this.threadManager = new ThreadManager(this);
        this.isGameRunning = false;
        this.isGameOverDialogShown = false;
    }

    public void startGame() {
        isGameRunning = true;
        isGameOverDialogShown = false;
        threadManager.startGameLoop();
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
        saveScoreToDatabase(scoreboard.getCurrentScore());
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
        pauseGame();
        initGameComponents();
        startGame();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Gambar latar belakang jika tersedia
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        if (isGameRunning) {
            player.draw(g);
            
            if(obstacle.position.x <= -65 ){
                banyakObstacle = random.nextInt(3)+1;
                System.out.println(banyakObstacle);
            }

            switch (banyakObstacle) {
                case 1:
                    obstacle.draw(g);
                    break;
                case 2:
                    obstacle.draw(g);
                    obstacle2.draw(g);
                    break;
                case 3:
                    obstacle.draw(g);
                    obstacle2.draw(g);
                    obstacle3.draw(g);
                    break;
                default:
                    break;
            }

            // Gunakan font khusus jika tersedia untuk skor
            if (customFont != null) {
                g.setFont(customFont);
            } else {
                g.setFont(new Font("Arial", Font.BOLD, 24)); // Default font
            }

            g.setColor(Color.WHITE);
            g.drawString("Score: " + scoreboard.getCurrentScore(), 10, 30);
            g.drawString("Highscore: " + scoreboard.getHighScore(), 10, 60);
        } else {
            g.setColor(Color.WHITE);
            g.drawString("Press Enter to Start", 350, 200);
        }
    }

    public void update() {
        if (!isGameRunning) return;

        player.move();

        switch (banyakObstacle) {
            case 1:
                obstacle.move();
                break;
            case 2:
                obstacle.move();
                obstacle2.move();
                break;
            case 3:
                obstacle.move();
                obstacle2.move();
                obstacle3.move();
                break;
            default:
                break;
        }

        if (player.checkCollision(obstacle) || player.checkCollision(obstacle2) || player.checkCollision(obstacle3)) {
            endGame();
        }

        scoreboard.updateScore();
        repaint();
    }

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
            System.err.println("Gagal menyimpan skor ke database: " + e.getMessage());
        }
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