import java.awt.*;

public class Player extends GameObject {

    private int velocity;
    private boolean isJumping;
    private int jumpHeight;
    private int groundLevel;

    public Player(int x, int y) {
        this.position = new Point(x, y);
        this.velocity = 0;    // Kecepatan lompatan diinisialisasi ke 0
        this.isJumping = false;
        this.jumpHeight = 20; // Tinggi lompatan
        this.groundLevel = y; // Posisi dasar (tanah)
    }

    public void move() {
        if (isJumping) {
            position.y -= velocity; // Gerakkan pemain ke atas
            velocity--;

            // Cek jika lompatan selesai (kecepatan negatif dan kembali ke tanah)
            if (position.y >= groundLevel) {
                position.y = groundLevel; // Pastikan posisi di tanah
                isJumping = false; // Set ulang status lompat
                velocity = 0; // Set ulang kecepatan
            }
        } else {
            // Pastikan pemain tetap di tanah
            if (position.y < groundLevel) {
                position.y += 4; // Efek gravitasi saat turun kembali
            }
        }
    }

    public void jump() {
        if (!isJumping && position.y == groundLevel) {
            isJumping = true;
            velocity = jumpHeight; // Set velocity untuk lompatan
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(position.x, position.y, 50, 50); // Gambar pemain
    }

    public boolean checkCollision(Obstacle obstacle) {
        Rectangle playerRect = new Rectangle(position.x, position.y, 50, 50);
        Rectangle obstacleRect = new Rectangle(obstacle.getPosition().x, obstacle.getPosition().y, obstacle.getWidth(), obstacle.getHeight());
        return playerRect.intersects(obstacleRect); // Periksa tabrakan
    }

    public void reset() {
        position = new Point(50, groundLevel); // Reset posisi pemain ke tanah
        velocity = 0;  // Reset kecepatan lompatan
        isJumping = false; // Reset status lompatan
    }
}