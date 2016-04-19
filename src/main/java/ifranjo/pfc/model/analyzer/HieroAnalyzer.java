/**
 *
 */
package ifranjo.pfc.model.analyzer;

import ifranjo.pfc.model.tokenizer.HieroTokenizer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * @author estibaliz.ifranjo
 */
public class HieroAnalyzer extends Analyzer {
    Version luceneVersion = Version.LUCENE_4_9;

    @Override
    protected TokenStreamComponents createComponents(String fieldName,
                                                     Reader reader) {

        Tokenizer hieroTokenizer = new HieroTokenizer(luceneVersion, reader);
        TokenStreamComponents tokenStreamComponents = new TokenStreamComponents(
                hieroTokenizer);
        return tokenStreamComponents;
    }

}
