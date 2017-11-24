package ifranjo.pfc.main;

/**
 *
 */

import ifranjo.pfc.controllers.InterfaceController;
import ifranjo.pfc.i18n.Languages;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * @author estibaliz.ifranjo
 */
public class Main extends Application {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Preferences userPreferences = Preferences.userRoot().node(
                "es.udc.pfc.hieroglyphs");

        // First time execution
        if (userPreferences.get("indexPath", null) == null) {
            userPreferences.put("indexPath", applicationDataFolder());
            System.out.println("First time execution. Index path is in \"" +
                    userPreferences.get("indexPath", "<error>") + "\"");
        }
        launch(args);
    }

    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/appInterface.fxml"));
            Preferences userPreferences = Preferences.userRoot().node(
                    "es.udc.pfc.hieroglyphs");
            String language = userPreferences.get("language", "English");
            loader.setResources(ResourceBundle.getBundle("Bundles.appInterface.appInterface", new Locale(Languages
                    .getLocale(language))));
            BorderPane page = loader.load();
            InterfaceController ic = loader.getController();
            ic.setPrincipalStage(primaryStage);

            Scene scene = new Scene(page);

            primaryStage.setMinHeight(600);
            primaryStage.setMinWidth(760);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Hieroglyphs");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String applicationDataFolder() {
        java.nio.file.Path path;
        String OS = (System.getProperty("os.name")).toLowerCase(Locale.ENGLISH);
        if (OS.contains("win")) {
            path = java.nio.file.Paths.get(System.getenv("AppData"), "Hieroglyphs");
        } else {
            path = java.nio.file.Paths.get(System.getProperty("user.home"), ".local", "share", "hieroglyphs");
        }
        if (!java.nio.file.Files.exists(path)) {
            try {
                java.nio.file.Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        return path.toString();
    }
}
