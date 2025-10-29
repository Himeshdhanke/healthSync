package hospital.dao;

import hospital.model.Employee;
import hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // 🔹 Add new employee
    public boolean addEmployee(Employee emp) throws SQLException {
        String sql = "INSERT INTO Employee (Name, Role, Dept, Qualification, Salary, Sex, Mob_No, Address, State, City, Pin_no) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, emp.getName());
            pst.setString(2, emp.getRole());
            pst.setString(3, emp.getDept());
            pst.setString(4, emp.getQualification());
            pst.setDouble(5, emp.getSalary());
            pst.setString(6, emp.getSex());
            pst.setString(7, emp.getMobNo());
            pst.setString(8, emp.getAddress());
            pst.setString(9, emp.getState());
            pst.setString(10, emp.getCity());
            pst.setString(11, emp.getPinNo());

            int affectedRows = pst.executeUpdate();
            if (affectedRows == 0) return false;

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    emp.setEId(generatedKeys.getInt(1));
                }
            }
            return true;
        }
    }

    // 🔹 Update employee
    public boolean updateEmployee(Employee emp) throws SQLException {
        String sql = "UPDATE Employee SET Name=?, Role=?, Dept=?, Qualification=?, Salary=?, Sex=?, Mob_No=?, Address=?, State=?, City=?, Pin_no=? WHERE E_ID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, emp.getName());
            pst.setString(2, emp.getRole());
            pst.setString(3, emp.getDept());
            pst.setString(4, emp.getQualification());
            pst.setDouble(5, emp.getSalary());
            pst.setString(6, emp.getSex());
            pst.setString(7, emp.getMobNo());
            pst.setString(8, emp.getAddress());
            pst.setString(9, emp.getState());
            pst.setString(10, emp.getCity());
            pst.setString(11, emp.getPinNo());
            pst.setInt(12, emp.getEId());

            return pst.executeUpdate() > 0;
        }
    }

    // 🔹 Delete employee
    public boolean deleteEmployee(int eId) throws SQLException {
        String sql = "DELETE FROM Employee WHERE E_ID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, eId);
            return pst.executeUpdate() > 0;
        }
    }

    // 🔹 Get all employees
    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM Employee";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapEmployee(rs));
            }
        }
        return list;
    }

    // 🔹 Get employees by role
    public List<Employee> getEmployeesByRole(String role) throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE Role=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, role);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(mapEmployee(rs));
                }
            }
        }
        return list;
    }

    // 🔹 Get employee by ID
    public Employee getEmployeeById(int eId) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE E_ID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, eId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapEmployee(rs);
            }
        }
        return null;
    }

    // 🔹 Validate login (Name + E_ID as password for now)
    public Employee validateLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE Name=? AND E_ID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, username);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapEmployee(rs);
            }
        }
        return null;
    }

    // 🔹 Helper to map ResultSet → Employee
    private Employee mapEmployee(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setEId(rs.getInt("E_ID"));
        emp.setName(rs.getString("Name"));
        emp.setRole(rs.getString("Role"));
        emp.setDept(rs.getString("Dept"));
        emp.setQualification(rs.getString("Qualification"));
        emp.setSalary(rs.getDouble("Salary"));
        emp.setSex(rs.getString("Sex"));
        emp.setMobNo(rs.getString("Mob_No"));
        emp.setAddress(rs.getString("Address"));
        emp.setState(rs.getString("State"));
        emp.setCity(rs.getString("City"));
        emp.setPinNo(rs.getString("Pin_no"));
        return emp;
    }
}
