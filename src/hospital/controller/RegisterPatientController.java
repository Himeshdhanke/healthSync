package hospital.controller;

import hospital.dao.PatientDAO;
import hospital.model.Patient;
import hospital.util.SceneUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class RegisterPatientController {

    @FXML private TextField txtName;
    @FXML private TextField txtAge;
    @FXML private ComboBox<String> cmbGender;
    @FXML private DatePicker dpDob;
    @FXML private TextField txtPhone;
    @FXML private Button btnSave;
    @FXML private Button btnBack;

    private final PatientDAO patientDAO = new PatientDAO();

    @FXML
    public void initialize() {
        cmbGender.getItems().addAll("Male", "Female", "Other");
    }

    @FXML
    private void handleSavePatient() {
        try {
            String name = txtName.getText().trim();
            String ageText = txtAge.getText().trim();
            String gender = cmbGender.getValue();
            LocalDate dob = dpDob.getValue();
            String phone = txtPhone.getText().trim();

            if (name.isEmpty() || ageText.isEmpty() || gender == null || dob == null || phone.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Incomplete Data", "Please fill all fields.");
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Age", "Please enter a valid number for age.");
                return;
            }

            Patient patient = new Patient(name, age, gender, dob, phone);
            boolean success = patientDAO.addPatient(patient);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Patient registered successfully!");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to register patient.");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        SceneUtil.switchScene(stage, "ReceptionistDashboard.fxml", "Receptionist Dashboard");
    }

    private void clearFields() {
        txtName.clear();
        txtAge.clear();
        cmbGender.getSelectionModel().clearSelection();
        dpDob.setValue(null);
        txtPhone.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
