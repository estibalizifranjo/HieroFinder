/**
 *
 */
package es.udc.pfc.model.texts;

import es.udc.pfc.model.utils.Pair;

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

        Pattern pattern = Pattern.compile("\\+[bli][^\\+]*\\+s[\\-!]*");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            latinText.append(matcher.group());
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
        Pattern pattern = Pattern.compile("\\+[bli][^\\+]*\\+s[\\-!]*");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            latinText.append(matcher.group());
            positions.add(new Pair(lastEnd, matcher.start()));
            lastEnd = matcher.end() + 1;
        }
        positions.add(new Pair(lastEnd, text.length()));

        String[] bothText = {latinText.toString(),
                buildText(positions, text).toString()};
        // HieroAnalyzer analyzer = new HieroAnalyzer();
        // TokenStream stream = analyzer.tokenStream("hieroText", bothText[1]);
        // CharTermAttribute termAtt =
        // stream.addAttribute(CharTermAttribute.class);
        //
        // try {
        // stream.reset();
        //
        // // print all tokens until stream is exhausted
        // while (stream.incrementToken()) {
        // System.out.println(termAtt.toString());
        // }
        //
        // stream.end();
        // } finally {
        // stream.close();
        // }

        // System.out.println("LATIN TEXT " + latinText.toString());
        // System.out.println("HIERO TEXT " + bothText[1]);
        return bothText;
    }

    private static String buildText(ArrayList<Pair> positions, String text) {
        StringBuilder rebuildText = new StringBuilder();
        if (!positions.isEmpty()) {
            for (int i = 0; i < (positions.size()); i++) {
                rebuildText.append(text.substring((Integer) positions.get(i)
                        .getFirst(), (Integer) positions.get(i).getSecond()));
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

    // TODO Eliminar headers de los textos (empiezan por ++ y terminan con +s)

}