package es.udc.pfc.model.ServicesFacade;

import es.udc.pfc.hieroglyphs.Cartouche;
import es.udc.pfc.hieroglyphs.Shade;
import javafx.collections.ObservableList;
import jsesh.editor.JMDCEditor;
import jsesh.mdc.MDCSyntaxError;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by estibaliz.ifranjo on 11/13/15.
 */
public interface Services {
    public ArrayList<Document> hieroSearch(String userQuery, Boolean isFuzzy) throws ParseException, IOException;

    public ArrayList<Document> latinSearch(String userQuery, Boolean isFuzzy) throws ParseException, IOException;

    public ArrayList<Document> multipleSearch(String userLatinQuery, String userHieroQuery, Boolean isLatinFuzzy,
                                              Boolean isHieroFuzzy) throws IOException, ParseException;

    public String groupVertically(JMDCEditor editor);

    public String groupHorizontally(JMDCEditor editor);

    public String applyShade(JMDCEditor editor, Shade s);

    public String applyCartouche(JMDCEditor editor, Cartouche c);

    public ObservableList<Cartouche> generateCartouchesFromSamples() throws MDCSyntaxError;

    public ObservableList<Shade> generateShadesFromSamples() throws MDCSyntaxError;

    public String getTextFromFile(String path) throws IOException;

    public String generateHTML(String documentTitle, String textFromDocumentSelected, ArrayList<String> queries,
                               ArrayList<String> fields, Boolean approximatedHieroSearch, Boolean
                                       approximatedLatinSearch) throws IOException, MDCSyntaxError;

}
