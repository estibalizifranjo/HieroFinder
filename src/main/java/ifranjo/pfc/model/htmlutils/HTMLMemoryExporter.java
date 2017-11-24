package ifranjo.pfc.model.htmlutils;

import jsesh.mdc.MDCParserModelGenerator;
import jsesh.mdc.MDCSyntaxError;
import jsesh.mdc.model.*;
import jsesh.mdcDisplayer.draw.ViewDrawer;
import jsesh.mdcDisplayer.layout.SimpleViewBuilder;
import jsesh.mdcDisplayer.mdcView.MDCView;
import jsesh.mdcDisplayer.preferences.DrawingSpecification;
import jsesh.mdcDisplayer.preferences.DrawingSpecificationsImplementation;
import jsesh.mdcDisplayer.preferences.PageLayout;
import jsesh.swing.utils.GraphicsUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is _based_ on jsesh.graphics.export.HTMLExporter
 *
 * @author estibaliz.ifranjo
 */
public class HTMLMemoryExporter {
    private StringBuilder htmlText = new StringBuilder();
    private StringBuilder htmlHeader = new StringBuilder();
    private HashMap<String, Base64> base64 = new HashMap<String, Base64>();
    private int cont = 0;

    private ArrayList<String>  latinText =  new ArrayList<String>();
    private ArrayList<String> hieroTokens = new ArrayList<String>();

    private int match = 0;
    private Long idCount = new Long(0);

    private String title;

    private Color backgroundColor = new Color(0, 0, 0, 0);

    private String latinColor = "#87CEEB";
    private String hieroColor = "#FFFF00";
    private Boolean disableHieroHighlight;
    private Boolean disableLatinHighlight;
    private ArrayList<Long> idsToHighlight = new ArrayList<Long>();
    private Boolean approximatedHieroSearch;
    private Boolean approximatedLatinSearch;

    private int lineHeight;

    /**
     * Vertical margin to draw above and below pictures.
     */
    private int pictureMargin;

    /**
     * Chooses the way special HTML characters are dealt with.
     * <p>
     * If true, replace all &gt; &lt; and &amp; by &amp;gt: &amp;lt; and
     * &amp;amp; (for people looking directly at this comment : protects special
     * HTML characters). This is usefull mainly if you are writing a
     * documentation about the "manuel" de codage. If set
     */
    private boolean htmlSpecialProtected;

    /**
     * if newLineReplacement is NEW_LINE, -! will be replaced by simple new
     * lines in html
     */
    public static final int SPACE = 0;

    /**
     * if newLineReplacement is BREAK, -! will be replaced by &lt;BR&gt;
     */
    public static final int BREAK = 1;

    /**
     * if newLineReplacement is PARAGRAPH, -! will be replaced by &lt;P&gt;
     */
    public static final int PARAGRAPH = 2;

    /**
     * One of SPACE, BREAK, PARAGRAPH
     */
    private int newLinesReplacement;

    private DrawingSpecification drawingSpecifications;

    public HTMLMemoryExporter(String fileName, ArrayList<String> queriesToHighlight,
                              ArrayList<String> searchFields, Boolean disableHieroHighlight,
                              Boolean disableLatinHighlight, Boolean approximatedHieroSearch, Boolean
                                      approximatedLatinSearch) throws IOException, MDCSyntaxError {
        setDefaults();
        setTitle(fileName);
        if ((queriesToHighlight.size() == 1)
                && (searchFields.get(0).equalsIgnoreCase("latinText"))) {
            this.latinText = splitLatinTextToHighlight(queriesToHighlight.get(0));
        } else if ((queriesToHighlight.size() == 1)
                && (searchFields.get(0).equalsIgnoreCase("hieroText"))) {
            this.hieroTokens = splitHieroTextToHighlight(queriesToHighlight.get(0));
        } else if (queriesToHighlight.size() == 2) {
            this.latinText = splitLatinTextToHighlight(queriesToHighlight.get(0));
            this.hieroTokens = splitHieroTextToHighlight(queriesToHighlight.get(1));
        }

        this.disableHieroHighlight = disableHieroHighlight;
        this.disableLatinHighlight = disableLatinHighlight;
        this.approximatedHieroSearch = approximatedHieroSearch;
        this.approximatedLatinSearch = approximatedLatinSearch;
    }

