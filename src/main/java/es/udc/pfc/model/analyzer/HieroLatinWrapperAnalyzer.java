package es.udc.pfc.model.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.util.Version;

import java.util.HashMap;
import java.util.Map;

public class HieroLatinWrapperAnalyzer {

    private Map<String, Analyzer> analyzerPerField = new HashMap<String, Analyzer>();
    private Version luceneVersion = Version.LUCENE_4_9;
    private PerFieldAnalyzerWrapper wrapper;

    public HieroLatinWrapperAnalyzer() {
        analyzerPerField.put("hieroText", new HieroAnalyzer());
        this.wrapper = new PerFieldAnalyzerWrapper(new LatinAnalyzer(), this.analyzerPerField);
    }

    public PerFieldAnalyzerWrapper getWrapper() {
        return wrapper;
    }

}
