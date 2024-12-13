import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Obstacle extends GameObject {

    private int width, height;
    private int speed; 
    private Image image;
    private Scoreboard scoreboard; // Referensi ke scoreboard untuk akses skor
    private int lastScoreCheckpoint; // Checkpoint terakhir untuk peningkatan kecepatan
    
    public Obstacle(int x, int y, int width, int height, Scoreboard scoreboard) {
        this.position = new Point(x, y);
        this.width = width;
        this.height = height;
        this.speed = 4; // Kecepatan awal rintangan
        this.scoreboard = scoreboard; // Referensi ke scoreboard
        this.lastScoreCheckpoint = 0; // Checkpoint awal

        try {
            image = ImageIO.read(new File("Assets\\Menu\\obstacle.png")); 
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal memuat gambar obstacle.");
        }
    }

    @Override
    public void move() {
        if (scoreboard.getCurrentScore() >= lastScoreCheckpoint + 1000) {
            lastScoreCheckpoint += 200;
            increaseSpeed();
        }

        position.x -= speed; // Menggerakkan rintangan ke kiri

        if (position.x < -width) { // Jika rintangan keluar layar, reset posisi
            resetPosition();
        }
    }

    private void increaseSpeed() {
        speed += 2; // Tingkatkan kecepatan
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, position.x, position.y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(position.x, position.y, width, height);
        }
    }

    public void resetPosition() {
        position.x = 800; // Reset posisi rintangan ke sebelah kanan layar
        position.y = 410;
        speed = 4; // Reset kecepatan rintangan
        lastScoreCheckpoint = 0; // Reset checkpoint saat game di-reset
    }

    public Point getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}