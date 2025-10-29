package hospital.dao;

import hospital.model.Patient;
import hospital.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // ✅ Get next patient ID
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

    // ✅ Add new patient
    public boolean addPatient(Patient p) throws SQLException {
        int newId = getNextPatientId();
        String sql = "INSERT INTO patient(P_ID, Name, Age, Gender, DOB, Mob_No) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newId);
            ps.setString(2, p.getName());
            ps.setInt(3, p.getAge());
            ps.setString(4, p.getGender());
            ps.setDate(5, p.getDob() != null ? Date.valueOf(p.getDob()) : null);
            ps.setString(6, p.getMobNo());

            boolean success = ps.executeUpdate() > 0;
            if (success) p.setPId(newId); // ✅ corrected setter name
            return success;
        }
    }

    // ✅ Update patient details
    public boolean updatePatient(Patient p) throws SQLException {
        String sql = "UPDATE patient SET Name=?, Age=?, Gender=?, DOB=?, Mob_No=? WHERE P_ID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setInt(2, p.getAge());
            ps.setString(3, p.getGender());
            ps.setDate(4, p.getDob() != null ? Date.valueOf(p.getDob()) : null);
            ps.setString(5, p.getMobNo());
            ps.setInt(6, p.getPId()); // ✅ corrected getter
            return ps.executeUpdate() > 0;
        }
    }

    // ✅ Delete a patient by ID
    public boolean deletePatient(int id) throws SQLException {
        String sql = "DELETE FROM patient WHERE P_ID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ✅ Fetch patient by ID
    public Patient getPatientById(int id) throws SQLException {
        String sql = "SELECT * FROM Patient WHERE P_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Patient(
                        rs.getInt("P_ID"),
                        rs.getString("Name"),
                        rs.getInt("Age"),
                        rs.getString("Gender"),
                        rs.getDate("DOB") != null ? rs.getDate("DOB").toLocalDate() : null,
                        rs.getString("Mob_No")
                );
            }
        }
        return null;
    }

    // ✅ Search patients by name or mobile number
    public List<Patient> searchPatients(String keyword) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patient WHERE Name LIKE ? OR Mob_No LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                patients.add(new Patient(
                        rs.getInt("P_ID"),
                        rs.getString("Name"),
                        rs.getInt("Age"),
                        rs.getString("Gender"),
                        rs.getDate("DOB") != null ? rs.getDate("DOB").toLocalDate() : null,
                        rs.getString("Mob_No")
                ));
            }
        }
        return patients;
    }

    // ✅ Fetch all patients
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
                        rs.getDate("DOB") != null ? rs.getDate("DOB").toLocalDate() : null,
                        rs.getString("Mob_No")
                ));
            }
        }
        return list;
    }
}
