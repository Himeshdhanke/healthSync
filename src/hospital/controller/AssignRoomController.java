package hospital.controller;

import hospital.dao.AssignedDAO;
import hospital.dao.PatientDAO;
import hospital.dao.RoomDAO;
import hospital.model.Patient;
import hospital.model.Room;
import hospital.util.SceneUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class AssignRoomController {

    @FXML private TableView<Room> roomTable;
    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Room, Integer> colRoomId;
    @FXML private TableColumn<Room, String> colType;
    @FXML private TableColumn<Room, Integer> colCapacity;
    @FXML private TableColumn<Room, String> colAvailability;
    @FXML private TableColumn<Patient, Integer> colPatientId;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, Integer> colAge;
    @FXML private TableColumn<Patient, String> colGender;

    private final RoomDAO roomDAO = new RoomDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final AssignedDAO assignedDAO = new AssignedDAO();

    private final ObservableList<Room> roomList = FXCollections.observableArrayList();
    private final ObservableList<Patient> patientList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colRoomId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("rId"));
        colType.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("type"));
        colCapacity.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("capacity"));
        colAvailability.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("availability"));

        colPatientId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("pId"));
        colName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        colAge.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("age"));
        colGender.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("gender"));

        loadData();
    }

    private void loadData() {
        try {
            roomList.setAll(roomDAO.getAllRooms());
            patientList.setAll(patientDAO.getAllPatients());
            roomTable.setItems(roomList);
            patientTable.setItems(patientList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load data from the database.");
        }
    }

    @FXML
    private void handleAssign() {
        Room room = roomTable.getSelectionModel().getSelectedItem();
        Patient patient = patientTable.getSelectionModel().getSelectedItem();

        if (room == null || patient == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select both a room and a patient.");
            return;
        }

        if (!room.getAvailability().equalsIgnoreCase("Available")) {
            showAlert(Alert.AlertType.WARNING, "Room Occupied", "This room is already occupied.");
            return;
        }

        if (assignedDAO.assignRoom(patient.getPId(), room.getRId())) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Room assigned successfully.");
            loadData();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to assign room.");
        }
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) roomTable.getScene().getWindow();
        SceneUtil.switchScene(stage, "ReceptionistDashboard.fxml", "Receptionist Dashboard");
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
