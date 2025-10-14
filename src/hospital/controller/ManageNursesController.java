package hospital.controller;

import hospital.dao.NurseDAO;
import hospital.model.Nurse;
import hospital.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

public class ManageNursesController {

    @FXML private TableView<Nurse> nurseTable;
    @FXML private TableColumn<Nurse, Integer> colId;
    @FXML private TableColumn<Nurse, String> colName;
    @FXML private TableColumn<Nurse, String> colSalary;
    @FXML private TableColumn<Nurse, String> colSex;
    @FXML private TableColumn<Nurse, String> colMob;
    @FXML private TableColumn<Nurse, String> colAddress;
    @FXML private TableColumn<Nurse, String> colState;
    @FXML private TableColumn<Nurse, String> colCity;
    @FXML private TableColumn<Nurse, String> colPin;

    @FXML private TextField txtName, txtSalary, txtSex, txtMobNo, txtAddress, txtState, txtCity, txtPin;
    @FXML private Button btnAdd, btnUpdate, btnDelete, btnClear;

    private final NurseDAO nurseDAO = new NurseDAO();
    private ObservableList<Nurse> nurseList;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().geteId()).asObject());
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colSalary.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getSalary())));
        colSex.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSex()));
        colMob.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMobNo()));
        colAddress.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAddress()));
        colState.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getState()));
        colCity.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCity()));
        colPin.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPinNo()));

        loadNurses();

        nurseTable.setOnMouseClicked(event -> {
            Nurse n = nurseTable.getSelectionModel().getSelectedItem();
            if (n != null) fillForm(n);
        });
    }

    private void loadNurses() {
        try {
            nurseList = FXCollections.observableArrayList(nurseDAO.getAllNurses());
            nurseTable.setItems(nurseList);
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", e.getMessage());
        }
    }

    private void fillForm(Nurse n) {
        txtName.setText(n.getName());
        txtSalary.setText(String.valueOf(n.getSalary()));
        txtSex.setText(n.getSex());
        txtMobNo.setText(n.getMobNo());
        txtAddress.setText(n.getAddress());
        txtState.setText(n.getState());
        txtCity.setText(n.getCity());
        txtPin.setText(n.getPinNo());
    }

    @FXML
    private void handleAdd() {
        try {
            Nurse n = new Nurse();
            n.setName(txtName.getText());
            n.setSalary(Double.parseDouble(txtSalary.getText()));
            n.setSex(txtSex.getText());
            n.setMobNo(txtMobNo.getText());
            n.setAddress(txtAddress.getText());
            n.setState(txtState.getText());
            n.setCity(txtCity.getText());
            n.setPinNo(txtPin.getText());

            if (nurseDAO.addNurse(n)) {
                AlertUtil.showInfo("Success", "Nurse added successfully!");
                loadNurses();
                clearFields();
            }
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        Nurse selected = nurseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("Selection Error", "Select a nurse first!");
            return;
        }

        try {
            selected.setName(txtName.getText());
            selected.setSalary(Double.parseDouble(txtSalary.getText()));
            selected.setSex(txtSex.getText());
            selected.setMobNo(txtMobNo.getText());
            selected.setAddress(txtAddress.getText());
            selected.setState(txtState.getText());
            selected.setCity(txtCity.getText());
            selected.setPinNo(txtPin.getText());

            if (nurseDAO.updateNurse(selected)) {
                AlertUtil.showInfo("Success", "Nurse updated successfully!");
                loadNurses();
                clearFields();
            } else {
                AlertUtil.showError("Update Failed", "Could not update nurse record!");
            }
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Nurse selected = nurseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("Selection Error", "Select a nurse to delete!");
            return;
        }

        if (AlertUtil.showConfirmation("Confirm Delete", "Are you sure you want to delete this nurse?")) {
            try {
                nurseDAO.deleteNurse(selected.geteId());
                AlertUtil.showInfo("Deleted", "Nurse deleted successfully!");
                loadNurses();
                clearFields();
            } catch (SQLException e) {
                AlertUtil.showError("Database Error", e.getMessage());
            }
        }
    }

    @FXML
    private void clearFields() {
        txtName.clear();
        txtSalary.clear();
        txtSex.clear();
        txtMobNo.clear();
        txtAddress.clear();
        txtState.clear();
        txtCity.clear();
        txtPin.clear();
    }
}
