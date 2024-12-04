import java.awt.*;

public class Obstacle extends GameObject {

    private int width, height;
    private int speed; // Kecepatan pergerakan rintangan

    public Obstacle(int x, int y, int width, int height) {
        this.position = new Point(x, y);
        this.width = width;
        this.height = height;
        this.speed = 4; // Kecepatan awal rintangan
    }

    public void move() {
        position.x -= speed; // Menggerakkan rintangan ke kiri
        if (position.x < -width) {  // Jika rintangan keluar layar, reset posisi
            resetPosition();
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED); // Gambar rintangan dengan warna merah
        g.fillRect(position.x, position.y, width, height);
    }

    public void resetPosition() {
        position.x = 800; // Reset posisi rintangan ke sebelah kanan layar
        position.y = 300;
        speed = 4; // Reset kecepatan rintangan
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
