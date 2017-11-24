/**
 *
 */
package ifranjo.pfc.controllers;

import ifranjo.pfc.model.userservices.ServicesImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * @author estibaliz.ifranjo
 */
public class PreferencesController implements Initializable {

    @FXML
    private ColorPicker latinColorPicker;
    @FXML
    private ColorPicker hieroColorPicker;
    @FXML
    private CheckBox disableHiero;
    @FXML
    private CheckBox disableLatin;
    @FXML
    private TextField maxDocsTextField;
    @FXML
    private TextField dataPath;
    @FXML
    private Button pathBrowser;
    @FXML
    private TextField indexPath;
    @FXML
    private Button cancelButton;
    @FXML
    private Button acceptButton;
    @FXML
    private ComboBox languageCombobox;


    private Preferences userPreferences = Preferences.userRoot().node(
            "es.udc.pfc.hieroglyphs");
    DirectoryChooser dataDirectoryChooser = new DirectoryChooser();
    ServicesImpl services = new ServicesImpl();

    @FXML
    public void cancelButtonAction() throws IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void acceptButtonAction() throws IOException {
        userPreferences.put("latinColor", Integer.toHexString(latinColorPicker.getValue().hashCode()).substring(0, 6));
        userPreferences.put("hieroColor", Integer.toHexString(hieroColorPicker.getValue().hashCode()).substring(0, 6));
        userPreferences.put("maxDocs", maxDocsTextField.getText());
        userPreferences.putBoolean("disableHieroHighlight", disableHiero.isSelected());
        userPreferences.putBoolean("disableLatinHighlight", disableLatin.isSelected());
        userPreferences.put("language", languageCombobox.getSelectionModel().getSelectedItem().toString());
        userPreferences.put("dataPath", dataPath.getText());
        userPreferences.put("indexPath", indexPath.getText());

        Stage stage = (Stage) acceptButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void chooseDataDirectory() throws Exception {
        if (dataPath.getText().isEmpty()) {
            dataDirectoryChooser.setInitialDirectory(new File(System
                    .getProperty("user.home")));
        } else {
            dataDirectoryChooser.setInitialDirectory(new File(userPreferences
                    .get("dataPath", dataPath.getText())));
        }
        File dataDirectory = dataDirectoryChooser
                .showDialog(pathBrowser.getScene().getWindow());
        if (dataDirectory != null) {
            dataPath.setText(dataDirectory.getAbsolutePath());
            services.indexDocuments();
        }
    }

    @FXML
    public void chooseIndexDirectory() {
        if (indexPath.getText().isEmpty()) {
            dataDirectoryChooser.setInitialDirectory(new File(System
                    .getProperty("user.home")));
        } else {
            dataDirectoryChooser.setInitialDirectory(new File(userPreferences
                    .get("indexPath", indexPath.getText())));
        }
        File dataDirectory = dataDirectoryChooser
                .showDialog(pathBrowser.getScene().getWindow());
        if (dataDirectory != null) {
            indexPath.setText(dataDirectory.getAbsolutePath());

        }
    }

    @FXML
    public void resetToDefaultGeneralValues() {
        maxDocsTextField.setText("20");
        languageCombobox.setValue("English");
    }

    @FXML
    public void resetToDefaultHighlightValues() {
        hieroColorPicker.setValue(Color.web("#FFFF00"));
        latinColorPicker.setValue(Color.web("#87CEEB"));
        disableHiero.setSelected(false);
        disableLatin.setSelected(false);
    }

    public void populateLanguageCombobox() {
        ObservableList<String> languages = FXCollections.observableArrayList("Español", "French", "English", "Galego");
        languages = languages.sorted(new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        languageCombobox.setItems(languages);
    }

    public void initialize(URL location, ResourceBundle resources) {
        hieroColorPicker.setValue(Color.web(userPreferences.get("hieroColor", "#FFFF00")));
        latinColorPicker.setValue(Color.web(userPreferences.get("latinColor", "#87CEEB")));
        maxDocsTextField.setText(userPreferences.get("maxDocs", "20"));
        disableHiero.setIndeterminate(false);
        disableLatin.setIndeterminate(false);
        disableHiero.setSelected(userPreferences.getBoolean("disableHieroHighlight", false));
        disableLatin.setSelected(userPreferences.getBoolean("disableLatinHighlight", false));
        dataPath.setText(userPreferences.get("dataPath", ""));
        indexPath.setText(userPreferences.get("indexPath", ""));

        populateLanguageCombobox();
        String language = userPreferences.get("language", "English");
        languageCombobox.setValue(language);
    }

}
