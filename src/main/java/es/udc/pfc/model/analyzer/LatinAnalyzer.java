package es.udc.pfc.model.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * Created by estibaliz.ifranjo on 10/23/15.
 */
public class LatinAnalyzer extends Analyzer {
    Version luceneVersion = Version.LUCENE_4_9;

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        StandardTokenizer tokenizer = new StandardTokenizer(luceneVersion, reader);
        TokenStream tokenfilter = new LowerCaseFilter(luceneVersion, tokenizer);
        TokenStreamComponents tokenStreamComponents = new TokenStreamComponents(tokenizer, new PorterStemFilter(tokenfilter));
        return tokenStreamComponents;
    }
}
