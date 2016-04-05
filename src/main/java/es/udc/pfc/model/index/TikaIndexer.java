/**
 *
 */
package es.udc.pfc.model.index;

import es.udc.pfc.model.extractText.TikaTextExtractor;
import es.udc.pfc.model.texts.Parser;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.io.File;
import java.io.IOException;

/**
 * @author estibaliz.ifranjo
 */
public class TikaIndexer extends Indexer {

    public TikaIndexer() throws IOException {
        super();
    }

    public Document getDocument(File file) throws Exception {

        String extractedText = TikaTextExtractor.extractPlainText(file);
        Document doc = new Document();

        String[] parsedText = Parser.splitText(extractedText);

        Field pathField = new StringField("pathField", file.getPath(),
                Field.Store.YES);
        Field fileName = new StringField("fileName", file.getName(),
                Field.Store.YES);
        // Field contents = new TextField("contents", new BufferedReader(new
        // InputStreamReader(isfile, StandardCharsets.UTF_8)));
        Field latinText = new TextField("latinText", parsedText[0],
                Field.Store.YES);
        Field hieroText = new TextField("hieroText", parsedText[1],
                Field.Store.YES);
        Field fullContent = new TextField("fullContent", extractedText,
                Field.Store.YES);

        doc.add(pathField);
        doc.add(fileName);
        doc.add(latinText);
        doc.add(hieroText);
        doc.add(fullContent);

        return doc;

    }
}
