import java.awt.*;

public class Player extends GameObject {

    private int velocity;
    private boolean isJumping;
    private int jumpHeight;

    public Player(int x, int y) {
        this.position = new Point(x, y);
        this.velocity = 0;
        this.isJumping = false;
        this.jumpHeight = 15;
    }

    public void move() {
        if (isJumping) {
            position.y -= jumpHeight;
            velocity--;
            if (velocity <= 0) {
                isJumping = false;
            }
        } else {
            if (position.y < 300) {
                position.y += 5;
            }
        }
    }


    public void jump() {
        if (!isJumping) {
            isJumping = true;
            velocity = jumpHeight;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(position.x, position.y, 50, 50);
    }

    public boolean checkCollision(Obstacle obstacle) {
        Rectangle playerRect = new Rectangle(position.x, position.y, 50, 50);
        Rectangle obstacleRect = new Rectangle(obstacle.getPosition().x, obstacle.getPosition().y, obstacle.getWidth(), obstacle.getHeight());
        return playerRect.intersects(obstacleRect);
    }

    public void reset() {
        position = new Point(50, 300);
        velocity = 0;
        isJumping = false;
    }
}
