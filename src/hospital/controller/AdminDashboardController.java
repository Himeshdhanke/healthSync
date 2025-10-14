package hospital.controller;

import hospital.util.SceneUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminDashboardController {

    @FXML private Button btnLogout;
    @FXML private Button btnManagePatients;
    @FXML private Button btnManageEmployees;
    @FXML private Button btnManageRooms;

    @FXML
    private void handleManagePatients() {
        // Get the current stage from any node in the scene
        Stage stage = (Stage) btnManagePatients.getScene().getWindow();
        SceneUtil.switchScene(stage, "ManagePatients.fxml", "Manage Patients");
    }

    @FXML
    private void handleManageEmployees() {
        Stage stage = (Stage) btnManageEmployees.getScene().getWindow();
        SceneUtil.switchScene(stage, "ManageEmployees.fxml", "Manage Employees");
    }

    @FXML
    private void handleManageRooms() {
        Stage stage = (Stage) btnManageRooms.getScene().getWindow();
        SceneUtil.switchScene(stage, "ManageRooms.fxml", "Manage Rooms");
    }

    @FXML
    private void handleLogout() {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        SceneUtil.switchScene(stage, "Login.fxml", "Login");
    }
}
