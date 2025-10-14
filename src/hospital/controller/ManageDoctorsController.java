package hospital.controller;

import hospital.dao.DoctorDAO;
import hospital.model.Doctor;
import hospital.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

public class ManageDoctorsController {

    @FXML private TableView<Doctor> doctorTable;
    @FXML private TableColumn<Doctor, Integer> colId;
    @FXML private TableColumn<Doctor, String> colName;
    @FXML private TableColumn<Doctor, Double> colSalary;
    @FXML private TableColumn<Doctor, String> colSex;
    @FXML private TableColumn<Doctor, String> colMob;
    @FXML private TableColumn<Doctor, String> colAddress;
    @FXML private TableColumn<Doctor, String> colState;
    @FXML private TableColumn<Doctor, String> colCity;
    @FXML private TableColumn<Doctor, String> colPin;
    @FXML private TableColumn<Doctor, String> colDept;
    @FXML private TableColumn<Doctor, String> colQualification;

    @FXML private TextField txtName, txtSalary, txtSex, txtMobNo, txtAddress, txtState, txtCity, txtPin, txtDept, txtQualification;
    @FXML private Button btnAdd, btnUpdate, btnDelete, btnClear;

    private final DoctorDAO doctorDAO = new DoctorDAO();
    private ObservableList<Doctor> doctorList;

    @FXML
    public void initialize() {
        // Map TableColumns to Doctor properties
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().geteId()).asObject());
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colSalary.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getSalary()).asObject());
        colSex.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSex()));
        colMob.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMobNo()));
        colAddress.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAddress()));
        colState.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getState()));
        colCity.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCity()));
        colPin.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPinNo()));
        colDept.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDept()));
        colQualification.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getQualification()));

        loadDoctors();

        doctorTable.setOnMouseClicked(event -> {
            Doctor d = doctorTable.getSelectionModel().getSelectedItem();
            if (d != null) fillForm(d);
        });
    }

    private void loadDoctors() {
        try {
            doctorList = FXCollections.observableArrayList(doctorDAO.getAllDoctors());
            doctorTable.setItems(doctorList);
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", e.getMessage());
        }
    }

    private void fillForm(Doctor d) {
        txtName.setText(d.getName());
        txtSalary.setText(String.valueOf(d.getSalary()));
        txtSex.setText(d.getSex());
        txtMobNo.setText(d.getMobNo());
        txtAddress.setText(d.getAddress());
        txtState.setText(d.getState());
        txtCity.setText(d.getCity());
        txtPin.setText(d.getPinNo());
        txtDept.setText(d.getDept());
        txtQualification.setText(d.getQualification());
    }

    @FXML
    private void handleAdd() {
        try {
            Doctor d = new Doctor();
            d.setName(txtName.getText());
            d.setSalary(Double.parseDouble(txtSalary.getText()));
            d.setSex(txtSex.getText());
            d.setMobNo(txtMobNo.getText());
            d.setAddress(txtAddress.getText());
            d.setState(txtState.getText());
            d.setCity(txtCity.getText());
            d.setPinNo(txtPin.getText());
            d.setDept(txtDept.getText());
            d.setQualification(txtQualification.getText());

            if (doctorDAO.addDoctor(d)) {
                AlertUtil.showInfo("Success", "Doctor added successfully!");
                loadDoctors();
                clearFields();
            }
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("Selection Error", "Select a doctor first to update!");
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
            selected.setDept(txtDept.getText());
            selected.setQualification(txtQualification.getText());

            if (doctorDAO.updateDoctor(selected)) {
                AlertUtil.showInfo("Success", "Doctor updated successfully!");
                loadDoctors();
                clearFields();
            } else {
                AlertUtil.showError("Update Failed", "Could not update doctor record!");
            }
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("Selection Error", "Select a doctor to delete!");
            return;
        }

        if (AlertUtil.showConfirmation("Confirm Delete", "Are you sure you want to delete this doctor?")) {
            try {
                doctorDAO.deleteDoctor(selected.geteId());
                AlertUtil.showInfo("Deleted", "Doctor deleted successfully!");
                loadDoctors();
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
        txtDept.clear();
        txtQualification.clear();
    }
}
