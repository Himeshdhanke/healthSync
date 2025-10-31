package hospital.dao;

import hospital.model.TestReport;
import hospital.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestReportDAO {

    // ✅ Fetch all reports
    public List<TestReport> getAllReports() throws SQLException {
        List<TestReport> reports = new ArrayList<>();
        String sql = "SELECT R_ID, P_ID, Patient_Name, Test_Type, Result, Report_Date, Status FROM TestReport ORDER BY Report_Date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reports.add(new TestReport(
                        rs.getInt("R_ID"),
                        rs.getInt("P_ID"),
                        rs.getString("Patient_Name"),
                        rs.getString("Test_Type"),
                        rs.getString("Result"),
                        rs.getDate("Report_Date").toLocalDate(),
                        rs.getString("Status")
                ));
            }
        }
        return reports;
    }

    // ✅ Fetch reports by patient ID
    public List<TestReport> getReportsByPatient(int patientId) throws SQLException {
        List<TestReport> reports = new ArrayList<>();
        String sql = "SELECT R_ID, P_ID, Patient_Name, Test_Type, Result, Report_Date, Status FROM TestReport WHERE P_ID = ? ORDER BY Report_Date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                reports.add(new TestReport(
                        rs.getInt("R_ID"),
                        rs.getInt("P_ID"),
                        rs.getString("Patient_Name"),
                        rs.getString("Test_Type"),
                        rs.getString("Result"),
                        rs.getDate("Report_Date").toLocalDate(),
                        rs.getString("Status")
                ));
            }
        }
        return reports;
    }

    // ✅ Add a new report
    public boolean addReport(TestReport report) throws SQLException {
        String sql = "INSERT INTO TestReport (P_ID, Patient_Name, Test_Type, Result, Report_Date, Status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, report.getPatientId());
            ps.setString(2, report.getPatientName());
            ps.setString(3, report.getTestType());
            ps.setString(4, report.getResult());
            ps.setDate(5, Date.valueOf(report.getReportDate()));
            ps.setString(6, report.getStatus());

            return ps.executeUpdate() > 0;
        }
    }

    // ✅ Update report
    public boolean updateReport(TestReport report) throws SQLException {
        String sql = "UPDATE TestReport SET Patient_Name=?, Test_Type=?, Result=?, Report_Date=?, Status=? WHERE R_ID=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, report.getPatientName());
            ps.setString(2, report.getTestType());
            ps.setString(3, report.getResult());
            ps.setDate(4, Date.valueOf(report.getReportDate()));
            ps.setString(5, report.getStatus());
            ps.setInt(6, report.getReportId());

            return ps.executeUpdate() > 0;
        }
    }

    // ✅ Delete report
    public boolean deleteReport(int reportId) throws SQLException {
        String sql = "DELETE FROM TestReport WHERE R_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reportId);
            return ps.executeUpdate() > 0;
        }
    }
}
