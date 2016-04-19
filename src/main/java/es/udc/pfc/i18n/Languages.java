package es.udc.pfc.i18n;

/**
 * Created by estibaliz.ifranjo on 7/31/15.
 */
public class Languages {

    public static String getLocale(String language) {
        String locale;
        switch (language) {
            case "English":
                locale = "EN";
                break;
            case "Español":
                locale = "ES";
                break;
            case "French":
                locale = "FR";
                break;
            case "Galego":
                locale = "GL";
                break;
            default:
                locale = "EN";
        }
        return locale;
    }

}
