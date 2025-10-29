package hospital.controller;

import hospital.dao.EmployeeDAO;
import hospital.model.Employee;
import javafx.stage.Stage;
import hospital.util.SceneUtil;
import hospital.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ManageEmployeesController {

    @FXML private ComboBox<String> roleFilterCombo;
    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, Integer> colEId;
    @FXML private TableColumn<Employee, String> colName, colRole, colDept, colQualification, colSex, colMobNo, colCity;
    @FXML private TableColumn<Employee, Double> colSalary;

    @FXML private TextField txtName, txtDept, txtQualification, txtSalary, txtSex, txtMobNo, txtAddress, txtState, txtCity, txtPinNo;
    @FXML private ComboBox<String> cmbRole;
    @FXML private Button btnAdd, btnUpdate, btnDelete, btnRefresh;

    private Employee selectedEmployee;
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    @FXML
    public void initialize() {
        // Setup table columns
        colEId.setCellValueFactory(new PropertyValueFactory<>("eId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colDept.setCellValueFactory(new PropertyValueFactory<>("dept"));
        colQualification.setCellValueFactory(new PropertyValueFactory<>("qualification"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colSex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        colMobNo.setCellValueFactory(new PropertyValueFactory<>("mobNo"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));

        // Only non-admin roles
        ObservableList<String> roles = FXCollections.observableArrayList("Doctor", "Nurse", "Receptionist");
        roleFilterCombo.setItems(FXCollections.observableArrayList("All", "Doctor", "Nurse", "Receptionist"));
        cmbRole.setItems(roles);
        roleFilterCombo.setValue("All");

        loadAllEmployees();

        roleFilterCombo.setOnAction(e -> {
            String selectedRole = roleFilterCombo.getValue();
            if ("All".equals(selectedRole)) loadAllEmployees();
            else loadEmployeesByRole(selectedRole);
        });

        employeeTable.setOnMouseClicked(this::onRowSelect);
    }

    private void loadAllEmployees() {
        try {
            List<Employee> employees = employeeDAO.getAllEmployees()
                    .stream()
                    .filter(emp -> !emp.getRole().equalsIgnoreCase("administrator"))
                    .collect(Collectors.toList());
            employeeTable.setItems(FXCollections.observableArrayList(employees));
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtil.showError("Database Error", "Failed to load employees.");
        }
    }

    private void loadEmployeesByRole(String role) {
        try {
            List<Employee> employees = employeeDAO.getAllEmployees()
                    .stream()
                    .filter(emp -> emp.getRole().equalsIgnoreCase(role))
                    .collect(Collectors.toList());
            employeeTable.setItems(FXCollections.observableArrayList(employees));
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtil.showError("Database Error", "Failed to load employees.");
        }
    }

    private void onRowSelect(MouseEvent event) {
        selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            txtName.setText(selectedEmployee.getName());
            cmbRole.setValue(selectedEmployee.getRole());
            txtDept.setText(selectedEmployee.getDept());
            txtQualification.setText(selectedEmployee.getQualification());
            txtSalary.setText(String.valueOf(selectedEmployee.getSalary()));
            txtSex.setText(selectedEmployee.getSex());
            txtMobNo.setText(selectedEmployee.getMobNo());
            txtAddress.setText(selectedEmployee.getAddress());
            txtState.setText(selectedEmployee.getState());
            txtCity.setText(selectedEmployee.getCity());
            txtPinNo.setText(selectedEmployee.getPinNo());
        }
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        Employee e = new Employee(0,
                txtName.getText(),
                cmbRole.getValue(),
                txtDept.getText(),
                txtQualification.getText(),
                Double.parseDouble(txtSalary.getText()),
                txtSex.getText(),
                txtMobNo.getText(),
                txtAddress.getText(),
                txtState.getText(),
                txtCity.getText(),
                txtPinNo.getText()
        );

        try {
            if (employeeDAO.addEmployee(e)) {
                AlertUtil.showInfo("Success", "Employee added successfully!");
                loadAllEmployees();
            } else {
                AlertUtil.showError("Error", "Failed to add employee.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            AlertUtil.showError("Database Error", "Failed to add employee.");
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        if (selectedEmployee == null) {
            AlertUtil.showError("Error", "Select an employee to update.");
            return;
        }

        selectedEmployee.setName(txtName.getText());
        selectedEmployee.setRole(cmbRole.getValue());
        selectedEmployee.setDept(txtDept.getText());
        selectedEmployee.setQualification(txtQualification.getText());
        selectedEmployee.setSalary(Double.parseDouble(txtSalary.getText()));
        selectedEmployee.setSex(txtSex.getText());
        selectedEmployee.setMobNo(txtMobNo.getText());
        selectedEmployee.setAddress(txtAddress.getText());
        selectedEmployee.setState(txtState.getText());
        selectedEmployee.setCity(txtCity.getText());
        selectedEmployee.setPinNo(txtPinNo.getText());

        try {
            if (employeeDAO.updateEmployee(selectedEmployee)) {
                AlertUtil.showInfo("Success", "Employee updated successfully!");
                loadAllEmployees();
            } else {
                AlertUtil.showError("Error", "Failed to update employee.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            AlertUtil.showError("Database Error", "Failed to update employee.");
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        if (selectedEmployee == null) {
            AlertUtil.showError("Error", "Select an employee to delete.");
            return;
        }

        try {
            if (employeeDAO.deleteEmployee(selectedEmployee.getEId())) {
                AlertUtil.showInfo("Success", "Employee deleted successfully!");
                loadAllEmployees();
            } else {
                AlertUtil.showError("Error", "Failed to delete employee.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            AlertUtil.showError("Database Error", "Failed to delete employee.");
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadAllEmployees();
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) employeeTable.getScene().getWindow();
        SceneUtil.switchScene(stage, "AdminDashboard.fxml", "Admin Dashboard");
    }

}
