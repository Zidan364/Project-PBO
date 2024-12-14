import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class Obstacle extends GameObject {

    private int width, height;
    private int speed; 
    private Image image;
    // private Point position2;
    Random random = new Random();
    
    public Obstacle(int x, int y, int width, int height) {
        this.position = new Point(x, y);
        // this.position2 = new Point(x + width, y);
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
        // Pindahkan kedua objek ke kiri
        position.x -= speed;
        // position2.x -= speed;

        // Reset posisi objek pertama dan kedua jika mereka sudah keluar layar
        if (position.x < -width) {
            // Random random = new Random();
            // int randomY = random.nextInt(3);
            // System.out.println(randomY);
            position.x = 800;  // Posisi awal objek pertama
        }
        // if (position2.x < -width) {
        //     position2.x = 800;  // Posisi awal objek kedua
        // }
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            
            g.drawImage(image, position.x, position.y, width, height, null);
            // g.drawImage(image, position2.x, position2.y, width, height, null);
            // if (random.nextInt(2)+1 == 1){
            //     // g.drawImage(image, position.x, position.y, width, height, null);
                
            // }else if (random.nextInt(2)+1 == 2) {
                
            // }
            // // else if (random.nextInt(4)+1 == 3) {
                
            // // }else if (random.nextInt(4)+1 == 4) {
                
            // // }
        } else {
            g.setColor(Color.RED);
            g.fillRect(position.x, position.y, width, height);
            // g.fillRect(position2.x, position2.y, width, height);
        }
    }

    // public void resetPosition() {
    //     position.x = 800; 
    //     position.y = 415; 
    //     speed = 4;
    // }

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