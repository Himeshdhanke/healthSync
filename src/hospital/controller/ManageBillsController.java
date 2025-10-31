package hospital.controller;

import hospital.dao.BillDAO;
import hospital.model.Bill;
import hospital.util.AlertUtil;
import hospital.util.SceneUtil;
import hospital.util.Session;
import hospital.model.User;
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

public class ManageBillsController {

    @FXML private TableView<Bill> billTable;
    @FXML private TableColumn<Bill, Integer> colBillId;
    @FXML private TableColumn<Bill, String> colPatientName;
    @FXML private TableColumn<Bill, Double> colAmount;
    @FXML private TableColumn<Bill, String> colBillDate;
    @FXML private TableColumn<Bill, String> colStatus;

    @FXML private TextField txtPatientId;
    @FXML private TextField txtPatientName;
    @FXML private TextField txtAmount;
    @FXML private DatePicker txtBillDate;
    @FXML private TextField txtStatus;

    private final BillDAO billDAO = new BillDAO();

    @FXML
    public void initialize() {
        colBillId.setCellValueFactory(new PropertyValueFactory<>("billId"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colBillDate.setCellValueFactory(new PropertyValueFactory<>("billDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadBills();
    }

    @FXML
    private void loadBills() {
        try {
            List<Bill> list = billDAO.getAllBills();
            ObservableList<Bill> data = FXCollections.observableArrayList(list);
            billTable.setItems(data);
        } catch (SQLException e) {
            AlertUtil.showError("Error loading bills", e.getMessage());
        }
    }

    @FXML
    private void handleAdd() {
        try {
            int patientId = Integer.parseInt(txtPatientId.getText().trim());
            String patientName = txtPatientName.getText().trim();
            double amount = Double.parseDouble(txtAmount.getText().trim());
            LocalDate billDate = txtBillDate.getValue();
            String status = txtStatus.getText().trim();

            if (patientName.isEmpty() || status.isEmpty() || billDate == null) {
                AlertUtil.showWarning("Validation Error", "All fields are required!");
                return;
            }

            Bill bill = new Bill(0, patientId, patientName, amount, billDate.toString(), status);

            if (billDAO.addBill(bill)) {
                AlertUtil.showInfo("Success", "Bill added successfully!");
                loadBills();
                clearFields();
            } else {
                AlertUtil.showError("Error", "Failed to add bill!");
            }
        } catch (Exception e) {
            AlertUtil.showError("Exception", e.getMessage());
        }
    }

    @FXML
    private void handleEdit() {
        Bill selectedBill = billTable.getSelectionModel().getSelectedItem();
        if (selectedBill != null) {
            txtPatientId.setText(String.valueOf(selectedBill.getPatientId()));
            txtPatientName.setText(selectedBill.getPatientName());
            txtAmount.setText(String.valueOf(selectedBill.getAmount()));
            txtBillDate.setValue(LocalDate.parse(selectedBill.getBillDate()));
            txtStatus.setText(selectedBill.getStatus());
        } else {
            AlertUtil.showWarning("Selection Error", "Please select a bill first!");
        }
    }

    @FXML
    private void handleUpdate() {
        Bill selectedBill = billTable.getSelectionModel().getSelectedItem();
        if (selectedBill == null) {
            AlertUtil.showWarning("Selection Error", "Select a bill first!");
            return;
        }

        try {
            int patientId = Integer.parseInt(txtPatientId.getText().trim());
            String patientName = txtPatientName.getText().trim();
            double amount = Double.parseDouble(txtAmount.getText().trim());
            LocalDate billDate = txtBillDate.getValue();
            String status = txtStatus.getText().trim();

            if (patientName.isEmpty() || status.isEmpty() || billDate == null) {
                AlertUtil.showWarning("Validation Error", "All fields are required!");
                return;
            }

            selectedBill = new Bill(
                    selectedBill.getBillId(),
                    patientId,
                    patientName,
                    amount,
                    billDate.toString(),
                    status
            );

            if (billDAO.updateBill(selectedBill)) {
                AlertUtil.showInfo("Success", "Bill updated successfully!");
                loadBills();
                clearFields();
            } else {
                AlertUtil.showError("Error", "Failed to update bill!");
            }
        } catch (Exception e) {
            AlertUtil.showError("Exception", e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Bill selectedBill = billTable.getSelectionModel().getSelectedItem();
        if (selectedBill != null) {
            boolean confirmed = AlertUtil.showConfirmation("Confirm Delete", "Are you sure you want to delete this bill?");
            if (confirmed) {
                try {
                    if (billDAO.deleteBill(selectedBill.getBillId())) {
                        AlertUtil.showInfo("Deleted", "Bill deleted successfully!");
                        loadBills();
                        clearFields();
                    } else {
                        AlertUtil.showError("Error", "Failed to delete bill!");
                    }
                } catch (SQLException e) {
                    AlertUtil.showError("Exception", e.getMessage());
                }
            }
        } else {
            AlertUtil.showWarning("Selection Error", "Select a bill first!");
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
        txtPatientId.clear();
        txtPatientName.clear();
        txtAmount.clear();
        txtBillDate.setValue(null); // ✅ Correct way
        txtStatus.clear();
    }
}
