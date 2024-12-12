import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Obstacle extends GameObject {

    private int width, height;
    private int speed; 
    private Image image;
    
    public Obstacle(int x, int y, int width, int height) {
        this.position = new Point(x, y);
        this.width = width;
        this.height = height;
        this.speed = 5; // Kecepatan awal rintangan

        try {
            image = ImageIO.read(new File("Assets\\Menu\\obstacle.png")); 
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal memuat gambar obstacle.");
        }
    }

    @Override
    public void move() {
        position.x -= speed; 
        if (position.x < -width) {  
            resetPosition();
        }
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
        position.x = 800; 
        position.y = 415; 
        speed = 4;
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