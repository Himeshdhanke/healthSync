package hospital.controller;

import hospital.util.SceneUtil;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ReceptionistDashboardController {

    @FXML
    private void handleAssignRoom() {
        Stage stage = SceneUtil.getCurrentStageFromEvent();
        SceneUtil.switchScene(stage, "AssignRoom.fxml", "Assign Room");
    }

    @FXML
    private void handleCreateBill() {
        Stage stage = SceneUtil.getCurrentStageFromEvent();
        SceneUtil.switchScene(stage, "ManageBills.fxml", "Manage Bills");
    }

    @FXML
    private void handleViewReports() {
        Stage stage = SceneUtil.getCurrentStageFromEvent();
        SceneUtil.switchScene(stage, "ManageReports.fxml", "Manage Reports");
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
