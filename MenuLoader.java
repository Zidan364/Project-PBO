import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class MenuLoader {

    public static Map<String, String> loadMenuData(int idMenu) {
        Map<String, String> menuData = new HashMap<>();
        String query = "SELECT Judul, Background, Start FROM Menu WHERE id_menu = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idMenu);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                menuData.put("Judul", resultSet.getString("Judul"));
                menuData.put("Background", resultSet.getString("Background"));
                menuData.put("Start", resultSet.getString("Start"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menuData;
    }
}