    public ArrayList<String> splitHieroTextToHighlight(String hieroTextToHighlight) throws MDCSyntaxError {
        ArrayList<String> splitedHieroText = new ArrayList<String>();

        MDCParserModelGenerator parser = new MDCParserModelGenerator();
        TopItemList itemList = parser.parse(hieroTextToHighlight);

        for (int i = 0; i < itemList.getNumberOfChildren(); i++) {
            if (!itemList.getChildAt(i).buildTopItem().toMdC().startsWith("<")) {
                splitedHieroText.addAll(Arrays.asList(itemList.getChildAt(i).buildTopItem().toMdC().split("-")));
            } else {
                splitedHieroText.add(itemList.getChildAt(i).buildTopItem().toMdC());

            }
        }

        return splitedHieroText;
    }

    public ArrayList<String> splitLatinTextToHighlight (String latinText) {
        return new ArrayList<String>(Arrays.asList(latinText.split(" ")));
    }

    public void setDefaults() {
        lineHeight = 30;
        pictureMargin = 0;
        htmlSpecialProtected = true;
        newLinesReplacement = PARAGRAPH;

        setDrawingSpecifications(new DrawingSpecificationsImplementation());
    }

    /**
     * @param drawingSpecifications The drawingSpecifications to set.
     */
    public void setDrawingSpecifications(
            DrawingSpecification drawingSpecifications) {
        this.drawingSpecifications = drawingSpecifications.copy();
        PageLayout pageLayout = this.drawingSpecifications.getPageLayout();
        pageLayout.setTopMargin(0);
        pageLayout.setLeftMargin(0);
        this.drawingSpecifications.setPageLayout(pageLayout);
    }

    /**
     * @return Returns the drawingSpecifications.
     */
    public DrawingSpecification getDrawingSpecifications() {
        return drawingSpecifications;
    }

    public StringBuilder exportModel(TopItemList model) {
        HTMLExporterAux visitor = new HTMLExporterAux();
        model.accept(visitor);
        return htmlText;
    }

    private class HTMLExporterAux extends ModelElementAdapter {

        ArrayList elements;

