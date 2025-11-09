package cincuentazo.view;

import cincuentazo.controller.CincuentazoWelcomeController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * View class for the welcome/menu screen.
 * First screen shown when the application starts.
 */
public class WelcomeStage extends Stage {

    private static WelcomeStage instance;
    private CincuentazoWelcomeController controller;

    /**
     * Private constructor for singleton pattern.
     */
    private WelcomeStage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cincuentazo/cincuentazo-menu-view.fxml"));
            Parent root = loader.load();

            controller = loader.getController();
            controller.setStage(this);

            Scene scene = new Scene(root);
            this.setScene(scene);
            this.setTitle("Cincuentazo - Card Game");
            this.setResizable(false);

            // Set application icon
            try {
                Image icon = new Image(getClass().getResourceAsStream("/cincuentazo/images/icon.png"));
                this.getIcons().add(icon);
            } catch (Exception e) {
                System.out.println("Icon not found, continuing without it.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load welcome view", e);
        }
    }

    /**
     * Gets the singleton instance of WelcomeStage.
     * @return the WelcomeStage instance
     */
    public static WelcomeStage getInstance() {
        if (instance == null) {
            instance = new WelcomeStage();
        }
        return instance;
    }

    /**
     * Deletes the current instance.
     */
    public static void deleteInstance() {
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }

    /**
     * Gets the controller for this stage.
     * @return the welcome controller
     */
    public CincuentazoWelcomeController getController() {
        return controller;
    }
}