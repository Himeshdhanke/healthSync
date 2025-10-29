package hospital.dao;

import hospital.model.Assigned;
import hospital.model.Room;
import hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    // ✅ Fetch all rooms with assignment info (who is in which room)
    public List<Assigned> getAllRoomsWithAssignments() {
        List<Assigned> assignedList = new ArrayList<>();

        String sql = """
            SELECT 
                r.R_ID,
                r.Type AS RoomType,
                r.Capacity,
                r.Availability,
                a.P_ID,
                p.Name AS PatientName
            FROM Rooms r
            LEFT JOIN Assigned a ON r.R_ID = a.R_ID
            LEFT JOIN Patient p ON a.P_ID = p.P_ID
            ORDER BY r.R_ID;
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Assigned assigned = new Assigned(
                        rs.getInt("P_ID"),
                        rs.getInt("R_ID"),
                        rs.getString("PatientName"),
                        rs.getString("RoomType"),
                        rs.getInt("Capacity"),
                        rs.getString("Availability")
                );
                assignedList.add(assigned);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assignedList;
    }

    // ✅ Fetch only available rooms
    public List<Room> getAvailableRooms() {
        List<Room> roomList = new ArrayList<>();

        String sql = "SELECT * FROM Rooms WHERE Availability = 'Available' ORDER BY R_ID";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("R_ID"),
                        rs.getString("Type"),
                        rs.getInt("Capacity"),
                        rs.getString("Availability")
                );
                roomList.add(room);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomList;
    }

    // ✅ Fetch all rooms (without patient info)
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();

        String sql = "SELECT * FROM Rooms ORDER BY R_ID";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("R_ID"),
                        rs.getString("Type"),
                        rs.getInt("Capacity"),
                        rs.getString("Availability")
                );
                rooms.add(room);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    // ✅ Add a new room
    public boolean addRoom(Room room) {
        String sql = "INSERT INTO Rooms (Type, Capacity, Availability) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, room.getType());
            stmt.setInt(2, room.getCapacity());
            stmt.setString(3, room.getAvailability());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Update existing room details
    public boolean updateRoom(Room room) {
        String sql = "UPDATE Rooms SET Type = ?, Capacity = ?, Availability = ? WHERE R_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, room.getType());
            stmt.setInt(2, room.getCapacity());
            stmt.setString(3, room.getAvailability());
            stmt.setInt(4, room.getRId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Delete a room
    public boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM Rooms WHERE R_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Update room availability (when assigned or released)
    public void updateRoomAvailability(int roomId, String availability) {
        String sql = "UPDATE Rooms SET Availability = ? WHERE R_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, availability);
            stmt.setInt(2, roomId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
