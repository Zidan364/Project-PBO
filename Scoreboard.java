import java.awt.*;

public class Scoreboard {

    private int currentScore;

    public Scoreboard() {
        this.currentScore = 0;
    }

    public void updateScore() {
        currentScore++;
    }

    public void display(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("Score: " + currentScore, 10, 20);
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void reset() {
        currentScore = 0;
    }
}
