import java.awt.*;

public class Obstacle extends GameObject {

    private int width, height;
    public int speed; // Kecepatan pergerakan rintangan
    private Scoreboard scoreboard; // Referensi ke scoreboard untuk akses skor
    private int lastScoreCheckpoint; // Checkpoint terakhir untuk peningkatan kecepatan


    public Obstacle(int x, int y, int width, int height, Scoreboard scoreboard) {
        this.position = new Point(x, y);
        this.width = width;
        this.height = height;
        this.speed = 4; // Kecepatan awal rintangan
        this.scoreboard = scoreboard; // Referensi ke scoreboard
        this.lastScoreCheckpoint = 0; // Checkpoint awal

    }

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

    public void draw(Graphics g) {
        g.setColor(Color.RED); // Gambar rintangan dengan warna merah
        g.fillRect(position.x, position.y, width, height);
    }

    public void resetPosition() {
        position.x = 800; // Reset posisi rintangan ke sebelah kanan layar
        position.y = 300;
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
