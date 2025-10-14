package hospital.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneUtil {

    public static void switchScene(Stage stage, String fxmlName, String title) {
        try {
            // Ensure fxmlName has no leading slash
            if (fxmlName.startsWith("/")) {
                fxmlName = fxmlName.substring(1);
            }

            FXMLLoader loader = new FXMLLoader(SceneUtil.class.getResource("/hospital/view/" + fxmlName));
            if (loader.getLocation() == null) {
                throw new RuntimeException("FXML file not found: /hospital/view/" + fxmlName);
            }

            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error loading " + fxmlName);
        }
    }
}
