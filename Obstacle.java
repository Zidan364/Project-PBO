import java.awt.*;

public class Obstacle extends GameObject {

    private int width, height;

    public Obstacle(int x, int y, int width, int height) {
        this.position = new Point(x, y);
        this.width = width;
        this.height = height;
    }

    public void move() {
        position.x -= 5;
        if (position.x < -width) {
            resetPosition();
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(position.x, position.y, width, height);
    }

    public void resetPosition() {
        position.x = 800;
        position.y = 300;
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
