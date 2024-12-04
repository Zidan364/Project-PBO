import java.awt.*;

public class Scoreboard {

    private int currentScore;
    private int highScore;

    public Scoreboard() {
        this.currentScore = 0;
        this.highScore = 0;
    }

    public void updateScore() {
        currentScore++;
    }

    public void display(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("Score: " + currentScore, 10, 20); 
        g.drawString("Highscore: " + highScore, 10, 40);
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void reset() {
        currentScore = 0;
    }

    public void updateHighScore() {
        if (currentScore > highScore) {
            highScore = currentScore;
        }
    }

    public int getHighScore() {
        return highScore;
    }
}
