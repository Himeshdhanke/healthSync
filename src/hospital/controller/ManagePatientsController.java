package hospital.controller;

import hospital.dao.PatientDAO;
import hospital.model.Patient;
import hospital.util.AlertUtil;
import hospital.util.SceneUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javafx.stage.Stage;

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

    private PatientDAO patientDAO = new PatientDAO();

    @FXML
    public void initialize() {
        // Setup table columns using PropertyValueFactory
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
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
        // Load selected patient data into form fields
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

        // Read and validate input fields
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

        // Set updated values
        selected.setName(name);
        selected.setAge(age);
        selected.setGender(gender);
        selected.setDob(dob);
        selected.setMobNo(mobNo);

        // Update in DB
        try {
            if (patientDAO.updatePatient(selected)) {
                AlertUtil.showInfo("Success", "Patient updated successfully!");
                loadPatients();  // Refresh table
                clearFields();   // Clear form
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
                    if (patientDAO.deletePatient(selected.getId())) {
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
    public void goBack() {
        // Navigate back to Admin Dashboard
        try {
            Stage stage = (Stage) patientTable.getScene().getWindow();
            SceneUtil.switchScene(stage, "/AdminDashboard.fxml","Admin DashBoard");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Navigation Error", "Cannot go back: " + e.getMessage());
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
