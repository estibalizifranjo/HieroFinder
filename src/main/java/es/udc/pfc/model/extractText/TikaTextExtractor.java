/**
 *
 */
package es.udc.pfc.model.extractText;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author estibaliz.ifranjo
 */
public class TikaTextExtractor {

    public static String extractPlainText(File file) throws IOException {

        ContentHandler contentHandler = new BodyContentHandler();
        return parseDoc(file, contentHandler);

    }

    private static String parseDoc(File file, ContentHandler contentHandler)
            throws IOException {
        // Le pasamos el nombre del archivo para que pueda emplearlo para
        // establecer el parser adecuado.
        Metadata metadata = new Metadata();
        metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());

        InputStream isfile = new FileInputStream(file);
        // Autodetect the parser
        Parser parser = new AutoDetectParser();
        ParseContext context = new ParseContext();
        context.set(Parser.class, parser);
        try {
            // Parses a document stream into a sequence of XHTML SAX events.
            // Fills in related document metadata in the given metadata object.
            parser.parse(isfile, contentHandler, metadata, context);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TikaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            isfile.close();
        }

        return contentHandler.toString();
    }

}
