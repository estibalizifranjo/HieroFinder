/**
 *
 */
package es.udc.pfc.model.htmlUtils;

import jsesh.mdc.MDCParserModelGenerator;
import jsesh.mdc.MDCSyntaxError;
import jsesh.mdc.model.TopItemList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

/**
 * @author estibaliz.ifranjo
 */

public class HTMLGenerator {

    private boolean disableHieroHighlight;
    private boolean disableLatinHighlight;

    private String latinColor;
    private String hieroColor;

    private String originalText;
    private ArrayList<String> queriesToHighlight;
    private ArrayList<String> searchFields;
    private String fileName;
    private Boolean approximatedHieroSearch;
    private Boolean approximatedLatinSearch;


    public HTMLGenerator(String fileName, String originalText, ArrayList<String> queriesToHighlight,
                         ArrayList<String> searchFields, Boolean approximatedHieroSearch, Boolean
                                 approximatedLatinSearch) throws IOException, MDCSyntaxError {

        Preferences userPreferences = Preferences.userRoot().node(
                "es.udc.pfc.hieroglyphs");
        disableHieroHighlight = Boolean.valueOf(userPreferences.get(
                "disableHieroHighlight", "false"));
        disableLatinHighlight = Boolean.valueOf(userPreferences.get(
                "disableLatinHighlight", "false"));

        if ((userPreferences.get("latinColor", "#87CEEB").compareToIgnoreCase(
                "#87CEEB") == 0)) {
            latinColor = userPreferences.get("latinColor", "#87CEEB");
        } else
            latinColor = "#" + userPreferences.get("latinColor", "#87CEEB");
        if (userPreferences.get("hieroColor", "#FFFF00").compareToIgnoreCase(
                "#87CEEB") == 0) {
            hieroColor = "#" + userPreferences.get("hieroColor", "#FFFF00");
        } else
            hieroColor = "#" + userPreferences.get("hieroColor", "#FFFF00");
        this.fileName = fileName;
        this.originalText = originalText;
        this.queriesToHighlight = queriesToHighlight;
        this.searchFields = searchFields;
        this.approximatedHieroSearch = approximatedHieroSearch;
        this.approximatedLatinSearch = approximatedLatinSearch;
    }

    public String generateHTML() throws IOException, MDCSyntaxError {
        HTMLMemoryExporter htmlExporter = new HTMLMemoryExporter(fileName, queriesToHighlight, searchFields,
                disableHieroHighlight, disableLatinHighlight, approximatedHieroSearch, approximatedLatinSearch);
        htmlExporter.setHieroColor(hieroColor);
        htmlExporter.setLatinColor(latinColor);

        MDCParserModelGenerator parser = new MDCParserModelGenerator();
        TopItemList topItemList = parser.parse(originalText);

        return htmlExporter.exportModel(topItemList).toString();
    }

}
