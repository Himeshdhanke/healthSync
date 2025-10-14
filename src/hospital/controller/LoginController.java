package hospital.controller;

import hospital.dao.EmployeeDAO;
import hospital.model.Employee;
import hospital.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblStatus;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Please fill all fields!");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }

        Employee emp = EmployeeDAO.validateLogin(username, password);

        if (emp != null) {
            lblStatus.setText("Login successful! Role: " + emp.getRole());
            lblStatus.setStyle("-fx-text-fill: green;");

            Stage stage = (Stage) txtUsername.getScene().getWindow();

            System.out.println("Role = " + emp.getRole());

            // Navigate based on role
            switch (emp.getRole().toLowerCase()) {
                case "administrator":
                    SceneUtil.switchScene(stage, "AdminDashboard.fxml", "Admin Dashboard");
                    break;
                case "receptionist":
                    SceneUtil.switchScene(stage, "ReceptionistDashboard.fxml", "Receptionist Dashboard");
                    break;
                case "doctor":
                    SceneUtil.switchScene(stage, "PatientDashboard.fxml", "Doctor Dashboard");
                    break;
                default:
                    lblStatus.setText("No dashboard defined for role: " + emp.getRole());
                    lblStatus.setStyle("-fx-text-fill: red;");
            }

        } else {
            lblStatus.setText("Invalid username or password!");
            lblStatus.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleReset() {
        txtUsername.clear();
        txtPassword.clear();
        lblStatus.setText("");
    }
}
