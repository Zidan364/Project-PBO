import java.awt.*;

public class Player extends GameObject {

    private int velocity;
    private boolean isJumping;
    private int jumpHeight;

    public Player(int x, int y) {
        this.position = new Point(x, y);
        this.velocity = 0;    // Kecepatan lompatan diinisialisasi ke 0
        this.isJumping = false;
        this.jumpHeight = 15; // Tinggi lompatan
    }

    public void move() {
        if (isJumping) {
            position.y -= jumpHeight; // Gerakkan pemain ke atas saat melompat
            velocity--;
            if (velocity <= 0) {
                isJumping = false;
            }
        } else {
            if (position.y < 300) {
                position.y += 4; // Menurunkan pemain untuk kembali ke posisi dasar
            }
        }
    }

    public void jump() {
        if (!isJumping) {
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
        position = new Point(50, 300); // Reset posisi pemain
        velocity = 0;  // Reset kecepatan lompatan
        isJumping = false; // Reset status lompatan
    }
}
