package hospital.dao;

import hospital.model.TestReport;
import hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestReportDAO {

    public List<TestReport> getReportsByPatient(int patientId) throws SQLException {
        List<TestReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM TestReport WHERE P_ID = ? ORDER BY Report_Date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TestReport r = new TestReport(
                        rs.getInt("R_ID"),
                        rs.getInt("P_ID"),
                        rs.getString("Patient_Name"),
                        rs.getString("Test_Type"),
                        rs.getString("Result"),
                        rs.getDate("Report_Date").toLocalDate(),
                        rs.getString("Status")
                );
                reports.add(r);
            }
        }

        return reports;
    }
}
