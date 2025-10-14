package hospital.dao;

import hospital.model.Employee;
import hospital.util.DBConnection;
import java.sql.*;

public class EmployeeDAO {

    public static Employee validateLogin(String username, String password) {
        Employee emp = null;
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                emp = new Employee(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emp;
    }
}
