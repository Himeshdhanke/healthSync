package hospital.controller;

import hospital.dao.RoomDAO;
import hospital.model.Room;
import hospital.util.SceneUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class ManageRoomsController {

    @FXML private TextField txtType;
    @FXML private TextField txtCapacity;
    @FXML private ComboBox<String> cmbAvailability;
    @FXML private TableView<Room> roomTable;
    @FXML private TableColumn<Room, Integer> colRId;
    @FXML private TableColumn<Room, String> colType;
    @FXML private TableColumn<Room, Integer> colCapacity;
    @FXML private TableColumn<Room, String> colAvailability;

    private final RoomDAO roomDAO = new RoomDAO();
    private final ObservableList<Room> roomList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cmbAvailability.setItems(FXCollections.observableArrayList("Available", "Occupied"));

        colRId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("rId"));
        colType.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("type"));
        colCapacity.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("capacity"));
        colAvailability.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("availability"));

        loadRooms();
    }

    @FXML
    private void loadRooms() {
        List<Room> rooms = roomDAO.getAllRooms();
        roomList.setAll(rooms);
        roomTable.setItems(roomList);
    }

    @FXML
    private void handleAdd() {
        if (txtType.getText().isEmpty() || txtCapacity.getText().isEmpty() || cmbAvailability.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill all fields.");
            return;
        }

        Room room = new Room(0, txtType.getText(), Integer.parseInt(txtCapacity.getText()), cmbAvailability.getValue());
        if (roomDAO.addRoom(room)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Room added successfully.");
            loadRooms();
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add room.");
        }
    }

    @FXML
    private void handleUpdate() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a room to update.");
            return;
        }

        if (txtType.getText().isEmpty() || txtCapacity.getText().isEmpty() || cmbAvailability.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill all fields.");
            return;
        }

        selected.setType(txtType.getText());
        selected.setCapacity(Integer.parseInt(txtCapacity.getText()));
        selected.setAvailability(cmbAvailability.getValue());

        if (roomDAO.updateRoom(selected)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Room updated successfully.");
            loadRooms();
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update room.");
        }
    }

    @FXML
    private void handleDelete() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a room to delete.");
            return;
        }

        if (roomDAO.deleteRoom(selected.getRId())) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Room deleted successfully.");
            loadRooms();
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete room.");
        }
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtType.setText(selected.getType());
            txtCapacity.setText(String.valueOf(selected.getCapacity()));
            cmbAvailability.setValue(selected.getAvailability());
        }
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) roomTable.getScene().getWindow();
        SceneUtil.switchScene(stage, "AdminDashboard.fxml", "Admin Dashboard");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        txtType.clear();
        txtCapacity.clear();
        cmbAvailability.setValue(null);
    }
}
