package hospital.controller;

import hospital.dao.PatientDAO;
import hospital.model.Patient;
import hospital.model.User;
import hospital.util.AlertUtil;
import hospital.util.SceneUtil;
import hospital.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ManagePatientsController {

    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Integer> colId;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, Integer> colAge;
    @FXML private TableColumn<Patient, String> colGender;
    @FXML private TableColumn<Patient, LocalDate> colDOB;
    @FXML private TableColumn<Patient, String> colMobNo;

    @FXML private TextField txtName;
    @FXML private TextField txtAge;
    @FXML private TextField txtGender;
    @FXML private DatePicker txtDOB;
    @FXML private TextField txtContact;

    private final PatientDAO patientDAO = new PatientDAO();

    @FXML
    public void initialize() {
        // ✅ Setup table columns correctly
        colId.setCellValueFactory(new PropertyValueFactory<>("pId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colMobNo.setCellValueFactory(new PropertyValueFactory<>("mobNo"));

        loadPatients();
    }

    @FXML
    public void loadPatients() {
        try {
            List<Patient> list = patientDAO.getAllPatients();
            ObservableList<Patient> data = FXCollections.observableArrayList(list);
            patientTable.setItems(data);
        } catch (SQLException e) {
            AlertUtil.showError("Error loading patients", e.getMessage());
        }
    }

    @FXML
    public void loadSelectedPatient() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtName.setText(selected.getName());
            txtAge.setText(String.valueOf(selected.getAge()));
            txtGender.setText(selected.getGender());
            txtDOB.setValue(selected.getDob());
            txtContact.setText(selected.getMobNo());
        } else {
            AlertUtil.showWarning("Selection Error", "Please select a patient first!");
        }
    }

    @FXML
    public void handleAdd() {
        try {
            String name = txtName.getText().trim();
            String ageText = txtAge.getText().trim();
            String gender = txtGender.getText().trim();
            LocalDate dob = txtDOB.getValue();
            String mobNo = txtContact.getText().trim();

            if (name.isEmpty() || ageText.isEmpty() || gender.isEmpty() || dob == null || mobNo.isEmpty()) {
                AlertUtil.showWarning("Validation Error", "All fields are required!");
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException e) {
                AlertUtil.showWarning("Validation Error", "Age must be a number!");
                return;
            }

            Patient patient = new Patient();
            patient.setName(name);
            patient.setAge(age);
            patient.setGender(gender);
            patient.setDob(dob);
            patient.setMobNo(mobNo);

            if (patientDAO.addPatient(patient)) {
                AlertUtil.showInfo("Success", "Patient added successfully!");
                loadPatients();
                clearFields();
            } else {
                AlertUtil.showError("Error", "Failed to add patient!");
            }

        } catch (Exception e) {
            AlertUtil.showError("Exception", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleEdit() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtName.setText(selected.getName());
            txtAge.setText(String.valueOf(selected.getAge()));
            txtGender.setText(selected.getGender());
            txtDOB.setValue(selected.getDob());
            txtContact.setText(selected.getMobNo());
        } else {
            AlertUtil.showWarning("Selection Error", "Please select a patient first!");
        }
    }

    @FXML
    public void handleUpdate() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("Selection Error", "Please select a patient first!");
            return;
        }

        // Validate input
        String name = txtName.getText().trim();
        String ageText = txtAge.getText().trim();
        String gender = txtGender.getText().trim();
        LocalDate dob = txtDOB.getValue();
        String mobNo = txtContact.getText().trim();

        if (name.isEmpty() || ageText.isEmpty() || gender.isEmpty() || dob == null || mobNo.isEmpty()) {
            AlertUtil.showWarning("Validation Error", "All fields are required!");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            AlertUtil.showWarning("Validation Error", "Age must be a number!");
            return;
        }

        // Update fields
        selected.setName(name);
        selected.setAge(age);
        selected.setGender(gender);
        selected.setDob(dob);
        selected.setMobNo(mobNo);

        try {
            if (patientDAO.updatePatient(selected)) {
                AlertUtil.showInfo("Success", "Patient updated successfully!");
                loadPatients();
                clearFields();
            } else {
                AlertUtil.showError("Error", "Failed to update patient!");
            }
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", e.getMessage());
        }
    }

    @FXML
    public void handleDelete() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean confirmed = AlertUtil.showConfirmation("Confirm Delete", "Are you sure you want to delete this patient?");
            if (confirmed) {
                try {
                    // ✅ FIXED: use getPId() instead of getId()
                    if (patientDAO.deletePatient(selected.getPId())) {
                        AlertUtil.showInfo("Deleted", "Patient deleted successfully!");
                        loadPatients();
                        clearFields();
                    } else {
                        AlertUtil.showError("Error", "Failed to delete patient!");
                    }
                } catch (SQLException e) {
                    AlertUtil.showError("Exception", e.getMessage());
                }
            }
        } else {
            AlertUtil.showWarning("Selection Error", "Select a patient first!");
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        User loggedInUser = Session.getLoggedInUser();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (loggedInUser == null) {
            // Not logged in → send back to Login
            SceneUtil.switchScene(stage, "Login.fxml", "Login");
            return;
        }

        String role = loggedInUser.getRole();

        if ("Administrator".equalsIgnoreCase(role)) {
            SceneUtil.switchScene(stage, "AdminDashboard.fxml", "Admin Dashboard");
        } else if ("Receptionist".equalsIgnoreCase(role)) {
            SceneUtil.switchScene(stage, "ReceptionistDashboard.fxml", "Receptionist Dashboard");
        } else {
            // Unknown role fallback
            SceneUtil.switchScene(stage, "Login.fxml", "Login");
        }
    }

    private void clearFields() {
        txtName.clear();
        txtAge.clear();
        txtGender.clear();
        txtDOB.setValue(null);
        txtContact.clear();
    }
}
