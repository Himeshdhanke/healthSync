package hospital.dao;

import hospital.model.Assigned;
import hospital.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignedDAO {

    // Get all assigned rooms
    public List<Assigned> getAllAssignedRooms() {
        List<Assigned> list = new ArrayList<>();
        String sql = """
            SELECT 
                a.P_ID, 
                r.R_ID, 
                p.Name AS PatientName, 
                r.Type AS RoomType, 
                r.Capacity, 
                r.Availability
            FROM Rooms r
            LEFT JOIN Assigned a ON r.R_ID = a.R_ID
            LEFT JOIN Patient p ON a.P_ID = p.P_ID
            ORDER BY r.R_ID;
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Assigned(
                        rs.getInt("P_ID"),
                        rs.getInt("R_ID"),
                        rs.getString("PatientName"),
                        rs.getString("RoomType"),
                        rs.getInt("Capacity"),
                        rs.getString("Availability")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Assign room to patient
    public boolean assignRoom(int patientId, int roomId) {
        String insertSQL = "INSERT INTO Assigned (P_ID, R_ID) VALUES (?, ?)";
        String updateSQL = "UPDATE Rooms SET Availability = 'Occupied' WHERE R_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement insert = conn.prepareStatement(insertSQL);
             PreparedStatement update = conn.prepareStatement(updateSQL)) {

            conn.setAutoCommit(false);
            insert.setInt(1, patientId);
            insert.setInt(2, roomId);
            insert.executeUpdate();

            update.setInt(1, roomId);
            update.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
