package hospital.controller;

import hospital.dao.PatientDAO;
import hospital.model.Patient;
import hospital.util.SceneUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class ViewPatientsController {

    @FXML private TableView<Patient> tblPatients;
    @FXML private TableColumn<Patient, Integer> colId;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, Integer> colAge;
    @FXML private TableColumn<Patient, String> colGender;
    @FXML private TableColumn<Patient, String> colDob;
    @FXML private TableColumn<Patient, String> colMobNo;
    @FXML private Button btnBack;

    private final PatientDAO patientDAO = new PatientDAO();

    @FXML
    public void initialize() {
        setupColumns();
        loadPatients();
    }

    private void setupColumns() {
        colId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPId()).asObject());
        colName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colAge.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getAge()).asObject());
        colGender.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getGender()));
        colDob.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDob() != null ? data.getValue().getDob().toString() : ""));
        colMobNo.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getMobNo()));
    }

    private void loadPatients() {
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            tblPatients.setItems(FXCollections.observableArrayList(patients));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading patients: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        SceneUtil.switchScene(stage, "ReceptionistDashboard.fxml", "Receptionist Dashboard");
    }
}
