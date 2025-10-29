package hospital.controller;

import hospital.dao.PatientDAO;
import hospital.dao.PrescriptionDAO;
import hospital.dao.TestReportDAO;
import hospital.model.Patient;
import hospital.model.Prescription;
import hospital.model.TestReport;
import hospital.util.Session;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DoctorDashboardController {

    @FXML private TableView<Patient> tblPatients;
    @FXML private TableColumn<Patient, Integer> colId;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, Integer> colAge;
    @FXML private TableColumn<Patient, String> colGender;

    @FXML private Label lblSelectedPatient;
    @FXML private TextArea txtPrescription;

    @FXML private TableView<TestReport> tblReports;
    @FXML private TableColumn<TestReport, Integer> colReportId;
    @FXML private TableColumn<TestReport, String> colTestType;
    @FXML private TableColumn<TestReport, String> colResult;
    @FXML private TableColumn<TestReport, LocalDate> colReportDate;
    @FXML private TableColumn<TestReport, String> colStatus;

    @FXML private TableView<Prescription> tblPrescriptionHistory;
    @FXML private TableColumn<Prescription, String> colDoctor;
    @FXML private TableColumn<Prescription, String> colPrescription;
    @FXML private TableColumn<Prescription, String> colDate;

    @FXML private Button btnLogout;


    private Patient selectedPatient;
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final TestReportDAO testReportDAO = new TestReportDAO();

    @FXML
    public void initialize() {
        setupPatientColumns();
        setupPrescriptionHistoryColumns();
        setupReportColumns();

        try {
            loadPatients();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // When a patient is selected
        tblPatients.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedPatient = newSel;
            if (newSel != null) {
                lblSelectedPatient.setText("Selected: " + newSel.getName());
                loadLatestPrescription(newSel.getPId());
                loadPrescriptionHistory(newSel.getPId());
                loadReportsForPatient(newSel.getPId());
            }
        });
    }

    private void setupPatientColumns() {
        colId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPId()).asObject());
        colName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colAge.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getAge()).asObject());
        colGender.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getGender()));
    }

    private void setupPrescriptionHistoryColumns() {
        colDoctor.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDoctorName()));
        colPrescription.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getPrescription()));
        colDate.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDate().toString()));
    }

    private void setupReportColumns() {
        colReportId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getReportId()).asObject());
        colTestType.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTestType()));
        colResult.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getResult()));
        colReportDate.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getReportDate()));
        colStatus.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
    }

    private void loadPatients() throws SQLException {
        PatientDAO dao = new PatientDAO();
        List<Patient> patients = dao.getAllPatients();
        tblPatients.setItems(FXCollections.observableArrayList(patients));
    }

    private void loadReportsForPatient(int patientId) {
        try {
            List<TestReport> reports = testReportDAO.getReportsByPatient(patientId);
            tblReports.setItems(FXCollections.observableArrayList(reports));
        } catch (SQLException e) {
            e.printStackTrace();
            tblReports.getItems().clear();
        }
    }

    private void loadLatestPrescription(int patientId) {
        try {
            Prescription latest = prescriptionDAO.getLatestPrescriptionByPatient(patientId);
            txtPrescription.setText(latest != null ? latest.getPrescription() : "");
        } catch (SQLException e) {
            e.printStackTrace();
            txtPrescription.clear();
        }
    }

    private void loadPrescriptionHistory(int patientId) {
        try {
            List<Prescription> history = prescriptionDAO.getPrescriptionsByPatient(patientId);
            tblPrescriptionHistory.setItems(FXCollections.observableArrayList(history));
        } catch (SQLException e) {
            e.printStackTrace();
            tblPrescriptionHistory.getItems().clear();
        }
    }

    @FXML
    private void handleSavePrescription() {
        try {
            if (selectedPatient == null) {
                showAlert("Select Patient", "Please select a patient first.");
                return;
            }

            String text = txtPrescription.getText().trim();
            if (text.isEmpty()) {
                showAlert("Empty Field", "Prescription cannot be empty.");
                return;
            }

            String doctorName = Session.getLoggedInUser().getUsername();

            // Always insert a new prescription (no updates)
            Prescription p = new Prescription(selectedPatient.getPId(), doctorName, text);
            boolean saved = prescriptionDAO.addPrescription(p);
            if (saved) {
                showAlert("Success", "Prescription added successfully!");
                loadLatestPrescription(selectedPatient.getPId());
                loadPrescriptionHistory(selectedPatient.getPId());
            } else {
                showAlert("Failed", "Unable to save prescription.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleMouseEnter(MouseEvent event) {
        Button b = (Button) event.getSource();
        b.setStyle("-fx-background-color: linear-gradient(to right, #388e3c, #43a047);"
                + "-fx-text-fill: white; -fx-font-weight: bold;"
                + "-fx-background-radius: 10; -fx-padding: 8 20; -fx-cursor: hand;");
    }

    @FXML
    private void handleMouseExit(MouseEvent event) {
        Button b = (Button) event.getSource();
        b.setStyle("-fx-background-color: linear-gradient(to right, #43a047, #66bb6a);"
                + "-fx-text-fill: white; -fx-font-weight: bold;"
                + "-fx-background-radius: 10; -fx-padding: 8 20; -fx-cursor: hand;");
    }

    @FXML
    private void handleLogout() {
        try {
            // Clear session if you use one
            hospital.util.Session.clear();

            // Load login screen again
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/hospital/view/Login.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) btnLogout.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
