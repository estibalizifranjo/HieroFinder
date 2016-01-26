package es.udc.pfc.model.extractText;

import java.io.File;
import java.io.IOException;

/**
 * Created by estibaliz.ifranjo on 11/1/15.
 */
public interface TextExtractor {

    String extractPlainText(File file) throws IOException;
}
