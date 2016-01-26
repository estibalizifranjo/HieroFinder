/**
 *
 */
package es.udc.pfc.model.index;

import es.udc.pfc.model.analyzer.HieroLatinWrapperAnalyzer;
import es.udc.pfc.model.texts.Parser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * @author estibaliz.ifranjo
 */
public class Indexer {

    private String indexPath;
    private String dataPath;
    private IndexWriterConfig iwc;
    private IndexWriter iWriter;
    private Analyzer analyzer = new HieroLatinWrapperAnalyzer().getWrapper();

    public Indexer() throws IOException {
        Preferences userPreferences = Preferences.userRoot().node("es.udc.pfc.hieroglyphs");

        indexPath = userPreferences.get("indexPath", "");
        dataPath = userPreferences.get("dataPath", "");

        // Path where index is created.
        Directory dir = FSDirectory.open(new File(indexPath));

        this.iwc = new IndexWriterConfig(Version.LUCENE_4_9, this.analyzer);

        this.iwc.setOpenMode(OpenMode.CREATE);

        // creates a new index or opens an existing one, and adds, removes, or
        // updates documents in the index
        this.iWriter = new IndexWriter(dir, iwc);

        File docsPath = new File(dataPath);

        // Test if the path not exist or it can't be read
        if (!docsPath.exists() || !docsPath.canRead()) {
            System.err.println("Indexer.indexDocuments: Can't open the file");
        }
    }

    public IndexWriter getiWriter() {
        return iWriter;
    }

    public String getDir() {
        return indexPath;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void close() throws IOException {
        this.iWriter.close();
    }

    public int indexDocuments(File dataPath, IndexWriter iwriter)
            throws Exception {

        // Test if is a directory
        if (dataPath.canRead()) {
            if (dataPath.isDirectory()) {
                String[] files = dataPath.list();

                for (String file : files) {
                    indexDocuments(new File(dataPath, file), iwriter);
                }
            } else {// if it's a file
                Document doc = getDocument(dataPath);

                if (iwriter.getConfig().getOpenMode() == OpenMode.CREATE) {
                    this.iWriter.addDocument(doc);
                }
            }
        }
        return iwriter.maxDoc();
    }

    public Document getDocument(File file) throws Exception {
        Document doc = new Document();

        String[] parsedText = Parser.splitText(file);

        Field pathField = new StringField("path", file.getPath(),
                Field.Store.YES);
        Field fileName = new StringField("fileName", file.getName(),
                Field.Store.YES);
        Field latinText = new TextField("latinText", parsedText[0],
                Field.Store.NO);
        Field hieroText = new TextField("hieroText", parsedText[1],
                Field.Store.NO);
        Field fullContent = new TextField("fullContent", new FileReader(file));
        Field lastModified = new LongField("lastModified", file.lastModified(),
                Field.Store.NO);

        doc.add(pathField);
        doc.add(fileName);
        doc.add(hieroText);
        doc.add(latinText);
        doc.add(fullContent);
        doc.add(lastModified);

        return doc;

    }

}
