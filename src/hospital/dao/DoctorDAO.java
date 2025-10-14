package hospital.dao;

import hospital.model.Doctor;
import hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    public boolean addDoctor(Doctor d) throws SQLException {
        String empSql = "INSERT INTO Employee(Name, Salary, Sex, Mob_No, Address, State, City, Pin_no) VALUES(?,?,?,?,?,?,?,?)";
        String docSql = "INSERT INTO Doctor(E_ID, Dept, Qualification) VALUES(?,?,?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psEmp = conn.prepareStatement(empSql, Statement.RETURN_GENERATED_KEYS)) {
                psEmp.setString(1, d.getName());
                psEmp.setDouble(2, d.getSalary());
                psEmp.setString(3, d.getSex());
                psEmp.setString(4, d.getMobNo());
                psEmp.setString(5, d.getAddress());
                psEmp.setString(6, d.getState());
                psEmp.setString(7, d.getCity());
                psEmp.setString(8, d.getPinNo());
                psEmp.executeUpdate();

                ResultSet rs = psEmp.getGeneratedKeys();
                if (rs.next()) {
                    int eId = rs.getInt(1);
                    d.seteId(eId);
                }
            }

            try (PreparedStatement psDoc = conn.prepareStatement(docSql)) {
                psDoc.setInt(1, d.geteId());
                psDoc.setString(2, d.getDept());
                psDoc.setString(3, d.getQualification());
                psDoc.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean updateDoctor(Doctor d) throws SQLException {
        String empSql = "UPDATE Employee SET Name=?, Salary=?, Sex=?, Mob_No=?, Address=?, State=?, City=?, Pin_no=? WHERE E_ID=?";
        String docSql = "UPDATE Doctor SET Dept=?, Qualification=? WHERE E_ID=?";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psEmp = conn.prepareStatement(empSql)) {
                psEmp.setString(1, d.getName());
                psEmp.setDouble(2, d.getSalary());
                psEmp.setString(3, d.getSex());
                psEmp.setString(4, d.getMobNo());
                psEmp.setString(5, d.getAddress());
                psEmp.setString(6, d.getState());
                psEmp.setString(7, d.getCity());
                psEmp.setString(8, d.getPinNo());
                psEmp.setInt(9, d.geteId());
                psEmp.executeUpdate();
            }

            try (PreparedStatement psDoc = conn.prepareStatement(docSql)) {
                psDoc.setString(1, d.getDept());
                psDoc.setString(2, d.getQualification());
                psDoc.setInt(3, d.geteId());
                psDoc.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean deleteDoctor(int eId) throws SQLException {
        String sqlDoc = "DELETE FROM Doctor WHERE E_ID=?";
        String sqlEmp = "DELETE FROM Employee WHERE E_ID=?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psDoc = conn.prepareStatement(sqlDoc)) {
                psDoc.setInt(1, eId);
                psDoc.executeUpdate();
            }

            try (PreparedStatement psEmp = conn.prepareStatement(sqlEmp)) {
                psEmp.setInt(1, eId);
                psEmp.executeUpdate();
            }

            conn.commit();
            return true;
        }
    }

    public List<Doctor> getAllDoctors() throws SQLException {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT e.E_ID, e.Name, e.Salary, e.Sex, e.Mob_No, e.Address, e.State, e.City, e.Pin_no, d.Dept, d.Qualification " +
                "FROM Employee e INNER JOIN Doctor d ON e.E_ID = d.E_ID";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Doctor(
                        rs.getInt("E_ID"),
                        rs.getString("Name"),
                        rs.getDouble("Salary"),
                        rs.getString("Sex"),
                        rs.getString("Mob_No"),
                        rs.getString("Address"),
                        rs.getString("State"),
                        rs.getString("City"),
                        rs.getString("Pin_no"),
                        rs.getString("Dept"),
                        rs.getString("Qualification")
                ));
            }
        }
        return list;
    }
}
