/**
 *
 */
package es.udc.pfc.model.tokenizer;

import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * @author estibaliz.ifranjo
 */
public class HieroTokenizer extends CharTokenizer {

    public HieroTokenizer(Version matchVersion, Reader input) {
        super(matchVersion, input);
    }

    @Override
    protected boolean isTokenChar(int c) {
        int hyphen = (int) '-';
        return !(c == hyphen);
    }

}
