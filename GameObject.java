import java.awt.*;

public abstract class GameObject {

    protected Point position;

    public abstract void move();
    public abstract void draw(Graphics g);
}
