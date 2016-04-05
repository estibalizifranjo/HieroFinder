package es.udc.pfc.model.analyzer;

import es.udc.pfc.model.tokenizer.HieroTokenizer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.util.Version;

import java.io.Reader;

public class HieroNGramsAnalyzer extends Analyzer {

    private int maxNgrams = 4;
    private int minNgrams = 1;
    private Version luceneVersion = Version.LUCENE_4_9;

    public HieroNGramsAnalyzer() {
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName,
                                                     Reader reader) {

        Tokenizer hieroTokenizer = new HieroTokenizer(luceneVersion, reader);
        TokenFilter hieroNgramFilter = new ShingleFilter(hieroTokenizer,2,2);

        return new TokenStreamComponents(hieroTokenizer, hieroNgramFilter);
    }

}