        /*
         * (non-Javadoc)
         *
         * @see
         * jsesh.mdc.model.ModelElementAdapter#visitTopItemList(jsesh.mdc.model
         * .TopItemList)
         */
        public void visitTopItemList(TopItemList t) {
            try {
                int i = 0;
                elements = null;
                startPage();
                while (i < t.getNumberOfChildren()) {
                    t.getChildAt(i).accept(this);
                    i++;
                }
                closePage(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * jsesh.mdc.model.ModelElementAdapter#visitAlphabeticText(jsesh.mdc
         * .model.AlphabeticText)
         */
        public void visitAlphabeticText(AlphabeticText t) {
            try {
                flushElements();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            switch (t.getScriptCode()) {
                case 'b':
                    htmlText.append("<b>");
                    break;
                case 'i':
                    htmlText.append("<i>");
                    break;
                case 't':
                    htmlText.append("<font face=\"MDCTranslitLC,TransliterationItalic\">"); //$NON-NLS-1$
                    break;
                case '+':
                    htmlText.append("<!--"); //$NON-NLS-1$
            }
            if (htmlSpecialProtected) {
                htmlText.append(highlightText(
                        t.getText().replaceAll("&", "&amp;") //$NON-NLS-1$ //$NON-NLS-2$
                                .replaceAll("<", "&lt;").replaceAll(">", "&gt;"), disableLatinHighlight)); //$NON-NLS-1$
                // $NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            } else
                htmlText.append(highlightText(t.getText(), disableLatinHighlight));
            switch (t.getScriptCode()) {
                case 'b':
                    htmlText.append("</b>"); //$NON-NLS-1$
                    break;
                case 'i':
                    htmlText.append("</i>"); //$NON-NLS-1$
                    break;
                case 't':
                    htmlText.append("</font>"); //$NON-NLS-1$
                    break;
                case '+':
                    htmlText.append("-->"); //$NON-NLS-1$
                    break;
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * jsesh.mdc.model.ModelElementAdapter#visitPageBreak(jsesh.mdc.model
         * .PageBreak)
         */
        public void visitPageBreak(PageBreak b) {
            try {
                flushElements();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            htmlText.append("<br/>\n<hrule/>\n"); //$NON-NLS-1$
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * jsesh.mdc.model.ModelElementAdapter#visitLineBreak(jsesh.mdc.model
         * .LineBreak)
         */
        public void visitLineBreak(LineBreak b) {
            try {
                flushElements();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            switch (newLinesReplacement) {
                case SPACE:
                    htmlText.append(""); //$NON-NLS-1$
                    break;
                case BREAK:
                    htmlText.append("<br/>"); //$NON-NLS-1$
                    break;
                case PARAGRAPH:
                    htmlText.append("<p>"); //$NON-NLS-1$
                    break;
            }

        }

        /*
         * (non-Javadoc)
         *
         * @see
         * jsesh.mdc.model.ModelElementAdapter#visitDefault(jsesh.mdc.model.
         * ModelElement)
         */
        public void visitDefault(ModelElement t) {
            getElements().add(t);
            try {
                flushElements();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        private java.util.List getElements() {
            if (elements == null)
                elements = new ArrayList();
            return elements;
        }

        /**
         * Called when an image should be written.
         *
         * @throws IOException
         */
        private void flushElements() throws IOException {
            if (elements != null) {
                TopItemList smallModel = new TopItemList();
                smallModel.addAll(elements);
                String imgClass = "";

                if (!base64.containsKey(smallModel.toMdC())) {
                    SimpleViewBuilder builder = new SimpleViewBuilder();

                    // scale Compute

                    double scale = (double) lineHeight
                            / drawingSpecifications.getMaxCadratHeight();

                    MDCView view = builder.buildView(smallModel,
                            drawingSpecifications);

                    if (view.getWidth() == 0 || view.getHeight() == 0)
                        return;
                    ViewDrawer drawer = new ViewDrawer();

                    BufferedImage image = new BufferedImage(
                            (int) Math.ceil(view.getWidth() * scale + 1),
                            (int) Math.ceil(view.getHeight() * scale + 2
                                    * pictureMargin + 1),
                            BufferedImage.TYPE_INT_ARGB);

                    Graphics2D g = image.createGraphics();
                    GraphicsUtils.antialias(g);

                    g.setColor(backgroundColor);
                    g.fillRect(0, 0, image.getWidth(), image.getHeight());
                    g.setColor(drawingSpecifications.getBlackColor());
                    g.translate(1, 1 + pictureMargin);

                    g.scale(scale, scale);
                    drawer.draw(g, view, drawingSpecifications);
                    g.dispose();

                    imgClass = "img" + cont;
                    Base64 b64 = new Base64(htmlTob64(image), imgClass);

                    base64.put(smallModel.toMdC(), b64);

                    createClassCSS(b64, smallModel.toMdC());
                    cont++;
                    image.flush();

                } else {
                    imgClass = base64.get(smallModel.toMdC()).getclassID();
                }
                if (!hieroTokens.isEmpty()) {
                    if (approximatedHieroSearch) {
                        if (smallModel.toMdC().contains(hieroTokens.get(match))) {
                            if ((match < hieroTokens.size() - 1)) {
                                match++;
                            } else {
                                match = 0;
                            }
                            idsToHighlight.add(idCount);
                        } else if ((smallModel.toMdC().contains(
                                hieroTokens.get(match)))
                                && (match != 0)) {
                            while (match != 0) {
                                idsToHighlight.remove(idsToHighlight.size() - 1);
                                match--;
                            }
                            if (smallModel.toMdC().contains(hieroTokens.get(0))) {
                                match = 1;
                                idsToHighlight.add(idCount);
                            }
                        }
                    } else if (smallModel.toMdC().compareTo(hieroTokens.get(match)) == 0) {
                        if ((match < hieroTokens.size() - 1)) {
                            match++;
                        } else {
                            match = 0;
                        }
                        idsToHighlight.add(idCount);
                    } else if ((smallModel.toMdC().compareTo(
                            hieroTokens.get(match)) != 0)
                            && (match != 0)) {
                        while (match != 0) {
                            idsToHighlight.remove(idsToHighlight.size() - 1);
                            match--;
                        }
                        if (smallModel.toMdC().compareTo(hieroTokens.get(0)) == 0) {
                            match = 1;
                            idsToHighlight.add(idCount);
                        }
                    }
                }

                htmlText.append("<img id=\"i" + idCount + "\" class=\""
                        + imgClass + "\"/>");
                idCount++;

                elements = null;
            }
        }

        @SuppressWarnings("restriction")
        private String htmlTob64(BufferedImage image) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageInByteArray = baos.toByteArray();
            baos.close();
            return javax.xml.bind.DatatypeConverter
                    .printBase64Binary(imageInByteArray);
        }

        private String highlightText(String s, Boolean disableLatinHighlight) {
            String span;
            String result = "";

            for (String latinToken : latinText) {
                if (!disableLatinHighlight && !latinText.isEmpty()) {
                    Pattern pattern = Pattern.compile(latinToken.toLowerCase());
                    Matcher matcher = pattern.matcher(s.toLowerCase());
                    while (matcher.find()) {

                        span = "<span style=\"background-color:" + latinColor + "\"> "
                                + s.substring(matcher.start(), matcher.end()) + "</span>";
                        result = s.replaceAll(s.substring(matcher.start(), matcher.end()), span);
                        if (!result.isEmpty()) {
                            s = result;
                        }
                    }
                    if (result.isEmpty()) {
                        result = s;
                    }
                }
            }
            if (latinText.isEmpty()) {
                result = s;
            }
            return result;
        }


        private void startPage() throws IOException {
            starthtmlHeader();
        }

        private void closePage(boolean hasNext) {
            endHtlmHeader();
            htmlFooter(hasNext);
        }

        private void starthtmlHeader() {
            try {
                flushElements();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            htmlHeader
                    .append("<!DOCTYPE html> <meta charset=\"utf-8\">\n <html>\n <head>\n <title>"
                            + title + "</title>\n ");

            if (title != null)
                htmlText.append("<h1>" + title + "</h1>");

            htmlHeader.append("<style> img { text-align:center; }");
        }

        private void endHtlmHeader() {
            if (!disableHieroHighlight) {
                generateHighlights();
            }
            htmlHeader.append(("</style> </head> <body>"));
            htmlText.insert(0, htmlHeader);
        }

        private void htmlFooter(boolean hasNext) {
            htmlText.append("</body> </html>");
        }

        private void createClassCSS(Base64 b64, String mdcCode)
                throws IOException {
            // <style type=\"text/css\"> img.text {width: 0px; height: 0px;
            // content: url(data:image/png;base64, base64churro )}


            htmlHeader.append(".");
            htmlHeader.append(b64.getclassID());
            htmlHeader.append("{content:url(data:image/png;base64,");
            htmlHeader.append(b64.getB64());
            htmlHeader.append(")}\n");
        }

        private void generateHighlights() {
            for (Long id : idsToHighlight) {
                htmlHeader.append("#i");
                htmlHeader.append(id);
                htmlHeader.append(" {background-color:");
                htmlHeader.append(hieroColor);
                htmlHeader.append(";}\n");
            }
        }
    }

    private class Base64 {
        String b64;
        String classID;

        public Base64(String b64, String classID) {
            this.b64 = b64;
            this.classID = classID;
        }

        public String getB64() {
            return b64;
        }

        public String getclassID() {
            return classID;
        }

    }


    /**
     * @return the page title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param string
     */
    public void setTitle(String string) {
        title = string;
    }

    /**
     * The typical height of a line of text, in pixels.
     *
     * @return The typical height of a line of text, in pixels.
     */
    public int getLineHeight() {
        return lineHeight;
    }

    /**
     * sets the typical height of a line of text, in pixels.
     *
     * @param i
     */
    public void setLineHeight(int i) {
        lineHeight = i;
    }

    /**
     * Returns the vertical margin to draw above and below pictures.
     *
     * @return Returns the vertical margin to draw above and below pictures.
     */
    public int getPictureMargin() {
        return pictureMargin;
    }

    /**
     * Sets the vertical margin to draw above and below pictures.
     *
     * @param pictureMargin a margin, in pixels, to draw above and below pictures.
     */
    public void setPictureMargin(int pictureMargin) {
        this.pictureMargin = pictureMargin;
    }

    public void setLatinColor(String latinColor) {
        this.latinColor = latinColor;
    }

    public void setHieroColor(String hieroColor) {
        this.hieroColor = hieroColor;
    }
}
