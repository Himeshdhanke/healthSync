package hospital.dao;

import hospital.model.Bill;
import hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {

    // ✅ Add a new bill
    public boolean addBill(Bill bill) throws SQLException {
        String sql = "INSERT INTO Bills (P_ID, Patient_Name, Amount, Bill_Date, Status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bill.getPatientId());
            stmt.setString(2, bill.getPatientName());
            stmt.setDouble(3, bill.getAmount());
            stmt.setString(4, bill.getBillDate()); // stored as string (YYYY-MM-DD)
            stmt.setString(5, bill.getStatus());

            return stmt.executeUpdate() > 0;
        }
    }

    // ✅ Update existing bill
    public boolean updateBill(Bill bill) throws SQLException {
        String sql = "UPDATE Bills SET P_ID=?, Patient_Name=?, Amount=?, Bill_Date=?, Status=? WHERE B_ID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bill.getPatientId());
            stmt.setString(2, bill.getPatientName());
            stmt.setDouble(3, bill.getAmount());
            stmt.setString(4, bill.getBillDate());
            stmt.setString(5, bill.getStatus());
            stmt.setInt(6, bill.getBillId());

            return stmt.executeUpdate() > 0;
        }
    }

    // ✅ Delete a bill
    public boolean deleteBill(int billId) throws SQLException {
        String sql = "DELETE FROM Bills WHERE B_ID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, billId);
            return stmt.executeUpdate() > 0;
        }
    }

    // ✅ Get all bills
    public List<Bill> getAllBills() throws SQLException {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT * FROM Bills ORDER BY B_ID DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Bill bill = new Bill(
                        rs.getInt("B_ID"),
                        rs.getInt("P_ID"),
                        rs.getString("Patient_Name"),
                        rs.getDouble("Amount"),
                        rs.getString("Bill_Date"),
                        rs.getString("Status")
                );
                list.add(bill);
            }
        }
        return list;
    }

    // ✅ Get bill by ID
    public Bill getBillById(int billId) throws SQLException {
        String sql = "SELECT * FROM Bills WHERE B_ID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, billId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Bill(
                        rs.getInt("B_ID"),
                        rs.getInt("P_ID"),
                        rs.getString("Patient_Name"),
                        rs.getDouble("Amount"),
                        rs.getString("Bill_Date"),
                        rs.getString("Status")
                );
            }
        }
        return null;
    }
}
