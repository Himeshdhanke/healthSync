package hospital.dao;

import hospital.model.Prescription;
import hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {

    // Add a new prescription
    public boolean addPrescription(Prescription prescription) throws SQLException {
        String sql = "INSERT INTO prescription (patient_id, doctor_name, prescription) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, prescription.getPatientId());
            stmt.setString(2, prescription.getDoctorName());
            stmt.setString(3, prescription.getPrescription());
            return stmt.executeUpdate() > 0;
        }
    }

    // Get all prescriptions for a patient (for history)
    public List<Prescription> getPrescriptionsByPatient(int patientId) throws SQLException {
        String sql = "SELECT * FROM prescription WHERE patient_id = ? ORDER BY date DESC";
        List<Prescription> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Prescription(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getString("doctor_name"),
                        rs.getString("prescription"),
                        rs.getTimestamp("date")
                ));
            }
        }
        return list;
    }

    // ✅ Get only the latest prescription for a patient
    public Prescription getLatestPrescriptionByPatient(int patientId) throws SQLException {
        String sql = "SELECT * FROM prescription WHERE patient_id = ? ORDER BY date DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Prescription(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getString("doctor_name"),
                        rs.getString("prescription"),
                        rs.getTimestamp("date")
                );
            }
        }
        return null;
    }
}
