package hospital.controller;

import hospital.util.SceneUtil;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ReceptionistDashboardController {

    @FXML
    private void handleRegisterPatient() {
        Stage stage = SceneUtil.getCurrentStageFromEvent();
        SceneUtil.switchScene(stage, "RegisterPatient.fxml", "Register Patient");
    }

    @FXML
    private void handleAssignRoom() {
        Stage stage = SceneUtil.getCurrentStageFromEvent();
        SceneUtil.switchScene(stage, "AssignRoom.fxml", "Assign Room");
    }

    @FXML
    private void handleCreateBill() {
        Stage stage = SceneUtil.getCurrentStageFromEvent();
        SceneUtil.switchScene(stage, "CreateBill.fxml", "Create Bill");
    }

    @FXML
    private void handleViewBills() {
        Stage stage = SceneUtil.getCurrentStageFromEvent();
        SceneUtil.switchScene(stage, "ViewBills.fxml", "View Bills");
    }

    @FXML
    private void handleViewReports() {
        Stage stage = SceneUtil.getCurrentStageFromEvent();
        SceneUtil.switchScene(stage, "ViewReports.fxml", "View Reports");
    }

    @FXML
    private void handleViewPatients() {
        Stage stage = SceneUtil.getCurrentStageFromEvent();
        SceneUtil.switchScene(stage, "ManagePatients.fxml", "Manage Patients");
    }

    @FXML
    private void handleLogout() {
        Stage stage = SceneUtil.getCurrentStageFromEvent();
        SceneUtil.switchScene(stage, "Login.fxml", "Login");
    }
}
