package es.udc.pfc.Controllers;

import es.udc.pfc.hieroglyphs.Cartouche;
import es.udc.pfc.hieroglyphs.Shade;
import es.udc.pfc.iuUtils.CustomCellsCartouche;
import es.udc.pfc.iuUtils.CustomCellsShade;
import es.udc.pfc.main.Main;
import es.udc.pfc.model.ServicesFacade.ServicesImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import jsesh.editor.JMDCEditor;
import jsesh.mdc.MDCSyntaxError;
import jsesh.swing.signPalette.HieroglyphPaletteListener;
import jsesh.swing.signPalette.PalettePresenter;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class InterfaceController implements Initializable {

    @FXML
    private Button searchButton;
    @FXML
    private TextField searchQuery;
    @FXML
    private TextField hieroSearchQuery;
    @FXML
    private ListView<String> results;
    @FXML
    private Label notifyLabel;
    @FXML
    private GridPane grid;
    @FXML
    private Button horizontal;
    @FXML
    private Button vertical;
    @FXML
    private ComboBox<Cartouche> cartoucheComboBox;
    @FXML
    private ComboBox<Shade> shadesComboBox;
    @FXML
    private MenuItem preferences;
    @FXML
    private MenuItem hieroSignPaletteShowMenu;
    @FXML
    private CheckBox approximatedLatinSearch;
    @FXML
    private CheckBox approximatedHieroSearch;
    private ResourceBundle bundle;

    private Window principalStage;
    private Stage hieroPaletteWindow;
    private DocumentViewerController docViewerController;
    ArrayList<String> queries = new ArrayList<String>();
    ArrayList<String> fields = new ArrayList<String>();
    SwingNode swingNodeEditor = new SwingNode();

    ArrayList<Document> documents = new ArrayList<Document>();
    ServicesImpl services = new ServicesImpl();

    public void setPrincipalStage(Window principalStage) {
        this.principalStage = principalStage;
    }

    public void Search(ActionEvent e) throws IOException, ParseException {
        queries.clear();
        fields.clear();

        if (searchQuery.getText().isEmpty() && hieroSearchQuery.getText().isEmpty()) {
            info(0);
            return;
        }
        if (searchQuery.getText().isEmpty()) {
            documents = services.hieroSearch(hieroSearchQuery.getText(), approximatedHieroSearch.isSelected());
            queries.add(hieroSearchQuery.getText());
            fields.add("hieroText");
        } else if (hieroSearchQuery.getText().isEmpty()) {
            documents = services.latinSearch(searchQuery.getText(), approximatedLatinSearch.isSelected());
            queries.add(searchQuery.getText());
            fields.add("latinText");
        } else {
            documents = services.multipleSearch(searchQuery.getText(), hieroSearchQuery.getText(),
                    approximatedLatinSearch.isSelected(), approximatedHieroSearch.isSelected());
            queries.add(searchQuery.getText());
            fields.add("latinText");
            queries.add(hieroSearchQuery.getText());
            fields.add("hieroText");
        }
        info(documents.size());
        ShowListResults(documents);

    }

    public void info(int numberResults) {
        if (numberResults == 0) {
            notifyLabel.setText("No results found");
        } else {
            notifyLabel.setText("Results");
        }

    }

    public void ShowListResults(ArrayList<Document> documents) throws IOException {
        String field = "fileName";
        ObservableList<String> data = FXCollections.observableArrayList();
        results.getItems().clear();

        for (Document doc : documents) {
            data.add(doc.get(field));
        }
        results.setItems(data);
        results.setVisible(true);
    }

    public void itemSelected(MouseEvent e) throws IOException, MDCSyntaxError {
        int focusedItem;

        if (e.getClickCount() == 2) {
            focusedItem = results.getFocusModel().getFocusedIndex();
            openNewWindow(documents.get(focusedItem));
        }
    }

    public void openNewWindow(Document doc) throws IOException, MDCSyntaxError {

        String docTitle = doc.get("fileName");
        String docPath = doc.get("pathField");

        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/DocumentViewer.fxml"));
        loader.setResources(ResourceBundle.getBundle("Bundles.documentViewer.documentViewer", Locale.ENGLISH));

        BorderPane layout = loader.load();

        Stage documentViewer = new Stage();
        documentViewer.setMinHeight(600);
        documentViewer.setMinWidth(600);
        Scene scene = new Scene(layout);
        documentViewer.setScene(scene);

        documentViewer.setTitle(docTitle);
        documentViewer.initOwner(principalStage);
        docViewerController = loader.getController();

        docViewerController.setDocumentText(docPath, queries, fields, approximatedHieroSearch.isSelected(),
                approximatedLatinSearch.isSelected());
        documentViewer.show();

    }

    public void openSignPalette(ActionEvent e) {
        if (hieroPaletteWindow == null) {
            StackPane page = new StackPane();
            hieroPaletteWindow = new Stage();
            Scene scene = new Scene(page);
            hieroPaletteWindow.initOwner(principalStage);
            SwingNode swingNode = new SwingNode();
            createAndSetSwingPalette(swingNode);

            page.getChildren().add(swingNode);
            hieroPaletteWindow.initOwner(principalStage);
            hieroPaletteWindow.setScene(scene);
            hieroPaletteWindow.setTitle("Hieroglyph Palette");
            hieroPaletteWindow.setY(100);
            hieroPaletteWindow.setX(100);
            hieroPaletteWindow.setMinHeight(700);
            hieroPaletteWindow.setMinWidth(600);
            hieroPaletteWindow
                    .setOnCloseRequest(new EventHandler<WindowEvent>() {
                        public void handle(WindowEvent event) {
                            hieroSignPaletteShowMenu
                                    .setText("Show Hieroglyph Sign Palette");
                        }
                    });
            hieroPaletteWindow.show();
            hieroSignPaletteShowMenu.setText("Hide Hieroglyph Sign Palette");
        } else {
            hieroSignPaletteShowMenu.setText("Show Hieroglyph Sign Palette");
            hieroPaletteWindow.close();
            hieroPaletteWindow = null;
        }

    }

    public void groupVertically(ActionEvent e) {
        JMDCEditor editor = (JMDCEditor) swingNodeEditor.getContent();
        hieroSearchQuery.setText(services.groupVertically(editor));
    }

    public void groupHorizontally(ActionEvent e) {
        JMDCEditor editor = (JMDCEditor) swingNodeEditor.getContent();
        hieroSearchQuery.setText(services.groupHorizontally(editor));
    }

    public void renderingHieroSearchQuery() {
        JMDCEditor editor = (JMDCEditor) swingNodeEditor.getContent();
        editor.setMDCText(hieroSearchQuery.getText());
    }

    public void applyCartouche() {
        JMDCEditor editor = (JMDCEditor) swingNodeEditor.getContent();
        Cartouche c = cartoucheComboBox.getSelectionModel()
                .getSelectedItem();
        cartoucheComboBox.setButtonCell(new CustomCellsCartouche());
        hieroSearchQuery.setText(services.applyCartouche(editor, c));
    }

    public void applyShade() {
        JMDCEditor editor = (JMDCEditor) swingNodeEditor.getContent();
        Shade s = shadesComboBox.getSelectionModel().getSelectedItem();
        hieroSearchQuery.setText(services.applyShade(editor, s));
    }

    public void poblateCartoucheComboBox() throws MDCSyntaxError {
        ObservableList<Cartouche> cartouchesIcons = services.generateCartouchesFromSamples();
        cartoucheComboBox.setItems(cartouchesIcons);
        cartoucheComboBox
                .setCellFactory(new Callback<ListView<Cartouche>, ListCell<Cartouche>>() {

                    public ListCell<Cartouche> call(ListView<Cartouche> param) {
                        return new CustomCellsCartouche();
                    }
                });
    }

    public void poblateShadeComboBox() throws MDCSyntaxError {
        ObservableList<Shade> shadesIcons = services.generateShadesFromSamples();
        shadesComboBox.setItems(shadesIcons);
        shadesComboBox
                .setCellFactory(new Callback<ListView<Shade>, ListCell<Shade>>() {

                    public ListCell<Shade> call(ListView<Shade> param) {
                        return new CustomCellsShade();
                    }
                });
    }

    public void closeApp(ActionEvent e) {
        System.exit(0);
    }

    public void openAbout(ActionEvent e) {

    }

    private void createAndSetSwingPalette(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PalettePresenter palettePresenter = new PalettePresenter();
                MyHieroglyphicPaletteListener hieroglyphPaletteListener = new MyHieroglyphicPaletteListener();
                palettePresenter
                        .setHieroglyphPaletteListener(hieroglyphPaletteListener);
                swingNode.setContent(palettePresenter.getSimplePalette());
            }
        });
    }

    private void createAndSetSwingEditor(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JMDCEditor editor = new JMDCEditor();
                editor.setEditable(true);
                editor.setVisible(true);
                swingNode.setContent(editor);

            }
        });
    }

    private class MyHieroglyphicPaletteListener implements
            HieroglyphPaletteListener {

        public void signSelected(final String arg0) {
            final JMDCEditor edit = (JMDCEditor) swingNodeEditor.getContent();
            edit.getWorkflow().addSign(arg0);
            // TODO: Mirar si esto está bien.
            Platform.runLater(new Runnable() {
                public void run() {
                    hieroSearchQuery.setText(edit.getWorkflow().getMDCCode());
                }
            });
        }
    }

    public void openPreferencesWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/Preferences.fxml"));
        loader.setResources(ResourceBundle.getBundle("Bundles.preferences.preferences", Locale.ENGLISH));
        BorderPane layout = loader.load();

        Stage preferencesWindow = new Stage();
        preferencesWindow.setMinHeight(600);
        preferencesWindow.setMinWidth(600);

        Scene scene = new Scene(layout);
        preferencesWindow.setScene(scene);

        preferencesWindow.setTitle("Preferences");
        preferencesWindow.initOwner(principalStage);

        loader.getController();
        preferencesWindow.show();
    }

    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        createAndSetSwingEditor(swingNodeEditor);
        grid.add(swingNodeEditor, 0, 1);
        try {
            poblateCartoucheComboBox();
            poblateShadeComboBox();
            approximatedHieroSearch.setIndeterminate(false);
            approximatedLatinSearch.setIndeterminate(false);
        } catch (MDCSyntaxError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
