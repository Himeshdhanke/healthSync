package hospital.dao;

import hospital.model.User;
import hospital.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Validate login
    public User validateLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE Username=? AND Password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, username);
            pst.setString(2, password);  // For simplicity. Hashing recommended in production

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("User_ID"));
                    user.setUsername(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setRole(rs.getString("Role"));
                    return user;
                }
            }
        }
        return null;
    }
}
