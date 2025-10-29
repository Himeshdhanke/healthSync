package hospital.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class SceneUtil {

    /**
     * Switches the entire scene (used for login/logout or major role switches)
     */
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




    public static Stage getCurrentStageFromEvent() {
        for (Window window : Stage.getWindows()) {
            if (window.isShowing() && window instanceof Stage) {
                return (Stage) window;
            }
        }
        throw new IllegalStateException("No active stage found.");
    }



    /**
     * Loads an FXML file into an existing Pane (for dashboard content areas)
     */
    public static void loadFXMLInto(Pane container, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneUtil.class.getResource(fxmlPath));
            Parent content = loader.load();
            container.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Error loading into container: " + fxmlPath);
        }
    }

    /**
     * Returns the current Stage from any Node (used for logout, etc.)
     */
    public static Stage getCurrentStage(Node node) {
        return (Stage) node.getScene().getWindow();
    }
}
