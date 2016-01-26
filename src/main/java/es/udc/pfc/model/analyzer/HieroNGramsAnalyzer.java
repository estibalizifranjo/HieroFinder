package es.udc.pfc.model.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.util.Version;

import java.io.Reader;

public class HieroNGramsAnalyzer extends Analyzer {

    private int maxNgrams = 20;
    private int minNgrams = 2;
    private Version luceneVersion = Version.LUCENE_4_9;

    public HieroNGramsAnalyzer() {
    }

    public HieroNGramsAnalyzer(int minNgrams, int maxNgrams) {
        this.minNgrams = minNgrams;
        this.maxNgrams = maxNgrams;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName,
                                                     Reader reader) {
        Tokenizer tokensSeq = new NGramTokenizer(luceneVersion, reader,
                minNgrams, maxNgrams);

        return new TokenStreamComponents(tokensSeq);
    }

}
