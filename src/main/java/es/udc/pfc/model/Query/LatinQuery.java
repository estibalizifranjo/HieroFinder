package es.udc.pfc.model.Query;

import es.udc.pfc.model.analyzer.LatinAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

/**
 * Created by estibaliz.ifranjo on 11/1/15.
 */
public class LatinQuery extends SimpleQuery {

    private String field = "latinText";

    public LatinQuery(String userQuery, Boolean isFuzzyQuery) {
        super(isFuzzyQuery, userQuery);
    }

    public String getField() {
        return field;
    }

    @Override
    public Query createQuery() throws ParseException {
        QueryParser parser = null;

        if (!userQuery.isEmpty()) {
            parser = new QueryParser(luceneVersion, field,
                    new LatinAnalyzer());
            if (this.isFuzzyQuery) {
                userQuery = userQuery.concat("*");
            }
        }

        return parser.parse(userQuery);
    }

    @Override
    public String escapeQuery() {
        return userQuery;
    }

}
