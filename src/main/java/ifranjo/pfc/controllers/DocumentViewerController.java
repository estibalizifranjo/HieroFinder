package ifranjo.pfc.controllers;

import ifranjo.pfc.model.ServicesFacade.ServicesImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import jsesh.mdc.MDCSyntaxError;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * @author estibaliz.ifranjo
 */
public class DocumentViewerController implements Initializable {

    @FXML
    private WebView htmlContent;

    private WebEngine engine;

    @FXML
    private ScrollPane windowScroll;

    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private MenuBar menuBar;
    @FXML
    private CheckMenuItem disableHieroHighlight;
    @FXML
    private CheckMenuItem disableLatinHighlight;

    private String textFromDocumentSelected;
    private ArrayList<String> queries;
    private ArrayList<String> fields;
    private String documentTitle;
    private Boolean approximatedHieroSearch;
    private Boolean approximatedLatinSearch;
    private ServicesImpl services = new ServicesImpl();

    public void setDocumentText(String documentPath, ArrayList<String> queries,
                                ArrayList<String> fields, Boolean approximatedHieroSearch, Boolean
                                        approximatedLatinSearch) throws IOException, MDCSyntaxError {
        this.fields = fields;
        this.queries = queries;
        this.approximatedHieroSearch = approximatedHieroSearch;
        this.approximatedLatinSearch = approximatedLatinSearch;
        textFromDocumentSelected = services.getTextFromFile(documentPath);
        loadHTMLContent();
    }

    public void loadHTMLContent() throws IOException, MDCSyntaxError {
        engine.loadContent(services.generateHTML(documentTitle, textFromDocumentSelected, queries, fields, approximatedHieroSearch, approximatedLatinSearch));
    }

    @FXML
    public void closeWindow(ActionEvent e) {
        ((Stage) menuBar.getScene().getWindow()).close();
    }

    @FXML
    public void disableHieroHighlight(ActionEvent e) throws IOException,
            MDCSyntaxError {
        Preferences userPreferences = Preferences.userRoot().node(
                "es.udc.pfc.hieroglyphs");
        userPreferences.putBoolean("disableHieroHighlight",
                disableHieroHighlight.isSelected());
        loadHTMLContent();
    }

    @FXML
    public void disableLatinHighlight(ActionEvent e) throws IOException,
            MDCSyntaxError {
        Preferences userPreferences = Preferences.userRoot().node(
                "es.udc.pfc.hieroglyphs");
        userPreferences.putBoolean("disableLatinHighlight",
                disableLatinHighlight.isSelected());
        loadHTMLContent();
    }

    public void initialize(URL location, ResourceBundle resources) {
        htmlContent.setVisible(true);
        engine = htmlContent.getEngine();
        Preferences userPreferences = Preferences.userRoot().node(
                "es.udc.pfc.hieroglyphs");

        disableHieroHighlight.setSelected(userPreferences.getBoolean(
                "disableHieroHighlight", false));

        disableLatinHighlight.setSelected(userPreferences.getBoolean(
                "disableLatinHighlight", false));
    }

}
