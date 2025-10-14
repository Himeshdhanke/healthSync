package hospital;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println(getClass().getResource("/hospital/view/ManageDoctors.fxml")); // Check if file is found
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/view/ManageDoctors.fxml"));
            Parent root = loader.load();

            primaryStage.setTitle("HealthSync - Login");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading Login Page. Check your file path.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
