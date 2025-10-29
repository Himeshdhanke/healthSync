package hospital.controller;

import hospital.dao.UserDAO;
import hospital.model.User;
import hospital.util.SceneUtil;
import hospital.util.Session; // ✅ Import Session
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.SQLException;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblStatus;
    @FXML private Button btnLogin;
    @FXML private Button btnReset;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Please fill all fields!");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }

        UserDAO userDAO = new UserDAO();

        try {
            User user = userDAO.validateLogin(username, password);

            if (user != null) {
                // ✅ Save logged-in user to Session
                Session.setLoggedInUser(user);

                lblStatus.setText("Login successful! Role: " + user.getRole());
                lblStatus.setStyle("-fx-text-fill: green;");

                Stage stage = (Stage) txtUsername.getScene().getWindow();
                System.out.println("Role = " + user.getRole());

                switch (user.getRole().toLowerCase()) {
                    case "administrator":
                        SceneUtil.switchScene(stage, "AdminDashboard.fxml", "Admin Dashboard");
                        break;
                    case "doctor":
                        SceneUtil.switchScene(stage, "DoctorDashboard.fxml", "Doctor Dashboard");
                        break;
                    case "receptionist":
                        SceneUtil.switchScene(stage, "ReceptionistDashboard.fxml", "Receptionist Dashboard");
                        break;
                    default:
                        lblStatus.setText("No dashboard defined for role: " + user.getRole());
                        lblStatus.setStyle("-fx-text-fill: red;");
                }

            } else {
                lblStatus.setText("Invalid username or password!");
                lblStatus.setStyle("-fx-text-fill: red;");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Error connecting to database: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleReset() {
        txtUsername.clear();
        txtPassword.clear();
        lblStatus.setText("");
    }

    // Hover animations for buttons
    @FXML
    private void handleMouseEnter(MouseEvent event) {
        Button b = (Button) event.getSource();
        if (b == btnLogin) {
            b.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #64b5f6);"
                    + "-fx-text-fill: white; -fx-font-weight: bold; "
                    + "-fx-background-radius: 10; -fx-padding: 8 24;");
        } else if (b == btnReset) {
            b.setStyle("-fx-background-color: #cfd8dc; -fx-text-fill: #263238;"
                    + "-fx-background-radius: 10; -fx-padding: 8 24;");
        }
    }

    @FXML
    private void handleMouseExit(MouseEvent event) {
        Button b = (Button) event.getSource();
        if (b == btnLogin) {
            b.setStyle("-fx-background-color: linear-gradient(to right, #2196f3, #42a5f5);"
                    + "-fx-text-fill: white; -fx-font-weight: bold; "
                    + "-fx-background-radius: 10; -fx-padding: 8 24;");
        } else if (b == btnReset) {
            b.setStyle("-fx-background-color: #eceff1; -fx-text-fill: #263238;"
                    + "-fx-background-radius: 10; -fx-padding: 8 24;");
        }
    }
}
