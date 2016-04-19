/**
 *
 */
package ifranjo.pfc.model.texts;

import ifranjo.pfc.model.utils.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author estibaliz.ifranjo
 */

public class Parser {

    public static String[] splitText(File file) throws IOException {
        // Extraemos el texto del archivo para poder tokenizarlo.
        String originalText = new String(Files.readAllBytes(Paths.get(file
                .getPath())));
        String text = removeHeader(originalText);

        StringBuilder latinText = new StringBuilder();
        ArrayList<Pair> positions = new ArrayList<Pair>();
        int lastEnd = 0;

        Pattern pattern = Pattern.compile("\\+[bli]([^\\+]*)\\+s[\\-!]*");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            latinText.append(matcher.group(1));
            positions.add(new Pair(lastEnd, matcher.start()));
            lastEnd = matcher.end() + 1;
        }
        positions.add(new Pair(lastEnd, text.length()));

        return new String[]{latinText.toString(),
                buildText(positions, text)};
    }

    public static String[] splitText(String originalText) throws IOException {
        StringBuilder latinText = new StringBuilder();
        ArrayList<Pair> positions = new ArrayList<Pair>();
        int lastEnd = 0;

        String text = removeHeader(originalText);
        Pattern pattern = Pattern.compile("\\+[bli]([^\\+]*)\\+s[\\-!]*");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            latinText.append(matcher.group(1));
            positions.add(new Pair(lastEnd, matcher.start()));
            lastEnd = matcher.end() + 1;
        }
        positions.add(new Pair(lastEnd, text.length()));

        String[] bothText = {latinText.toString(),
                buildText(positions, text).toString()};
        return bothText;
    }

    private static String buildText(ArrayList<Pair> positions, String text) {
        StringBuilder rebuildText = new StringBuilder();
        if (!positions.isEmpty()) {
            for (int i = 0; i < (positions.size()); i++) {
                rebuildText.append(text.substring(positions.get(i)
                  .getFirst(), positions.get(i).getSecond()));
            }
        }

        return rebuildText.toString();

    }

    private static String removeHeader(String text) {
        ArrayList<Pair> positions = new ArrayList<Pair>();
        Pattern pattern = Pattern.compile("\\+\\+[^\\+]*\\+s");
        Matcher matcher = pattern.matcher(text);
        StringBuilder header = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            header.append(matcher.group());
            positions.add(new Pair(lastEnd, matcher.start()));
            lastEnd = matcher.end() + 1;
        }
        positions.add(new Pair(lastEnd, text.length() - 1));

        return buildText(positions, text);
    }


}