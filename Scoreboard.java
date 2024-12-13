import java.awt.*;
import java.sql.*;

public class Scoreboard {

    private int currentScore;
    private int highScore;

    private Connection con;
    private Statement stm;

    public Scoreboard() {
        this.currentScore = 0;
        this.highScore = 0;

        // Database connection using DatabaseConnector
        try {
            con = DatabaseConnector.getConnection();
            stm = con.createStatement();

            // Retrieve high score from the database
            String query = "SELECT MAX(score) FROM nilai";
            ResultSet resultSet = stm.executeQuery(query);
            if (resultSet.next()) {
                highScore = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection errors
        }
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

    public void updateHighScore() {
        if (currentScore > highScore) {
            highScore = currentScore;

            // Update high score in the database
            try {
                String query = "UPDATE nilai SET score = ? WHERE player_name = ?";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, highScore); // Set the new high score
                preparedStatement.setString(2, "player1"); // Specify the player name
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Score updated successfully.");
                } else {
                    System.out.println("No record found for player1.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database update errors
            }
        }
    }

    public int getHighScore() {
        return highScore;
    }

    public void reset() {
        currentScore = 0;
    }

    
}