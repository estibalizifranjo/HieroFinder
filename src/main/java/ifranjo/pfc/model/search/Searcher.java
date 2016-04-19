/**
 *
 */
package ifranjo.pfc.model.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * @author estibaliz.ifranjo
 */
public class Searcher {

    private IndexReader indexReader;
    private IndexSearcher indexSearcher;
    private TopDocs topDocs = null;
    private int maxDocs;
    private Version luceneVersion = Version.LUCENE_4_9;
    private Analyzer analyzer;

    public Searcher(String indexPath) throws IOException {

        Directory dir = FSDirectory.open(new File(indexPath));
        this.indexReader = DirectoryReader.open(dir);

        this.indexSearcher = new IndexSearcher(indexReader);

        Preferences userPreferences = Preferences.userRoot().node(
                "es.udc.pfc.hieroglyphs");
        maxDocs = Integer.parseInt(userPreferences.get("maxDocs", "20"));
    }

    public IndexSearcher getIndexSearcher() {
        return indexSearcher;
    }

    public TopDocs searchInIndex(Query query)
            throws ParseException, IOException {
        // Searchs a query in the index and returns hits documents
        // with/without a filter
        this.topDocs = this.indexSearcher.search(query, null, this.maxDocs);
        return topDocs;
    }

    public void closeIndexReader() throws IOException {
        this.indexReader.close();
    }

}
