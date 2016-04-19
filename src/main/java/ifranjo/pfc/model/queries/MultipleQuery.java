package ifranjo.pfc.model.queries;

import ifranjo.pfc.model.analyzer.HieroLatinWrapperAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

/**
 * Created by estibaliz.ifranjo on 11/1/15.
 */
public class MultipleQuery {
    private LatinQuery latinQuery;
    private HieroQuery hieroQuery;

    public MultipleQuery(String latinUserQuery, String hieroUserQuery, Boolean isFuzzyLatinQuery, Boolean
            isFuzzyHieroQuery) {
        latinQuery = new LatinQuery(latinUserQuery, isFuzzyLatinQuery);
        hieroQuery = new HieroQuery(hieroUserQuery, isFuzzyHieroQuery);
    }

    public Query createMultipleQuery() throws ParseException {
        String[] fields = {latinQuery.getField(), hieroQuery.getField()};
        String[] queries = {latinQuery.escapeQuery(), hieroQuery.escapeQuery()};

        Analyzer analyzer = new HieroLatinWrapperAnalyzer().getWrapper();

        return MultiFieldQueryParser.parse(latinQuery.getLuceneVersion(), queries, fields, analyzer);
    }

    public Query createFuzzyMultipleQuery() throws ParseException {
        BooleanQuery booleanQuery = new BooleanQuery();

        booleanQuery.add(latinQuery.createQuery(), BooleanClause.Occur.MUST);
        booleanQuery.add(hieroQuery.createQuery(), BooleanClause.Occur.MUST);

        return booleanQuery;
    }

}
