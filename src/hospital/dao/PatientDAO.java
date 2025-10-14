package hospital.dao;

import hospital.model.Patient;
import hospital.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public int getNextPatientId() throws SQLException {
        String sql = "SELECT MAX(P_ID) AS maxId FROM patient";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("maxId") + 1;
            } else {
                return 1; // first patient
            }
        }
    }



    public boolean addPatient(Patient p) throws SQLException {
        int newId = getNextPatientId();
        String sql = "INSERT INTO patient(P_ID, Name, Age, Gender, DOB, Mob_No) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newId);
            ps.setString(2, p.getName());
            ps.setInt(3, p.getAge());
            ps.setString(4, p.getGender());
            ps.setDate(5, Date.valueOf(p.getDob()));
            ps.setString(6, p.getMobNo());
            boolean success = ps.executeUpdate() > 0;
            if (success) p.setId(newId); // set generated ID in object
            return success;
        }
    }

    public boolean updatePatient(Patient p) throws SQLException {
        String sql = "UPDATE patient SET Name=?, Age=?, Gender=?, DOB=?, Mob_No=? WHERE P_ID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setInt(2, p.getAge());
            ps.setString(3, p.getGender());
            ps.setDate(4, Date.valueOf(p.getDob()));
            ps.setString(5, p.getMobNo());
            ps.setInt(6, p.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deletePatient(int id) throws SQLException {
        String sql = "DELETE FROM patient WHERE P_ID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patient";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Patient(
                        rs.getInt("P_ID"),
                        rs.getString("Name"),
                        rs.getInt("Age"),
                        rs.getString("Gender"),
                        rs.getDate("DOB").toLocalDate(),
                        rs.getString("Mob_No")
                ));
            }
        }
        return list;
    }
}
