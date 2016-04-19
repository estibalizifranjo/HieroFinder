package ifranjo.pfc.model.userservices;

import ifranjo.pfc.hieroglyphs.Cartouche;
import ifranjo.pfc.hieroglyphs.CartoucheType;
import ifranjo.pfc.hieroglyphs.Shade;
import ifranjo.pfc.hieroglyphs.ShadeType;
import ifranjo.pfc.model.queries.HieroQuery;
import ifranjo.pfc.model.queries.LatinQuery;
import ifranjo.pfc.model.queries.MultipleQuery;
import ifranjo.pfc.model.extractText.TikaTextExtractor;
import ifranjo.pfc.model.htmlUtils.HTMLGenerator;
import ifranjo.pfc.model.index.Indexer;
import ifranjo.pfc.model.index.TikaIndexer;
import ifranjo.pfc.model.search.Searcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import jsesh.editor.JMDCEditor;
import jsesh.mdc.MDCSyntaxError;
import jsesh.mdcDisplayer.draw.MDCDrawingFacade;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

/**
 * Created by estibaliz.ifranjo on 11/13/15.
 */
public class ServicesImpl implements Services {
    // Get from the preferences the index path and the field.
    Preferences userPreferences = Preferences.userRoot().node("es.udc.pfc.hieroglyphs");
    String indexPath = userPreferences.get("indexPath", "");

    @Override
    public ArrayList<Document> hieroSearch(String userQuery, Boolean isFuzzy) throws ParseException, IOException {
        if (indexPath.isEmpty()) {
            // It there is no index, we can't do a search
            return new ArrayList<>();
        }
        HieroQuery hieroQuery = new HieroQuery(userQuery, isFuzzy);
        Query query = hieroQuery.createQuery();
        ArrayList<Document> docsList = new ArrayList<Document>();
        Searcher searcher = new Searcher(indexPath);
        TopDocs topDocs = searcher.searchInIndex(query);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.getIndexSearcher().doc(scoreDoc.doc);
            docsList.add(doc);
        }
        return docsList;
    }

    @Override
    public ArrayList<Document> latinSearch(String userQuery, Boolean isFuzzy) throws ParseException, IOException {
        if (indexPath.isEmpty()) {
            // It there is no index, we can't do a search
            return new ArrayList<>();
        }
        LatinQuery latinQuery = new LatinQuery(userQuery, isFuzzy);
        Query query = latinQuery.createQuery();
        ArrayList<Document> docsList = new ArrayList<Document>();
        Searcher searcher = new Searcher(indexPath);
        TopDocs topDocs = searcher.searchInIndex(query);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.getIndexSearcher().doc(scoreDoc.doc);
            docsList.add(doc);
        }
        return docsList;
    }

    @Override
    public ArrayList<Document> multipleSearch(String userLatinQuery, String userHieroQuery, Boolean isLatinFuzzy,
                                              Boolean isHieroFuzzy) throws IOException, ParseException {
        if (indexPath.isEmpty()) {
            // It there is no index, we can't do a search
            return new ArrayList<>();
        }
        MultipleQuery multipleQuery = new MultipleQuery(userLatinQuery, userHieroQuery, isLatinFuzzy, isHieroFuzzy);
        Query query = null;
        ArrayList<Document> docsList = new ArrayList<Document>();

        if (isHieroFuzzy || isLatinFuzzy) {
            query = multipleQuery.createFuzzyMultipleQuery();
        } else {
            query = multipleQuery.createMultipleQuery();
        }
        Searcher searcher = new Searcher(indexPath);
        TopDocs topDocs = searcher.searchInIndex(query);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.getIndexSearcher().doc(scoreDoc.doc);
            docsList.add(doc);
        }
        return docsList;
    }

    @Override
    public String groupVertically(JMDCEditor editor) {
        if (editor.hasSelection()) {
            editor.getWorkflow().groupVertically();
        }
        return editor.getMDCText();
    }

    @Override
    public String groupHorizontally(JMDCEditor editor) {
        if (editor.hasSelection()) {
            editor.getWorkflow().groupHorizontally();
        }
        return editor.getMDCText();
    }

    @Override
    public String applyShade(JMDCEditor editor, Shade s) {
        if (editor.hasSelection()) {
            String stype = s.getShadeType().getShadeCode();
            editor.getWorkflow().doShade(Integer.parseInt(stype));
        }
        return editor.getMDCText();

    }

    @Override
    public String applyCartouche(JMDCEditor editor, Cartouche c) {
        if (editor.hasSelection()) {
            char ctype = c.getCartoucheType().getType();
            int start = c.getCartoucheType().getStart();
            int end = c.getCartoucheType().getEnd();
            editor.getWorkflow().addCartouche(ctype, start, end);
        }

        return editor.getMDCText();
    }

    @Override
    public ObservableList<Cartouche> generateCartouchesFromSamples() throws MDCSyntaxError {
        ObservableList<Cartouche> cartouches = FXCollections.observableArrayList();
        for (int i = 0; i < Cartouche.cartouchesMDCSamples.size(); i++) {

            MDCDrawingFacade drawing = new MDCDrawingFacade();
            BufferedImage bufferedImage = drawing
                    .createImage(Cartouche.cartouchesMDCSamples.get(i));
            WritableImage image = new WritableImage(bufferedImage.getWidth(),
                    bufferedImage.getHeight());
            SwingFXUtils.toFXImage(bufferedImage, image);

            char type = CartoucheType.cartoucheTypes.get(i).getType();
            int start = CartoucheType.cartoucheTypes.get(i).getStart();
            int end = CartoucheType.cartoucheTypes.get(i).getEnd();
            CartoucheType cartoucheType = new CartoucheType(type, start, end);
            Cartouche c = new Cartouche(cartoucheType, image);
            cartouches.add(c);
        }

        return cartouches;
    }

    @Override
    public ObservableList<Shade> generateShadesFromSamples() throws MDCSyntaxError {
        ObservableList<Shade> shadesIcons = FXCollections.observableArrayList();

        for (int i = 0; i < Shade.shadeSamples.size(); i++) {
            MDCDrawingFacade drawing = new MDCDrawingFacade();
            BufferedImage bufferedImage = drawing.createImage(Shade.shadeSamples
                    .get(i));
            WritableImage image = new WritableImage(bufferedImage.getWidth(),
                    bufferedImage.getHeight());
            SwingFXUtils.toFXImage(bufferedImage, image);
            ShadeType shadeType = ShadeType.shadeTypes.get(i);
            Shade s = new Shade(shadeType, image);
            shadesIcons.add(s);
        }

        return shadesIcons;
    }

    @Override
    public String getTextFromFile(String path) throws IOException {
        File file = new File(path);
        return TikaTextExtractor.extractPlainText(file);
    }

    @Override
    public String generateHTML(String documentTitle, String textFromDocumentSelected, ArrayList<String> queries,
                               ArrayList<String> fields, Boolean approximatedHieroSearch, Boolean
                                       approximatedLatinSearch) throws IOException, MDCSyntaxError {
        HTMLGenerator exporter = new HTMLGenerator(documentTitle, textFromDocumentSelected, queries, fields,
                approximatedHieroSearch, approximatedLatinSearch);
        return exporter.generateHTML();
    }

    @Override
    public void indexDocuments() throws Exception {
        Indexer indexDocs = new TikaIndexer();
        // Indexing Documents
        String dataPath = userPreferences.get("dataPath", "");
        indexDocs.indexDocuments(new File(dataPath), indexDocs.getiWriter());
        indexDocs.getiWriter().close();
    }
}
