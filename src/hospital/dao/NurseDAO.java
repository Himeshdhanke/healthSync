package hospital.dao;

import hospital.model.Nurse;
import hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NurseDAO {

    public List<Nurse> getAllNurses() throws SQLException {
        List<Nurse> list = new ArrayList<>();
        String query = "SELECT * FROM Employee e JOIN Nurse n ON e.E_ID = n.E_ID";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Nurse nurse = new Nurse();
                nurse.seteId(rs.getInt("E_ID"));
                nurse.setName(rs.getString("Name"));
                nurse.setSalary(rs.getDouble("Salary"));
                nurse.setSex(rs.getString("Sex"));
                nurse.setMobNo(rs.getString("Mob_No"));
                nurse.setAddress(rs.getString("Address"));
                nurse.setState(rs.getString("State"));
                nurse.setCity(rs.getString("City"));
                nurse.setPinNo(rs.getString("Pin_no"));
                list.add(nurse);
            }
        }
        return list;
    }

    public boolean addNurse(Nurse nurse) throws SQLException {
        String empQuery = "INSERT INTO Employee (Name, Salary, Sex, Mob_No, Address, State, City, Pin_no) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String nurseQuery = "INSERT INTO Nurse (E_ID) VALUES (?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstEmp = con.prepareStatement(empQuery, Statement.RETURN_GENERATED_KEYS)) {

            pstEmp.setString(1, nurse.getName());
            pstEmp.setDouble(2, nurse.getSalary());
            pstEmp.setString(3, nurse.getSex());
            pstEmp.setString(4, nurse.getMobNo());
            pstEmp.setString(5, nurse.getAddress());
            pstEmp.setString(6, nurse.getState());
            pstEmp.setString(7, nurse.getCity());
            pstEmp.setString(8, nurse.getPinNo());

            int affectedRows = pstEmp.executeUpdate();
            if (affectedRows == 0) return false;

            try (ResultSet generatedKeys = pstEmp.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int eId = generatedKeys.getInt(1);
                    try (PreparedStatement pstNurse = con.prepareStatement(nurseQuery)) {
                        pstNurse.setInt(1, eId);
                        pstNurse.executeUpdate();
                    }
                } else return false;
            }
        }
        return true;
    }

    public boolean updateNurse(Nurse nurse) throws SQLException {
        String query = "UPDATE Employee SET Name=?, Salary=?, Sex=?, Mob_No=?, Address=?, State=?, City=?, Pin_no=? WHERE E_ID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, nurse.getName());
            pst.setDouble(2, nurse.getSalary());
            pst.setString(3, nurse.getSex());
            pst.setString(4, nurse.getMobNo());
            pst.setString(5, nurse.getAddress());
            pst.setString(6, nurse.getState());
            pst.setString(7, nurse.getCity());
            pst.setString(8, nurse.getPinNo());
            pst.setInt(9, nurse.geteId());

            return pst.executeUpdate() > 0;
        }
    }

    public boolean deleteNurse(int eId) throws SQLException {
        String query = "DELETE FROM Nurse WHERE E_ID=?";
        String empQuery = "DELETE FROM Employee WHERE E_ID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstNurse = con.prepareStatement(query);
             PreparedStatement pstEmp = con.prepareStatement(empQuery)) {

            pstNurse.setInt(1, eId);
            pstNurse.executeUpdate();

            pstEmp.setInt(1, eId);
            pstEmp.executeUpdate();
        }
        return true;
    }
}
