package es.udc.pfc.model.Query;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

/**
 * Created by estibaliz.ifranjo on 11/1/15.
 */
public abstract class SimpleQuery {

    protected Query query;
    protected String userQuery;
    protected Boolean isFuzzyQuery;
    protected Version luceneVersion = Version.LUCENE_4_9;

    public SimpleQuery(Boolean isFuzzyQuery, String userQuery) {
        this.isFuzzyQuery = isFuzzyQuery;
        this.userQuery = userQuery;
    }

    public Query getQuery() {
        return query;
    }

    public String getUserQuery() {
        return userQuery;
    }

    public Boolean getIsFuzzyQuery() {
        return isFuzzyQuery;
    }

    public Version getLuceneVersion() {
        return luceneVersion;
    }

    public abstract Query createQuery() throws ParseException;

    public abstract String escapeQuery();
}
