package hospital.controller;

import hospital.util.DBConnection;
import hospital.model.User;
import hospital.util.SceneUtil;
import hospital.util.Session;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import hospital.model.Bill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;

public class ViewBillsController {

    @FXML private TableView<Bill> billTable;
    @FXML private TableColumn<Bill, Integer> colBillId;
    @FXML private TableColumn<Bill, Integer> colPatientId;
    @FXML private TableColumn<Bill, String> colPatientName;
    @FXML private TableColumn<Bill, String> colBillDate;
    @FXML private TableColumn<Bill, Double> colAmount;
    @FXML private TableColumn<Bill, String> colStatus;

    private final ObservableList<Bill> billList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTable();
        loadBills();
    }

    private void setupTable() {
        colBillId.setCellValueFactory(new PropertyValueFactory<>("billId"));
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colBillDate.setCellValueFactory(new PropertyValueFactory<>("billDate"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadBills() {
        billList.clear();
        String sql = "SELECT B_ID, P_ID, Patient_Name, Amount, Bill_Date, Status FROM Bills";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Bill bill = new Bill(
                        rs.getInt("B_ID"),
                        rs.getInt("P_ID"),
                        rs.getString("Patient_Name"),
                        rs.getDouble("Amount"),
                        rs.getDate("Bill_Date").toString(),
                        rs.getString("Status")
                );
                billList.add(bill);
            }

            billTable.setItems(billList);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading bills: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        User loggedInUser = Session.getLoggedInUser();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (loggedInUser == null) {
            // Not logged in → send back to Login
            SceneUtil.switchScene(stage, "Login.fxml", "Login");
            return;
        }

        String role = loggedInUser.getRole();

        if ("Administrator".equalsIgnoreCase(role)) {
            SceneUtil.switchScene(stage, "AdminDashboard.fxml", "Admin Dashboard");
        } else if ("Receptionist".equalsIgnoreCase(role)) {
            SceneUtil.switchScene(stage, "ReceptionistDashboard.fxml", "Receptionist Dashboard");
        } else {
            // Unknown role fallback
            SceneUtil.switchScene(stage, "Login.fxml", "Login");
        }
    }
}
