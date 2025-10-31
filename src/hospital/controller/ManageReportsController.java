package hospital.controller;

import hospital.dao.TestReportDAO;
import hospital.model.TestReport;
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

public class ManageReportsController {

    @FXML private TableView<TestReport> reportTable;
    @FXML private TableColumn<TestReport, Integer> colReportId;
    @FXML private TableColumn<TestReport, String> colPatientName;
    @FXML private TableColumn<TestReport, String> colTestType;
    @FXML private TableColumn<TestReport, String> colResult;
    @FXML private TableColumn<TestReport, LocalDate> colReportDate;
    @FXML private TableColumn<TestReport, String> colStatus;

    @FXML private TextField txtPatientName;
    @FXML private TextField txtTestType;
    @FXML private TextArea txtResult;
    @FXML private DatePicker txtReportDate;
    @FXML private TextField txtStatus;

    private final TestReportDAO reportDAO = new TestReportDAO();

    @FXML
    public void initialize() {
        colReportId.setCellValueFactory(new PropertyValueFactory<>("reportId"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colTestType.setCellValueFactory(new PropertyValueFactory<>("testType"));
        colResult.setCellValueFactory(new PropertyValueFactory<>("result"));
        colReportDate.setCellValueFactory(new PropertyValueFactory<>("reportDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadReports();
    }

    @FXML
    public void loadReports() {
        try {
            List<TestReport> list = reportDAO.getAllReports();
            ObservableList<TestReport> data = FXCollections.observableArrayList(list);
            reportTable.setItems(data);
        } catch (SQLException e) {
            AlertUtil.showError("Error loading reports", e.getMessage());
        }
    }

    @FXML
    public void handleAdd() {
        try {
            String patientName = txtPatientName.getText().trim();
            String testType = txtTestType.getText().trim();
            String result = txtResult.getText().trim();
            LocalDate reportDate = txtReportDate.getValue();
            String status = txtStatus.getText().trim();

            if (patientName.isEmpty() || testType.isEmpty() || result.isEmpty() || reportDate == null || status.isEmpty()) {
                AlertUtil.showWarning("Validation Error", "All fields are required!");
                return;
            }

            TestReport report = new TestReport();
            report.setPatientName(patientName);
            report.setTestType(testType);
            report.setResult(result);
            report.setReportDate(reportDate);
            report.setStatus(status);

            if (reportDAO.addReport(report)) {
                AlertUtil.showInfo("Success", "Report added successfully!");
                loadReports();
                clearFields();
            } else {
                AlertUtil.showError("Error", "Failed to add report!");
            }
        } catch (Exception e) {
            AlertUtil.showError("Exception", e.getMessage());
        }
    }

    @FXML
    public void handleEdit() {
        TestReport selected = reportTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtPatientName.setText(selected.getPatientName());
            txtTestType.setText(selected.getTestType());
            txtResult.setText(selected.getResult());
            txtReportDate.setValue(selected.getReportDate());
            txtStatus.setText(selected.getStatus());
        } else {
            AlertUtil.showWarning("Selection Error", "Please select a report first!");
        }
    }

    @FXML
    public void handleUpdate() {
        TestReport selected = reportTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("Selection Error", "Select a report first!");
            return;
        }

        String patientName = txtPatientName.getText().trim();
        String testType = txtTestType.getText().trim();
        String result = txtResult.getText().trim();
        LocalDate reportDate = txtReportDate.getValue();
        String status = txtStatus.getText().trim();

        if (patientName.isEmpty() || testType.isEmpty() || result.isEmpty() || reportDate == null || status.isEmpty()) {
            AlertUtil.showWarning("Validation Error", "All fields are required!");
            return;
        }

        selected.setPatientName(patientName);
        selected.setTestType(testType);
        selected.setResult(result);
        selected.setReportDate(reportDate);
        selected.setStatus(status);

        try {
            if (reportDAO.updateReport(selected)) {
                AlertUtil.showInfo("Success", "Report updated successfully!");
                loadReports();
                clearFields();
            } else {
                AlertUtil.showError("Error", "Failed to update report!");
            }
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", e.getMessage());
        }
    }

    @FXML
    public void handleDelete() {
        TestReport selected = reportTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean confirmed = AlertUtil.showConfirmation("Confirm Delete", "Are you sure you want to delete this report?");
            if (confirmed) {
                try {
                    if (reportDAO.deleteReport(selected.getReportId())) {
                        AlertUtil.showInfo("Deleted", "Report deleted successfully!");
                        loadReports();
                        clearFields();
                    } else {
                        AlertUtil.showError("Error", "Failed to delete report!");
                    }
                } catch (SQLException e) {
                    AlertUtil.showError("Exception", e.getMessage());
                }
            }
        } else {
            AlertUtil.showWarning("Selection Error", "Select a report first!");
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        User loggedInUser = Session.getLoggedInUser();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (loggedInUser == null) {
            SceneUtil.switchScene(stage, "Login.fxml", "Login");
            return;
        }

        String role = loggedInUser.getRole();

        if ("Administrator".equalsIgnoreCase(role)) {
            SceneUtil.switchScene(stage, "AdminDashboard.fxml", "Admin Dashboard");
        } else if ("Receptionist".equalsIgnoreCase(role)) {
            SceneUtil.switchScene(stage, "ReceptionistDashboard.fxml", "Receptionist Dashboard");
        } else {
            SceneUtil.switchScene(stage, "Login.fxml", "Login");
        }
    }

    private void clearFields() {
        txtPatientName.clear();
        txtTestType.clear();
        txtResult.clear();
        txtReportDate.setValue(null);
        txtStatus.clear();
    }
}
