package es.udc.pfc.model.Query;

import es.udc.pfc.model.analyzer.HieroAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

/**
 * Created by estibaliz.ifranjo on 11/1/15.
 */
public class HieroQuery extends SimpleQuery {
    private String field = "hieroText";

    public HieroQuery(String userQuery, Boolean isFuzzyQuery) {
        super(isFuzzyQuery, userQuery);
    }

    public String getField() {
        return field;
    }

    @Override
    public Query createQuery() throws ParseException {
        QueryParser parser = null;
        if (!userQuery.isEmpty()) {
            userQuery = escapeQuery();
            parser = new QueryParser(luceneVersion, field,
                    new HieroAnalyzer());
            if (this.isFuzzyQuery) {
                userQuery = "*" + userQuery;
                userQuery = userQuery.concat("*");
                parser.setLowercaseExpandedTerms(false);
                parser.setAllowLeadingWildcard(true);
            }
        }

        return parser.parse(userQuery);
    }

    @Override
    public String escapeQuery() {
        return MultiFieldQueryParser.escape(userQuery);
    }
}
