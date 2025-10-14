//package hospital.controller;
//
//import hospital.dao.EmployeeDAO;
//import hospital.model.Employee;
//import hospital.util.AlertUtil;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//
//import java.sql.SQLException;
//
//public class ManageEmployeesController {
//
//    @FXML private TableView<Employee> employeeTable;
//    @FXML private TableColumn<Employee, Integer> colId;
//    @FXML private TableColumn<Employee, String> colName;
//    @FXML private TableColumn<Employee, Double> colSalary;
//    @FXML private TableColumn<Employee, String> colSex;
//    @FXML private TableColumn<Employee, String> colMob;
//    @FXML private TableColumn<Employee, String> colAddress;
//    @FXML private TableColumn<Employee, String> colState;
//    @FXML private TableColumn<Employee, String> colCity;
//    @FXML private TableColumn<Employee, String> colPin;
//
//    @FXML private TextField txtName, txtSalary, txtSex, txtMobNo, txtAddress, txtState, txtCity, txtPin;
//    @FXML private Button btnAdd, btnUpdate, btnDelete, btnClear;
//
//    private final EmployeeDAO employeeDAO = new EmployeeDAO();
//    private ObservableList<Employee> employeeList;
//
//    @FXML
//    public void initialize() {
//        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().geteId()).asObject());
//        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
//        colSalary.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getSalary()));
//        colSex.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSex()));
//        colMob.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMobNo()));
//        colAddress.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAddress()));
//        colState.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getState()));
//        colCity.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCity()));
//        colPin.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPinNo()));
//
//        loadEmployees();
//
//        employeeTable.setOnMouseClicked(event -> {
//            Employee e = employeeTable.getSelectionModel().getSelectedItem();
//            if (e != null) fillForm(e);
//        });
//    }
//
//    private void loadEmployees() {
//        try {
//            employeeList = FXCollections.observableArrayList(employeeDAO.getAllEmployees());
//            employeeTable.setItems(employeeList);
//        } catch (SQLException e) {
//            AlertUtil.showError("Database Error", e.getMessage());
//        }
//    }
//
//    private void fillForm(Employee e) {
//        txtName.setText(e.getName());
//        txtSalary.setText(String.valueOf(e.getSalary()));
//        txtSex.setText(e.getSex());
//        txtMobNo.setText(e.getMobNo());
//        txtAddress.setText(e.getAddress());
//        txtState.setText(e.getState());
//        txtCity.setText(e.getCity());
//        txtPin.setText(e.getPinNo());
//    }
//
//    @FXML
//    private void handleAdd() {
//        try {
//            Employee e = new Employee();
//            e.setName(txtName.getText());
//            e.setSalary(Double.parseDouble(txtSalary.getText()));
//            e.setSex(txtSex.getText());
//            e.setMobNo(txtMobNo.getText());
//            e.setAddress(txtAddress.getText());
//            e.setState(txtState.getText());
//            e.setCity(txtCity.getText());
//            e.setPinNo(txtPin.getText());
//
//            if (employeeDAO.addEmployee(e)) {
//                AlertUtil.showInfo("Success", "Employee added successfully!");
//                loadEmployees();
//                clearFields();
//            }
//        } catch (Exception ex) {
//            AlertUtil.showError("Error", ex.getMessage());
//        }
//    }
//
//    @FXML
//    private void handleUpdate() {
//        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
//        if (selected == null) {
//            AlertUtil.showWarning("Selection Error", "Select an employee first!");
//            return;
//        }
//
//        try {
//            selected.setName(txtName.getText());
//            selected.setSalary(Double.parseDouble(txtSalary.getText()));
//            selected.setSex(txtSex.getText());
//            selected.setMobNo(txtMobNo.getText());
//            selected.setAddress(txtAddress.getText());
//            selected.setState(txtState.getText());
//            selected.setCity(txtCity.getText());
//            selected.setPinNo(txtPin.getText());
//
//            if (employeeDAO.updateEmployee(selected)) {
//                AlertUtil.showInfo("Success", "Employee updated successfully!");
//                loadEmployees();
//                clearFields();
//            } else {
//                AlertUtil.showError("Update Failed", "Could not update employee record!");
//            }
//        } catch (Exception ex) {
//            AlertUtil.showError("Error", ex.getMessage());
//        }
//    }
//
//    @FXML
//    private void handleDelete() {
//        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
//        if (selected == null) {
//            AlertUtil.showWarning("Selection Error", "Select an employee to delete!");
//            return;
//        }
//
//        if (AlertUtil.showConfirmation("Confirm Delete", "Are you sure you want to delete this employee?")) {
//            try {
//                employeeDAO.deleteEmployee(selected.geteId());
//                AlertUtil.showInfo("Deleted", "Employee deleted successfully!");
//                loadEmployees();
//                clearFields();
//            } catch (SQLException e) {
//                AlertUtil.showError("Database Error", e.getMessage());
//            }
//        }
//    }
//
//    @FXML
//    private void clearFields() {
//        txtName.clear();
//        txtSalary.clear();
//        txtSex.clear();
//        txtMobNo.clear();
//        txtAddress.clear();
//        txtState.clear();
//        txtCity.clear();
//        txtPin.clear();
//    }
//}
